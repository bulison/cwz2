package cn.fooltech.fool_ops.domain.period.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalPeriod;
import cn.fooltech.fool_ops.domain.period.entity.StockPeriod;
import cn.fooltech.fool_ops.domain.period.repository.StockPeriodRepository;
import cn.fooltech.fool_ops.domain.period.vo.StockPeriodVo;
import cn.fooltech.fool_ops.utils.DateUtil;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.SecurityUtil;


/**
 * <p>仓储会计期间网页服务类</p>
 */
@Service
public class StockPeriodService extends BaseService<StockPeriod, StockPeriodVo, String> {
	
    @Autowired  
    private JdbcTemplate jdbcTemplate;  
    
	@Autowired
	private  StockPeriodRepository periodRepo;
	
    /**
     * 启用仓储会计期间
     * @param periodId 仓储会计期间
     * @return
     */
    public RequestResult updateUsed(String periodId){
    	Assert.notNull(periodId);
    	
    	StockPeriod period = periodRepo.findOne(periodId);
    	if(period.getCheckoutStatus() != StockPeriod.UN_USED){
    		return buildFailRequestResult("会计期间不是未启用状态");
    	}
    	
    	//判断期间是否连续
    	StockPeriod prePeriod = getPrePeriod(periodId);
    	if(prePeriod != null){
    		if(prePeriod.getCheckoutStatus() == StockPeriod.UN_USED){
    			return buildFailRequestResult("上一个会计期间未启用");
    		}
    		if(!checkDateContinue(prePeriod.getEndDate(), period.getStartDate())){
    			return buildFailRequestResult("会计期间不连续");
    		}
    	}
    	
    	StockPeriod nextPeriod = getNextPeriod(periodId);
    	if(nextPeriod != null){
    		if(!checkDateContinue(period.getEndDate(), nextPeriod.getStartDate())){
    			return buildFailRequestResult("会计期间不连续");
    		}
    	}
    	
    	period.setCheckoutStatus(StockPeriod.USED);
    	periodRepo.save(period);
    	return buildSuccessRequestResult();
    }
	
	/**
	 * 结账和反结账功能
	 * @param vo
	 * @author rqh
	 */
    @Transactional
    public RequestResult changeStatus(StockPeriodVo vo){
       StockPeriod curStockPeriod = periodRepo.findOne(vo.getFid());
       
       //改为未结账
       StockPeriod nextStockPeriod = getNextPeriod(curStockPeriod.getFid()); //下一个会计期间
       if(curStockPeriod.getCheckoutStatus() == StockPeriod.CHECKED){
    	   if(nextStockPeriod != null && nextStockPeriod.getCheckoutStatus() == StockPeriod.CHECKED){
    		   return buildFailRequestResult("无效操作，下一个会计期间已结账!"); 
    	   }
    	   else{
    		   curStockPeriod.setCheckoutStatus(StockPeriod.USED);
    		   unSettleAccount(curStockPeriod.getFid());
        	   return new RequestResult();
    	   }
       }
       
       //改为已结账
       if(nextStockPeriod == null){
    	   return buildFailRequestResult("无效操作，下一个会计期间不存在!"); 
       }
       
       StockPeriod firstPeroid = findFirstPeriod(); //第一个会计期间
       if(firstPeroid != null && curStockPeriod.getFid().equals(firstPeroid.getFid())){
    	   settleAccount(curStockPeriod.getFid());
    	   return new RequestResult();
       }
       
       StockPeriod preStockPeriod = getPrePeriod(curStockPeriod.getFid()); //上一个会计期间
       if(preStockPeriod == null){
    	   return buildFailRequestResult("无效操作，上一个会计期间不存在!");
       }
       else if(preStockPeriod.getCheckoutStatus() == StockPeriod.UN_USED){
    	   return buildFailRequestResult("无效操作，上一个会计期间未启用!");
       }
       else if(preStockPeriod.getCheckoutStatus() == StockPeriod.USED){
    	   return buildFailRequestResult("无效操作，上一个会计期间未结账!");
	   }
	   else{
		   settleAccount(curStockPeriod.getFid());
		   return buildSuccessRequestResult();
	   }
    }

	/**
	 * 结账
	 * @param id
	 */
	@Transactional
    public void settleAccount(String id){
    	StockPeriod stockPeriod = periodRepo.findOne(id);
    	String orgId = stockPeriod.getOrg().getFid();
    	periodRepo.settleAccount(orgId, id);
    }

	/**
	 * 反结账
	 * @param id
	 */
	@Transactional
	public void unSettleAccount(String id){
		StockPeriod stockPeriod = periodRepo.findOne(id);
		String orgId = stockPeriod.getOrg().getFid();
		periodRepo.unSettleAccount(orgId, id);
	}
    
    /**
     * 获取指定会计期间的下一个会计期间
     * @param id 会计期间ID
     * @return
     */
    public StockPeriod getNextPeriod(String id){
    	StockPeriod stockPeriod = periodRepo.findOne(id);
    	Date startDate = DateUtilTools.addDateTime(stockPeriod.getEndDate(), 1, 0, 0, 0);
    	
    	String accId = stockPeriod.getFiscalAccount().getFid();
    	return periodRepo.findTopPeriodByStartDate(accId, startDate);
    }
    
    /**
     * 获取指定会计期间的下一个会计期间
     * @param id 会计期间ID
     * @return
     */
    public StockPeriod getPrePeriod(String id){
    	StockPeriod stockPeriod = periodRepo.findOne(id);
    	Date pre = DateUtilTools.addDateTime(stockPeriod.getStartDate(), -1, 0, 0, 0);
    	
    	String accId = stockPeriod.getFiscalAccount().getFid();
    	return periodRepo.findTopByEndDate(accId, pre);
    }
    
    /**
     * 克隆财务会计期间
     * @param fiscalPeriod 财务会计期间 
     * @return
     * @author rqh
     */
    public StockPeriod clonePeroid(FiscalPeriod fiscalPeriod){
    	Assert.notNull(fiscalPeriod, "财务会计期间不能为空!");
    	StockPeriod stockPeriod = new StockPeriod();
    	stockPeriod.setFid(fiscalPeriod.getFid());
    	stockPeriod.setOrg(fiscalPeriod.getOrg());
    	stockPeriod.setDept(fiscalPeriod.getDept());
    	stockPeriod.setPeriod(fiscalPeriod.getPeriod());
    	stockPeriod.setEndDate(fiscalPeriod.getEndDate());
    	stockPeriod.setCreator(fiscalPeriod.getCreator());
    	stockPeriod.setStartDate(fiscalPeriod.getStartDate());
    	stockPeriod.setUpdateTime(fiscalPeriod.getUpdateTime());
    	stockPeriod.setDescription(fiscalPeriod.getDescription());
    	stockPeriod.setFiscalAccount(fiscalPeriod.getFiscalAccount());
    	stockPeriod.setCheckoutStatus(fiscalPeriod.getCheckoutStatus());
    	return stockPeriod;
    }
    
    /**
	 * 获取最后一个会计期间
	 * @return
	 */
    public StockPeriod getLastPeriod(){
    	String accId = SecurityUtil.getFiscalAccountId();
		Sort sort = new Sort(Direction.DESC, "endDate");
		return periodRepo.findTopPeriodByAccId(accId, sort);
    }
    
    /**
     * 删除仓储会计期间
     * @param id 仓储会计期间
     * @return
     */
    public RequestResult delete(String id){
    	StockPeriod stockPeriod = periodRepo.findOne(id);
    	if(stockPeriod == null) {
			return buildFailRequestResult("数据不存在，可能已被删除，请刷新再试");
		}
    	
    	StockPeriod period = findLastPeriod();
    	if(stockPeriod.getEndDate().compareTo(period.getEndDate())!=0){
    		return buildFailRequestResult("该会计期间不是最后一条，不能删除！");
    	}
    	if(stockPeriod.getCheckoutStatus() == StockPeriod.CHECKED){
    		return buildFailRequestResult("该会计期间已经结账，不能删除！");
    	}
    	boolean relation = isRelation(id);
    	if(relation){
    		return buildFailRequestResult("该会计期间已经被引用，不能删除！");
    	}
    	periodRepo.delete(id);
    	return buildSuccessRequestResult();
    }
    
    /**
     * 判断会计期间是否已被关系或使用
     * @param id
     * @return
     */
    public boolean isRelation(String id) {
    	Long count = periodRepo.isRelation(id);
    	if(count!=null && count>0){
    		return true;
    	}
		return false;
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
	 * 获得最后未结账会计期间
	 * @return
	 */
	public StockPeriod getLastNotCheck() {
		String accId = SecurityUtil.getFiscalAccountId();
		Sort sort = new Sort(Direction.DESC, "startDate");
		return periodRepo.findTopPeriodByUnCheck(accId, sort);
	}
	
	/**
	 * 判断第一个会计期间是否结账
	 * @return
	 */
	public boolean theFirstCheck(){
		StockPeriod stockPeriod = findFirstPeriod();
		if(stockPeriod == null){
			return false;
		}
		return stockPeriod.getCheckoutStatus() == StockPeriod.CHECKED;
	}
	
	/**
	 * 查找第一个会计期间
	 * @return
	 */
	public StockPeriod findFirstPeriod(){
		String accId = SecurityUtil.getFiscalAccountId();
		Sort sort = new Sort(Direction.ASC, "startDate");
		return periodRepo.findTopPeriodByAccId(accId, sort);
	}
	
	/**
	 * 获取最后一个会计期间
	 * @return
	 */
    public StockPeriod findLastPeriod(){
    	String accId = SecurityUtil.getFiscalAccountId();
		Sort sort = new Sort(Direction.DESC, "startDate");
		return periodRepo.findTopPeriodByAccId(accId, sort);
    }
	
	/**
	 * 获取所有未结账的会计期间
	 * @return
	 */
    public List<StockPeriodVo> getNotCheck(){
    	String accId = SecurityUtil.getFiscalAccountId();
    	List<StockPeriod> entities = periodRepo.findByCheckStatus(accId, StockPeriod.USED);
    	return getVos(entities);
    }
    
    /**
     * 获取所有未结账、已结账的会计期间
     * @return
     */
    public List<StockPeriodVo> queryAll(){
    	String accId = SecurityUtil.getFiscalAccountId();
    	List<StockPeriod> entities = periodRepo.findByAccountId(accId);
    	return getVos(entities);
    }
    
    /**
     * 获取会计期间
     * @param date 日期
     * @param accountId 财务账套ID
     * @return
     */
    public StockPeriod getPeriod(Date date, String accountId){
  	  date = DateUtil.getSimpleDate(date);
  	  return periodRepo.findTopByDate(accountId, date);
    }
    

    /**
     * 单个实体转vo
     * @return
     */
	@Override
	public StockPeriodVo getVo(StockPeriod entity) {
		StockPeriodVo vo = new StockPeriodVo();
    	vo.setFid(entity.getFid());
    	vo.setPeriod(entity.getPeriod());
    	vo.setDescription(entity.getDescription());
    	vo.setCheckoutStatus(entity.getCheckoutStatus());
    	vo.setEndDate(DateUtilTools.date2String(entity.getEndDate()));
    	vo.setStartDate(DateUtilTools.date2String(entity.getStartDate()));
    	return vo;
	}

	@Override
	public CrudRepository<StockPeriod, String> getRepository() {
		return this.periodRepo;
	}
	  /**
	   * 获取第一个会计期间
	   * @param orgId 机构ID
	   * @param accountId 财务账套ID
	   * @return
	   */
	public StockPeriod getTheFristPeriod(String orgId, String accountId){
	  Date minDate = getMinDate(orgId, accountId);
	  StockPeriod period = periodRepo.getTheFristPeriod(orgId, accountId, minDate);
	  return period!=null?period:null;
	}	
	/**
	 * 获取第一个会计期间开始时间
	 * @param orgId 机构ID
	 * @param accountId 财务账套ID
	 * @return
	 */
	public Date getMinDate(String orgId, String accountId) {
		String sql = "select min(s.FEND_DATE) as endDate from tbd_stock_period s where FORG_ID='" + orgId
				+ "' and FACC_ID='" + accountId+"'";
		Map<String, Object> map = jdbcTemplate.queryForMap(sql);
		return map.get("endDate") == null ? null : DateUtilTools.string2Date(map.get("endDate").toString());
	}
    /**
     * 统计已结账的会计期间个数
     * @param orgId 机构ID
     * @param accountId 财务账套ID
     * @return
     */
    public Long countCheckedPeriod(String orgId, String accountId){
    	return periodRepo.countCheckedPeriod(orgId,accountId);
    }
}
