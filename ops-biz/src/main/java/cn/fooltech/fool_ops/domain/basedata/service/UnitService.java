package cn.fooltech.fool_ops.domain.basedata.service;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.Goods;
import cn.fooltech.fool_ops.domain.basedata.entity.Unit;
import cn.fooltech.fool_ops.domain.basedata.repository.GoodsPriceRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.GoodsRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.UnitRepository;
import cn.fooltech.fool_ops.domain.basedata.vo.UnitVo;
import cn.fooltech.fool_ops.domain.common.vo.CommonTreeVo;
import cn.fooltech.fool_ops.domain.fiscal.repository.FiscalAccountingSubjectRepository;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.warehouse.repository.WarehouseBillDetailRepository;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.TreeDataUtil;
import cn.fooltech.fool_ops.validator.ValidatorUtils;
import com.google.common.base.Strings;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.*;


/**
 * <p>单位服务类</p>
 *
 * @author xjh
 * @date 2016-10-26
 */
@Service
public class UnitService extends BaseService<Unit, UnitVo, String> {


    @Autowired
    private UnitRepository unitRepo;

    @Autowired
    private GoodsRepository goodsRepo;

    @Autowired
    private GoodsPriceRepository priceRepo;

    @Autowired
    private WarehouseBillDetailRepository detailRepo;

    @Autowired
    private FiscalAccountingSubjectRepository subjectRepo;

    @Override
    public CrudRepository<Unit, String> getRepository() {
        return unitRepo;
    }

    @Override
    public UnitVo getVo(Unit entity) {
        if (entity == null) return null;
        UnitVo vo = new UnitVo();
        vo.setFid(entity.getFid());
        vo.setCode(entity.getCode());
        vo.setName(entity.getName());
        vo.setFlag(entity.getFlag());
        vo.setScale(entity.getScale().toString());
        vo.setDescribe(entity.getDescribe());
        vo.setCreateTime(entity.getCreateTime());
        vo.setUpdateTime(DateUtilTools.time2String(entity.getUpdateTime()));
        vo.setEnable(entity.getEnable());
        vo.setFirst(entity.getScale().compareTo(new BigDecimal(1)) == 0);
        //组织机构
        Organization org = entity.getOrg();
        if (org != null) {
            vo.setOrgId(org.getFid());
        }
        //父单位
        Unit parent = entity.getParent();
        if (parent != null) {
            vo.setParentId(parent.getFid());
            vo.setParentName(parent.getName());
            vo.setRoot(false);
        } else {
            vo.setRoot(true);
        }
        return vo;
    }

    /**
     * 根据父ID获取同样父ID的换算关系=1的记账单位
     *
     * @param parentId
     * @return
     */
    public Unit findAccountUnit(String parentId) {
        Unit unit = unitRepo.findTopByGroupId(parentId);
        if (null == unit) return null;
        return unit;
    }

    /**
     * 根据父ID判断当前同级节点个数
     *
     * @param parentId
     * @return
     */
    public int sameLevelCount(String parentId) {
        Long count = unitRepo.countByParentId(parentId);
        return count == null ? 0 : count.intValue();
    }

    /**
     * 新增、编辑
     */
    @Transactional
    public RequestResult save(UnitVo vo) {
        String inValid = ValidatorUtils.inValidMsg(vo);
        if (inValid != null) {
            return buildFailRequestResult(inValid);
        }
        String fid = vo.getFid();
        String name = vo.getName();
        String code = vo.getCode();
        String scale = vo.getScale();
        String parentId = vo.getParentId();
        String describe = vo.getDescribe();
        Integer enable = vo.getEnable() == null ? Unit.ENABLE : vo.getEnable();
        Date now = new Date();

        Organization org = SecurityUtil.getCurrentOrg();
        Assert.notNull(org, "用户所属机构不存在!");

        //校验数据
        RequestResult result = checkByRule(vo);
        if (RequestResult.RETURN_FAILURE == result.getReturnCode()) {
            return result;
        }

        Unit entity = null;
        if (StringUtils.isBlank(fid)) {
            entity = new Unit();
            entity.setOrg(org);
            entity.setDept(SecurityUtil.getCurrentDept());
            entity.setCreateTime(now);
            entity.setUpdateTime(now);
            entity.setCreator(SecurityUtil.getCurrentUser());
        } else {
            entity = unitRepo.findOne(fid);
            if (entity == null) {
                return new RequestResult(RequestResult.RETURN_FAILURE, "该记录不存在或已被删除!");
            }
            entity.setUpdateTime(now);
        }
        entity.setCode(code.trim());
        entity.setName(name.trim());
        entity.setScale(new BigDecimal(scale.trim())); //换算关系
        entity.setDescribe(describe);

        //父单位
        if (StringUtils.isNotBlank(parentId)) {
            Unit parent = unitRepo.findOne(parentId);
            entity.setParent(parent);
            // 标识
            if (parent == null || parent.getParent() == null || StringUtils.isBlank(parent.getParent().getFid())) {
                entity.setFlag(Unit.FLAG_GROUP);
            } else {
                entity.setFlag(Unit.FLAG_UNIT);
            }
        }

        //是否有效
        if (entity.getFlag() == Unit.FLAG_GROUP && enable == Unit.UNABLE && CollectionUtils.isNotEmpty(entity.getChilds())) {
            Set<Unit> childs = entity.getChilds();
            for (Unit child : childs) {
                if (child.getEnable() == Unit.ENABLE) {
                    child.setEnable(Unit.UNABLE);
                    unitRepo.save(child);
                }
            }
        }
        if (entity.getFlag() == Unit.FLAG_GROUP && enable == Unit.ENABLE && CollectionUtils.isNotEmpty(entity.getChilds())) {
            Set<Unit> childs = entity.getChilds();
            for (Unit child : childs) {
                if (child.getEnable() == Unit.UNABLE) {
                    child.setEnable(Unit.ENABLE);
                    unitRepo.save(child);
                }
            }
        }
        entity.setEnable(enable);

        //新增、编辑
        unitRepo.save(entity);
        result.setData(entity.getFid());
        return result;
    }

    /**
     * 获取所有单位
     *
     * @return
     */
    public List<CommonTreeVo> getAll() {
        Map<String, String> alias = new HashMap<String, String>();
        String orgId = SecurityUtil.getCurrentOrgId();
        alias.put("id", "fid");
        alias.put("text", "code,name");
        alias.put("children", "childs");
        Unit root = unitRepo.getRootUnit(orgId);
        TreeDataUtil<Unit> util = new TreeDataUtil<Unit>(alias, "createTime", true, true, UnitVo.class);
        return util.getTreeData(root);
    }

    /**
     * 获取全部有效的单位
     *
     * @return
     */
    public List<CommonTreeVo> getTree() {
        Map<String, String> alias = new HashMap<String, String>();
        String orgId = SecurityUtil.getCurrentOrgId();
        alias.put("id", "fid");
        alias.put("text", "name");
        alias.put("children", "childs");
        Unit root = unitRepo.getRootUnit(orgId);
        TreeDataUtil<Unit> util = new TreeDataUtil<Unit>(alias, "code", true, "enable", new Integer[]{Unit.UNABLE}, true, UnitVo.class);
        return util.getTreeData(root);
    }

    /**
     * 获取单位组下所有有效的单位
     *
     * @param groupId 单位组ID
     * @return
     */
    public List<CommonTreeVo> getPartTree(String unitGroupId) {
        Map<String, String> alias = new HashMap<String, String>();
        alias.put("id", "fid");
        alias.put("text", "name");
        alias.put("children", "childs");
        Unit root = unitRepo.findOne(unitGroupId);
        TreeDataUtil<Unit> util = new TreeDataUtil<Unit>(alias, "code", true, "enable", new Integer[]{Unit.UNABLE}, true, UnitVo.class);
        return util.getTreeData(root);
    }

    /**
     * 获取单位组下的有效单位
     *
     * @param unitGroupId 单位组ID
     * @return
     */
    public List<UnitVo> getChilds(String unitGroupId) {
        List<Unit> entities = unitRepo.getChilds(unitGroupId);
        return getVos(entities);
    }

    /**
     * 获取单位组下的有效单位，通过单位名称模糊匹配
     *
     * @param unitGroupId 单位组ID
     * @param unitName    单位名称
     * @return
     */
    public List<UnitVo> getChildsOfMatch(String unitGroupId, String unitName) {
        List<Unit> entities = null;
        if (Strings.isNullOrEmpty(unitName)) {
            entities = unitRepo.getChilds(unitGroupId);
        } else {
            entities = unitRepo.getChildsOfMatch(unitGroupId, unitName);
        }

        return getVos(entities);
    }

    /**
     * 通过货品ID获取单位组
     *
     * @return
     */
    public Unit getByGoodsId(String goodsId) {
        Goods goods = goodsRepo.findOne(goodsId);
        Unit unitGroup = goods.getUnitGroup();
        return unitGroup;
    }

    /**
     * 删除
     *
     * @param id 单位ID
     */
    public RequestResult delete(String id) {
        RequestResult result = new RequestResult();
        Unit unit = unitRepo.findOne(id);
        if (unit.getFlag() == Unit.FLAG_UNIT && BigDecimal.ONE.compareTo(unit.getScale()) == 0) {
            return buildFailRequestResult("不允许删除换算关系为1的单位!");
        }
        if (CollectionUtils.isNotEmpty(unit.getChilds())) {
            return buildFailRequestResult("请先删除子单位!");
        }
        if (isUnitUsed(unit.getFid())) {
            return buildFailRequestResult("无法删除，该单位已被使用!");
        }
        unitRepo.delete(unit);
        return result;
    }

    /**
     * 更新操作时，校验数据的实时性
     *
     * @param vo 主键、更新时间
     * @return true 有效  false 无效
     */
    private boolean checkDataRealTime(UnitVo vo) {
        if (StringUtils.isNotBlank(vo.getFid())) {
            Unit entity = unitRepo.findOne(vo.getFid());
            Date formDate = DateUtilTools.string2Time(vo.getUpdateTime());
            int num = formDate.compareTo(entity.getUpdateTime());
            return num == 0;
        }
        return true;
    }

    /**
     * 根据规则校验数据
     *
     * @param unit
     * @return
     */
    public RequestResult checkByRule(UnitVo unit) {
        String orgId = SecurityUtil.getCurrentOrgId();
        RequestResult result = new RequestResult();
        if (!checkDataRealTime(unit)) {
            return buildFailRequestResult("页面数据失效，请重新刷新页面!");
        }
        if (isCodeExist(orgId, unit.getParentId(),unit.getCode(), unit.getFid())) {
            return buildFailRequestResult("组内编号重复!");
        }
        if (isNameExist(orgId, unit.getParentId(),unit.getName(), unit.getFid())) {
            return buildFailRequestResult("组内名称重复!");
        }
        if (!unit.isRoot() && (StringUtils.isBlank(unit.getFid()) ? true : unit.getFlag() != Unit.FLAG_GROUP)
                && !hasChilds(unit.getFid())
                && isScaleExist(orgId, unit.getParentId(), new BigDecimal(unit.getScale().trim()), unit.getFid())) {
            return buildFailRequestResult("组内换算关系重复!");
        }
        return result;
    }

    /**
     * 判断某个机构里，单位编号在组内是否重复
     *
     * @param orgId     组织机构ID
     * @param groupId   组ID
     * @param code      单位编号
     * @param excludeId 排除实体的ID
     * @return
     */
    public boolean isCodeExist(String orgId, String groupId, String code, String excludeId) {
        Long count = null;
        if (Strings.isNullOrEmpty(excludeId)) {
            count = unitRepo.countByGroupCode(orgId, groupId, code);
        } else {
            count = unitRepo.countByGroupCode(orgId, groupId, code, excludeId);
        }
        if (count != null && count > 0) return true;
        return false;
    }

    /**
     * 判断某个机构里，单位名称在组内是否重复
     *
     * @param orgId     组织机构ID
     * @param groupId   组ID
     * @param name      单位名称
     * @param excludeId 排除实体的ID
     * @return
     */
    public boolean isNameExist(String orgId, String groupId, String name, String excludeId) {
        Long count = null;
        if (Strings.isNullOrEmpty(excludeId)) {
            count = unitRepo.countByGroupName(orgId, groupId, name);
        } else {
            count = unitRepo.countByGroupName(orgId, groupId, name, excludeId);
        }
        if (count != null && count > 0) return true;
        return false;
    }

    /**
     * 判断某个机构里，换算单位在组内是否重复
     *
     * @param orgId     组织机构ID
     * @param groupId   组ID
     * @param scale     换算单位
     * @param excludeId 排除实体的ID
     * @return
     */
    public boolean isScaleExist(String orgId, String groupId, BigDecimal scale, String excludeId) {
        Long count = null;
        if (Strings.isNullOrEmpty(excludeId)) {
            count = unitRepo.countByGroupScale(orgId, groupId, scale);
        } else {
            count = unitRepo.countByGroupScale(orgId, groupId, scale, excludeId);
        }
        if (count != null && count > 0) return true;
        return false;
    }

    /**
     * 判断单位是否已被使用
     *
     * @param unitId
     * @return true 是  false 不是
     */
    public boolean isUnitUsed(String unitId) {
        //货品
        if (goodsRepo.countByUnitId(unitId) > 0) {
            return true;
        }
        //货品定价
        if (priceRepo.countByUnitId(unitId) > 0) {
            return true;
        }
        //仓库单据明细
        if (detailRepo.countByUnitId(unitId) > 0) {
            return true;
        }
        //科目
        if (subjectRepo.countByUnitId(unitId) > 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否有子单位
     *
     * @param fid 单位ID
     * @return
     */
    public boolean hasChilds(String fid) {
        if (StringUtils.isNotBlank(fid)) {
            Long count = unitRepo.countByParentId(fid);
            if (count != null && count > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取机构下的叶子单位
     *
     * @return
     */
    public List<UnitVo> getLeafUnit() {
        String orgId = SecurityUtil.getCurrentOrgId();
        List<Unit> list = unitRepo.getLeafUnit(orgId);
        return getVos(list);
    }

    /**
     * 获取某个机构下，最顶级的单位
     *
     * @return
     */
    public UnitVo getRootUnit() {
        String orgId = SecurityUtil.getCurrentOrgId();
        Unit unit = unitRepo.getRootUnit(orgId);
        return getVo(unit);
    }

    /**
     * 根据编号查找
     *
     * @param orgId
     * @param unitCode
     * @return
     */
    public Unit getByCode(String orgId, String unitCode) {
        return unitRepo.findTopByCode(orgId, unitCode);
    }

    /**
     * 根据子节点的编号查找
     * @param orgId
     * @param code
     * @return
     */
    public Unit findTopByLeafCode(String orgId, String code){
    	return unitRepo.findTopByLeafCode(orgId, code);
    }
    
    public Unit findTopByCodeAndParent(String orgId, String code,String parentId){
    	return unitRepo.findTopByCodeAndParent(orgId, code, parentId);
    }

}
