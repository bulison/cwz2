package cn.fooltech.fool_ops.domain.report.service;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalPeriod;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalPeriodService;
import cn.fooltech.fool_ops.domain.report.entity.FormulaVo;
import cn.fooltech.fool_ops.domain.report.entity.ProfitFormula;
import cn.fooltech.fool_ops.domain.report.entity.ProfitSheet;
import cn.fooltech.fool_ops.domain.report.entity.ProfitSheetFormula;
import cn.fooltech.fool_ops.domain.report.repository.ProfitSheetRepository;
import cn.fooltech.fool_ops.domain.report.vo.ProfitSheetVo;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.ExpressionUtils;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.VoFactory;

/**
 * <p>利润网页服务类</p>
 * @author xjh
 * @version 1.0
 * @date 2016-02-18 16:27:29
 */
@Service
public class ProfitSheetService extends BaseService<ProfitSheet, ProfitSheetVo, String> {
	
	private static final Logger logger = LoggerFactory.getLogger(ProfitSheetService.class);
	
	/**
	 * 利润服务类
	 */
	@Autowired
	private ProfitSheetRepository profitSheetRepo;
	
	/**
	 * 会计期间服务类
	 */
	@Autowired
	private FiscalPeriodService periodService;
	
	/**
	 * 利润公式服务类
	 */
	@Autowired
	private ProfitSheetFormulaService formulaService;
	
	/**
	 * 资产负债公式服务类
	 */
	@Autowired
	private ReportFormulaService rfService;
	
	/**
	 * 先查询期间利润表有没有数据，没有则从默认公式表复制公式并计算写入到期间利润表中
	 */
	public List<ProfitSheet> saveInitProfitSheet(String periodId, boolean autoCompute){
		
		if(Strings.isNullOrEmpty(periodId))return null;
		Long count = profitSheetRepo.countByPeriodId(periodId);
		if(count==0){
			return saveNewProfitSheet(periodId, autoCompute);
		}
		return null;
	}
	
	/**
	 * 从利润公式表中创建数据
	 */
	@Transactional
	private List<ProfitSheet> saveNewProfitSheet(String periodId, boolean autoCompute){
		FiscalAccount account = SecurityUtil.getFiscalAccount();
		
		List<ProfitSheetFormula> formulas = formulaService.findByAccountId(account.getFid());
		List<ProfitSheet> list = Lists.newArrayList();
		
		Map<String,BigDecimal> pcache = Maps.newHashMap();
		Map<String,BigDecimal> ycache = Maps.newHashMap();
		
		Organization org = SecurityUtil.getCurrentOrg();
		Organization dept = SecurityUtil.getCurrentDept();
		Date updateTime = Calendar.getInstance().getTime();
		FiscalPeriod period = periodService.get(periodId);
		
		for(ProfitSheetFormula formula:formulas){
			ProfitSheet newdata = copyProperties(formula, account, org, dept);
			if(autoCompute){
				String oldFormula = newdata.getFormula();
				if(oldFormula.contains(ExpressionUtils.DS_FUNCTION)){
					newdata.setFormula(recurseDSFromula(oldFormula, formulas));
				}
				logger.debug("当前计算行号:"+newdata.getNumber());
				newdata = saveSheetValue(newdata, period, pcache, ycache);
				newdata.setFormula(oldFormula);
			}
			newdata.setUpdateTime(updateTime);
			newdata.setPeriod(period);
			profitSheetRepo.save(newdata);
			list.add(newdata);
		}
		return list;
	}
	
	
	/**
	 * 检查循环引用
	 * @return
	 */
	public boolean checkRecurseRef(String formula, Integer rowNum, List<ProfitSheet> list, List<Integer> refList){
		
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
	 * 递归统计DS函数的值
	 * @param list
	 * @return
	 */
	public String recurseDSFromula(String fromula, List<? extends ProfitFormula> list){
		logger.debug("before replace:"+fromula);
		
		List<String> items = Lists.newArrayList();
		ExpressionUtils.splitFormula(fromula, items);
		
		Map<String,String> map = Maps.newHashMap();
		
		for(String item:items){
			String fh = ExpressionUtils.getLeftExpression(item);
			item = ExpressionUtils.removeExpression(item);
			
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
				map.put(pReplace, paramFormula);
			}
		}
		
		for(String replacement:map.keySet()){
			fromula = fromula.replaceAll(replacement, map.get(replacement));
		}
		
		logger.debug("after replace:"+fromula);
		if(fromula.contains(ExpressionUtils.DS_FUNCTION)){
			return recurseDSFromula(fromula, list);
		}
		return fromula;
	}
	
	/**
	 * 通过行号查找公式
	 * @param row
	 * @param list
	 * @return
	 */
	private String findFormulaByRow(String row, List<? extends ProfitFormula> list, String fh){
		
		String formula = "";
		int rowi = Integer.parseInt(row);
		for(ProfitFormula bsf:list){
			if(bsf.getNumber()!=null && bsf.getNumber()==rowi){
				if(Strings.isNullOrEmpty(bsf.getFormula())){
					return "";
				}
				formula = bsf.getFormula();
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
	 * 保存表格的值
	 * @return
	 */
	public ProfitSheet saveSheetValue(ProfitSheet sheet, FiscalPeriod period, 
			final Map<String,BigDecimal> pcache, final Map<String,BigDecimal> ycache){
		
		String pfromula1 = null;
		String yfromula1 = null;
		
		List<String> items1 = Lists.newArrayList();
		if(!Strings.isNullOrEmpty(sheet.getFormula())){
			ExpressionUtils.splitFormula(sheet.getFormula(), items1);
			pfromula1 = sheet.getFormula();
			yfromula1 = sheet.getFormula();
		}
		
		initCache(items1, pcache, ycache, period);
		
		pfromula1 = ExpressionUtils.replaceFormula(pfromula1, pcache);
		yfromula1 = ExpressionUtils.replaceFormula(yfromula1, ycache);
		
		// 编译表达式 
        BigDecimal pcalVal1 = ExpressionUtils.computeByAviatorEvaluator(pfromula1);
        BigDecimal yVal1 = ExpressionUtils.computeByAviatorEvaluator(yfromula1);
        
		sheet.setCurrentPeriodAmount(pcalVal1);
		sheet.setLastPeriodAmount(yVal1);
		
		return sheet;
	}
	
	/**
	 * 初始化公式缓存
	 */
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public void initCache(List<String> items,final Map<String,BigDecimal> lcache,
			final Map<String,BigDecimal> ccache, FiscalPeriod current){
		if(items!=null){
			for(String item:items){
				if(!lcache.containsKey(item)){
					BigDecimal periodEnd = getFormulaValue(item, current, Boolean.TRUE);
					lcache.put(item, periodEnd);
				}
				if(!ccache.containsKey(item)){
					BigDecimal yearBegin = getFormulaValue(item, current, Boolean.FALSE);
					ccache.put(item, yearBegin);
				}
			}
		}
	}
	
	/**
	 * 计算公式的值
	 * @param item
	 * @param period 会计期间
	 * @param flag 是否上一年
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public BigDecimal getFormulaValue(String item, FiscalPeriod period, boolean flag){
		FormulaVo fvo = ExpressionUtils.parseFormula(item);
		
		
		String accId = SecurityUtil.getFiscalAccountId();
		Class clazz = ReportFormulaService.class;
		try {
			FiscalPeriod min = periodService.getMinPeriod(period);
			Date startDate = DateUtilTools.changeToYearStart(min.getStartDate());
			Date endDate = period.getEndDate();
			
			if(flag){
				startDate = DateUtilTools.changeToPreYear(startDate);
				endDate = DateUtilTools.changeToPreYear(endDate);
			}
			
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
	 * 检查公式格式
	 * @param sheet
	 * @return
	 */
	public RequestResult checkValidation(ProfitSheetVo sheet){
		
		List<ProfitSheet> datas = profitSheetRepo.findByPeriodId(sheet.getPeriodId());
		
		ProfitSheet newobj = VoFactory.createValue(ProfitSheet.class, sheet);
		datas.add(newobj);
		
		if(!Strings.isNullOrEmpty(sheet.getFormula())){
			String name = sheet.getItem();
/*			List<String> items = Lists.newArrayList();
			ExpressionUtils.splitFormula(sheet.getFormula(), items);
			for(String item:items){
				if(!ExpressionUtils.checkSingleFormula(item)){
					return buildFailRequestResult("["+name+"]非法公式");
				}
			}*/
			if(!ExpressionUtils.regularCheck(sheet.getFormula())){
				return buildFailRequestResult("["+name+"]非法公式");
			}
			for(ProfitSheet ps:datas){
				List<Integer> refList = Lists.newArrayList();
				if(checkRecurseRef(ps.getFormula(), ps.getNumber(), datas, refList)){
					return buildFailRequestResult("["+name+"]公式重复引用");
				}
			}
		}
		
		if(!Strings.isNullOrEmpty(sheet.getFid())){
			ProfitSheet ps = profitSheetRepo.findOne(sheet.getFid());
			if(!checkUpdateTime(sheet.getUpdateTime(), ps.getUpdateTime())){
				return buildFailRequestResult("公式已被其他用户修改，请刷新再试");
			}
		}
		
		Integer number = sheet.getNumber();
		String periodId = sheet.getPeriodId();
		String excludeId = sheet.getFid();
		
		if(Strings.isNullOrEmpty(sheet.getFid())){
			long count = profitSheetRepo.countByNumberAndPeriodId(number, periodId);
			if(count>0)return buildFailRequestResult("["+sheet.getItem()+"]行号重复");
		}else{
			long count = profitSheetRepo.countByNumberAndPeriodId(number, periodId, excludeId);
			if(count>0)return buildFailRequestResult("["+sheet.getItem()+"]行号重复");
		}
		
		return buildSuccessRequestResult();
	}
	
	/**
	 * 查询利润列表信息，按照利润主键降序排列<br>
	 * @param vo
	 */
	@SuppressWarnings("unchecked")
	public List<ProfitSheet> query(ProfitSheetVo vo){
		
		String periodId = vo.getPeriodId();
		if(Strings.isNullOrEmpty(periodId))return Collections.EMPTY_LIST;
		
		Sort sort = new Sort(Direction.ASC, "number");
		
		return profitSheetRepo.findByPeriodId(periodId, sort);
	}
	
	
	/**
	 * 单个利润实体转换为vo
	 * @param entity
	 * @return
	 */
	@Override
	public ProfitSheetVo getVo(ProfitSheet entity){
		if(entity == null)
			return null;
		ProfitSheetVo vo = new ProfitSheetVo();
		vo.setItem(entity.getItem());
		vo.setNumber(entity.getNumber());
		vo.setFormula(entity.getFormula());
		vo.setUpdateTime(DateUtilTools.date2String(entity.getUpdateTime(), DATE_TIME));
		vo.setFid(entity.getFid());
		
		if(entity.getCurrentPeriodAmount()!=null){
			vo.setCurrentPeriodAmount(NumberUtil.scale(entity.getCurrentPeriodAmount(),2));
		}
		
		if(entity.getLastPeriodAmount()!=null){
			vo.setLastPeriodAmount(NumberUtil.scale(entity.getLastPeriodAmount(),2));
		}
		
		return vo;
	}
	
	/**
	 * 新增/编辑利润
	 * @param vo
	 */
	public RequestResult save(ProfitSheetVo vo) {
		
		RequestResult check = checkValidation(vo);
		if(!check.isSuccess()){
			return check;
		}
		FiscalPeriod period = periodService.get(vo.getPeriodId());
		ProfitSheet entity = new ProfitSheet();
		if(StringUtils.isBlank(vo.getFid())){
			
			entity.setOrg(SecurityUtil.getCurrentOrg());
			entity.setUpdateTime(new Date());
			entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
			entity.setDept(SecurityUtil.getCurrentDept());
			entity.setPeriod(period);
			
		}else {
			entity = profitSheetRepo.findOne(vo.getFid());
		}
		
		entity.setItem(vo.getItem());
		entity.setNumber(vo.getNumber());
		entity.setFormula(vo.getFormula());
		entity.setUpdateTime(Calendar.getInstance().getTime());
		
		profitSheetRepo.save(entity);
		
		String newUpdateTime = DateUtilTools.time2String(entity.getUpdateTime());
		return buildSuccessRequestResult(newUpdateTime);
	}
	
	/**
	 * 复制属性
	 * @return
	 */
	private ProfitSheet copyProperties(ProfitSheetFormula formula,FiscalAccount account,
			Organization org, Organization dept){
		ProfitSheet sheet = new ProfitSheet();
		sheet.setFormula(formula.getFormula());
		sheet.setItem(formula.getItem());
		sheet.setNumber(formula.getNumber());
		sheet.setOrg(org);
		sheet.setFiscalAccount(account);
		sheet.setDept(dept);
		return sheet;
	}
	
	/**
	 * 复制属性
	 * @return
	 */
	private ProfitSheetFormula copyProperties(ProfitSheet sheet, FiscalAccount account,
			Organization org, Organization dept){
		ProfitSheetFormula formula = new ProfitSheetFormula();
		formula.setFormula(sheet.getFormula());
		formula.setItem(sheet.getItem());
		formula.setNumber(sheet.getNumber());
		formula.setOrg(org);
		formula.setFiscalAccount(account);
		formula.setDept(dept);
		return formula;
	}
	
	/**
	 * 计算资产负债表
	 */
	@Transactional
	public RequestResult saveComputeFormula(String periodId){
		
		FiscalPeriod period = periodService.get(periodId);
		List<ProfitSheet> sheets = profitSheetRepo.findByPeriodId(periodId);
		
		Map<String,BigDecimal> pcache = Maps.newHashMap();
		Map<String,BigDecimal> ycache = Maps.newHashMap();
		
		for(ProfitSheet sheet:sheets){
			
			String oldFormula = sheet.getFormula();
			if(oldFormula.contains(ExpressionUtils.DS_FUNCTION)){
				sheet.setFormula(recurseDSFromula(oldFormula, sheets));
			}
			
			sheet = saveSheetValue(sheet, period, pcache, ycache);
			
			sheet.setFormula(oldFormula);
			
			profitSheetRepo.save(sheet);
		}
		
		return buildSuccessRequestResult();
	}
	
	/**
	 * 把当前公式写入资产负债公式表中
	 * @return
	 */
	@Transactional
	public RequestResult saveSheetFormula(String periodId){
		
		FiscalAccount account = SecurityUtil.getFiscalAccount();
		Organization dept = SecurityUtil.getCurrentDept();
		Organization org = SecurityUtil.getCurrentOrg();
		String accId = account.getFid();
		Date updateTime = Calendar.getInstance().getTime();
		
		formulaService.deleteByAccountId(accId);
		
		List<ProfitSheet> sheets = profitSheetRepo.findByPeriodId(periodId);
		
		
		for(ProfitSheet sheet:sheets){
			ProfitSheetFormula formula = copyProperties(sheet, account, org, dept);
			formula.setUpdateTime(updateTime);
			formulaService.save(formula);
		}
		
		return buildSuccessRequestResult();
	}
	
	
	/**
	 * 从利润表公式表写入当前公式数据列中
	 * @return
	 */
	@Transactional
	public RequestResult saveResumeFormula(String periodId){
		
		//1、删除资产负债表数据
		deleteByPeriodId(periodId);
		
		//2、从公式表恢复数据
		saveNewProfitSheet(periodId, false);
		
		return buildSuccessRequestResult();
	}
	
	/**
	 * 根据会计期间的ID删除利润表数据
	 * @param periodId
	 */
	public void deleteByPeriodId(String periodId) {
		List<ProfitSheet> sheets = profitSheetRepo.findByPeriodId(periodId);
		profitSheetRepo.delete(sheets);
	}

	@Override
	public CrudRepository<ProfitSheet, String> getRepository() {
		return profitSheetRepo;
	}
}
