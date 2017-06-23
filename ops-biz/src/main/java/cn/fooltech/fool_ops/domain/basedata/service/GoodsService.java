package cn.fooltech.fool_ops.domain.basedata.service;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.component.redis.BaseDataCache;
import cn.fooltech.fool_ops.component.redis.RedisService;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.BaseConstant;
import cn.fooltech.fool_ops.domain.basedata.entity.*;
import cn.fooltech.fool_ops.domain.basedata.repository.AuxiliaryAttrRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.GoodsRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.GoodsSpecRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.UnitRepository;
import cn.fooltech.fool_ops.domain.basedata.vo.GoodsVo;
import cn.fooltech.fool_ops.domain.common.vo.CommonTreeVo;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalAccountingSubjectService;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.sysman.service.UserAttrService;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBillDetail;
import cn.fooltech.fool_ops.domain.warehouse.service.WarehouseBillDetailService;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCode;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.utils.*;
import cn.fooltech.fool_ops.utils.input.FivepenUtils;
import cn.fooltech.fool_ops.utils.input.PinyinUtils;
import cn.fooltech.fool_ops.validator.ValidatorUtils;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * <p>
 * 货品服务类
 * </p>
 *
 * @author xjh
 * @date 2016-10-26
 */
@Service
public class GoodsService extends BaseService<Goods, GoodsVo, String>  implements BaseDataCache {

    // 单据类型
    public static final List<Integer> types = Lists.newArrayList(WarehouseBuilderCodeHelper.xsbjd,
            WarehouseBuilderCodeHelper.xsdd, WarehouseBuilderCodeHelper.xsch, WarehouseBuilderCodeHelper.xsth);
    public static final List<Integer> types2 = Lists.newArrayList(WarehouseBuilderCodeHelper.cgrk,
            WarehouseBuilderCodeHelper.scll, WarehouseBuilderCodeHelper.sctl, WarehouseBuilderCodeHelper.pdd,
            WarehouseBuilderCodeHelper.bsd, WarehouseBuilderCodeHelper.cgth);
    public static final List<Integer> types3 = Lists.newArrayList(WarehouseBuilderCodeHelper.cprk);
    // 单据查询类型
    public static final List<Integer> selectTypes = Lists.newArrayList(WarehouseBuilderCodeHelper.xsch,
            WarehouseBuilderCodeHelper.xsdd, WarehouseBuilderCodeHelper.xsbjd);
    public static final List<Integer> selectTypes2 = Lists.newArrayList(WarehouseBuilderCodeHelper.cgrk,
            WarehouseBuilderCodeHelper.cgdd, WarehouseBuilderCodeHelper.cgxjd, WarehouseBuilderCodeHelper.cprk);
    public static final List<Integer> selectTypes3 = Lists.newArrayList(WarehouseBuilderCodeHelper.cprk);
    @Autowired
    private AuxiliaryAttrRepository attrRepo;
    @Autowired
    private GoodsRepository goodsRepo;
    @Autowired
    private GoodsSpecRepository specRepo;
    @Autowired
    private UnitRepository unitRepo;
    @Autowired
    private WarehouseBillDetailService billDetailService;
    @Autowired
    private GoodsPriceService priceService;
    @Autowired
    private GoodsBarService goodsBarService;
    @Autowired
    private UserAttrService userAttrService;

    @Autowired
    private RedisService redisService;
	/**
	 * 科目服务类
	 */
	@Autowired
	private FiscalAccountingSubjectService subjectService;
	

    @Override
    public CrudRepository<Goods, String> getRepository() {
        return goodsRepo;
    }

    @Override
    public GoodsVo getVo(Goods entity) {
        if (entity == null)
            return null;
        GoodsVo vo = VoFactory.createValue(GoodsVo.class, entity);
        vo.setCreateTime(DateUtilTools.time2String(entity.getCreateTime()));
        vo.setUpdateTime(DateUtilTools.time2String(entity.getUpdateTime()));
        // 重量
        if (entity.getWeight() != null) {
            vo.setWeight(NumberUtil.stripTrailingZeros(entity.getWeight()).toPlainString());
        }
        // 体积
        if (entity.getVolume() != null) {
            vo.setVolume(NumberUtil.stripTrailingZeros(entity.getVolume()).toPlainString());
        }
        // 组织机构
        Organization org = entity.getOrg();
        if (org != null) {
            vo.setOrgId(org.getFid());
        }
        // 分类
        AuxiliaryAttr category = entity.getCategory();
        if (category != null) {
            vo.setCategoryId(category.getFid());
            vo.setCategoryName(category.getName());
            vo.setCategoryCode(category.getCode());
        }
        // 货品属性
        GoodsSpec goodsSpec = entity.getGoodsSpec();
        if (goodsSpec != null) {
            vo.setGoodsSpecId(goodsSpec.getFid());
            vo.setGoodsSpecName(goodsSpec.getName());
            vo.setGoodsSpecGroupId(goodsSpec.getFid());
            vo.setGoodsSpecCode(goodsSpec.getCode());
        }
        // 单位
        Unit unit = entity.getUnit();
        if (unit != null) {
            vo.setUnitId(unit.getFid());
            vo.setUnitName(unit.getName());
            vo.setUnitScale(NumberUtil.bigDecimalToStr(unit.getScale()));
            vo.setUnitCode(unit.getCode());
        }
        // 上级货品
        Goods parent = entity.getParent();
        if (parent != null) {
            vo.setParentId(parent.getFid());
            vo.setParentName(parent.getName());
            vo.setParentCode(parent.getCode());
        }
        // 创建人
        User creator = entity.getCreator();
        if (creator != null) {
            vo.setCreatorId(creator.getFid());
            vo.setCreatorName(creator.getUserName());
        }
        // 单位组Id
        Unit unitGroup = entity.getUnitGroup();
        if (unitGroup != null) {
            vo.setUnitGroupId(unitGroup.getFid());
            vo.setUnitGroupName(unitGroup.getName());
        }
        return vo;
    }

    /**
     * 获取企业的所有货品
     *
     * @return
     */
    public List<CommonTreeVo> getAll() {
        String orgId = SecurityUtil.getCurrentOrgId();
        Map<String, String> alias = new HashMap<String, String>();
        alias.put("id", "fid");
        alias.put("text", "code,name");
        alias.put("children", "childs");
        Goods root = goodsRepo.findTopRootGoodsOfOrgId(orgId);
        TreeDataUtil<Goods> util = new TreeDataUtil<Goods>(alias, "code", true, "flag",
                new Object[]{Goods.FLAG_GOODS}, true, GoodsVo.class);
        return util.getTreeData(root);
    }





    /**
     * 获取企业的全部有效的货品
     *
     * @return
     */
    public List<CommonTreeVo> getTree() {
        String orgId = SecurityUtil.getCurrentOrgId();
        Map<String, String> alias = new HashMap<String, String>();
        alias.put("id", "fid");
        alias.put("text", "name");
        alias.put("children", "childs");
        Goods root = goodsRepo.findTopRootGoodsOfOrgId(orgId);
        TreeDataUtil<Goods> util = new TreeDataUtil<Goods>(alias, "code", true, "recordStatus",
                new String[]{Goods.STATUS_SNU}, true, GoodsVo.class);
        return util.getTreeData(root);
    }

    /**
     * 获取货品组下的有效的货品
     *
     * @param groupId 货品组ID
     * @return
     */
    public List<CommonTreeVo> getPartTree(String groupId) {
        Map<String, String> alias = new HashMap<String, String>();
        alias.put("id", "fid");
        alias.put("text", "name");
        alias.put("children", "childs");
        Goods goods = goodsRepo.findOne(groupId);
        TreeDataUtil<Goods> util = new TreeDataUtil<Goods>(alias, "code", true, "recordStatus",
                new String[]{Goods.STATUS_SNU}, true, GoodsVo.class);
        return util.getTreeData(goods);
    }

    /**
     * 获取所有标识为货品的记录
     *
     * @param vo
     * @return
     */
    public Page<GoodsVo> getChilds(GoodsVo vo, PageParamater paramater) {
        String orgId = SecurityUtil.getCurrentOrgId();
        String name = vo.getName();
        String fid = vo.getFid();
        String code = vo.getCode();
        String parentId = vo.getParentId();
        String searchKey = vo.getSearchKey();
        String spec = vo.getSpec();
        String nids = vo.getNids();
        Integer showChild = vo.getShowChild();
        Integer showDisable = vo.getShowDisable();

        List<String> excludeIds = null;

        if (StringUtils.isNotBlank(nids)) {
            Splitter splitter = Splitter.on(",").omitEmptyStrings().trimResults();
            excludeIds = splitter.splitToList(vo.getNids());
        }

        Sort sort = new Sort(Direction.ASC, "code");
        PageRequest pageRequest = getPageRequest(paramater, sort);
        Page<Goods> page = goodsRepo.findAllChildren(fid, orgId, parentId, showChild,
                showDisable, code, name, searchKey, spec, excludeIds, pageRequest);

        Page<GoodsVo> pageVos = getPageVos(page, pageRequest);
        //loadOtherPrice(vo.getBillType(), vo.getCustomerId(), vo.getSupplierId(), pageVos.getContent());
        return pageVos;
    }

    /**
     * 加载货品的最低销售价、参考单价、成本价
     *
     * @param billType   仓库单据类型
     * @param customerId 客户ID
     * @param supplierId 供应商ID
     * @param vos
     */
    private void loadOtherPrice(WarehouseBuilderCode billType, String customerId, String supplierId,
                                List<GoodsVo> vos) {
        if (billType == null || CollectionUtils.isEmpty(vos)) {
            return;
        }

        for (GoodsVo vo : vos) {
            // 参考单价
            BigDecimal referencePrice = getReferenceUnitPrice(billType, customerId, supplierId, vo.getFid(),
                    vo.getUnitId(), vo.getGoodsSpecId());
            vo.setReferencePrice(referencePrice.toString());

            if (billType == WarehouseBuilderCode.xsth || billType == WarehouseBuilderCode.sctl) {
                // 成本单价
                BigDecimal costPrice = getCostPrice(customerId, vo.getFid(), vo.getUnitId(), vo.getGoodsSpecId());
                vo.setCostPrice(costPrice.toString());
            } else {
                // 最低销售价
                BigDecimal lowestPrice = priceService.getLowestPrice(vo.getFid(), vo.getUnitId(),
                        vo.getGoodsSpecId());
                vo.setLowestPrice(lowestPrice.toString());
            }
        }
    }

    /**
     * 获取货品的参考单价
     *
     * @param billType    仓库单据类型
     * @param customerId  客户ID
     * @param supplierId  供应商ID
     * @param goodsId     货品ID
     * @param unitId      货品单位ID
     * @param goodsSpecId 货品属性ID
     * @return
     */
    public BigDecimal getReferenceUnitPrice(WarehouseBuilderCode billType, String customerId, String supplierId,
                                            String goodsId, String unitId, String goodsSpecId) {
        /*
		 * 1)单价根据上一次销售出货单、销售报价单、销售订单查询出来 销售报价单、销售订单、销售出货 、销售退货
		 * 2)单价根据上一次采购入库，采购订单或者采购询价单查询出来 采购入库、生产领料、生产退料、盘点单、报损单、采购退货
		 * 3)单价根据上一次成品入库单查询出来 成品入库
		 */
        Integer type = WarehouseBuilderCodeHelper.getBillType(billType);
        if (type == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal referencePrice = BigDecimal.ZERO;

        // 直接使用货品单价作为参考单价
        if (types.contains(type)) {
            referencePrice = billDetailService.getUnitPrice(selectTypes, customerId, supplierId, goodsId, unitId,
                    goodsSpecId);
        } else if (types2.contains(type)) {
            referencePrice = billDetailService.getUnitPrice(selectTypes2, customerId, supplierId, goodsId, unitId,
                    goodsSpecId);
        } else if (types3.contains(type)) {
            referencePrice = billDetailService.getUnitPrice(selectTypes3, customerId, supplierId, goodsId, unitId,
                    goodsSpecId);
        }

        if (referencePrice.compareTo(BigDecimal.ZERO) == 0) {
            // 通过记账单价获取参考单价
            BigDecimal accountUintPrice = BigDecimal.ZERO;
            if (types.contains(type)) {
                accountUintPrice = billDetailService.getAccountUintPrice(selectTypes, customerId, supplierId, goodsId,
                        goodsSpecId);
            } else if (types2.contains(type)) {
                accountUintPrice = billDetailService.getAccountUintPrice(selectTypes2, customerId, supplierId, goodsId,
                        goodsSpecId);
            } else if (types3.contains(type)) {
                accountUintPrice = billDetailService.getAccountUintPrice(selectTypes3, customerId, supplierId, goodsId,
                        goodsSpecId);
            }
            Unit unit = unitRepo.findOne(unitId);
            referencePrice = unit.getScale().multiply(accountUintPrice);
        }
        return referencePrice;
    }

    /**
     * 针对销售退货单、生成退料，获取货品的成本单价
     *
     * @param customerId  客户ID
     * @param goodsId     货品ID
     * @param unitId      货品单位ID
     * @param goodsSpecId 货品属性ID
     * @return
     */
    public BigDecimal getCostPrice(String customerId, String goodsId, String unitId, String goodsSpecId) {
        // 成本单价根据上一次期初库存、采购入库、盘点单、成品入库查询出来
        List<Integer> selectTypes = new ArrayList<Integer>();
        selectTypes.add(WarehouseBuilderCodeHelper.qckc);
        selectTypes.add(WarehouseBuilderCodeHelper.cgrk);
        selectTypes.add(WarehouseBuilderCodeHelper.pdd);
        selectTypes.add(WarehouseBuilderCodeHelper.cprk);

        // 通过单价获取成本单价
        BigDecimal costPrice = billDetailService.getUnitPrice(selectTypes, customerId, null, goodsId, unitId,
                goodsSpecId);

        // 通过记账单价获取成本单价
        if (costPrice.compareTo(BigDecimal.ZERO) == 0) {
            BigDecimal accountUintPrice = billDetailService.getAccountUintPrice(selectTypes, customerId, null, goodsId,
                    goodsSpecId);
            Unit unit = unitRepo.findOne(unitId);
            costPrice = unit.getScale().multiply(accountUintPrice);
        }
        return costPrice;
    }

    /**
     * 新增、编辑
     *
     * @param vo
     * @return
     */
    public RequestResult save(GoodsVo vo) {
        String inValid = ValidatorUtils.inValidMsg(vo);
        if (inValid != null) {
            return new RequestResult(RequestResult.RETURN_FAILURE, inValid);
        }
        String fid = vo.getFid();
        String code = vo.getCode();
        String name = vo.getName();
        String spec = vo.getSpec();
        String barCode = vo.getBarCode();
        String describe = vo.getDescribe();
        String weight = vo.getWeight();
        String volume = vo.getVolume();
        Integer storagePeriod = vo.getStoragePeriod();
        String recordStatus = vo.getRecordStatus(); //有效状态
        Integer flag = vo.getFlag(); //货品标识
        Integer accountFlag = vo.getAccountFlag(); //记账标识
        String categoryId = vo.getCategoryId(); //分类
        String categoryCode = vo.getCategoryCode();
        String goodsSpecId = vo.getGoodsSpecId(); //货品属性ID
        String goodsSpecCode = vo.getGoodsSpecCode();
        String unitId = vo.getUnitId(); //单位ID
        String unitCode = vo.getUnitCode();
        String parentId = vo.getParentId(); //父ID
        String parentCode = vo.getParentCode();
        String orgId = SecurityUtil.getCurrentOrgId();
        Date now = new Date();
        flag = flag == null ? Goods.FLAG_GROUP : flag;
        accountFlag = accountFlag == null ? Goods.ACCOUNT_FLAG_YES : accountFlag;
        recordStatus = StringUtils.isBlank(recordStatus) ? Goods.STATUS_SAC : recordStatus;
        BigDecimal percentage = vo.getPercentage();//提成点数

        //校验数据
        RequestResult result = checkByRule(vo);
        if (result.getReturnCode() == RequestResult.RETURN_FAILURE) {
            return result;
        }

        if (!Strings.isNullOrEmpty(vo.getBarCode())) {
            if (goodsBarService.isBarCodeExist(vo.getBarCode(), orgId, null)) {
                return buildFailRequestResult("货品条码冲突");
            }

            if (isBarCodeExist(vo.getBarCode(), orgId, vo.getFid())) {
                return buildFailRequestResult("货品条码冲突");
            }
        }

        Goods entity = null;
        if (StringUtils.isBlank(fid)) {
            entity = new Goods();
            entity.setCreateTime(now);
            entity.setUpdateTime(now);
            entity.setOrg(SecurityUtil.getCurrentOrg());
            entity.setDept(SecurityUtil.getCurrentDept());
            entity.setCreator(SecurityUtil.getCurrentUser());
            entity.setFlag(flag); //可以为货品或货品组
        } else {
            entity = goodsRepo.findOne(fid);
            if (entity == null) {
                return new RequestResult(RequestResult.RETURN_FAILURE, "该记录不存在或已被删除!");
            }
            entity.setUpdateTime(now);
            //货品标识
            if (entity.getFlag() != flag && entity.getUseFlag() == Goods.USE_FLAG_USED) {
                throw new RuntimeException("不允许修改货品标识!");
            } else if (hasChilds(fid) && vo.getFlag() == Goods.FLAG_GOODS) {
                throw new RuntimeException("不允许修改货品标识!");
            } else {
                entity.setFlag(flag);
            }
        }
        if (weight != null && NumberUtil.isNum(weight) == false) {
            return new RequestResult(RequestResult.RETURN_FAILURE, "重量格式错误");
        }
        if (volume != null && NumberUtil.isNum(volume) == false) {
            return new RequestResult(RequestResult.RETURN_FAILURE, "体积格式错误");
        }
        entity.setPinyin(PinyinUtils.getPinyinCode(name, 300));
        entity.setFivepen(FivepenUtils.getFivePenCode(name));
        entity.setCode(code.trim());
        entity.setName(name.trim());
        entity.setSpec(spec);
        entity.setBarCode(barCode);
        entity.setDescribe(describe);
        entity.setWeight(NumberUtil.toBigDeciaml(weight));
        entity.setVolume(NumberUtil.toBigDeciaml(volume));
        entity.setStoragePeriod(storagePeriod);

        //分类
        if (StringUtils.isNotBlank(categoryId)) {
            entity.setCategory(attrRepo.findOne(categoryId));
        } else if (StringUtils.isNotBlank(categoryCode)) {
            AuxiliaryAttr attr = attrRepo.findTopByCode(orgId, AuxiliaryAttrType.CODE_GOODS_TYPE, categoryCode);
            if (attr != null) {
                entity.setCategory(attr);
            } else {
                return new RequestResult(RequestResult.RETURN_FAILURE, "找不到分类属性");
            }
        }else{
        	 entity.setCategory(null);
        }

        if (StringUtils.isBlank(fid) || (StringUtils.isNotBlank(fid) && entity.getUseFlag() == Goods.USE_FLAG_UNUSED)) {
            //单位、单位组
            if (StringUtils.isNotBlank(unitId)) {
                Unit unit = unitRepo.findOne(unitId);
                Unit unitGroup = unit.getParent();
                entity.setUnit(unit);
                entity.setUnitGroup(unitGroup);
            } else if (StringUtils.isNotBlank(unitCode)) {
                Unit unit = unitRepo.findTopByCode(orgId, unitCode);
                if (unit != null) {
                	if(unit.getFlag()==0){
                		entity.setUnitGroup(unit);
                		Unit sonunit=unitRepo.findSonByCode(orgId, unitCode, unit.getFid());
                		if(sonunit!=null){
                			entity.setUnit(sonunit);
                		}else{
                			return new RequestResult(RequestResult.RETURN_FAILURE, "找不到单位!");
                		}
                		
                	}else{
                        Unit unitGroup = unit.getParent();
                        entity.setUnit(unit);
                        entity.setUnitGroup(unitGroup);
                	}
                } else {
                    return new RequestResult(RequestResult.RETURN_FAILURE, "找不到单位!");
                }
            } else if (entity.getFlag() == Goods.FLAG_GOODS) {
                return new RequestResult(RequestResult.RETURN_FAILURE, "单位必填!");
            }

            //货品属性
            if (StringUtils.isNotBlank(goodsSpecId)) {
                entity.setGoodsSpec(specRepo.findOne(goodsSpecId));
            } else if (StringUtils.isNotBlank(goodsSpecCode)) {
                GoodsSpec goodsspec = specRepo.findTopByCode(orgId, goodsSpecCode);
                if (goodsspec != null) {
                    entity.setGoodsSpec(goodsspec);
                } else {
                    return new RequestResult(RequestResult.RETURN_FAILURE, "找不到货品属性!");
                }
            }else{
            	 entity.setGoodsSpec(null);
            }

            //记账标识
            entity.setAccountFlag(accountFlag);
        }

        Goods parent = null;
        //上级货品
        if (StringUtils.isNotBlank(parentId)) {
            parent = goodsRepo.findOne(parentId);
        } else if (StringUtils.isNotBlank(parentCode)) {
            parent = goodsRepo.findTopByCode(orgId, parentCode);
        }

        if (parent == null) {
            return new RequestResult(RequestResult.RETURN_FAILURE, "找不到上级货品");
        }

        int parentLevel = parent.getLevel(); //上级的层级数
        String parentFullPath = parent.getFullParentId(); //上级的父路径
        if (parentFullPath == null) {
            entity.setFullParentId(parentId);
        } else {
            entity.setFullParentId(parentFullPath + "," + parentId);
        }
        entity.setParent(parent);
        entity.setLevel(parentLevel + 1);

        //有效状态
        if (entity.getFlag() == Goods.FLAG_GROUP && recordStatus.equals(Goods.STATUS_SNU) && CollectionUtils.isNotEmpty(entity.getChilds())) {
            Set<Goods> childs = entity.getChilds();
            for (Goods child : childs) {
                if (child.getRecordStatus().equals(Goods.STATUS_SAC)) {
                    child.setRecordStatus(Goods.STATUS_SNU);
                    goodsRepo.save(child);
                }
            }
        }
        entity.setRecordStatus(recordStatus);
        //设置提成点数
        entity.setPercentage(percentage);
        goodsRepo.save(entity);

        GoodsVo goodsVo = VoFactory.createValue(GoodsVo.class, entity);
        goodsVo.setUpdateTime(DateUtilTools.time2String(entity.getUpdateTime()));
        result.setData(goodsVo);

        redisService.remove(getCacheKey());

        return result;
    }

    /**
     * 判断条码是否已存在
     *
     * @param orgId
     * @param barCode
     * @param orgId
     * @return
     */
    private boolean isBarCodeExist(String barCode, String orgId, String excludeId) {
        Long count = null;
        if (Strings.isNullOrEmpty(excludeId)) {
            count = goodsRepo.countByBarCode(orgId, barCode);
        } else {
            count = goodsRepo.countByBarCode(orgId, barCode, excludeId);
        }
        if (count != null && count > 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否有下级货品
     *
     * @param goodsId
     * @return true 有 false 没有
     */
    private boolean hasChilds(String goodsId) {
        Long count = goodsRepo.countByParentId(goodsId);
        if (count != null && count > 0) {
            return true;
        }
        return false;
    }

    /**
     * 更新操作时，校验数据的实时性
     *
     * @param vo 主键、更新时间
     * @return true 有效  false 无效
     */
    private boolean checkDataRealTime(GoodsVo vo) {
        if (StringUtils.isNotBlank(vo.getFid())) {
            Goods entity = goodsRepo.findOne(vo.getFid());
            Date formDate = DateUtilTools.string2Time(vo.getUpdateTime());
            int num = formDate.compareTo(entity.getUpdateTime());
            return num == 0;
        }
        return true;
    }

    /**
     * 根据规则校验
     *
     * @param vo
     * @return
     */
    public RequestResult checkByRule(GoodsVo vo) {
        String orgId = SecurityUtil.getCurrentOrgId();
        if (!checkDataRealTime(vo)) {
            return buildFailRequestResult("页面数据失效，请重新刷新页面!");
        }
        if (isCodeExist(orgId, vo.getCode(), vo.getFlag(), vo.getFid())) {
            return buildFailRequestResult("编号重复!");
        }
        return buildSuccessRequestResult();
    }

    /**
     * 判断编号是否有效
     *
     * @param code 编号
     * @return
     */
    public boolean isCodeExist(String orgId, String code, int flag, String excludeId) {
        Long count = null;
        if (Strings.isNullOrEmpty(excludeId)) {
            count = goodsRepo.countByCode(orgId, code, flag);
        } else {
            count = goodsRepo.countByCode(orgId, code, flag, excludeId);
        }
        if (count != null && count > 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断编号是否有效
     *
     * @return
     */
    public boolean isBarCodeExist(String orgId, String barCode) {
        Long count = goodsRepo.countByBarCode(orgId, barCode);
        if (count != null && count > 0) {
            return true;
        }
        return false;
    }

    /**
     * 获取某张单据下的所有货品
     *
     * @param vo
     * @param paramater
     * @return
     */
    public Page<GoodsVo> getGoods(GoodsVo vo, PageParamater paramater) {
        PageRequest pageRequest = getPageRequest(paramater);
        Page<Goods> goods = goodsRepo.findPageBy(vo.getBillId(), vo.getCode(), vo.getName(), vo.getSpec(), pageRequest);
        Page<GoodsVo> goodVos = getPageVos(goods, pageRequest);
        loadOtherPrice(vo.getBillType(), vo.getCustomerId(), vo.getSupplierId(), goodVos.getContent());
        return goodVos;
    }

    /**
     * 判断编号是否有效
     *
     * @param vo
     * @return
     */
    public RequestResult isCodeValid(GoodsVo vo) {
        String orgId = SecurityUtil.getCurrentOrgId();
        if (isCodeExist(orgId, vo.getCode(), vo.getFlag(), vo.getFid())) {
            return buildFailRequestResult("编号已存在");
        }
        return buildSuccessRequestResult();
    }

    /**
     * 模糊匹配查找货品(货品条形码，货品编号，货品名称)<br>
     *
     * @return
     */
    public List<GoodsVo> vagueSearch(GoodsVo vo) {
        String userId = SecurityUtil.getCurrentUserId();
        String orgId = SecurityUtil.getCurrentOrgId();
        String inputType = userAttrService.getInputType(userId);

        Splitter splitter = Splitter.on(",").omitEmptyStrings().trimResults();
        List<String> excludeIds = null;
        if (!Strings.isNullOrEmpty(vo.getNids())) {
            excludeIds = splitter.splitToList(vo.getNids());
        }

        List<Goods> entities = goodsRepo.findByVagueSearch(orgId, inputType, vo.getSearchKey(), vo.getSearchSize(), excludeIds);
        List<GoodsVo> vos = getVos(entities);
        loadOtherPrice(vo.getBillType(), vo.getCustomerId(), vo.getSupplierId(), vos);
        return getVos(entities);
    }

    /**
     * 设置相关货品的已使用标识为已使用过
     *
     * @param billDetails 仓库单据明细集合
     */
    @Transactional
    public void setGoodsUsed(List<WarehouseBillDetail> billDetails) {
        if (CollectionUtils.isNotEmpty(billDetails)) {
            for (WarehouseBillDetail detail : billDetails) {
                Goods goods = detail.getGoods();
                if (goods.getUseFlag() == Goods.USE_FLAG_UNUSED) {
                    goods.setUseFlag(Goods.USE_FLAG_USED);
                    goodsRepo.save(goods);
                }
            }
        }
    }

    /**
     * 根据编号查找
     *
     * @return
     */
    public Goods getByCode(String orgId, String code) {
        return goodsRepo.findTopByCode(orgId, code);
    }

    /**
     * 判断货品名称是否有重复
     *
     * @param goodsName
     * @return
     */
    public RequestResult checkGoodsName(String fid, String goodsName) {
        Long count = 0L;
        String orgId = SecurityUtil.getCurrentOrgId();
        if (Strings.isNullOrEmpty(fid)) {
            count = goodsRepo.countByGoodsName(goodsName, orgId);
        } else {
            count = goodsRepo.countByGoodsName(goodsName, fid, orgId);
        }

        if (count > 0) return buildFailRequestResult("货品名称重复");
        return buildSuccessRequestResult();
    }

	/**
	 * 删除货品
	 * @param id 货品ID
	 * @return
	 */
    @Transactional
	public RequestResult delete(String id){
		RequestResult result = new RequestResult();
		Goods goods = get(id);
		if(CollectionUtils.isNotEmpty(goods.getChilds())){
			result.setMessage("请先删除子货品!");
			result.setReturnCode(RequestResult.RETURN_FAILURE);
		}
		else if(goods.getUseFlag() == Goods.USE_FLAG_USED || subjectService.countByGoods(id) > 0){
			result.setMessage("无法删除，该货品已被使用!");
			result.setReturnCode(RequestResult.RETURN_FAILURE);
		}
		else{
			goodsRepo.delete(id);
		}
		
		goodsBarService.deleteByGoodsId(id);

        redisService.remove(getCacheKey());
		
		return result;
	}

    @Override
    public String getCacheName() {
        return BaseConstant.GOODS;
    }
    /**
     * 通过FID查询货品
     * @param fid
     * @return
     */
    public GoodsVo findGoodsById(String fid){
    	return getVo(goodsRepo.findGoodsById(fid,SecurityUtil.getCurrentOrgId()));
    	
    }
}
