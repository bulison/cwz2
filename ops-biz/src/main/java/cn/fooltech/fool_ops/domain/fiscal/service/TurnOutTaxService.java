package cn.fooltech.fool_ops.domain.fiscal.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import cn.fooltech.fool_ops.domain.voucher.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.service.AuxiliaryAttrService;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalPeriod;
import cn.fooltech.fool_ops.domain.fiscal.entity.TurnOutTax;
import cn.fooltech.fool_ops.domain.fiscal.repository.FiscalInitBalanceRepository;
import cn.fooltech.fool_ops.domain.fiscal.repository.TurnOutTaxRepository;
import cn.fooltech.fool_ops.domain.fiscal.vo.TurnOutTaxVo;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.voucher.entity.Voucher;
import cn.fooltech.fool_ops.domain.voucher.entity.VoucherDetail;
import cn.fooltech.fool_ops.domain.voucher.repository.VoucherDetailRepository;
import cn.fooltech.fool_ops.domain.voucher.repository.VoucherRepository;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.validator.ValidatorUtils;

/**
 * <p>转出未交增值税网页服务类</p>
 * @author xjh
 * @version 1.0
 * @date 2016-04-12 10:12:03
 */
@Service
public class TurnOutTaxService extends BaseService<TurnOutTax, TurnOutTaxVo, String> {
	
	@Autowired
	private TurnOutTaxRepository turnOutTaxRepo;
	
	/**
	 * 科目服务类
	 */
	@Autowired
	private FiscalAccountingSubjectService subjectService;
	
	/**
	 * 属性服务类
	 */
	@Autowired
	private AuxiliaryAttrService attrService;
	
	
	@Autowired
	private VoucherDetailRepository voucherDetailRepo;
	
	@Autowired
	private FiscalInitBalanceRepository initBalanceRepo;
	
	/**
	 * 会计期间服务类
	 */
	@Autowired
	private FiscalPeriodService periodService;
	
	@Autowired
	private VoucherRepository voucherRepo;

	@Autowired
	private VoucherService voucherService;
	
	/**
	 * 单个转出未交增值税实体转换为vo
	 * @param entity
	 * @return
	 */
	@Override
	public TurnOutTaxVo getVo(TurnOutTax entity){
		if(entity == null)
			return null;
		TurnOutTaxVo vo = new TurnOutTaxVo();
		vo.setUpdateTime(DateUtilTools.date2String(entity.getUpdateTime(), DATE_TIME));
		vo.setFid(entity.getFid());
		
		AuxiliaryAttr voucherWord = entity.getVoucherWord();
		if(voucherWord!=null){
			vo.setVoucherWordId(voucherWord.getFid());
			vo.setVoucherWordName(voucherWord.getName());
		}
		
		FiscalAccountingSubject outSubject = entity.getOutSubject();
		if(outSubject!=null){
			vo.setOutSubjectId(outSubject.getFid());
			vo.setOutSubjectName(outSubject.getName());
			vo.setOutSubjectCode(outSubject.getCode());
		}
		
		FiscalAccountingSubject inSubject = entity.getInSubject();
		if(inSubject!=null){
			vo.setInSubjectId(inSubject.getFid());
			vo.setInSubjectName(inSubject.getName());
			vo.setInSubjectCode(inSubject.getCode());
		}
		
		FiscalAccountingSubject taxSubject = entity.getTaxSubject();
		if(taxSubject!=null){
			vo.setTaxSubjectId(taxSubject.getFid());
			vo.setTaxSubjectName(taxSubject.getName());
			vo.setTaxSubjectCode(taxSubject.getCode());
		}
		
		FiscalAccountingSubject unpaidSubject = entity.getUnpaidSubject();
		if(unpaidSubject!=null){
			vo.setUnpaidSubjectId(unpaidSubject.getFid());
			vo.setUnpaidSubjectName(unpaidSubject.getName());
			vo.setUnpaidSubjectCode(unpaidSubject.getCode());
		}
		
		FiscalAccountingSubject paySubject = entity.getPaySubject();
		if(paySubject!=null){
			vo.setPaySubjectId(unpaidSubject.getFid());
			vo.setPaySubjectName(unpaidSubject.getName());
			vo.setPaySubjectCode(unpaidSubject.getCode());
		}
		
		return vo;
	}
	
	/**
	 * 获取转出未交增值税信息
	 * @return
	 */
	public TurnOutTaxVo queryByAccount() {
		String accId = SecurityUtil.getFiscalAccountId();
		return getVo(turnOutTaxRepo.findByAccountId(accId));
	}
	
	/**
	 * 修改或更新
	 * @param vo
	 * @return
	 */
	private TurnOutTax saveOrUpdateTurnOutTax(TurnOutTaxVo vo){
		
		String accId = SecurityUtil.getFiscalAccountId();
		
		TurnOutTax entity = turnOutTaxRepo.findByAccountId(accId);
		
		if(entity==null){
			entity = new TurnOutTax();
			
			entity.setDept(SecurityUtil.getCurrentDept());
			entity.setOrg(SecurityUtil.getCurrentOrg());
			entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
			entity.setUpdateTime(Calendar.getInstance().getTime());
		}
		
		FiscalAccountingSubject inSubject = subjectService.get(vo.getInSubjectId());
		FiscalAccountingSubject outSubject = subjectService.get(vo.getOutSubjectId());
		FiscalAccountingSubject taxSubject = subjectService.get(vo.getTaxSubjectId());
		FiscalAccountingSubject unpaidSubject = subjectService.get(vo.getUnpaidSubjectId());
		FiscalAccountingSubject paySubject = subjectService.get(vo.getPaySubjectId());
		
		entity.setInSubject(inSubject);
		entity.setOutSubject(outSubject);
		entity.setTaxSubject(taxSubject);
		entity.setUnpaidSubject(unpaidSubject);
		entity.setPaySubject(paySubject);
		entity.setVoucherWord(attrService.get(vo.getVoucherWordId()));
		
		turnOutTaxRepo.save(entity);
		
		return entity;
	}
	

	/**
	 * 结转转出未交增值税
	 * @param vo
	 */
	public RequestResult saveOutUnpaidTax(TurnOutTaxVo vo) {
		String inValid = ValidatorUtils.inValidMsg(vo);
		if(inValid!=null){
			return buildFailRequestResult(inValid);
		}
		
		TurnOutTax entity = saveOrUpdateTurnOutTax(vo);
		FiscalAccountingSubject inSubject = entity.getInSubject();
		FiscalAccountingSubject outSubject = entity.getOutSubject();
		
		Date voucherDate = DateUtilTools.string2Date(vo.getVoucherDate(), DateUtilTools.DATE_PATTERN_YYYY_MM_DD);
		AuxiliaryAttr voucherWord = entity.getVoucherWord();
		String accId = SecurityUtil.getFiscalAccountId();
		String resume = vo.getResume();
		
		FiscalPeriod period = periodService.getFirstNotCheck();
		if(period==null)return buildFailRequestResult("找不到未结账的会计期间");
		
		//销项税科目的余额
		BigDecimal outInitAmount = initBalanceRepo.getAmountBySubjectId(outSubject.getFid(), null, accId);
		BigDecimal outVoucherAmount = voucherDetailRepo.getAmountBySubjectId(outSubject.getFid(), period.getStartDate(),
				period.getEndDate(), accId, Lists.newArrayList(Voucher.STATUS_POSTED));
		
		outInitAmount = NumberUtil.multiply(outInitAmount, new BigDecimal(outSubject.getDirection()));
		
		//销项税科目的余额
		BigDecimal outAmount = NumberUtil.add(outInitAmount, outVoucherAmount);
		
		//销项税科目的贷方余额
		BigDecimal creditOutAmount = NumberUtil.multiply(outAmount, new BigDecimal(-1));
		
		BigDecimal inInitAmount = initBalanceRepo.getAmountBySubjectId(inSubject.getFid(), null, accId);
		BigDecimal inVoucherAmount = voucherDetailRepo.getAmountBySubjectId(inSubject.getFid(), period.getStartDate(),
				period.getEndDate(), accId, Lists.newArrayList(Voucher.STATUS_POSTED));
		
		inInitAmount = NumberUtil.multiply(inInitAmount, new BigDecimal(inSubject.getDirection()));
		
		//进项税科目的余额/借方余额
		BigDecimal inAmount = NumberUtil.add(inInitAmount, inVoucherAmount);
		
		if(inSubject.getFid().equals(outSubject.getFid())){
			return saveVoucherWithInOutSameSubject(period, voucherWord, entity, creditOutAmount, inAmount, voucherDate, resume);
		}else{
			return saveVoucherWithInOutNotSameSubject(period, voucherWord, entity, creditOutAmount, inAmount, voucherDate, resume);
		}
	}
	
	
	/**
	 * 结转未交增值税
	 * @param vo
	 */
	public RequestResult saveChangeUnpaidTax(TurnOutTaxVo vo) {
		
		String inValid = ValidatorUtils.inValidMsg(vo);
		if(inValid!=null){
			return buildFailRequestResult(inValid);
		}
		
		Date voucherDate = DateUtilTools.string2Date(vo.getVoucherDate(), DateUtilTools.DATE_PATTERN_YYYY_MM_DD);
		TurnOutTax entity = saveOrUpdateTurnOutTax(vo);
		AuxiliaryAttr voucherWord = entity.getVoucherWord();
		FiscalAccountingSubject taxSubject = entity.getTaxSubject();
		String resume = vo.getResume();
				
		String accId = SecurityUtil.getFiscalAccountId();
		FiscalPeriod period = periodService.getFirstNotCheck();
		if(period==null)return buildFailRequestResult("找不到未结账的会计期间");
		
		//转出未交增值税科目的余额
		BigDecimal taxInitAmount = initBalanceRepo.getAmountBySubjectId(taxSubject.getFid(), null, accId);
		BigDecimal taxVoucherAmount = voucherDetailRepo.getAmountBySubjectId(taxSubject.getFid(), period.getStartDate(),
				period.getEndDate(), accId, Lists.newArrayList(Voucher.STATUS_POSTED));
		
		taxInitAmount = NumberUtil.multiply(taxInitAmount, new BigDecimal(taxSubject.getDirection()));
		
		//转出未交增值税科目的余额
		BigDecimal taxAmount = NumberUtil.add(taxInitAmount, taxVoucherAmount);
		
		//转出未交增值税科目的贷方余额
		BigDecimal creditTaxAmount = NumberUtil.multiply(taxAmount, new BigDecimal(-1));
		
		if(creditTaxAmount.compareTo(BigDecimal.ZERO)<0){
			return buildFailRequestResult("转出未交增值税科目的余额方向在借方，不能结转未交增值税");
		}else if(creditTaxAmount.compareTo(BigDecimal.ZERO)==0){
			return buildFailRequestResult("转出未交增值税科目的余额等于0，不能结转未交增值税");
		}
		
		Voucher voucher = saveVoucher(period, voucherWord, voucherDate);
		saveVoucherDetail(voucher, entity.getTaxSubject(), creditTaxAmount, BigDecimal.ZERO, resume);
		saveVoucherDetail(voucher, entity.getUnpaidSubject(), BigDecimal.ZERO, creditTaxAmount, resume);
		
		RequestResult result = new RequestResult();
		result.setData(voucher.getFid());
		return result;
	}
	
	

	/**
	 * 支付未交增值税
	 * @param vo
	 */
	public RequestResult savePayUnpaidTax(TurnOutTaxVo vo) {
		String inValid = ValidatorUtils.inValidMsg(vo);
		if(inValid!=null){
			return buildFailRequestResult(inValid);
		}
		
		Date voucherDate = DateUtilTools.string2Date(vo.getVoucherDate(), DateUtilTools.DATE_PATTERN_YYYY_MM_DD);
		TurnOutTax entity = saveOrUpdateTurnOutTax(vo);
		AuxiliaryAttr voucherWord = entity.getVoucherWord();
		FiscalAccountingSubject unpaidSubject = entity.getUnpaidSubject();
		String resume = vo.getResume();
		
		String accId = SecurityUtil.getFiscalAccountId();
		FiscalPeriod period = periodService.getFirstNotCheck();
		if(period==null)return buildFailRequestResult("找不到未结账的会计期间");
		
		//转出未交增值税科目的余额
		BigDecimal unpaidInitAmount = initBalanceRepo.getAmountBySubjectId(unpaidSubject.getFid(), null, accId);
		BigDecimal unpaidVoucherAmount = voucherDetailRepo.getAmountBySubjectId(unpaidSubject.getFid(), period.getStartDate(),
				period.getEndDate(), accId, Lists.newArrayList(Voucher.STATUS_POSTED));
		
		unpaidInitAmount = NumberUtil.multiply(unpaidInitAmount, new BigDecimal(unpaidSubject.getDirection()));
		
		//转出未交增值税科目的余额
		BigDecimal unpaidAmount = NumberUtil.add(unpaidInitAmount, unpaidVoucherAmount);
		
		//转出未交增值税科目的贷方余额
		BigDecimal creditUnpaidAmount = NumberUtil.multiply(unpaidAmount, new BigDecimal(-1));
		
		if(creditUnpaidAmount.compareTo(BigDecimal.ZERO)<0){
			return buildFailRequestResult("未交增值税科目的余额方向在借方，不用支付未交增值税");
		}else if(creditUnpaidAmount.compareTo(BigDecimal.ZERO)==0){
			return buildFailRequestResult("未交增值税科目的余额等于0，不能结转未交增值税");
		}
		
		Voucher voucher = saveVoucher(period, voucherWord, voucherDate);
		saveVoucherDetail(voucher, entity.getUnpaidSubject(), creditUnpaidAmount, BigDecimal.ZERO, resume);
		saveVoucherDetail(voucher, entity.getPaySubject(), BigDecimal.ZERO, creditUnpaidAmount, resume);
		
		RequestResult result = new RequestResult();
		result.setData(voucher.getFid());
		return result;
	}

	/**
	 * 保存凭证(进税科目与销税科目一致)
	 * @param period
	 * @param voucherWord
	 * @param entity
	 * @param outAmount
	 * @param inAmount
	 * @param voucherDate
	 * @param resume
	 * @return
	 */
	private RequestResult saveVoucherWithInOutSameSubject(FiscalPeriod period, AuxiliaryAttr voucherWord, TurnOutTax entity, 
			BigDecimal outAmount, BigDecimal inAmount, Date voucherDate, String resume){
		
		if(outAmount.compareTo(BigDecimal.ZERO)==0){
			return buildFailRequestResult("销项税科目的余额为0，生成凭证失败");
		}
		Voucher voucher = saveVoucher(period, voucherWord, voucherDate);
		
		if(outAmount.compareTo(BigDecimal.ZERO)>0){
			saveVoucherDetail(voucher, entity.getOutSubject(), outAmount, BigDecimal.ZERO, resume);
			saveVoucherDetail(voucher, entity.getTaxSubject(), BigDecimal.ZERO, outAmount, resume);
		}else{
			saveVoucherDetail(voucher, entity.getTaxSubject(), outAmount.abs(), BigDecimal.ZERO, resume);
			saveVoucherDetail(voucher, entity.getOutSubject(), BigDecimal.ZERO, outAmount.abs(), resume);
		}
		
		RequestResult result = new RequestResult();
		result.setData(voucher.getFid());
		return result;
	}


	/**
	 * 保存凭证(进税科目与销税科目不一致)
	 * @param period
	 * @param voucherWord
	 * @param entity
	 * @param outAmount
	 * @param inAmount
	 * @param voucherDate
	 * @param resume
	 * @return
	 */
	private RequestResult saveVoucherWithInOutNotSameSubject(FiscalPeriod period, AuxiliaryAttr voucherWord, TurnOutTax entity, 
			BigDecimal outAmount, BigDecimal inAmount, Date voucherDate, String resume){
		
		if(outAmount.compareTo(BigDecimal.ZERO)==0){
			return buildFailRequestResult("销项税科目的余额为0，生成凭证失败");
		}
		
		Voucher voucher = saveVoucher(period, voucherWord, voucherDate);
		
		BigDecimal delta = NumberUtil.subtract(outAmount, inAmount);
		
		if(delta.compareTo(BigDecimal.ZERO)>0){
			saveVoucherDetail(voucher, entity.getOutSubject(), outAmount, BigDecimal.ZERO, resume);
			saveVoucherDetail(voucher, entity.getInSubject(), BigDecimal.ZERO, inAmount, resume);
			saveVoucherDetail(voucher, entity.getTaxSubject(), BigDecimal.ZERO, delta, resume);
		}else{
			saveVoucherDetail(voucher, entity.getTaxSubject(), NumberUtil.multiply(delta, new BigDecimal(-1)), BigDecimal.ZERO, resume);
			saveVoucherDetail(voucher, entity.getInSubject(), BigDecimal.ZERO, inAmount, resume);
			saveVoucherDetail(voucher, entity.getOutSubject(), outAmount, BigDecimal.ZERO, resume);
		}
		
		RequestResult result = new RequestResult();
		result.setData(voucher.getFid());
		return result;
	}
	
	/**
	 * 保存凭证
	 * @param period
	 * @param voucherWord
	 * @return
	 */
	private Voucher saveVoucher(FiscalPeriod period, AuxiliaryAttr voucherWord, Date voucherDate){
		
		String voucherWordId = voucherWord==null?"":voucherWord.getFid();
		Integer voucherNumber = voucherRepo.getMaxVoucherNumberAuto(period.getFid(), voucherWordId) + 1; //凭证号
		Integer number = voucherService.getMaxNumber(period.getFid())+1; //凭证顺序号

		String voucherWordNumber = voucherWord==null?"":voucherWord.getName()+"-"+voucherNumber;
		
		Organization org = SecurityUtil.getCurrentOrg();
		User user = SecurityUtil.getCurrentUser();
		Organization dept = SecurityUtil.getCurrentDept();
		FiscalAccount account = SecurityUtil.getFiscalAccount();
		
		Voucher voucher = new Voucher();
		voucher.setNumber(number);				
		voucher.setVoucherNumber(voucherNumber);
		voucher.setVoucherWord(voucherWord);
		voucher.setVoucherWordNumber(voucherWordNumber);
		voucher.setVoucherDate(voucherDate);
		voucher.setAccessoryNumber(0);
		voucher.setOrg(org);
		voucher.setCreator(user);
		voucher.setFiscalPeriod(period);
		voucher.setFiscalAccount(account);
		voucher.setCreateTime(Calendar.getInstance().getTime());
		voucher.setUpdateTime(Calendar.getInstance().getTime());
		voucher.setDept(dept);
		
		voucherRepo.save(voucher);
		
		return voucher;
	}
	
	/**
	 * 保存凭证明细
	 * @param voucher
	 * @param subject
	 * @param debitAmont
	 * @param creditAmount
	 * @return
	 */
	private VoucherDetail saveVoucherDetail(Voucher voucher, FiscalAccountingSubject subject, BigDecimal debitAmont, 
			BigDecimal creditAmount, String resume){
		
		if(debitAmont.compareTo(creditAmount)==0 && debitAmont.compareTo(BigDecimal.ZERO)==0 ){
			return null;
		}
		
		Organization org = SecurityUtil.getCurrentOrg();
		User user = SecurityUtil.getCurrentUser();
		FiscalAccount account = SecurityUtil.getFiscalAccount();
		
		VoucherDetail detail = new VoucherDetail();
		detail.setVoucher(voucher);
		detail.setResume(resume);
		detail.setOrg(org);
		detail.setCreator(user);
		detail.setFiscalAccount(account);
		detail.setCreateTime(Calendar.getInstance().getTime());
		detail.setUpdateTime(Calendar.getInstance().getTime());
		detail.setAccountingSubject(subject);
		detail.setDebitAmount(debitAmont);
		detail.setCreditAmount(creditAmount);
		
		voucherDetailRepo.save(detail);
		
		return detail;
	}

	@Override
	public CrudRepository<TurnOutTax, String> getRepository() {
		return turnOutTaxRepo;
	}

	/**
	 * 统计某个科目被引用的次数
	 * @param subjectId 科目ID
	 * @return
	 */
	public Long countBySubjectId(String subjectId) {
		return turnOutTaxRepo.countBySubjectId(subjectId);
	}
}
