package cn.fooltech.fool_ops.domain.basedata.service;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.GoodsSpec;
import cn.fooltech.fool_ops.domain.basedata.entity.PurchasePrice;
import cn.fooltech.fool_ops.domain.basedata.repository.GoodsPriceRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.GoodsRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.GoodsSpecRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.PurchasePriceRepository;
import cn.fooltech.fool_ops.domain.basedata.vo.GoodsSpecVo;
import cn.fooltech.fool_ops.domain.common.vo.CommonTreeVo;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.warehouse.repository.WarehouseBillDetailRepository;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.TreeDataUtil;
import cn.fooltech.fool_ops.utils.VoFactory;
import cn.fooltech.fool_ops.validator.ValidatorUtils;
import com.google.common.base.Strings;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * <p>货品属性服务类</p>
 *
 * @author xjh
 * @date 2016-10-26
 */
@Service
public class GoodsSpecService extends BaseService<GoodsSpec, GoodsSpecVo, String> {


    @Autowired
    private GoodsSpecRepository goodsSpecRepo;

    @Autowired
    private GoodsRepository goodsRepo;

    @Autowired
    private GoodsPriceRepository priceRepo;

    @Autowired
    private WarehouseBillDetailRepository detailRepo;

    @Autowired
    private PurchasePriceRepository purchasePriceRepository;

    @Override
    public CrudRepository<GoodsSpec, String> getRepository() {
        return goodsSpecRepo;
    }

    @Override
    public GoodsSpecVo getVo(GoodsSpec entity) {
        if (entity == null) return null;
        GoodsSpecVo vo = VoFactory.createValue(GoodsSpecVo.class, entity);
        vo.setUpdateTime(DateUtilTools.time2String(entity.getUpdateTime()));
        //组织机构
        Organization org = entity.getOrg();
        if (org != null) {
            vo.setOrgId(org.getFid());
        }
        //父属性
        GoodsSpec parent = entity.getParent();
        if (parent != null) {
            vo.setParentId(parent.getFid());
            vo.setParentName(parent.getName());
        }
        return vo;
    }

    /**
     * 获取所有货品属性
     *
     * @return
     */
    public List<CommonTreeVo> getAll() {
        Map<String, String> alias = new HashMap<String, String>();
        alias.put("id", "fid");
        alias.put("text", "code,name");
        alias.put("children", "childs");
        GoodsSpec root = goodsSpecRepo.findTopByRoot(SecurityUtil.getCurrentOrgId());
        TreeDataUtil<GoodsSpec> util = new TreeDataUtil<GoodsSpec>(alias, "createTime", true, true, GoodsSpecVo.class);
        return util.getTreeData(root);
    }

    /**
     * 获取全部有效的货品属性
     *
     * @return
     */
    public List<CommonTreeVo> getTree() {
        Map<String, String> alias = new HashMap<String, String>();
        alias.put("id", "fid");
        alias.put("text", "name");
        alias.put("children", "childs");
        GoodsSpec root = goodsSpecRepo.findTopByRoot(SecurityUtil.getCurrentOrgId());
        TreeDataUtil<GoodsSpec> util = new TreeDataUtil<GoodsSpec>(alias, "code", true, "recordStatus", new String[]{GoodsSpec.STATUS_SNU}, true, GoodsSpecVo.class);
        return util.getTreeData(root);
    }

    /**
     * 获取货品属性组下有效的属性
     *
     * @param groupId 属性组ID
     * @return
     */
    public List<CommonTreeVo> getPartTree(String groupId) {
        Map<String, String> alias = new HashMap<String, String>();
        alias.put("id", "fid");
        alias.put("text", "name");
        alias.put("children", "childs");
        GoodsSpec goodSpec = goodsSpecRepo.findOne(groupId);
        TreeDataUtil<GoodsSpec> util = new TreeDataUtil<GoodsSpec>(alias, "code", true, "recordStatus", new String[]{GoodsSpec.STATUS_SNU}, true, GoodsSpecVo.class);
        return util.getTreeData(goodSpec);
    }

    /**
     * 获取货品属性组下的子属性(分页)
     *
     * @param groupId   货品属性组ID
     * @param name      货品属性名称
     * @param paramater
     * @return
     */
    public Page<GoodsSpecVo> getChilds(String groupId, String name, PageParamater paramater) {

        String orgId = SecurityUtil.getCurrentOrgId();
        Sort sort = new Sort(Direction.ASC, "code");
        PageRequest pageRequest = getPageRequest(paramater, sort);

        Page<GoodsSpec> page = goodsSpecRepo.findPageByName(orgId, groupId, name, pageRequest);
        return getPageVos(page, pageRequest);
    }

    /**
     * 获取货品属性组下的子属性
     *
     * @param groupId 货品属性组ID
     * @param name    货品属性名称
     * @return
     */
    public List<GoodsSpecVo> getChidlList(String groupId, String name) {
        String orgId = SecurityUtil.getCurrentOrgId();
        List<GoodsSpec> datas = goodsSpecRepo.findByParentIdAndName(orgId, groupId, name);
        return getVos(datas);
    }

    /**
     * 获取全部有效的货品组
     *
     * @return
     */
    public List<CommonTreeVo> getSpecGroups() {
        String orgId = SecurityUtil.getCurrentOrgId();
        GoodsSpec root = goodsSpecRepo.findTopByRoot(orgId);
        List<GoodsSpec> entities = goodsSpecRepo.findSpecGroups(orgId, root.getFid());

        List<CommonTreeVo> result = new ArrayList<CommonTreeVo>();
        CommonTreeVo rootVo = getCommonTreeVo(root);
        rootVo.setChildren(getCommonTreeVos(entities));
        result.add(rootVo);
        return result;
    }

    /**
     * 多个实体转CommonTreeVo
     *
     * @param entities
     * @return
     */
    private LinkedHashSet<CommonTreeVo> getCommonTreeVos(List<GoodsSpec> entities) {
    	/*2017-4-26 cwz 把原来的Set类型改为 LinkedHashSet*/
    	LinkedHashSet<CommonTreeVo> set = new LinkedHashSet<CommonTreeVo>();
        for (GoodsSpec entity : entities) {
            set.add(getCommonTreeVo(entity));
        }
        return set;
    }

    /**
     * 单个实体转CommonTreeVo
     *
     * @param entity
     * @return
     */
    public CommonTreeVo getCommonTreeVo(GoodsSpec entity) {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("detail", getVo(entity));

        CommonTreeVo vo = new CommonTreeVo();
        vo.setId(entity.getFid());
        vo.setText(entity.getName());
        vo.setAttributes(attributes);
        return vo;
    }

    /**
     * 添加、编辑
     *
     * @param vo
     * @return
     */
    public RequestResult save(GoodsSpecVo vo) {
        String inValid = ValidatorUtils.inValidMsg(vo);
        if (inValid != null) {
            return new RequestResult(RequestResult.RETURN_FAILURE, inValid);
        }
        String fid = vo.getFid();
        String code = vo.getCode();
        String name = vo.getName();
        String describe = vo.getDescribe();
        String parentId = vo.getParentId();
        String recordStatus = vo.getRecordStatus();
        Date now = new Date();
        recordStatus = StringUtils.isBlank(recordStatus) ? GoodsSpec.STATUS_SAC : recordStatus; //默认有效

        RequestResult result = checkByRule(vo);
        if (result.getReturnCode() == RequestResult.RETURN_FAILURE) {
            return result;
        }

        GoodsSpec entity = null;
        if (StringUtils.isBlank(fid)) {
            entity = new GoodsSpec();
            entity.setCreateTime(now);
            entity.setUpdateTime(now);
            entity.setCreator(SecurityUtil.getCurrentUser());
            entity.setOrg(SecurityUtil.getCurrentOrg());
            entity.setDept(SecurityUtil.getCurrentDept());
        } else {
            entity = goodsSpecRepo.findOne(fid);
            if (entity == null) {
                return new RequestResult(RequestResult.RETURN_FAILURE, "该记录不存在或已被删除!");
            }
            entity.setUpdateTime(now);
        }
        entity.setCode(code.trim());
        entity.setName(name.trim());
        entity.setDescribe(describe);

        //父属性
        if (StringUtils.isNotBlank(parentId)) {
            GoodsSpec parent = goodsSpecRepo.findOne(parentId);
            entity.setParent(parent);

            //标识
            if (parent == null || parent.getParent() == null || StringUtils.isBlank(parent.getParent().getFid())) {
                entity.setFlag(GoodsSpec.FLAG_GROUP);
            } else {
                entity.setFlag(GoodsSpec.FLAG_SPEC);
            }
        }

        //是否有效
        if (entity.getFlag() == GoodsSpec.FLAG_GROUP && recordStatus.equals(GoodsSpec.STATUS_SNU) && CollectionUtils.isNotEmpty(entity.getChilds())) {
            Set<GoodsSpec> childs = entity.getChilds();
            for (GoodsSpec child : childs) {
                if (child.getRecordStatus().equals(GoodsSpec.STATUS_SAC)) {
                    child.setRecordStatus(GoodsSpec.STATUS_SNU);
                    goodsSpecRepo.save(child);
                }
            }
        }
        if (entity.getFlag() == GoodsSpec.FLAG_GROUP && recordStatus.equals(GoodsSpec.STATUS_SAC) && CollectionUtils.isNotEmpty(entity.getChilds())) {
            Set<GoodsSpec> childs = entity.getChilds();
            for (GoodsSpec child : childs) {
                if (child.getRecordStatus().equals(GoodsSpec.STATUS_SNU)) {
                    child.setRecordStatus(GoodsSpec.STATUS_SAC);
                    goodsSpecRepo.save(child);
                }
            }
        }
        entity.setRecordStatus(recordStatus);

        goodsSpecRepo.save(entity);
        result.setData(VoFactory.createValue(GoodsSpecVo.class, entity));
        return result;
    }

    /**
     * 根据规则校验数据
     *
     * @param vo
     * @return
     */
    private RequestResult checkByRule(GoodsSpecVo vo) {
        RequestResult result = new RequestResult();
        if (!checkDataRealTime(vo)) {
            return buildFailRequestResult("页面数据失效，请重新刷新页面!");
        }
        String fid = vo.getFid();
        String parentId = vo.getParentId();
        if (isCodeExist(vo.getCode(), parentId,fid)) {
            return buildFailRequestResult("编号重复!");
        }
        if (isNameExist(vo.getName(),vo.getParentId(), vo.getFid())) {
            return buildFailRequestResult("名称重复!");
        }
        return result;
    }

    /**
     * 判断编号是否已存在
     *
     * @return
     */
    public boolean isCodeExist(String code, String excludeId,String fid) {
        String orgId = SecurityUtil.getCurrentOrgId();
        Long count = null;
        if(fid==null){
        	if(Strings.isNullOrEmpty(excludeId)){
        		count = goodsSpecRepo.countByCode(orgId, code);
        	}else{
        		count = goodsSpecRepo.countByCodeAndParent(orgId, code,excludeId);
        	}
        	
        }
        else if (Strings.isNullOrEmpty(excludeId)) {
            count = goodsSpecRepo.countByCode(orgId, code,fid);
        } else {
            count = goodsSpecRepo.countByCode(orgId, code, excludeId,fid);
        }
        if (count != null && count > 0) return true;
        return false;
    }

    /**
     * 判断组内名称是否已存在
     *
     * @return
     */
    public boolean isNameExist(String name, String parentId, String excludeId) {
        String orgId = SecurityUtil.getCurrentOrgId();
        Long count = null;
        if (Strings.isNullOrEmpty(excludeId)) {
            count = goodsSpecRepo.countByName(orgId, parentId, name);
        } else {
            count = goodsSpecRepo.countByName(orgId, parentId, name, excludeId);
        }
        if (count != null && count > 0) return true;
        return false;
    }

    /**
     * 更新操作时，校验数据的实时性
     *
     * @param vo 主键、更新时间
     * @return true 有效  false 无效
     */
    private boolean checkDataRealTime(GoodsSpecVo vo) {
        if (StringUtils.isNotBlank(vo.getFid())) {
            GoodsSpec entity = goodsSpecRepo.findOne(vo.getFid());
            Date formDate = DateUtilTools.string2Time(vo.getUpdateTime());
            int num = formDate.compareTo(entity.getUpdateTime());
            return num == 0;
        }
        return true;
    }

    /**
     * 删除
     *
     * @param id 货品属性ID
     * @return
     */
    public RequestResult delete(String id) {
        RequestResult result = new RequestResult();

        GoodsSpec goodsSpec = goodsSpecRepo.findOne(id);
        Set<GoodsSpec> childs = goodsSpec.getChilds();
        if (CollectionUtils.isNotEmpty(childs)) {
            result.setMessage("请先删除子货品属性!");
            result.setReturnCode(RequestResult.RETURN_FAILURE);
        } else if (isSpecUsed(id)) {
            result.setMessage("无效操作，该属性已被使用!");
            result.setReturnCode(RequestResult.RETURN_FAILURE);
        } else {
            goodsSpecRepo.delete(goodsSpec);
        }
        return result;
    }

    /**
     * 判断是否已被使用
     *
     * @param id 货品属性ID
     * @return
     */
    public boolean isSpecUsed(String id) {
        if (goodsRepo.countByGoodsSpecId(id) > 0) {
            return true;
        }
        if (priceRepo.countByGoodsSpecId(id) > 0) {
            return true;
        }
        if (detailRepo.countByGoodsSpecId(id) > 0) {
            return true;
        }
        if (purchasePriceRepository.countByGoodsSpecId(id) > 0) {
            return true;
        }
        return false;
    }

    /**
     * 获取某个机构全部有效的叶子货品属性
     *
     * @return
     */
    public List<GoodsSpecVo> getLeafSpec() {
        String orgId = SecurityUtil.getCurrentOrgId();
        List<GoodsSpec> list = goodsSpecRepo.getLeafSpec(orgId);
        return getVos(list);
    }


    /**
     * 获取某个机构下，最顶级的属性
     *
     * @return
     */
    public GoodsSpecVo getRootGoodsSpec() {
        String orgId = SecurityUtil.getCurrentOrgId();
        GoodsSpec goodsSpec = goodsSpecRepo.findTopByRoot(orgId);
        return getVo(goodsSpec);
    }

    /**
     * 模糊搜索货品属性(根据编号、名称)
     *
     * @param vo
     * @return
     */
    public List<GoodsSpecVo> vagueSearch(GoodsSpecVo vo) {
        String orgId = SecurityUtil.getCurrentOrgId();
        String parentId = vo.getParentId();
        String searchKey = vo.getSearchKey();
        Integer pageSize = vo.getSearchSize();

        Sort sort = new Sort(Direction.ASC, "code", "name");
        PageRequest pageRequest = new PageRequest(0, pageSize, sort);

        Page<GoodsSpec> page = goodsSpecRepo.findPageByVagueSearch(orgId, parentId, searchKey, pageRequest);
        List<GoodsSpec> entities = page.getContent();
        return getVos(entities);
    }

    /**
     * 根据编号查找
     *
     * @param orgId
     * @param goodsSpecCode
     * @return
     */
    public GoodsSpec getByCode(String orgId, String goodsSpecCode) {
        return goodsSpecRepo.findTopByCode(orgId, goodsSpecCode);
    }
    public GoodsSpec findTopByCodeAndSpecGroupId(String orgId, String goodsSpecCode,String parentId) {
    	return goodsSpecRepo.findTopByCodeAndSpecGroupId(orgId, goodsSpecCode,parentId);
    }
}
