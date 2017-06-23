package cn.fooltech.fool_ops.domain.rate.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalPeriod;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalAccountService;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalPeriodService;
import cn.fooltech.fool_ops.domain.rate.entity.BusinessFormula;
import cn.fooltech.fool_ops.domain.rate.entity.FiscalBusiness;
import cn.fooltech.fool_ops.domain.rate.entity.FiscalBusinessFormula;
import cn.fooltech.fool_ops.domain.rate.repository.FiscalBusinessRepository;
import cn.fooltech.fool_ops.domain.rate.vo.FiscalBusinessVo;
import cn.fooltech.fool_ops.domain.report.entity.FormulaVo;
import cn.fooltech.fool_ops.domain.report.service.ReportFormulaService;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.sysman.repository.UserRepository;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.ExpressionUtils;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.VoFactory;
/**
 * 经营收益率分析表服务类
 * @author hjr
 *	2017-04-17
 */
@Service
public class FiscalBusinessService extends BaseService<FiscalBusiness,FiscalBusinessVo,String>{
	@Autowired
	private FiscalBusinessRepository businessRepo;
	@Autowired
	private FiscalBusinessFormulaService formulaService;
	/**
	 * 资产负债公式服务类
	 */
	@Autowired
	private ReportFormulaService rfService;

	private static final Logger logger = LoggerFactory.getLogger(FiscalBusinessService.class);
	/**
	 * 会计期间服务类
	 */
	@Autowired
	private FiscalPeriodService periodService;
	@Override
	public FiscalBusinessVo getVo(FiscalBusiness entity) {
		FiscalBusinessVo vo=new FiscalBusinessVo();
		vo.setFid(entity.getFid());
		vo.setPeriodId(entity.getPeriod().getFid());
		vo.setPeriodName(entity.getPeriod().getPeriod());
		vo.setDeptId(entity.getDept().getFid());
		vo.setDeptName(entity.getDept().getOrgName());
		if(entity.getNumber()!=null){
			vo.setNumber(entity.getNumber());
		}
		if(entity.getFormula()!=null){
			vo.setFormula(entity.getFormula());
		}
		if(entity.getUpdateTime()!=null){
			vo.setUpdateTime(DateUtilTools.date2String(entity.getUpdateTime(), DATE_TIME));
		}
		if(entity.getItem()!=null){
			vo.setItem(entity.getItem());
		}
		if(entity.getValue()!=null){
			vo.setValue(entity.getValue());
		}
		return vo;
	}
	public List<FiscalBusinessVo> query(FiscalBusinessVo vo){
		String periodId = vo.getPeriodId();
		if(Strings.isNullOrEmpty(periodId))return Collections.EMPTY_LIST;
		
		Sort sort = new Sort(Direction.ASC, "number");
		return getVos(businessRepo.findByPeriod(periodId,sort));
		
	}
	/**
	 * 先查询经营收益率分析表有没有数据,如果没有数据则从默认公式表写入
	 */
	public List<FiscalBusiness> saveInitFiscalBusiness(String periodId,boolean autoCompute){
		if(Strings.isNullOrEmpty(periodId))return null;
		Long count=businessRepo.countByPeriodId(periodId);
		if(count!=null && count==0){
			return saveNewFiscalBusiness(periodId,autoCompute);
		}
		return null;
		
	}
	/**
	 * 从默认公式表写入公式
	 * @param periodId
	 * @return
	 */
	private List<FiscalBusiness> saveNewFiscalBusiness(String periodId,boolean autoCompute) {
		FiscalAccount acc = SecurityUtil.getFiscalAccount();
		List<FiscalBusinessFormula> formulas=formulaService.findByAccountId(acc.getFid());
		List<FiscalBusiness> list=Lists.newArrayList();
		
		Organization org = SecurityUtil.getCurrentOrg();
		Organization dept = SecurityUtil.getCurrentDept();
		Map<String,BigDecimal> pcache = Maps.newHashMap();
		Date updateTime = Calendar.getInstance().getTime();
		FiscalPeriod period = periodService.get(periodId);
		for(FiscalBusinessFormula formula:formulas){
			FiscalBusiness newdata=copyProperties(formula, acc, org, dept);
			if(autoCompute){
				String oldFormula = newdata.getFormula();
				if(oldFormula.contains(ExpressionUtils.DS_FUNCTION)){
					newdata.setFormula(recurseDSFormula(oldFormula, formulas));
				}
				logger.debug("当前计算行号:"+newdata.getNumber());
				newdata = saveBusinessValue(newdata, period, pcache);
				newdata.setFormula(oldFormula);
			}
			newdata.setUpdateTime(updateTime);
			newdata.setPeriod(period);
			businessRepo.save(newdata);
			list.add(newdata);
		}
		return list;
	}
	/**
	 * 把当前公式写入公式表
	 */
	@Transactional
	public RequestResult saveBusinessFormula(String periodId){
		FiscalAccount account = SecurityUtil.getFiscalAccount();
		Organization dept = SecurityUtil.getCurrentDept();
		Organization org = SecurityUtil.getCurrentOrg();
		String accId = account.getFid();
		Date updateTime = Calendar.getInstance().getTime();
		formulaService.deleteByAccountId(accId);
		List<FiscalBusiness> businesss=businessRepo.findByPeriod(periodId);
		for(FiscalBusiness business:businesss){
			FiscalBusinessFormula formula=copyProperties(business,account, org, dept);
			formula.setUpdateTime(updateTime);
			formulaService.save(formula);
		}
		return buildSuccessRequestResult();
		
	}
	/**
	 * 从经营收益率公式表写入公式 
	 */
	@Transactional
	public RequestResult saveResumeFormula(String periodId){
		deleteByPeriodId(periodId);
		saveNewFiscalBusiness(periodId,false);
		return buildSuccessRequestResult();
		
	}
	
	/**
	 * 根据会记期间删除公式
	 */
	public void deleteByPeriodId(String periodId) {
		List<FiscalBusiness> list= businessRepo.findByPeriod(periodId);
		businessRepo.delete(list);
	}
	/**
	 * 把公式变数据变为分析表数据
	 * @param formula
	 * @param acc
	 * @param org
	 * @param dept
	 * @return
	 */
	public FiscalBusiness copyProperties(FiscalBusinessFormula formula,FiscalAccount acc,Organization org,Organization dept){
		FiscalBusiness business=new FiscalBusiness();
		business.setDept(dept);
		business.setFiscalAccount(acc);
		business.setFormula(formula.getFormula());
		business.setItem(formula.getItem());
		business.setNumber(formula.getNumber());
		business.setOrg(org);
		return business;
		
	}
	/**
	 * 把分析表数据变成公式表数据
	 * @param business
	 * @param acc
	 * @param org
	 * @param dept
	 * @return
	 */
	public FiscalBusinessFormula copyProperties(FiscalBusiness business,FiscalAccount acc,Organization org,Organization dept){
		FiscalBusinessFormula formula=new FiscalBusinessFormula();
		formula.setFormula(business.getFormula());
		formula.setItem(business.getItem());
		formula.setNumber(business.getNumber());
		formula.setOrg(org);
		formula.setFiscalAccount(acc);
		formula.setDept(dept);
		return formula;
		
	}
	/**
	 * 初始化公式缓存
	 */
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public void initCache(List<String> items,final Map<String,BigDecimal> lcache,
			FiscalPeriod current){

		if(items!=null){
			for(String item:items){
				if(!lcache.containsKey(item)){
					BigDecimal periodEnd = getFormulaValue(item, current);
					lcache.put(item, periodEnd);
				}
			}
		}
	
		
	}
	/**
	 * 计算公式的值
	 * @param item
	 * @param period
	 * @param flag
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public BigDecimal getFormulaValue(String item, FiscalPeriod period){
		FormulaVo fvo = ExpressionUtils.parseFormula(item);
		String accId = SecurityUtil.getFiscalAccountId();
		Class clazz = ReportFormulaService.class;
		try {
			FiscalPeriod min = periodService.getMinPeriod(period);
			Date startDate = DateUtilTools.changeToYearStart(min.getStartDate());
			Date endDate = period.getEndDate();
			
			
			//1：函数参数；2：账套；3：时间点1 4：时间点2
			Method method = clazz.getMethod("get"+fvo.getName()+"Period", String.class, String.class, Date.class, Date.class);
			//method.setAccessible(true);	
			Object result = method.invoke(rfService, fvo.getParamater(), accId, startDate, endDate);
			if(result!=null)return (BigDecimal)result;
		} catch (Exception e) {
			logger.error("error item:"+item);
			logger.error("error method:get"+fvo.getName());
			e.printStackTrace();
		}
		return BigDecimal.ZERO;
		
	}
	/**
	 * 保存表格的值
	 * @return
	 */
	public FiscalBusiness saveBusinessValue(FiscalBusiness business,FiscalPeriod period,
			final Map<String,BigDecimal> pcache){
		String fromula = null;
		List<String> items = Lists.newArrayList();
		if(!Strings.isNullOrEmpty(business.getFormula())){
			ExpressionUtils.splitFormula(business.getFormula(),items);
			fromula=business.getFormula();
		}
		initCache(items,pcache,period);
		fromula = ExpressionUtils.replaceFormula(fromula, pcache);
		BigDecimal calVal = ExpressionUtils.computeByAviatorEvaluator(fromula);
		business.setValue(calVal);
		return business;
		
	}
	/**
	 * 递归统计DS函数的值
	 * @param list
	 * @return
	 */
	public String recurseDSFormula(String fromula, List<? extends BusinessFormula> list){
		logger.debug("before replace:"+fromula);
		List<String> items = Lists.newArrayList();
		ExpressionUtils.splitFormula(fromula, items);
		
		Map<String,String> map = Maps.newHashMap();
		for(String item:items){
			String fh = ExpressionUtils.getLeftExpression(item);
			item = ExpressionUtils.removeExpression(item);
			item = ExpressionUtils.removeLeftKh(item);

			if(ExpressionUtils.isStartWithDS(item)){
				FormulaVo vo = ExpressionUtils.parseFormula(item);
				String param = vo.getParamater();
				List<String> rows = ExpressionUtils.splitter_comma.splitToList(param);
				String paramFormula = "";
				for(String row:rows){
					String rowExp = ExpressionUtils.getLeftExpression(row);
					String resultExp = ExpressionUtils.computeExpression(fh, rowExp);
					String realRow = ExpressionUtils.removeExpression(row);
					paramFormula+=findFormulaByRow(realRow, list, resultExp);
				}
				if(ExpressionUtils.isStartWithPlus(paramFormula)){
					paramFormula = ExpressionUtils.removeExpression(paramFormula);
				}
				//小括号的正则式 
				String pReplace = item.replaceAll("\\(", "\\\\(").replaceAll("\\)", "\\\\)");
				if(!ExpressionUtils.isPositveFh(fh))pReplace = fh+pReplace;
				map.put(pReplace, "("+paramFormula+")");
			}
		}

		for(String replacement:map.keySet()){
			fromula = fromula.replaceAll(replacement, map.get(replacement));
		}

		logger.debug("after replace:"+fromula);
		if(fromula.contains(ExpressionUtils.DS_FUNCTION)){
			return recurseDSFormula(fromula, list);
		}

		return fromula;
		
	}
	/**
	 * 通过行号查找公式
	 * @param row
	 * @param list
	 * @return
	 */
	private String findFormulaByRow(String row, List<? extends BusinessFormula> list, String fh){
		
		String formula = "";
		int rowi = Integer.parseInt(row);
		for(BusinessFormula fbs:list){
			if(fbs.getNumber()!=null && fbs.getNumber()==rowi){
				if(Strings.isNullOrEmpty(fbs.getFormula())){
					return "";
				}
				formula = fbs.getFormula();
			}
		}
		if(Strings.isNullOrEmpty(formula))return "";
		String result = "";
		if(!ExpressionUtils.isPositveFh(fh)){
			List<String> functions = Lists.newArrayList();
			ExpressionUtils.splitFormula(formula, functions);
			
			for(String function:functions){
				String contaryFunction = ExpressionUtils.getContaryFunction(function);
				result+=contaryFunction;
			}
		}else{
			result = ExpressionUtils.addExpression(formula);
		}
		return result;
	}
	/**
	 * 新增/编辑经营收益率
	 * @param vo
	 */
	public RequestResult save(FiscalBusinessVo vo){
		RequestResult check = checkValidation(vo);
		if(!check.isSuccess()){
			return check;
		}
		
		FiscalPeriod period = periodService.get(vo.getPeriodId());
		FiscalBusiness entity=new FiscalBusiness();
		if(StringUtils.isBlank(vo.getFid())){
			entity.setOrg(SecurityUtil.getCurrentOrg());
			entity.setUpdateTime(new Date());
			entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
			entity.setDept(SecurityUtil.getCurrentDept());
			entity.setPeriod(period);
		}else {
			entity = businessRepo.findOne(vo.getFid());
		}
		entity.setItem(vo.getItem());
		entity.setNumber(vo.getNumber());
		entity.setFormula(vo.getFormula());
		entity.setUpdateTime(Calendar.getInstance().getTime());
		businessRepo.save(entity);
		String newUpdateTime = DateUtilTools.time2String(entity.getUpdateTime());
		return buildSuccessRequestResult(newUpdateTime);
		
	}
	/**
	 * 检查公式格式
	 * @param vo
	 * @return
	 */
	public RequestResult checkValidation(FiscalBusinessVo vo){
		List<FiscalBusiness> datas= businessRepo.findByPeriod(vo.getPeriodId());
		FiscalBusiness newobj=VoFactory.createValue(FiscalBusiness.class,vo);
		datas.add(newobj);
		if(!Strings.isNullOrEmpty(vo.getFormula())){
			String name=vo.getItem();
/*			List<String> items = Lists.newArrayList();
			boolean check1 = ExpressionUtils.splitFormula(vo.getFormula(),items);
			if(!check1)return buildFailRequestResult("["+name+"]非法公式");
			for(String item:items){
				if(!ExpressionUtils.checkSingleFormula(item)){
					return buildFailRequestResult("["+name+"]非法公式");
				}
			}*/
			if(!ExpressionUtils.regularCheck(vo.getFormula())){
				return buildFailRequestResult("["+name+"]非法公式");
			}
			for(FiscalBusiness business:datas){
				List<Integer> refList = Lists.newArrayList();
				if(checkRecurseRef(business.getFormula(), business.getNumber(), datas, refList)){
					return buildFailRequestResult("["+name+"]公式重复引用");
				}
			
			}
		}
		if(!Strings.isNullOrEmpty(vo.getFid())){
			FiscalBusiness fb = businessRepo.findOne(vo.getFid());
			if(!checkUpdateTime(vo.getUpdateTime(), fb.getUpdateTime())){
				return buildFailRequestResult("公式已被其他用户修改，请刷新再试");
			}
		}
		
		Integer number = vo.getNumber();
		String periodId = vo.getPeriodId();
		String excludeId = vo.getFid();
		
		if(Strings.isNullOrEmpty(vo.getFid())){
			long count = businessRepo.countByNumberAndPeriodId(periodId, number);
			if(count>0)return buildFailRequestResult("["+vo.getItem()+"]行号重复");
		}else{
			long count = businessRepo.countByNumberAndPeriodId(periodId, number, excludeId);
			if(count>0)return buildFailRequestResult("["+vo.getItem()+"]行号重复");
		}
		
		return buildSuccessRequestResult();

		
	}
	/**
	 * 检查循环引用
	 * @param formula
	 * @param rowNum
	 * @param list
	 * @param refList
	 * @return
	 */
	public boolean checkRecurseRef(String formula, Integer rowNum, List<FiscalBusiness> list, List<Integer> refList){
		
		if(Strings.isNullOrEmpty(formula)||rowNum==null||
				!formula.contains(ExpressionUtils.DS_FUNCTION))return false;
		
		if(refList.contains(rowNum))return true;
		refList.add(rowNum);
		
		List<String> items = Lists.newArrayList();
		ExpressionUtils.splitFormula(formula, items);
		
		for(String item:items){
			String fh = ExpressionUtils.getLeftExpression(item);
			item = ExpressionUtils.removeExpression(item);
			
			if(ExpressionUtils.isStartWithDS(item)){
				FormulaVo vo = ExpressionUtils.parseFormula(item);
				String param = vo.getParamater();
				List<String> rows = ExpressionUtils.splitter_comma.splitToList(param);
				
				String paramFormula = "";
				for(String row:rows){
					String function = findFormulaByRow(row, list, fh);
					paramFormula+=function;
					String rowStr = row;
					if(ExpressionUtils.isExpressionLeft(row)){
						rowStr = row.substring(1);
					}
					Integer rowi = Integer.parseInt(rowStr);
					
					List<Integer> newRefList = Lists.newArrayList(refList);
					if(checkRecurseRef(function, rowi, list, newRefList))return true;
				}
				ExpressionUtils.isStartWithPlus(paramFormula);
				paramFormula = ExpressionUtils.removeExpression(paramFormula);
			}
		}
		
		return false;
	}
	/**
	 * 计算经营收益率
	 * @param periodId
	 * @return
	 */
	@Transactional
	public RequestResult saveComputeFormula(String periodId){
		FiscalPeriod period = periodService.get(periodId);
		List<FiscalBusiness> businesss=businessRepo.findByPeriod(periodId);
		Map<String,BigDecimal> pcache = Maps.newHashMap();
		for(FiscalBusiness business:businesss){
			String oldFormula=business.getFormula();
			if(oldFormula.contains(ExpressionUtils.DS_FUNCTION)){
				business.setFormula(recurseDSFormula(oldFormula, businesss));
			}
			business=saveBusinessValue(business,period,pcache);
			business.setFormula(oldFormula);
			if(business.getNumber()==14||business.getNumber()==15){
				business.setValue(business.getValue().multiply(new BigDecimal(100)));
			}
			businessRepo.save(business);
		}
		return buildSuccessRequestResult();
	}
	/**
	 * 旧数据初始化,只需要运行一次,在程序上线部署时修复旧数据 
	 */
	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private UserRepository userRepo;
		//账套服务类
	@Autowired
	private FiscalAccountService accService;
	@Transactional 
	public RequestResult initFormula(){
		//获取所有用户的账套ID
		List<FiscalAccount> list=accService.findAll();
		String INIT_FILE = "classpath:datainit/init_fiscal_business.sql";
		for(FiscalAccount acc:list){
			if(formulaService.existsFormula(acc.getFid(), acc.getOrg().getFid())>0){
				continue;
			}
			Date now = Calendar.getInstance().getTime();
			LinkedHashMap<String,String> map = Maps.newLinkedHashMap();
			map.put("FCREATE_TIME", DateUtilTools.date2String(now, DateUtilTools.DATE_PATTERN_YYYY_MM_DDHHMMSS));
			if(acc.getCreator()!=null){
				map.put("FCREATOR_ID", acc.getCreator().getFid());
			}else{
				List<User> u=userRepo.findAdminByOrgId(acc.getOrg().getFid());
				if(u.size()>0){
					map.put("FCREATOR_ID",u.get(0).getFid());
				}
				else{
					continue;
				}
			}
			map.put("FORG_ID", acc.getOrg().getFid());
			map.put("FACC_ID", acc.getFid());
			if(acc.getDept()!=null){
				map.put("DEPTID", acc.getDept().getFid());
			}else{
				map.put("DEPTID", "");
			}
			List<String> lines = null;
			try {
				File sqlFile = ResourceUtils.getFile(INIT_FILE);
				lines = Files.readLines(sqlFile, Charsets.UTF_8);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	        for(String line:lines){
	        	if(StringUtils.isBlank(line)){
	        		continue;
	        	}
	        	String FID = UUID.randomUUID().toString().replaceAll("-", "");
	        	map.put("FID", FID);
	        	
	        	String exeSql = line;
	        	
	        	for(String key:map.keySet()){
	        		String processKey = ":"+key;
	        		exeSql = exeSql.replaceAll(processKey, map.get(key));
	        	}
	        	
		        	Query query = entityManager.createNativeQuery(exeSql);
		    		query.executeUpdate();
	        	
	        }
		}
		return buildSuccessRequestResult();
	}
	@Override
	public CrudRepository<FiscalBusiness, String> getRepository() {
		return businessRepo;
	}
}
