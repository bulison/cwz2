package cn.fooltech.fool_ops.domain.warehouse.service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import cn.fooltech.fool_ops.domain.freight.entity.FreightAddress;
import cn.fooltech.fool_ops.domain.freight.service.FreightAddressService;
import cn.fooltech.fool_ops.domain.payment.service.PaymentCheckService;
import cn.fooltech.fool_ops.domain.payment.service.PaymentCheckService.BillType;
import cn.fooltech.fool_ops.domain.rate.service.SaleOutRateService;
import cn.fooltech.fool_ops.domain.transport.entity.TransportBilldetail;
import cn.fooltech.fool_ops.domain.transport.service.TransportBilldetailService;
import cn.fooltech.fool_ops.domain.transport.vo.TransportBilldetailVo;
import cn.fooltech.fool_ops.domain.warehouse.strategy.CalMethod;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttrType;
import cn.fooltech.fool_ops.domain.basedata.entity.Customer;
import cn.fooltech.fool_ops.domain.basedata.entity.CustomerSupplierView;
import cn.fooltech.fool_ops.domain.basedata.entity.Goods;
import cn.fooltech.fool_ops.domain.basedata.entity.GoodsPercentage;
import cn.fooltech.fool_ops.domain.basedata.entity.GoodsSpec;
import cn.fooltech.fool_ops.domain.basedata.entity.Member;
import cn.fooltech.fool_ops.domain.basedata.entity.Supplier;
import cn.fooltech.fool_ops.domain.basedata.entity.Unit;
import cn.fooltech.fool_ops.domain.basedata.service.AuxiliaryAttrService;
import cn.fooltech.fool_ops.domain.basedata.service.CustomerService;
import cn.fooltech.fool_ops.domain.basedata.service.GoodsPercentageService;
import cn.fooltech.fool_ops.domain.basedata.service.GoodsService;
import cn.fooltech.fool_ops.domain.basedata.service.GoodsSpecService;
import cn.fooltech.fool_ops.domain.basedata.service.SupplierService;
import cn.fooltech.fool_ops.domain.basedata.service.UnitService;
import cn.fooltech.fool_ops.domain.basedata.vo.GoodsPercentageVo;
import cn.fooltech.fool_ops.domain.capital.entity.CapitalPlan;
import cn.fooltech.fool_ops.domain.capital.entity.CapitalPlanDetail;
import cn.fooltech.fool_ops.domain.capital.service.CapitalPlanDetailService;
import cn.fooltech.fool_ops.domain.capital.service.CapitalPlanService;
import cn.fooltech.fool_ops.domain.common.entity.ImportVoBean;
import cn.fooltech.fool_ops.domain.flow.service.TaskBillService;
import cn.fooltech.fool_ops.domain.flow.vo.TaskBillVo;
import cn.fooltech.fool_ops.domain.member.service.MemberService;
import cn.fooltech.fool_ops.domain.payment.entity.PaymentBill;
import cn.fooltech.fool_ops.domain.payment.service.PaymentBillService;
import cn.fooltech.fool_ops.domain.payment.service.PaymentCheckService;
import cn.fooltech.fool_ops.domain.payment.vo.PaymentBillVo;
import cn.fooltech.fool_ops.domain.payment.vo.PaymentCheckVo;
import cn.fooltech.fool_ops.domain.period.entity.StockPeriod;
import cn.fooltech.fool_ops.domain.period.service.StockPeriodService;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.sysman.service.OrgService;
import cn.fooltech.fool_ops.domain.sysman.service.SmgOrgAttrService;
import cn.fooltech.fool_ops.domain.sysman.vo.SmgOrgAttrVo;
import cn.fooltech.fool_ops.domain.warehouse.entity.BillRelation;
import cn.fooltech.fool_ops.domain.warehouse.entity.OutStorage;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBillDetail;
import cn.fooltech.fool_ops.domain.warehouse.repository.WarehouseBillRepository;
import cn.fooltech.fool_ops.domain.warehouse.service.bill.IWareHouseWebService;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.domain.warehouse.vo.StockLockingVo;
import cn.fooltech.fool_ops.domain.warehouse.vo.WarehouseBillDetailVo;
import cn.fooltech.fool_ops.domain.warehouse.vo.WarehouseBillVo;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.utils.ErrorCode;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.VoFactory;
import cn.fooltech.fool_ops.validator.ValidatorUtils;
import cn.fooltech.fool_ops.validator.bill.BillClassTransferUtils;
import cn.fooltech.fool_ops.validator.bill.Fhd;
import net.sf.json.JSONArray;

@Service("ops.WarehouseBillService")
public class WarehouseBillService extends BaseService<WarehouseBill, WarehouseBillVo, String>
		implements IWareHouseWebService {

	private Logger logger = LoggerFactory.getLogger(WarehouseBillService.class);

	@Autowired
	protected WarehouseBillRepository billRepo;

	@Autowired
	protected WarehouseBillDetailService billDetailService;

	@Autowired
	protected AuditService auditService;

	@Autowired
	protected BillRelationService relationService;

	/**
	 * 辅助属性服务类
	 */
	@Autowired
	protected AuxiliaryAttrService attrService;

	@Autowired
	private SaleOutRateService saleOutRateService;

	/**
	 * 客户服务类
	 */
	@Autowired
	protected CustomerService customerService;

	/**
	 * 供应商服务类
	 */
	@Autowired
	protected SupplierService supplierService;

	/**
	 * 机构服务类
	 */
	@Autowired
	protected OrgService orgService;

	/**
	 * 人员服务类
	 */
	@Autowired
	protected MemberService memberService;

	/**
	 * 会计期间服务类
	 */
	@Autowired
	protected StockPeriodService stockPeriodService;

	/**
	 * 货品服务类
	 */
	@Autowired
	protected GoodsService goodsService;

	/**
	 * 货品属性服务类
	 */
	@Autowired
	protected GoodsSpecService goodsSpecService;

	/**
	 * 单位服务类
	 */
	@Autowired
	protected UnitService unitService;

	/**
	 * 仓库单据审核时，校验货品价格的工具类
	 */
	@Autowired
	protected PriceVerifyService priceVerifyService;

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

	/**
	 * 即时分仓库存服务类
	 */
	@Autowired
	protected StockStoreService stockStoreService;

	@Autowired
	protected StockLockingService stockLockingService;

	@Autowired
	protected PaymentCheckService paycheckService;

	/**
	 * 收付款单服务类
	 */
	@Autowired
	private PaymentBillService paymentBillService;
	/**
	 * 收付款对单网页服务类
	 */
	@Autowired
	private PaymentCheckService paymentCheckService;

	@Autowired
	private FreightAddressService freightAddressService;

	@Autowired
	private TransportBilldetailService transportBilldetailService;

	@Autowired
	private SmgOrgAttrService smgOrgAttrService;
	
	@Autowired
	private CapitalPlanService capitalPlanService;

	/**
	 * 计划事件关联单据 服务类
	 */
	@Autowired
	private TaskBillService taskBillService;
	
	/**
	 * 货品提成服务类 
	 */
	@Autowired
	private GoodsPercentageService percentageService;

	@Override
	public WarehouseBillVo getVo(WarehouseBill entity) {
		return getVo(entity, Boolean.FALSE);
	}

	/**
	 * 添加是否查询子表的条件
	 * 
	 * @param entity
	 * @param transfer
	 *            是否加载单据明细
	 * @return
	 */
	public WarehouseBillVo getVo(WarehouseBill entity, boolean transfer) {
		WarehouseBillVo vo = new WarehouseBillVo();
		// 审核人
		User auditor = entity.getAuditor();
		if (auditor != null) {
			vo.setAuditorId(auditor.getFid());
			vo.setAuditorName(auditor.getUserName());
		}
		// 作废人
		User canceler = entity.getCancelor();
		if (canceler != null) {
			vo.setCancelorName(canceler.getUserName());
		}
		// 创建人
		User creator = entity.getCreator();
		if (creator != null) {
			vo.setCreateId(creator.getFid());
			vo.setCreatorName(creator.getUserName());
		}
		// 客户
		Customer customer = entity.getCustomer();
		if (customer != null) {
			vo.setCustomerId(customer.getFid());
			vo.setCustomerCode(customer.getCode());
			vo.setCustomerName(customer.getName());
			vo.setCustomerPhone(customer.getPhone());
			vo.setCustomerAddress(customer.getAddress());
			vo.setCsvId(customer.getFid());
			vo.setCsvName(customer.getName());
			vo.setCsvType(CustomerSupplierView.TYPE_CUSTOMER);
			vo.setCustomerContact(customer.getBusinessContact());
		}
		// 部门
		Organization dept = entity.getDept();
		if (dept != null) {
			vo.setDeptId(dept.getFid());
			vo.setDeptName(dept.getOrgName());
		}
		// 收货人
		Member inMember = entity.getInMember();
		if (inMember != null) {
			vo.setInMemberId(inMember.getFid());
			vo.setInMemberName(inMember.getUsername());
		}
		// 发货人
		Member outMember = entity.getOutMember();
		if (outMember != null) {
			vo.setOutMemberId(outMember.getFid());
			vo.setOutMemberName(outMember.getUsername());
		}
		// 进仓仓库
		AuxiliaryAttr inWareHouse = entity.getInWareHouse();
		if (inWareHouse != null) {
			vo.setInWareHouseId(inWareHouse.getFid());
			vo.setInWareHouseName(inWareHouse.getName());
		}
		// 出仓仓库
		AuxiliaryAttr outWareHouse = entity.getOutWareHouse();
		if (outWareHouse != null) {
			vo.setOutWareHouseId(outWareHouse.getFid());
			vo.setOutWareHouseName(outWareHouse.getName());
		}
		// 会计期间
		StockPeriod stockPeriod = entity.getStockPeriod();
		if (stockPeriod != null) {
			vo.setStockPeriodId(stockPeriod.getFid());
			vo.setStockPeriodDetail(stockPeriod.getPeriod());
		}
		// 供应商
		Supplier supplier = entity.getSupplier();
		if (supplier != null) {
			vo.setSupplierId(supplier.getFid());
			vo.setSupplierCode(supplier.getCode());
			vo.setSupplierName(supplier.getName());
			vo.setSupplierPhone(supplier.getPhone());
			vo.setSupplierAddress(supplier.getAddress());
			vo.setCsvId(supplier.getFid());
			vo.setCsvName(supplier.getName());
			vo.setCsvType(CustomerSupplierView.TYPE_SUPPLIER);
			vo.setSupplierContact(supplier.getBusinessContact());
		}

		// 仓库单据关联
		List<BillRelation> relationList = relationService.getRelation(entity.getFid());
		String relationIds = "";
		String relationCodes = "";
		Joiner joiner = Joiner.on(",").skipNulls();
		for (BillRelation relation : relationList) {
			WarehouseBill refBill = billRepo.findOne(relation.getRefBillId());
			if (refBill != null) {
				if (!Strings.isNullOrEmpty(relationCodes)) {
					relationIds = joiner.join(relationIds, refBill.getFid());
					relationCodes = joiner.join(relationCodes, refBill.getCode());
				} else {
					relationIds = refBill.getFid();
					relationCodes = refBill.getCode();
				}
			}
		}
		vo.setRelationId(relationIds);
		vo.setRelationName(relationCodes);

		// 仓库单据明细
		if (transfer) {
			List<WarehouseBillDetailVo> details = billDetailService.getBillDetailVos(entity.getFid());
			vo.setDetails(JSONArray.fromObject(details).toString());

			if (entity.getBillType() == WarehouseBuilderCodeHelper.fhd
					|| entity.getBillType() == WarehouseBuilderCodeHelper.shd) {
				List<TransportBilldetailVo> tdetails = transportBilldetailService
						.getTransportBillDetailsVo(entity.getFid());
				vo.setTransportDetails(JSONArray.fromObject(tdetails).toString());
			}
		}

		vo.setFid(entity.getFid());
		vo.setCode(entity.getCode());
		vo.setEndDate(entity.getEndDate());
		vo.setBillDate(entity.getBillDate());
		vo.setBillType(entity.getBillType());
		vo.setDescribe(entity.getDescribe());
		vo.setAuditTime(DateUtilTools.time2String(entity.getAuditTime()));
		vo.setCancelTime(DateUtilTools.time2String(entity.getCancelTime()));
		vo.setCreateTime(DateUtilTools.time2String(entity.getCreateTime()));
		vo.setVoucherCode(entity.getVoucherCode());
		vo.setRecordStatus(entity.getRecordStatus());
		vo.setFreeAmount(NumberUtil.bigDecimalToStr(NumberUtil.scale(entity.getFreeAmount(), 2)));
		vo.setTotalAmount(NumberUtil.bigDecimalToStr(NumberUtil.scale(entity.getTotalAmount(), 2)));
		vo.setTotalPayAmount(NumberUtil.bigDecimalToStr(NumberUtil.scale(entity.getTotalPayAmount(), 2)));
		vo.setExpenseAmount(NumberUtil.bigDecimalToStr(NumberUtil.scale(entity.getExpenseAmount(), 2)));
		vo.setUpdateTime(DateUtilTools.time2String(entity.getUpdateTime()));
		vo.setPlanStart(entity.getPlanStart());
		vo.setOtherCharges(NumberUtil.bigDecimalToStr(NumberUtil.scale(entity.getOtherCharges(), 2)));
		vo.setProductionStatus(entity.getProductionStatus());
		vo.setDetal(auditService.getBillTag(entity).toString());

		vo.setTransportNo(entity.getTransportNo());
		vo.setCarNo(entity.getCarNo());
		vo.setDriverName(entity.getDriverName());
		vo.setDriverPhone(entity.getDriverPhone());
		vo.setDeductionAmount(entity.getDeductionAmount());

		FreightAddress deliveryPlace = entity.getDeliveryPlace();
		if (deliveryPlace != null) {
			vo.setDeliveryPlaceName(deliveryPlace.getName());
			vo.setDeliveryPlaceId(deliveryPlace.getFid());
		}

		FreightAddress receiptPlace = entity.getReceiptPlace();
		if (receiptPlace != null) {
			vo.setReceiptPlaceName(receiptPlace.getName());
			vo.setReceiptPlaceId(receiptPlace.getFid());
		}

		AuxiliaryAttr transportType = entity.getTransportType();
		if (transportType != null) {
			vo.setTransportTypeName(transportType.getName());
			vo.setTransportTypeId(transportType.getFid());
		}

		AuxiliaryAttr shipmentType = entity.getShipmentType();
		if (shipmentType != null) {
			vo.setShipmentTypeName(shipmentType.getName());
			vo.setShipmentTypeId(shipmentType.getFid());
		}

		return vo;
	}

	public List<WarehouseBillVo> getVos(List<WarehouseBill> entities, boolean transfer) {
		List<WarehouseBillVo> vos = new ArrayList<WarehouseBillVo>();
		if (CollectionUtils.isNotEmpty(entities)) {
			for (WarehouseBill entity : entities) {
				vos.add(getVo(entity, transfer));
			}
		}
		return vos;
	}

	@Override
	public CrudRepository<WarehouseBill, String> getRepository() {
		return billRepo;
	}


	/**
	 * 删除
	 * @param id 实体ID
	 * @return
	 */
	@Override
	@Transactional
	public RequestResult delete(String id) {
		WarehouseBill entity = billRepo.findOne(id);
		//会计期间
		RequestResult periodCheck = canOperateByPeriod(entity.getBillDate());
		if(periodCheck.getReturnCode() == RequestResult.RETURN_FAILURE){
			return periodCheck;
		}

		RequestResult result = new RequestResult();
		if(entity.getRecordStatus() == WarehouseBill.STATUS_AUDITED){
			result.setMessage("无效操作，单据处于已审核状态!");
			result.setReturnCode(RequestResult.RETURN_FAILURE);
		}
		else if(relationService.isAssociated(id)){
			result.setMessage("无效操作，该单据已被关联!");
			result.setReturnCode(RequestResult.RETURN_FAILURE);
		}
		else{
			billDetailService.deleteByBill(id);
			billRepo.delete(id);
			relationService.deleteRelation(id);
		}
		return result;
	}

	/**
	 * 保存或新增
	 * 
	 * @param vo
	 * @return
	 */
	@Transactional
	public RequestResult save(WarehouseBillVo vo) {

		String fid = vo.getFid();
		String code = vo.getCode();
		String voucherCode = vo.getVoucherCode();// 凭证号
		Integer billType = vo.getBillType();// 单据类型
		Date endDate = vo.getEndDate();// 计划完成时间
		Date billDate = vo.getBillDate();// 单据日期
		String freeAmount = vo.getFreeAmount();
		String describe = vo.getDescribe();
		String inWareHouseId = vo.getInWareHouseId();// 进仓仓库ID
		String outWareHouseId = vo.getOutWareHouseId();// 出仓仓库ID
		String customerId = vo.getCustomerId();// 客户ID
		String supplierId = vo.getSupplierId();// 供应商ID
		String deptId = vo.getDeptId();// 部门ID
		String inMemberId = vo.getInMemberId();// 收货人ID
		String outMemberId = vo.getOutMemberId();// 发货人ID
		String otherCharge = vo.getOtherCharges();
		Integer productionStatus = vo.getProductionStatus();//
		Date planStart = vo.getPlanStart();
		Date now = new Date();
		// 校验数据
		RequestResult result = checkByRule(vo);
		if (result.getReturnCode() == RequestResult.RETURN_FAILURE) {
			return result;
		}
		// 判断收货单是否超过亏损数量预警
		if (vo.getBillType().equals(24)) {
			List<WarehouseBillDetailVo> detailList = vo.getDetailList();
			for (WarehouseBillDetailVo warehouseBillDetailVo : detailList) {
				String key="RECEIPT_LOSS_WARNING";//亏损数量预警阈值
				SmgOrgAttrVo smgOrgAttrVo = smgOrgAttrService.queryByOrg(SecurityUtil.getCurrentOrgId(),key);
				if (smgOrgAttrVo != null) {
					String value = smgOrgAttrVo.getValue()==null?"0":smgOrgAttrVo.getValue();
					BigDecimal loseQuantity = warehouseBillDetailVo.getLoseQuantity()==null?BigDecimal.ZERO:warehouseBillDetailVo.getLoseQuantity();
					int compareTo = loseQuantity.compareTo(new BigDecimal(value));
					if (compareTo >= 0)
						return buildFailRequestResult("收货单亏损数量不能超过预警数量【" + value + "】");
				}

			}
		}

		WarehouseBill entity = null;
		if (StringUtils.isBlank(fid)) {
			entity = new WarehouseBill();
			entity.setCreateTime(now);
			entity.setUpdateTime(now);
			entity.setOrg(SecurityUtil.getCurrentOrg());
			entity.setCreator(SecurityUtil.getCurrentUser());
			entity.setCreatorDept(SecurityUtil.getCurrentDept());
			entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
		} else {
			entity = billRepo.findOne(fid);
			if (entity == null) {
				return new RequestResult(RequestResult.RETURN_FAILURE, "该记录不存在或已被删除!");
			}
			entity.setUpdateTime(now);
			// 保存时关联单据为空，清楚关联数据表
			String relationId = vo.getRelationId();// 关联单据
			if (Strings.isNullOrEmpty(relationId))
				relationService.deleteRelation(fid);
		}
		entity.setCode(code.trim());
		entity.setEndDate(endDate);
		entity.setBillDate(billDate);
		entity.setDescribe(describe);
		entity.setBillType(billType);
		entity.setVoucherCode(voucherCode);
		entity.setFreeAmount(NumberUtil.toBigDeciaml(freeAmount));
		entity.setOtherCharges(NumberUtil.toBigDeciaml(otherCharge));
		if (productionStatus != null) {
			entity.setProductionStatus(productionStatus);
		} else {
			entity.setProductionStatus(WarehouseBill.NOT_STARTED);
		}
		entity.setPlanStart(planStart);
		// 会计期间
		StockPeriod stockPeriod = stockPeriodService.getPeriod(billDate, SecurityUtil.getFiscalAccountId());
		entity.setStockPeriod(stockPeriod);
		String deliveryPlaceId = vo.getDeliveryPlaceId();// 发货地
		String receiptPlaceId = vo.getReceiptPlaceId();// 收货地
		String transportTypeId = vo.getTransportTypeId();// 运输方式
		String shipmentTypeId = vo.getShipmentTypeId();// 装运方式
		// 发货地
		if (!Strings.isNullOrEmpty(deliveryPlaceId)) {
			FreightAddress info = freightAddressService.findOne(deliveryPlaceId);
			entity.setDeliveryPlace(info);
		}
		// 收货地
		if (!Strings.isNullOrEmpty(receiptPlaceId)) {
			FreightAddress info = freightAddressService.findOne(receiptPlaceId);
			entity.setReceiptPlace(info);
		}
		// 运输方式
		if (!Strings.isNullOrEmpty(transportTypeId)) {
			AuxiliaryAttr attr = attrService.findOne(transportTypeId);
			entity.setTransportType(attr);
		}
		// 装运方式
		if (!Strings.isNullOrEmpty(shipmentTypeId)) {
			AuxiliaryAttr attr = attrService.findOne(shipmentTypeId);
			entity.setShipmentType(attr);
			;
		}
		// 进仓仓库
		if (StringUtils.isNotBlank(inWareHouseId)) {
			entity.setInWareHouse(attrService.get(inWareHouseId));
		} else {
			entity.setInWareHouse(null);
		}
		// 出仓仓库
		if (StringUtils.isNotBlank(outWareHouseId)) {
			entity.setOutWareHouse(attrService.get(outWareHouseId));
		} else {
			entity.setOutWareHouse(null);
		}
		// 客户
		if (StringUtils.isNotBlank(customerId)) {
			entity.setCustomer(customerService.get(customerId));
		} else {
			entity.setCustomer(null);
		}
		// 供应商
		if (StringUtils.isNotBlank(supplierId)) {
			entity.setSupplier(supplierService.get(supplierId));
		} else {
			entity.setSupplier(null);
		}
		// 部门
		if (StringUtils.isNotBlank(deptId)) {
			entity.setDept(orgService.get(deptId));
		} else {
			entity.setDept(null);
		}
		// 收货人
		if (StringUtils.isNotBlank(inMemberId)) {
			entity.setInMember(memberService.get(inMemberId));
		} else {
			entity.setInMember(null);
		}
		// 发货人
		if (StringUtils.isNotBlank(outMemberId)) {
			entity.setOutMember(memberService.get(outMemberId));
		} else {
			entity.setOutMember(null);
		}
		entity.setTransportNo(vo.getTransportNo());
		entity.setCarNo(vo.getCarNo());
		entity.setDriverName(vo.getDriverName());
		entity.setDriverPhone(vo.getDriverPhone());

		billRepo.save(entity);
		// 新增、编辑仓库单据明细
		List<WarehouseBillDetail> details = saveBillDetail(entity, vo.getDetailList(),vo.getRelationId());

		// 单据的合计金额
		BigDecimal totalAmount = calculateTotalAmount(entity.getBillType(), details);
		entity.setTotalAmount(totalAmount);

		billRepo.save(entity);

		Map<String, Object> map = Maps.newHashMap();
		map.put("updateTime", DateUtilTools.time2String(entity.getUpdateTime()));
		result.setData(entity.getFid());
		result.setDataExt(map);
		return result;
	}

	List<Integer> commonTypes = Lists.newArrayList(WarehouseBuilderCodeHelper.cgxjd, WarehouseBuilderCodeHelper.cgdd,
			WarehouseBuilderCodeHelper.xsbjd, WarehouseBuilderCodeHelper.xsdd, WarehouseBuilderCodeHelper.dcd,
			WarehouseBuilderCodeHelper.pdd);

	/**
	 * 新增、编辑仓库单据明细
	 * 
	 * @param bill
	 * @param details
	 */
	public List<WarehouseBillDetail> saveBillDetail(WarehouseBill bill, List<WarehouseBillDetailVo> details,String refDetailId) {
		Assert.notNull(bill, "仓库单据不能为空!");
		billDetailService.deleteByBill(bill.getFid()); // 清空单据下的所有明细记录

		if (CollectionUtils.isNotEmpty(details)) {
			for (WarehouseBillDetailVo detail : details) {
				if (commonTypes.contains(bill.getBillType())) {
					// 单据明细中的仓库属性取自单据的仓库属性
					AuxiliaryAttr inWareHouse = bill.getInWareHouse();
					AuxiliaryAttr outWareHouse = bill.getOutWareHouse();
					if (inWareHouse != null) {
						detail.setInWareHouseId(inWareHouse.getFid());
					}
					if (outWareHouse != null) {
						detail.setOutWareHouseId(outWareHouse.getFid());
					}
				}
				detail.setFid(null);
				detail.setBillId(bill.getFid());
//				detail.setRefDetailId(refDetailId);
				billDetailService.save(detail,bill);
			}
		}
		// 修改货品的已使用标识
		List<WarehouseBillDetail> detailList = billDetailService.getBillDetails(bill.getFid());
		goodsService.setGoodsUsed(detailList);
		return detailList;
	}

	/**
	 * 计算单据的合计金额
	 * 
	 * @param details
	 *            单据明细集合
	 * @return
	 */
	public BigDecimal calculateTotalAmount(int billType, List<WarehouseBillDetail> details) {
		BigDecimal total = BigDecimal.ZERO;
		if (CollectionUtils.isNotEmpty(details)) {
			for (WarehouseBillDetail detail : details) {
				if (billType == WarehouseBuilderCodeHelper.cgfp || billType == WarehouseBuilderCodeHelper.xsfp) {
					BigDecimal accountAmount = detail.getTaxAmount();
					if (accountAmount != null) {
						total = total.add(accountAmount);
					}
				} else if (billType == WarehouseBuilderCodeHelper.shd || billType == WarehouseBuilderCodeHelper.fhd) {
					BigDecimal accountAmount = detail.getTransportAmount();
					if (accountAmount != null) {
						total = total.add(accountAmount);
					}
				} else {
					BigDecimal accountAmount = detail.getType();
					if (accountAmount != null) {
						total = total.add(accountAmount);
					}
				}
			}
		}
		return total;
	}

	/**
	 * 根据规则校验数据
	 * 
	 * @param vo
	 * @return
	 */
	public RequestResult checkByRule(WarehouseBillVo vo) {

		// 验证主表字段
		Class clazz = BillClassTransferUtils.getSupportClass(vo.getBillType());
		String inValid = null;
		if (clazz != null) {
			inValid = ValidatorUtils.inValidMsg(vo, clazz);
		} else {
			inValid = ValidatorUtils.inValidMsg(vo);
		}
		if (inValid != null) {
			return buildFailRequestResult(inValid);
		}

		// 验证明细
		List<WarehouseBillDetailVo> detailVos = vo.getDetailList();
		for (WarehouseBillDetailVo detailVo : detailVos) {
			if (clazz != null) {
				inValid = ValidatorUtils.inValidMsg(detailVo, clazz);
			} else {
				inValid = ValidatorUtils.inValidMsg(detailVo);
			}
			if (inValid != null) {
				return buildFailRequestResult(inValid);
			}

			String goodsId = detailVo.getGoodsId();// 货品ID
			String goodsSpecId = detailVo.getGoodsSpecId();// 货品属性ID
			String unitId = detailVo.getUnitId();// 货品单位
			String inWareHouseId = detailVo.getInWareHouseId();// 进仓仓库ID
			String outWareHouseId = detailVo.getOutWareHouseId();// 出仓仓库ID

			Goods goods = null;
			if (!Strings.isNullOrEmpty(goodsId)) {
				goods = goodsService.get(goodsId);
				if (goods == null) {
					return buildFailRequestResult("货品填写错误或已被删除");
				}
			}
			Unit unit = null;
			if (!Strings.isNullOrEmpty(unitId)) {
				unit = unitService.get(unitId);
				if (unit == null) {
					return buildFailRequestResult("单位填写错误或已被删除");
				}
				if (!unit.getParent().getFid().equals(goods.getUnitGroup().getFid())) {
					return buildFailRequestResult("单位组不对应货品单位组");
				}
			}
			GoodsSpec spec = null;
			if (!Strings.isNullOrEmpty(goodsSpecId)) {
				spec = goodsSpecService.get(goodsSpecId);
				if (spec == null) {
					return buildFailRequestResult("货品属性填写错误或已被删除");
				}
				if (goods.getGoodsSpec() == null) {
					return buildFailRequestResult("货品没有属性");
				}
				if (!spec.getParent().getFid().equals(goods.getGoodsSpec().getFid())) {
					return buildFailRequestResult("货品属性组不对应货品属性组");
				}
			}
			AuxiliaryAttr inWarehouse = null;
			if (!Strings.isNullOrEmpty(inWareHouseId)) {
				inWarehouse = attrService.get(inWareHouseId);
				if (inWarehouse == null) {
					return buildFailRequestResult("进仓仓库填写错误或已被删除");
				}
			}

			AuxiliaryAttr outWarehouse = null;
			if (!Strings.isNullOrEmpty(outWareHouseId)) {
				outWarehouse = attrService.get(outWareHouseId);
				if (outWarehouse == null) {
					return buildFailRequestResult("出仓仓库填写错误或已被删除");
				}
			}
		}

		RequestResult periodCheck = canOperateByPeriod(vo.getBillDate());
		if (periodCheck.getReturnCode() == RequestResult.RETURN_FAILURE) {
			return periodCheck;
		}

		RequestResult result = new RequestResult();
		if (!checkDataRealTime(vo)) {
			result.setMessage("页面数据失效，请重新刷新页面!");
			result.setReturnCode(RequestResult.RETURN_FAILURE);
			return result;
		}

		String accId = SecurityUtil.getFiscalAccountId();

		if (isCodeExist(accId, vo.getCode(), vo.getFid())) {
			result.setMessage("单号已存在!");
			result.setReturnCode(RequestResult.RETURN_FAILURE);
			return result;
		}
		if (isVoucherCodeExist(accId, vo.getVoucherCode(), vo.getFid(), vo.getBillDate(), vo.getBillType())) {
			result.setMessage("原始单号已存在!");
			result.setReturnCode(RequestResult.RETURN_FAILURE);
			return result;
		}
		if (StringUtils.isNotBlank(vo.getFid()) && !canOperateByStatus(vo.getFid())) {
			result.setMessage("无效操作，单据处于已审核状态!");
			result.setReturnCode(RequestResult.RETURN_FAILURE);
			return result;
		}

		// 收货单关联的发货单的日期一定要小于收货单日期
		if (vo.getBillType() == WarehouseBuilderCodeHelper.shd) {
			WarehouseBill fhdBill = billRepo.findOne(vo.getRelationId());
			if (fhdBill.getBillDate().compareTo(vo.getBillDate()) > 0) {
				return buildFailRequestResult("收货单关联的发货单的日期一定要小于收货单日期");
			}
		}

		return result;
	}

	/**
	 * 判断编号是否存在
	 * 
	 * @param accId
	 * @param code
	 * @param excludeId
	 * @return
	 */
	public boolean isCodeExist(String accId, String code, String excludeId) {
		if (Strings.isNullOrEmpty(code))
			return false;
		Long count = null;
		if (Strings.isNullOrEmpty(excludeId)) {
			count = billRepo.countByCode(accId, code);
		} else {
			count = billRepo.countByCode(accId, code, excludeId);
		}
		if (count != null && count > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 判断原始单号是否存在
	 * 
	 * @param accId
	 * @param voucherCode
	 * @param excludeId
	 * @param billDate
	 *            单据日期
	 * @return
	 */
	public boolean isVoucherCodeExist(String accId, String voucherCode, String excludeId, Date billDate, int billType) {
		if (Strings.isNullOrEmpty(voucherCode))
			return false;
		Long count = null;
		Date start = null, end = null;

		if (billType == WarehouseBuilderCodeHelper.qckc) {
			StockPeriod period = stockPeriodService.findFirstPeriod();
			start = period.getStartDate();
			end = period.getEndDate();
			end = DateUtilTools.changeDateTime(end, 0, 0, 23, 59, 59);
		} else {
			String date = DateUtils.getStringByFormat(billDate, "yyyy-MM-dd");
			start = DateUtils.getDateFromString(date + " 00:00:00");
			end = DateUtils.getDateFromString(date + " 23:59:59");
		}

		if (Strings.isNullOrEmpty(excludeId)) {
			count = billRepo.countByVoucherCode(accId, voucherCode, start, end);
		} else {
			count = billRepo.countByVoucherCode(accId, voucherCode, excludeId, start, end);
		}
		if (count != null && count > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 根据会计期间，判断单据是否可以进行增删改操作
	 * 
	 * @param date
	 *            单据日期
	 * @return
	 */
	public RequestResult canOperateByPeriod(Date date) {
		Assert.notNull("单据日期不能为空!");
		StockPeriod stockPeriod = stockPeriodService.getPeriod(date, SecurityUtil.getFiscalAccountId());
		if (stockPeriod == null) {
			return new RequestResult(RequestResult.RETURN_FAILURE, ErrorCode.STOCK_PERIOD_NOT_EXIST, "会计期间不存在!");
		}
		if (stockPeriod.getCheckoutStatus() == StockPeriod.CHECKED) {
			return new RequestResult(RequestResult.RETURN_FAILURE, ErrorCode.STOCK_PERIOD_CHECKED, "会计期间已结账!");
		}
		if (stockPeriod.getCheckoutStatus() == StockPeriod.UN_USED) {
			return new RequestResult(RequestResult.RETURN_FAILURE, ErrorCode.STOCK_PERIOD_UN_USED, "会计期间未启用!");
		}
		return new RequestResult();
	}

	/**
	 * 更新操作时，校验数据的实时性
	 * 
	 * @param vo
	 *            主键、更新时间
	 * @return true 有效 false 无效
	 */
	public boolean checkDataRealTime(WarehouseBillVo vo) {
		if (StringUtils.isNotBlank(vo.getFid())) {
			WarehouseBill entity = billRepo.findOne(vo.getFid());
			Date formDate = DateUtilTools.string2Time(vo.getUpdateTime());
			int num = formDate.compareTo(entity.getUpdateTime());
			return num == 0;
		}
		return true;
	}

	/**
	 * 根据状态，判断仓库单据是否可以进行修改，删除，增加单据明细操作
	 * 
	 * @param billId
	 *            仓库单据ID
	 * @return
	 */
	public boolean canOperateByStatus(String billId) {
		WarehouseBill bill = get(billId);
		if (bill.getRecordStatus() == WarehouseBill.STATUS_AUDITED) {
			return false;
		}
		return true;
	}

	/**
	 * 列表查询
	 * 
	 * @param vo
	 * @param paramater
	 * @return
	 */
	public Page<WarehouseBillVo> query(WarehouseBillVo vo, PageParamater paramater) {
		Sort sort = new Sort(Direction.DESC, "code");
		PageRequest pageRequest = getPageRequest(paramater, sort);
		String accId = SecurityUtil.getFiscalAccountId();
		Page<WarehouseBill> billPage = billRepo.findPageBy(accId, vo, pageRequest);
		return getPageVos(billPage, pageRequest);
	}

	@Override
	@Transactional
	public RequestResult passAudit(String id, StockLockingVo vo) {
		RequestResult result;
		try {
			result = new RequestResult();
			WarehouseBill bill = billRepo.findOne(id);
			if (bill == null) {
				return new RequestResult(RequestResult.RETURN_FAILURE, "该记录不存在或已被删除!");
			}
			if (bill.getRecordStatus() != WarehouseBill.STATUS_UNAUDITED) {
				result.setMessage("无效操作，非待审核状态!");
				result.setReturnCode(RequestResult.RETURN_FAILURE);
				return result;
			}
			if (StockPeriod.CHECKED == bill.getStockPeriod().getCheckoutStatus()) {
				result.setMessage("无效操作，会计期间已结账!");
				result.setReturnCode(RequestResult.RETURN_FAILURE);
				return result;
			}

			// 根据权限校验货品价格
			RequestResult verifyResult = priceVerifyService.verify(SecurityUtil.getCurrentUserId(), bill);
			if (verifyResult.getReturnCode() == RequestResult.RETURN_FAILURE) {
				return verifyResult;
			}

			// 判断库存是否足够
			List<WarehouseBillDetail> details = billDetailService.getBillDetails(id);

			Map<String, Object> resultMap = auditService.checkStock(bill, details);
			boolean isEnough = (Boolean) resultMap.get("isEnough");
			if (!isEnough) {
				List<Object[]> list = (List<Object[]>) resultMap.get("detail");
				StringBuffer bufferStr = new StringBuffer();
				for (Object[] obj : list) {
					String goodsName = goodsService.get((String) obj[0]).getName();
					bufferStr.append(goodsName);
					if (StringUtils.isNotBlank((String) obj[1])) {
						String goodsSpecName = goodsSpecService.get((String) obj[1]).getName();
						bufferStr.append("(").append(goodsSpecName).append(")");
					}
					bufferStr.append("库存不足<br>");
				}
				result.setMessage(bufferStr.toString());
				result.setReturnCode(RequestResult.RETURN_FAILURE);
				return result;
			}

			// 库存锁定处理
			RequestResult stockLockResult = stockLockingService.handle(bill, vo);
			if (stockLockResult.getReturnCode() == RequestResult.RETURN_FAILURE) {
				return stockLockResult;
			}

			// 收货单的判断条件
			if (bill.getBillType() == WarehouseBuilderCodeHelper.shd) {
				RequestResult check = checkSame(id);
				if (!check.isSuccess()) {
					return check;
				}
			}

			// 处理出入库库存
			// List<OutStorage> list = auditService.handleAudit(bill, details);

			// 处理总库存、分仓库存
			// processInOut(bill, details, list);

			// 处理即时分仓库存
			// stockStoreService.handleStock(bill);

			// 处理总库存、分仓库存、处理即时分仓库存
			auditService.passAduit(bill, details);

			// 更新仓库单据关联
			relationService.updateStatus(id, BillRelation.AUTH);

			// 更新审核人、审核时间
			bill.setAuditor(SecurityUtil.getCurrentUser());
			bill.setAuditTime(Calendar.getInstance().getTime());
			bill.setRecordStatus(WarehouseBill.STATUS_AUDITED);
			billRepo.save(bill);
			/**资金计划审核**/
			CapitalPlan capitalPlan = capitalPlanService.queryByRelation(id);
			if(capitalPlan!=null){
				RequestResult storagePassAudit = capitalPlanService.passAudit(capitalPlan.getId(), CapitalPlan.STATUS_EXECUTING,DateUtils.getStringByFormat(capitalPlan.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
				if(storagePassAudit.getReturnCode()==1){
					return storagePassAudit;
				}
			}

			//插入预计收益率记录
			if (bill.getBillType() == WarehouseBuilderCodeHelper.xsch) {
				saleOutRateService.computeRate(bill, details);
			}
			//销售出库单审核时，判断货品提成表是否存在该提成点数记录
			if(bill.getBillType()==WarehouseBuilderCodeHelper.xsch){
				for (WarehouseBillDetail billDetail : details) {
					String goodId = billDetail.getGoods().getFid();
					BigDecimal percentage = billDetail.getPercentage();
					String orgId = billDetail.getOrg().getFid();
					String accId = billDetail.getFiscalAccount().getFid();
					if(percentage.compareTo(BigDecimal.ZERO)>0&&percentage.compareTo(new BigDecimal(100))<=0){
						GoodsPercentage goodsPercentage = percentageService.findTopBy(goodId, percentage, orgId, accId);
						if(goodsPercentage==null){
							GoodsPercentageVo percentageVo = new GoodsPercentageVo();
							percentageVo.setGoodsId(goodId);
							percentageVo.setIsLast(1);
							percentageVo.setPercentage(percentage);
							percentageService.save(percentageVo);
						}else{
							goodsPercentage.setIsLast(1);
							goodsPercentage.setUpdateTime(new Date());
							percentageService.save(goodsPercentage);
						}
					}
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("审核仓库单据出错!");
		}
		return buildSuccessRequestResult();
	}

	/**
	 * 判断收货单的箱号和封号是否有重复（如果一张发货单对应多张收货单，要合并判断），如果重复，提示用户，不能审核；
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public RequestResult checkSame(String id) {
		// 1、找到发货单
		List<BillRelation> relations = relationService.getRelation(id);

		// 2、找到所有的收货单
		List<String> fhdIds = relations.stream().map(BillRelation::getRefBillId).collect(Collectors.toList());
		List<BillRelation> shdRelations = relationService.findByRefBillIdIn(fhdIds);
		List<String> shdIds = shdRelations.stream().map(BillRelation::getBillId).collect(Collectors.toList());

		// 排除本收货单
		shdIds = shdIds.stream().filter(p -> !p.equals(id)).collect(Collectors.toList());

		// 判断收货单的箱号和封号是否有重复（如果一张发货单对应多张收货单，要合并判断），
		// 如果重复，提示用户，不能审核；
		List<TransportBilldetail> tdetails = transportBilldetailService.getTransportBillDetails(id);
		Set<String> containerNumbers = Sets.newHashSet();
		Set<String> sealingNumbers1 = Sets.newHashSet();
		Set<String> sealingNumbers2 = Sets.newHashSet();

		for (TransportBilldetail detail : tdetails) {
			if (containerNumbers.contains(detail.getContainerNumber())) {
				return buildFailRequestResult("箱号重复:" + detail.getContainerNumber());
			}
			if (sealingNumbers1.contains(detail.getSealingNumber1())) {
				return buildFailRequestResult("封号1重复:" + detail.getSealingNumber1());
			}
			if (sealingNumbers2.contains(detail.getSealingNumber2())) {
				return buildFailRequestResult("封号2重复:" + detail.getSealingNumber2());
			}
			if (sealingNumbers1.contains(detail.getSealingNumber2())) {
				return buildFailRequestResult("封号1与封号2有重复:" + detail.getSealingNumber2());
			}
			if (sealingNumbers2.contains(detail.getSealingNumber1())) {
				return buildFailRequestResult("封号1与封号2有重复:" + detail.getSealingNumber1());
			}
			if (detail.getSealingNumber1().equals(detail.getSealingNumber2())) {
				return buildFailRequestResult("封号1与封号2重复:" + detail.getSealingNumber2());
			}
			containerNumbers.add(detail.getContainerNumber());
			sealingNumbers1.add(detail.getSealingNumber1());
			sealingNumbers2.add(detail.getSealingNumber2());
		}

		for (String shdId : shdIds) {
			if (shdId.equals(id))
				continue;
			List<TransportBilldetail> shddetails = transportBilldetailService.getTransportBillDetails(shdId);

			WarehouseBill tbill = billRepo.findOne(shdId);
			String billCode = tbill.getCode();
			for (TransportBilldetail detail : shddetails) {
				if (containerNumbers.contains(detail.getContainerNumber())) {
					return buildFailRequestResult("单据单号:" + billCode + " 箱号重复:" + detail.getContainerNumber());
				}
				if (sealingNumbers1.contains(detail.getSealingNumber1())) {
					return buildFailRequestResult("单据单号:" + billCode + " 封号1重复:" + detail.getSealingNumber1());
				}
				if (sealingNumbers2.contains(detail.getSealingNumber2())) {
					return buildFailRequestResult("单据单号:" + billCode + " 封号2重复:" + detail.getSealingNumber2());
				}
				if (sealingNumbers1.contains(detail.getSealingNumber2())) {
					return buildFailRequestResult("单据单号:" + billCode + " 封号1与封号2有重复:" + detail.getSealingNumber2());
				}
				if (sealingNumbers2.contains(detail.getSealingNumber1())) {
					return buildFailRequestResult("单据单号:" + billCode + " 封号1与封号2有重复:" + detail.getSealingNumber1());
				}
				if (detail.getSealingNumber1().equals(detail.getSealingNumber2())) {
					return buildFailRequestResult("单据单号:" + billCode + " 封号1与封号2重复:" + detail.getSealingNumber2());
				}
				containerNumbers.add(detail.getContainerNumber());
				sealingNumbers1.add(detail.getSealingNumber1());
				sealingNumbers2.add(detail.getSealingNumber2());
			}
		}
		return buildSuccessRequestResult();
	}

	/**
	 * 作废单据
	 */
	@Override
	@Transactional
	public RequestResult cancel(String id) {
		RequestResult result = new RequestResult();
		try {
			TaskBillVo taskBillVo = taskBillService.queryByRelation(id);
			if(taskBillVo!=null){
				String planName = taskBillVo.getPlanName();
				String taskName = taskBillVo.getTaskName();
				return buildFailRequestResult("该单据已关联"+planName+"计划中的"+taskName+"事件!");
			}
			WarehouseBill bill = billRepo.findOne(id);
			if (bill == null) {
				return new RequestResult(RequestResult.RETURN_FAILURE, "该记录不存在或已被删除!");
			}
			List<WarehouseBillDetail> details = billDetailService.getBillDetails(id);
			// 会计期间
			RequestResult periodCheck = canOperateByPeriod(bill.getBillDate());
			if (periodCheck.getReturnCode() == RequestResult.RETURN_FAILURE) {
				return periodCheck;
			}

			if (bill.getRecordStatus() == WarehouseBill.STATUS_CANCELED) {
				result.setMessage("无效操作，单据已被取消!");
				result.setReturnCode(RequestResult.RETURN_FAILURE);
				return result;
			}
			if (relationService.isAssociated(id)) {
				result.setMessage("无效操作，该单据已被关联!");
				result.setReturnCode(RequestResult.RETURN_FAILURE);
				return result;
			}
			if (paycheckService.countByBillId(id) > 0) {
				result.setMessage("无效操作，该单据存在收付款记录!");
				result.setReturnCode(RequestResult.RETURN_FAILURE);
				return result;
			}
			if (!auditService.checkReverseIn(bill, details)) {
				result.setMessage("无效操作，单据存在出库记录!");
				result.setReturnCode(RequestResult.RETURN_FAILURE);
				return result;
			}

			// 如果单据为审核状态时，要删除相应的出入库记录
			if (bill.getRecordStatus() == WarehouseBill.STATUS_AUDITED) {
				/*
				 * reverseInOut(bill, details, null);
				 * stockStoreService.reverseHandleStock(bill);
				 * auditService.cancel(bill, details);
				 */

				// 处理总库存、分仓库存、处理即时分仓库存
				if(!auditService.cancleBill(bill, details)){
					result.setMessage("无效操作，库存不足");
					result.setReturnCode(RequestResult.RETURN_FAILURE);
					return result;
				}
			}

			// 取消单据关联
			relationService.cancelAssociate(id);

			bill.setCancelTime(new Date());
			bill.setCancelor(SecurityUtil.getCurrentUser());
			bill.setRecordStatus(WarehouseBill.STATUS_CANCELED);
			save(bill);
			/** 资金计划取消**/
			CapitalPlan capitalPlan = capitalPlanService.queryByRelation(id);
			if(capitalPlan!=null){
				RequestResult storagePassAudit = capitalPlanService.cancel(capitalPlan.getId(), CapitalPlan.STATUS_CANCEL,DateUtils.getStringByFormat(capitalPlan.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
				if(storagePassAudit.getReturnCode()==1){
					return storagePassAudit;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("作废单据");
		}
		return result;
	}

	/**
	 * 多单合并，合并销售订单，合并采购订单
	 */
	public List<WarehouseBillDetailVo> mergeBillDetails(String billIds, int mergeSame, int onlyMaterial) {
		Splitter splitter = Splitter.on(",").trimResults().omitEmptyStrings();
		List<String> idList = splitter.splitToList(billIds);
		Map<String, WarehouseBillDetail> cache = Maps.newLinkedHashMap();
		for (String billId : idList) {
			List<WarehouseBillDetail> detailList = billDetailService.getBillDetails(billId);
			for (WarehouseBillDetail detail : detailList) {
				if (onlyMaterial == 1) {
					if (detail.getDetailType() == null
							|| detail.getDetailType() != WarehouseBillDetail.DETAIL_TYPE_METERIAL)
						continue;
				} else if (onlyMaterial == 2) {
					if (detail.getDetailType() != null
							&& detail.getDetailType() == WarehouseBillDetail.DETAIL_TYPE_METERIAL)
						continue;
				}
				String goodsId = detail.getGoods().getFid();
				String specId = detail.getGoodsSpec() == null ? "" : detail.getGoodsSpec().getFid();
				String key = "";
				if (mergeSame == 1) {
					key = goodsId + "#" + specId;
				} else {
					key = detail.getFid();
				}
				if (cache.containsKey(key)) {
					WarehouseBillDetail detailCache = cache.get(key);

					BigDecimal accountQuentity = NumberUtil.add(detailCache.getAccountQuentity(),
							detail.getAccountQuentity());
					BigDecimal amount = NumberUtil.add(detailCache.getType(), detail.getType());
					detailCache.setAccountQuentity(accountQuentity);
					detailCache.setQuentity(accountQuentity);
					detailCache.setUnit(detailCache.getAccountUint());
					detailCache.setUnitPrice(detailCache.getAccountUintPrice());
					detailCache.setScale(detailCache.getAccountUint().getScale());
					detailCache.setRefDetailId(detail.getFid());
					detailCache.setType(amount);
					detailCache.setTransportUint(detail.getTransportUint());
				} else {
					WarehouseBillDetail detailCache = VoFactory.createValue(WarehouseBillDetail.class, detail);
					detailCache.setFid(null);
					detailCache.setRefDetailId(detail.getFid());
					detailCache.setAccountUint(detail.getAccountUint());
					detailCache.setType(detail.getType());
					detailCache.setBill(detail.getBill());
					detailCache.setGoods(detail.getGoods());
					detailCache.setGoodsSpec(detail.getGoodsSpec());
					detailCache.setInWareHouse(detail.getInWareHouse());
					detailCache.setOutWareHouse(detail.getOutWareHouse());
					detailCache.setUnit(detail.getUnit());
					cache.put(key, detailCache);
				}
			}
		}
		return billDetailService.getVos(Lists.newArrayList(cache.values()));
	}

	/**
	 * 把导入的数据初始化
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<ImportVoBean> initialDetailVos(List<ImportVoBean> voBeans) {

		String orgId = SecurityUtil.getCurrentOrgId();

		for (ImportVoBean voBean : voBeans) {
			if (!voBean.getVaild())
				continue;
			WarehouseBillDetailVo vo = (WarehouseBillDetailVo) voBean.getVo();

			String goodsCode = vo.getGoodsCode();
			String goodsSpecCode = vo.getGoodsSpecCode();
			String unitCode = vo.getUnitCode();
			String inWareHouseCode = vo.getInWareHouseCode();

			if (!NumberUtils.isNumber(vo.getQuentity())) {
				continue;
			}
			if (!NumberUtils.isNumber(vo.getUnitPrice())) {
				continue;
			}

			Goods goods = null;

			// 货品
			if (StringUtils.isNotBlank(goodsCode)) {
				goods = goodsService.getByCode(orgId, goodsCode);
				if (goods == null) {
					voBean.setVaild(false);
					voBean.setMsg("货品无效");
					continue;
				}
				if (goods.getRecordStatus() == Goods.STATUS_SNU) {
					voBean.setVaild(false);
					voBean.setMsg("货品无效");
					continue;
				}
				vo.setGoodsId(goods.getFid());
				vo.setGoodsName(goods.getName());
				vo.setGoodsSpec(goods.getSpec());
				// 规格
				/*cwz 2017-5-2  导入时判断货品属性组下的属性是否有编号*/
				if (StringUtils.isNotBlank(goodsSpecCode)) {
					GoodsSpec goodsSpec2 = goods.getGoodsSpec();
					if(goodsSpec2==null)continue;
//					GoodsSpec goodsSpec = goodsSpecService.getByCode(orgId, goodsSpecCode);
					GoodsSpec goodsSpec = goodsSpecService.findTopByCodeAndSpecGroupId(orgId, goodsSpecCode,goodsSpec2.getFid());
					if (goodsSpec == null) {
						voBean.setVaild(false);
						voBean.setMsg("货品属性无效");
						continue;
					}

					vo.setGoodsSpecId(goodsSpec.getFid());
					vo.setGoodsSpecName(goodsSpec.getName());

					GoodsSpec group = goodsSpec.getParent();
					if (group == null) {
						voBean.setVaild(false);
						voBean.setMsg("货品属性找不到上级");
						continue;
					}
					vo.setGoodsSpecGroupId(group.getFid());
					vo.setGoodsSpecGroupName(group.getName());

					GoodsSpec goodsGroup = goods.getGoodsSpec();
					if (goodsGroup == null) {
						voBean.setVaild(false);
						voBean.setMsg("货品属性找不到上级");
						continue;
					}
					if (!group.getFid().equals(goodsGroup.getFid())) {
						voBean.setVaild(false);
						voBean.setMsg("货品属性组与填写的属性组不对应");
						continue;
					}
				} else {
					if (goods.getGoodsSpec() != null) {
						voBean.setVaild(false);
						voBean.setMsg("货品有属性但你没有填写货品属性编码");
						continue;
					}
				}
				
				// 单位
				if (StringUtils.isNotBlank(unitCode)) {
					Unit unit2 = goods.getUnit();
					if(unit2==null) continue;
//					Unit unit = unitService.getByCode(orgId, unitCode);
				
					Unit unit = unitService.findTopByLeafCode(orgId, unitCode);
//					Unit unit = unitService.findTopByCodeAndParent(orgId, unitCode, unitGroup.getFid());
					if (unit == null) {
						Unit group = goods.getUnitGroup();
						vo.setUnitGroupId(group.getFid());
						vo.setUnitGroupName(group.getName());
					} else {
						if (!goods.getUnitGroup().getFid().equals(unit.getParent().getFid())) {
							voBean.setVaild(false);
							voBean.setMsg("单位组不匹配");
							continue;
						}

						vo.setUnitId(unit.getFid());
						vo.setUnitName(unit.getName());
						vo.setScale(unit.getScale().toPlainString());

						Unit group = unit.getParent();
						vo.setUnitGroupId(group.getFid());
						vo.setUnitGroupName(group.getName());
					}
				}
				
			}



			if (StringUtils.isNotBlank(inWareHouseCode)) {
				AuxiliaryAttr warehouse = attrService.getByCode(orgId, AuxiliaryAttrType.CODE_WAREHOUSE,
						inWareHouseCode);
				if (warehouse != null) {
					vo.setInWareHouseId(warehouse.getFid());
					vo.setInWareHouseName(warehouse.getName());
				}
			}

		}

		return voBeans;
	}

	/**
	 * 插入单据关联
	 * 
	 * @param billId
	 * @param relationIds
	 * @param masterType
	 * @param refType(这个暂时不用)
	 */
	public List<WarehouseBill> insertBillRelation(String billId, String relationIds, int masterType, int refType) {
		relationService.deleteRelation(billId);

		Splitter splitter = Splitter.on(",").trimResults().omitEmptyStrings();
		List<String> relationList = splitter.splitToList(relationIds);
		List<WarehouseBill> refBills = Lists.newArrayList();
		String orgId = SecurityUtil.getCurrentOrgId();

		for (String relationId : relationList) {
			WarehouseBill t_bill = billRepo.findOne(relationId);
			int t_bill_type = t_bill.getBillType();
			BillRelation relation = new BillRelation();
			relation.setBillId(billId);
			relation.setBillType(masterType);
			relation.setIsDetail(BillRelation.BILL);
			relation.setOrgId(orgId);
			relation.setRefBillId(relationId);
			relation.setRefBillType(t_bill_type);
			relation.setRefIsDetail(BillRelation.BILL);
			relation.setRecordStatus(BillRelation.UN_AUTH);
			relationService.save(relation);

			refBills.add(t_bill);
		}
		return refBills;
	}

	/**
	 * 寻找关联单据的关联明细，如果有多条，取第一条
	 * 
	 * @param refBills
	 * @param compare
	 * @return
	 */
	public WarehouseBillDetail catchRefDetail(List<WarehouseBill> refBills, WarehouseBillDetailVo compare) {
		for (WarehouseBill bill : refBills) {
			List<WarehouseBillDetail> refDetails = bill.getDetails();
			for (WarehouseBillDetail detail : refDetails) {
				if (isSameGoods(detail, compare))
					return detail;
			}
		}
		return null;
	}

	/**
	 * 根据货品ID、货品属性ID、单位ID，判断是否相同
	 * 
	 * @param detail1
	 *            仓库单据明细
	 * @param detail2
	 *            单据收益明细
	 * @return
	 */
	public boolean isSameGoods(WarehouseBillDetail detail1, WarehouseBillDetailVo detail2) {

		if (!detail1.getGoods().getFid().equals(detail2.getGoodsId())) {
			return false;
		}
		if (!detail1.getUnit().getFid().equals(detail2.getUnitId())) {
			return false;
		}
		if (detail1.getGoodsSpec() != null && detail2.getGoodsSpecId() != null
				&& !detail1.getGoodsSpec().getFid().equals(detail2.getGoodsSpecId())) {
			return false;
		}
		if (detail1.getGoodsSpec() == null && detail2.getGoodsSpecId() != null) {
			return false;
		}
		if (detail1.getGoodsSpec() != null && detail2.getGoodsSpecId() == null) {
			return false;
		}
		return true;
	}

	/**
	 * 处理总仓、分仓数据
	 */
	public void processStockAmount(WarehouseBill bill, List<WarehouseBillDetail> details, List<OutStorage> outList,
			int inout) {

		String accId = SecurityUtil.getFiscalAccountId();
		// 期间总存金额表
		periodAmountService.refreshPeriodAmount(bill, details, outList, inout, accId);
		// 期间分仓库存表
		periodStockAmountService.refreshPeriodStockAmount(bill, details, outList, inout, accId);
	}

	/**
	 * 处理总仓、分仓数据(反向处理)
	 */
	public void reverseStockAmount(WarehouseBill bill, List<WarehouseBillDetail> details, List<OutStorage> outList,
			int inout) {
		String accId = SecurityUtil.getFiscalAccountId();
		// 期间总存金额表
		periodAmountService.reversePeriodAmount(bill, details, outList, inout, accId);
		// 期间分仓库存表
		periodStockAmountService.reversePeriodStockAmount(bill, details, outList, inout, accId);
	}

	/**
	 * 统计某个单据类型下同一供应商的单据数量
	 * 
	 * @param orgId
	 *            机构ID
	 * @param accountId
	 *            财务账套ID
	 * @param billType
	 * @param supplierId
	 * @return add by xjh
	 */
	public long countByBillTypeSupplier(String orgId, String accountId, int billType, String supplierId,
			String selfId) {
		return billRepo.countByBillTypeSupplier(orgId, accountId, billType, supplierId, selfId);
	}

	/**
	 * 统计某个单据类型下同一客户的单据数量
	 * 
	 * @param orgId
	 *            机构ID
	 * @param accountId
	 *            财务账套ID
	 * @param billType
	 * @param customerId
	 * @return add by xjh
	 */
	@SuppressWarnings("rawtypes")
	public long countByBillTypeCustomer(String orgId, String accountId, int billType, String customerId,
			String selfId) {
		return billRepo.countByBillTypeCustomer(orgId, accountId, billType, customerId, selfId);
	}

	/**
	 * 根据时间段获取销售出货-销售退货总金额
	 * 
	 * @param startDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @param accountId
	 *            账套ID
	 * @param customerId
	 *            客户ID
	 * @return
	 */
	public BigDecimal getCustomerSummary(Date startDate, Date endDate, String accountId, String customerId) {
		return billRepo.getCustomerSummary(startDate, endDate, accountId, customerId);
	}

	/**
	 * 根据时间段获取采购入库-采购退货总金额
	 * 
	 * @param startDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @param accountId
	 *            账套ID
	 * @param supplierId
	 *            供应商ID
	 * @return
	 */
	public BigDecimal getSupplierSummary(Date startDate, Date endDate, String accountId, String supplierId) {
		return billRepo.getSupplierSummary(startDate, endDate, accountId, supplierId);
	}

	/**
	 * 根据时间获取单据
	 * 
	 * @param startDate
	 * @param endDate
	 * @param orgId
	 * @param accountId
	 * @return
	 */
	public List<WarehouseBill> getWarehouseBills(Date startDate, Date endDate, String orgId, String accountId,
			Integer billType1, Integer billType2) {

		List<Integer> billTypes = Lists.newArrayList(billType1, billType2);
		return billRepo.getWarehouseBills(startDate, endDate, accountId, billTypes);
	}

	/**
	 * 分页查询
	 * 
	 * @param paybill
	 * @param accId
	 * @param checkStartDay
	 * @param checkEndDay
	 * @param checkBillCode
	 * @param checkBillType
	 * @param excludeIds
	 * @param page
	 * @return
	 */
	public Page<WarehouseBill> queryUnCheckBills(PaymentBill paybill, String accId, String checkStartDay,
			String checkEndDay, String checkBillCode, int checkBillType, List<String> excludeIds, Pageable page) {
		return billRepo.queryUnCheckBills(paybill, accId, checkStartDay, checkEndDay, checkBillCode, checkBillType,
				excludeIds, page);
	}

	/**
	 * 保存收付款单和勾对表记录
	 * 
	 * @param vo
	 *            表单传输对象 - 收付款单
	 * @param ckbillType
	 *            勾对单据类型：11.采购入库;12.采购退货;15.采购发票;41.销售出货;42.销售退货;44.销售发票.
	 * @param billId
	 *            单据id
	 * @param caleType
	 *            计算类型(1.整数运算;2.负数运算)
	 * @return
	 */
	@Transactional
	public RequestResult savePaymentReceived(PaymentBillVo vo, Integer ckbillType, String billId, Integer caleType) {
		RequestResult save = null;
		try {
			// 设置单据日期为当前时间
			vo.setBillDate(DateUtils.getCurrentDate());
			// 累计收付款金额
			WarehouseBill warehouseBill = get(billId);
			// //累计收付款金额
			BigDecimal totalPayAmount = warehouseBill.getTotalPayAmount();
			// 合计金额
			BigDecimal befAmount = warehouseBill.getTotalAmount();
			// 免单金额
			BigDecimal freeAmount = warehouseBill.getFreeAmount();
			// 输入金额
			BigDecimal amount = Strings.isNullOrEmpty(vo.getAmount()) ? BigDecimal.ZERO
					: new BigDecimal(vo.getAmount());
			// 收/付款金额不能为0
			if (amount.compareTo(BigDecimal.ZERO) == 0) {
				return new RequestResult(RequestResult.RETURN_FAILURE, "金额不能为0");
			}
			if (caleType != null && caleType == 2) {
				if (amount.compareTo(BigDecimal.ZERO) >= 0)
					return new RequestResult(RequestResult.RETURN_FAILURE, "金额必须是负数");
				totalPayAmount = totalPayAmount.add(amount.multiply(new BigDecimal(-1)));
				// 费用单累计收付款金额不能大于费用单金额。
				if (totalPayAmount.compareTo(befAmount.subtract(freeAmount)) > 0) {
					return new RequestResult(RequestResult.RETURN_FAILURE, "收/付款金额不能小于实际金额[合计金额-优惠金额]");
				}
			}
			if (caleType != null && caleType == 1) {
				if (amount.compareTo(BigDecimal.ZERO) <= 0)
					return new RequestResult(RequestResult.RETURN_FAILURE, "金额必须是正数");
				totalPayAmount = totalPayAmount.add(amount);
				// 费用单累计收付款金额不能大于费用单金额。
				if (totalPayAmount.compareTo(befAmount.subtract(freeAmount)) > 0) {
					return new RequestResult(RequestResult.RETURN_FAILURE, "收/付款金额不能大于实际金额[合计金额-优惠金额]");
				}
			}

			Integer recordStatus = warehouseBill.getRecordStatus();
			if (recordStatus == 0)
				return new RequestResult(RequestResult.RETURN_FAILURE, "请审核该费用单！");
			if (recordStatus == 2)
				return new RequestResult(RequestResult.RETURN_FAILURE, "该费用单已作废！");
			if (vo != null && vo.getBillType() != null) {
				//收款单只填写客户信息
				if (vo.getBillType() == 51) {
					if (warehouseBill.getCustomer() != null)
						vo.setCustomerId(warehouseBill.getCustomer().getFid());
				} else {
					// 单据不为空，设置供应商或者客户
					if (warehouseBill.getSupplier() != null)
						vo.setSupplierId(warehouseBill.getSupplier().getFid());
					if (warehouseBill.getCustomer() != null)
						vo.setCustomerId(warehouseBill.getCustomer().getFid());
				}

				// 收付款单状态设置为已审核
				// 新增/编辑收付款单
				save = paymentBillService.save(vo);

				if (save.getReturnCode() == 1)
					return new RequestResult(RequestResult.RETURN_FAILURE, save.getMessage());
				@SuppressWarnings("unchecked")
				// 获取收付款单新增编号
				Map<String, Object> map = save.getDataExt();
				String fid = map.get("fid") == null ? "" : (String) map.get("fid");
				// 审核收付款单
				RequestResult passAudit = paymentBillService.passAudit(fid);

				if (passAudit.getReturnCode() == 1)
					return new RequestResult(RequestResult.RETURN_FAILURE, passAudit.getMessage());

				if (Strings.isNullOrEmpty(fid)) {
					if (vo.getBillType() == 51) {
						return new RequestResult(RequestResult.RETURN_FAILURE, "收款单ID不能为空");
					} else if (vo.getBillType() == 52) {
						return new RequestResult(RequestResult.RETURN_FAILURE, "付款单ID不能为空");
					}
				}
				// 勾对表中自动添加一条记录
				PaymentCheckVo pmcVo = new PaymentCheckVo();
				pmcVo.setPaymentBillId(fid);// 收付款单ID
				pmcVo.setBillType(ckbillType);// 单据类型
				pmcVo.setBillId(billId); // 单据id
				pmcVo.setFreeAmount(warehouseBill.getFreeAmount());// 免单金额
				pmcVo.setAmount(amount.abs());// 勾对金额不能为负数
				/*资金计划自动对单*/
				BigDecimal totalAmount = warehouseBill.getTotalAmount();
				pmcVo.setBillTotalAmount(totalAmount);
				pmcVo.setBillTotalPayAmount(totalPayAmount);//收付金额
//				RequestResult changePaymentAmount = capitalPlanService.changePaymentAmount(pmcVo, 1);
//				if(changePaymentAmount.getReturnCode()==1){
//					return changePaymentAmount;
//				}
				save = paymentCheckService.save(pmcVo);
				if (save.getReturnCode() == 1)
					return save;
				// 更新累计收付款金额
				warehouseBill.setTotalPayAmount(totalPayAmount);
				save(warehouseBill);

				
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new RequestResult(RequestResult.RETURN_FAILURE, "保存收付款单和勾对表记录有误！");
		}
		return save;
	}

	/**
	 * 根据发货地查询记录
	 * 
	 * @param fid
	 * @return
	 */
	public Long queryByDeliveryPlaceCount(String fid) {
		return billRepo.queryByDeliveryPlaceCount(fid);
	};

	/**
	 * 根据收货地查询记录
	 * 
	 * @param fid
	 * @return
	 */
	public Long queryByReceiptPlaceCount(String fid) {
		return billRepo.queryByReceiptPlaceCount(fid);
	};

	/**
	 * 统计所有单据引用了某个供应商的数量
	 * 
	 * @param supplierId
	 *            供应商ID
	 * @return
	 */
	public long countBySupplier(String supplierId) {
		return billRepo.countBySupplier(supplierId);
	}

	/**
	 * 统计所有单据引用了某个客户的数量
	 * 
	 * @param customerId
	 *            客户ID
	 * @return
	 */
	public long countByCustomer(String customerId) {
		return billRepo.countByCustomer(customerId);
	}

	/**
	 * 判断箱号和封号是否存在引用的发货单中，如果不存在提示用户是否审核，但不限制审核（不限制原因怕发货单输入错误）；
	 * 判断货品数量是否大于发货单的货品数量（如果一张发货单对应多张收货单，要合并判断），如果大于提示用户是否审核，但不限制审核；
	 * 判断运输数量是否大于发货单的运输数量（如果一张发货单对应多张收货单，要合并判断），如果大于提示用户是否审核，但不限制审核；
	 * 
	 * @return
	 */
	public RequestResult checkShd(String id) {

		List<BillRelation> relations = relationService.getRelation(id);
		if (relations.size() == 0)
			return buildFailRequestResult("找不到关联的发货单");
		BillRelation one = relations.get(0);

		List<TransportBilldetail> shdDetails = transportBilldetailService.getTransportBillDetails(id);
		List<TransportBilldetail> fhdDetails = transportBilldetailService.getTransportBillDetails(one.getRefBillId());

		Set<String> containerNumbers = fhdDetails.stream().map(TransportBilldetail::getContainerNumber)
				.collect(Collectors.toSet());
		Set<String> sealingNumbers1 = fhdDetails.stream().map(TransportBilldetail::getSealingNumber1)
				.collect(Collectors.toSet());
		Set<String> sealingNumbers2 = fhdDetails.stream().map(TransportBilldetail::getSealingNumber2)
				.collect(Collectors.toSet());

		List<String> notExistContainer = Lists.newArrayList();
		List<String> notExistSeal = Lists.newArrayList();
		List<String> quentityOverflow = Lists.newArrayList();
		List<String> transportOverflow = Lists.newArrayList();

		// 判断箱号封号是否存在发货单明细表2中
		for (TransportBilldetail detail : shdDetails) {
			if (!containerNumbers.contains(detail.getContainerNumber())) {
				notExistContainer.add(detail.getContainerNumber());
			}

			if (!sealingNumbers1.contains(detail.getSealingNumber1())
					&& !sealingNumbers2.contains(detail.getSealingNumber1())) {
				notExistSeal.add(detail.getSealingNumber1());
			}

			if (!sealingNumbers1.contains(detail.getSealingNumber2())
					&& !sealingNumbers2.contains(detail.getSealingNumber2())) {
				notExistSeal.add(detail.getSealingNumber2());
			}
		}

		checkShdQuentity(id, relations, quentityOverflow, transportOverflow);

		Joiner joiner = Joiner.on(",").skipNulls();
		String tip = "";

		boolean flag = false;
		if (notExistContainer.size() > 0) {
			flag = true;
			tip = tip + "发货单不存在箱号:" + joiner.join(notExistContainer) + "<br/>";
		}

		if (notExistSeal.size() > 0) {
			flag = true;
			tip = tip + "发货单不存在封号:" + joiner.join(notExistSeal) + "<br/>";
		}

		if (quentityOverflow.size() > 0) {
			flag = true;
			tip = tip + "以下货品收货单数量大于发货单数量:" + joiner.join(quentityOverflow) + "<br/>";
		}

		if (transportOverflow.size() > 0) {
			flag = true;
			tip = tip + "以下货品收货单运输数量大于发货单的运输数量:" + joiner.join(transportOverflow) + "<br/>";
		}
		if (flag) {
			return buildFailRequestResult(tip);
		} else {
			return buildSuccessRequestResult();
		}
	}

	@Transactional(readOnly = true)
	private void checkShdQuentity(String billId, List<BillRelation> relations, List<String> quentityOverflow,
			List<String> transportOverflow) {

		WarehouseBill shd = billRepo.findOne(billId);

		// 找到所有的收货单
		List<String> fhdIds = relations.stream().map(BillRelation::getRefBillId).collect(Collectors.toList());
		List<BillRelation> shdRelations = relationService.findByRefBillIdIn(fhdIds);
		List<String> shdIds = shdRelations.stream().map(BillRelation::getBillId).collect(Collectors.toList());

		// 排除本收货单
		shdIds = shdIds.stream().filter(p -> !p.equals(billId)).collect(Collectors.toList());

		List<WarehouseBillDetail> shdDetails = shd.getDetails();
		List<WarehouseBillDetail> fhdDetails = billDetailService.getBillDetails(fhdIds);

		List<WarehouseBillDetail> relationDetails = Lists.newArrayList();
		if (shdIds.size() > 0) {
			relationDetails = billDetailService.getBillDetails(shdIds);
		}

		List<WarehouseBillDetailVo> shdDetailVos = billDetailService.getVos(shdDetails);
		List<WarehouseBillDetailVo> relationDetailVos = billDetailService.getVos(relationDetails);
		List<WarehouseBillDetailVo> fhdDetailVos = billDetailService.getVos(fhdDetails);

		Map<String, WarehouseBillDetailVo> current = Maps.newHashMap();
		Map<String, WarehouseBillDetailVo> fhd = Maps.newHashMap();

		// 填充数量到Map
		fillQuentityMap(current, shdDetailVos, true);
		fillQuentityMap(current, relationDetailVos, false);
		fillQuentityMap(fhd, fhdDetailVos, true);

		for (String key : current.keySet()) {

			WarehouseBillDetailVo fhdVo = fhd.get(key);
			WarehouseBillDetailVo shdVo = current.get(key);
			String name = shdVo.getGoodsName()
					+ (shdVo.getGoodsSpecName() == null ? "" : "#" + shdVo.getGoodsSpecName());
			if (fhdVo == null) {
				quentityOverflow.add(name);
			} else {
				/*
				 * cwz 2017-5-17 2242
				 * 收货单审核提示：收货数量大于发货数量，实际我填入的数据是小于的（lolo1用户，lolo051501-成本单价帐套）；
				 * 限制有提示信息，也不能审核成功，应该不限制审核提交的
				 *  start
				 */
				BigDecimal fhAccountQuentity = fhdVo.getAccountQuentity()==null?BigDecimal.ZERO:new BigDecimal(fhdVo.getAccountQuentity());
				BigDecimal shAccountQuentity = shdVo.getAccountQuentity()==null?BigDecimal.ZERO:new BigDecimal(shdVo.getAccountQuentity());
				if (fhAccountQuentity.compareTo(shAccountQuentity) < 0) {
					quentityOverflow.add(name);
				}
				/*
				 * cwz 2017-5-17 2242
				 * 收货单审核提示：收货数量大于发货数量，实际我填入的数据是小于的（lolo1用户，lolo051501-成本单价帐套）；
				 * 限制有提示信息，也不能审核成功，应该不限制审核提交的
				 *  end
				 */
				if (fhdVo.getTransportQuentity().compareTo(shdVo.getTransportQuentity()) < 0) {
					transportOverflow.add(name);
				}
			}
		}
	}

	/**
	 * 填充数量到Map
	 * 
	 * @param map
	 * @param details
	 * @param fill
	 *            map不存在时是否填充
	 * @return
	 */
	private void fillQuentityMap(Map<String, WarehouseBillDetailVo> map, List<WarehouseBillDetailVo> details,
			boolean fill) {
		for (WarehouseBillDetailVo detail : details) {
			String key1 = detail.getGoodsId();
			String key2 = Strings.isNullOrEmpty(detail.getGoodsSpecId()) ? "" : detail.getGoodsSpecId();
			String key = key1 + "#" + key2;
			WarehouseBillDetailVo val = map.get(key);

			if (!fill && val == null)
				continue;

			BigDecimal tval = new BigDecimal(detail.getAccountQuentity());
			BigDecimal transportVal = NumberUtil.multiply(detail.getTransportQuentity(), detail.getTransprotScale());
			if (val != null) {
				BigDecimal tval2 = new BigDecimal(val.getAccountQuentity());
				tval = NumberUtil.add(tval, tval2);
				transportVal = NumberUtil.add(transportVal, detail.getTransportQuentity());
			} else {
				val = detail;
			}

			val.setTransportQuentity(transportVal);
			val.setAccountQuentity(NumberUtil.bigDecimalToStr(tval));
			map.put(key, val);
		}
	}
	/**
	 * 是否勾对
	 * @param id
	 * @param check
	 */
	public RequestResult wareHouseBillCheck(String id,short check){
		WarehouseBill entity=billRepo.findOne(id);
		if(entity!=null){
			entity.setIsCheck(check);
			billRepo.save(entity);
			return buildSuccessRequestResult();
		}else{
			return buildFailRequestResult("单据不存在!");
		}
		
	}
}
