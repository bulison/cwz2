package cn.fooltech.fool_ops.domain.basedata.service;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.Goods;
import cn.fooltech.fool_ops.domain.basedata.entity.GoodsPrice;
import cn.fooltech.fool_ops.domain.basedata.entity.GoodsSpec;
import cn.fooltech.fool_ops.domain.basedata.entity.Unit;
import cn.fooltech.fool_ops.domain.basedata.repository.GoodsPriceRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.GoodsRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.GoodsSpecRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.UnitRepository;
import cn.fooltech.fool_ops.domain.basedata.vo.GoodsPriceVo;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.validator.ValidatorUtils;
import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * <p>
 * 货品价格服务类
 * </p>
 *
 * @author xjh
 * @date 2016-10-26
 */
@Service
public class GoodsPriceService extends BaseService<GoodsPrice, GoodsPriceVo, String> {

    @Autowired
    private GoodsPriceRepository priceRepo;

    @Autowired
    private GoodsRepository goodsRepo;

    @Autowired
    private UnitRepository unitRepo;

    @Autowired
    private GoodsSpecRepository specRepo;

    @Override
    public CrudRepository<GoodsPrice, String> getRepository() {
        return priceRepo;
    }

    @Override
    public GoodsPriceVo getVo(GoodsPrice entity) {
        if (entity == null)
            return null;
        GoodsPriceVo vo = new GoodsPriceVo();
        vo.setFid(entity.getFid());
        vo.setCreateTime(DateUtilTools.time2String(entity.getCreateTime()));

        Goods goods = entity.getGoods();
        if (goods != null) {
            vo.setSpec(goods.getSpec());
            vo.setGoodsId(goods.getFid());
            vo.setGoodsCode(goods.getCode());
            vo.setGoodsName(goods.getName());
            vo.setBarCode(goods.getBarCode());
        }

        if (entity.getGoodsSpec() != null) {
            vo.setGoodsSpecId(entity.getGoodsSpec().getFid());
            vo.setGoodsSpecName(entity.getGoodsSpec().getName());
            vo.setGoodsSpecCode(entity.getGoodsSpec().getCode());
        }
        vo.setHeightestStock(NumberUtil.stripTrailingZeros(entity.getHeightestStock()));
        vo.setLowestPrice(NumberUtil.stripTrailingZeros(entity.getLowestPrice()));
        vo.setLowestStock(NumberUtil.stripTrailingZeros(entity.getLowestStock()));
        vo.setSalePrice(NumberUtil.stripTrailingZeros(entity.getSalePrice()));
        vo.setUnitId(entity.getUnit().getFid());
        vo.setUnitName(entity.getUnit().getName());
        vo.setUnitCode(entity.getUnit().getCode());
        vo.setSalesLowerLimit1(NumberUtil.stripTrailingZeros(entity.getSalesLowerLimit1()));
        vo.setSalesLowerLimit2(NumberUtil.stripTrailingZeros(entity.getSalesLowerLimit2()));
        vo.setSalesUpperLimit1(NumberUtil.stripTrailingZeros(entity.getSalesUpperLimit1()));
        vo.setSalesUpperLimit2(NumberUtil.stripTrailingZeros(entity.getSalesUpperLimit2()));
        vo.setPurchaseLowerLimit1(NumberUtil.stripTrailingZeros(entity.getPurchaseLowerLimit1()));
        vo.setPurchaseLowerLimit2(NumberUtil.stripTrailingZeros(entity.getPurchaseLowerLimit2()));
        vo.setPurchaseUpperLimit1(NumberUtil.stripTrailingZeros(entity.getPurchaseUpperLimit1()));
        vo.setPurchaseUpperLimit2(NumberUtil.stripTrailingZeros(entity.getPurchaseUpperLimit2()));
        vo.setCapacity(NumberUtil.stripTrailingZeros(entity.getCapacity()));
        vo.setPurchasingCycle(NumberUtil.stripTrailingZeros(entity.getPurchasingCycle()));
        vo.setUpdateTime(DateUtilTools.time2String(entity.getUpdateTime()));
        vo.setVipPrice(NumberUtil.stripTrailingZeros(entity.getVipPrice()));
        return vo;
    }

    /**
     * 获取货品的最低销售价
     *
     * @param goodsId     货品ID
     * @param unitId      货品单位ID
     * @param goodsSpecId 货品属性ID
     * @return
     * @author rqh
     */
    public BigDecimal getLowestPrice(String goodsId, String unitId, String goodsSpecId) {
        Assert.notNull(goodsId);
        Assert.notNull(unitId);

        GoodsPrice price = null;
        if (StringUtils.isNotBlank(goodsSpecId)) {
            price = priceRepo.findTopBy(goodsId, unitId, goodsSpecId);
        } else {
            price = priceRepo.findTopBy(goodsId, unitId);
        }

        if (price != null) {
            BigDecimal lowestPrice = price.getLowestPrice();
            return lowestPrice != null ? lowestPrice : BigDecimal.ZERO;
        }
        return BigDecimal.ZERO;
    }

    /**
     * 查询现金银行期初列表信息<br>
     * 默认为第一页，每页大小默认为10<br>
     *
     * @param vo
     */
    public Page<GoodsPriceVo> query(GoodsPriceVo vo, PageParamater pageParamater) {

        String orgId = SecurityUtil.getCurrentOrgId();
        Sort sort = new Sort(Direction.ASC, "goods.code");
        PageRequest pageRequest = getPageRequest(pageParamater, sort);
        String searchKey = vo.getSearchKey();

        if (Strings.isNullOrEmpty(searchKey)) {
            return getPageVos(priceRepo.findPageBy(orgId, pageRequest), pageRequest);
        } else {
            return getPageVos(priceRepo.findPageBy(orgId, searchKey, pageRequest), pageRequest);
        }
    }

    /**
     * 新增/编辑货品定价
     *
     * @param vo
     */
    public RequestResult save(GoodsPriceVo vo) {
        if (vo.getPurchaseLowerLimit1() == null) vo.setPurchaseLowerLimit1(BigDecimal.ZERO);
        if (vo.getPurchaseLowerLimit2() == null) vo.setPurchaseLowerLimit2(BigDecimal.ZERO);
        if (vo.getPurchaseUpperLimit1() == null) vo.setPurchaseUpperLimit1(BigDecimal.ZERO);
        if (vo.getPurchaseUpperLimit2() == null) vo.setPurchaseUpperLimit2(BigDecimal.ZERO);
        if (vo.getSalesLowerLimit1() == null && vo.getSalePrice() != null) vo.setSalesLowerLimit1(vo.getSalePrice());
        if (vo.getSalesLowerLimit2() == null && vo.getSalePrice() != null) vo.setSalesLowerLimit2(vo.getSalePrice());
        if (vo.getSalesUpperLimit1() == null && vo.getSalePrice() != null) vo.setSalesUpperLimit1(vo.getSalePrice());
        if (vo.getSalesUpperLimit2() == null && vo.getSalePrice() != null) vo.setSalesUpperLimit2(vo.getSalePrice());
        if (vo.getVipPrice() == null && vo.getSalePrice() != null) vo.setVipPrice(vo.getSalePrice());
        //Bean Validation 验证
        String inValid = ValidatorUtils.inValidMsg(vo);
        if (inValid != null) {
            return buildFailRequestResult(inValid);
        }

        RequestResult checkPrice = checkPriceValidation(vo);
        if (!checkPrice.isSuccess()) {
            return checkPrice;
        }

        GoodsPrice entity = new GoodsPrice();
        Goods goods = null;
        GoodsSpec goodsSpec = null;
        Unit unit = null;
        String orgId = SecurityUtil.getCurrentOrgId();

        if (StringUtils.isNotBlank(vo.getGoodsId())) {
            goods = goodsRepo.findOne(vo.getGoodsId());
        } else if (StringUtils.isNotBlank(vo.getGoodsCode())) {
            goods = goodsRepo.findTopByCode(orgId, vo.getGoodsCode());
        } else {
            return buildFailRequestResult("货品必填");
        }
        //设置货品id，用于判断是否重复添加相同货品
        vo.setGoodsId(goods.getFid());
        if (StringUtils.isNotBlank(vo.getUnitId())) {
            unit = unitRepo.findOne(vo.getUnitId());
        } else if (StringUtils.isNotBlank(vo.getGoodsCode())) {
            unit = unitRepo.findTopByCodeAndParent(orgId, vo.getUnitCode(),goods.getUnitGroup().getFid());
        } else {
            return buildFailRequestResult("货品单位必填");
        }

        if (StringUtils.isNotBlank(vo.getGoodsSpecId())) {
            goodsSpec = specRepo.findOne(vo.getGoodsSpecId());

        } else if (StringUtils.isNotBlank(vo.getGoodsSpecCode())) {
            goodsSpec = specRepo.findTopByCodeAndSpecGroupId(orgId, vo.getGoodsSpecCode(),goods.getGoodsSpec().getFid());
            Integer flag = goodsSpec.getFlag();
            if (flag == 0) return buildFailRequestResult("货品属性组必须是子项");
        } else {
            if (goods != null) {
                if (goods.getGoodsSpec() != null) {
                    return buildFailRequestResult("该货品属性必填");
                }
            }
        }
        //设置货品属性id，用于判断是否重复添加相同货品
        vo.setGoodsSpecId(goodsSpec != null ? goodsSpec.getFid() : "");
        boolean exist = isPriceExist(vo.getGoodsId(), vo.getGoodsSpecId(), vo.getFid());
        if (exist) {
            return buildFailRequestResult("同一货品+属性已定价");
        }

        if (StringUtils.isBlank(vo.getFid())) {
            entity.setCreateTime(Calendar.getInstance().getTime());
            entity.setCreator(SecurityUtil.getCurrentUser());
            entity.setOrg(SecurityUtil.getCurrentOrg());
            entity.setDept(SecurityUtil.getCurrentDept());

            entity.setGoods(goods);
            entity.setGoodsSpec(goodsSpec);
            entity.setUnit(unit);

            entity.setHeightestStock(vo.getHeightestStock());
            entity.setLowestPrice(vo.getLowestPrice());
            entity.setLowestStock(vo.getLowestStock());
            entity.setSalePrice(vo.getSalePrice());
            entity.setVipPrice(vo.getVipPrice());
            entity.setSalesLowerLimit1(vo.getSalesLowerLimit1());
            entity.setSalesLowerLimit2(vo.getSalesLowerLimit2());
            entity.setSalesUpperLimit1(vo.getSalesUpperLimit1());
            entity.setSalesUpperLimit2(vo.getSalesUpperLimit2());
            entity.setPurchaseLowerLimit2(vo.getPurchaseLowerLimit2());
            entity.setPurchaseLowerLimit1(vo.getPurchaseLowerLimit1());
            entity.setPurchaseUpperLimit1(vo.getPurchaseUpperLimit1());
            entity.setPurchaseUpperLimit2(vo.getPurchaseUpperLimit2());
            entity.setCapacity(vo.getCapacity());
            entity.setPurchasingCycle(vo.getPurchasingCycle());
            priceRepo.save(entity);

            //更新货品的已使用标识
            goods.setUseFlag(Goods.USE_FLAG_USED);
            goodsRepo.save(goods);

        } else {

            entity = priceRepo.findOne(vo.getFid());

            if (entity == null) {
                return buildFailRequestResult("该记录不存在或已被删除!");
            }

            String updateTime = DateUtilTools.time2String(entity.getUpdateTime());
            if (!vo.getUpdateTime().equals(updateTime)) {
                return buildFailRequestResult("数据已被其他用户修改");
            }

            entity.setGoods(goods);
            entity.setGoodsSpec(goodsSpec);
            entity.setUnit(unit);

            entity.setHeightestStock(vo.getHeightestStock());
            entity.setLowestPrice(vo.getLowestPrice());
            entity.setLowestStock(vo.getLowestStock());
            entity.setSalePrice(vo.getSalePrice());
            entity.setVipPrice(vo.getVipPrice());

            entity.setSalesLowerLimit1(vo.getSalesLowerLimit1());
            entity.setSalesLowerLimit2(vo.getSalesLowerLimit2());
            entity.setSalesUpperLimit1(vo.getSalesUpperLimit1());
            entity.setSalesUpperLimit2(vo.getSalesUpperLimit2());
            entity.setPurchaseLowerLimit2(vo.getPurchaseLowerLimit2());
            entity.setPurchaseLowerLimit1(vo.getPurchaseLowerLimit1());
            entity.setPurchaseUpperLimit1(vo.getPurchaseUpperLimit1());
            entity.setPurchaseUpperLimit2(vo.getPurchaseUpperLimit2());
            entity.setCapacity(vo.getCapacity());
            entity.setPurchasingCycle(vo.getPurchasingCycle());

            priceRepo.save(entity);
        }

        return buildSuccessRequestResult();
    }

    /**
     * 判断价格是否已存在
     *
     * @param goodsId
     * @param specId
     * @param excludeId
     * @return
     */
    public boolean isPriceExist(String goodsId, String specId, String excludeId) {
        Long count = priceRepo.countByGoodsIdAndSpecId(goodsId, specId, excludeId);
        if (count != null && count > 0) {
            return true;
        }
        return false;
    }

    /**
     * 检查数据合法性
     * 销售二级价上限 >= 销售一级价上限 >= 销售价 >= 会员价 >= 销售一级价下限 >= 销售二级价下限 >= 最低售价
     * 采购二级价上限 >= 采购一级价上限 >= 采购一级价下限 >= 采购二级价下限
     */
    private RequestResult checkPriceValidation(GoodsPriceVo vo) {

        if (vo.getSalesUpperLimit2().compareTo(vo.getSalesUpperLimit1()) < 0) {
            return buildFailRequestResult("价格条件（销售二级价上限  >= 销售一级价上限）不满足不能保存");
        }
        if (vo.getSalesUpperLimit1().compareTo(vo.getSalePrice()) < 0) {
            return buildFailRequestResult("价格条件（销售一级价上限  >= 销售价）不满足不能保存");
        }
        if (vo.getSalePrice().compareTo(vo.getVipPrice()) < 0) {
            return buildFailRequestResult("价格条件（销售价  >= 会员价）不满足不能保存");
        }
        if (vo.getVipPrice().compareTo(vo.getSalesLowerLimit1()) < 0) {
            return buildFailRequestResult("价格条件（会员价  >= 销售一级价下限）不满足不能保存");
        }
        if (vo.getSalesLowerLimit1().compareTo(vo.getSalesLowerLimit2()) < 0) {
            return buildFailRequestResult("价格条件（销售一级价下限  >= 销售二级价下限）不满足不能保存");
        }
        if (vo.getSalesLowerLimit2().compareTo(vo.getLowestPrice()) < 0) {
            return buildFailRequestResult("价格条件（销售二级价下限  >= 最低售价）不满足不能保存");
        }
        if (vo.getPurchaseUpperLimit2().compareTo(vo.getPurchaseUpperLimit1()) < 0) {
            return buildFailRequestResult("价格条件（采购二级价上限  >= 采购一级价上限）不满足不能保存");
        }
        if (vo.getPurchaseUpperLimit1().compareTo(vo.getPurchaseLowerLimit1()) < 0) {
            return buildFailRequestResult("价格条件（采购一级价上限  >= 采购一级价下限）不满足不能保存");
        }
        if (vo.getPurchaseLowerLimit1().compareTo(vo.getPurchaseLowerLimit2()) < 0) {
            return buildFailRequestResult("价格条件（采购一级价下限  >= 采购二级价下限）不满足不能保存");
        }
        return buildSuccessRequestResult();
    }

    /**
     * 根据货品ID和属性ID查询
     * @param goodsId
     * @param specId
     * @return
     */
    public GoodsPrice findByGoodsIdAndSpecId(String goodsId, String specId){
        return priceRepo.findTopBy(goodsId, specId);
    }
	/**
	 * 获取货品定价
	 * @param goodsId
	 * @param goodsSpecId
	 * @return
	 */
	public GoodsPrice getByGoodSpec(String goodsId, String goodsSpecId){
		return priceRepo.findByGoodsIdAndSpecId(goodsId, goodsSpecId);
	}
}
