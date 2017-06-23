package cn.fooltech.fool_ops.domain.warehouse.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.basedata.entity.Goods;
import cn.fooltech.fool_ops.domain.basedata.entity.GoodsPrice;
import cn.fooltech.fool_ops.domain.basedata.entity.GoodsSpec;
import cn.fooltech.fool_ops.domain.basedata.repository.GoodsPriceRepository;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBillDetail;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.SystemConstant;


/**
 * <p>仓库单据审核时，根据权限校验货品价格的工具类</p>
 */
@Service
public class PriceVerifyService {
	
	@Autowired
	private GoodsPriceRepository goodsPriceRepo;
	
	/**
	 * 是否忽略价格的校验
	 * @param price 货品定价的价格
	 * @return
	 */
	private boolean isIgnore(BigDecimal price){
		if(price == null){
			return true;
		}
		if(price.compareTo(BigDecimal.ZERO) == 0){
			return true;
		}
		return false;
	}
	
	/**
	 * 计算记账单价
	 * @param unitPrice 单价
	 * @param scale 换算关系
	 * @return
	 */
	private BigDecimal calculateAccountUintPrice(BigDecimal unitPrice, BigDecimal scale){
		return unitPrice.divide(scale, WarehouseBillDetailService.ACCOUNT_UINT_PRICE_SCALE, BigDecimal.ROUND_HALF_UP);
	}
	
	/**
	 * 根据权限校验价格范围
	 * @param user 当前用户
	 * @param bill 仓库单据
	 * @return
	 */
	public RequestResult verify(String userId, WarehouseBill bill){
		List<WarehouseBillDetail> billDetails =  bill.getDetails();
		if(CollectionUtils.isEmpty(billDetails)){
			return new RequestResult();
		}
		
		Set<String> permitCodes = SecurityUtil.getPermitCodes(userId);
		
		switch(bill.getBillType()){
			//采购申请、采购订单、采购入库、采购退货
			case WarehouseBuilderCodeHelper.cgsqd:
			case WarehouseBuilderCodeHelper.cgdd:
			case WarehouseBuilderCodeHelper.cgrk:
			case WarehouseBuilderCodeHelper.cgth:{
				for(WarehouseBillDetail billDetail : billDetails){
					//单价明细的换算关系
					BigDecimal initScale = billDetail.getScale();
					//单据明细的单价
					BigDecimal unitPrice = billDetail.getUnitPrice();
					//单据明细的记账单价
					BigDecimal accountUintPrice = billDetail.getAccountUintPrice();
					
					//货品、货品属性
					Goods goods = billDetail.getGoods();
					GoodsSpec goodsSpec = billDetail.getGoodsSpec();
					
					//货品+属性名称
					String goodsName = "";
					if(goodsSpec == null){
						goodsName = goods.getName();
					}
					else{
						goodsName = "%s(%s)";
						goodsName = String.format(goodsName, goods.getName(), goodsSpec.getName());
					}
					
					//货品定价
					String goodsSpecId = goodsSpec == null ? null : goodsSpec.getFid();
					GoodsPrice goodsPrice = goodsPriceRepo.findByGoodsIdAndSpecId(goods.getFid(), goodsSpecId);
					if(goodsPrice == null){
						continue;
					}
					
					//货品定价的换算关系
					BigDecimal goodsPriceScale = goodsPrice.getUnit().getScale();
					
					if(permitCodes.contains(SystemConstant.PERMISSION_PURCHASE_ZERO_PRICE)){
						if(unitPrice.compareTo(BigDecimal.ZERO) == 0){
							continue;
						}
						else{
							//采购二级价下限
							if(!isIgnore(goodsPrice.getPurchaseLowerLimit2())){
								BigDecimal purchaseLowerLimit2 = calculateAccountUintPrice(goodsPrice.getPurchaseLowerLimit2(), goodsPriceScale);
								if(accountUintPrice.compareTo(purchaseLowerLimit2) < 0 ){
									return new RequestResult(RequestResult.RETURN_FAILURE, 
											goodsName + "单价必须大于等于" + NumberUtil.stripTrailingZeros(initScale.multiply(purchaseLowerLimit2), 4));
								}
							}
							//采购二级价上限
							if(!isIgnore(goodsPrice.getPurchaseUpperLimit2())){
								BigDecimal purchaseUpperLimit2 = calculateAccountUintPrice(goodsPrice.getPurchaseUpperLimit2(), goodsPriceScale);
								if(accountUintPrice.compareTo(purchaseUpperLimit2) > 0){
									return new RequestResult(RequestResult.RETURN_FAILURE, 
											goodsName + "单价必须小于等于" + NumberUtil.stripTrailingZeros(initScale.multiply(purchaseUpperLimit2), 4));
								}
							}
						}
					}
					else if(permitCodes.contains(SystemConstant.PERMISSION_PURCHASE_SECOND_PRICE)){
						//采购二级价下限
						if(!isIgnore(goodsPrice.getPurchaseLowerLimit2())){
							BigDecimal purchaseLowerLimit2 = calculateAccountUintPrice(goodsPrice.getPurchaseLowerLimit2(), goodsPriceScale);
							if(accountUintPrice.compareTo(purchaseLowerLimit2) < 0 ){
								return new RequestResult(RequestResult.RETURN_FAILURE, 
											goodsName + "单价必须大于等于" + NumberUtil.stripTrailingZeros(initScale.multiply(purchaseLowerLimit2), 4));
							}
						}
						//采购二级价上限
						if(!isIgnore(goodsPrice.getPurchaseUpperLimit2())){
							BigDecimal purchaseUpperLimit2 = calculateAccountUintPrice(goodsPrice.getPurchaseUpperLimit2(), goodsPriceScale);
							if(accountUintPrice.compareTo(purchaseUpperLimit2) > 0){
								return new RequestResult(RequestResult.RETURN_FAILURE, 
											goodsName + "单价必须小于等于" + NumberUtil.stripTrailingZeros(initScale.multiply(purchaseUpperLimit2), 4));
							}
						}
					}
					else if(permitCodes.contains(SystemConstant.PERMISSION_PURCHASE_FIRST_PRICE)){
						//采购一级价下限
						if(!isIgnore(goodsPrice.getPurchaseLowerLimit1())){
							BigDecimal purchaseLowerLimit1 = calculateAccountUintPrice(goodsPrice.getPurchaseLowerLimit1(), goodsPriceScale);
							if(accountUintPrice.compareTo(purchaseLowerLimit1) < 0){
								return new RequestResult(RequestResult.RETURN_FAILURE, 
											goodsName + "单价必须大于等于" + NumberUtil.stripTrailingZeros(initScale.multiply(purchaseLowerLimit1), 4));
							}
						}
						//采购一级价上限
						if(!isIgnore(goodsPrice.getPurchaseUpperLimit1())){
							BigDecimal purchaseUpperLimit1 = calculateAccountUintPrice(goodsPrice.getPurchaseUpperLimit1(), goodsPriceScale);
							if(accountUintPrice.compareTo(purchaseUpperLimit1) > 0){
								return new RequestResult(RequestResult.RETURN_FAILURE, 
											goodsName + "单价必须小于等于" + NumberUtil.stripTrailingZeros(initScale.multiply(purchaseUpperLimit1), 4));
							}
						}
					}
					else{
						return new RequestResult(RequestResult.RETURN_FAILURE, "缺少价格审核权限，请联系企业管理员进行授权!");
					}
				}
				break;
			}
			
			//销售订单、销售出货、销售退货
			case WarehouseBuilderCodeHelper.xsdd:
			case WarehouseBuilderCodeHelper.xsch:
			case WarehouseBuilderCodeHelper.xsth:{
				for(WarehouseBillDetail billDetail : billDetails){
					//单价明细的换算关系
					BigDecimal initScale = billDetail.getScale();
					//单据明细的单价
					BigDecimal unitPrice = billDetail.getUnitPrice();
					//单据明细的记账单价
					BigDecimal accountUintPrice = billDetail.getAccountUintPrice();
					
					//货品、货品属性
					Goods goods = billDetail.getGoods();
					GoodsSpec goodsSpec = billDetail.getGoodsSpec();
					
					//货品+属性名称
					String goodsName = "";
					if(goodsSpec == null){
						goodsName = goods.getName();
					}
					else{
						goodsName = "%s(%s)";
						goodsName = String.format(goodsName, goods.getName(), goodsSpec.getName());
					}
					
					//货品定价
					String goodsSpecId = goodsSpec == null ? null : goodsSpec.getFid();
					GoodsPrice goodsPrice = goodsPriceRepo.findByGoodsIdAndSpecId(goods.getFid(), goodsSpecId);
					if(goodsPrice == null){
						continue;
					}
					
					//货品定价的换算关系
					BigDecimal goodsPriceScale = goodsPrice.getUnit().getScale();
					
					if(permitCodes.contains(SystemConstant.PERMISSION_SELL_ZERO_PRICE)){
						if(unitPrice.compareTo(BigDecimal.ZERO) == 0){
							continue;
						}
						else{
							//最低售价
							if(!isIgnore(goodsPrice.getLowestPrice())){
								BigDecimal lowestUintPrice = calculateAccountUintPrice(goodsPrice.getLowestPrice(), goodsPriceScale);
								if(accountUintPrice.compareTo(lowestUintPrice) < 0 ){
									return new RequestResult(RequestResult.RETURN_FAILURE, 
												goodsName + "单价必须大于等于" + NumberUtil.stripTrailingZeros(initScale.multiply(lowestUintPrice), 4));
								}
							}
						}
					}
					else if(permitCodes.contains(SystemConstant.PERMISSION_SELL_SECOND_PRICE)){
						//销售二级价下限
						if(!isIgnore(goodsPrice.getSalesLowerLimit2())){
							BigDecimal salesLowerLimit2 = calculateAccountUintPrice(goodsPrice.getSalesLowerLimit2(), goodsPriceScale);
							if(accountUintPrice.compareTo(salesLowerLimit2) < 0 ){
								return new RequestResult(RequestResult.RETURN_FAILURE, 
											goodsName + "单价必须大于等于" + NumberUtil.stripTrailingZeros(initScale.multiply(salesLowerLimit2), 4));
							}
						}
						//销售二级价上限
						if(!isIgnore(goodsPrice.getSalesUpperLimit2())){
							BigDecimal salesUpperLimit2 = calculateAccountUintPrice(goodsPrice.getSalesUpperLimit2(), goodsPriceScale);
							if(accountUintPrice.compareTo(salesUpperLimit2) > 0){
								return new RequestResult(RequestResult.RETURN_FAILURE, 
											goodsName + "单价必须小于等于" + NumberUtil.stripTrailingZeros(initScale.multiply(salesUpperLimit2), 4));
							}
						}
					}
					else if(permitCodes.contains(SystemConstant.PERMISSION_SELL_FIRST_PRICE)){
						//销售一级价下限
						if(!isIgnore(goodsPrice.getSalesLowerLimit1())){
							BigDecimal salesLowerLimit1 = calculateAccountUintPrice(goodsPrice.getSalesLowerLimit1(), goodsPriceScale);
							if(accountUintPrice.compareTo(salesLowerLimit1) < 0){
								return new RequestResult(RequestResult.RETURN_FAILURE, 
											goodsName + "单价必须大于等于" + NumberUtil.stripTrailingZeros(initScale.multiply(salesLowerLimit1), 4));
							}
						}
						//销售一级价上限
						if(!isIgnore(goodsPrice.getSalesUpperLimit1())){
							BigDecimal salesUpperLimit1 = calculateAccountUintPrice(goodsPrice.getSalesUpperLimit1(), goodsPriceScale);
							if(accountUintPrice.compareTo(salesUpperLimit1) > 0){
								return new RequestResult(RequestResult.RETURN_FAILURE, 
											goodsName + "单价必须小于等于" + NumberUtil.stripTrailingZeros(initScale.multiply(salesUpperLimit1), 4));
							}
						}
					}
					else{
						return new RequestResult(RequestResult.RETURN_FAILURE, "缺少价格审核权限，请联系企业管理员进行授权!");
					}
				}
				break;
			}
		}
		return new RequestResult();
	}
	
}
