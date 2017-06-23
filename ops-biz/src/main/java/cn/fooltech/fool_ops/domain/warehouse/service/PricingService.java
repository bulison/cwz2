package cn.fooltech.fool_ops.domain.warehouse.service;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.Goods;
import cn.fooltech.fool_ops.domain.basedata.entity.GoodsPrice;
import cn.fooltech.fool_ops.domain.basedata.entity.GoodsSpec;
import cn.fooltech.fool_ops.domain.basedata.service.GoodsPriceService;
import cn.fooltech.fool_ops.domain.basedata.service.GoodsService;
import cn.fooltech.fool_ops.domain.basedata.service.GoodsSpecService;
import cn.fooltech.fool_ops.domain.basedata.service.UnitService;
import cn.fooltech.fool_ops.domain.payment.service.PaymentCheckService;
import cn.fooltech.fool_ops.domain.period.entity.StockPeriod;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.warehouse.entity.*;
import cn.fooltech.fool_ops.domain.warehouse.repository.PricingRepository;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.domain.warehouse.vo.PricingDetailVo;
import cn.fooltech.fool_ops.domain.warehouse.vo.PricingVo;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.SystemConstant;
import net.sf.json.JSONArray;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;



/**
 * <p>单据核价网页服务类</p>
 * @author rqh
 * @version 1.0
 * @date 2016年4月5日
 */
@Service("ops.PricingService")
public class PricingService extends BaseService<Pricing, PricingVo, String> {

	@Autowired
	private PricingRepository pricingRepository;
	
	/**
	 * 仓储单据服务类
	 */
	@Resource(name = "ops.WarehouseBillService")
	private WarehouseBillService billService;
	
	/**
	 * 仓储单据明细服务类
	 */
	@Autowired
	private WarehouseBillDetailService billDetailService;
	
	/**
	 * 进库记录服务类
	 */
	@Autowired
	private InStorageService inStorageService;
	
	/**
	 * 出库记录服务类
	 */
	@Autowired
	private OutStorageService outStorageService;
	
	/**
	 * 单位服务类
	 */
	@Autowired
	private UnitService unitService;
	
	/**
	 * 货品服务类
	 */
	@Autowired
	private GoodsService goodsService;
	
	/**
	 * 货品属性服务类
	 */
	@Autowired
	private GoodsSpecService goodsSpecService;
	
	/**
	 * 货品定价服务类
	 */
	@Autowired
	private GoodsPriceService goodsPriceService;
	
	/**
	 * 期间总库存金额服务类
	 */
	@Autowired
	private PeriodAmountService periodAmountService;
	
	/**
	 * 收付款单勾对服务类
	 */
	@Autowired
	private PaymentCheckService paymentCheckService;
	
	
	/**
	 * 判断数据是否有效
	 * @param vo
	 * @return
	 */
	private boolean isValid(PricingDetailVo vo){
		String afterPrice = vo.getAfterPrice();
		String afterAmount = vo.getAfterAmount();
		if(StringUtils.isBlank(afterPrice) || StringUtils.isBlank(afterAmount)){
			return false;
		}
		return true;
	}
	
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
	 * 核算价格
	 * @param vo
	 * @return
	 */
	@Transactional
	public RequestResult checkPrice(PricingVo vo){
		WarehouseBill bill = billService.get(vo.getBillId());
		List<PricingDetailVo> detailVos = getVos(vo);
		
		StockPeriod stockPeriod = bill.getStockPeriod();
		if(stockPeriod.getCheckoutStatus() == StockPeriod.CHECKED){
			return new RequestResult(RequestResult.RETURN_FAILURE, "仓储会计期间已结账，无法进行核价操作!");
		} 
		
		if(bill.getRecordStatus() != WarehouseBill.STATUS_AUDITED){
			return new RequestResult(RequestResult.RETURN_FAILURE, "单据处于非已审核状态，无法进行核价操作!");
		}
		
		if(paymentCheckService.countByBillId(bill.getFid()) > 0){
			return new RequestResult(RequestResult.RETURN_FAILURE, "该单据已有收付款记录，无法进行核价操作!");
		}
		
		//用户身份校验
		User user = null;
		if(StringUtils.isNotBlank(vo.getUserCode()) || StringUtils.isNotBlank(vo.getPassword())){
			user = SecurityUtil.getUser(vo.getUserCode(), vo.getPassword());
			if(user == null){
				return new RequestResult(RequestResult.RETURN_FAILURE, "用户名或者密码不正确!");
			}
			if(user.getFid().equals(SecurityUtil.getCurrentUserId())){
				return new RequestResult(RequestResult.RETURN_FAILURE, "请输入其他用户的账号信息!");
			}
		}
		else{
			user = SecurityUtil.getCurrentUser();
		}

		//判断用户是否有核价权限
		if(!SecurityUtil.isPermit(user.getFid(), SystemConstant.PERMISSION_PRICING)){
			return new RequestResult(RequestResult.RETURN_FAILURE, "无权限进行核价操作，请联系企业管理员进行授权!");
		}
		
		//判断输入的价格范围是否该用户的价格范围之内
		RequestResult requestResult = verifyPriceByPermission(user, bill, detailVos);
		if(requestResult.getReturnCode() == RequestResult.RETURN_FAILURE){
			return requestResult;
		}
		
		switch(bill.getBillType()){
			//采购入库、销售退货
			case WarehouseBuilderCodeHelper.cgrk:
			case WarehouseBuilderCodeHelper.xsth:{
				BigDecimal totalAfterAccountAmount = BigDecimal.ZERO; //单据修改后的总结账金额
				for(PricingDetailVo detailVo : detailVos){
					//单据明细
					WarehouseBillDetail billDetail = billDetailService.get(detailVo.getBillDetailId());
					if(!isValid(detailVo)){
						totalAfterAccountAmount = totalAfterAccountAmount.add(billDetail.getType());
						continue;
					}
					
					//修改后的单价
					BigDecimal afterUnitPrice = NumberUtil.toBigDeciaml(detailVo.getAfterPrice());
					//修改后的记账金额
					BigDecimal afterAccountAmount = NumberUtil.toBigDeciaml(detailVo.getAfterAmount());
					//修改后的记账单价
					BigDecimal afterAccountUintPrice = calculateAccountUintPrice(afterUnitPrice, billDetail.getScale());
					totalAfterAccountAmount = totalAfterAccountAmount.add(afterAccountAmount);
					
					//修改前的单价
					BigDecimal beforePrice = NumberUtil.toBigDeciaml(detailVo.getBeforePrice());
//					if(beforePrice.compareTo(afterUnitPrice) == 0){
//						continue;
//					}
					
					//维护单据明细
					billDetail.setType(afterAccountAmount);
					billDetail.setUnitPrice(afterUnitPrice);
					billDetail.setAccountUintPrice(afterAccountUintPrice);
					billDetailService.save(billDetail);
					
					//维护入库记录
					BigDecimal inStorageBeforeAmount = BigDecimal.ZERO; //入库记录修改前的金额
					InStorage inStorage = inStorageService.getRecord(detailVo.getBillDetailId(), detailVo.getGoodsId(), detailVo.getGoodsSpecId());
					if(inStorage != null){
						inStorageBeforeAmount = inStorage.getAccountAmount();
						inStorage.setAccountUintPrice(afterAccountUintPrice);
						inStorage.setAccountAmount(afterAccountAmount);
						inStorageService.save(inStorage);
					}
					
					//维护出库记录
					List<OutStorage> list = outStorageService.getRecordByDetailIn(detailVo.getBillDetailId(), detailVo.getGoodsId(), detailVo.getGoodsSpecId());
					//出库记录修改前的总入库记账金额
					BigDecimal beforeTotalAmountIn = BigDecimal.ZERO;
					//出库记录修改后的总入库记账金额
					BigDecimal afterTotalAmountIn = BigDecimal.ZERO;
					if(CollectionUtils.isNotEmpty(list)){
						for(OutStorage outStorage : list){
							InStorage inStore = outStorage.getInStorage();
							//新入库记账单价
							BigDecimal accountUintPrice = inStore.getAccountUintPrice();
							//出库记账数量
							BigDecimal accountQuentity = outStorage.getAccountQuentity(); 
							//出库记录修改后的入库记账金额
							BigDecimal afterAmountIn = accountUintPrice.multiply(accountQuentity);
							
							beforeTotalAmountIn = beforeTotalAmountIn.add(outStorage.getAmountIn());
							afterTotalAmountIn = afterTotalAmountIn.add(afterAmountIn);
							
							outStorage.setAmountIn(afterAmountIn);
							outStorage.setInPrice(accountUintPrice);
							outStorageService.save(outStorage);
						}
						
						//处理入库的余额问题
						InStorage inStore = list.get(0).getInStorage();
						BigDecimal totalOut = inStore.getTotalOut();
						BigDecimal accountQuentity = inStore.getAccountQuentity();
						if(accountQuentity.compareTo(totalOut) == 0 && accountQuentity.compareTo(BigDecimal.ZERO) != 0){
							OutStorage lastOutStorage = list.get(list.size() - 1); //最后一条出库记录
							BigDecimal amountIn = lastOutStorage.getAmountIn();
							amountIn = amountIn.add(inStore.getAccountAmount());
							amountIn = amountIn.subtract(afterTotalAmountIn);
							lastOutStorage.setAmountIn(amountIn);
							outStorageService.save(lastOutStorage);
							
							afterTotalAmountIn = inStore.getAccountAmount();
						}
					}
					
					//维护期间总库存
					PeriodAmount periodAmount = periodAmountService.existRecord(stockPeriod.getFid(), detailVo.getGoodsId(), detailVo.getGoodsSpecId());
					//本期入库金额
					BigDecimal inAmount = periodAmount.getInAmount();
					inAmount = inAmount.add(afterAccountAmount);
					inAmount = inAmount.subtract(inStorageBeforeAmount);
					//本期出库金额
					BigDecimal outAmount = periodAmount.getOutAmount();
					outAmount = outAmount.add(afterTotalAmountIn);
					outAmount = outAmount.subtract(beforeTotalAmountIn);
					//本期结存金额
					BigDecimal accountAmount = periodAmount.getPreAmount();
					accountAmount = accountAmount.add(inAmount);
					accountAmount = accountAmount.subtract(outAmount);
					
					periodAmount.setInAmount(inAmount);
					periodAmount.setOutAmount(outAmount);
					periodAmount.setAccountAmount(accountAmount);
					periodAmountService.save(periodAmount);
				}
				
				//维护单据主表的合计金额
				bill.setTotalAmount(totalAfterAccountAmount);
				billService.save(bill);
				
				//添加核价日志记录
				savePricingRecord(detailVos);
				
				break;
			}
			
			//采购退货、销售出货
			case WarehouseBuilderCodeHelper.cgth: 
			case WarehouseBuilderCodeHelper.xsch:{
				BigDecimal totalAfterAccountAmount = BigDecimal.ZERO; //单据修改后的总结账金额
				for(PricingDetailVo detailVo : detailVos){
					//单据明细
					WarehouseBillDetail billDetail = billDetailService.get(detailVo.getBillDetailId());
					if(!isValid(detailVo)){
						totalAfterAccountAmount = totalAfterAccountAmount.add(billDetail.getType());
						continue;
					}
					
					//修改后的单价
					BigDecimal afterUnitPrice = NumberUtil.toBigDeciaml(detailVo.getAfterPrice());
					//修改后的记账金额
					BigDecimal afterAccountAmount = NumberUtil.toBigDeciaml(detailVo.getAfterAmount());
					//修改后的记账单价
					BigDecimal afterAccountUintPrice = calculateAccountUintPrice(afterUnitPrice, billDetail.getScale());
					totalAfterAccountAmount = totalAfterAccountAmount.add(afterAccountAmount);
					
					//修改前的单价
					BigDecimal beforePrice = NumberUtil.toBigDeciaml(detailVo.getBeforePrice());
					if(beforePrice.compareTo(afterUnitPrice) == 0){
						continue;
					}
					
					//维护单据明细
					billDetail.setType(afterAccountAmount);
					billDetail.setUnitPrice(afterUnitPrice);
					billDetail.setAccountUintPrice(afterAccountUintPrice);
					billDetailService.save(billDetail);
					
					//维护出库记录
					List<OutStorage> list = outStorageService.getRecordByDetailOut(detailVo.getBillDetailId(), detailVo.getGoodsId(), detailVo.getGoodsSpecId());
					if(CollectionUtils.isNotEmpty(list)){
						BigDecimal afterTotalOutAccountAmount = BigDecimal.ZERO; //汇总出库记录修改后的总结账金额
						for(OutStorage outStorage : list){
							BigDecimal accountQuentity = outStorage.getAccountQuentity();
							BigDecimal newAccountAmount =  accountQuentity.multiply(afterAccountUintPrice);
							outStorage.setAccountUintPrice(afterAccountUintPrice);
							outStorage.setAccountAmount(newAccountAmount);
							outStorageService.save(outStorage);
							
							afterTotalOutAccountAmount = afterTotalOutAccountAmount.add(newAccountAmount);
						}
						
						//处理入库的余额问题
						OutStorage lastOutStorage = list.get(list.size() - 1); //最后一条出库记录
						BigDecimal accountAmount = lastOutStorage.getAccountAmount();
						accountAmount = accountAmount.add(afterAccountAmount);
						accountAmount = accountAmount.subtract(afterTotalOutAccountAmount);
						lastOutStorage.setAccountAmount(accountAmount);
						outStorageService.save(lastOutStorage);
					}
				}
				
				//维护单据主表的合计金额
				bill.setTotalAmount(totalAfterAccountAmount);
				billService.save(bill);
				
				//添加核价日志记录
				savePricingRecord(detailVos);
				
				break;
			}
		}
		return new RequestResult();
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
	 * @param user
	 * @param bill
	 * @param detailVos
	 * @return
	 */
	private RequestResult verifyPriceByPermission(User user, WarehouseBill bill, List<PricingDetailVo> detailVos){
		switch(bill.getBillType()){
			//采购入库、采购退货
			case WarehouseBuilderCodeHelper.cgrk:
			case WarehouseBuilderCodeHelper.cgth:{
				for(PricingDetailVo detailVo : detailVos){
					if(!isValid(detailVo)){
						continue;
					}
					
					WarehouseBillDetail billDetail = billDetailService.get(detailVo.getBillDetailId());
					//单价明细原本的换算关系
					BigDecimal initScale = billDetail.getScale();
					//修改后的单价
					BigDecimal afterUnitPrice = NumberUtil.toBigDeciaml(detailVo.getAfterPrice());
					//修改后的记账单价
					BigDecimal afterAccountUintPrice = calculateAccountUintPrice(afterUnitPrice, initScale);
					
					//货品定价
					GoodsPrice goodsPrice = goodsPriceService.findByGoodsIdAndSpecId(detailVo.getGoodsId(), detailVo.getGoodsSpecId());
					if(goodsPrice == null){
						continue;
					}
					
					//货品定价的换算关系
					BigDecimal goodsPriceScale = goodsPrice.getUnit().getScale();
					
					//货品名称
					String goodsName = "";
					Goods goods = goodsService.get(detailVo.getGoodsId());
					if(StringUtils.isBlank(detailVo.getGoodsSpecId())){
						goodsName = goods.getName();
					}
					else{
						GoodsSpec goodsSpec = goodsSpecService.get(detailVo.getGoodsSpecId());
						goodsName = "%s(%s)";
						goodsName = String.format(goodsName, goods.getName(), goodsSpec.getName());
					}
						
					if(SecurityUtil.isPermit(user.getFid(), SystemConstant.PERMISSION_PURCHASE_ZERO_PRICE)){
						if(afterUnitPrice.compareTo(BigDecimal.ZERO) == 0){
							continue;
						}
						else{
							//采购二级价下限
							if(!isIgnore(goodsPrice.getPurchaseLowerLimit2())){
								BigDecimal purchaseLowerLimit2 = calculateAccountUintPrice(goodsPrice.getPurchaseLowerLimit2(), goodsPriceScale);
								if(afterAccountUintPrice.compareTo(purchaseLowerLimit2) < 0 ){
									return new RequestResult(RequestResult.RETURN_FAILURE, 
											goodsName + "单价必须大于等于" + NumberUtil.stripTrailingZeros(initScale.multiply(purchaseLowerLimit2), 4));
								}
							}
							//采购二级价上限
							if(!isIgnore(goodsPrice.getPurchaseUpperLimit2())){
								BigDecimal purchaseUpperLimit2 = calculateAccountUintPrice(goodsPrice.getPurchaseUpperLimit2(), goodsPriceScale);
								if(afterAccountUintPrice.compareTo(purchaseUpperLimit2) > 0){
									return new RequestResult(RequestResult.RETURN_FAILURE, 
											goodsName + "单价必须小于等于" + NumberUtil.stripTrailingZeros(initScale.multiply(purchaseUpperLimit2), 4));
								}
							}
						}
					}
					else if(SecurityUtil.isPermit(user.getFid(), SystemConstant.PERMISSION_PURCHASE_SECOND_PRICE)){
						//采购二级价下限
						if(!isIgnore(goodsPrice.getPurchaseLowerLimit2())){
							BigDecimal purchaseLowerLimit2 = calculateAccountUintPrice(goodsPrice.getPurchaseLowerLimit2(), goodsPriceScale);
							if(afterAccountUintPrice.compareTo(purchaseLowerLimit2) < 0 ){
								return new RequestResult(RequestResult.RETURN_FAILURE, 
											goodsName + "单价必须大于等于" + NumberUtil.stripTrailingZeros(initScale.multiply(purchaseLowerLimit2), 4));
							}
						}
						//采购二级价上限
						if(!isIgnore(goodsPrice.getPurchaseUpperLimit2())){
							BigDecimal purchaseUpperLimit2 = calculateAccountUintPrice(goodsPrice.getPurchaseUpperLimit2(), goodsPriceScale);
							if(afterAccountUintPrice.compareTo(purchaseUpperLimit2) > 0){
								return new RequestResult(RequestResult.RETURN_FAILURE, 
											goodsName + "单价必须小于等于" + NumberUtil.stripTrailingZeros(initScale.multiply(purchaseUpperLimit2), 4));
							}
						}
					}
					else if(SecurityUtil.isPermit(user.getFid(), SystemConstant.PERMISSION_PURCHASE_FIRST_PRICE)){
						//采购一级价下限
						if(!isIgnore(goodsPrice.getPurchaseLowerLimit1())){
							BigDecimal purchaseLowerLimit1 = calculateAccountUintPrice(goodsPrice.getPurchaseLowerLimit1(), goodsPriceScale);
							if(afterAccountUintPrice.compareTo(purchaseLowerLimit1) < 0){
								return new RequestResult(RequestResult.RETURN_FAILURE, 
											goodsName + "单价必须大于等于" + NumberUtil.stripTrailingZeros(initScale.multiply(purchaseLowerLimit1), 4));
							}
						}
						//采购一级价上限
						if(!isIgnore(goodsPrice.getPurchaseUpperLimit1())){
							BigDecimal purchaseUpperLimit1 = calculateAccountUintPrice(goodsPrice.getPurchaseUpperLimit1(), goodsPriceScale);
							if(afterAccountUintPrice.compareTo(purchaseUpperLimit1) > 0){
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
			
			//销售出货、销售退货
			case WarehouseBuilderCodeHelper.xsch:
			case WarehouseBuilderCodeHelper.xsth:{
				for(PricingDetailVo detailVo : detailVos){
					if(!isValid(detailVo)){
						continue;
					}
					
					WarehouseBillDetail billDetail = billDetailService.get(detailVo.getBillDetailId());
					//单价明细原本的换算关系
					BigDecimal initScale = billDetail.getScale();
					//修改后的单价
					BigDecimal afterUnitPrice = NumberUtil.toBigDeciaml(detailVo.getAfterPrice());
					//修改后的记账单价
					BigDecimal afterAccountUintPrice = calculateAccountUintPrice(afterUnitPrice, initScale);
					
					//货品定价
					GoodsPrice goodsPrice = goodsPriceService.findByGoodsIdAndSpecId(detailVo.getGoodsId(), detailVo.getGoodsSpecId());
					if(goodsPrice == null){
						continue;
					}
					
					//货品定价的换算关系
					BigDecimal goodsPriceScale = goodsPrice.getUnit().getScale();
					
					//货品名称
					String goodsName = "";
					Goods goods = goodsService.get(detailVo.getGoodsId());
					if(StringUtils.isBlank(detailVo.getGoodsSpecId())){
						goodsName = goods.getName();
					}
					else{
						GoodsSpec goodsSpec = goodsSpecService.get(detailVo.getGoodsSpecId());
						goodsName = String.format("%s(%s)", goods.getName(), goodsSpec.getName());
					}
					
					if(SecurityUtil.isPermit(user.getFid(), SystemConstant.PERMISSION_SELL_ZERO_PRICE)){
						if(afterUnitPrice.compareTo(BigDecimal.ZERO) == 0){
							continue;
						}
						else{
							//最低售价
							if(!isIgnore(goodsPrice.getLowestPrice())){
								BigDecimal lowestUintPrice = calculateAccountUintPrice(goodsPrice.getLowestPrice(), goodsPriceScale);
								if(afterAccountUintPrice.compareTo(lowestUintPrice) < 0 ){
									return new RequestResult(RequestResult.RETURN_FAILURE, 
												goodsName + "单价必须大于等于" + NumberUtil.stripTrailingZeros(initScale.multiply(lowestUintPrice), 4));
								}
							}
						}
					}
					else if(SecurityUtil.isPermit(user.getFid(), SystemConstant.PERMISSION_SELL_SECOND_PRICE)){
						//销售二级价下限
						if(!isIgnore(goodsPrice.getSalesLowerLimit2())){
							BigDecimal salesLowerLimit2 = calculateAccountUintPrice(goodsPrice.getSalesLowerLimit2(), goodsPriceScale);
							if(afterAccountUintPrice.compareTo(salesLowerLimit2) < 0 ){
								return new RequestResult(RequestResult.RETURN_FAILURE, 
											goodsName + "单价必须大于等于" + NumberUtil.stripTrailingZeros(initScale.multiply(salesLowerLimit2), 4));
							}
						}
						//销售二级价上限
						if(!isIgnore(goodsPrice.getSalesUpperLimit2())){
							BigDecimal salesUpperLimit2 = calculateAccountUintPrice(goodsPrice.getSalesUpperLimit2(), goodsPriceScale);
							if(afterAccountUintPrice.compareTo(salesUpperLimit2) > 0){
								return new RequestResult(RequestResult.RETURN_FAILURE, 
											goodsName + "单价必须小于等于" + NumberUtil.stripTrailingZeros(initScale.multiply(salesUpperLimit2), 4));
							}
						}
					}
					else if(SecurityUtil.isPermit(user.getFid(), SystemConstant.PERMISSION_SELL_FIRST_PRICE)){
						//销售一级价下限
						if(!isIgnore(goodsPrice.getSalesLowerLimit1())){
							BigDecimal salesLowerLimit1 = calculateAccountUintPrice(goodsPrice.getSalesLowerLimit1(), goodsPriceScale);
							if(afterAccountUintPrice.compareTo(salesLowerLimit1) < 0){
								return new RequestResult(RequestResult.RETURN_FAILURE, 
											goodsName + "单价必须大于等于" + NumberUtil.stripTrailingZeros(initScale.multiply(salesLowerLimit1), 4));
							}
						}
						//销售一级价上限
						if(!isIgnore(goodsPrice.getSalesUpperLimit1())){
							BigDecimal salesUpperLimit1 = calculateAccountUintPrice(goodsPrice.getSalesUpperLimit1(), goodsPriceScale);
							if(afterAccountUintPrice.compareTo(salesUpperLimit1) > 0){
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
	
	/**
	 * 新增核价记录
	 * @param vos
	 */
	public void savePricingRecord(List<PricingDetailVo> vos){
		for(PricingDetailVo vo : vos){
			//单据
			WarehouseBill bill = billService.get(vo.getBillId());
			//单据明细
			WarehouseBillDetail billDetail = billDetailService.get(vo.getBillDetailId());
			//修改后的单价
			BigDecimal afterUnitPrice = NumberUtil.toBigDeciaml(vo.getAfterPrice());
			//修改后的记账金额
			BigDecimal afterAccountAmount = NumberUtil.toBigDeciaml(vo.getAfterAmount());
			//修改后的记账单价
			BigDecimal afterAccountUintPrice = afterUnitPrice.divide(billDetail.getScale(), WarehouseBillDetailService.ACCOUNT_UINT_PRICE_SCALE, BigDecimal.ROUND_HALF_UP);
			
			Pricing entity = new Pricing();
			entity.setBill(bill);
			entity.setBillDetail(billDetail);
			entity.setBeforeAccountPrice(billDetail.getAccountUintPrice());
			entity.setAfterAccountPrice(afterAccountUintPrice);
			entity.setBeforePrice(billDetail.getUnitPrice());
			entity.setAfterPrice(afterUnitPrice);
			entity.setBeforeAmount(billDetail.getType());
			entity.setAfterAmount(afterAccountAmount);
			entity.setUnit(unitService.get(vo.getUnitId()));
			entity.setGoods(goodsService.get(vo.getGoodsId()));
			entity.setOrg(SecurityUtil.getCurrentOrg());
			entity.setCreator(SecurityUtil.getCurrentUser());
			entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
			entity.setCreateTime(Calendar.getInstance().getTime());
			if(StringUtils.isNotBlank(vo.getGoodsSpecId())){
				entity.setGoodSpec(goodsSpecService.get(vo.getGoodsSpecId()));
			}
			save(entity);
		}
	}
	
	/**
	 * 获取表单的核价明细信息 
	 * @param vo
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<PricingDetailVo> getVos(PricingVo vo){
		List<PricingDetailVo> details = new ArrayList<PricingDetailVo>();
		if(StringUtils.isNotBlank(vo.getDetails())){
			JSONArray jsonArray = JSONArray.fromObject(vo.getDetails());
			List jsonList = (List) JSONArray.toCollection(jsonArray, PricingDetailVo.class);
			Iterator<Object> iterator = jsonList.iterator();
			while(iterator.hasNext()){
				PricingDetailVo detail = (PricingDetailVo) iterator.next();
				details.add(detail);
			}
		}
		return details;
	}

	@Override
	public PricingVo getVo(Pricing entity) {
		return null;
	}

	@Override
	public CrudRepository<Pricing, String> getRepository() {
		return pricingRepository;
	}
}
