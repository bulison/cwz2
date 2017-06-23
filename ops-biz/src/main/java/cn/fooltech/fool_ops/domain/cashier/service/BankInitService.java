package cn.fooltech.fool_ops.domain.cashier.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import cn.fooltech.fool_ops.validator.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.cashier.entity.BankBill;
import cn.fooltech.fool_ops.domain.cashier.entity.BankInit;
import cn.fooltech.fool_ops.domain.cashier.repository.BankInitRepository;
import cn.fooltech.fool_ops.domain.cashier.vo.BankInitVo;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalAccountingSubjectService;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.VoFactory;

/**
 * <p>出纳-初始银行设置网页服务类</p>
 * @author xjh
 * @version 1.0
 * @date 2015-12-14 11:12:47
 */
@Service
public class BankInitService extends BaseService<BankInit,BankInitVo,String> {
	
	/**
	 * 出纳-初始银行设置持久类
	 */
	@Autowired
	private BankInitRepository bankInitRepository;
	
	/**
	 * 轧账日期服务类
	 */
	@Autowired
	private BankCheckdateService bankCheckdateService;
	
	/**
	 * 银行单据记录服务类
	 */
	@Autowired
	private BankBillService bankBillService;
	
	/**
	 * 财务-科目服务类
	 */
	@Autowired
	private FiscalAccountingSubjectService subjectService;
	
	
	/**
	 * 查询出纳-初始银行设置列表信息，按照出纳-初始银行设置主键降序排列<br>
	 * 默认为第一页，每页大小默认为10<br>
	 * @param vo
	 */
	public Page<BankInit> query(BankInitVo bankInitVo,PageParamater pageParamater){
		Sort sort  = new Sort(Direction.DESC,"createTime");
		PageRequest request = getPageRequest(pageParamater,sort);
		Page<BankInit> page = bankInitRepository.query(bankInitVo, request);
		return page;
	}
	/**
	 * 删除出纳-初始银行设置<br>
	 */
	public RequestResult delete(String fid){
		String accountId = SecurityUtil.getFiscalAccountId();
		long countStart = bankInitRepository.countStart(accountId);
		if(countStart>0){
			return new RequestResult(RequestResult.RETURN_FAILURE, "出纳系统已启用，不能删除");
		}
		long countCheckDate = bankCheckdateService.countAll(accountId);
		if(countCheckDate>0)return new RequestResult(RequestResult.RETURN_FAILURE, "已结账不能删除");
		
		//删除银行单据记录表中相关记录（科目ID相等的记录）
		BankInit bankInit = bankInitRepository.findOne(fid);
		bankBillService.deleteBySubject(bankInit.getSubject());
		
		bankInitRepository.delete(fid);
		return buildSuccessRequestResult();
	}
	
	/**
	 * 是否允许修改删除
	 * @return
	 */
	public boolean enableEditOrDelete(){
		String accountId = SecurityUtil.getFiscalAccountId();
		long countStart = bankInitRepository.countStart(accountId);
		if(countStart>0){
			return false;
		}
		long countCheckDate = bankCheckdateService.countAll(accountId);
		if(countCheckDate>0)return false;
		return true;
	}
	
	/**
	 * 获取出纳-初始银行设置信息
	 * @param fid 出纳-初始银行设置ID
	 * @return
	 */
	public BankInitVo getByFid(String fid) {
		Assert.notNull(fid);
		return getVo(bankInitRepository.findOne(fid));
	}
	

	/**
	 * 新增/编辑出纳-初始银行设置
	 * @param vo
	 */
	public RequestResult save(BankInitVo vo) {

		String inValid = ValidatorUtils.inValidMsg(vo);
		if(inValid!=null){
			return buildFailRequestResult(inValid);
		}

		BankInit entity = null;
		if(StringUtils.isBlank(vo.getFid())){
			
			entity = VoFactory.createValue(BankInit.class, vo);
			entity.setOrg(SecurityUtil.getCurrentOrg());
			entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
			entity.setCreateTime(new Date());
			entity.setCreator(SecurityUtil.getCurrentUser());
			entity.setDept(SecurityUtil.getCurrentDept());
			
		}else {
			entity = bankInitRepository.findOne(vo.getFid());
			if(!checkUpdateTime(vo.getUpdateTime(), entity.getUpdateTime())){
				return new RequestResult(RequestResult.RETURN_FAILURE, "数据已被修改，请刷新再试");
			}
			
			String accountId = SecurityUtil.getFiscalAccountId();
			long countStart = bankInitRepository.countStart(accountId);
			if(countStart>0){
				return new RequestResult(RequestResult.RETURN_FAILURE, "出纳系统已启用，不能修改");
			}
			long countCheckDate = bankCheckdateService.countAll(accountId);
			if(countCheckDate>0)return new RequestResult(RequestResult.RETURN_FAILURE, "已结账不能修改");
			
			FiscalAccountingSubject subject = entity.getSubject();
			if(subject.getCashSubject()!=null && subject.getCashSubject()==FiscalAccountingSubject.CASH_YES){
				entity.setAccountInit(vo.getAccountInit());
				
				//日记账调整后余额=日记账期初余额+日记账贷方金额-日记账借方金额；
				BigDecimal accountBalance = NumberUtil.add(entity.getAccountInit(), entity.getAccountCredit());
				accountBalance = NumberUtil.subtract(accountBalance, entity.getAccountDebit());
				entity.setAccountBalance(accountBalance);
			}
			
			if(subject.getBankSubject()!=null && subject.getBankSubject()==FiscalAccountingSubject.BANK_YES){
				entity.setAccountInit(vo.getAccountInit());
				
				//日记账调整后余额=日记账期初余额+日记账贷方金额-日记账借方金额；
				BigDecimal accountBalance = NumberUtil.add(entity.getAccountInit(), entity.getAccountCredit());
				accountBalance = NumberUtil.subtract(accountBalance, entity.getAccountDebit());
				entity.setAccountBalance(accountBalance);
				
				entity.setStatementInit(vo.getStatementInit());
				
				//对账单调整后余额=对账单期初余额+对账单借方金额-对账单贷方金额；
				BigDecimal statementBalance = NumberUtil.add(entity.getStatementInit(), entity.getStatementDebit());
				statementBalance = NumberUtil.subtract(statementBalance, entity.getStatementCredit());
				
				entity.setStatementBalance(statementBalance);
			}
		}
		
		bankInitRepository.save(entity);
		
		return buildSuccessRequestResult();
	}
	
	/**
	 * 把期初银行设置表中的记录状态改为已启用
	 * @return
	 */
	public RequestResult updateUse(){
		List<BankInit> bankInits = bankInitRepository.getAll(SecurityUtil.getFiscalAccountId());
		
		for(BankInit bankInit:bankInits){
			String name = bankInit.getSubject().getName();
			FiscalAccountingSubject subject = bankInit.getSubject();
			if(subject!=null && subject.getBankSubject()!=null && 
					subject.getBankSubject()==FiscalAccountingSubject.BANK_YES){
				if(!bankInit.getAccountBalance().equals(bankInit.getStatementBalance())){
					return new RequestResult(RequestResult.RETURN_FAILURE, "["+name+"]金额不平衡，不能启用");
				}
			}
		}
		
		for(BankInit bankInit:bankInits){
			bankInit.setStart(BankInit.START);
			bankInitRepository.save(bankInit);
		}
		
		return buildSuccessRequestResult();
	}
	
	/**
	 * 是否能启用
	 * @return
	 */
	public boolean enableUse(){
		List<BankInit> bankInits = bankInitRepository.getAll(SecurityUtil.getFiscalAccountId());
		
		for(BankInit bankInit:bankInits){
			FiscalAccountingSubject subject = bankInit.getSubject();
			if(subject!=null && subject.getBankSubject()!=null && 
					subject.getBankSubject()==FiscalAccountingSubject.BANK_YES){
				if(!bankInit.getAccountBalance().equals(bankInit.getStatementBalance())){
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 把期初银行设置表中的记录状态改为未启用
	 * @return
	 */
	public RequestResult updateUnUse(){
		
		String accountId = SecurityUtil.getFiscalAccountId();
		long countCheckDate = bankCheckdateService.countAll(accountId);
		if(countCheckDate>0)return new RequestResult(RequestResult.RETURN_FAILURE, "已结账不能反启用");
		
		List<BankInit> bankInits = bankInitRepository.getAll(accountId);
		
		for(BankInit bankInit:bankInits){
			bankInit.setStart(BankInit.UN_START);
			bankInitRepository.save(bankInit);
		}
		
		return buildSuccessRequestResult();
	}
	
	/**
	 * 是否能反启用
	 * @return
	 */
	public boolean enableUnUse(){
		String accountId = SecurityUtil.getFiscalAccountId();
		long countCheckDate = bankCheckdateService.countAll(accountId);
		if(countCheckDate>0)return false;
		return true;
	}
	
	/**
	 * 引入：把科目表中的银行科目（FBANK_SUBJECT和FCASH_SUBJECT）=1的科目写入到出纳初始数据表中
	 * @return
	 */
	public RequestResult saveByImport(){
		FiscalAccount account = SecurityUtil.getFiscalAccount();
		String accountId = account.getFid();
		
		long countStart = bankInitRepository.countStart(accountId);
		if(countStart>0){
			return new RequestResult(RequestResult.RETURN_FAILURE, "出纳系统已启用，不能引入");
		}
		
		List<FiscalAccountingSubject> subjects = subjectService.getBankCashSubjects(accountId);
		
		User creator = SecurityUtil.getCurrentUser();
		Organization org = SecurityUtil.getCurrentOrg();
		
		for(FiscalAccountingSubject subject:subjects){
			
			if(bankInitRepository.countBySubjectId(subject.getFid())>0){
				continue;
			}
			
			BankInit bankInit = new BankInit();
			bankInit.setCreateTime(new Date());
			bankInit.setCreator(creator);
			bankInit.setOrg(org);
			bankInit.setFiscalAccount(account);
			bankInit.setSubject(subject);
			bankInit.setDept(SecurityUtil.getCurrentDept());
			bankInitRepository.save(bankInit);
		}
		return buildSuccessRequestResult();
	}



	  


	/**
	 * 添加、修改、删除每一条BankBill记录时都要维护出纳初始数据表和数据，
	 * 更新tbd_bank_init的日记账借方金额和日记账贷方金额及日记账调整后余额（按科目来比较更新那条记录）；
	 * 更新tbd_bank_init的对账单借方金额和对账单贷方金额及对账单调整后余额（按科目来比较更新那条记录）；
	 * 日记账调整后余额=日记账期初余额+日记账贷方金额-日记账借方金额
	 * 对账单调整后余额=对账单期初余额+对账单借方金额-对账单贷方金额
	 * @param deltaCredit
	 * @param deltaDebit
	 */
	public void updateAmountBySubject(BigDecimal deltaCredit,
			BigDecimal deltaDebit, BankBill bankBill) {
		String accountId = SecurityUtil.getFiscalAccountId();
		List<BankInit> list = bankInitRepository.getBySubjectRecord(accountId, bankBill.getSubject().getFid());
		for(BankInit entity:list){
			
			if(bankBill.getType()==BankBill.TYPE_COMPANY){
				
				BigDecimal accountCredit = NumberUtil.add(entity.getAccountCredit(), deltaCredit);
				BigDecimal accountDebit = NumberUtil.add(entity.getAccountDebit(), deltaDebit);
				
				entity.setAccountCredit(accountCredit);
				entity.setAccountDebit(accountDebit);
				
				//日记账调整后余额=日记账期初余额+日记账贷方金额-日记账借方金额；
				BigDecimal accountBalance = NumberUtil.add(entity.getAccountInit(), entity.getAccountCredit());
				accountBalance = NumberUtil.subtract(accountBalance, entity.getAccountDebit());
				entity.setAccountBalance(accountBalance);
			}else if(bankBill.getType()==BankBill.TYPE_BANK){
				
				BigDecimal statementCredit = NumberUtil.add(entity.getStatementCredit(), deltaCredit);
				BigDecimal statementDebit = NumberUtil.add(entity.getStatementDebit(), deltaDebit);
				
				entity.setStatementCredit(statementCredit);
				entity.setStatementDebit(statementDebit);
				
				//对账单调整后余额=对账单期初余额+对账单借方金额-对账单贷方金额；
				BigDecimal statementBalance = NumberUtil.add(entity.getStatementInit(), entity.getStatementDebit());
				statementBalance = NumberUtil.subtract(statementBalance, entity.getStatementCredit());
				entity.setStatementBalance(statementBalance);
			}
			
			save(entity);
		}
	}
	@Override
	public CrudRepository<BankInit, String> getRepository() {
		return bankInitRepository;
	}
	/**
	 * 单个出纳-初始银行设置实体转换为vo
	 * @param entity
	 * @return
	 */
	@Override
	public BankInitVo getVo(BankInit entity){
		if(entity == null)
			return null;
		BankInitVo vo = new BankInitVo();
		vo.setAccountInit(entity.getAccountInit());
		vo.setStatementInit(entity.getStatementInit());
		vo.setAccountDebit(entity.getAccountDebit());
		vo.setAccountCredit(entity.getAccountCredit());
		vo.setAccountBalance(entity.getAccountBalance());
		vo.setStatementDebit(entity.getStatementDebit());
		vo.setStatementCredit(entity.getStatementCredit());
		vo.setStatementBalance(entity.getStatementBalance());
		vo.setStart(entity.getStart());
		vo.setCreateTime(DateUtilTools.date2String(entity.getCreateTime(), DATE_TIME));
		vo.setUpdateTime(DateUtilTools.date2String(entity.getUpdateTime(), DATE_TIME));
		vo.setFid(entity.getFid());
		
		FiscalAccountingSubject subject = entity.getSubject();
		if(subject!=null){
			vo.setSubjectId(subject.getFid());
			vo.setSubjectName(subject.getName());
			vo.setSubjectCode(subject.getCode());
			vo.setBankSubject(subject.getBankSubject());
			vo.setCashSubject(subject.getCashSubject());
		}
		
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
		
		return vo;
	}
	/**
	 * 获取记录
	 * @param fiscalAccountId 财务账套ID
	 * @param subjectId 科目ID
	 * @return
	 * @author rqh
	 */
	public BankInit getRecord(String fiscalAccountId, String subjectId){
		return bankInitRepository.getRecord(fiscalAccountId, subjectId);
	}
	/**
	 * 检查修改时间戳；相同返回true，否则返回false
	 * @param updateTimeStr
	 * @param updateTime
	 * @return
	 */
	public boolean checkUpdateTime(String updateTimeStr, Date updateTime){
		String updateTimeStr2 = DateUtilTools.date2String(updateTime, 
				DateUtilTools.DATE_PATTERN_YYYY_MM_DDHHMMSS);
		if(updateTimeStr.equals(updateTimeStr2))return true;
		return false;
	}
	
	/**
	 * 根据科目ID统计
	 * @return
	 */
	public long countBySubjectId(String subjectId) {
		return bankInitRepository.countBySubjectId(subjectId);
	}
	
}
