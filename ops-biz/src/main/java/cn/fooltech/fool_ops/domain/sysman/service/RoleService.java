package cn.fooltech.fool_ops.domain.sysman.service;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.component.exception.DataNotExistException;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.sysman.entity.Role;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.sysman.repository.RoleRepository;
import cn.fooltech.fool_ops.domain.sysman.repository.UserRepository;
import cn.fooltech.fool_ops.domain.sysman.vo.RoleVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>角色网页服务类</p>
 *
 * @author lzf
 * @version 1.0
 * @date 2015年5月19日
 */
@Service("ops.RoleService")
public class RoleService extends BaseService<Role, RoleVo, String> {

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private UserRepository userRepo;


    /**
     * 单个实体转vo
     *
     * @param entity
     * @return
     */
    @Override
    public RoleVo getVo(Role entity) {
        RoleVo vo = new RoleVo();
        vo.setFid(entity.getFid());
        vo.setRoleCode(entity.getRoleCode());
        vo.setRoleDesc(entity.getRoleDesc());
        vo.setRoleName(entity.getRoleName());
        vo.setValidation(entity.getValidation());
        return vo;
    }

    /**
     * 查询角色列表信息<br>
     *
     * @param vo
     * @return
     */
    public Page<RoleVo> query(int pageNo, int pageSize) {

        String orgId = SecurityUtil.getCurrentOrgId();
        Sort sort = new Sort(Direction.ASC, "roleCode");
        PageRequest pageRequest = new PageRequest(pageNo - 1, pageSize, sort);
        Page<Role> page = roleRepo.findPageByOrgId(orgId, pageRequest);
        return getPageVos(page, pageRequest);
    }

    /**
     * 用户信息管理-新增用户信息
     *
     * @param vo
     * @return
     */
    public void saveOrUpdate(RoleVo vo) {
        String fid = vo.getFid();
        String roleCode = vo.getRoleCode();
        String roleDesc = vo.getRoleDesc();
        String roleName = vo.getRoleName();
        Short validation = vo.getValidation();

        Role entity = null;
        if (StringUtils.isNotBlank(fid)) {
            entity = roleRepo.findOne(fid);
            entity.setValidation(validation);
        } else {
            entity = new Role();
            entity.setOrgId(SecurityUtil.getCurrentOrg());
            entity.setValidation((short) 1);
        }

        entity.setRoleDesc(roleDesc);
        entity.setRoleName(roleName);
        entity.setRoleCode(roleCode);

        roleRepo.save(entity);
    }

    /**
     * 用户信息管理-刪除用户信息
     *
     * @param vo
     * @return
     */
    public RequestResult delete(String id) {
        Role role = roleRepo.findOne(id);
        if (role != null) {
            role.setValidation((short) 0);
            roleRepo.save(role);
            return buildSuccessRequestResult();
        } else {
            throw new DataNotExistException();
        }
    }


    @Override
    public CrudRepository<Role, String> getRepository() {
        return this.roleRepo;
    }

    /**
     * 角色授权给用户
     *
     * @param addUserFids
     * @param delUserFids
     * @param roleId
     */
    public RequestResult savaRoleUser(String addUserFids, String delUserFids, String roleId) {
        Role role = roleRepo.findOne(roleId);
        Splitter splitter = Splitter.on(";").omitEmptyStrings().trimResults();

        if (StringUtils.isNotBlank(delUserFids)) {
            List<String> delIds = splitter.splitToList(delUserFids);
            List<User> users = role.getUsers();
            for (String delId : delIds) {
                for (int i = users.size() - 1; i >= 0; i--) {
                    User user = users.get(i);
                    if (delId.equals(user.getFid())) {
                        users.remove(i);
                        break;
                    }
                }
            }
        }

        if (StringUtils.isNotBlank(addUserFids)) {
            List<String> addIds = splitter.splitToList(addUserFids);
            List<User> users = role.getUsers();
            for (String userId : addIds) {
                boolean exist = false;
                for (User user : users) {
                    if (user.getFid().equals(userId)) {
                        exist = true;
                        break;
                    }
                }
                if (!exist) {
                    User adduser = userRepo.findOne(userId);
                    users.add(adduser);
                }
            }
        }

        roleRepo.save(role);
        return buildSuccessRequestResult();
    }

}
