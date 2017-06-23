package cn.fooltech.fool_ops.domain.fiscal.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalPeriod;
import cn.fooltech.fool_ops.domain.fiscal.repository.FiscalPeriodRepository;
import cn.fooltech.fool_ops.domain.fiscal.vo.FiscalPeriodVo;
import cn.fooltech.fool_ops.domain.period.entity.StockPeriod;
import cn.fooltech.fool_ops.domain.period.service.StockPeriodService;
import cn.fooltech.fool_ops.domain.voucher.repository.VoucherRepository;
import cn.fooltech.fool_ops.domain.voucher.service.VoucherService;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.ErrorCode;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.VoFactory;
import cn.fooltech.fool_ops.validator.ValidatorUtils;

/**
 * <p>财务参数设置服务类</p>
 * @author xjh
 */
@Service
public class FiscalPeriodService extends BaseService<FiscalPeriod, FiscalPeriodVo, String>{
	
	private static final Logger logger = LoggerFactory.getLogger(FiscalPeriodService.class);
	
	@Autowired
	private FiscalPeriodRepository periodRepo;
	
	@Autowired
	private VoucherRepository voucherRepo;
	
	@Autowired
	private VoucherService voucherService;
	
	@Autowired
	private StockPeriodService stockPeriodService;
	
	@Autowired
	private FiscalInitBalanceService initService;

	@Override
	public FiscalPeriodVo getVo(FiscalPeriod entity) {
		if (entity == null)
			return null;
		FiscalPeriodVo periodVo = VoFactory.createValue(FiscalPeriodVo.class,
				entity);
		periodVo.setStartDate(DateUtilTools.date2String(entity.getStartDate()));
		periodVo.setEndDate(DateUtilTools.date2String(entity.getEndDate()));
		periodVo.setUpdateTime(DateUtilTools.time2String(entity.getUpdateTime()));

		FiscalAccount account = entity.getFiscalAccount();
		periodVo.setFiscalAccountId(account.getFid());
		periodVo.setFiscalAccountName(account.getName());
		
		//仓储会计期间
		StockPeriod stockPeriod = stockPeriodService.findOne(entity.getFid());
		if(stockPeriod == null){
			throw new RuntimeException("仓储会计期间与财务会计期间不同步!");
		}
		else{
			periodVo.setStockCheckoutStatus(stockPeriod.getCheckoutStatus());
		}
		
		return periodVo;
	}

	@Override
	public CrudRepository<FiscalPeriod, String> getRepository() {
		return periodRepo;
	}
	
	/**
	 * 保存会计期间
	 * 
	 * @param vo
	 */
	@Transactional
	public RequestResult saveOneYear(FiscalPeriodVo vo) throws Exception {
		Integer day = vo.getNumber();
		FiscalPeriod lastPeriod = getLastPeriod();
		
		Date lastDate = null;
		if(lastPeriod!=null){
			lastDate = lastPeriod.getEndDate();
		}
		
		for (int j = 0; j < 12; j++) {
			Date startDate = getStartDate(lastDate, day);
			Date endDate = getEndDate(startDate, day);
			
			FiscalPeriod entity = new FiscalPeriod();
			entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
			entity.setOrg(SecurityUtil.getCurrentOrg());
			entity.setDept(SecurityUtil.getCurrentDept());
			entity.setCreator(SecurityUtil.getCurrentUser());
			entity.setCheckoutStatus(FiscalPeriod.UN_USED);
			entity.setDescription("");
			entity.setStartDate(startDate);
			entity.setEndDate(endDate);
			entity.setPeriod(getPeriodName(startDate));
			entity.setUpdateTime(new Date());
			
			//新增财务会计期间
			periodRepo.save(entity);
			
			//新增仓库会计期间
			StockPeriod stockPeriod = stockPeriodService.clonePeroid(entity);
			stockPeriodService.save(stockPeriod);
			
			lastDate = endDate;
		}
		return buildSuccessRequestResult();
	}
	
	/**
	 * 删除财务会计期间<br>
	 */
	@Transactional
	public RequestResult deleteById(String id) {
		FiscalPeriod period = periodRepo.findOne(id);
		if(period==null) {
			return buildFailRequestResult("数据不存在，可能已被删除，请刷新再试");
		}
			
		FiscalPeriod lastPeriod = getLastPeriod();
		if (period.getEndDate().compareTo(lastPeriod.getEndDate()) != 0) {
			return buildFailRequestResult("该会计期间不是最后一条，不能删除！");
		}
		if (period.getCheckoutStatus() != FiscalPeriod.UN_USED) {
			return buildFailRequestResult("该会计期间非未启用状态，不能删除！");
		}

		// 查找凭证是否有关联 有关联不能删除
		Long count = voucherRepo.countByPeriodId(id);
		if(count>0){
			return buildFailRequestResult("该会计期间已被关联到凭证，不能删除！");
		}
		
		//删除仓储会计期间
		RequestResult deleteResult = stockPeriodService.delete(id);
		if(!deleteResult.isSuccess()){
			return deleteResult;
		}
		
		periodRepo.delete(id);
		return buildSuccessRequestResult();
	}
	
	/**
	 * 获取最后一个会计期间
	 * @return
	 */
    public FiscalPeriod getLastPeriod(){
    	String accId = SecurityUtil.getFiscalAccountId();
		Sort sort = new Sort(Direction.DESC, "endDate");
		return periodRepo.findTopPeriodByAccId(accId, sort);
    }
    
    /**
	 * 获取最后一个会计期间
	 * @return
	 */
    public FiscalPeriod getFirstPeriod(){
    	String accId = SecurityUtil.getFiscalAccountId();
		Sort sort = new Sort(Direction.ASC, "endDate");
		return periodRepo.findTopPeriodByAccId(accId, sort);
    }
    
    /**
	 * 获得第一个未结账的会计期间
	 * @return
	 */
	public FiscalPeriod getFirstNotCheck() {
		String accId = SecurityUtil.getFiscalAccountId();
		Sort sort = new Sort(Direction.ASC, "startDate");
		return periodRepo.findTopPeriodByUnCheck(accId, sort);
	}
    
    /**
     * 获取前面的会计期间获取开始日期
     * @param pre
     * @param day
     * @return
     */
    private Date getStartDate(Date pre, int day){
    	if(pre!=null){
    		Date change = DateUtilTools.changeDateTime(pre, 0, 1, 0, 0, 0);
    		return change;
    	}else{
    		
    		Calendar calendar = Calendar.getInstance();
    		if(day>=20){
    			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)-1);
    		}else{
    			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
    		}
			calendar.set(Calendar.DAY_OF_MONTH, day);
			calendar.set(Calendar.HOUR, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
    		return calendar.getTime();
    	}
    }
    
    /**
     * 根据开始日期获取结束日期
     * @param start
     * @return
     */
    private Date getEndDate(Date start, int day){
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(start);
    	
    	int startDay = calendar.get(Calendar.DAY_OF_MONTH);
    	if(day<=startDay){
    		//匹配上输入的日
    		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)+1);
    	}
    	
		calendar.set(Calendar.DAY_OF_MONTH, day-1);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
    }
    
    /**
     * 获得灰机期间的名称
     * @param date
     * @return
     */
    private String getPeriodName(Date date){
    	String name = DateUtilTools.date2String(date, DateUtilTools.DATE_PATTERN_YYYYMM);
    	String temName = name;
    	String accId = SecurityUtil.getFiscalAccountId();
    	Long count = periodRepo.countByName(temName, accId);
    	if(count==null)count = 0L;
    	int buf = 2;
    	while(count>0){
    		temName =  (name+"_"+buf);
    		count = periodRepo.countByName(temName, accId);
    		if(count==null)count = 0L;
    		buf++;
    	}
    	return temName;
    }
    
    /**
	 * 查询财务会计期间列表信息，按照财务会计期间主键降序排列<br>
	 * 默认为第一页，每页大小默认为10<br>
	 * 
	 */
	public Page<FiscalPeriodVo> query(FiscalPeriodVo fiscalPeriodVo,
			PageParamater pageParamater) {

		Sort sort = new Sort(Direction.ASC, "startDate");
		PageRequest pageRequest = getPageRequest(pageParamater, sort);
		String accId = SecurityUtil.getFiscalAccountId();
		
		return getPageVos(periodRepo.findPageByAccountId(accId, pageRequest), pageRequest);
	}
	
	/**
	 * 根据日期获取会计期间
	 * @param date
	 * @return
	 */
	public FiscalPeriodVo getPeriodByDate(Date date){
		String accId = SecurityUtil.getFiscalAccountId();
		return getVo(periodRepo.findTopByDate(accId, date));
	}
	
	/**
	 * 新增/编辑财务会计期间
	 * 
	 * @param vo
	 */
	@Transactional
	public RequestResult save(FiscalPeriodVo vo) {
		String inValid = ValidatorUtils.inValidMsg(vo);
		if(inValid!=null){
			return buildFailRequestResult(inValid);
		}

		Date startDate = DateUtilTools.string2Date(vo.getStartDate());
		Date endDate = DateUtilTools.string2Date(vo.getEndDate());
		String periodName = vo.getPeriod();
		String description = vo.getDescription();
		
		if (startDate.compareTo(endDate)>=0) {
			return buildFailRequestResult("结束时间必须要大于开始时间！");
		}
		
		FiscalAccount acc = SecurityUtil.getFiscalAccount();
		
		Long count = periodRepo.countByPeriod(startDate, endDate, acc.getFid(), vo.getFid());
		if (count!=null && count>0) {
			return buildFailRequestResult("会计期间时间重复！");
		}
		if (isNameExist(vo.getPeriod(), acc.getFid(), vo.getFid())) {
			return buildFailRequestResult("会计期间名称重复！");
		}

		FiscalPeriod period = null;
		if (StringUtils.isNotBlank(vo.getFid())) {
			period = periodRepo.findOne(vo.getFid());
			
			if(period==null){
				return buildFailRequestResult("数据不存在，可能已被删除，请刷新再试");
			}
			
			if(period.getCheckoutStatus()!=FiscalPeriod.UN_USED){
				return buildFailRequestResult("数据已启用或已结账不能修改");
			}
			
			if (!checkDataRealTime(vo, period)) {
				return buildFailRequestResult("已被其他用户修改，请刷新再试");
			}
			period.setEndDate(endDate);
			period.setStartDate(startDate);
			period.setPeriod(periodName);
			period.setDescription(description);
			period.setUpdateTime(new Date());
			
			//编辑财务会计期间
			periodRepo.save(period);
			//编辑仓库会计期间
			StockPeriod stockPeriod = stockPeriodService.clonePeroid(period);
			stockPeriodService.save(stockPeriod);
		} else {
			period = new FiscalPeriod();
			period.setCheckoutStatus(FiscalPeriod.UN_USED);
			period.setFiscalAccount(acc);
			period.setDept(SecurityUtil.getCurrentDept());
			period.setCreator(SecurityUtil.getCurrentUser());
			period.setUpdateTime(new Date());
			period.setOrg(SecurityUtil.getCurrentOrg());
			
			period.setEndDate(endDate);
			period.setStartDate(startDate);
			period.setPeriod(periodName);
			period.setDescription(description);
			
			//新增财务会计期间
			periodRepo.save(period);
			//新增仓库会计期间
			StockPeriod stockPeriod = stockPeriodService.clonePeroid(period);
			stockPeriodService.save(stockPeriod);
		}

		return buildSuccessRequestResult();
	}
	
	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @param accId
	 * @param excludeId
	 * @return
	 */
	public boolean isPeriodDuplicate(Date startDate, Date endDate, String accId, String excludeId){
		Long count = periodRepo.countByPeriod(startDate, endDate, accId, excludeId);
		if(count!=null && count>0){
			return true;
		}
		return false;
	}
	
	/**
	 * 判断名称是否存在
	 * @param name
	 * @param accId
	 * @param excludeId
	 * @return
	 */
	public boolean isNameExist(String name, String accId, String excludeId){
		Long count = null;
		if(Strings.isNullOrEmpty(excludeId)){
			count = periodRepo.countByName(name, accId);
		}else{
			count = periodRepo.countByName(name, accId, excludeId);
		}
		if(count!=null && count>0){
			return true;
		}
		return false;
	}


	/**
	 * 更新操作时，校验数据的实时性
	 * 
	 * @param vo
	 *            主键、更新时间
	 * @return true 有效 false 无效
	 */
	private boolean checkDataRealTime(FiscalPeriodVo vo, FiscalPeriod oldPeriod) {
		String updateTime = DateUtilTools.date2String(
				oldPeriod.getUpdateTime(), DATE_TIME);
		return updateTime.equals(vo.getUpdateTime());
	}
	
	/**
	 * 标识最后一个会计期间
	 * @param list
	 */
	public void setLastPeriod(List<FiscalPeriodVo> list, FiscalPeriod lastPeriod){
		if(lastPeriod==null)return;
		ListIterator<FiscalPeriodVo> it = list.listIterator();
		while(it.hasNext()){
			FiscalPeriodVo periodVo = it.next();
            if(periodVo.getFid().equals(lastPeriod.getFid())){
            	periodVo.setIsLast(FiscalPeriodVo.LAST);
			}
		}
	}
	
	/**
	 * 获取所有会计期间
	 * @return
	 */
	public List<FiscalPeriodVo> getAllPeriod(){
		String accId = SecurityUtil.getFiscalAccountId();
		return getVos(periodRepo.findByAccountId(accId));
	}
	
	/**
	 * 获取所有会计期间
	 * @return
	 */
	public List<FiscalPeriodVo> getAllUsedPeriod(){
		String accId = SecurityUtil.getFiscalAccountId();
		return getVos(periodRepo.findByCheckStatus(accId, FiscalPeriod.USED));
	}

	/**
	 * 获取所有会计期间
	 * @return
	 */
	public List<FiscalPeriodVo> getAllUsedCheckedPeriod(){
		String accId = SecurityUtil.getFiscalAccountId();
		return getVos(periodRepo.getAllUsedCheckedPeriod(accId));
	}
	
	/**
	 * 获取最后会计期间(结束日期+1)
	 * @return
	 */
	public FiscalPeriodVo getLastPeriodPlusOne(){
		FiscalPeriod period = getLastPeriod();
		if(period!=null){
			Date endDate = DateUtilTools.changeDateTime(period.getEndDate(), 0, 1, 0, 0, 0);
			period.setEndDate(endDate);
			period.setPeriod(DateUtilTools.date2String(endDate, DateUtilTools.DATE_PATTERN_YYYYMM));
			return getVo(period);
		}
		return null;
	}
	
	/**
	 * 所有未结账的会计期间
	 * @return
	 */
    public List<FiscalPeriodVo> getNotCheck(){
    	String accId = SecurityUtil.getFiscalAccountId();
    	Sort sort = new Sort(Direction.DESC, "startDate");
		return getVos(periodRepo.findByUnCheck(accId, sort));
    }
    
    /**
     * 启用会计期间
     */
    @Transactional
    public RequestResult updateUsed(String periodId){
    	Assert.notNull(periodId);
    	FiscalPeriod first = getFirstPeriod();
    	
    	//如果是第一个会计期间
    	if(periodId.equals(first.getFid())){
    		RequestResult result = initService.trailInital();
        	if(!result.isSuccess()){
        		return result;
        	}
    	}
    	
    	FiscalPeriod period = periodRepo.findOne(periodId);
    	if(period.getCheckoutStatus()!=FiscalPeriod.UN_USED){
    		return buildFailRequestResult("会计期间不是未启用状态");
    	}
    	
    	//判断期间是否连续
    	FiscalPeriod prePeriod = getPrePeriod(periodId);
    	if(prePeriod!=null){
    		if(prePeriod.getCheckoutStatus()==FiscalPeriod.UN_USED){
    			return buildFailRequestResult("上一个会计期间未启用");
    		}
    		if(!checkDateContinue(prePeriod.getEndDate(), period.getStartDate())){
    			return buildFailRequestResult("会计期间不连续");
    		}
    	}
//    	FiscalPeriod nextPeriod = getNextPeriod(periodId);
//    	if(nextPeriod!=null){
//    		if(!checkDateContinue(period.getEndDate(), nextPeriod.getStartDate())){
//    			return buildFailRequestResult("会计期间不连续");
//    		}
//    	}
    	
    	//启用仓储会计期间
    	RequestResult result = stockPeriodService.updateUsed(periodId);
    	if(!result.isSuccess()){
    		return result;
    	}
    	//启用财务会计期间
    	period.setCheckoutStatus(FiscalPeriod.USED);
    	periodRepo.save(period);
    	
    	return buildSuccessRequestResult();
    }
    
    /**
     * 会计期间结账
     * @param periodId 会计期间ID
     * @param flag 未结转损益，是否必须结账标识，1 是 0 否
     * @return
     * @update rqh 2016-02-18 
     */
    public RequestResult updateChecked(String periodId, int flag){
    	Assert.notNull(periodId);

		FiscalPeriod prePeriod = getPrePeriod(periodId);
		if(prePeriod!=null && prePeriod.getCheckoutStatus()!=FiscalPeriod.CHECKED){
			return buildFailRequestResult("无效操作，上一个会计期间未结账");
		}

    	if(!voucherService.isAllPostedOrCanceled(periodId)){
    		return buildFailRequestResult("存在未过账凭证，不能结账!");
    	}
    	
    	if(flag == 0){
    		double balance = periodRepo.getBalanceOfLossSubject(periodId);
        	if(balance != 0){
        		RequestResult result = buildFailRequestResult("未结转损益，是否结账?");
        		result.setErrorCode(ErrorCode.BALANCE_OF_LOSS_SUBJECT_NOT_ZERO);
        	}
    	}
    	
    	FiscalPeriod entity = periodRepo.findOne(periodId);
    	entity.setCheckoutStatus(FiscalPeriod.CHECKED);
    	periodRepo.save(entity);
    	return buildSuccessRequestResult();
    }
    
    
    /**
     * 会计期间反结账
     */
    public RequestResult updateUnChecked(String periodId){
    	Assert.notNull(periodId);
    	FiscalPeriod nextPeriod = getNextPeriod(periodId);
    	if(nextPeriod != null && nextPeriod.getCheckoutStatus() == FiscalPeriod.CHECKED){
    		return buildFailRequestResult("无效操作，下一个会计期间已结账!");
    	}
    	
    	FiscalPeriod entity = periodRepo.findOne(periodId);
    	entity.setCheckoutStatus(FiscalPeriod.USED);
    	periodRepo.save(entity);
    	return buildSuccessRequestResult();
    }
    
    
    /**
     * 检查时间是否连续
     */
    private boolean checkDateContinue(Date one, Date two){
    	Date oneChange = DateUtilTools.changeDateTime(one, 0, 1, 0, 0, 0);
    	Date twoChange = DateUtilTools.changeDateTime(two, 0, 0, 0, 0, 0);
    	if(oneChange.getTime()==twoChange.getTime())return true;
    	return false;
    }
    
    /**
     * 获取上一个会计期间(结束时间+1日)
     * @return
     */
    public FiscalPeriodVo getPrePeriodPlusOne(String fid){
    	FiscalPeriod pre = getPrePeriod(fid);
    	
    	if(pre==null){
    		return null;
    	}else{
    		Date endDate = pre.getEndDate();
        	endDate = DateUtilTools.changeDateTime(endDate, 0, 1, 0, 0, 0);
        	pre.setEndDate(endDate);
    	}
    	return getVo(pre);
    }
    
    /**
     * 获取指定会计期间的上一个会计期间
     * @param id 会计期间ID
     * @return
     */
    public FiscalPeriod getPrePeriod(String id){
    	FiscalPeriod period = periodRepo.findOne(id);
    	String accId = period.getFiscalAccount().getFid();
    	return periodRepo.findPrePeriodByDate(accId, period.getStartDate());
    }
    
    /**
     * 获取指定会计期间的下一个会计期间
     * @param id 会计期间ID
     * @return
     */
    public FiscalPeriod getNextPeriod(String id){
    	FiscalPeriod period = periodRepo.findOne(id);
    	String accId = period.getFiscalAccount().getFid();
    	return periodRepo.findTopPeriodByStartDate(accId, period.getEndDate());
    }
    
    /**
     * 统计已结账或启用的会计期间个数
     * @param accId
     * @return
     */
    public Long countUsedPeriod(String accId){
    	List<Integer> statuLists = Lists.newArrayList(FiscalPeriod.USED, FiscalPeriod.CHECKED);
    	Long count = periodRepo.countByAccIdAndStatus(accId, statuLists);
    	if(count==null)return 0L;
    	return count;
    }

    /**
     * 根据日期获得会计期间
     * @param voucherDate
     * @param fiscalAccountId
     * @param checkoutStatus
     * @return
     */
	public FiscalPeriod getPeriod(Date voucherDate, String fiscalAccountId, int checkoutStatus) {
		return periodRepo.findTopByDate(fiscalAccountId, voucherDate, checkoutStatus);
	}
	
	/**
     * 根据日期获得会计期间
     * @param voucherDate
     * @param fiscalAccountId
     * @return
     */
	public FiscalPeriod getPeriod(Date voucherDate, String fiscalAccountId) {
		return periodRepo.findTopByDate(fiscalAccountId, voucherDate);
	}

	  /**
     * 获得当年最小的会计期间
     * @return
     */
    public FiscalPeriod getMinPeriod(FiscalPeriod period){
    	return periodRepo.getMinPeriod(period);
    }
    /**
	 * 获取最后一个已结账的会计期间
	 * @param accountId
	 * @return
	 */
    public FiscalPeriod getLastCheckedPeriod(String accountId){
    	Sort sort = new Sort(Direction.DESC, "endDate");
    	return periodRepo.getLastCheckedPeriod(accountId, sort);
    }
}
