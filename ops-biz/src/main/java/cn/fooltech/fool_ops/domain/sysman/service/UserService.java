package cn.fooltech.fool_ops.domain.sysman.service;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.component.core.filesystem.FileSystem;
import cn.fooltech.fool_ops.component.redis.BaseDataCache;
import cn.fooltech.fool_ops.component.redis.RedisService;
import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.BaseConstant;
import cn.fooltech.fool_ops.domain.common.entity.Attach;
import cn.fooltech.fool_ops.domain.common.service.AttachService;
import cn.fooltech.fool_ops.domain.common.vo.AttachVo;
import cn.fooltech.fool_ops.domain.flow.entity.SecurityLevel;
import cn.fooltech.fool_ops.domain.flow.service.SecurityLevelService;
import cn.fooltech.fool_ops.domain.sysman.entity.*;
import cn.fooltech.fool_ops.domain.sysman.entity.Resource;
import cn.fooltech.fool_ops.domain.sysman.repository.*;
import cn.fooltech.fool_ops.domain.sysman.vo.UserVo;
import cn.fooltech.fool_ops.utils.MD5Util;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.*;
import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <p>用户网页服务类</p>
 *
 * @author xjh
 * @version 2.0
 * @date 2016年10月18日
 */
@Service("ops.UserService")
public class UserService extends BaseService<User, UserVo, String> implements BaseDataCache {
    private final Logger log = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepo;

    /**
     * 部门服务类
     */
    @Autowired
    private OrganizationRepository orgRepo;

    /**
     * 部门服务类
     */
    @Autowired
    private OrgService orgService;

    /**
     * 用户属性服务类
     */
    @Autowired
    private UserAttrRepository userAttrRepo;

    @Autowired
    private UserAttrService userAttrService;

    @Autowired
    private FileSystem fileSystem;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private RoleAuthRepository roleAuthRepo;

    @Autowired
    private UserAuthRepository userAuthRepo;

    @Autowired
    private ResourceRepository resourceRepo;

    @Autowired
    private AttachService attachService;

    @Autowired
    private SecurityLevelService securityLevelService;

    @Autowired
    private UserSecurityLevelService userSecurityLevelService;
    @Autowired
    private RedisService redisService;

    /**
     * 查询用户信息
     *
     * @return
     */
    public UserVo getUser() {
        String userId = SecurityUtil.getCurrentUserId();
        User entity = userRepo.findOne(userId);
        return getVo(entity);
    }

    /**
     * 修改用户信息
     *
     * @param vo
     * @return
     */
    @Transactional
    public RequestResult save(UserVo vo) {
        String userName = vo.getUserName();
        String address = vo.getFaddress();
        Short sex = vo.getSex();
        String phone = vo.getPhoneOne();
        String email = vo.getEmail();
        String oldPsw = vo.getOldPsw();
        String newPsw = vo.getNewPsw();
        String fid = vo.getFid();
        String desc = vo.getUserDesc();


        String fax = vo.getFax();
        Short fisinterface = vo.getFisinterface();
        Short isMobileLogin = vo.getIsMobileLogin();
        String postCode = vo.getPostCode();
        String idCard = vo.getIdCard();
        Integer validPrice = vo.getValidPrice();    //有效报价:0--否，1--是

        User entity = userRepo.findOne(fid);

        if (isPhoneExists(SecurityUtil.getCurrentOrgId(), phone, fid)) {
            return new RequestResult(RequestResult.RETURN_FAILURE, "手机号码已存在!");
        }
        String userCode = vo.getUserCode();
        if (StringUtils.isNotBlank(userCode)) {
            if (isCodeExists(userCode, vo.getFid())) {
                return new RequestResult(RequestResult.RETURN_FAILURE, "登录账号已存在");
            }
            entity.setUserCode(userCode);
        }
        if (StringUtils.isNotBlank(newPsw)) {
            oldPsw = MD5Util.encrypt(oldPsw);
            newPsw = MD5Util.encrypt(newPsw);
            if (!oldPsw.equalsIgnoreCase(entity.getPassWord())) {
                return new RequestResult(RequestResult.RETURN_FAILURE, "原始密码错误!");
            }
            entity.setPassWord(newPsw);
        }
        entity.setUserName(userName);
        entity.setFaddress(address);
        entity.setSex(sex);
        entity.setPhoneOne(phone);
        entity.setEmail(email);
        entity.setTopOrg(SecurityUtil.getCurrentOrg());
        entity.setValidPrice(vo.getValidPrice());
        entity.setUserDesc(desc);

        entity.setFax(fax);
        entity.setIsMobileLogin(isMobileLogin);
        entity.setValidPrice(validPrice);

        if (fisinterface != null) {
            entity.setFisinterface(fisinterface);
        }
        if (postCode != null) {
            entity.setPostCode(postCode);
        }
        if (idCard != null) {
            entity.setIdCard(idCard);
        }
        String securityLevelId = vo.getSecurityLevelId(); //保密级别ID
        userRepo.save(entity);
        updateSecurityLevel(entity, securityLevelId);
        updateInputType(fid, vo.getInputType());
        updateLocalCache(fid, vo.getLocalCache());

        redisService.remove(getCacheKey());

        return new RequestResult();
    }


    /**
     * 修改用户的默认输入方式
     *
     * @param userId
     * @param inputType
     */
    private void updateInputType(final String userId, final String inputType) {

        if (Strings.isNullOrEmpty(inputType)) return;

        List<UserAttr> userAttrs = userAttrService.queryByIdAndKey(userId, UserAttr.INPUT_TYPE);
        for (UserAttr temp : userAttrs) {
            userAttrService.delete(temp);
        }
        UserAttr newData = new UserAttr();
        newData.setKey(UserAttr.INPUT_TYPE);
        newData.setValue(inputType);
        newData.setUserID(userId);
        userAttrService.save(newData);


    }

    /**
     * 修改用户的默认输入方式
     *
     * @param userId
     * @param enable
     */
    private void updateLocalCache(final String userId, String enable) {

        if (Strings.isNullOrEmpty(enable)) return;

        List<UserAttr> userAttrs = userAttrService.queryByIdAndKey(userId, UserAttr.LOCAL_CACHE);
        for (UserAttr temp : userAttrs) {
            userAttrService.delete(temp);
        }
        UserAttr newData = new UserAttr();
        newData.setKey(UserAttr.LOCAL_CACHE);
        newData.setValue(enable);
        newData.setUserID(userId);
        userAttrService.save(newData);
    }

    /**
     * 根据部门查找用户列表
     * @param deptId 部门ID
     * @return
     */
    /*public List<CommonTreeVo> queryTreeByDept(String deptId){
			List<User> users = getByDept(deptId);
			List<CommonTreeVo> treeList = new ArrayList<CommonTreeVo>();
			CommonTreeVo treeVo = null;
			for (User user : users) {
				treeVo = new CommonTreeVo();
				treeVo.setId(user.getFid());
				treeVo.setText(user.getUserName());
				treeVo.setState("open");
				treeList.add(treeVo);
			}
			return treeList;
		}*/

    /**
     * 用户信息管理-用户列表<br>
     * 当orgId为空时，查询整个机构的用户，不为空时，查询某个部门的用户<br>
     *
     * @param orgId     部门ID
     * @param paramater
     * @return
     */
    public Page<UserVo> queryByDept(String orgId, PageParamater paramater, int subDept) {

        PageRequest request = getPageRequest(paramater);

        Page<User> page = null;
        if (StringUtils.isBlank(orgId)) {
            String topOrgId = SecurityUtil.getCurrentOrgId();
            page = userRepo.findPageByTopOrgId(topOrgId, request);
        } else {
            if (subDept == 1) {
                Organization org = orgRepo.findOne(orgId);
                List<Organization> subDepts = orgService.getAllChildren(org);
                subDepts.add(org);
                page = userRepo.findPageByDepts(subDepts, request);
            } else {
                page = userRepo.findPageByDeptId(orgId, request);
            }
        }
        return getPageVos(page, request);
    }

    /**
     * 按照部门查询用户列表信息，按照用户编号降序排列<br>
     * 帮助类
     * @param deptId
     * @param pageNo
     * @param pageSize
     * @author tjr 2015-06-16
     * @return
     */
	/*private Object queryByDeptHelp(String deptId, int pageNo, int pageSize){
			List<String> whereClause = new ArrayList<String>();
			whereClause.add("orgId.fid = " + deptId);

			Condition cond = new Condition();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("__whereClause", StringUtils.join(whereClause, " and "));
			parameters.put("__sortClause", "fid_true");

			if(pageNo==0&&pageSize==0){
				List list =	userService.queryAllByCondition(cond, parameters);
				return list;
			}else{
				Page<User> page = userService.queryPageByCondition(cond, parameters);
				return page;
			}
		}*/

    /**
     * 更新用户的保密级别
     *
     * @param entity
     * @param securityLevelId 保密级别ID
     */
    public void updateSecurityLevel(User entity, String securityLevelId) {
        if (StringUtils.isNotBlank(securityLevelId)) {
            SecurityLevel securityLevel = securityLevelService.get(securityLevelId);
            UserSecurityLevel userSecurityLevel = userSecurityLevelService.getByUserId(entity.getFid());
            if (userSecurityLevel == null) {
                userSecurityLevel = new UserSecurityLevel(entity, securityLevel);
            } else {
                userSecurityLevel.setSecurityLevel(securityLevel);
            }
            userSecurityLevelService.save(userSecurityLevel);
        }else{
        	UserSecurityLevel userSecurityLevel = userSecurityLevelService.getByUserId(entity.getFid());
        	if (userSecurityLevel != null) {
        		userSecurityLevelService.delete(userSecurityLevel);
            }
        	
        }
    }

    /**
     * 用户信息管理-新增/保存用户信息
     *
     * @param vo
     * @return
     */
    public RequestResult create(UserVo vo) {
        String fid = vo.getFid();
        String userName = vo.getUserName();
        String address = vo.getFaddress();
        Short sex = vo.getSex();
        String phone = vo.getPhoneOne();            //手机号
        String email = vo.getEmail();
        String fax = vo.getFax();
        Short fisinterface = vo.getFisinterface();
        Short isMobileLogin = vo.getIsMobileLogin();
        String postCode = vo.getPostCode();
        String idCard = vo.getIdCard();
        String deptId = vo.getOrgId(); //用户所属部门ID
        String securityLevelId = vo.getSecurityLevelId(); //保密级别ID
        Integer validPrice = vo.getValidPrice();    //有效报价:0--否，1--是

        String orgId = SecurityUtil.getCurrentOrgId();

        if (isPhoneExists(orgId, phone, fid)) {
            return new RequestResult(RequestResult.RETURN_FAILURE, "手机号码已存在!");
        }

        User entity = null;
        if (StringUtils.isNotBlank(fid)) {
            entity = userRepo.findOne(fid);
        } else {
            entity = new User();
            entity.setPassWord(MD5Util.encrypt(Constants.NEW_USER_DEFAULT_PASSWORD));
            entity.setIsAdmin((short) 0);
            entity.setValidation((short) 1);

            if (!Strings.isNullOrEmpty(deptId)) {
                Organization dept = orgService.findOne(deptId);
                entity.setOrgId(dept);
            }
        }

        entity.setUserName(userName);
        entity.setPhoneOne(phone);
        entity.setEmail(email);
        entity.setFax(fax);
        entity.setIsMobileLogin(isMobileLogin);
        entity.setValidPrice(validPrice);
        if (address != null) {
            entity.setFaddress(address);
        }
        if (sex != null) {
            entity.setSex(sex);
        }
        if (fisinterface != null) {
            entity.setFisinterface(fisinterface);
        }
        if (postCode != null) {
            entity.setPostCode(postCode);
        }
        if (idCard != null) {
            entity.setIdCard(idCard);
        }
        String userCode = vo.getUserCode();
        if (StringUtils.isNotBlank(userCode)) {
            if (isCodeExists(userCode, vo.getFid())) {
                return new RequestResult(RequestResult.RETURN_FAILURE, "登录账号已存在");
            }
            entity.setUserCode(userCode);
        }
        entity.setTopOrg(SecurityUtil.getCurrentOrg());
        userRepo.save(entity);

        updateSecurityLevel(entity, securityLevelId);
        updateInputType(entity.getFid(), vo.getInputType());

        return new RequestResult();
    }

    /**
     * 用户信息管理-刪除用户信息
     *
     * @param vo
     * @return
     */
    public RequestResult delete(UserVo vo) {
        User entity = userRepo.findOne(vo.getFid());
        entity.setValidation((short) 0);
        userRepo.save(entity);

        redisService.remove(getCacheKey());
        return buildSuccessRequestResult();
    }


    /**
     * 判断用户的手机号码是否已存在
     *
     * @param orgId     机构ID
     * @param phone     手机号码
     * @param excludeId 排除实体的ID
     * @return
     */
    public boolean isPhoneExists(String orgId, String phone, String excludeId) {

        Long count = 0L;
        if (StringUtils.isNotBlank(excludeId)) {
            count = userRepo.countByPhone(orgId, phone, excludeId);
        } else {
            count = userRepo.countByPhone(orgId, phone);
        }
        if (count != null && count > 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断用户的编号是否已存在
     *
     * @param userCode
     * @param excludeId 排除实体的ID
     * @return
     */
    public boolean isCodeExists(String userCode, String excludeId) {

        Long count = 0L;
        if (StringUtils.isNotBlank(excludeId)) {
            count = userRepo.countByUserCode(userCode, excludeId);
        } else {
            count = userRepo.countByUserCode(userCode);
        }
        if (count != null && count > 0) {
            return true;
        }
        return false;
    }


    /**
     * 获取某个部门的用户(分页)
     *
     * @param deptId     部门ID
     * @param start
     * @param maxResults
     * @return
     */
    private Page<User> queryPageByDept(String deptId, int start, int maxResults, int subDept) {

        PageRequest pageRequest = new PageRequest(start, maxResults, Direction.ASC, "userCode");
        Criterion c = null;
        if (subDept == 1) {
            Organization dept = orgRepo.findOne(deptId);
            List<Organization> subDepts = Lists.newArrayList(dept.getChilds());
            subDepts.add(dept);
            return userRepo.findPageByDepts(subDepts, pageRequest);
        } else {
            return userRepo.findPageByDeptId(deptId, pageRequest);
        }
    }

    /**
     * 更新头像
     *
     * @param userId
     * @param headPortrait
     */
    public void updateHeadPortrait(String userId, String headPortrait) {
        User user = userRepo.findOne(userId);
        user.setHeadPortRait(headPortrait);
        userRepo.save(user);
    }

    /**
     * 模糊搜索
     *
     * @param vo
     * @return
     */
    public List<UserVo> vagueSearch(UserVo vo) {
        String orgId = SecurityUtil.getCurrentOrgId();
        String searchKey = vo.getSearchKey();
        Integer limit = vo.getSearchSize();

        List<User> users = userRepo.findBySearchKey(orgId, searchKey, limit);
        List<UserVo> userVos = Lists.newArrayList();

        for(User user:users){
            userVos.add(getFastVo(user));
        }

        return userVos;
    }

    /**
     * 把资源授权给角色
     *
     * @param check
     * @param resId
     * @param roleId
     * @param subRes
     * @return
     */
    @Transactional(readOnly = false)
    public RequestResult saveFunctionRole(String check, String resId, String roleId, Integer subRes) {
        Role role = roleRepo.findOne(roleId);

        Resource rootRes = resourceRepo.findOne(resId);
        while (rootRes.getParent() != null) {
            rootRes = rootRes.getParent();
        }
        List<Resource> lstr = resourceRepo.getChildResources(resId);
        List<RoleAuth> lst = roleAuthRepo.findByRoleAndResources(role, lstr);
        roleAuthRepo.delete(lst);

        if (!Strings.isNullOrEmpty(check)) {
            JSONArray array = JSONArray.fromObject(check);
            for (int i = 0; i < array.size(); i++) {
                JSONObject json = JSONObject.fromObject(array.get(i));
                String fid = json.getString("fid");
                Integer dateFilter = json.getInt("dateFilter");

                Resource resource = resourceRepo.findOne(fid);
                if (resource != null) {
                    RoleAuth roleAuth = new RoleAuth();
                    roleAuth.setRole(role);
                    roleAuth.setResource(resource);
                    roleAuth.setDataFilter(dateFilter);
                    roleAuthRepo.save(roleAuth);

                    if (subRes == 1) {
                        saveRecurseFunctionRole(fid, role, dateFilter);
                    }
                }
            }
        }

        //看看根节点是否已授权，未授权则授权
        List<Resource> rootList = Lists.newArrayList(rootRes);
        List<RoleAuth> rootResList = roleAuthRepo.findByRoleAndResources(role, rootList);
        if (rootResList == null || rootResList.size() == 0) {
            RoleAuth roleAuth = new RoleAuth();
            roleAuth.setRole(role);
            roleAuth.setResource(rootRes);
            roleAuthRepo.save(roleAuth);
        }

        return buildSuccessRequestResult();
    }

    /**
     * 递归把子资源授权给角色
     *
     * @param parentResId
     * @param role
     * @param dateFilter
     */
    @Transactional(readOnly = false)
    public void saveRecurseFunctionRole(String parentResId, Role role, Integer dateFilter) {
        List<Resource> childs = resourceRepo.getChildResources(parentResId);

        if (childs.size() == 0) return;
        List<RoleAuth> lst = roleAuthRepo.findByRoleAndResources(role, childs);
        roleAuthRepo.delete(lst);

        for (Resource res : childs) {
            RoleAuth roleAuth = new RoleAuth();
            roleAuth.setRole(role);
            roleAuth.setResource(res);
            roleAuth.setDataFilter(dateFilter);
            roleAuthRepo.save(roleAuth);

            saveRecurseFunctionRole(res.getFid(), role, dateFilter);
        }
    }

    /**
     * entity转换VO 不提取不需要的属性
     * @param entity
     * @return
     */
    public UserVo getFastVo(User entity){
        UserVo vo = new UserVo();
        vo.setFid(entity.getFid());
        vo.setUserCode(entity.getUserCode());
        vo.setUserName(entity.getUserName());
        vo.setEmail(entity.getEmail());
        vo.setFaddress(entity.getFaddress());
        vo.setSex(entity.getSex());
        vo.setFax(entity.getFax());
        vo.setIsMobileLogin(entity.getIsMobileLogin());
        vo.setFisinterface(entity.getFisinterface());
        vo.setPostCode(entity.getPostCode());
        vo.setIdCard(entity.getIdCard());
        vo.setUserDesc(entity.getUserDesc());
        vo.setPhoneOne(entity.getPhoneOne());
        vo.setValidPrice(entity.getValidPrice());

        Organization dept = entity.getOrgId();
        if(dept!=null){
            vo.setDeptId(dept.getFid());
            vo.setDeptName(dept.getOrgName());
        }
        return vo;
    }

    @Override
    public UserVo getVo(User entity) {
        UserVo vo = new UserVo();
        vo.setFid(entity.getFid());
        vo.setUserCode(entity.getUserCode());
        vo.setUserName(entity.getUserName());
        vo.setEmail(entity.getEmail());
        vo.setFaddress(entity.getFaddress());
        vo.setSex(entity.getSex());
        vo.setFax(entity.getFax());
        vo.setIsMobileLogin(entity.getIsMobileLogin());
        vo.setFisinterface(entity.getFisinterface());
        vo.setPostCode(entity.getPostCode());
        vo.setIdCard(entity.getIdCard());
        vo.setUserDesc(entity.getUserDesc());
        vo.setPhoneOne(entity.getPhoneOne());
        vo.setValidPrice(entity.getValidPrice());

        UserAttr inputTypeAttr = userAttrRepo.findTopBy(entity.getFid(), UserAttr.INPUT_TYPE);
        vo.setInputType(inputTypeAttr == null ? "" : inputTypeAttr.getValue());

        UserAttr localCacheAttr = userAttrRepo.findTopBy(entity.getFid(), UserAttr.LOCAL_CACHE);
        vo.setLocalCache(localCacheAttr == null ? "" : localCacheAttr.getValue());

        //保密级别
        SecurityLevel securityLevel = userSecurityLevelService.getSecurityLevel(entity.getFid());
        if (securityLevel != null) {
            vo.setSecurityLevelId(securityLevel.getFid());
            vo.setSecurityLevelName(securityLevel.getName());
            vo.setSecurityLevel(securityLevel.getLevel());
        }

        Organization dept = entity.getOrgId();
        vo.setDeptId(dept.getFid());
        vo.setDeptName(dept.getOrgName());

        return vo;
    }

    /**
     * 获得用户已授权角色信息
     */
    @Transactional(readOnly = true)
    public List<String> getUserRole(String userId) {
        User entity = userRepo.findOne(userId);
        List<Role> roles = entity.getRoles();
        List<String> ids = new ArrayList<String>();
        for (Role role : roles) {
            String id = role.getFid();
            ids.add(id);
        }
        return ids;
    }

    /**
     * 获取附件
     *
     * @param userId 用户ID
     * @return
     * @author lzf
     */
    public AttachVo getUploadAttach(String userId) {
        //添加附件
        List<AttachVo> vos = attachService.getAttachVosByBusId(userId);
        if (vos != null && !vos.isEmpty()) {
            return vos.get(0);
        }
        return null;
    }

    /**
     * 处理附件实体
     *
     * @param userId 用户ID
     * @param file
     * @return
     */
    @Transactional
    public Attach uploadAttach(String userId, MultipartFile file) throws Exception {
        AttachVo vo = getUploadAttach(userId);
        Attach entity = null;
        if (vo != null) {
            attachService.delete(vo.getFid());
        }

        String root = fileSystem.getRoot();
        String path = root + File.separator + UUID.randomUUID().toString();
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }
        if (entity == null) {
            entity = new Attach();
        }

        entity.setFileName(file.getOriginalFilename());
        entity.setBusId(userId);
        entity.setType(Attach.TYPE_IMAGE);
        entity.setStatus(Attach.STATUS_INSERT);
        entity.setCreator(SecurityUtil.getCurrentUser());
        entity.setCreateTime(new Date());

        attachService.saveMultiPartFile(entity, file);
        attachService.save(entity);
        return entity;
    }

    /**
     * 根据角色获取用户s
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<UserVo> getUserByRole(String roleId) {
        Role role = roleRepo.findOne(roleId);
        List<User> userList = role.getUsers();
        return getVos(userList);
    }

    @Override
    public CrudRepository<User, String> getRepository() {
        return this.userRepo;
    }

    /**
     * 按功能授权
     *
     * @param check
     * @param parent
     * @param userId
     * @return
     */
    @Transactional(readOnly = false)
    public RequestResult functionUser(String check, String parent, String userId) {
        User user = userRepo.findOne(userId);
        String[] checkIds = null;
        if (check != null) {
            check = check + parent;
            checkIds = check.split(",");
        }

        List<UserAuth> lst = userAuthRepo.findByUser_fid(userId);
        for (UserAuth userAuth : lst) {
            userAuthRepo.delete(userAuth);
        }
        if (checkIds != null) {
            for (String rid : checkIds) {
                Resource resource = resourceRepo.findOne(rid);
                if (resource != null && !"".equals(resource.getFid())) {
                    UserAuth userAuth = new UserAuth();
                    userAuth.setUser(user);
                    userAuth.setResource(resource);
                    userAuthRepo.save(userAuth);
                }
            }
        }
        return buildSuccessRequestResult();
    }

    public void changePassword(String password) {

        User user = userRepo.getOne(SecurityUtil.getCurrentUserId());
        if (user != null) {
            String encryptedPassword = MD5Util.encrypt(password);
            user.setPassWord(encryptedPassword);
            userRepo.save(user);
            log.debug("Changed password for User: {}", user);
        }

    }

    @Override
    public String getCacheName() {
        return BaseConstant.USER;
    }
}
