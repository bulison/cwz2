package cn.fooltech.fool_ops.domain.basedata.service;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.component.exception.DataNotExistException;
import cn.fooltech.fool_ops.component.redis.RedisService;
import cn.fooltech.fool_ops.domain.base.entity.StorageBaseEntity;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.BaseConstant;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttrType;
import cn.fooltech.fool_ops.domain.basedata.repository.AuxiliaryAttrRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.AuxiliaryAttrTypeRepository;
import cn.fooltech.fool_ops.domain.basedata.vo.AuxiliaryAttrVo;
import cn.fooltech.fool_ops.domain.common.vo.CommonTreeVo;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.StrUtil;
import cn.fooltech.fool_ops.utils.TreeDataUtil;
import cn.fooltech.fool_ops.utils.input.PinyinUtils;
import cn.fooltech.fool_ops.utils.tree.TreeCallback;
import cn.fooltech.fool_ops.utils.tree.TreeUtils;
import cn.fooltech.fool_ops.utils.tree.TreeVo;
import cn.fooltech.fool_ops.validator.ValidatorUtils;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;


/**
 * <p>辅助属性网页服务类</p>
 *
 * @author cwz
 * @date 2016-10-24
 */
@Service
public class AuxiliaryAttrService extends BaseService<AuxiliaryAttr, AuxiliaryAttrVo, String> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 辅助属性服务类
     */
    @Autowired
    private AuxiliaryAttrRepository attrRepository;

    @Autowired
    private AuxiliaryAttrTypeService attrTypeService;

    /**
     * 辅助属性类型服务类
     */
    @Autowired
    private AuxiliaryAttrTypeRepository attrTypeRepository;

	@Autowired
	private RedisService redisService;

    @Autowired
    private List<AuxiliaryBiz> bizList;


    /**
     * 检查名名称后者编号是否存在
     * @param entity entity
     * @param flag flag
     */
    private RequestResult checkRepeatByCategory(AuxiliaryAttr entity, int flag) {
        Object obj = attrRepository.findRepeatByCategory(entity, flag);
        String msg = "名称已经存在";
        if (flag == 1) msg = "编号已经存在";
        if (obj != null) return buildFailRequestResult(msg);
        return buildSuccessRequestResult();
    }

    /**
     * 根据类别编号查询列表
     */
    public List<AuxiliaryAttrVo> findByCategoryCode(String typeCode){
        String accId = SecurityUtil.getFiscalAccountId();
        String orgId = SecurityUtil.getCurrentOrgId();
        return getVos(attrRepository.findByCategoryCode(typeCode, orgId, accId));
    }

    /**
     * 单个辅助属性实体转换为vo
     *
     * @param entity entity
     */
    @Override
    public AuxiliaryAttrVo getVo(AuxiliaryAttr entity) {
        if (entity == null)
            return null;

        AuxiliaryAttrVo vo = new AuxiliaryAttrVo();
        vo.setFid(entity.getFid());
        vo.setCode(entity.getCode());
        vo.setName(entity.getName());
        vo.setDescribe(entity.getDescribe());
        vo.setCreateTime(entity.getCreateTime());
        vo.setUpdateTime(DateUtilTools.time2String(entity.getUpdateTime()));
        vo.setEnable(entity.getEnable());
        vo.setSystemSign(entity.getSystemSign());
        //运输单位
        if(entity.getScale()!=null){
        	vo.setScale(entity.getScale());
        }
        
        //类别
        AuxiliaryAttrType type = entity.getCategory();
        if (type != null) {
            vo.setCategoryId(type.getFid());
            vo.setCategoryName(type.getName());
        }
        //父节点
        AuxiliaryAttr parent = entity.getParent();
        if (parent != null) {
            vo.setParentId(parent.getFid());
            vo.setParentName(parent.getName());
        }
        //财务账套
        FiscalAccount fiscalAccount = entity.getFiscalAccount();
        if (fiscalAccount != null) {
            vo.setFiscalAccountId(fiscalAccount.getFid());
            vo.setFiscalAccountName(fiscalAccount.getName());
        }
       
        
        return vo;
    }

    /**
     * 删除辅助属性<br>
     */
    public RequestResult delete(String fid) {
        RequestResult result;
        if (StringUtils.isBlank(fid)) {
            result = new RequestResult();
            result.setMessage("辅助属性不存在");
            result.setReturnCode(RequestResult.RETURN_FAILURE);
            return result;
        }
        if (!this.auxiliaryUse(fid)) {
            AuxiliaryAttr attr = attrRepository.findOne(fid);
            String typeCode = attr.getCategory().getCode();
            attrRepository.delete(fid);
            redisService.remove(getCacheKey(typeCode));
            return null;
        } else {
            result = new RequestResult();
            result.setMessage("辅助属性已被使用，不允许删除");
            result.setReturnCode(RequestResult.RETURN_FAILURE);
            return result;
        }
    }

    /**
     * 获取辅助属性信息
     *
     * @param fid 辅助属性ID
     */
    public AuxiliaryAttrVo getByFid(String fid) {
        Assert.notNull(fid);
        AuxiliaryAttrVo attrVo = getById(fid);
        if (attrVo == null) throw new DataNotExistException();
        return attrVo;
    }

    /**
     * 快速新增
     *
     * @param vo add by xjh
     */
    public RequestResult saveFast(AuxiliaryAttrVo vo) {
    	//使用这一步跳过非空验证
    	if(Strings.isNullOrEmpty(vo.getCode())){
    		vo.setCode("跳过非空验证 ");
    	}
        String inValid = ValidatorUtils.inValidMsg(vo);
        if(inValid!=null){
            return buildFailRequestResult(inValid);
        }
        //跳过验证后重新把code的值设为NULL
        if(vo.getCode().equals("跳过非空验证 ")){
        	vo.setCode(null);
        }
        String accId = SecurityUtil.getFiscalAccountId();
        String orgId = SecurityUtil.getCurrentOrgId();
        AuxiliaryAttrType attrType = attrTypeService.findByCode(vo.getCategoryCode(), orgId, accId);
        if (attrType == null) return new RequestResult(RequestResult.RETURN_FAILURE, "找不到对应的类别");

        AuxiliaryAttr entity = new AuxiliaryAttr();
        FiscalAccount fiscalAccount = attrType.getFiscalAccount();

        String pinyin = PinyinUtils.getPinyinCode(vo.getName(), 10);

        entity.setCode(getNotExistCode(pinyin, fiscalAccount, vo.getCategoryCode()));
        entity.setName(vo.getName());
        entity.setCreateTime(Calendar.getInstance().getTime());
        entity.setUpdateTime(Calendar.getInstance().getTime());
        entity.setEnable(AuxiliaryAttr.STATUS_ENABLE);
        entity.setFlag(AuxiliaryAttr.LEAF);
//		entity.setCreator(getCurrentUser());
        entity.setCreator(SecurityUtil.getCurrentUser());
        entity.setOrg(SecurityUtil.getCurrentOrg());
        entity.setCategory(attrType);
        entity.setParentIds("");

        //财务账套
        if (fiscalAccount != null) {
            entity.setFiscalAccount(fiscalAccount);
        }	

        try {
            RequestResult check = checkRepeatByCategory(entity, 0);
            if(!check.isSuccess())return check;
            attrRepository.save(entity);
        } catch (Exception e) {
            return new RequestResult(RequestResult.RETURN_FAILURE, "系统异常");
        }

        redisService.remove(getCacheKey(attrType.getCode()));
        return buildSuccessRequestResult(entity.getFid());
    }

    /**
     * 获得缓存的Key
     */
    protected String getCacheKey(final String categoryCode){

        String key = "";
        key += BaseConstant.getCacheKey(categoryCode);
        key += ":";
        key += SecurityUtil.getCurrentOrgId();
        key += ":";
        key += SecurityUtil.getFiscalAccountId();

        return key;
    }

    /**
     * 获得不存在的编号
     *
     * @param code code
     */
    private String getNotExistCode(String code, FiscalAccount fiscalAccount, String typeCode) {
        String accountId = null;
        if (fiscalAccount != null) accountId = fiscalAccount.getFid();
        String tempCode = code;
        int i = 2;
        do {
            boolean exist = isCodeExist(SecurityUtil.getCurrentOrgId(), accountId, typeCode, tempCode, null, null);
            if (!exist) {
                return tempCode;
            }
            tempCode = code + i;
            i++;
        } while (true);
    }

    /**
     * 新增/编辑辅助属性
     *
     * @param vo vo
     */
    @Transactional
    public RequestResult save(AuxiliaryAttrVo vo) {

        String inValid = ValidatorUtils.inValidMsg(vo);
        if (inValid != null) {
            return buildFailRequestResult(inValid);
        }

        AuxiliaryAttr entity = new AuxiliaryAttr();
        if (StringUtils.isBlank(vo.getFid())) {
            entity.setCode(vo.getCode());
            entity.setName(vo.getName());
            entity.setDescribe(vo.getDescribe());
            entity.setCreateTime(Calendar.getInstance().getTime());
            entity.setUpdateTime(Calendar.getInstance().getTime());
            //entity.setEnable(AuxiliaryAttr.STATUS_ENABLE);
            entity.setEnable(vo.getEnable());

            entity.setFlag((short) 1);
            entity.setCreator(SecurityUtil.getCurrentUser());
            entity.setOrg(SecurityUtil.getCurrentOrg());

//			if(StringUtils.isNotBlank(vo.getParentId())){
//				entity.setParent(attrRepository.get(vo.getParentId()));
//			}

            AuxiliaryAttrType attrType = attrTypeRepository.getOne(vo.getCategoryId());
            if (StrUtil.isSame(vo.getFirst(), "isFirst")) {
                entity.setCategory(attrType);
                entity.setParentIds("");
            } else {
                AuxiliaryAttr parent = attrRepository.getOne(vo.getParentId());
                entity.setParent(parent);
                entity.setCategory(parent.getCategory());
                String parentIds = parent.getParentIds();

                String newParentIds;
                if (Strings.isNullOrEmpty(parentIds)) {
                    newParentIds = parent.getFid();
                } else {
                    newParentIds = Joiner.on(",").skipNulls().join(parentIds, parent.getFid());
                }
                entity.setParentIds(newParentIds);
            }

            //财务账套
            FiscalAccount fiscalAccount = attrType.getFiscalAccount();
            if (fiscalAccount != null) {
                entity.setFiscalAccount(fiscalAccount);
            }

            RequestResult check = checkRepeatByCategory(entity, 1);
            if(!check.isSuccess())return check;

            check = checkRepeatByCategory(entity, 0);
            if(!check.isSuccess())return check;
            if(vo.getScale()!=null){
            	entity.setScale(vo.getScale());
            }
            attrRepository.save(entity);
            
            for (AuxiliaryBiz biz : bizList) {
                if (biz.isSupport(attrType.getCode())) {
                    biz.saveAfter(entity.getFid());
                }
            }
            
        } else {

            if (vo.getEnable() == AuxiliaryAttr.STATUS_DISABLE) {
                List list = attrRepository.findByParentAndEnable(SecurityUtil.getCurrentOrgId(), vo.getFid());
                if (list != null && list.size()>0) return buildFailRequestResult("该节点存在有效的子节点，不允许设置为无效！");
            }

            entity = attrRepository.getOne(vo.getFid());

            if (entity == null) throw new DataNotExistException();

            if(!checkUpdateTime(vo.getUpdateTime(), entity.getUpdateTime())){
                return buildFailRequestResult("数据已被其他用户修改，请刷新再试！");
            }

            entity.setUpdateTime(Calendar.getInstance().getTime());
            entity.setCode(vo.getCode());
            entity.setName(vo.getName());
            entity.setDescribe(vo.getDescribe());
            entity.setEnable(vo.getEnable());
            if(vo.getScale()!=null){
            	entity.setScale(vo.getScale());
            }
            
            //entity.setCategory(new AuxiliaryAttrType(vo.getCategoryId()));

            //if(StringUtils.isNotBlank(vo.getParentId())){
            //entity.setParent(auxiliaryAttrService.get(vo.getParentId()));
            //}

            RequestResult check = checkRepeatByCategory(entity, 1);
            if(!check.isSuccess())return check;

            check = checkRepeatByCategory(entity, 0);
            if(!check.isSuccess())return check;

            attrRepository.save(entity);
        }

        redisService.remove(getCacheKey(entity.getCategory().getCode()));

//        if(vo.getTreeFlag()==AuxiliaryAttrVo.TREE_FLAG_MULTI){
//            return getSubTreeVo(entity);
//        }

        return buildSuccessRequestResult(getVo(entity));
    }

    /*
     * 获得entity的目录节点的树
     * @param entity
     * @return
     */
//    private RequestResult getSubTreeVo(AuxiliaryAttr entity){
//        Map<String, String> alias = new HashMap<String, String>();
//        alias.put("id", "fid");
//        alias.put("text", "code,name");
//        alias.put("children", "childs");
//        TreeUtils<AuxiliaryAttr, AuxiliaryAttrVo> util = new TreeUtils<AuxiliaryAttr, AuxiliaryAttrVo>(
//                alias, false, false, true);
//
//        final String orgId = SecurityUtil.getCurrentOrgId();
//        final String accountId = SecurityUtil.getFiscalAccountId();
//
//        AuxiliaryAttrType type = entity.getCategory();
//        List<AuxiliaryAttr> rootDatas = attrRepository.findRootData(type.getFid(), orgId);
//
//        List<TreeVo> subTree = Lists.newArrayList();
//
//        for(AuxiliaryAttr root:rootDatas){
//            TreeVo rootVo = new TreeVo();
//            rootVo.setId(root.getFid());
//            rootVo.setText(root.getCode()+" "+root.getName());
//
//            List<TreeVo> childTreeVos = util.getTreeData(new TreeCallback<AuxiliaryAttr, AuxiliaryAttrVo>() {
//
//                public AuxiliaryAttrVo getDataVo(AuxiliaryAttr e) {
//                    return getVo(e);
//                }
//
//                public List<AuxiliaryAttr> getChildren(AuxiliaryAttr e) {
//                    return attrRepository.findByParent(e, orgId, accountId);
//                }
//            }, root);
//            rootVo.getChildren().addAll(childTreeVos);
//
//            subTree.add(rootVo);
//        }
//
//        return buildSuccessRequestResult(entity);
//    }

    /**
     * 查找辅助属性，返回树结构
     */
    public List<TreeVo> findTree() {

        final String orgId = SecurityUtil.getCurrentOrgId();
        final String accountId = SecurityUtil.getFiscalAccountId();

        List<AuxiliaryAttrType> typeLists = attrTypeRepository.findAllEnable(orgId, accountId);

        List<TreeVo> typeTreeVos = Lists.newArrayList();

        Map<String, Object> map;

        for (AuxiliaryAttrType type : typeLists) {
            map = new HashMap<>();
            TreeVo typeTreeVo = new TreeVo();
            typeTreeVo.setText(type.getName());
            typeTreeVo.setId(type.getFid());

            map.put("isFirst", "isFirst");
            map.put("categoryId", type.getFid());
            map.put("treeFlag", type.getTreeFlag());

            typeTreeVo.setAttributes(map);
            typeTreeVos.add(typeTreeVo);
        }

        List<AuxiliaryAttr> rootData = attrRepository.findRootData(orgId);

        Map<String, String> alias = new HashMap<>();
        alias.put("id", "fid");
        alias.put("text", "code,name");
        alias.put("children", "childs");
        TreeUtils<AuxiliaryAttr, AuxiliaryAttrVo> util = new TreeUtils<>(
                alias, false, false, true);
        if (rootData != null && rootData.size() > 0) {

            for (AuxiliaryAttr attr : rootData) {
                if (attr.getCategory() == null) continue;
                for (TreeVo attrType : typeTreeVos) {
                    if (attr.getCategory().getFid().equals(attrType.getId())) {
                        List<cn.fooltech.fool_ops.utils.tree.TreeVo> childTreeVos = util.getTreeData(new TreeCallback<AuxiliaryAttr, AuxiliaryAttrVo>() {

                            public AuxiliaryAttrVo getDataVo(AuxiliaryAttr e) {
                                return getVo(e);
                            }

                            public List<AuxiliaryAttr> getChildren(AuxiliaryAttr e) {
                                return attrRepository.findByParent(e, orgId, accountId);
                            }
                        }, attr);
                        attrType.getChildren().addAll(childTreeVos);
                        break;
                    }
                }
            }
        }
        return typeTreeVos;
    }

    /**
     * 根据分类编码查找某一分类的所有辅助属性(忽略无效节点)
     */
    public List<CommonTreeVo> findSubAuxiliaryAttrTree(String code) {

        String orgId = SecurityUtil.getCurrentOrgId();
        String accountId = SecurityUtil.getFiscalAccountId();

        AuxiliaryAttrType type = attrTypeService.findByCode(code, orgId, accountId);
        List<AuxiliaryAttr> rootData = attrRepository.findRootData(type.getFid(), orgId);

        List<CommonTreeVo> typeTreeVos = Lists.newArrayList();

        Map<String, String> alias = new HashMap<>();
        alias.put("id", "fid");
        alias.put("text", "name");
        alias.put("children", "childs");
        TreeDataUtil<AuxiliaryAttr> util = new TreeDataUtil<>(alias, "code", false,
                true, AuxiliaryAttrVo.class);

        if (rootData != null && rootData.size() > 0) {
            for (AuxiliaryAttr attr : rootData) {
                List<CommonTreeVo> childTreeVos = util.getTreeData(attr);
                typeTreeVos.addAll(childTreeVos);
            }
        }

        CommonTreeVo vo = new CommonTreeVo();
        vo.setId(type.getFid());
        vo.setText(type.getName());
        vo.setChildren(new LinkedHashSet<>(typeTreeVos));

        List<CommonTreeVo> result = new ArrayList<>();
        result.add(vo);
        return result;

    }
    /**
     * 根据分类编码查找某一分类的所有辅助属性(忽略无效节点,加入模糊匹配)
     */
    public List<CommonTreeVo> fuzzyFindSubAuxiliaryAttrTree(String code,String searchKey) {

        String orgId = SecurityUtil.getCurrentOrgId();
        String accountId = SecurityUtil.getFiscalAccountId();

        AuxiliaryAttrType type = attrTypeService.findByCode(code, orgId, accountId);
        List<AuxiliaryAttr> rootData = attrRepository.fuzzyKeyFindRootData(type.getFid(), orgId,searchKey);

        List<CommonTreeVo> typeTreeVos = Lists.newArrayList();

        Map<String, String> alias = new HashMap<>();
        alias.put("id", "fid");
        alias.put("text", "name");
        alias.put("children", "childs");
        TreeDataUtil<AuxiliaryAttr> util = new TreeDataUtil<>(alias, "code", false,
                true, AuxiliaryAttrVo.class);

        if (rootData != null && rootData.size() > 0) {
            for (AuxiliaryAttr attr : rootData) {
                List<CommonTreeVo> childTreeVos = util.getTreeData(attr);
                typeTreeVos.addAll(childTreeVos);
            }
        }

        CommonTreeVo vo = new CommonTreeVo();
        vo.setId(type.getFid());
        vo.setText(type.getName());
        vo.setChildren(new LinkedHashSet<>(typeTreeVos));

        List<CommonTreeVo> result = new ArrayList<>();
        result.add(vo);
        return result;

    }
    /**
     * 根据分类编码查找某一分类的所有辅助属性(包含无效节点)
     */
    public List<CommonTreeVo> findAllSubAuxiliaryAttrTree(String code) {

        String orgId = SecurityUtil.getCurrentOrgId();
        String accountId = SecurityUtil.getFiscalAccountId();

        AuxiliaryAttrType type = attrTypeService.findByCode(code, orgId, accountId);
        List<AuxiliaryAttr> rootData = attrRepository.findAllRootData(type.getFid(), orgId);

        List<CommonTreeVo> typeTreeVos = Lists.newArrayList();

        Map<String, String> alias = new HashMap<>();
        alias.put("id", "fid");
        alias.put("text", "name");
        alias.put("children", "childs");
        TreeDataUtil<AuxiliaryAttr> util = new TreeDataUtil<>(alias, "code", false,
                true, AuxiliaryAttrVo.class);

        if (rootData != null && rootData.size() > 0) {
            for (AuxiliaryAttr attr : rootData) {
                List<CommonTreeVo> childTreeVos = util.getTreeData(attr);
                typeTreeVos.addAll(childTreeVos);
            }
        }

        CommonTreeVo vo = new CommonTreeVo();
        vo.setId(type.getFid());
        vo.setText(type.getName());
        vo.setChildren(new LinkedHashSet<>(typeTreeVos));

        List<CommonTreeVo> result = new ArrayList<>();
        result.add(vo);
        return result;

    }

    /**
     * 维护ParentIds
     */
    public RequestResult updateOrgParentIds() {
        List<AuxiliaryAttr> attrs = attrRepository.findAll();
        for (AuxiliaryAttr attr : attrs) {
            String parentIds = attrRepository.getParentIds(attr, "");
            Splitter splitter = Splitter.on(",").omitEmptyStrings().trimResults();
            List<String> ids = splitter.splitToList(parentIds);
            attr.setParentIds(parentIds);
            attr.setLevel(ids.size() + 1);
            attrRepository.save(attr);
        }
        return buildSuccessRequestResult();
    }

    /**
     * 根据编号，名称检索科目类别（财务科目导入）
     *
     * @param typeCode 类别编号
     * @param code     编号
     * @param name     类别名称
     */
    @SuppressWarnings({"unused"})
    public RequestResult checkSubAuxiliaryAttr(String typeCode, String code, String name) {
        AuxiliaryAttrVo vo = new AuxiliaryAttrVo();
        AuxiliaryAttr info = new AuxiliaryAttr();
        String orgId = SecurityUtil.getCurrentOrgId();// 机构id
        String accountId = SecurityUtil.getFiscalAccountId();// 账套id
        try {
            // 编号没有填写，名称没有填写：提示错误跳过
            if (Strings.isNullOrEmpty(code) && Strings.isNullOrEmpty(name)) {
                return new RequestResult(RequestResult.RETURN_FAILURE, -1, "科目类别编号和名称为空！");
            }
            // 编号没有填写，名称有填写：
            if (Strings.isNullOrEmpty(code) && !Strings.isNullOrEmpty(name)) {
                List<AuxiliaryAttr> list2 = attrRepository.checkByCodeAndName(orgId, typeCode, "", accountId, name);
                // 根据名称取数据存在则取存在数据，不存在则提示错误跳过
                if (list2 != null && list2.size() > 0) {
                    return new RequestResult(RequestResult.RETURN_SUCCESS, 1, list2.get(0).getCode());
                } else {
                    return new RequestResult(RequestResult.RETURN_FAILURE, -1, "科目类别编号为空，科目类别名称不存在！");
                }

            }
            // 编号有填写，名称没有填写：
            if (!Strings.isNullOrEmpty(code) && Strings.isNullOrEmpty(name)) {
                // 根据编号取数据存在则取存在数据，不存在则提示错误跳过
                List<AuxiliaryAttr> list2 = attrRepository.checkByCodeAndName(orgId, typeCode, code, accountId, "");
                // 根据名称取数据存在则取存在数据，不存在则提示错误跳过
                if (list2 != null && list2.size() > 0) {
                    return new RequestResult(RequestResult.RETURN_SUCCESS, 2, list2.get(0).getName());
                } else {
                    return new RequestResult(RequestResult.RETURN_FAILURE, -1, "科目类别编号不存在，科目类别名称为空！");
                }

            }
            // 编号名称都有填写
            if (!Strings.isNullOrEmpty(code) && !Strings.isNullOrEmpty(name)) {
                List<AuxiliaryAttr> list2 = attrRepository.checkByCodeAndName(orgId, typeCode, code, accountId, name);
                if (list2 != null && list2.size() > 0)
                    return new RequestResult(RequestResult.RETURN_SUCCESS, 0, "科目类别编号已存在，科目类别名称已存在");

                // 编号不存在，名称存在 提示错误跳过
                List<AuxiliaryAttr> list3 = attrRepository.checkByCodeAndName(orgId, typeCode, "", accountId, name);
                if (list3 != null && list3.size() > 0) {
                    if (list3.get(0).getName().equals(name) && !list3.get(0).getCode().equals(code))
                        return new RequestResult(RequestResult.RETURN_FAILURE, -1, "科目类别编号不存在，科目类别名称已存在！");
                }
                //编号存在，名称不存在 提示错误跳过
                List<AuxiliaryAttr> list4 = attrRepository.checkByCodeAndName(orgId, typeCode, code, accountId, "");
                if (list4 != null && list4.size() > 0) {
                    if (!list4.get(0).getName().equals(name) && list4.get(0).getCode().equals(code))
                        return new RequestResult(RequestResult.RETURN_FAILURE, -1, "科目类别编号已存在，科目类别名称不存在！");
                }
                //编号不存在，名称不存在 自动新建
                if (list2 == null || list2.size() <= 0) {
                    vo.setCode(code);
                    vo.setName(name);
                    vo.setCreateTime(Calendar.getInstance().getTime());
                    vo.setEnable(AuxiliaryAttr.STATUS_ENABLE);
                    AuxiliaryAttrType findByCode = attrTypeService.findByCode(typeCode, orgId, accountId);
                    if (findByCode != null) {
                        vo.setCategoryId(findByCode.getFid());
                        vo.setFirst("isFirst");
                        save(vo);
                        return new RequestResult(RequestResult.RETURN_SUCCESS, 0, "科目类别编号不存在，科目类别名称不存在！");
                    } else {
                        return new RequestResult(RequestResult.RETURN_FAILURE, 0, "添加科目类别异常");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new RequestResult(RequestResult.RETURN_FAILURE, "科目类别异常");
        }
        return null;
    }

    @Override
    public CrudRepository<AuxiliaryAttr, String> getRepository() {
        return this.attrRepository;
    }

    /**
     * 根据肤质属性ID查找是否已经给使用
     *
     * @param auxiliaryId auxiliaryId
     */
    public  boolean auxiliaryUse(String auxiliaryId) {
        if (Strings.isNullOrEmpty(auxiliaryId)) return false;
        String sql = "select f_auxiliary_is_use('" + auxiliaryId + "') as count";
        Map<String, Object> map = jdbcTemplate.queryForMap(sql);
        if (map.isEmpty()) return false;
        Integer count = map.get("count") == null ? 0 : Integer.parseInt(map.get("count").toString());
        return count > 0;
    }

    /**
     * 修复账套属性
     *
     * @param category category
     */
    @Transactional
    public void fixAccountByCategory(AuxiliaryAttrType category) {
        List<AuxiliaryAttr> list = attrRepository.findRepeatByCategory(category.getFid());
        for (AuxiliaryAttr attr : list) {
            attr.setFiscalAccount(category.getFiscalAccount());
            attrRepository.save(attr);
        }
    }

    /**
     * 根据编号查找
     *
     * @param ordId    机构id
     * @param typeCode 关联科目的code
     * @param code     科目code
     */
    public AuxiliaryAttr getByCode(String ordId, String typeCode, String code) {
        return attrRepository.getByCode(ordId, typeCode, code);
    }

    /**
     * 根据编号查找
     *
     * @param ordId    机构id
     * @param typeCode 关联科目的code
     * @param code     科目code
     */
    public AuxiliaryAttr getByCode(String ordId, String typeCode, String code, String accId) {
        return attrRepository.getByCode(ordId, typeCode, code, accId);
    }

    /**
     * 根据分类ID查找辅助属性的根节点(包含无效节点)
     */
    public List<AuxiliaryAttr> findAllRootData(String catagoryId, String ordId) {
        return attrRepository.findAllRootData(catagoryId, ordId);
    }    /**
     * 根据分类ID查找辅助属性的节点(包含无效节点)
     */
    public List<AuxiliaryAttr> findAllData(String catagoryId, String ordId) {
        return attrRepository.findAllData(catagoryId, ordId);
    }
    /**
     * 根据分类ID查找辅助属性的根节点(包含无效节点)
     * 返回
     */
    public Page<AuxiliaryAttr> findPageAllRootData(String catagoryId, String ordId,Pageable page) {
    	
        return attrRepository.findPageAllRootData(catagoryId, ordId,page);
    }
    /**
     * 判断组内编号是否已存在
     *
     * @param orgId     机构ID
     * @param accountId 财务账套ID
     * @param typeCode  类别的编号
     * @param code      属性的编号
     * @param parentId  父节点ID
     * @param excludeId 排除的ID
     */
    public boolean isCodeExist(String orgId, String accountId, String typeCode, String code, String parentId, String excludeId) {
        return attrRepository.isCodeExist(orgId, accountId, typeCode, code, parentId, excludeId);

    }

    /**
     * 根据名字查找类目
     * @param cateName 类目名字
     * @param orgId 机构ID
     * @param accId 账套
     */
    public List<AuxiliaryAttr> getAuxiliaryAttrByCate(String cateName, String orgId, String accId) {
        AuxiliaryAttrType auxiliaryAttrType = attrTypeRepository.getAttrTypeByName(cateName, orgId);
        List<AuxiliaryAttr> list = attrRepository.findByCategoryId(auxiliaryAttrType.getFid(), orgId, accId);
        list.sort(Comparator.comparing(StorageBaseEntity::getCode));
        return list;
    }
}
