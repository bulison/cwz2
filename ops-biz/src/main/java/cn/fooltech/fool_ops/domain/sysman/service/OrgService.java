package cn.fooltech.fool_ops.domain.sysman.service;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.component.redis.BaseDataCache;
import cn.fooltech.fool_ops.component.redis.RedisService;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.BaseConstant;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.repository.OrganizationRepository;
import cn.fooltech.fool_ops.domain.sysman.vo.OrganizationVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.tree.FastTreeUtils;
import cn.fooltech.fool_ops.utils.tree.TreeRootCallBack;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.mysql.jdbc.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>用户网页服务类</p>
 *
 * @author xjh
 * @version 2.0
 * @date 2016年10月18日
 */
@Service("ops.OrgService")
public class OrgService extends BaseService<Organization, OrganizationVo, String> implements BaseDataCache {


    @Autowired
    private OrganizationRepository orgRepo;

    @Autowired
    private RedisService redisService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public OrganizationVo getVo(Organization entity) {
        OrganizationVo vo = new OrganizationVo();
        vo.setFid(entity.getFid());
        vo.setEmail(entity.getEmail());
        vo.setFaddress(entity.getFaddress());
        vo.setFax(entity.getFax());
        vo.setHomePage(entity.getHomePage());
        vo.setOrgDesc(entity.getOrgDesc());
        vo.setOrgName(entity.getOrgName());
        vo.setPhoneOne(entity.getPhoneOne());
        vo.setPostCode(entity.getPostCode());
        vo.setOrgCode(entity.getOrgCode());
        if (entity.getParent() != null) {
            vo.setParent(entity.getParent().getFid());
        }
        return vo;
    }

    @Override
    public CrudRepository<Organization, String> getRepository() {
        return this.orgRepo;
    }

    /**
     * 获得登录用户所有部门的树结构数据
     *
     * @return
     */
    public List<OrganizationVo> getTreeData() {
        String orgId = SecurityUtil.getCurrentOrgId();
        List<Organization> orgs = orgRepo.findByTopOrgId(orgId);
        List<OrganizationVo> orgVos = getVos(orgs);
        FastTreeUtils<OrganizationVo> util = new FastTreeUtils<OrganizationVo>();
        OrganizationVo comparator = new OrganizationVo();
        orgVos = util.buildTreeData(orgVos, 2, comparator, new TreeRootCallBack<OrganizationVo>() {

            @Override
            public boolean isRoot(OrganizationVo v) {
                return StringUtils.isNullOrEmpty(v.getParentId());
            }
        });
        return orgVos;
    }

    /**
     * 获得所有子节点（不包含自己）
     *
     * @param org
     * @return
     */
    public List<Organization> getAllChildren(Organization org) {
        String parentIds = org.getParentIds();
        if (Strings.isNullOrEmpty(parentIds)) {
            parentIds = org.getFid();
        } else {
            parentIds = parentIds + "," + org.getFid();
        }

        return orgRepo.findByParentIdsStartingWith(parentIds);
    }

    /**
     * 获得当前机构
     *
     * @return
     */
    public OrganizationVo getOrg() {
        Organization org = SecurityUtil.getCurrentOrg();
        return getVo(org);
    }


    /**
     * 保存组织信息
     *
     * @param vo
     * @return
     */
    public void save(OrganizationVo vo) {
        String email = vo.getEmail();
        String address = vo.getFaddress();
        String fax = vo.getFax();
        String homePage = vo.getHomePage();
        String orgDesc = vo.getOrgDesc();
        String orgName = vo.getOrgName();
        String phoneOne = vo.getPhoneOne();
        String postCode = vo.getPostCode();

        Organization entity = SecurityUtil.getCurrentOrg();
        entity.setEmail(email);
        entity.setFaddress(address);
        entity.setFax(fax);
        entity.setHomePage(homePage);
        entity.setOrgDesc(orgDesc);
        entity.setOrgName(orgName);
        entity.setPhoneOne(phoneOne);
        entity.setPostCode(postCode);
        entity.setParentIds("");
        orgRepo.save(entity);

        redisService.remove(getCacheKey());
    }

    /**
     * 保存部门信息
     *
     * @return
     */
    public RequestResult saveDept(OrganizationVo vo) {
        Organization entity = null;
        String orgName = vo.getOrgName();
        String phoneOne = vo.getPhoneOne();
        String parentId = vo.getParent();
        String orgCode = vo.getOrgCode();
        String orgId = SecurityUtil.getCurrentOrgId();
        if (isCodeExist(orgId, orgCode, vo.getFid())) {
            return buildFailRequestResult("编号重复");
        }
        if (isNameExist(orgId, orgName, vo.getFid())) {
        	return buildFailRequestResult("名称重复");
        }

        Organization parent = orgRepo.findOne(parentId);

        String parentIds = "";
        Short parentLevel = null;

        if (parent != null) {
            parentLevel = parent.getFlevel();
            if (parentLevel == null) parentLevel = 0;
            parentIds = parent.getParentIds();
        }

        String newParentIds = null;
        if (parent == null) {
            newParentIds = "";
        } else {
            if (Strings.isNullOrEmpty(parentIds)) {
                newParentIds = parent.getFid();
            } else {
                newParentIds = Joiner.on(",").skipNulls().join(parentIds, parent.getFid());
            }
        }

        if (vo.getFid() == null || "".equals(vo.getFid())) {
            entity = new Organization();
            entity.setCreateUser(SecurityUtil.getCurrentUser());
            entity.setValidation((short) 1);
            entity.setOrgId(orgId);
            if (parentLevel != null) {
                int newLevel = parentLevel + 1;
                entity.setFlevel(Short.valueOf(newLevel + ""));
            } else {
                entity.setFlevel((short) 0);
            }
        } else {
            entity = orgRepo.findOne(vo.getFid());
        }
        entity.setOrgName(orgName);
        entity.setPhoneOne(phoneOne);
        entity.setParent(parent);
        entity.setOrgCode(orgCode);
        entity.setParentIds(newParentIds);
        orgRepo.save(entity);

        vo = getVo(entity);

        redisService.remove(getCacheKey());

        return buildSuccessRequestResult(vo);

    }

    /**
     * 判断是否存在部门编码
     *
     * @param orgId
     * @param orgCode
     * @param excludeId
     * @return
     */
    public boolean isCodeExist(String orgId, String orgCode, String excludeId) {
        Long count = 0L;
        if (Strings.isNullOrEmpty(excludeId)) {
            count = orgRepo.countByCode(orgId, orgCode);
        } else {
            count = orgRepo.countByCode(orgId, orgCode, excludeId);
        }
        if (count != null && count > 0) {
            return true;
        }
        return false;
    }
    /**
     * 判断是否存在部门名称
     * @param orgId
     * @param orgName
     * @param excludeId
     * @return
     */
    public boolean isNameExist(String orgId, String orgName, String excludeId) {
    	Long count = 0L;
    	if (Strings.isNullOrEmpty(excludeId)) {
    		count = orgRepo.countByName(orgId, orgName);
    	} else {
    		count = orgRepo.countByName(orgId, orgName, excludeId);
    	}
    	if (count != null && count > 0) {
    		return true;
    	}
    	return false;
    }

    /**
     * 判断是否存在部门编码
     *
     * @param orgCode
     * @return
     */
    public boolean isCodeExist(String orgCode) {
        Long count = 0L;
        if (Strings.isNullOrEmpty(orgCode)) {
            count = orgRepo.countByCode(orgCode);
        }

        if (count != null && count > 0) {
            return true;
        }
        return false;
    }

    /**
     * 刪除部门信息
     *
     * @param fid
     * @return
     */
    @Transactional
    public RequestResult deleteDept(String fid) {
        try {
			if (!this.deptIsUse(fid)) {
			    Organization entity = orgRepo.findOne(fid);
			    entity.setValidation((short) 0);
			    orgRepo.save(entity);
			    redisService.remove(getCacheKey());
			    return buildSuccessRequestResult();
			} else {
			    return buildFailRequestResult("部门信息已被使用，不允许删除!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			 return buildFailRequestResult("删除部门出错!");
		}

    }
    /**
     * 根据部门ID查找记录是否给引用
     *
     * @param deptId 部门id
     */
    public boolean deptIsUse(String deptId) {
        if (Strings.isNullOrEmpty(deptId)) return false;
        String sql = "select f_dept_is_use('" + deptId + "') as count";
        Map<String, Object> map = jdbcTemplate.queryForMap(sql);
        if (map.isEmpty()) return false;
        Integer count = map.get("count") == null ? 0 : Integer.parseInt(map.get("count").toString());
        return count > 0;
    }

    /**
     * 查找所有机构
     *
     * @return
     */
    public List<OrganizationVo> getAllOrg(OrganizationVo vo) {
        return getVos(orgRepo.findByOrgIdIsNull(vo));
    }

    public Organization getByCode(String orgId, String orgCode, String selfId) {
        if (Strings.isNullOrEmpty(selfId)) {
            return orgRepo.getByCode(orgId, orgCode);
        } else {
            return orgRepo.getByCode(orgId, orgCode, selfId);
        }
    }

    @Override
    public String getCacheName() {
        return BaseConstant.DEMARTMENT;
    }
}
