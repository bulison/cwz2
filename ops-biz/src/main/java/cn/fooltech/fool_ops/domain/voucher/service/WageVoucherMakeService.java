package cn.fooltech.fool_ops.domain.voucher.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.service.AuxiliaryAttrService;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalPeriod;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.voucher.entity.Voucher;
import cn.fooltech.fool_ops.domain.voucher.entity.VoucherBill;
import cn.fooltech.fool_ops.domain.voucher.entity.VoucherDetail;
import cn.fooltech.fool_ops.domain.voucher.entity.WageVoucher;
import cn.fooltech.fool_ops.domain.voucher.vo.VoucherMakeVo;
import cn.fooltech.fool_ops.domain.wage.entity.Wage;
import cn.fooltech.fool_ops.domain.wage.entity.WageDetail;
import cn.fooltech.fool_ops.domain.wage.entity.WageFormula;
import cn.fooltech.fool_ops.domain.wage.service.WageDetailService;
import cn.fooltech.fool_ops.domain.wage.service.WageService;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.utils.SecurityUtil;



/**
 * <p>工资单凭证制作服务类</p>
 * @author rqh
 * @version 1.0
 * @date 2015年12月8日
 */
@Service
public class WageVoucherMakeService extends VoucherMakeAbstractService{

	/**
	 * 工资单服务类
	 */
	@Autowired
	private WageService wageService;
	
	/**
	 * 工资明细服务类
	 */
	@Autowired
	private WageDetailService wageDetailService;
	
	/**
	 * 凭证、单据关联服务类
	 */
	@Autowired
	private VoucherBillService voucherBillService;
	
	/**
	 * 凭证明细服务类
	 */
	@Autowired
	private VoucherDetailService voucherDetailService;
	
	/**
	 * 凭证服务类
	 */
	@Autowired
	private VoucherService voucherService;
	
	
	/**
	 * 工资凭证服务类
	 */
	@Autowired
	private WageVoucherService wageVoucherService;
	
	/**
	 * 辅助属性服务类
	 */
	@Autowired
	private AuxiliaryAttrService auxiliaryAttrService;
	
	/**
	 * 制作收付款单的凭证
	 * @param vo
	 * @return
	 */
	@Transactional
	public RequestResult makeVoucher(VoucherMakeVo vo){
		//财务账套
		FiscalAccount fiscalAccount = SecurityUtil.getFiscalAccount();
		User user = SecurityUtil.getCurrentUser();
		Organization org = SecurityUtil.getCurrentOrg();
		String accId = fiscalAccount.getFid();
		
		//会计期间
		FiscalPeriod fiscalPeriod = null;
		RequestResult periodCheck = checkFiscalPeriod(accId, vo.getVoucherDate());
		if(!periodCheck.isSuccess()){
			return periodCheck;
		}
		else{
			fiscalPeriod = (FiscalPeriod) periodCheck.getData();
		}
		
		List<WageVoucher> wageVouchers = wageVoucherService.findByAccId(accId);
		if(CollectionUtils.isEmpty(wageVouchers)){
			return buildFailRequestResult("请添加工资凭证配置!");
		}
		
		//校验数据
		for(WageVoucher wageVoucher : wageVouchers){
			//科目
			RequestResult creditCheck = check(WarehouseBuilderCodeHelper.gzd, wageVoucher.getWageSubject());
			if(!creditCheck.isSuccess()){
				return creditCheck;
			}
		}
		
		
		//添加凭证
		AuxiliaryAttr voucherWord = auxiliaryAttrService.get(vo.getVoucherWordId());
		int number = voucherService.getMaxNumber(fiscalPeriod.getFid()) + 1; //凭证顺序号
		int voucherNumber = voucherService.getMaxVoucherNumber(accId, fiscalPeriod.getFid(), voucherWord.getFid()) + 1; //凭证号
		Voucher voucher = new Voucher();
		voucher.setNumber(number);
		voucher.setOrg(org);
		voucher.setCreator(user);
		voucher.setVoucherWord(voucherWord);
		voucher.setVoucherNumber(voucherNumber);
		voucher.setVoucherDate(vo.getVoucherDate());
		voucher.setAccessoryNumber(vo.getAccessoryNumber());
		voucher.setFiscalPeriod(fiscalPeriod);
		voucher.setFiscalAccount(fiscalAccount);
		voucher.setCreateTime(Calendar.getInstance().getTime());
		voucher.setUpdateTime(Calendar.getInstance().getTime());
		voucher.setVoucherWordNumber(voucherWord.getName() + "-" + voucherNumber);
		voucherService.save(voucher);
		
		boolean flag = true;
		List<String> wageIds = null;
		//添加凭证明细
		for(WageVoucher wageVoucher : wageVouchers){
			Organization dept = wageVoucher.getDept();
			String resume = vo.getVoucherResume(); //摘要
			WageFormula wageFormula = wageVoucher.getWageFormula(); //工资项目ID
			if(wageIds==null){
				wageIds = wageDetailService.getWageIds(accId, vo.getMonth(), wageFormula.getFid(), dept.getFid());
			}
			for(String wageId : wageIds){
				saveVoucherDetail(voucher, fiscalAccount, wageVoucher.getWageSubject(), resume, wageId, wageFormula.getFid(), wageVoucher.getDirection());
				flag = false;
			}
			
			//添加单据、凭证关联
			saveVoucherBill(fiscalAccount, voucher, wageIds);
		}

		if(flag){
			voucherService.delete(voucher);
			return buildFailRequestResult("找不到相关的工资单用于生成凭证!");
		}
		
		RequestResult request = new RequestResult();
		request.setData(voucher.getFid());
		return request;
	}
	
	/**
	 * 添加凭证明细信息
	 * @param voucher 凭证
	 * @param fiscalAccount 财务账套
	 * @param subject 科目
	 * @param resume 摘要
	 * @param wageId 工资单ID
	 * @param wageFormulaId 工资项目ID
	 * @param direction 余额方向(借方金额:1;贷方金额:-1)
	 * @return
	 */
	@Transactional
	private void saveVoucherDetail(Voucher voucher, FiscalAccount fiscalAccount, FiscalAccountingSubject subject, 
			String resume, String wageId, String wageFormulaId, int direction){
		
		if(subject.getMemberSign() == FiscalAccountingSubject.ACCOUNT){
			//核算人员
			List<String> detailIds = wageDetailService.getDetailIds(fiscalAccount.getFid(), wageId, wageFormulaId);
			for(String detailId : detailIds){
				WageDetail wageDetail = wageDetailService.get(detailId);
				VoucherDetail voucherDetail = getVoucherDetail(voucher, fiscalAccount, subject, resume, wageDetail.getValue(), direction);
				voucherDetail.setMember(wageDetail.getMember());
				if(subject.getDepartmentSign() == FiscalAccountingSubject.ACCOUNT){
					//核算部门
					voucherDetail.setDepartment(wageDetail.getWage().getDept());
				}
				voucherDetailService.save(voucherDetail);
			}
		}
		else if(subject.getDepartmentSign() == FiscalAccountingSubject.ACCOUNT){
			//核算部门
			Wage wage = wageService.get(wageId);
			Organization dept = wage.getDept();
			BigDecimal amount = wageDetailService.sumAmountByWageIdAndFormulaId(wageId, wageFormulaId);
			VoucherDetail voucherDetail = getVoucherDetail(voucher, fiscalAccount, subject, resume, amount, direction);
			voucherDetail.setDepartment(dept);
			voucherDetailService.save(voucherDetail);
		}
		else{
			//不核算
			BigDecimal amount = wageDetailService.sumAmountByWageIdAndFormulaId(wageId, wageFormulaId);
			VoucherDetail voucherDetail = getVoucherDetail(voucher, fiscalAccount, subject, resume, amount, direction);
			voucherDetailService.save(voucherDetail);
		}
	}
	
	/**
	 * 获取凭证明细
	 * @param voucher 凭证
	 * @param fiscalAccount 财务账套
	 * @param subject 科目
	 * @param voucherResume 摘要
	 * @param amount 金额
	 * @param direction 余额方向
	 * @return
	 */
	private VoucherDetail getVoucherDetail(Voucher voucher, FiscalAccount fiscalAccount, 
			FiscalAccountingSubject subject, String voucherResume, BigDecimal amount, int direction){
		VoucherDetail voucherDetail = new VoucherDetail();
		voucherDetail.setVoucher(voucher);
		voucherDetail.setResume(voucherResume);
		voucherDetail.setAccountingSubject(subject);
		voucherDetail.setFiscalAccount(fiscalAccount);
		voucherDetail.setOrg(SecurityUtil.getCurrentOrg());
		voucherDetail.setCreator(SecurityUtil.getCurrentUser());
		voucherDetail.setCreateTime(Calendar.getInstance().getTime());
		voucherDetail.setUpdateTime(Calendar.getInstance().getTime());
		//借方金额、贷方金额
		if(direction == FiscalAccountingSubject.DIRECTION_BORROW){
			voucherDetail.setDebitAmount(amount);
		}
		else{
			voucherDetail.setCreditAmount(amount);
		}
		return voucherDetail;
	}
	
	/**
	 * 添加单据、凭证关联
	 * @param fiscalAccount 财务账套
	 * @param voucher 凭证
	 * @param wageIds 工资单据ID
	 */
	@Transactional
	private void saveVoucherBill(FiscalAccount fiscalAccount, Voucher voucher, List<String> wageIds){
		if(CollectionUtils.isNotEmpty(wageIds)){
			for(String wageId : wageIds){
				VoucherBill voucherBill = new VoucherBill();
				voucherBill.setVoucher(voucher);
				voucherBill.setBillType(WarehouseBuilderCodeHelper.gzd);
				voucherBill.setBillId(wageId);
				voucherBill.setFiscalAccount(fiscalAccount);
				voucherBill.setOrg(SecurityUtil.getCurrentOrg());
				voucherBill.setCreator(SecurityUtil.getCurrentUser());
				voucherBill.setCreateTime(Calendar.getInstance().getTime());
				voucherBillService.save(voucherBill);
			}
		}
	}
	
	/**
	 * 校验规则
	 * @param billType 单据类型
	 * @param subject 科目
	 * @return
	 */
	@Override
	public RequestResult check(Integer billType, FiscalAccountingSubject subject, Object ... args){
		if(subject.getCustomerSign() == FiscalAccountingSubject.ACCOUNT){
			return buildFailRequestResult(subject.getName() + "科目设置了核算销售商!");
		}
		if(subject.getSupplierSign() == FiscalAccountingSubject.ACCOUNT){
			return buildFailRequestResult(subject.getName() + "科目设置了核算供应商!");
		}
		if(subject.getWarehouseSign() == FiscalAccountingSubject.ACCOUNT){
			return buildFailRequestResult(subject.getName() + "科目设置了核算仓库!");
		}
		if(subject.getProjectSign() == FiscalAccountingSubject.ACCOUNT){
			return buildFailRequestResult(subject.getName() + "科目设置了核算项目!");
		}
		if(subject.getGoodsSign() == FiscalAccountingSubject.ACCOUNT){
			return buildFailRequestResult(subject.getName() + "科目设置了核算货品!");
		}
		if(subject.getQuantitySign() == FiscalAccountingSubject.ACCOUNT){
			return buildFailRequestResult(subject.getName() + "科目设置了核算数量!");
		}
		return buildSuccessRequestResult();
	}

	
}
