package cn.fooltech.fool_ops.web.wage;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.print.entity.PrintTemp;
import cn.fooltech.fool_ops.domain.print.service.PrintTempService;
import cn.fooltech.fool_ops.domain.sysman.service.OrgService;
import cn.fooltech.fool_ops.domain.wage.entity.Wage;
import cn.fooltech.fool_ops.domain.wage.entity.WageFormula;
import cn.fooltech.fool_ops.domain.wage.service.WageDetailService;
import cn.fooltech.fool_ops.domain.wage.service.WageFormulaService;
import cn.fooltech.fool_ops.domain.wage.service.WageService;
import cn.fooltech.fool_ops.domain.wage.vo.WageDetailVo;
import cn.fooltech.fool_ops.domain.wage.vo.WageFormulaVo;
import cn.fooltech.fool_ops.domain.wage.vo.WageVo;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.web.base.BaseController;

/**
 * <p>
 * 工资网页控制器类
 * </p>
 * 
 * @author xjh
 * @version 1.0
 * @date 2015-12-21 10:05:05
 */
@Controller
@RequestMapping(value = "/wage")
public class WageController extends BaseController{


	@Autowired
	private WageService wageService;

	@Autowired
	private WageDetailService detailService;

	@Autowired
	private WageFormulaService formulaService;
	
	/**
	 * 机构网页服务类
	 */
	@Autowired
	private OrgService orgService;
    
	/**
	 * 打印模板配置服务表
	 */
	@Autowired
	private PrintTempService printTempWebService;
	/**
	 * 工资列表信息页面<br>
	 */
	@RequestMapping("/listWage")
	public String listWage(ModelMap model) {
		return "/wage/wage";
	}

	/**
	 * 新增/编辑工资页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/editWage")
	public String editWage(String fid, ModelMap model) {

		List<WageFormulaVo> formulas = wageService.getExistFormula(fid);;
		model.put("titles", formulas);
		if(!Strings.isNullOrEmpty(fid)){
			WageVo wage = wageService.getByFid(fid);
			model.put("wage", wage);
		}

		return "/wage/editWage";
	}
	
	/**
	 * 工资打印条页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/print")
	public String print(WageVo vo, ModelMap model,
						Short isView) {
		List<WageFormulaVo> formulas = wageService.getExistViewFormula(null);;
		model.put("titles", formulas);
		return "/wage/print/print";
	}

	/**
	 * 工资发放页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/issue")
	public String issue(WageVo vo, ModelMap model) {
		List<WageFormulaVo> formulas = wageService.getExistViewFormula(null);;
		model.put("titles", formulas);
		return "/wage/issue/manage";
	}
	
	/**
	 * 工资表汇总页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/summary")
	public String summary(WageVo vo, ModelMap model) {
		List<WageFormulaVo> formulas = wageService.getExistViewFormula(null);;
		model.put("titles", formulas);
		return "/wage/summary/manage";
	}
	
	/**
	 * 工资明细列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/detailList")
	@ResponseBody
	public JSONArray detailList(@RequestParam String wageId) {
		
		List<WageFormulaVo> formulas = formulaService.getByAccountId(WageFormula.VIEW);

		List<List<WageDetailVo>> process = detailService.queryDetail(wageId, null);

		JSONArray array = processDetails(process, formulas);

		return array;
	}
	
	/**
	 * 组装json数据
	 * @param process
	 * @param formulas
	 * @return
	 */
	private JSONArray processDetails(List<List<WageDetailVo>> process, List<WageFormulaVo> formulas){
		JSONArray array = new JSONArray();
		JSONObject json = null;
		for (List<WageDetailVo> tempList : process) {
			int i = 0;
			for (WageFormulaVo formulaVo : formulas) {

				WageDetailVo find = findDetail(formulaVo, tempList);
				if (find != null) {
					if (i == 0) {
						json = new JSONObject();
						json.put("memberId", find.getMemberId());
						json.put("memberCode", find.getMemberCode());
						json.put("memberName", find.getMemberName());
						json.put("memberDept", find.getMemberDept());
						json.put("detailId", find.getFid());
						json.put("date", find.getWageDate());
					}
					json.put(formulaVo.getFid(), find.getValue());
				} else {
					json.put(formulaVo.getFid(), "0");
				}
				i++;
			}
			array.add(json);
		}
		return array;
	}

	private WageDetailVo findDetail(WageFormulaVo formulaVo,
			List<WageDetailVo> details) {
		for (WageDetailVo detail : details) {
			if (detail.getFormulaId().equals(formulaVo.getFid()))
				return detail;
		}
		return null;
	}

	/**
	 * 工资列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public PageJson list(WageVo wageVo, PageParamater pageParamater) {
		Page<WageVo> page = wageService.query(wageVo, pageParamater);
		PageJson pageJson = new PageJson(page);
		return pageJson;
	}
	
	/**
	 * 工资列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/listAll")
	@ResponseBody
	public List<WageVo> listAll(WageVo wageVo, PageParamater pageParamater) {
		pageParamater.setRows(Integer.MAX_VALUE);
		Page<WageVo> page = wageService.query(wageVo, pageParamater);
		return page.getContent();
	}


	/**
	 * 保存工资
	 * 
	 * @return
	 */
	@RequestMapping(value = "/save")
	@ResponseBody
	public RequestResult save(WageVo vo) {
		try {
			RequestResult result = wageService.save(vo);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return new RequestResult(RequestResult.RETURN_FAILURE, "发生异常，请稍后再试");
		}
	}
	
	/**
	 * 引入工资
	 */
	@RequestMapping(value = "/saveByImport")
	@ResponseBody
	public RequestResult saveByImport(WageVo vo) {
		return wageService.saveByImport(vo);
	}

	/**
	 * 删除工资
	 * 
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public RequestResult delete(WageVo vo) {
		return wageService.delete(vo.getFid());
	}

	/**
	 * 通过审核
	 */
	@RequestMapping(value = "/passAudit")
	@ResponseBody
	public RequestResult passAudit(String fid) {
		return wageService.passAudit(fid);
	}

	/**
	 * 反审核
	 */
	@RequestMapping(value = "/cancleAudit")
	@ResponseBody
	public RequestResult cancleAudit(String fid) {
		return wageService.cancleAudit(fid);
	}
	
	/**
	 * 工资打印条
	 * @return
	 */
	@RequestMapping(value = "/wageReport")
	@ResponseBody
	public String wageReport(WageVo vo){
		List<List<List<WageDetailVo>>> details = wageService.queryAllDetails(vo);
		List<WageFormulaVo> formulas = formulaService.getByAccountId(WageFormula.VIEW);
		
		Map<String, Object> sum = Maps.newHashMap();
		
		for(WageFormulaVo formula:formulas){
			sum.put(formula.getFid(), BigDecimal.ZERO);
		}
		
		JSONArray array = new JSONArray();
		for(List<List<WageDetailVo>> oneWage:details){
			array.addAll(processDetails(oneWage, formulas));
			
			//计算合计数据
			for(List<WageDetailVo> oneMember:oneWage){
				
				for(WageDetailVo oneDetail:oneMember){
					Object obj = sum.get(oneDetail.getFormulaId());
					
					BigDecimal value = BigDecimal.ZERO;
					if(obj!=null){
						value = (BigDecimal)obj;
						value = value.add(oneDetail.getValue());
					}else{
						value = oneDetail.getValue();
					}
					sum.put(oneDetail.getFormulaId(), value);
				}
			}
		}
		
		sum.put("memberDept", "合计");
		JSONObject total = JSONObject.fromObject(sum);
		array.add(total);
		return array.toString();
	}
	
	/**
	 * 工资发放
	 * @return
	 */
	@RequestMapping(value = "/wagePublish")
	@ResponseBody
	public String wagePublish(WageVo vo){
		List<List<List<WageDetailVo>>> details = wageService.queryAllDetails(vo);
		List<WageFormulaVo> formulas = formulaService.getByAccountId(WageFormula.VIEW);
		
		Map<String, Object> sum = Maps.newHashMap();
		
		for(WageFormulaVo formula:formulas){
			sum.put(formula.getFid(), BigDecimal.ZERO);
		}
		
		JSONArray array = new JSONArray();
		for(List<List<WageDetailVo>> oneWage:details){
			if(oneWage.size()==0)continue;
			array.addAll(processDetails(oneWage, formulas));
			
			Map<String, Object> deptSum = Maps.newHashMap();
			
			//计算合计数据
			for(List<WageDetailVo> oneMember:oneWage){
				
				for(WageDetailVo oneDetail:oneMember){
					
					//所有合计
					Object obj = sum.get(oneDetail.getFormulaId());
					BigDecimal value = BigDecimal.ZERO;
					if(obj!=null){
						value = (BigDecimal)obj;
						value = value.add(oneDetail.getValue());
					}else{
						value = oneDetail.getValue();
					}
					sum.put(oneDetail.getFormulaId(), value);
					
					//部门合计
					Object deptObj = deptSum.get(oneDetail.getFormulaId());
					BigDecimal deptValue = BigDecimal.ZERO;
					if(deptObj!=null){
						deptValue = (BigDecimal)deptObj;
						deptValue = deptValue.add(oneDetail.getValue());
					}else{
						deptValue = oneDetail.getValue();
					}
					deptSum.put(oneDetail.getFormulaId(), deptValue);
				}
			}
			
			deptSum.put("memberDept", "部门合计");
			JSONObject deptTotal = JSONObject.fromObject(deptSum);
			array.add(deptTotal);
		}
		
		sum.put("memberDept", "所有合计");
		JSONObject total = JSONObject.fromObject(sum);
		array.add(total);
		return array.toString();
	}
	
	
	/**
	 * 工资汇总
	 * @return
	 */
	@RequestMapping(value = "/wageSummary")
	@ResponseBody
	public String wageSummary(WageVo vo){
		
		//所有<工资单<一行记录<>>>
		List<List<List<WageDetailVo>>> details = wageService.queryAllDetails(vo);
		List<WageFormulaVo> formulas = formulaService.getByAccountId(WageFormula.VIEW);
		
		JSONArray array = new JSONArray();
		Map<String, BigDecimal> allSum = Maps.newHashMap();
		
		for (WageFormulaVo formulaVo : formulas) {
			allSum.put(formulaVo.getFid(), BigDecimal.ZERO);
		}
		
		int totalCount = 0;
		for(List<List<WageDetailVo>> oneWage:details){
			
			if(oneWage.size()==0)continue;
			if(oneWage.get(0).size()==0)continue;
			
			WageDetailVo detail = oneWage.get(0).get(0);
			String deptId = detail.getDeptId();
			String deptName = detail.getDeptName();
			String wageDate = detail.getWageDate();
			
			//Map<部门, Map<公式,值>>>
			Map<String, Map<String, BigDecimal>> deptSum = Maps.newHashMap();
			
			for (WageFormulaVo formulaVo : formulas) {
				Map<String, BigDecimal> temp = Maps.newHashMap();
				temp.put(formulaVo.getFid(), BigDecimal.ZERO);
				deptSum.put(deptId, temp);
			}
			
			int memberCount = 0;
			for(List<WageDetailVo> oneMember:oneWage){
				
				for(WageDetailVo oneDetail:oneMember){
					Map<String, BigDecimal> mapValue = deptSum.get(oneDetail.getDeptId());
					
					BigDecimal value = BigDecimal.ZERO;
					if(mapValue!=null){
						value = mapValue.get(oneDetail.getFormulaId());
						if(value!=null){
							value = value.add(oneDetail.getValue());
						}else{
							value = oneDetail.getValue();
						}
						mapValue.put(oneDetail.getFormulaId(), value);
					}else{
						mapValue = Maps.newHashMap();
						value = oneDetail.getValue();
						mapValue.put(oneDetail.getFormulaId(), value);
						deptSum.put(oneDetail.getDeptId(), mapValue);
					}
					
					BigDecimal allValue = allSum.get(oneDetail.getFormulaId());
					if(allValue!=null){
						allValue = allValue.add(oneDetail.getValue());
						allSum.put(oneDetail.getFormulaId(), allValue);
					}
				}
				memberCount++;
			}
			
			Set<String> keys = deptSum.keySet();
			for(String key:keys){
				JSONObject json = new JSONObject();
				json.put("deptName", deptName);//部门名称
				json.put("deptId", key);//部门ID
				json.put("wageDate", wageDate);//月份
				json.put("memberCount", memberCount);
				json.putAll(deptSum.get(key));
				array.add(json);
			}
			totalCount+=memberCount;
		}
		
		JSONObject json = new JSONObject();
		json.put("deptName", "全部总计");
		json.put("memberCount", totalCount);
		json.putAll(allSum);
		array.add(json);
		return array.toString();
	}
	
	
	/**
	 * 工资统计
	 * @return
	 */
	@RequestMapping(value = "/wageStat")
	@ResponseBody
	public String wageStat(WageVo vo){
		
		String year = vo.getYear();
		if(StringUtils.isBlank(year)){
			year = DateUtilTools.date2String(Calendar.getInstance().getTime(), "yyyy");
			vo.setYear(year);
		}
		//所有<工资单<一行记录<>>>
		List<List<List<WageDetailVo>>> details = wageService.queryAllDetails(vo);
		List<WageFormulaVo> formulas = formulaService.getByAccountId(WageFormula.VIEW);
		
		List<String> months = wageService.queryAllMonth(year, vo.getDeptId());
		
		JSONArray array = new JSONArray();
		
		//Map<公式ID,Map<月份,值>>
		Map<String, Map<String, BigDecimal>> monthSum = Maps.newLinkedHashMap();
		Map<String, BigDecimal> allSum = Maps.newHashMap();
		
		//初始化数据
		for(String month:months){
			for (WageFormulaVo formulaVo : formulas) {
				Map<String, BigDecimal> temp = Maps.newHashMap();
				temp.put(month, BigDecimal.ZERO);
				monthSum.put(formulaVo.getFid(), temp);
			}
			allSum.put(month, BigDecimal.ZERO);
		}
		
		for(List<List<WageDetailVo>> oneWage:details){
			
			for(List<WageDetailVo> oneMember:oneWage){
				
				for(WageDetailVo oneDetail:oneMember){
					Map<String, BigDecimal> mapValue = monthSum.get(oneDetail.getFormulaId());
					
					BigDecimal value = BigDecimal.ZERO;
					if(mapValue!=null){
						value = mapValue.get(oneDetail.getWageDate());
						if(value!=null){
							value = value.add(oneDetail.getValue());
						}else{
							value = oneDetail.getValue();
						}
						mapValue.put(oneDetail.getWageDate(), value);
					}else{
						mapValue = Maps.newHashMap();
						value = oneDetail.getValue();
						mapValue.put(oneDetail.getWageDate(), value);
						monthSum.put(oneDetail.getFormulaId(), mapValue);
					}
					
					BigDecimal allValue = allSum.get(oneDetail.getWageDate());
					allValue = allValue.add(oneDetail.getValue());
					allSum.put(oneDetail.getWageDate(), allValue);
				}
			}
		}
		
		Set<String> keys = monthSum.keySet();
		
		for(String key:keys){
			WageFormulaVo formula = findFormula(key, formulas);

			if(formula.getIsView()==WageFormula.NOT_VIEW)continue;

			JSONObject json = new JSONObject();
			json.put("columnName", formula.getColumnName());//公式名称
			json.putAll(monthSum.get(key));
			array.add(json);
		}
		
		/*JSONObject total = new JSONObject();
		total.put("columnName", "全部总计");
		total.putAll(allSum);
		array.add(total);*/
		return array.toString();
	}
	
	/**
	 * 查找公式
	 * @param formulaId
	 * @param formulas
	 * @return
	 */
	private WageFormulaVo findFormula(String formulaId, List<WageFormulaVo> formulas){
		for(WageFormulaVo vo:formulas){
			if(vo.getFid().equals(formulaId)){
				return vo;
			}
		}
		return formulaService.getByFid(formulaId);
	}
	
	/**
	 * 工资统计页面（根据年份获取月份，没有年份参数则取当前年份）
	 * @return
	 */
	@RequestMapping("/wageMonths")
	public String wageMonths(ModelMap model){
		return "/wage/stat/manage";
	}
	
	/**
	 * 工资统计页面（根据年份获取月份，没有年份参数则取当前年份，JSON格式）
	 * @param year 年份
	 * @param deptId 部门Id
	 * @return
	 */
	@RequestMapping("/wageMonthsJson")
	@ResponseBody
	public List<String> wageMonthsJson(String year, String deptId){
		if(StringUtils.isBlank(year)){
			year = DateUtilTools.date2String(Calendar.getInstance().getTime(), "yyyy");
		}
		return wageService.queryAllMonth(year, deptId);
	}
	
	/**
	 * 打印工资打印条
	 * @return
	 */
	@RequestMapping("wagePrinter")
	public String printWageReport(WageVo vo, ModelMap model,String code){
		String data = wageReport(vo);
		List<WageFormulaVo> formulas = wageService.getExistFormula(null);
		
		model.put("titles", formulas);
		model.put("data", data);
		model.put("org", orgService.getOrg());
		PrintTemp temp = printTempWebService.getByOrgId(SecurityUtil.getCurrentOrgId(),code);
		if (temp != null) {
			String printUrl = temp.getPrintTempUrl();
			if (temp.getPageRow() != null) {
				model.addAttribute("pageRow", temp.getPageRow());
			}
			if (StringUtils.isNotBlank(printUrl)) {
				return printUrl;
			}
			
		}
		return "/wage/print/printer";
	}
	
	/**
	 * 打印工资发放
	 * @return
	 */
	@RequestMapping("wageIssuePrinter")
	public String printWagePublish(WageVo vo, ModelMap model){
		String data = wagePublish(vo);
		List<WageFormulaVo> formulas = wageService.getExistFormula(null);
		
		model.put("titles", formulas);
		model.put("data", data);
		model.put("org", orgService.getOrg());
		return "/wage/issue/printer";
	}
	
	/**
	 * 打印工资汇总
	 * @return
	 */
	@RequestMapping("wageStatPrinter")
	public String wageStatPrinter(WageVo vo, ModelMap model){
		String data = wageStat(vo);
		List<WageFormulaVo> formulas = wageService.getExistFormula(null);
		
		model.put("titles", formulas);
		model.put("data", data);
		model.put("org", orgService.getOrg());
		return "/wage/stat/printer";
	}
	
	/**
	 * 打印工资表汇总
	 * @return
	 */
	@RequestMapping("wageSumPrinter")
	public String wageSumPrinter(WageVo vo, ModelMap model){
		String data = wageSummary(vo);
		List<WageFormulaVo> formulas = wageService.getExistFormula(null);
		
		model.put("titles", formulas);
		model.put("data", data);
		model.put("org", orgService.getOrg());
		return "/wage/summary/printer";
	}
}