package cn.fooltech.fool_ops.domain.sysman.service;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.component.security.SecurityUser;
import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.sysman.entity.Resource;
import cn.fooltech.fool_ops.domain.sysman.entity.Role;
import cn.fooltech.fool_ops.domain.sysman.entity.RoleAuth;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.sysman.repository.*;
import cn.fooltech.fool_ops.domain.sysman.vo.ResourceVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.tree.FastTreeUtils;
import cn.fooltech.fool_ops.utils.tree.TreeRootCallBack;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 资源服务类
 *
 * @author xjh
 */
@Service
public class ResourceService extends BaseService<Resource, ResourceVo, String> {

    @Autowired
    private ResourceRepository resRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleAuthRepository roleAuthRepo;

    @Autowired
    private UserAuthRepository userAuthRepo;

    @Override
    public ResourceVo getVo(Resource entity) {
        ResourceVo vo = new ResourceVo();
        vo.setFid(entity.getFid());
        if (entity.getParent() != null) {
            vo.setParent(entity.getParent().getFid());
        }
        vo.setResDesc(entity.getResDesc());
        vo.setResName(entity.getResName());
        vo.setResString(entity.getResString());
        vo.setResType(entity.getResType());
        vo.setCode(entity.getCode());
        vo.setSmallIcoPath(entity.getSmallIcoPath());
        vo.setPermType(entity.getPermType());
        vo.setRankOrder(entity.getRankOrder());
        vo.setShow(entity.getShow());
        return vo;
    }

    /**
     * 根据角色获取资源s
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<ResourceVo> getResourcesByRoleId(String roleId) {
        Role role = roleRepo.findOne(roleId);
        List<Resource> resList = role.getResources();
        return getVos(resList);
    }

    /**
     * 根据用户ID获取资源s
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<ResourceVo> getResourcesByUserId(String userId) {
        User user = userRepo.findOne(userId);
        List<Resource> resList = user.getResources();
        return getVos(resList);
    }

    /**
     * 获取子资源数据
     *
     * @param fid 资源Id
     * @return
     */
    public List<ResourceVo> getResChilds(String fid) {
        return getVos(resRepo.getChildResources(fid));
    }


    /**
     * 保存资源信息
     */
    @Transactional
    public RequestResult save(ResourceVo vo) {
        Resource entity = null;
        String fid = vo.getFid();
        String resDesc = vo.getResDesc();
        String resName = vo.getResName();
        String resString = vo.getResString();
        short resType = vo.getResType();
        String code = vo.getCode();
        Integer rankOrder = vo.getRankOrder();
        Integer dataFilter = vo.getDateFilter();
        Integer show=vo.getShow();

        boolean check = existResourceCode(code, fid, null);
        if (check) {
            return new RequestResult(RequestResult.RETURN_FAILURE, "资源编号重复");
        }

        Resource parent = resRepo.findOne(vo.getParent());

        resString = resString == null ? "" : resString;
        if (fid == null || fid == "") {
            entity = new Resource();
            entity.setValidation((short) 1);
        } else {
            entity = resRepo.findOne(fid);
        }
        Integer resApp = vo.getResApp();
        if (resApp == null) {
            resApp = 1;
        }
        entity.setResApp(resApp);
        if (parent != null) {
            entity.setParent(parent);
        }
        entity.setResDesc(resDesc);
        entity.setResName(resName);
        entity.setResString(resString.trim());
        entity.setResType(resType);
        entity.setCode(code);
        entity.setRankOrder(rankOrder);
        entity.setSmallIcoPath(vo.getSmallIcoPath());
        entity.setDataFilter(dataFilter);
        entity.setShow(show);
        entity.setPermType(vo.getPermType());
        resRepo.save(entity);
        List<Role> roles = roleRepo.findByRoleName("system");                //平台最高权限的角色
        if (roles.size() > 0) {
            RoleAuth roleAuth = new RoleAuth();
            roleAuth.setRole(roles.get(0));
            roleAuth.setResource(entity);
            roleAuth.setDataFilter(dataFilter);
            roleAuthRepo.save(roleAuth);
        }
        return new RequestResult();
    }

    /**
     * 构建资源菜单树
     *
     * @param trimRoot
     * @return
     */
    public List<ResourceVo> buildResourceTree(boolean trimRoot) {
        SecurityUser user = SecurityUtil.getSecurityUser();
        if (user == null) return Collections.emptyList();

        List<Resource> resList = resRepo.findAllValid(new Sort(Direction.DESC, "rankOrder"));

        List<ResourceVo> menus = getVos(resList);
        FastTreeUtils<ResourceVo> util = new FastTreeUtils<ResourceVo>();
        ResourceVo comparator = new ResourceVo();
        menus = util.buildTreeData(menus, 0, comparator, new TreeRootCallBack<ResourceVo>() {

            @Override
            public boolean isRoot(ResourceVo v) {
                return Constants.RESOURCE_ROOT.equals(v.getCode());
            }
        });
        if (trimRoot) {
            menus = menus.get(0).getChildren();//去掉根节点
        }

        return menus;
    }

    /**
     * 检查资源编号是否存在
     *
     * @param code      资源编号
     * @param excludeId 要排除的fid
     * @param neResType 要排除的资源类型
     * @return
     */
    public boolean existResourceCode(String code, String excludeId, Short neResType) {
        Long count = resRepo.countByResourceCode(code, excludeId, neResType);
        if (count != null && count > 0) return true;
        return false;
    }

    /**
     * 根据用户ID查找用户权限编码
     *
     * @param userId
     * @return
     */
    public Set<String> queryAuthCode(String userId) {
        User user = userRepo.findOne(userId);
        if (user == null) return null;

        List<Resource> resources = userAuthRepo.findByUser(user.getFid());
        Set<String> permCodes = Sets.newHashSet();
        for (Resource perm : resources) {
            permCodes.add(perm.getCode());
        }

        List<Role> roles = user.getRoles();
        for (Role role : roles) {
            List<Resource> roleAuths = role.getResources();
            for (Resource perm : roleAuths) {
                permCodes.add(perm.getCode());
            }
        }

        return permCodes;
    }

    @Override
    public CrudRepository<Resource, String> getRepository() {
        return this.resRepo;
    }


}
