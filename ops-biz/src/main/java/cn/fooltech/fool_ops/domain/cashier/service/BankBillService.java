package cn.fooltech.fool_ops.domain.cashier.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttrType;
import cn.fooltech.fool_ops.domain.basedata.entity.Member;
import cn.fooltech.fool_ops.domain.basedata.service.AuxiliaryAttrService;
import cn.fooltech.fool_ops.domain.cashier.entity.BankBill;
import cn.fooltech.fool_ops.domain.cashier.entity.BankCheckdate;
import cn.fooltech.fool_ops.domain.cashier.entity.BankInit;
import cn.fooltech.fool_ops.domain.cashier.repository.BankBillRepository;
import cn.fooltech.fool_ops.domain.cashier.vo.BankBillVo;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalAccountingSubjectService;
import cn.fooltech.fool_ops.domain.member.service.MemberService;
import cn.fooltech.fool_ops.domain.payment.service.PaymentBillService;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.validator.ValidatorUtils;

/**
 * <p>出纳-银行单据记录网页服务类</p>
 * @author xjh
 * @version 1.0
 * @date 2015-12-14 15:07:24
 */
@Service
public class BankBillService extends BaseService<BankBill,BankBillVo,String> {
	
	/**
	 * 初始银行设置服务类
	 */
	@Autowired
	private BankInitService bankInitService;
	
	/**
	 * 银行单据记录持久类
	 */
	@Autowired
	private BankBillRepository bankBillRepository;
	
	/**
	 * 轧账日期服务类
	 */
	@Autowired
	private BankCheckdateService bankCheckdateService;
	
	/**
	 * 人员服务类
	 */
	@Autowired
	private MemberService memberService;
	
	/**
	 * 财务-科目服务类
	 */
	@Autowired
	private FiscalAccountingSubjectService subjectService;
	
	/**
	 * 属性服务类
	 */
	@Autowired
	private AuxiliaryAttrService attrService;

    /**
     * 收付款单服务
     */
    @Autowired
    private PaymentBillService paymentBillService;
	
	
	/**
	 * 查询出纳-银行单据记录列表信息，按照出纳-银行单据记录主键降序排列<br>
	 * 默认为第一页，每页大小默认为10<br>
	 * @param vo
	 */
	public Page<BankBill> query(BankBillVo vo,PageParamater pageParamater){
		Sort sort = new Sort(Direction.ASC, "voucherDate","orderno");
		PageRequest request = getPageRequest(pageParamater,sort);
		Page<BankBill> query = bankBillRepository.query(vo, request);
		return query;
	}

	/**
	 *  根据IDs查找银行对账单单
	 * @param ids
	 * @return
	 */
	public List<BankBill> getByIds(List<String> ids){
		if(ids == null || ids.size() == 0){
			return Collections.emptyList();
		}
		return bankBillRepository.getByIDs(ids);
	}

	/**
	 * 银行单设置余额
	 * @param vos 银行单
	 */
	public List<BankBillVo> getVo(List<BankBillVo> vos) {
		List<BankBillVo> news = Lists.newArrayList();
		MyCompartor mc = new MyCompartor();
		MyCompartor2 mc2 = new MyCompartor2();
		Collections.sort(vos, mc); // 按照凭证日期（单据日期）升序
		Collections.sort(vos, mc); // 按照当天顺序号升序
//		BigDecimal accountAmount= null;
		BigDecimal statementAmount = null;
//		BigDecimal cashAmount= null;
		for (BankBillVo vo: vos) {
			BankBill entity = bankBillRepository.findOne(vo.getFid());
			if (entity == null)
				continue;
			if (BankBill.TYPE_CREDIT == entity.getType()) {
				BigDecimal balance = getAccountAmount(vo.getSubjectId(), entity);
				vo.setBalance(balance);
			} else if (BankBill.TYPE_STATEMENT == entity.getType()) {
				if(statementAmount==null){
					BigDecimal balance = getStatementAmount(vo.getSubjectId(), entity);
					vo.setBalance(balance);
					statementAmount=balance;
				}else{
					/*银行日记账=银行初始化对账单余额-日记账借方金额+日记账贷方金额；*/
					BigDecimal balance=statementAmount.subtract(entity.getDebit()).add(entity.getCredit());
					vo.setBalance(balance);
					statementAmount=balance;
				}
				
			} else if (BankBill.TYPE_CASH == entity.getType()) {
				BigDecimal balance = getCashAmount(vo.getSubjectId(), entity);
				vo.setBalance(balance);
			}

			news.add(vo);
		}
		return news;
	}
	/**
	 * 银行单设置余额
	 * @param vo 银行单
	 */
	public List<BankBillVo> getOneVo(BankBillVo vo) {
		List<BankBillVo> news = Lists.newArrayList();
			BankBill entity = bankBillRepository.findOne(vo.getFid());
			if (entity == null)
				return null;
			if (BankBill.TYPE_CREDIT == entity.getType()) {
				BigDecimal balance = getAccountAmount(vo.getSubjectId(), entity);
				vo.setBalance(balance);
			} else if (BankBill.TYPE_STATEMENT == entity.getType()) {
				BigDecimal balance = getStatementAmount(vo.getSubjectId(), entity);
				vo.setBalance(balance);
			}else if (BankBill.TYPE_CASH == entity.getType()) {
				BigDecimal balance = getCashAmount(vo.getSubjectId(), entity);
				vo.setBalance(balance);
			}

			news.add(vo);
		
		return news;
	}
	/**
	 * 单个出纳-银行单据记录实体转换为vo
	 * @param entity
	 * @return
	 */
	@Override
	public BankBillVo getVo(BankBill entity){
		if(entity == null)
			return null;
		BankBillVo vo = new BankBillVo();
		vo.setType(entity.getType());
		vo.setSettlementDate(DateUtilTools.date2String( entity.getSettlementDate(), DATE_TIME));
		if(entity.getOrderno()!=null){vo.setOrderno(new BigDecimal(entity.getOrderno()));}
		vo.setSettlementNo(entity.getSettlementNo());
		vo.setDebit(NumberUtil.scale(entity.getDebit(), 2));
		vo.setCredit(NumberUtil.scale(entity.getCredit(), 2));
		vo.setVoucherDate(DateUtilTools.date2String( entity.getVoucherDate(), DATE_TIME));
		vo.setResume(entity.getResume());
		vo.setFchecked(entity.getChecked());
		vo.setCheckDate(DateUtilTools.date2String( entity.getCheckDate(), DATE_TIME));
		vo.setCreateTime(DateUtilTools.date2String( entity.getCreateTime(), DATE_TIME));
		vo.setUpdateTime(DateUtilTools.date2String( entity.getUpdateTime(), DATE_TIME));
		vo.setFid(entity.getFid());
		vo.setDirection(entity.getDirection());
		vo.setSource(entity.getSource());
		
		User creator = entity.getCreator();
		if(creator!=null){
			vo.setCreatorId(creator.getFid());
			vo.setCreatorName(creator.getUserName());
		}
		
		FiscalAccount account = entity.getFiscalAccount();
		if(account!=null){
			vo.setFiscalAccountId(account.getFid());
			vo.setFiscalAccountName(account.getName());
		}
		
		FiscalAccountingSubject subject = entity.getSubject();
		if(subject!=null){
			vo.setSubjectId(subject.getFid());
			vo.setSubjectName(subject.getName());
		}
		
		Member member = entity.getMember();
		if(member!=null){
			vo.setMemberId(member.getFid());
			vo.setMemberName(member.getUsername());
		}
		
		AuxiliaryAttr settlementType = entity.getSettlementType();
		if(settlementType!=null){
			vo.setSettlementTypeId(settlementType.getFid());
			vo.setSettlementTypeName(settlementType.getName());
		}
		
		return vo;
	}
	
	/**
	 * 删除出纳-银行单据记录<br>
	 */
	public RequestResult delete(BankBillVo vo){
		
		String accountId = SecurityUtil.getFiscalAccountId();
		
/*		if(bankCheckdateService.countAll(accountId)>0){
			return new RequestResult(RequestResult.RETURN_FAILURE, "已结账不能删除");
		}*/


		BankBill entity = bankBillRepository.findOne(vo.getFid());
		if (entity == null) {
		    return new RequestResult(RequestResult.RETURN_FAILURE, "单据ID不能为空，请刷新再试！");
        }

		BankCheckdate checkdate = bankCheckdateService.getMaxCheckDate(accountId);
		String source = entity.getSource() == null ? "" : entity.getSource();

		if(checkdate!=null){
			if(checkdate.getCheckDate().compareTo(entity.getVoucherDate())>=0){
				if (entity.getType() != BankBill.TYPE_CASH && entity.getType() != BankBill.TYPE_STATEMENT) {
					return new RequestResult(RequestResult.RETURN_FAILURE, "单据日期要在结账日期之后");
				}else{
					return new RequestResult(RequestResult.RETURN_FAILURE, "日期要在结账日期之后");
				}
			}
			if (entity.getType() != BankBill.TYPE_CASH && entity.getType() != BankBill.TYPE_STATEMENT && entity.getType() != BankBill.TYPE_COMPANY) {
				if(checkdate.getCheckDate().compareTo(entity.getSettlementDate())>=0){
					return new RequestResult(RequestResult.RETURN_FAILURE, "业务日期要在结账日期之后");
				}
			}
			
		}
		if(entity.getChecked()!=null && entity.getChecked()==BankBill.CHECKED){
			return new RequestResult(RequestResult.RETURN_FAILURE, "已勾对不能删除");
		}
		
		if(entity.getType()==BankBill.TYPE_BANK||entity.getType()==BankBill.TYPE_COMPANY){
			BankInit bankInit = bankInitService.get(vo.getBankInitId());
			if(bankInit.getStart()==BankInit.START){
				return new RequestResult(RequestResult.RETURN_FAILURE, "初始银行设置已启用");
			}
			
			BigDecimal deltaCredit = NumberUtil.subtract(BigDecimal.ZERO, entity.getCredit());
			BigDecimal deltaDebit = NumberUtil.subtract(BigDecimal.ZERO, entity.getDebit());
			bankInitService.updateAmountBySubject(deltaCredit, deltaDebit, entity);
		}
		
		bankBillRepository.delete(vo.getFid());
		// 同时擦除收付款单生成日记单记录
        paymentBillService.cancelBankBillRecord(source);
		return buildSuccessRequestResult();
	}
	
	/**
	 * 获取出纳-银行单据记录信息
	 * @param fid 出纳-银行单据记录ID
	 * @return
	 */
	public BankBillVo getByFid(String fid) {
		Assert.notNull(fid);
		return getVo(bankBillRepository.findOne(fid));
	}
	

	/**
	 * 新增/编辑出纳-银行单据记录
	 * @param vo
	 */
	public RequestResult save(BankBillVo vo) {
		String inValid = ValidatorUtils.inValidMsg(vo);
		if(inValid!=null){
			return new RequestResult(RequestResult.RETURN_FAILURE,inValid);
		}
		String accountId = SecurityUtil.getFiscalAccountId();
		String orgId = SecurityUtil.getCurrentOrgId();
		
		if(vo.getCredit()==null)vo.setCredit(BigDecimal.ZERO);
		if(vo.getDebit()==null)vo.setDebit(BigDecimal.ZERO);
			
		if(vo.getCredit().equals(BigDecimal.ZERO) && vo.getDebit().equals(BigDecimal.ZERO)){
			return new RequestResult(RequestResult.RETURN_FAILURE, "借方金额和贷方金额不能两个同时为0");
		}
		
		if(vo.getCredit().compareTo(BigDecimal.ZERO) != 0 && vo.getDebit().compareTo(BigDecimal.ZERO) != 0){
			return new RequestResult(RequestResult.RETURN_FAILURE, "借方金额和贷方金额不能两个同时不为0");
		}
		
		FiscalAccountingSubject subject = null;

		if(vo.getType()==BankBill.TYPE_BANK||vo.getType()==BankBill.TYPE_COMPANY){
			BankInit bankInit = bankInitService.get(vo.getBankInitId());
			subject = bankInit.getSubject();
			if(bankInit.getStart()==BankInit.START){
				return new RequestResult(RequestResult.RETURN_FAILURE, "初始银行设置已启用");
			}
		}else{
			if(StringUtils.isNotBlank(vo.getSubjectId())){
				subject = subjectService.get(vo.getSubjectId());
			}else if(StringUtils.isNotBlank(vo.getSubjectCode())){
				subject = subjectService.getByCode(vo.getSubjectCode());
			}
			if(subject==null){
				return new RequestResult(RequestResult.RETURN_FAILURE, "科目未填写或科目填写错误");
			}
				
			if((subject.getBankSubject()==null || subject.getBankSubject()!=FiscalAccountingSubject.BANK_YES)
					&& vo.getType()==BankBill.TYPE_STATEMENT ){
				return new RequestResult(RequestResult.RETURN_FAILURE, "非银行科目");
			}
			if((subject.getBankSubject()==null || subject.getBankSubject()!=FiscalAccountingSubject.BANK_YES)
					&& vo.getType()==BankBill.TYPE_CREDIT ){
				return new RequestResult(RequestResult.RETURN_FAILURE, "非银行科目");
			}
			if((subject.getCashSubject()==null || subject.getCashSubject()!=FiscalAccountingSubject.CASH_YES)
					&& vo.getType()==BankBill.TYPE_CASH ){
				return new RequestResult(RequestResult.RETURN_FAILURE, "非现金科目");
			}
			if(subject.getFlag()==FiscalAccountingSubject.FLAG_PARENT){
				return new RequestResult(RequestResult.RETURN_FAILURE, "科目不能是父科目");
			}
		}
		
		Date voucherDate = DateUtilTools.string2Date(vo.getVoucherDate(), DATE);
		Date settlementDate = DateUtilTools.string2Date(vo.getSettlementDate(), DATE);
		
		if(voucherDate==null){
			if (vo.getType() != BankBill.TYPE_CASH && vo.getType() != BankBill.TYPE_STATEMENT) {
				return new RequestResult(RequestResult.RETURN_FAILURE, "单据日期未填写或填写错误");
			}else{
				return new RequestResult(RequestResult.RETURN_FAILURE, "日期未填写或填写错误");
			}
			
		}
		BigDecimal orderno=vo.getOrderno();
		if(voucherDate!=null&&orderno!=null){
			if(!Strings.isNullOrEmpty(vo.getFid())){
				if (bankBillRepository.countByDateAndNo(voucherDate,orderno.intValue(),subject.getFid(),vo.getFid(),SecurityUtil.getFiscalAccountId(),vo.getType())>0) {
					return new RequestResult(RequestResult.RETURN_FAILURE, "当日顺序号重复");
				}
			}else{
				if (bankBillRepository.countByDateAndNo(voucherDate,orderno.intValue(),subject.getFid(),SecurityUtil.getFiscalAccountId(),vo.getType())>0) {
					return new RequestResult(RequestResult.RETURN_FAILURE, "当日顺序号重复");
				}
			}
			
		}
		if (vo.getType() != BankBill.TYPE_CASH && vo.getType() != BankBill.TYPE_STATEMENT && vo.getType() != BankBill.TYPE_COMPANY) {
			if(settlementDate==null)return new RequestResult(RequestResult.RETURN_FAILURE, "业务日期未填写或填写错误");
		}
		
		BankCheckdate checkdate = bankCheckdateService.getMaxCheckDate(accountId);
		
		if(checkdate!=null){
			if(checkdate.getCheckDate().compareTo(voucherDate)>=0){
				if (vo.getType() != BankBill.TYPE_CASH && vo.getType() != BankBill.TYPE_STATEMENT) {
					return new RequestResult(RequestResult.RETURN_FAILURE, "单据日期要在结账日期之后");
				}else{
					return new RequestResult(RequestResult.RETURN_FAILURE, "日期要在结账日期之后");
				}
			}
			if (vo.getType() != BankBill.TYPE_CASH && vo.getType() != BankBill.TYPE_STATEMENT && vo.getType() != BankBill.TYPE_COMPANY) {
				if(checkdate.getCheckDate().compareTo(settlementDate)>=0){
					return new RequestResult(RequestResult.RETURN_FAILURE, "业务日期要在结账日期之后");
				}
			}
			
		}
		
		BankBill entity = null;
		
		BigDecimal deltaCredit = BigDecimal.ZERO;
		BigDecimal deltaDebit = BigDecimal.ZERO;
		//手续费
		BigDecimal poundage = BigDecimal.ZERO; 
		
		if(StringUtils.isBlank(vo.getFid())){
			entity = new BankBill();
			entity.setCreateTime(new Date());
			entity.setCreator(SecurityUtil.getCurrentUser());
			entity.setOrg(SecurityUtil.getCurrentOrg());
			entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
			
			deltaCredit = vo.getCredit();
			deltaDebit = vo.getDebit();
			if(vo.getType()==3){
				poundage = vo.getPoundage();
			}
			
		}else{
			entity = bankBillRepository.findOne(vo.getFid());

			//银行日记账单是收付费单生成的不能修改除摘要外的信息
			if (StringUtils.isNotEmpty(entity.getSource())) {
				Integer orderNo = entity.getOrderno() == null ? 0 : entity.getOrderno();
				Integer voOrderNo = vo.getOrderno() == null ? 0 : vo.getOrderno().intValue();
				String settlementTypeId = entity.getSettlementType() == null ? "" : entity.getSettlementType().getFid();
				String settlementNo = entity.getSettlementNo() == null ? "" : entity.getSettlementNo();
				String memberId = entity.getMember() == null ? "" : entity.getMember().getFid();
				String voMemberId = vo.getMemberId() == null ? "" : vo.getMemberId();
				if (entity.getVoucherDate().getTime() != DateUtilTools.string2Date(vo.getVoucherDate()).getTime()
						|| entity.getSettlementDate().getTime() != DateUtilTools.string2Date(vo.getSettlementDate()).getTime()
						|| !settlementNo.equals(vo.getSettlementNo())
						|| !settlementTypeId.equals(vo.getSettlementTypeId())
						|| entity.getDebit().compareTo(vo.getDebit()) != 0
						|| entity.getCredit().compareTo(vo.getCredit()) != 0
						|| orderNo.compareTo(voOrderNo)  != 0
						|| !memberId.equals(voMemberId)
						) {
					return new RequestResult(RequestResult.RETURN_FAILURE, "不允许修改除摘要外的信息！");
				}
			}
			if(entity.getChecked()==BankBill.CHECKED){
				return new RequestResult(RequestResult.RETURN_FAILURE, "已勾对不能修改");
			}
			
			deltaCredit = NumberUtil.subtract(vo.getCredit(), entity.getCredit());
			deltaDebit = NumberUtil.subtract(vo.getDebit(), entity.getDebit());
		}

		if (checkOrderNoExist(subject.getFid(), vo,vo.getType()) < 0) {
            return new RequestResult(RequestResult.RETURN_FAILURE, "当天顺序号不能重复");
        }

		Member member = null;
		if(StringUtils.isNotBlank(vo.getMemberId())){
			member = memberService.get(vo.getMemberId());
		}else if(StringUtils.isNotBlank(vo.getMemberCode())){
			member = memberService.getByCode(orgId, vo.getMemberCode());
		}
		if(member==null){
			if (vo.getType() != BankBill.TYPE_CASH && vo.getType() != BankBill.TYPE_STATEMENT && vo.getType() != BankBill.TYPE_COMPANY) {
				
				return new RequestResult(RequestResult.RETURN_FAILURE, "经手人填写错误");
			}
			
		}
		
		entity.setMember(member);
		
		AuxiliaryAttr settlementType = null;
		if(StringUtils.isNotBlank(vo.getSettlementTypeId())){
			settlementType = attrService.get(vo.getSettlementTypeId());
		}else if(StringUtils.isNotBlank(vo.getSettlementTypeCode())){
			settlementType = attrService.getByCode(orgId, AuxiliaryAttrType.CODE_SETTLEMENT_TYPE, 
					vo.getSettlementTypeCode(), accountId);
			if(settlementType==null){
				return new RequestResult(RequestResult.RETURN_FAILURE, "结算方式填写错误");
			}
		}
		if(!Strings.isNullOrEmpty(vo.getSettlementNo())){
			try {
				String settlementNo = vo.getSettlementNo();
//				Integer.valueOf(settlementNo);// 把字符串强制转换为数字
				BigDecimal.valueOf(Double.valueOf(settlementNo));
			} catch (Exception e) {
				return new RequestResult(RequestResult.RETURN_FAILURE, "结算号填写错误");
			}
		}
		
		entity.setSettlementType(settlementType);
		
		entity.setSubject(subject);
		entity.setType(vo.getType());
		entity.setSettlementDate(settlementDate);

        if (vo.getOrderno()!=null) {
            entity.setOrderno(vo.getOrderno().intValue());
        } else {
            int maxOrderNo = getMaxOrderNo(subject.getFid(), vo,vo.getType()) + 1;
            entity.setOrderno(maxOrderNo);
        }

		entity.setSettlementNo(vo.getSettlementNo());
		entity.setDebit(vo.getDebit());
		entity.setCredit(vo.getCredit());
		entity.setVoucherDate(voucherDate);
		entity.setResume(vo.getResume());
		entity.setDept(SecurityUtil.getCurrentDept());
		
		bankInitService.updateAmountBySubject(deltaCredit, deltaDebit, entity);
		BankBill entity2 = new BankBill();
		fillProperties(entity, entity2);
		bankBillRepository.save(entity);
		//当手续费不为空时添加一条手续费记录
		if (vo.getType()==3) {
			if (poundage!=null&&poundage!=BigDecimal.ZERO) {
				if (entity.getDebit()!=null&&entity.getDebit().compareTo(BigDecimal.ZERO)>0) {
					entity2.setDebit(poundage);
					entity2.setCredit(BigDecimal.ZERO);
				}else{
					entity2.setDebit(BigDecimal.ZERO);
					entity2.setCredit(poundage);
				}
				entity2.setSettlementDate(settlementDate);
				String date2String = DateUtilTools.date2String(settlementDate, DATE);
				entity2.setResume(""+date2String+"手续费");
				bankBillRepository.save(entity2);
			}
			
		}
		
		return buildSuccessRequestResult(getOneVo(getVo(entity)));
	}

	/**
	 * 保存银行单据记录
	 * @param vo
	 */
	public String saveBankBill(BankBillVo vo) {
		BankBill entity = new BankBill();

		String accountId = SecurityUtil.getFiscalAccountId();
		String orgId = SecurityUtil.getCurrentOrgId();
		BigDecimal deltaCredit = vo.getCredit();
		BigDecimal deltaDebit = vo.getDebit();

		entity.setCreateTime(new Date());
		entity.setCreator(SecurityUtil.getCurrentUser());
		entity.setOrg(SecurityUtil.getCurrentOrg());
		entity.setFiscalAccount(SecurityUtil.getFiscalAccount());

		BankCheckdate checkdate = bankCheckdateService.getMaxCheckDate(accountId);
		Date voucherDate = DateUtilTools.string2Date(vo.getVoucherDate(), DATE);
		Date settlementDate = DateUtilTools.string2Date(vo.getSettlementDate(), DATE);

		if(checkdate!=null){
			if(checkdate.getCheckDate().compareTo(voucherDate)>=0){
				if (vo.getType() != BankBill.TYPE_CASH
						&& vo.getType() != BankBill.TYPE_STATEMENT) {
					return new String("ERROR:单据日期要在结账日期之后");
				}else{
					return new String("ERROR:日期要在结账日期之后");
				}
			}
			if (vo.getType() != BankBill.TYPE_CASH
					&& vo.getType() != BankBill.TYPE_STATEMENT
					&& vo.getType() != BankBill.TYPE_COMPANY) {
				if(checkdate.getCheckDate().compareTo(settlementDate)>=0){
					return new String("ERROR:业务日期要在结账日期之后");
				}
			}
		}

		Member member = null;
		if (StringUtils.isNotBlank(vo.getMemberId())) {
			member = memberService.get(vo.getMemberId());
		} else if (StringUtils.isNotBlank(vo.getMemberCode())){
			member = memberService.getByCode(orgId, vo.getMemberCode());
		}
		if (member==null) {
			if (vo.getType() != BankBill.TYPE_CASH
                    && vo.getType() != BankBill.TYPE_STATEMENT
                    && vo.getType() != BankBill.TYPE_COMPANY) {
				return new String("ERROR:经手人填写错误");
			}
		}
		entity.setMember(member);

        FiscalAccountingSubject subject = null;
        if(StringUtils.isNotBlank(vo.getSubjectId())){
            subject = subjectService.get(vo.getSubjectId());
        }
        if(subject==null){
            return new String("ERROR:科目未填写或科目填写错误");
        }

        if ((subject.getBankSubject()==null || subject.getBankSubject()!=FiscalAccountingSubject.BANK_YES)
                && vo.getType()==BankBill.TYPE_CREDIT ) {
            return new String("ERROR:非银行科目");
        }

        if (subject.getFlag()==FiscalAccountingSubject.FLAG_PARENT) {
            return new String("ERROR:科目不能是父科目");
        }

		entity.setSubject(subject);
		entity.setType(vo.getType());
		entity.setSettlementDate(DateUtilTools.string2Date(vo.getSettlementDate()));
        int maxOrderNo = getMaxOrderNo(subject.getFid(), vo,vo.getType()) + 1;
        entity.setOrderno(maxOrderNo);
		entity.setSettlementNo(vo.getSettlementNo());
		entity.setDebit(vo.getDebit());
		entity.setCredit(vo.getCredit());
		entity.setVoucherDate(DateUtilTools.string2Date(vo.getVoucherDate()));
		entity.setResume(vo.getResume());
		entity.setDept(SecurityUtil.getCurrentDept());
		entity.setSource(vo.getSource());

		bankInitService.updateAmountBySubject(deltaCredit, deltaDebit, entity);
		BankBill entity2 = new BankBill();
		fillProperties(entity, entity2);
		bankBillRepository.save(entity);

		return entity.getFid().toString();
	}

	/**
	 * 实体对象属性拷贝
	 * @param src 原实体
	 * @param dest	新实体
	 */
	private void fillProperties(BankBill src, BankBill dest) {
		dest.setCheckBill(src.getCheckBill());
		dest.setCheckDate(src.getCheckDate());
		dest.setChecked(src.getChecked());
		dest.setCreateTime(src.getCreateTime());
		dest.setCreator(src.getCreator());
		dest.setCredit(src.getCredit());
		dest.setDebit(src.getDebit());
		dest.setDept(src.getDept());
		dest.setDirection(src.getDirection());
		dest.setFiscalAccount(src.getFiscalAccount());
		dest.setMember(src.getMember());
		dest.setOrderno(src.getOrderno());
		dest.setOrg(src.getOrg());
		dest.setResume(src.getResume());
		dest.setSettlementDate(src.getSettlementDate());
		dest.setSettlementNo(src.getSettlementNo());
		dest.setSettlementType(src.getSettlementType());
		dest.setSubject(src.getSubject());
		dest.setType(src.getType());
		dest.setUpdateTime(src.getUpdateTime());
		dest.setVoucherDate(src.getVoucherDate());
		
	}
	
	/**
	 * 手动对账
	 * @param upId 上表的ID
	 * @param downId 下表的ID
	 * @return
	 */
	public RequestResult saveByCheck(String upId, String downId, String checkDateStr){
		BankBill up = bankBillRepository.findOne(upId);
		BankBill down = bankBillRepository.findOne(downId);
		
		if(!up.getDebit().equals(down.getCredit())||!up.getCredit().equals(down.getDebit())){
			return new RequestResult(RequestResult.RETURN_FAILURE, "借贷方必须在相反方向且金额相等才能勾对");
		}
		if(up.getChecked()!=BankBill.UN_CHECKED||down.getChecked()!=BankBill.UN_CHECKED){
			return new RequestResult(RequestResult.RETURN_FAILURE, "数据已勾对，对账失败");
		}
		
		Date checkDate = DateUtilTools.string2Date(checkDateStr, DATE);
		
		up.setChecked(BankBill.CHECKED);
		up.setCheckBill(down);
		up.setCheckDate(checkDate);
		
		down.setChecked(BankBill.CHECKED);
		down.setCheckBill(up);
		down.setCheckDate(checkDate);
		down.setResume(up.getResume());
		
		bankBillRepository.save(up);
		bankBillRepository.save(down);
		
		return buildSuccessRequestResult();
	}
	
	/**
	 * 自动对账
	 * @param checkDateStr 对账日期
	 * @param limitDateStr 截止日期
	 * @param checkDay 相隔日期是否勾选 0：不勾选 1：勾选
	 * @param days 相隔多少日期（当checkDay=1时有效）
	 * @param settlementType 结算方式相同是否勾选 0：不勾选 1：勾选
	 * @param settlementNo 结算号相同是否勾选 0：不勾选 1：勾选
	 * @param settlementDate 结算日期相同是否勾选 0：不勾选 1：勾选
	 * @param subjectId 科目id
	 * @return
	 */
	public RequestResult saveByAutoCheck(String checkDateStr, String limitDateStr, 
			int checkDay, int days, 
			int settlementType, int settlementNo, Integer settlementDate,String subjectId){
		
		Date checkDate = DateUtilTools.string2Date(checkDateStr, DATE);
		Date limitDate = DateUtilTools.string2Date(limitDateStr, DATE);
		
		String accountId = SecurityUtil.getFiscalAccountId();
		
		if(Strings.isNullOrEmpty(subjectId)) return new RequestResult(RequestResult.RETURN_FAILURE, "请先选择银行科目!");
		List<BankBill> upList = bankBillRepository.queryAutoCheckUp(accountId,subjectId);
		
		for(BankBill up:upList){
			List<BankBill> downList = bankBillRepository.queryAutoCheckDown(accountId, limitDate, checkDay, days, settlementType, settlementNo, settlementDate, up,subjectId);
			if(downList.size()!=1)continue;
			BankBill down = downList.get(0);
			
			up.setChecked(BankBill.CHECKED);
			up.setCheckBill(down);
			up.setCheckDate(checkDate);
			
			down.setChecked(BankBill.CHECKED);
			down.setCheckBill(up);
			down.setCheckDate(checkDate);
			down.setResume(up.getResume());
			
			bankBillRepository.save(up);
			bankBillRepository.save(down);
		}
		
		return  buildSuccessRequestResult();
	}
	
	/**
	 * 取消对账
	 * @param ids 多个ID用逗号隔开
	 * @return
	 */
	public RequestResult deleteCheck(String ids){
		Splitter splitter = Splitter.on(",").omitEmptyStrings().trimResults();
		List<String> idList = splitter.splitToList(ids);
		
		if (idList.size()==0) {
			return new RequestResult(RequestResult.RETURN_FAILURE, "取消对账失败，你未勾选任何数据");
		}
		
		for (String fid :idList) {
			BankBill curr = bankBillRepository.findOne(fid);
			if(curr.getChecked()!=BankBill.CHECKED){
				return new RequestResult(RequestResult.RETURN_FAILURE, "取消对账失败，数据非已对账状态");
			}
		}
		
		for (String id :idList) {
			BankBill curr = bankBillRepository.findOne(id);
			BankBill other = curr.getCheckBill();

			curr.setCheckBill(null);
			curr.setCheckDate(null);
			curr.setChecked(BankBill.UN_CHECKED);
            curr.setResume(curr.getType().compareTo(BankBill.TYPE_STATEMENT)==0?null:curr.getResume());
			bankBillRepository.save(curr);

			if (other!=null && other.getChecked()==BankBill.CHECKED) {
                other.setCheckBill(null);
                other.setCheckDate(null);
                other.setResume(other.getType().compareTo(BankBill.TYPE_STATEMENT)==0?null:other.getResume());
                other.setChecked(BankBill.UN_CHECKED);
				bankBillRepository.save(other);
			}
		}
		
		return buildSuccessRequestResult();
	}

	@Override
	public CrudRepository<BankBill, String> getRepository() {
		return bankBillRepository;
	}
	/**
	 * 统计贷方金额
	 * @param fiscalAccountId 财务账套ID
	 * @param subjectId 科目ID
	 * @param checked 勾对标识：0--未勾对 1--已勾对
	 * @param type 单据类型
	 * @return
	 * @author rqh
	 */
	public BigDecimal countCreditAmount(String fiscalAccountId, String subjectId, short checked, List<Integer> type){
		BigDecimal decimal = bankBillRepository.countCreditAmount(fiscalAccountId,subjectId,checked,type);
		return decimal==null?BigDecimal.ZERO:decimal;
	}
	/**
	 * 查询记录
	 * @param fiscalAccountId 财务账套ID
	 * @param subjectId 科目ID
	 * @param checked 勾对标识：0--未勾对 1--已勾对
	 * @param type 单据类型
	 * @return
	 * @author rqh
	 */
	public List<BankBill> getBillds(String fiscalAccountId, String subjectId, short checked, List<Integer> type){
		return bankBillRepository.getBillds(fiscalAccountId, subjectId, checked, type);
		
	}
	/**
	 * 查询记录
	 * @param fiscalAccountId 财务账套ID
	 * @param subjectId 科目ID
	 * @param type 单据类型
     * @param voucherDate 单据日期
	 */
	public List<BankBill> getBillFilterBy(String fiscalAccountId, String subjectId, Integer type, Date voucherDate){
		return bankBillRepository.getBillFilterBy(fiscalAccountId, subjectId, type, voucherDate);

	}
	/**
	 * 统计借方金额
	 * @param fiscalAccountId 财务账套ID
	 * @param subjectId 科目ID
	 * @param checked 勾对标识：0--未勾对 1--已勾对
	 * @param type 单据类型
	 * @return
	 * @author rqh
	 */
	public BigDecimal countDebitAmount(String fiscalAccountId, String subjectId, short checked, List<Integer>type){
		 BigDecimal decimal = bankBillRepository.countDebitAmount(fiscalAccountId, subjectId, checked, type);
		return decimal==null?BigDecimal.ZERO:decimal;
	}
	
	/**
	 * 根据科目删除数据
	 * @param subject
	 */
	public void deleteBySubject(FiscalAccountingSubject subject) {
		List<BankBill> list = bankBillRepository.queryBySubject(subject.getFid());
		for(BankBill bill:list){
			delete(bill.getFid());
		}
	}
	
	/**
	 * 根据科目ID统计
	 * @return
	 */
	public Long countBySubjectId(String subjectId) {
		return bankBillRepository.countBySubjectId(subjectId);
	}


	/**
	 * 获取银行日记账余额(带筛选过滤）
	 * @param subjectId 科目ID
	 * @param bankBill 银行单
	 * */
	private BigDecimal getAccountAmount(String subjectId, BankBill bankBill){
		//银行日记账期初余额
		BankInit bankInit = bankInitService.getRecord(SecurityUtil.getFiscalAccountId(), subjectId);
		BigDecimal accountInit = (bankInit == null) ? BigDecimal.ZERO : bankInit.getAccountInit();

		// 查询该单之前的订单
		List<BankBill> bills = getBillFilterBy(SecurityUtil.getFiscalAccountId(),
				subjectId, BankBill.TYPE_CREDIT, bankBill.getVoucherDate());

		/*cwz 2017-5-9 禅道2118 银行对账单的余额处理有误  start*/
//		/*银行日记账=银行初始化余额+日记账借方金额-日记账贷方金额；*/
		//计算金额
		Double change = bills.stream().filter(b -> b.getVoucherDate().getTime() < bankBill.getVoucherDate().getTime()
                || b.getOrderno() == null
                || (bankBill.getOrderno() !=null && b.getOrderno() <= bankBill.getOrderno()))
				.mapToDouble(b -> b.getDebit().subtract(b.getCredit()).doubleValue()).sum();
		return accountInit.add(BigDecimal.valueOf(change));
		/*cwz 2017-5-9 禅道2118 银行对账单的余额处理有误  end*/
	}

	/**
	 * 获取银行对账账余额(带筛选过滤）
	 * @param subjectId 科目ID
	 * @param bankBill 银行单
	 */
	private BigDecimal getStatementAmount(String subjectId, BankBill bankBill){
		//银行对账单期初余额
		BankInit bankInit = bankInitService.getRecord(SecurityUtil.getFiscalAccountId(), subjectId);
		BigDecimal statementInit = (bankInit == null) ? BigDecimal.ZERO : bankInit.getStatementInit();

		// 查询该单之前的订单
		List<BankBill> bills = getBillFilterBy(SecurityUtil.getFiscalAccountId(),
				subjectId, BankBill.TYPE_STATEMENT, bankBill.getVoucherDate());

		/*cwz 2017-5-9 禅道2118 银行对账单的余额处理有误  start*/
		/*银行日记账=银行初始化对账单余额-日记账借方金额+日记账贷方金额；*/
        //计算金额
        Double change = bills.stream().filter(b -> b.getVoucherDate().getTime() < bankBill.getVoucherDate().getTime()
                || b.getOrderno() == null
                || (bankBill.getOrderno() !=null && b.getOrderno() <= bankBill.getOrderno()))
                .mapToDouble(b -> b.getDebit().add(b.getCredit()).doubleValue()).sum();
        return statementInit.subtract(BigDecimal.valueOf(change));
//  		BigDecimal debit = bankBill.getDebit();//借方金额
//		BigDecimal credit = bankBill.getCredit();//贷方金额
//        return statementInit.subtract(debit).add(credit);
		/*cwz 2017-5-9 禅道2118 银行对账单的余额处理有误  end */
	}
	
	/**
	 * 获取现金日记账余额
	 * @param subjectId 科目ID
	 * @param bankBill 银行单
	 */
	private BigDecimal getCashAmount(String subjectId, BankBill bankBill){
		//银行对账单期初余额
		BankInit bankInit = bankInitService.getRecord(SecurityUtil.getFiscalAccountId(), subjectId);
		BigDecimal accountBalance = (bankInit == null) ? BigDecimal.ZERO : bankInit.getAccountBalance();
		// 查询该单之前的订单
		List<BankBill> bills = getBillFilterBy(SecurityUtil.getFiscalAccountId(),
				subjectId, BankBill.TYPE_CASH, bankBill.getVoucherDate());

        //计算金额
        Double change = bills.stream().filter(b -> b.getVoucherDate().getTime() < bankBill.getVoucherDate().getTime()
                || b.getOrderno() == null
                || (bankBill.getOrderno() !=null && b.getOrderno() <= bankBill.getOrderno()))
                .mapToDouble(b -> b.getDebit().subtract(b.getCredit()).doubleValue()).sum();

        return accountBalance.add(BigDecimal.valueOf(change));
//		BigDecimal debit = bankBill.getDebit();//借方金额
//		BigDecimal credit = bankBill.getCredit();//贷方金额
//		/*计算公式为：期初+借方-贷方*/
//		return accountBalance.add(debit).subtract(credit);
	}
    /**
     * 检查当天顺序号是否重复
     */
    private int checkOrderNoExist(String subjectId, BankBillVo vo,Integer type) {
        // 查询该单之前的订单
        Date voVoucherDate = DateUtilTools.string2Date(vo.getVoucherDate());
        List<BankBill> bills = getBillFilterBy(SecurityUtil.getFiscalAccountId(),
                subjectId, type, voVoucherDate);

        if (bills == null || bills.isEmpty()) return 1;

        //只取同一天的单不包括自己
        bills.removeIf((b) -> b.getVoucherDate().getTime() != voVoucherDate.getTime() ||
                        (vo.getFid() != null && !"".equals(vo.getFid()) && b.getFid().equals(vo.getFid())));

        Iterator<BankBill> it2 = bills.iterator();
        while (it2.hasNext()) {
            BankBill b = it2.next();
            if (vo.getOrderno() != null && vo.getOrderno().intValue() == (b.getOrderno() == null?0:b.getOrderno())) {
                return  -1;
            }
        }
        return 1;
    }

  /**
   * 获取最大当天顺序号
   * @param subjectId 科目id
   * @param vo		出纳-银行单据记录
   * @param type	 类型：1-期初企业未到账；2-期初银行未到账；3-银行日记账；4-银行对账单;5-现金日记账
   * @return
   * cwz 2017-5-18 新增类型参数，按照类型查询
   */
    private int getMaxOrderNo(String subjectId, BankBillVo vo,Integer type) {
        // 查询该单之前的订单
        Date voVoucherDate = DateUtilTools.string2Date(vo.getVoucherDate());
        List<BankBill> bills = getBillFilterBy(SecurityUtil.getFiscalAccountId(),
                subjectId, type, voVoucherDate);
        /* cwz 2017-5-17 2250 收付款管理—生成银行日记帐单—当天顺序号间断了从2~开始，应该是是1~开始*/
        if (bills == null || bills.isEmpty()) return 0;

        //只取同一天的单包括自己
//        Iterator<BankBill> it = bills.iterator();
//        while (it.hasNext()) {
//            if (it.next().getVoucherDate().getTime() != voVoucherDate.getTime()) it.remove();
//        }
        bills.removeIf(b -> b.getVoucherDate().getTime() != voVoucherDate.getTime());

        //新增的时候返回可以自动填写的最大值，检查的时候重复了则返回-1
        /* cwz 2017-5-17 2250 收付款管理—生成银行日记帐单—当天顺序号间断了从2~开始，应该是是1~开始*/
        int maxOrderNo = 0;
        Iterator<BankBill> it2 = bills.iterator();
        while (it2.hasNext()) {
            BankBill b = it2.next();
            if ((b.getOrderno() == null ? 0 : b.getOrderno()) > maxOrderNo) maxOrderNo = b.getOrderno();
        }

        return maxOrderNo;
    }
	class MyCompartor implements Comparator<BankBillVo>
	{
	     @Override
	     public int compare(BankBillVo o1, BankBillVo o2)
	    {
	           return o1.getVoucherDate().compareTo(o1.getVoucherDate());
	    }
	}
	class MyCompartor2 implements Comparator<BankBillVo>
	{
		@Override
		public int compare(BankBillVo o1, BankBillVo o2)
		{
			return o1.getOrderno().compareTo(o1.getOrderno());
		}
	}
}
