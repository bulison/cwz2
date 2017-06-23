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
import cn.fooltech.fool_ops.domain.report.entity.BalanceSheet;
import cn.fooltech.fool_ops.domain.report.entity.BalanceSheetFormula;
import cn.fooltech.fool_ops.domain.report.entity.FormulaVo;
import cn.fooltech.fool_ops.domain.report.entity.SheetFormula;
import cn.fooltech.fool_ops.domain.report.repository.BalanceSheetRepository;
import cn.fooltech.fool_ops.domain.report.vo.BalanceSheetVo;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.ExpressionUtils;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.VoFactory;

/**
 * <p>资产负债网页服务类</p>
 * @author xjh
 * @version 1.0
 * @date 2016-02-16 09:34:26
 */
@Service
public class BalanceSheetService extends BaseService<BalanceSheet, BalanceSheetVo, String> {
	
	private static final Logger logger = LoggerFactory.getLogger(BalanceSheetService.class);
	
	/**
	 * 资产负债服务类
	 */
	@Autowired
	private BalanceSheetRepository balanceSheetRepo;
	
	/**
	 * 资产负债公式服务类
	 */
	@Autowired
	private BalanceSheetFormulaService formulaService;
	
	/**
	 * 资产负债公式服务类
	 */
	@Autowired
	private ReportFormulaService rfService;
	
	/**
	 * 会计期间服务类
	 */
	@Autowired
	private FiscalPeriodService periodService;
	
	/**
	 * 先查询期间资产负债表有没有数据，没有则从默认公式表复制公式并计算写入到资产负债表中
	 */
	public List<BalanceSheet> saveInitBalanceSheet(String periodId, boolean autoCompute){
		
		if(Strings.isNullOrEmpty(periodId))return null;
		Long count = balanceSheetRepo.countByPeriodId(periodId);
		if(count!=null && count==0){
			return saveNewBalanceSheet(periodId, autoCompute);
		}
		return null;
	}
	
	/**
	 * 从负债公式表中创建数据
	 */
	@Transactional
	private List<BalanceSheet> saveNewBalanceSheet(String periodId, boolean autoCompute){
		FiscalAccount acc = SecurityUtil.getFiscalAccount();
		
		List<BalanceSheetFormula> formulas = formulaService.findByAccountId(acc.getFid());
		List<BalanceSheet> list = Lists.newArrayList();
		
		Map<String,BigDecimal> pcache = Maps.newHashMap();
		Map<String,BigDecimal> ycache = Maps.newHashMap();
		
		Organization org = SecurityUtil.getCurrentOrg();
		Organization dept = SecurityUtil.getCurrentDept();
		Date updateTime = Calendar.getInstance().getTime();
		FiscalPeriod period = periodService.get(periodId);
		
		for(BalanceSheetFormula formula:formulas){
			BalanceSheet newdata = copyProperties(formula, acc, org, dept);
			if(autoCompute){
				String oldAssetFormula = newdata.getAssetFormula();
				String oldDebitFormula = newdata.getDebitFormula();
				if(oldAssetFormula.contains(ExpressionUtils.DS_FUNCTION)){
					newdata.setAssetFormula(recurseDSFromula(oldAssetFormula, formulas));
				}
				if(oldDebitFormula.contains(ExpressionUtils.DS_FUNCTION)){
					newdata.setDebitFormula(recurseDSFromula(oldDebitFormula, formulas));
				}
				logger.info("当前计算行号:"+newdata.getAssetNumber());
				newdata = saveSheetValue(newdata, period, pcache, ycache);
				newdata.setAssetFormula(oldAssetFormula);
				newdata.setDebitFormula(oldDebitFormula);
			}
			newdata.setUpdateTime(updateTime);
			newdata.setPeriod(period);
			balanceSheetRepo.save(newdata);
			list.add(newdata);
		}
		return list;
	}
	
	/**
	 * 检查循环引用
	 * @return
	 */
	public boolean checkRecurseRef(String formula, Integer rowNum, List<BalanceSheet> list, List<Integer> refList){
		
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
	public String recurseDSFromula(String fromula, List<? extends SheetFormula> list){
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
	private String findFormulaByRow(String row, List<? extends SheetFormula> list, String fh){
		
		String formula = "";
		int rowi = Integer.parseInt(row);
		for(SheetFormula bsf:list){
			if(bsf.getAssetNumber()!=null && bsf.getAssetNumber()==rowi){
				if(Strings.isNullOrEmpty(bsf.getAssetFormula())){
					return "";
				}
				formula = bsf.getAssetFormula();
			}
			if(bsf.getDebitNumber()!=null && bsf.getDebitNumber()==rowi){
				if(Strings.isNullOrEmpty(bsf.getDebitFormula())){
					return "";
				}
				formula = bsf.getDebitFormula();
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
	public BalanceSheet saveSheetValue(BalanceSheet sheet, FiscalPeriod period, 
			final Map<String,BigDecimal> pcache, final Map<String,BigDecimal> ycache){
		
		Date perioddate = period.getEndDate();
		
		FiscalPeriod min = periodService.getMinPeriod(period);
		Date yearBegindate = min.getStartDate();
		
		String pfromula1 = null;
		String yfromula1 = null;
		String pfromula2 = null;
		String yfromula2 = null;
		
		List<String> items1 = Lists.newArrayList();
		List<String> items2 = Lists.newArrayList();
		if(!Strings.isNullOrEmpty(sheet.getAssetFormula())){
			ExpressionUtils.splitFormula(sheet.getAssetFormula(), items1);
			pfromula1 = sheet.getAssetFormula();
			yfromula1 = sheet.getAssetFormula();
		}
		if(!Strings.isNullOrEmpty(sheet.getDebitFormula())){
			ExpressionUtils.splitFormula(sheet.getDebitFormula(), items2);
			pfromula2 = sheet.getDebitFormula();
			yfromula2 = sheet.getDebitFormula();
		}
		
		initCache(items1, pcache, ycache, perioddate, yearBegindate);
		initCache(items2, pcache, ycache, perioddate, yearBegindate);
		
		pfromula1 = ExpressionUtils.replaceFormula(pfromula1, pcache);
		yfromula1 = ExpressionUtils.replaceFormula(yfromula1, ycache);
		pfromula2 = ExpressionUtils.replaceFormula(pfromula2, pcache);
		yfromula2 = ExpressionUtils.replaceFormula(yfromula2, ycache);
		
		// 编译表达式 
        BigDecimal pcalVal1 = ExpressionUtils.computeByAviatorEvaluator(pfromula1);
        BigDecimal yVal1 = ExpressionUtils.computeByAviatorEvaluator(yfromula1);
        BigDecimal pcalVal2 = ExpressionUtils.computeByAviatorEvaluator(pfromula2);
        BigDecimal yVal2 = ExpressionUtils.computeByAviatorEvaluator(yfromula2);
        
		sheet.setAssetPeriodEnd(pcalVal1);
		sheet.setAssetYearBegin(yVal1);
		sheet.setDebitPeriodEnd(pcalVal2);
		sheet.setDebitYearBegin(yVal2);
		
		return sheet;
	}
	
	/**
	 * 初始化公式缓存
	 */
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public void initCache(List<String> items,final Map<String,BigDecimal> pcache,
			final Map<String,BigDecimal> ycache, Date perioddate, Date yearBegindate){
		if(items!=null){
			for(String item:items){
				if(!pcache.containsKey(item)){
					BigDecimal periodEnd = getFormulaValue(item, perioddate, false);
					pcache.put(item, periodEnd);
				}
				if(!ycache.containsKey(item)){
					BigDecimal yearBegin = getFormulaValue(item, yearBegindate, true);
					ycache.put(item, yearBegin);
				}
			}
		}
	}
	
	/**
	 * 计算公式的值
	 * @param item
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public BigDecimal getFormulaValue(String item, Date date, boolean year){
		FormulaVo fvo = ExpressionUtils.parseFormula(item);
		
		
		String accId = SecurityUtil.getFiscalAccountId();
		Class clazz = ReportFormulaService.class;
		try {
			//1：函数参数；2：账套；3：时间点
			Method method = clazz.getMethod("get"+fvo.getName(), String.class, String.class, Date.class, Boolean.class);
			//method.setAccessible(true);
			Object result = method.invoke(rfService, fvo.getParamater(), accId, date, year);
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
	public RequestResult checkValidation(BalanceSheetVo sheet){
		
		List<BalanceSheet> datas = balanceSheetRepo.findByPeriodId(sheet.getPeriodId());
		
		BalanceSheet newobj = VoFactory.createValue(BalanceSheet.class, sheet);
		datas.add(newobj);
		
		if(!Strings.isNullOrEmpty(sheet.getAssetFormula())){
			String name = sheet.getAssetItem();
/*			List<String> items = Lists.newArrayList();
			ExpressionUtils.splitFormula(sheet.getAssetFormula(), items);
			for(String item:items){
				if(!ExpressionUtils.checkSingleFormula(item)){
					return buildFailRequestResult("["+name+"]非法公式");
				}
			}*/
			if(!ExpressionUtils.regularCheck(sheet.getAssetFormula())){
				return buildFailRequestResult("["+name+"]非法公式");
			}
			for(BalanceSheet bs:datas){
				List<Integer> refList = Lists.newArrayList();
				if(checkRecurseRef(bs.getAssetFormula(), bs.getAssetNumber(), datas, refList)){
					return buildFailRequestResult("["+name+"]公式重复引用");
				}
			}
		}
		if(!Strings.isNullOrEmpty(sheet.getDebitFormula())){
			String name = sheet.getDebitItem();
			/*List<String> items = Lists.newArrayList();
			boolean check2 = ExpressionUtils.splitFormula(sheet.getDebitFormula(), items);
			if(!check2)return buildFailRequestResult("["+name+"]非法公式");
			for(String item:items){
				if(!ExpressionUtils.checkSingleFormula(item)){
					return buildFailRequestResult("["+name+"]非法公式");
				}
			}*/
			if(!ExpressionUtils.regularCheck(sheet.getDebitFormula())){
				return buildFailRequestResult("["+name+"]非法公式");
			}
			for(BalanceSheet bs:datas){
				List<Integer> refList = Lists.newArrayList();
				if(checkRecurseRef(bs.getDebitFormula(), bs.getDebitNumber(), datas, refList)){
					return buildFailRequestResult("["+name+"]公式重复引用");
				}
			}
		}
		
		if(!Strings.isNullOrEmpty(sheet.getFid())){
			BalanceSheet bs = balanceSheetRepo.findOne(sheet.getFid());
			if(!checkUpdateTime(sheet.getUpdateTime(), bs.getUpdateTime())){
				return buildFailRequestResult("公式已被其他用户修改，请刷新再试");
			}
		}
		
		if(sheet.getAssetNumber().equals(sheet.getDebitNumber())){
			return buildFailRequestResult("资产行号和负债行号相同");
		}
		
		Integer assetNumber = sheet.getAssetNumber();
		Integer debitNumber = sheet.getDebitNumber();
		String periodId = sheet.getPeriodId();
		String excludeId = sheet.getFid();
		
		if(Strings.isNullOrEmpty(sheet.getFid())){
			long count1 = balanceSheetRepo.countByTag(1, periodId, excludeId, assetNumber, debitNumber);
			if(count1>0)return buildFailRequestResult("["+sheet.getAssetItem()+"]行号重复");
			
			long count2 = balanceSheetRepo.countByTag(2, periodId, excludeId, assetNumber, debitNumber);
			if(count2>0)return buildFailRequestResult("["+sheet.getDebitItem()+"]行号重复");
		}else{
			long count1 = balanceSheetRepo.countByTag(3, periodId, excludeId, assetNumber, debitNumber);
			if(count1>0)return buildFailRequestResult("["+sheet.getAssetItem()+"]行号重复");
			
			long count2 = balanceSheetRepo.countByTag(4, periodId, excludeId, assetNumber, debitNumber);
			if(count2>0)return buildFailRequestResult("["+sheet.getDebitItem()+"]行号重复");
		}
		
		return buildSuccessRequestResult();
	}
	
	
	/**
	 * 查询资产负债列表信息，按照资产负债主键降序排列<br>
	 * @param vo
	 */
	@SuppressWarnings("unchecked")
	public List<BalanceSheet> query(BalanceSheetVo vo){
		
		String periodId = vo.getPeriodId();
		if(Strings.isNullOrEmpty(periodId))return Collections.EMPTY_LIST;
		
		Sort sort = new Sort(Direction.ASC, "assetNumber");
		return balanceSheetRepo.findByPeriodId(periodId, sort);
	}
	
	
	/**
	 * 单个资产负债实体转换为vo
	 * @param entity
	 * @return
	 */
	@Override
	public BalanceSheetVo getVo(BalanceSheet entity){
		if(entity == null)
			return null;
		BalanceSheetVo vo = new BalanceSheetVo();
		vo.setAssetItem(entity.getAssetItem());
		vo.setAssetNumber(entity.getAssetNumber());
		vo.setAssetFormula(entity.getAssetFormula());
		vo.setDebitItem(entity.getDebitItem());
		vo.setDebitNumber(entity.getDebitNumber());
		vo.setDebitFormula(entity.getDebitFormula());
		vo.setUpdateTime(DateUtilTools.date2String(entity.getUpdateTime(), DATE_TIME));
		vo.setFid(entity.getFid());
		
		if(entity.getAssetPeriodEnd()!=null){
			vo.setAssetPeriodEnd(NumberUtil.scale(entity.getAssetPeriodEnd(),2));
		}
		if(entity.getAssetYearBegin()!=null){
			vo.setAssetYearBegin(NumberUtil.scale(entity.getAssetYearBegin(),2));
		}
		if(entity.getDebitPeriodEnd()!=null){
			vo.setDebitPeriodEnd(NumberUtil.scale(entity.getDebitPeriodEnd(),2));
		}
		if(entity.getDebitYearBegin()!=null){
			vo.setDebitYearBegin(NumberUtil.scale(entity.getDebitYearBegin(),2));
		}
		
		return vo;
	}
	
	/**
	 * 新增/编辑资产负债
	 * @param vo
	 */
	public RequestResult save(BalanceSheetVo vo) {
		
		RequestResult check = checkValidation(vo);
		if(!check.isSuccess()){
			return check;
		}
		
		FiscalPeriod period = periodService.get(vo.getPeriodId());
		BalanceSheet entity = new BalanceSheet();
		if(StringUtils.isBlank(vo.getFid())){
			
			entity.setOrg(SecurityUtil.getCurrentOrg());
			entity.setUpdateTime(new Date());
			entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
			entity.setDept(SecurityUtil.getCurrentDept());
			entity.setPeriod(period);
			
		}else {
			entity = balanceSheetRepo.findOne(vo.getFid());
		}
		
		entity.setAssetItem(vo.getAssetItem());
		entity.setAssetNumber(vo.getAssetNumber());
		entity.setAssetFormula(vo.getAssetFormula());
		entity.setDebitItem(vo.getDebitItem());
		entity.setDebitNumber(vo.getDebitNumber());
		entity.setDebitFormula(vo.getDebitFormula());
		entity.setUpdateTime(Calendar.getInstance().getTime());
		
		balanceSheetRepo.save(entity);
		
		//String newUpdateTime = DateUtilTools.time2String(entity.getUpdateTime());
		return buildSuccessRequestResult(getVo(entity));
	}
	
	/**
	 * 计算资产负债表
	 */
	@Transactional
	public RequestResult saveComputeFormula(String periodId){
		
		FiscalPeriod period = periodService.get(periodId);
		List<BalanceSheet> sheets = balanceSheetRepo.findByPeriodId(periodId);
		
		Map<String,BigDecimal> pcache = Maps.newHashMap();
		Map<String,BigDecimal> ycache = Maps.newHashMap();
		
		for(BalanceSheet sheet:sheets){
			
			String oldAssetFormula = sheet.getAssetFormula();
			String oldDebitFormula = sheet.getDebitFormula();
			if(oldAssetFormula.contains(ExpressionUtils.DS_FUNCTION)){
				sheet.setAssetFormula(recurseDSFromula(oldAssetFormula, sheets));
			}
			if(oldDebitFormula.contains(ExpressionUtils.DS_FUNCTION)){
				sheet.setDebitFormula(recurseDSFromula(oldDebitFormula, sheets));
			}
			
			sheet = saveSheetValue(sheet, period, pcache, ycache);
			
			sheet.setAssetFormula(oldAssetFormula);
			sheet.setDebitFormula(oldDebitFormula);
			
			balanceSheetRepo.save(sheet);
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
		
		List<BalanceSheet> sheets = balanceSheetRepo.findByPeriodId(periodId);
		
		
		for(BalanceSheet sheet:sheets){
			BalanceSheetFormula formula = copyProperties(sheet, account, org, dept);
			formula.setUpdateTime(updateTime);
			formulaService.save(formula);
		}
		
		return buildSuccessRequestResult();
	}
	
	/**
	 * 复制属性
	 * @return
	 */
	private BalanceSheet copyProperties(BalanceSheetFormula formula,FiscalAccount account,
			Organization org, Organization dept){
		BalanceSheet sheet = new BalanceSheet();
		sheet.setAssetFormula(formula.getAssetFormula());
		sheet.setAssetItem(formula.getAssetItem());
		sheet.setAssetNumber(formula.getAssetNumber());
		sheet.setDebitFormula(formula.getDebitFormula());
		sheet.setDebitItem(formula.getDebitItem());
		sheet.setDebitNumber(formula.getDebitNumber());
		sheet.setOrg(org);
		sheet.setFiscalAccount(account);
		sheet.setDept(dept);
		return sheet;
	}
	
	/**
	 * 复制属性
	 * @return
	 */
	private BalanceSheetFormula copyProperties(BalanceSheet sheet, FiscalAccount account,
			Organization org, Organization dept){
		BalanceSheetFormula formula = new BalanceSheetFormula();
		formula.setAssetFormula(sheet.getAssetFormula());
		formula.setAssetItem(sheet.getAssetItem());
		formula.setAssetNumber(sheet.getAssetNumber());
		formula.setDebitFormula(sheet.getDebitFormula());
		formula.setDebitItem(sheet.getDebitItem());
		formula.setDebitNumber(sheet.getDebitNumber());
		formula.setOrg(org);
		formula.setFiscalAccount(account);
		formula.setDept(dept);
		return formula;
	}
	
	/**
	 * 从资产负债公式表写入当前公式数据列中
	 * @return
	 */
	@Transactional
	public RequestResult saveResumeFormula(String periodId){
		
		//1、删除资产负债表数据
		deleteByPeriodId(periodId);
		
		//2、从公式表恢复数据
		saveNewBalanceSheet(periodId, false);
		
		return buildSuccessRequestResult();
	}
	
	/**
	 * 根据会计期间的ID删除资产负债表数据
	 * @param periodId
	 */
	public void deleteByPeriodId(String periodId) {
		List<BalanceSheet> sheets = balanceSheetRepo.findByPeriodId(periodId);
		balanceSheetRepo.delete(sheets);
	}

	@Override
	public CrudRepository<BalanceSheet, String> getRepository() {
		return balanceSheetRepo;
	}
}
