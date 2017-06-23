package cn.fooltech.fool_ops.domain.cashier.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.cashier.entity.BankBill;
import cn.fooltech.fool_ops.domain.cashier.entity.BankInit;
import cn.fooltech.fool_ops.domain.cashier.vo.BalanceAdjustVo;
import cn.fooltech.fool_ops.domain.cashier.vo.BankBillSimpleVo;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;



/**
 * <p>余额调节网页服务类</p>
 * @author rqh
 * @version 1.0
 * @date 2016年1月6日
 */
@Service
public class BalanceAdjustService extends BaseService<BankBill, BalanceAdjustVo, String>{
	
	/**
	 * 初始银行设置服务类
	 */
	@Autowired
	private BankInitService bankInitService;
	
	/**
	 * 银行单据记录服务类
	 */
	@Autowired
	private BankBillService bankBillService;
	
	/**
	 * 标识- 借方金额
	 */
	private static final int FLAG_DEBIT = 1;
	
	/**
	 * 标识- 贷方金额
	 */
	private static final int FLAG_CREDIT = 2;
	
	/**
	 * 查询
	 * @param 科目ID
	 * @return
	 */
	public JSONObject query(String subjectId){
		BalanceAdjustVo leftFirst = getLeftFirst(subjectId);
		BalanceAdjustVo leftSecond = getLeftSecond(subjectId);
		BalanceAdjustVo rightFirst = getRightFirst(subjectId);
		BalanceAdjustVo rightSecond = getRightSecond(subjectId);
		
		//银行日记账余额
		BigDecimal journalAmount = NumberUtil.toBigDeciaml(leftFirst.getJournalAmount());
		
		//银行已收企业未收金额
		BigDecimal enterpriseUnReceiveAmount = NumberUtil.toBigDeciaml(leftFirst.getEnterpriseUnReceiveAmount());
		
		//银行已付企业未付金额
		BigDecimal enterpriseUnPayAmount = NumberUtil.toBigDeciaml(leftSecond.getEnterpriseUnPayAmount());
		
		//调节后余额(企业)
		BigDecimal adjustedOfEnterprise = journalAmount.add(enterpriseUnReceiveAmount).subtract(enterpriseUnPayAmount);
		
		//银行对账单余额
		BigDecimal statementAmount = NumberUtil.toBigDeciaml(rightFirst.getStatementAmount());
		
		//企业已收银行未收金额
		BigDecimal bankUnReceiveAmount = NumberUtil.toBigDeciaml(rightFirst.getBankUnReceiveAmount());
		
		//企业已付银行未付金额
		BigDecimal bankUnPayAmount = NumberUtil.toBigDeciaml(rightSecond.getBankUnPayAmount());
		
		//调节后余额(银行)
		BigDecimal adjustedOfBank = statementAmount.add(bankUnReceiveAmount).subtract(bankUnPayAmount); 
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.accumulate("adjustedOfBank", NumberUtil.bigDecimalToStr(adjustedOfBank));
		jsonObject.accumulate("adjustedOfEnterprise", NumberUtil.bigDecimalToStr(adjustedOfEnterprise));
		jsonObject.accumulate("leftFirst", leftFirst);
		jsonObject.accumulate("leftSecond", leftSecond);
		jsonObject.accumulate("rightFirst", rightFirst);
		jsonObject.accumulate("rightSecond", rightSecond);
		return jsonObject;
	}
	
	/**
	 * 左一
	 * @param subjectId
	 * @return
	 */
	private BalanceAdjustVo getLeftFirst(String subjectId){
		//银行日记账余额
		BigDecimal journalAmount = getJournalAmount(subjectId);
		List<Integer> types = Lists.newArrayList(BankBill.TYPE_COMPANY, BankBill.TYPE_STATEMENT);
		//银行已收企业未收金额
		BigDecimal enterpriseUnReceiveAmount = bankBillService.countCreditAmount(SecurityUtil.getFiscalAccountId(), subjectId, 
				BankBill.UN_CHECKED,types);
		
		//单据
		List<BankBill> bills = bankBillService.getBillds(SecurityUtil.getFiscalAccountId(), subjectId, BankBill.UN_CHECKED,
				types);
		
		BalanceAdjustVo result = new BalanceAdjustVo();
		result.setJournalAmount(NumberUtil.bigDecimalToStr(journalAmount));
		result.setEnterpriseUnReceiveAmount(NumberUtil.bigDecimalToStr(enterpriseUnReceiveAmount));
		result.setInfos(getBillVos(bills, FLAG_CREDIT));
		return result;
	}
	
	/**
	 * 左二
	 * @param subjectId
	 * @return
	 */
	private BalanceAdjustVo getLeftSecond(String subjectId){
		List<Integer> types = Lists.newArrayList(BankBill.TYPE_COMPANY, BankBill.TYPE_STATEMENT);
//		new Integer[]{BankBill.TYPE_COMPANY, BankBill.TYPE_STATEMENT}
		//银行已付企业未付金额
		BigDecimal enterpriseUnPayAmount = bankBillService.countDebitAmount(SecurityUtil.getFiscalAccountId(), subjectId, 
				BankBill.UN_CHECKED, types);
		//单据
		List<BankBill> bills = bankBillService.getBillds(SecurityUtil.getFiscalAccountId(), subjectId, BankBill.UN_CHECKED,
				types);
		
		BalanceAdjustVo result = new BalanceAdjustVo();
		result.setEnterpriseUnPayAmount(NumberUtil.bigDecimalToStr(enterpriseUnPayAmount));
		result.setInfos(getBillVos(bills, FLAG_DEBIT));
		return result;
	}
	
	/**
	 * 右一
	 * @param subjectId
	 * @return
	 */
	private BalanceAdjustVo getRightFirst(String subjectId){
		//银行对账单余额
		BigDecimal statementAmount = getStatementAmount(subjectId);
		List<Integer> types = Lists.newArrayList(BankBill.TYPE_BANK, BankBill.TYPE_CREDIT);
		//企业已收银行未收金额
		BigDecimal bankUnReceiveAmount = bankBillService.countDebitAmount(SecurityUtil.getFiscalAccountId(), subjectId, 
				BankBill.UN_CHECKED,types);
		
		//单据
		List<BankBill> list = bankBillService.getBillds(SecurityUtil.getFiscalAccountId(), subjectId, 
				BankBill.UN_CHECKED, types);
		
		BalanceAdjustVo result = new BalanceAdjustVo();
		result.setStatementAmount(NumberUtil.bigDecimalToStr(statementAmount));
		result.setBankUnReceiveAmount(NumberUtil.bigDecimalToStr(bankUnReceiveAmount));
		result.setInfos(getBillVos(list, FLAG_DEBIT));
		return result;
	}
	
	/**
	 * 右二
	 * @param subjectId
	 * @return
	 */
	private BalanceAdjustVo getRightSecond(String subjectId){
		List<Integer> types = Lists.newArrayList(BankBill.TYPE_BANK, BankBill.TYPE_CREDIT);
		//企业已付银行未付金额
		BigDecimal bankUnPayAmount = bankBillService.countCreditAmount(SecurityUtil.getFiscalAccountId(), subjectId, 
				BankBill.UN_CHECKED,types);
		
		//单据
		List<BankBill> list = bankBillService.getBillds(SecurityUtil.getFiscalAccountId(), subjectId, 
				BankBill.UN_CHECKED,types);
		
		BalanceAdjustVo result = new BalanceAdjustVo();
		result.setBankUnPayAmount(NumberUtil.bigDecimalToStr(bankUnPayAmount));
		result.setInfos(getBillVos(list, FLAG_CREDIT));
		return result;
	}
	
	/**
	 * 获取银行日记账余额
	 * @param subjectId 科目ID
	 * @return
	 */
	private BigDecimal getJournalAmount(String subjectId){
		//银行日记账期初余额
		BigDecimal accountInit = BigDecimal.ZERO; 
		BankInit bankInit = bankInitService.getRecord(SecurityUtil.getFiscalAccountId(), subjectId);
		if(bankInit != null){accountInit = bankInit.getAccountInit();}
		List<Integer> types = Lists.newArrayList(BankBill.TYPE_COMPANY, BankBill.TYPE_STATEMENT);
		//借方金额
		BigDecimal debitAmount = bankBillService.countDebitAmount(SecurityUtil.getFiscalAccountId(), subjectId,
				BankBill.CHECKED,types);
		//贷方金额
		BigDecimal creditAmount = bankBillService.countCreditAmount(SecurityUtil.getFiscalAccountId(), subjectId, 
				BankBill.CHECKED,types);
		return accountInit.add(debitAmount).subtract(creditAmount);
	}
	
	/**
	 * 获取银行对账单余额
	 * @return
	 */
	private BigDecimal getStatementAmount(String subjectId){
		//银行对账单期初余额
		BigDecimal statementInit = BigDecimal.ZERO;
		BankInit bankInit = bankInitService.getRecord(SecurityUtil.getFiscalAccountId(), subjectId);
		if(bankInit != null){statementInit = bankInit.getStatementInit();}
		List<Integer> types = Lists.newArrayList(BankBill.TYPE_BANK, BankBill.TYPE_CREDIT);
		//贷方金额
		BigDecimal creditAmount = bankBillService.countCreditAmount(SecurityUtil.getFiscalAccountId(), subjectId, 
				BankBill.CHECKED, types);
		
		//借方金额
		BigDecimal debitAmount = bankBillService.countDebitAmount(SecurityUtil.getFiscalAccountId(), subjectId, 
				BankBill.CHECKED, types);
		return statementInit.add(creditAmount).subtract(debitAmount);
	}
	
	/**
	 * 获取单据信息
	 * @param bills 单据
	 * @param flag 标识 
	 * @return
	 */
	private List<BankBillSimpleVo> getBillVos(List<BankBill> bills, int flag){
		List<BankBillSimpleVo> list = new ArrayList<BankBillSimpleVo>();
		for(BankBill bill : bills){
			BankBillSimpleVo vo = null;
			if(flag == FLAG_DEBIT){
				vo = new BankBillSimpleVo(bill.getVoucherDate(), bill.getDebit());
			}
			else{
				vo = new BankBillSimpleVo(bill.getVoucherDate(), bill.getCredit());
			}
			list.add(vo);
		}
		return list;
	}

	@Override
	public BalanceAdjustVo getVo(BankBill entity) {
		return null;
	}

	@Override
	public CrudRepository<BankBill, String> getRepository() {
		return null;
	}
	
}
