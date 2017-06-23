package cn.fooltech.fool_ops.domain.warehouse.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.Goods;
import cn.fooltech.fool_ops.domain.basedata.entity.GoodsBar;
import cn.fooltech.fool_ops.domain.basedata.entity.GoodsSpec;
import cn.fooltech.fool_ops.domain.basedata.entity.Unit;
import cn.fooltech.fool_ops.domain.basedata.service.AuxiliaryAttrService;
import cn.fooltech.fool_ops.domain.basedata.service.GoodsBarService;
import cn.fooltech.fool_ops.domain.basedata.service.GoodsPriceService;
import cn.fooltech.fool_ops.domain.basedata.service.GoodsService;
import cn.fooltech.fool_ops.domain.basedata.service.GoodsSpecService;
import cn.fooltech.fool_ops.domain.basedata.service.UnitService;
import cn.fooltech.fool_ops.domain.common.entity.PageBean;
import cn.fooltech.fool_ops.domain.common.entity.ResultObject;
import cn.fooltech.fool_ops.domain.sysman.entity.SmgOrgAttr;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.sysman.service.SmgOrgAttrService;
import cn.fooltech.fool_ops.domain.sysman.vo.SmgOrgAttrVo;
import cn.fooltech.fool_ops.domain.transport.entity.GoodsTransport;
import cn.fooltech.fool_ops.domain.transport.service.GoodsTransportService;
import cn.fooltech.fool_ops.domain.voucher.entity.BillSubjectDetail;
import cn.fooltech.fool_ops.domain.warehouse.entity.PeriodAmount;
import cn.fooltech.fool_ops.domain.warehouse.entity.PeriodStockAmount;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBillDetail;
import cn.fooltech.fool_ops.domain.warehouse.repository.WarehouseBillDetailRepository;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.domain.warehouse.vo.WarehouseBillDetailVo;
import cn.fooltech.fool_ops.utils.DateUtil;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;


/**
 * <p>仓储单据明细服务类</p>
 * @author xjh
 * @date 2016-10-26
 */
@Service
public class WarehouseBillDetailService extends BaseService<WarehouseBillDetail, WarehouseBillDetailVo, String> {
	
	/**
	 * 记账单价精度
	 */
	public static final int ACCOUNT_UINT_PRICE_SCALE = 8;
	
	/**
	 * 记账金额精度
	 */
	public static final int ACCOUNT_AMOUNT_SCALE = 2; 
	
	@Autowired
	private WarehouseBillDetailRepository billDetailRepo;
    
	@Autowired
	private GoodsPriceService priceService;
	
	/**
	 * 出库服务类
	 */
	@Autowired
	private OutStorageService outStorageService;
	
	@Resource(name = "ops.WarehouseBillService")
	private WarehouseBillService billService;
	
	/**
	 * 货品服务类
	 */
	@Autowired
	protected GoodsService goodsService;


	@Autowired
	protected GoodsTransportService goodsTransportService;
	
	/**
	 * 货品属性服务类
	 */
	@Autowired
	private GoodsSpecService goodsSpecService;
	
	/**
	 * 单位服务类
	 */
	@Autowired
	private UnitService unitService;
	
	/**
	 * 辅助属性服务类
	 */
	@Autowired
	private AuxiliaryAttrService attrService;
	/**
	 * 系统参数服务类
	 */
	@Autowired
	private SmgOrgAttrService smgOrgAttrService;
	/**
	 * 期间总库存金额表服务类
	 */
	@Autowired
	protected PeriodAmountService periodAmountService;
	/**
	 * 期间分仓库存服务类
	 */
	@Autowired
	protected PeriodStockAmountService periodStockAmountService;
	@Autowired
	private GoodsBarService barService;

	@Override
	public CrudRepository<WarehouseBillDetail, String> getRepository() {
		return billDetailRepo;
	}


	@Override
	public WarehouseBillDetailVo getVo(WarehouseBillDetail entity) {
		WarehouseBillDetailVo vo = new WarehouseBillDetailVo();
		vo.setFid(entity.getFid());
		vo.setDescribe(entity.getDescribe());
		vo.setCreateTime(entity.getCreateTime());
		vo.setCostPrice(NumberUtil.bigDecimalToStr(entity.getCostPrice(), NumberUtil.PRECISION_SCALE_4));
		vo.setType(NumberUtil.bigDecimalToStr(entity.getType()));
		vo.setScale(NumberUtil.bigDecimalToStr(entity.getScale()));
		vo.setAccountQuentity(NumberUtil.bigDecimalToStr(entity.getAccountQuentity()));
		vo.setAccountUintPrice(NumberUtil.bigDecimalToStr(entity.getAccountUintPrice()));
		vo.setQuentity(NumberUtil.bigDecimalToStr(entity.getQuentity()));
		vo.setUnitPrice(NumberUtil.bigDecimalToStr(entity.getUnitPrice(), NumberUtil.PRECISION_SCALE_4));
		vo.setUpdateTime(DateUtilTools.time2String(entity.getUpdateTime()));
		vo.setDetailType(entity.getDetailType());
		vo.setTaxRate(entity.getTaxRate());
		vo.setTaxAmount(entity.getTaxAmount());
		vo.setTotalAmount(entity.getTotalAmount());
		vo.setRefDetailId(entity.getRefDetailId());

		vo.setReceivedQuantity(entity.getReceivedQuantity());
		vo.setLoseAmount(entity.getLoseAmount());
		vo.setLoseQuantity(entity.getLoseQuantity());
		vo.setDeliveryCostAmount(entity.getDeliveryCostAmount());
		vo.setDeliveryCostPrice(entity.getDeliveryCostPrice());
		vo.setCostAmount(entity.getCostAmount());
		vo.setTransportAmount(entity.getTransportAmount());
		vo.setTransportPrice(entity.getTransportPrice());
		vo.setTransportQuentity(entity.getTransportQuentity());
		vo.setDeductionAmount(entity.getDeductionAmount());
		vo.setTransprotScale(entity.getTransprotScale());
		
		//记账单位
		Unit accountUint = entity.getAccountUint();
		if(accountUint != null){
			vo.setAccountUintID(accountUint.getFid());
			vo.setAccountUintName(accountUint.getName());
		}

		//运输单位
		try{
			AuxiliaryAttr transportUint = entity.getTransportUint();
			if(transportUint != null){
				vo.setTransportUnitId(transportUint.getFid());
				vo.setTransportUnitName(transportUint.getName());
				vo.setTransprotScale(entity.getTransprotScale());
			}
		}catch (Exception e){}


		//仓库单据
		WarehouseBill bill = entity.getBill();
		if(bill != null){
			vo.setBillId(bill.getFid());
			vo.setBillCode(bill.getCode());
		}
		//创建人
		User creator = entity.getCreator();
		if(creator != null){
			vo.setCreatorName(creator.getUserName());
		}
		//货品
		Goods goods = entity.getGoods();
		if(goods != null){
			vo.setGoodsId(goods.getFid());
			vo.setGoodsCode(goods.getCode());
			vo.setGoodsName(goods.getName());
			vo.setGoodsSpec(goods.getSpec());
			
			GoodsSpec goodsSpec = entity.getGoodsSpec();
			if(goodsSpec==null){
				vo.setBarCode(goods.getBarCode());
			}else{
				Unit unit = entity.getUnit();
				GoodsBar bar = barService.findByGoods(goods.getFid(), goodsSpec.getFid(), unit.getFid());
				if(bar!=null){
					vo.setBarCode(bar.getBarCode());
				}
			}
		}
		//货品属性
		GoodsSpec goodsSpec = entity.getGoodsSpec();
		if(goodsSpec != null){
			vo.setGoodsSpecId(goodsSpec.getFid());
			vo.setGoodsSpecName(goodsSpec.getName());
			//货品属性组
			GoodsSpec specGroup = goodsSpec.getParent();
			if(specGroup != null){
				vo.setGoodsSpecGroupId(specGroup.getFid());
				vo.setGoodsSpecGroupName(specGroup.getName());
			}
		}
		//进仓仓库
		AuxiliaryAttr inWareHouse = entity.getInWareHouse();
		if(inWareHouse != null){
			vo.setInWareHouseId(inWareHouse.getFid());
			vo.setInWareHouseName(inWareHouse.getName());
		}
		//出仓仓库
		AuxiliaryAttr outWareHouse = entity.getOutWareHouse();
		if(outWareHouse != null){
			vo.setOutWareHouseId(outWareHouse.getFid());
			vo.setOutWareHouseName(outWareHouse.getName());
		}
		//单位
		Unit unit = entity.getUnit();
		if(unit != null){
			vo.setUnitId(unit.getFid());
			vo.setUnitName(unit.getName());
			//单位组
			Unit unitGroup = unit.getParent();
			if(unitGroup != null){
				vo.setUnitGroupId(unitGroup.getFid());
				vo.setUnitGroupName(unitGroup.getName());
			}
		}
		//货品的最低销售价
		BigDecimal lowestPrice = priceService.getLowestPrice(vo.getGoodsId(), vo.getUnitId(), vo.getGoodsSpecId());
		vo.setLowestPrice(lowestPrice.toString());
		return vo;
	}
	
	
	/**
	 * 获取单据明细中货品的单价
	 * @param billTypes 仓库单据类型
	 * @param customerId 客户ID
	 * @param supplierId 供应商ID
	 * @param goodsId 货品ID
	 * @param unitId 货品单位ID
	 * @param goodsSpecId 货品属性ID 
	 * @return
	 */
	public BigDecimal getUnitPrice(List<Integer> billTypes, String customerId, String supplierId, String goodsId, String unitId, String goodsSpecId){
		Assert.isTrue(CollectionUtils.isNotEmpty(billTypes));
		Assert.isTrue(StringUtils.isNotBlank(goodsId));
		Assert.isTrue(StringUtils.isNotBlank(unitId));
		
		WarehouseBillDetail billDetail =billDetailRepo.findTopBy(billTypes, WarehouseBill.STATUS_AUDITED, customerId, 
				supplierId, goodsSpecId, goodsId, unitId);
		
		if(billDetail!=null){
			return billDetail.getUnitPrice();
		}
		return BigDecimal.ZERO;
	}
	
	/**
	 * 获取单据明细中货品的记账单价
	 * @param billTypes 仓库单据类型
	 * @param customerId 客户ID
	 * @param supplierId 供应商ID
	 * @param goodsId 货品ID
	 * @param goodsSpecId 货品属性ID 
	 * @return
	 */
	public BigDecimal getAccountUintPrice(List<Integer> billTypes, String customerId, String supplierId, String goodsId, String goodsSpecId){
		Assert.isTrue(CollectionUtils.isNotEmpty(billTypes));
		Assert.isTrue(StringUtils.isNotBlank(goodsId));
		
		WarehouseBillDetail billDetail =billDetailRepo.findTopBy(billTypes, WarehouseBill.STATUS_AUDITED, customerId, 
				supplierId, goodsSpecId, goodsId, null);
		
		if(billDetail!=null){
			return billDetail.getAccountUintPrice();
		}
		return BigDecimal.ZERO;
	}
	
	/**
	 * 获取仓库单据明细
	 * @param billId 仓库单据ID
	 * @return
	 */
	public List<WarehouseBillDetail> getBillDetails(String billId){
		return billDetailRepo.findByBillId(billId);
	}

	/**
	 * 获取仓库单据明细
	 * @param billIds 仓库单据IDs
	 * @return
	 */
	public List<WarehouseBillDetail> getBillDetails(List<String> billIds){
		return billDetailRepo.findByBillIds(billIds);
	}
	
	/**
	 * 获取仓库单据明细
	 * @param billId 仓库单据ID
	 * @return
	 */
	public List<WarehouseBillDetailVo> getBillDetailVos(String billId){
		return getVos(billDetailRepo.findByBillId(billId));
	}
	
	/**
	 * 计算记账金额
	 * @param accountQuentity 记账数量
	 * @param accountUintPrice 记账单价
	 * @return
	 */
	public BigDecimal calculateAccountAmount(BigDecimal accountQuentity, BigDecimal accountUintPrice){
		if(accountQuentity != null && accountUintPrice != null){
			BigDecimal accountAmount = accountQuentity.multiply(accountUintPrice);
			return accountAmount.setScale(ACCOUNT_AMOUNT_SCALE, BigDecimal.ROUND_HALF_UP);
		}
		return BigDecimal.ZERO;
	}
	
	/**
	 * 保存
	 * @param vo
	 * @return
	 */
	@SuppressWarnings("unused")
	public RequestResult save(WarehouseBillDetailVo vo,WarehouseBill bill){
		String fid = vo.getFid();
		String describe = vo.getDescribe();
		String quentity = vo.getQuentity();
		String costPrice = vo.getCostPrice();
		String unitPrice = vo.getUnitPrice();
		String billId = vo.getBillId();//仓库单据ID
		String goodsId = vo.getGoodsId();//货品ID
		String goodsSpecId = vo.getGoodsSpecId();//货品属性ID
		String unitId = vo.getUnitId();//货品单位
		String inWareHouseId = vo.getInWareHouseId();//进仓仓库ID
		String outWareHouseId = vo.getOutWareHouseId();//出仓仓库ID
		Integer detailType = vo.getDetailType();

		Date now = new Date();
		
		WarehouseBillDetail entity = null;
		if(StringUtils.isBlank(fid)){
			entity = new WarehouseBillDetail();
			entity.setCreateTime(now);
			entity.setUpdateTime(now);
			entity.setOrg(SecurityUtil.getCurrentOrg());
			entity.setCreator(SecurityUtil.getCurrentUser());
			entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
		}
		else{
			entity = billDetailRepo.findOne(fid);
			entity.setUpdateTime(now);
		}
		entity.setDescribe(describe);
		entity.setQuentity(NumberUtil.toBigDeciaml(quentity));
		entity.setUnitPrice(NumberUtil.toBigDeciaml(unitPrice));
		entity.setCostPrice(NumberUtil.toBigDeciaml(costPrice));
		
		entity.setTaxRate(vo.getTaxRate());
		entity.setTaxAmount(vo.getTaxAmount());
		BigDecimal totalAmount = vo.getTotalAmount();//金额【销售金额】
		entity.setTotalAmount(totalAmount);
		Integer billType = bill.getBillType();
		if(billType==WarehouseBuilderCodeHelper.xsch||billType==WarehouseBuilderCodeHelper.xsth){
			BigDecimal percentage = vo.getPercentage();//提成点数
			BigDecimal percentageAmount = vo.getPercentageAmount();//提成金额[销售金额*提成点数/100]
			if(percentage==null&&percentageAmount==null){
				return buildFailRequestResult("提成点数或者提成金额必须填写其中一项!");
			}
			//提成点数限制为0至100之间；
			if(percentage.compareTo(BigDecimal.ZERO)<0||percentage.compareTo(new BigDecimal(100))>0){
				return buildFailRequestResult("提成点数必须为0至100之间!");
			}
			//反算销售提成点数=提成金额/销售金额*100
			if(percentage==null){
				BigDecimal multiply = percentageAmount.divide(totalAmount,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
				percentage=multiply==null?BigDecimal.ZERO:multiply;
				entity.setPercentage(percentage);
			}
			//更新销售提成金额=销售金额*销售提成点数/100
			else if(percentageAmount==null){
				BigDecimal divide = totalAmount.multiply(percentage).divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP);
				percentageAmount=divide==null?BigDecimal.ZERO:divide;
				entity.setPercentageAmount(percentageAmount);
			}
			else{
				entity.setPercentage(percentage);
				entity.setPercentageAmount(percentageAmount);
			}
		}

		
		entity.setRefDetailId(vo.getRefDetailId());
		entity.setDetailType(detailType);

		entity.setReceivedQuantity(vo.getReceivedQuantity());
		entity.setLoseAmount(vo.getLoseAmount());
		entity.setLoseQuantity(vo.getLoseQuantity());
		entity.setDeliveryCostAmount(vo.getDeliveryCostAmount());
		entity.setDeliveryCostPrice(vo.getDeliveryCostPrice());
		entity.setCostAmount(vo.getCostAmount());
		entity.setTransportAmount(vo.getTransportAmount());
		entity.setTransportPrice(vo.getTransportPrice());
		entity.setTransportQuentity(vo.getTransportQuentity());
		entity.setDeductionAmount(vo.getDeductionAmount());
		entity.setTransprotScale(vo.getTransprotScale());

		//运输单位ID
		if(!Strings.isNullOrEmpty(vo.getTransportUnitId())){
			AuxiliaryAttr transportUint = attrService.get(vo.getTransportUnitId());
			entity.setTransportUint(transportUint);

			GoodsTransport goodsTransport = goodsTransportService.findGoodsTransport(goodsId,
					goodsSpecId, vo.getTransportUnitId());
			entity.setTransprotScale(goodsTransport.getConversionRate());

		}

		//仓库单据
		if(StringUtils.isNotBlank(billId)){
			entity.setBill(billService.get(billId));
		}
		//货品
		if(StringUtils.isNotBlank(goodsId)){
			entity.setGoods(goodsService.get(goodsId));
		}
		//货品属性
		if(StringUtils.isNotBlank(goodsSpecId)){
			entity.setGoodsSpec(goodsSpecService.get(goodsSpecId));
		}else{
			entity.setGoodsSpec(null);
		}
		//货品单位
		if(StringUtils.isNotBlank(unitId)){
			Unit unit = unitService.get(unitId);
			entity.setUnit(unit);
			entity.setScale(unit.getScale());
		}else{
			entity.setUnit(null);
		}
		//进仓仓库
		if(StringUtils.isNotBlank(inWareHouseId)){
			entity.setInWareHouse(attrService.get(inWareHouseId));
		}else{
			entity.setInWareHouse(null);
		}
		//出仓仓库
		if(StringUtils.isNotBlank(outWareHouseId)){
			entity.setOutWareHouse(attrService.get(outWareHouseId));
		}else{
			entity.setOutWareHouse(null);
		}
		//特殊处理生产退料单的成本价
		if(vo.getBillType() != null && vo.getBillType() == WarehouseBuilderCodeHelper.sctl){
			entity.setCostPrice(entity.getUnitPrice());
		}
		
		entity = fillAccountDetail(entity);
		
		billDetailRepo.save(entity);
		
		return new RequestResult();
	}
	
	/**
	 * 单据明细表的记账单位、记账数量、记账单价，记账金额、换算关系等，根据每行的货品ID的货品单位自动换算并写入记录（换算关系为1的单位）
	 * add by xjh
	 * @date 2015年9月25日
	 */
	@Transactional(readOnly=true)
	public WarehouseBillDetail fillAccountDetail(WarehouseBillDetail detail){
		Unit formUnit = detail.getUnit();
		Unit accountUnit = null;
		
		if(formUnit.getScale().equals(1)){
			accountUnit = formUnit;
		}else{
			accountUnit = unitService.findAccountUnit(formUnit.getParent().getFid());
			if(accountUnit == null) throw new RuntimeException("单位组内换算关系为1的单位不存在!");
		}
		//记账单位
		detail.setAccountUint(accountUnit);
		
		BigDecimal scale = detail.getScale();
		BigDecimal quentity = detail.getQuentity();
		BigDecimal unitPrice = detail.getUnitPrice();
		
		if(quentity != null && scale != null){
			WarehouseBill bill = detail.getBill();
			if(bill!=null&&bill.getBillType()==WarehouseBuilderCodeHelper.shd){
				//收货单记账数量=实收数量*换算关系
				detail.setAccountQuentity(detail.getReceivedQuantity().multiply(scale));
			}else{
				//记账数量=货品数量*换算关系
				detail.setAccountQuentity(detail.getQuentity().multiply(scale));
			}
			
		}
		if(unitPrice != null && scale != null && scale.compareTo(BigDecimal.ZERO) > 0){
			//记账单价
			detail.setAccountUintPrice(unitPrice.divide(scale, ACCOUNT_UINT_PRICE_SCALE, BigDecimal.ROUND_HALF_UP));
		}
		//记账金额
		BigDecimal accountAmount = calculateAccountAmount(detail.getAccountQuentity(), detail.getAccountUintPrice());
		detail.setType(accountAmount);
		return detail;
	}
	
	/**
	 * 删除仓库单据下的所有单据明细
	 * @param billId 仓库单据ID
	 */
	public void deleteByBill(String billId){
		if(!billService.canOperateByStatus(billId)){
			throw new RuntimeException("删除失败，单据处于已审核状态");
		}
		List<WarehouseBillDetail> details = billDetailRepo.findByBillId(billId);
		billDetailRepo.delete(details);
	}
	
	/**
	 * 根据金额来源，获取单据明细的金额
	 * @param billDetail 单据明细
	 * @param amountSource 金额来源: 1-单据金额（不含税金额）,2-成本金额, 3--税额, 4-含税金额, 5-利润
	 * @return
	 */
	public BigDecimal getAmount(WarehouseBillDetail billDetail, int amountSource){
		switch(amountSource){
			case BillSubjectDetail.AMOUNT_SOURCE_BILL:{
				return billDetail.getType();
			}
			case BillSubjectDetail.AMOUNT_SOURCE_COST:{
				return getDetailCostAmount(billDetail);
			}
			case BillSubjectDetail.AMOUNT_SOURCE_TAX:{
				return billDetail.getTaxAmount();
			}
			case BillSubjectDetail.AMOUNT_SOURCE_INCLUDE_TAX:{
				return billDetail.getTotalAmount();
			}
			case BillSubjectDetail.AMOUNT_SOURCE_PROFIT:{
				BigDecimal amount = billDetail.getType();
				BigDecimal costPrice = getDetailCostAmount(billDetail);
				return amount.subtract(costPrice);
			}
			case BillSubjectDetail.AMOUNT_PERCENTAGE:{
				return billDetail.getTotalAmount();
			}
		}
		return BigDecimal.ZERO;
	}
	
	/**
	 * 根据金额来源，计算单据明细的总金额
	 * @param billId 单据ID
	 * @param amountSource 金额来源: 1-单据金额（不含税金额）,2-成本金额, 3--税额, 4--含税金额, 5-利润
	 * @return
	 */
	public BigDecimal getTotalAmount(String billId, int amountSource){
		
		WarehouseBill bill = billService.get(billId);
		
		//成本
		if(amountSource==BillSubjectDetail.AMOUNT_SOURCE_COST){
			return getCostAmount(billId);
		//利润
		}else if(amountSource==BillSubjectDetail.AMOUNT_SOURCE_PROFIT){
			BigDecimal amount = bill.getTotalAmount();
			BigDecimal costPrice = getCostAmount(billId);
			return amount.subtract(costPrice);
		}else{
			BigDecimal result = null;
			
			switch(amountSource){
				case BillSubjectDetail.AMOUNT_SOURCE_BILL:{
					result = billDetailRepo.sumBillInCostAmount(billId);
					break;
				}
				case BillSubjectDetail.AMOUNT_SOURCE_TAX:{
					result = billDetailRepo.sumTaxAmount(billId);
					break;
				}
				case BillSubjectDetail.AMOUNT_SOURCE_INCLUDE_TAX:{
					result = billDetailRepo.sumTotalAmount(billId);
					break;
				}
				default:{
					return BigDecimal.ZERO;
				}
			}
			if(result==null){
				return BigDecimal.ZERO;
			}
			return result;
		}
	}
	
	/**
	 * 获取单据的成本金额
	 * @param billId 单据ID
	 * @return
	 */
	public BigDecimal getCostAmount(String billId){
		WarehouseBill bill = billService.get(billId);
		int billType = bill.getBillType();
		switch(billType){
			case WarehouseBuilderCodeHelper.qckc:return getBillInCostAmount(billId);
			case WarehouseBuilderCodeHelper.cgrk:return getBillInCostAmount(billId);
			case WarehouseBuilderCodeHelper.cgth:return getBillOutCostAmount(bill);
			case WarehouseBuilderCodeHelper.scll:return getBillOutCostAmount(bill);
			case WarehouseBuilderCodeHelper.sctl:return getBillOutReturnCostAmount(bill);
			case WarehouseBuilderCodeHelper.cprk:return getBillInCostAmount(billId);
			case WarehouseBuilderCodeHelper.cptk:return getBillOutCostAmount(bill);
			case WarehouseBuilderCodeHelper.xsch:return getBillOutCostAmount(bill);
			case WarehouseBuilderCodeHelper.xsth:return getBillOutReturnCostAmount(bill);
		}
		return BigDecimal.ZERO;
	}
	
	/**
	 * 获取单据明细的成本金额
	 * @param billDetail 单据ID
	 * @return
	 */
	public BigDecimal getDetailCostAmount(WarehouseBillDetail billDetail){
		WarehouseBill bill = billDetail.getBill();
		int billType = bill.getBillType();
		switch(billType){
			case WarehouseBuilderCodeHelper.qckc:return billDetail.getType();
			case WarehouseBuilderCodeHelper.cgrk:return billDetail.getType();
			case WarehouseBuilderCodeHelper.cgth:return getBillDetailOutCostAmount(billDetail);
			case WarehouseBuilderCodeHelper.scll:return getBillDetailOutCostAmount(billDetail);
			case WarehouseBuilderCodeHelper.sctl:return getBillDetailOutReturnCostAmount(billDetail);
			case WarehouseBuilderCodeHelper.cprk:return billDetail.getType();
			case WarehouseBuilderCodeHelper.cptk:return getBillDetailOutCostAmount(billDetail);
			case WarehouseBuilderCodeHelper.xsch:return getBillDetailOutCostAmount(billDetail);
			case WarehouseBuilderCodeHelper.xsth:return getBillDetailOutReturnCostAmount(billDetail);
		}
		return BigDecimal.ZERO;
	}
	
	/**
	 * 获取入库类型单据的成本金额
	 * @param billId 单据ID
	 * @return
	 */
	public BigDecimal getBillInCostAmount(String billId){
		BigDecimal sum = billDetailRepo.sumBillInCostAmount(billId);
		if(sum==null)return BigDecimal.ZERO;
		return sum;
	}
	
	
	/**
	 * 获取入库类型单据的成本金额
	 * @param bill 单据
	 * @return
	 */
	public BigDecimal getBillOutCostAmount(WarehouseBill bill){
		List<WarehouseBillDetail> details = bill.getDetails();
		List<String> detailIds = Lists.newArrayList();
		
		for(WarehouseBillDetail detail:details){
			detailIds.add(detail.getFid());
		}
		
		return outStorageService.findOutStorageSum(detailIds);
	}
	
	/**
	 * 获取入库类型单据的成本金额
	 * @param billDetail 单据
	 * @return
	 */
	public BigDecimal getBillDetailOutCostAmount(WarehouseBillDetail billDetail){
		List<String> detailIds = Lists.newArrayList();
		detailIds.add(billDetail.getFid());
		return outStorageService.findOutStorageSum(detailIds);
	}
	
	/**
	 * 获取出库再退货类型单据的成本金额
	 * @param bill 单据
	 * @return
	 */
	public BigDecimal getBillOutReturnCostAmount(WarehouseBill bill){
		List<WarehouseBillDetail> details = bill.getDetails();
		
		BigDecimal total = BigDecimal.ZERO;
		for(WarehouseBillDetail detail:details){
			BigDecimal costAmount = NumberUtil.multiply(detail.getQuentity(), detail.getCostPrice());
			total = NumberUtil.add(total, costAmount);
		}
		return total;
	}
	
	/**
	 * 获取出库再退货类型单据的成本金额
	 * @param detail 单据
	 * @return
	 */
	public BigDecimal getBillDetailOutReturnCostAmount(WarehouseBillDetail detail){
		return NumberUtil.multiply(detail.getQuentity(), detail.getCostPrice());
	}
	
	/**
	 * 通过分组查询，获取仓库单据明细
	 * @param billId 仓库单据ID
	 * @param groupType 分组查询类型，0 货品   1 仓库   2  货品+仓库
	 * @param amountSource 金额来源: 1-单据金额（不含税金额）,2-成本金额, 3--税额, 4-含税金额, 5-利润 
	 * @return 明细ID、金额
	 */
	public List<Object[]> getDetailsByGroup(String billId, int groupType, int amountSource){
		return billDetailRepo.getDetailsByGroup(billId, groupType, amountSource);
	}
	
	/**
	 * 根据辅助属性仓库id+货品+货品属性获取仓库单据明细
	 * @param inWareHouseId
	 * @param goodsId
	 * @param goodsSpecId
	 * @return
	 */
	public List<WarehouseBillDetail> findGoodsAndWarehouse(String planId,String inWareHouseId,String goodsId,String goodsSpecId){
		if(Strings.isNullOrEmpty(goodsSpecId)){
			return billDetailRepo.findGoodsAndWarehouseByPlanGoods(planId, inWareHouseId, goodsId);
		}else{
			return billDetailRepo.findGoodsAndWarehouseByPlanGoods(planId, inWareHouseId, goodsId,goodsSpecId);
		}
		
	}
	//单据审核、作废时操作
	@Transactional
	public void processDetail(WarehouseBillDetail detail,WarehouseBill bill){
		//查看是否有系统参数
		String key="WAREHOUSE_ACCOUNT";
		SmgOrgAttrVo sovo=smgOrgAttrService.queryByOrg(SecurityUtil.getCurrentOrgId(), key);
		if(sovo==null){
			//若无系统参数则添加系统参数
			SmgOrgAttr so=new SmgOrgAttr();
			so.setKey("WAREHOUSE_ACCOUNT");
			so.setValue("0");
			so.setDescribe("分仓核算 0-不分仓核算  1-分仓核算");
			so.setName("分仓核算");
			Date now = new Date();
			so.setCreateTime(now);
			so.setRecordState("SAC");
			so.setType("0");
			so.setOrg(SecurityUtil.getCurrentOrg());
			smgOrgAttrService.save(so);
			sovo=smgOrgAttrService.getVo(so);
		}
		String peridId = bill.getStockPeriod().getFid();
		String goodsId = detail.getGoods().getFid();
		String goodsSpecId = null;
		AuxiliaryAttr inWarehouse = detail.getInWareHouse();
		AuxiliaryAttr outWarehouse = detail.getOutWareHouse();
		if (detail.getGoodsSpec() != null) {
			goodsSpecId = detail.getGoodsSpec().getFid();
		}
		//期间总库存金额
		PeriodAmount periodAmount = periodAmountService.existRecord(peridId, goodsId, goodsSpecId);
		if (periodAmount == null) {
			periodAmount = periodAmountService.initPeriodAmount(bill, detail);
		}
		PeriodStockAmount periodStockAmount=periodStockAmountService.existRecord(peridId, inWarehouse.getFid(), goodsId, goodsSpecId);
		if(periodStockAmount==null){
			periodStockAmount=periodStockAmountService.initPeriodAmount(bill, detail, inWarehouse);
		}
		//期初库存
		if(bill.getBillType()==91){
			detail.setCostPrice(detail.getAccountUintPrice());
			detail.setCostAmount(detail.getType());
		}
		//采购入库
		if(bill.getBillType()==11){
			detail.setCostPrice(detail.getAccountUintPrice());
			detail.setCostAmount(detail.getType());
		}
		//采购退货
		if(bill.getBillType()==12){
			if(sovo.getValue().equals("1")){
				detail.setCostPrice(periodStockAmount.getAccountPrice());
				detail.setCostAmount(detail.getAccountQuentity().multiply(detail.getCostPrice()));
			}else{
				detail.setCostPrice(periodAmount.getAccountPrice());
				detail.setCostAmount(detail.getAccountQuentity().multiply(detail.getCostPrice()));
			}
		}
		//生产领料
		if(bill.getBillType()==30){
			if(sovo.getValue().equals("1")){
				detail.setCostPrice(periodStockAmount.getAccountPrice());
				detail.setCostAmount(detail.getAccountQuentity().multiply(detail.getCostPrice()));
			}else{
				detail.setCostPrice(periodAmount.getAccountPrice());
				detail.setCostAmount(detail.getAccountQuentity().multiply(detail.getCostPrice()));
			}
		}
		//生产退料
		if(bill.getBillType()==32){
			detail.setCostPrice(detail.getAccountUintPrice());
			detail.setCostAmount(NumberUtil.multiply(detail.getCostPrice(), detail.getAccountQuentity()));
		}
		//成品入库
		if(bill.getBillType()==31){
			detail.setCostPrice(detail.getAccountUintPrice());
			detail.setCostAmount(detail.getType());
		}
		//成品退库
		if(bill.getBillType()==33){
			if(sovo.getValue().equals("1")){
				detail.setCostPrice(periodStockAmount.getAccountPrice());
				detail.setCostAmount(detail.getAccountQuentity().multiply(detail.getCostPrice()));
			}else{
				detail.setCostPrice(periodAmount.getAccountPrice());
				detail.setCostAmount(detail.getAccountQuentity().multiply(detail.getCostPrice()));
			}
		}
		//销售出货
		if(bill.getBillType()==41){
			if(sovo.getValue().equals("1")){
				detail.setCostPrice(periodStockAmount.getAccountPrice());
				detail.setCostAmount(detail.getAccountQuentity().multiply(detail.getCostPrice()));
			}else{
				detail.setCostPrice(periodAmount.getAccountPrice());
				detail.setCostAmount(detail.getAccountQuentity().multiply(detail.getCostPrice()));
			}
		}
		//销售退货
		if(bill.getBillType()==42){
			detail.setCostPrice(detail.getCostPrice());
			detail.setCostAmount(NumberUtil.multiply(detail.getCostPrice(), detail.getAccountQuentity()));
		}
		//报损单
		if(bill.getBillType()==22){
			if(sovo.getValue().equals("1")){
				detail.setCostPrice(periodStockAmount.getAccountPrice());
				detail.setCostAmount(detail.getAccountQuentity().multiply(detail.getCostPrice()));
			}else{
				detail.setCostPrice(periodAmount.getAccountPrice());
				detail.setCostAmount(detail.getAccountQuentity().multiply(detail.getCostPrice()));
			}
		}
		//盘点单
		if(bill.getBillType()==20){
			detail.setCostPrice(detail.getAccountUintPrice());
			detail.setCostAmount(detail.getType());
		}
		//调仓单
		if(bill.getBillType()==21){
			if(sovo.getValue().equals("1")){
				PeriodStockAmount outPeriodStockAmount=periodStockAmountService.existRecord(peridId, outWarehouse.getFid(), goodsId, goodsSpecId);
				if(outPeriodStockAmount==null){
					outPeriodStockAmount=periodStockAmountService.initPeriodAmount(bill, detail, outWarehouse);
				}
				detail.setCostPrice(outPeriodStockAmount.getAccountPrice());
				detail.setCostAmount(detail.getAccountQuentity().multiply(detail.getCostPrice()));
			}else{	
				detail.setCostPrice(periodAmount.getAccountPrice());
				detail.setCostAmount(detail.getAccountQuentity().multiply(detail.getCostPrice()));
			}
		}
		//发货单
		if(bill.getBillType()==23){
			if(sovo.getValue().equals("1")){
				detail.setCostPrice(periodStockAmount.getAccountPrice());
				detail.setCostAmount(detail.getAccountQuentity().multiply(detail.getCostPrice()));
			}else{
				detail.setCostPrice(periodAmount.getAccountPrice());
				detail.setCostAmount(detail.getAccountQuentity().multiply(detail.getCostPrice()));
			}
		}
		//收货单
		if(bill.getBillType()==24){
			
			detail.setDeliveryCostPrice(billDetailRepo.findDetailbyDetailId(detail.getRefDetailId()));
			detail.setDeliveryCostAmount(NumberUtil.multiply(detail.getDeliveryCostPrice(),(detail.getReceivedQuantity().add(detail.getLoseQuantity()))).multiply(detail.getScale()));
			detail.setCostAmount((detail.getDeliveryCostAmount().add(detail.getTransportAmount())).subtract(detail.getDeductionAmount()));
			detail.setCostPrice(detail.getCostAmount().divide(detail.getReceivedQuantity().compareTo(BigDecimal.ZERO)==0?BigDecimal.ONE:(detail.getReceivedQuantity().multiply(detail.getScale())),8,BigDecimal.ROUND_HALF_UP));
			detail.setLoseAmount(NumberUtil.multiply(detail.getCostPrice(), detail.getLoseQuantity(), detail.getScale()));
		}
		detail.setCostUnitPrice(detail.getCostPrice());
		billDetailRepo.save(detail);
		
	}
	/**
	 * 根据计划查询仓库货品
	 * @param planId
	 * @return
	 */
	public List<WarehouseBillDetail> findGoodsByPlan(String planId){
		return billDetailRepo.findGoodsByPlan(planId);
	}
	/**
	 * 获取用户销售提成明细
	 * @param memberId
	 * @return
	 */
	public ResultObject getPercentageDetails(String memberId,PageParamater pageParamater){
		int currentPage = pageParamater.getPage(); //当前页
		int pageSize = pageParamater.getRows(); //分页大小
		int start = (currentPage - 1) * pageSize; //起始位置
		String orgId=SecurityUtil.getCurrentOrgId();
		String accId=SecurityUtil.getFiscalAccountId();
		PageRequest request = getPageRequest(pageParamater);
		List<WarehouseBillDetail> entitys=billDetailRepo.getPercentageDetailsByMemberId(memberId, orgId, accId);
		
		
		
		List<Object[]> datas=Lists.newArrayList();
		for(WarehouseBillDetail entity:entitys){
			WarehouseBill bill=entity.getBill();
			Object[] o=new Object[6];
			o[0]=bill.getCode();
			o[1]=DateUtil.format(bill.getBillDate());
			o[2]=bill.getInMember().getUsername();
			o[3]=bill.getTotalAmount();
			o[4]=entity.getPercentage();
			o[5]=entity.getPercentageAmount();
			datas.add(o);
		}
		int totalRows=datas.size();
		PageBean pageBean = new PageBean(totalRows, pageSize, currentPage);
		ResultObject resultObject = new ResultObject(datas, pageBean);
		return resultObject;
	}
}
