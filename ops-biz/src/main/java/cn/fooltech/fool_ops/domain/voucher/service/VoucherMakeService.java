package cn.fooltech.fool_ops.domain.voucher.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.PageService;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.Customer;
import cn.fooltech.fool_ops.domain.basedata.entity.CustomerSupplierView;
import cn.fooltech.fool_ops.domain.basedata.entity.Goods;
import cn.fooltech.fool_ops.domain.basedata.entity.Member;
import cn.fooltech.fool_ops.domain.basedata.entity.Supplier;
import cn.fooltech.fool_ops.domain.basedata.entity.Unit;
import cn.fooltech.fool_ops.domain.cost.entity.CostBill;
import cn.fooltech.fool_ops.domain.cost.repository.CostBillRepository;
import cn.fooltech.fool_ops.domain.payment.entity.PaymentBill;
import cn.fooltech.fool_ops.domain.payment.repository.PaymentBillRepository;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.voucher.entity.Voucher;
import cn.fooltech.fool_ops.domain.voucher.vo.VoucherMakeVo;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBillDetail;
import cn.fooltech.fool_ops.domain.warehouse.repository.WarehouseBillRepository;
import cn.fooltech.fool_ops.domain.warehouse.service.WarehouseBillDetailService;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.domain.warehouse.vo.WarehouseBillDetailVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;

/**
 * <p>凭证制作网页服务类</p>
 * @author rqh
 * @version 1.0
 * @date 2015年11月30日
 */
@Service
public class VoucherMakeService implements PageService{
	
	@Autowired
	private PaymentBillRepository paymentRepo;
	
	/**
	 * 费用单服务类
	 */
	@Autowired
	private CostBillRepository costBillRepo;
	
	/**
	 * 仓库单据服务类
	 */
	@Autowired
	private WarehouseBillRepository warehouseBillRepo;
	
	/**
	 * 仓库单据记录明细服务类
	 */
	@Autowired
	private WarehouseBillDetailService billDetailService;
	
	/**
	 * 凭证、单据关联服务类
	 */
	@Autowired
	private VoucherBillService voucherBillService;
	
	/**
	 * 费用单凭证制作网页服务类
	 */
	@Autowired
	private CostVoucherMakeService costVoucherMakeWebService;
	
	/**
	 * 收付款单凭证制作服务类
	 */
	@Autowired
	private PaymentVoucherMakeService paymentVoucherMakeWebService;
	
	/**
	 * 工资单凭证制作服务类
	 */
	@Autowired
	private WageVoucherMakeService wageVoucherMakeService;
	
	/**
	 * 固定资产生成凭证服务类
	 */
	@Autowired
	private AssetVoucherMakeService assetVoucherMakeService;
	
	/**
	 * 仓库单据凭证制作服务类
	 */
	@Autowired
	private BillVoucherMakeService billVoucherMakeService;
	
	@Autowired
	private PrepaidExpensesVoucherMakeService expensesVoucherMakeService;
	
	/**
	 * 单据分页查询
	 * @param vo
	 * @param paramater
	 * @return
	 */
	public Page<VoucherMakeVo> queryBill(VoucherMakeVo vo, PageParamater paramater){
		Assert.notNull(vo.getBillType(), "单据类型不能为空!");
		
		switch(vo.getBillType()){
			case WarehouseBuilderCodeHelper.fyd:{
				//费用单
				return queryCostCommonBill(vo, paramater);
			}
			case WarehouseBuilderCodeHelper.skd:
			case WarehouseBuilderCodeHelper.fkd:
			case WarehouseBuilderCodeHelper.cgfld:
			case WarehouseBuilderCodeHelper.xsfld:
			{
				//收款单、付款单、采购返利单、销售返利单
				return queryPaymentCommonBill(vo, paramater);
			}
			default:{
				//仓库单据
				return queryWarehouseCommonBill(vo, paramater);
			}
		}
	}
	
	/**
	 * 收付款单分页查询
	 * @param vo
	 * @param paramater
	 * @return
	 */
	private Page<VoucherMakeVo> queryPaymentCommonBill(VoucherMakeVo vo, PageParamater paramater){
		String accId = SecurityUtil.getFiscalAccountId();
		//排序
		Sort sort = new Sort(Direction.DESC, "createTime");
		//分页
		PageRequest pageRequest = getPageRequest(paramater, sort);
		Page<PaymentBill> page = paymentRepo.queryCommonBills(accId, vo, pageRequest);
		return getPaymentBillVo(page, pageRequest);
	}
	
	/**
	 * 收付款单实体转vo
	 * @return
	 */
	public Page<VoucherMakeVo> getPaymentBillVo(Page<PaymentBill> page, PageRequest pageRequest){
		List<VoucherMakeVo> vos = new ArrayList<VoucherMakeVo>();
		for(PaymentBill entity : page.getContent()){
			VoucherMakeVo vo = new VoucherMakeVo();
			vo.setFid(entity.getFid());
			vo.setCode(entity.getCode());
			vo.setBillType(entity.getBillType());
			vo.setBillDate(entity.getBillDate());
			vo.setCreateTime(entity.getCreateTime());
			vo.setAmount(bigDecimalToStr(entity.getAmount()));
			//客户
			Customer customer = entity.getCustomer();
			if(customer != null){
				vo.setContactUnitId(customer.getFid());
				vo.setContactUnitName(customer.getName());
			}
			//供应商
			Supplier supplier = entity.getSupplier();
			if(supplier != null){
				vo.setContactUnitId(supplier.getFid());
				vo.setContactUnitName(supplier.getName());
			}
			//经手人
			Member member = entity.getMember();
			if(member != null){
				vo.setMemberName(member.getUsername());
			}
			//凭证
			loadVoucherMsg(entity.getFid(), entity.getBillType(), vo);
			vos.add(vo);
		}
		return new PageImpl<VoucherMakeVo>(vos, pageRequest, 0);
	}
	
	/**
	 * 费用单分页查询
	 * @param vo
	 * @param paramater
	 * @return
	 */
	private Page<VoucherMakeVo> queryCostCommonBill(VoucherMakeVo vo, PageParamater paramater){
		String accId = SecurityUtil.getFiscalAccountId();
		//排序
		Sort sort = new Sort(Direction.DESC, "createTime");
		//分页
		PageRequest pageRequest = getPageRequest(paramater, sort);
		Page<CostBill> page = costBillRepo.queryCommonBills(accId, vo, pageRequest);
		Page<VoucherMakeVo> pageVos = getCostBillVo(page, pageRequest);
		return pageVos;
	}
		
	/**
	 * 费用单实体转vo
	 * @return
	 */
	public Page<VoucherMakeVo> getCostBillVo(Page<CostBill> page, PageRequest pageRequest){
		List<VoucherMakeVo> vos = Lists.newArrayList();
		for(CostBill entity : page.getContent()){
			VoucherMakeVo vo = new VoucherMakeVo();
			vo.setFid(entity.getFid());
			vo.setCode(entity.getCode());
			vo.setCreateTime(entity.getCreateTime());
			vo.setBillType(WarehouseBuilderCodeHelper.fyd);
			vo.setBillDate(entity.getBillDate());
			//金额
			BigDecimal amount = entity.getIncomeAmount().add(entity.getFreeAmount());
			vo.setAmount(bigDecimalToStr(amount));
			//往来单位
			CustomerSupplierView csv = entity.getCsv();
			if(csv != null){
				vo.setContactUnitId(csv.getFid());
				vo.setContactUnitName(csv.getName());
			}
			//部门
			Organization dept = entity.getDept();
			if(dept != null){
				vo.setDeptId(dept.getFid());
				vo.setDeptName(dept.getOrgName());
			}
			//经手人
			Member member = entity.getMember();
			if(member != null){
				vo.setMemberName(member.getUsername());
			}
			//凭证
			loadVoucherMsg(entity.getFid(), WarehouseBuilderCodeHelper.fyd, vo);
			vos.add(vo);
		}
		return new PageImpl<VoucherMakeVo>(vos, pageRequest, page.getTotalElements());
	}
	
	/**
	 * 普通单据分页查询
	 * @param vo
	 * @param paramater
	 * @return
	 */
	public Page<VoucherMakeVo> queryWarehouseCommonBill(VoucherMakeVo vo, PageParamater paramater){
		String accId = SecurityUtil.getFiscalAccountId();
		//排序
		Sort sort = new Sort(Direction.DESC, "createTime");
		//分页
		PageRequest pageRequest = getPageRequest(paramater, sort);
		Page<WarehouseBill> page = warehouseBillRepo.queryCommonBills(accId, vo, pageRequest);
		Page<VoucherMakeVo> pageVos = getCommonBillVo(page, pageRequest);
		return pageVos;
	}
	
	/**
	 * 普通单据实体转vo
	 * @return
	 */
	public Page<VoucherMakeVo> getCommonBillVo(Page<WarehouseBill> data, Pageable pageable){
		List<VoucherMakeVo> vos = new ArrayList<VoucherMakeVo>();
		
		//登录账套
		for(WarehouseBill entity: data.getContent()){
			VoucherMakeVo vo = new VoucherMakeVo();
			vo.setFid(entity.getFid());
			vo.setCode(entity.getCode());
			vo.setBillType(entity.getBillType());
			vo.setBillDate(entity.getBillDate());
			vo.setCreateTime(entity.getCreateTime());
			vo.setAmount(bigDecimalToStr(entity.getTotalAmount()));
			vo.setSpecialAmount(bigDecimalToStr(entity.getFreeAmount()));
			//客户
			Customer customer = entity.getCustomer();
			if(customer != null){
				vo.setContactUnitId(customer.getFid());
				vo.setContactUnitName(customer.getName());
			}
			//供应商
			Supplier supplier = entity.getSupplier();
			if(supplier != null){
				vo.setContactUnitId(supplier.getFid());
				vo.setContactUnitName(supplier.getName());
			}
			//部门
			Organization dept = entity.getDept();
			if(dept != null){
				vo.setDeptId(dept.getFid());
				vo.setDeptName(dept.getOrgName());
			}
			//经手人
			Member member = entity.getInMember();
			if(member != null){
				vo.setMemberName(member.getUsername());
			}
			//仓库
			AuxiliaryAttr warehouseStock = entity.getInWareHouse();
			if(warehouseStock != null){
				vo.setWarehouseStockName(warehouseStock.getName());
			}
			//凭证
			try {
				loadVoucherMsg(entity.getFid(), entity.getBillType(), vo);
			} catch (Exception e) {
			}
			vos.add(vo);
		}
		return new PageImpl<VoucherMakeVo>(vos, pageable, data.getTotalElements());
	}
	
	/**
	 * 收付款单，盘点单类型
	 */
	private List<Integer> specialTypes = Lists.newArrayList(
			WarehouseBuilderCodeHelper.skd,
			WarehouseBuilderCodeHelper.fkd);
	
	/**
	 * 获取单据明细
	 * @param billId 单据ID
	 * @param billType 单据类型
	 * @return
	 */
	public List<WarehouseBillDetailVo> getBillDetail(String billId, int billType){
		List<WarehouseBillDetailVo> vos = new ArrayList<WarehouseBillDetailVo>();
		if(specialTypes.contains(billType)){
			return vos;
		}
		List<WarehouseBillDetail> details = billDetailService.getBillDetails(billId);
		if(CollectionUtils.isNotEmpty(details)){
			for(WarehouseBillDetail detail: details){
				WarehouseBillDetailVo vo = new WarehouseBillDetailVo();
				vo.setType(bigDecimalToStr(detail.getType())); //金额
				vo.setQuentity(bigDecimalToStr(detail.getQuentity())); //数量
				vo.setUnitPrice(bigDecimalToStr(detail.getUnitPrice())); //单价
				//仓库
				AuxiliaryAttr inWareHouse = detail.getInWareHouse();
				if(inWareHouse != null){
					vo.setInWareHouseId(inWareHouse.getFid());
					vo.setInWareHouseName(inWareHouse.getName());
				}
				//货品
				Goods goods = detail.getGoods();
				if(goods != null){
					vo.setGoodsCode(goods.getCode());
					vo.setGoodsName(goods.getName());
					vo.setBarCode(goods.getBarCode());
					vo.setWeight(bigDecimalToStr(goods.getWeight()));
				}
				//单位
				Unit unit = detail.getUnit();
				if(unit != null){
					vo.setUnitName(unit.getName());
				}
				vos.add(vo);
			}
		}
		return vos;
	}
	
	/**
	 * 加载凭证信息
	 * @param billId 单据ID
	 * @param billType 单据类型
	 * @param vo
	 */
	public void loadVoucherMsg(String billId, Integer billType, VoucherMakeVo vo){
		String accId = SecurityUtil.getFiscalAccountId();
		Voucher voucher = voucherBillService.getVoucher(billId, billType, accId);
		if(voucher != null){
			//凭证ID
			vo.setVoucherId(voucher.getFid());
			//凭证号
			vo.setVoucherDate(voucher.getVoucherDate());
			vo.setVoucherNumber(voucher.getVoucherNumber());
			vo.setVoucherWordNumber(voucher.getVoucherWordNumber());
			//凭证字
			AuxiliaryAttr voucherWord = voucher.getVoucherWord();
			if(voucherWord != null){
				vo.setVoucherWordId(voucherWord.getFid());
				vo.setVoucherWordName(voucherWord.getName());
			}
		}
	}
	
	/**
	 * 制作凭证
	 * @param vo
	 * @return
	 */
	@Transactional
	public RequestResult makeVoucher(VoucherMakeVo vo){
		switch(vo.getBillType()){
			case WarehouseBuilderCodeHelper.fyd:{
				//费用单
				return costVoucherMakeWebService.makeVoucher(vo);
			}
			case WarehouseBuilderCodeHelper.skd:
			case WarehouseBuilderCodeHelper.fkd:
			case WarehouseBuilderCodeHelper.xsfld:
			case WarehouseBuilderCodeHelper.cgfld:	
			{
				//收款单、付款单、销售返利单、采购返利单
				return paymentVoucherMakeWebService.makeVoucher(vo);
			}
			case WarehouseBuilderCodeHelper.gzd:{
				//工资单
				return wageVoucherMakeService.makeVoucher(vo);
			}
			case WarehouseBuilderCodeHelper.gdzc:{
				//固定资产
				return assetVoucherMakeService.makeVoucher(vo);
			}
			case WarehouseBuilderCodeHelper.dtfy:{
				//待摊费用
				return expensesVoucherMakeService.makeVoucher(vo);
			}
			default:{
				//仓库单据
				return billVoucherMakeService.makeVoucher(vo);
			}
		}
	}
	
	/**
	 * BigDecimal转字符串
	 * @param num
	 * @return
	 */
	public static String bigDecimalToStr(BigDecimal num){
		if(num == null || num.compareTo(BigDecimal.ZERO) == 0){
			return null;
		}
		return num.setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString();
	}


}
