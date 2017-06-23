package cn.fooltech.fool_ops.domain.fiscal.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.fooltech.fool_ops.domain.voucher.service.VoucherService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.service.AuxiliaryAttrService;
import cn.fooltech.fool_ops.domain.fiscal.entity.AccrueTaxation;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalPeriod;
import cn.fooltech.fool_ops.domain.fiscal.repository.AccrueTaxationRepository;
import cn.fooltech.fool_ops.domain.fiscal.repository.FiscalInitBalanceRepository;
import cn.fooltech.fool_ops.domain.fiscal.vo.AccrueTaxationVo;
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
 * <p>计提税费网页服务类</p>
 * @author xjh
 * @version 1.0
 * @date 2016-04-12 16:14:16
 */
@Service
public class AccrueTaxationService extends BaseService<AccrueTaxation, AccrueTaxationVo, String> {
	
	/**
	 * 计提税费服务类
	 */
	@Autowired
	private AccrueTaxationRepository accrueTaxationRepo;
	
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
	
	@Autowired
	private VoucherDetailRepository voucherDetailRepo;
	
	
	/**
	 * 查询计提税费列表信息，按照计提税费updateTime降序排列<br>
	 */
	public List<AccrueTaxation> query(){
		String accId = SecurityUtil.getFiscalAccountId();
		return accrueTaxationRepo.findByAccId(accId);
	}
	
	
	/**
	 * 单个计提税费实体转换为vo
	 * @param entity
	 * @return
	 */
	@Override
	public AccrueTaxationVo getVo(AccrueTaxation entity){
		if(entity == null)
			return null;
		AccrueTaxationVo vo = new AccrueTaxationVo();
		vo.setPoint(NumberUtil.stripTrailingZeros(entity.getPoint()));
		vo.setUpdateTime(DateUtilTools.date2String(entity.getUpdateTime(), DATE_TIME));
		vo.setFid(entity.getFid());
		vo.setBaseType(entity.getBaseType());
		
		FiscalAccountingSubject baseSubject = entity.getBaseSubject();
		FiscalAccountingSubject paySubject = entity.getPaySubject();
		FiscalAccountingSubject addSubject = entity.getAddSubject();
		FiscalAccountingSubject collectSubject = entity.getCollectSubject();
		
		if(baseSubject!=null){
			vo.setBaseSubjectId(baseSubject.getFid());
			vo.setBaseSubjectCode(baseSubject.getCode());
			vo.setBaseSubjectName(baseSubject.getName());
		}
		
		if(paySubject!=null){
			vo.setPaySubjectId(paySubject.getFid());
			vo.setPaySubjectCode(paySubject.getCode());
			vo.setPaySubjectName(paySubject.getName());
		}
		
		if(addSubject!=null){
			vo.setAddSubjectId(addSubject.getFid());
			vo.setAddSubjectCode(addSubject.getCode());
			vo.setAddSubjectName(addSubject.getName());
		}
		
		if(collectSubject!=null){
			vo.setCollectSubjectId(collectSubject.getFid());
			vo.setCollectSubjectCode(collectSubject.getCode());
			vo.setCollectSubjectName(collectSubject.getName());
		}
		
		return vo;
	}
	
	/**
	 * 删除计提税费，多个ID用逗号隔开<br>
	 */
	public RequestResult delete(String fids){
		Splitter splitter = Splitter.on(",").omitEmptyStrings().trimResults();
		List<String> idList = splitter.splitToList(fids);
		
		for(String id:idList){
			accrueTaxationRepo.delete(id);
		}
		return buildSuccessRequestResult();
	}
	
	/**
	 * 删除所有<br>
	 */
	public RequestResult deleteAll(){
		List<AccrueTaxation> list = query();
		accrueTaxationRepo.delete(list);
		return buildSuccessRequestResult();
	}
	
	/**
	 * 新增/编辑计提税费
	 * @param vo
	 */
	public RequestResult save(AccrueTaxationVo vo) {
		
		String inValid = ValidatorUtils.inValidMsg(vo);
		if(inValid!=null){
			return buildFailRequestResult(inValid);
		}
		
		if(vo.getAddSubjectId().equals(vo.getPaySubjectId())){
			return buildFailRequestResult("应交税费科目与附加税费科目不能相同");
		}
		
		AccrueTaxation entity = null;
		if(StringUtils.isBlank(vo.getFid())){
			entity = new AccrueTaxation();
			entity.setUpdateTime(Calendar.getInstance().getTime());
			entity.setDept(SecurityUtil.getCurrentDept());
			entity.setOrg(SecurityUtil.getCurrentOrg());
			entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
		}else {
			entity = accrueTaxationRepo.findOne(vo.getFid());
			if(entity == null){
				return buildFailRequestResult("该记录不存在或已被删除!");
			}
			if(!checkUpdateTime(vo.getUpdateTime(), entity.getUpdateTime())){
				return buildFailRequestResult("数据已被其他用户修改，请刷新再试");
			}
		}
		
		FiscalAccountingSubject baseSubject = subjectService.get(vo.getBaseSubjectId());
		FiscalAccountingSubject paySubject = subjectService.get(vo.getPaySubjectId());
		FiscalAccountingSubject addSubject = subjectService.get(vo.getAddSubjectId());
		FiscalAccountingSubject collectSubject = subjectService.get(vo.getCollectSubjectId());
		
		entity.setBaseSubject(baseSubject);
		entity.setPaySubject(paySubject);
		entity.setAddSubject(addSubject);
		entity.setCollectSubject(collectSubject);
		
		entity.setPoint(vo.getPoint());
		entity.setBaseType(vo.getBaseType());
		
		accrueTaxationRepo.save(entity);
		return buildSuccessRequestResult();
	}
	
	/**
	 * 计提税费
	 * @return
	 */
	public RequestResult saveAccruedTax(AccrueTaxationVo vo){
		
		String accId = SecurityUtil.getFiscalAccountId();
		Organization org = SecurityUtil.getCurrentOrg();
		FiscalAccount account = SecurityUtil.getFiscalAccount();
		User user = SecurityUtil.getCurrentUser();
		
		RequestResult check = checkValid(vo);
		if(!check.isSuccess()){
			return check;
		}
		
		AuxiliaryAttr voucherWord = attrService.get(vo.getVoucherWordId());
		FiscalPeriod period = periodService.getFirstNotCheck();
		Date voucherDate = DateUtilTools.string2Date(vo.getVoucherDate(), DateUtilTools.DATE_PATTERN_YYYY_MM_DD);
		List<AccrueTaxation> data = query();
		String resume = vo.getResume();
		
		Voucher voucher = saveVoucher(period, voucherWord, voucherDate);
		boolean flag = true;
		
		for(AccrueTaxation accrueTaxation:data){
			
			FiscalAccountingSubject baseSubject = accrueTaxation.getBaseSubject();
			
			//税费基数科目的余额
			BigDecimal baseAmount = BigDecimal.ZERO;
			
			if(accrueTaxation.getBaseType()!=null && accrueTaxation.getBaseType()==AccrueTaxation.TYPE_PERIOD_AMOUNT){
				baseAmount = voucherDetailRepo.getAmountBySubjectId(baseSubject.getFid(), period.getStartDate(),
						period.getEndDate(), accId, Lists.newArrayList(Voucher.STATUS_POSTED));
			}else{
				BigDecimal baseInitAmount = initBalanceRepo.getAmountBySubjectId(baseSubject.getFid(), null, accId);
				BigDecimal baseVoucherAmount = voucherDetailRepo.getAmountBySubjectId(baseSubject.getFid(), accId, Lists.newArrayList(Voucher.STATUS_POSTED));
				
				baseInitAmount = NumberUtil.multiply(baseInitAmount, new BigDecimal(baseSubject.getDirection()));
				baseAmount = NumberUtil.add(baseInitAmount, baseVoucherAmount);
			}
			
			//税费基数科目的贷方余额
			BigDecimal amount = NumberUtil.multiply(baseAmount, new BigDecimal(-1));
			
			if(amount.compareTo(BigDecimal.ZERO)<=0){
				continue;
			}
			
			amount = NumberUtil.multiply(amount, accrueTaxation.getPoint());
			amount = NumberUtil.divide(amount, new BigDecimal(100), 20);
			amount = amount.setScale(2, RoundingMode.HALF_UP);//四舍五入保留两位小数
			
			VoucherDetail detail1 = saveVoucherDetail(voucher, accrueTaxation.getCollectSubject(), amount, 
					FiscalAccountingSubject.DIRECTION_BORROW, org, user, account, resume);
			
			VoucherDetail detail2 = saveVoucherDetail(voucher, accrueTaxation.getAddSubject(), amount, 
					FiscalAccountingSubject.DIRECTION_LOAN, org, user, account, resume);
			
			if(detail1!=null || detail2!=null)flag = false;
		}
		
		
		RequestResult result = new RequestResult();
		
		if(flag){
			//未找到需要保存凭证明细的记录，删除凭证主表
			voucherRepo.delete(voucher);
		}else{
			result.setData(voucher.getFid());
		}
		
		return result;
	}
	
	
	/**
	 * 支付税费
	 * @return
	 */
	public RequestResult savePayTax(AccrueTaxationVo vo){
		
		String accId = SecurityUtil.getFiscalAccountId();
		Organization org = SecurityUtil.getCurrentOrg();
		FiscalAccount account = SecurityUtil.getFiscalAccount();
		User user = SecurityUtil.getCurrentUser();
		
		
		RequestResult check = checkValid(vo);
		if(check.getReturnCode()==RequestResult.RETURN_FAILURE){
			return check;
		}
		
		AuxiliaryAttr voucherWord = attrService.get(vo.getVoucherWordId());
		FiscalPeriod period = periodService.getFirstNotCheck();
		Date voucherDate = DateUtilTools.string2Date(vo.getVoucherDate(), DateUtilTools.DATE_PATTERN_YYYY_MM_DD);
		String resume = vo.getResume();
		List<AccrueTaxation> data = query();
		
		Voucher voucher = saveVoucher(period, voucherWord, voucherDate);
		
		boolean flag = true;
		
		for(AccrueTaxation accrueTaxation:data){
			
			FiscalAccountingSubject addSubject = accrueTaxation.getAddSubject();
			
			//附加税费科目的余额
			BigDecimal payInitAmount = initBalanceRepo.getAmountBySubjectId(addSubject.getFid(), null, accId);
			BigDecimal payVoucherAmount = voucherDetailRepo.getAmountBySubjectId(addSubject.getFid(), accId, Lists.newArrayList(Voucher.STATUS_POSTED));
			
			payInitAmount = NumberUtil.multiply(payInitAmount, new BigDecimal(addSubject.getDirection()));
			BigDecimal payAmount = NumberUtil.add(payInitAmount, payVoucherAmount);
			
			//附加税费科目的贷方余额
			BigDecimal amount = NumberUtil.multiply(payAmount, new BigDecimal(-1));
			
			if(amount.compareTo(BigDecimal.ZERO)<=0){
				continue;
			}
			
			flag = false;
			
			saveVoucherDetail(voucher, addSubject, amount, 
					FiscalAccountingSubject.DIRECTION_BORROW, org, user, account, resume);
			
			saveVoucherDetail(voucher, accrueTaxation.getPaySubject(), amount, 
					FiscalAccountingSubject.DIRECTION_LOAN, org, user, account, resume);
		}
		
		
		RequestResult result = new RequestResult();
		
		if(flag){
			//未找到需要保存凭证明细的记录，删除凭证主表
			voucherRepo.delete(voucher);
		}else{
			result.setData(voucher.getFid());
		}
		
		return result;
	}
	
	/**
	 * 检查合法性
	 * @return
	 */
	private RequestResult checkValid(AccrueTaxationVo vo){
		
		if(Strings.isNullOrEmpty(vo.getVoucherWordId())){
			return buildFailRequestResult("凭证字必填");
		}
		
		if(Strings.isNullOrEmpty(vo.getVoucherDate())){
			return buildFailRequestResult("凭证日期必填");
		}
		
		FiscalPeriod period = periodService.getFirstNotCheck();
		if(period==null)buildFailRequestResult("找不到未结账的会计期间");
		
		List<AccrueTaxation> data = query();
		if(data.size()==0)return buildFailRequestResult("找不到要生成凭证的数据记录");
		
		return buildSuccessRequestResult();
	}
	
	/**
	 * 保存凭证明细
	 * @param voucher
	 * @param subject
	 * @param amount
	 * @param direction
	 */
	private VoucherDetail saveVoucherDetail(Voucher voucher, FiscalAccountingSubject subject, BigDecimal amount, 
			int direction, Organization org, User user, FiscalAccount account, String resume) {
		
		if(amount.compareTo(BigDecimal.ZERO)==0){
			return null;
		}
		
		VoucherDetail detail = new VoucherDetail();
		detail.setVoucher(voucher);
		detail.setResume(resume);
		detail.setAccountingSubject(subject);
		
		if(direction>0){
			detail.setDebitAmount(amount);
			detail.setCreditAmount(BigDecimal.ZERO);
		}else{
			detail.setDebitAmount(BigDecimal.ZERO);
			detail.setCreditAmount(amount);
		}
		detail.setOrg(org);
		detail.setCreator(user);
		detail.setFiscalAccount(account);
		detail.setCreateTime(Calendar.getInstance().getTime());
		detail.setUpdateTime(Calendar.getInstance().getTime());
		
		voucherDetailRepo.save(detail);
		
		return detail;
	}


	/**
	 * 保存凭证主表
	 * @param period
	 * @param voucherWord
	 * @param voucherDate
	 * @return
	 */
	private Voucher saveVoucher(FiscalPeriod period, AuxiliaryAttr voucherWord, Date voucherDate){
		
		String voucherWordId = voucherWord==null?"":voucherWord.getFid();
		int voucherNumber = voucherRepo.getMaxVoucherNumberAuto(period.getFid(), voucherWordId) + 1; //凭证号
		int number = voucherService.getMaxNumber(period.getFid()) + 1; //凭证顺序号
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


	@Override
	public CrudRepository<AccrueTaxation, String> getRepository() {
		return accrueTaxationRepo;
	}

	/**
	 * 根据科目id统计
	 * @param subjectId 科目id
	 * @return
	 */
	public Long countBySubjectId(String subjectId) {
		return accrueTaxationRepo.countBySubjectId(subjectId);
	}
}
