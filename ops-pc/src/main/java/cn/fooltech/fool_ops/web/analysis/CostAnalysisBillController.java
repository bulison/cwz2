package cn.fooltech.fool_ops.web.analysis;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.analysis.service.CostAnalysisBillService;
import cn.fooltech.fool_ops.domain.analysis.vo.CostAnalysisBillVo;
import cn.fooltech.fool_ops.domain.basedata.service.TodayCostAnalysisService;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 成本分析网页控制器
 * </p>
 * 
 * @author cwz
 * @date 2016-12-21
 */
@Controller
@RequestMapping("/costAnalysisBill")
public class CostAnalysisBillController {
	private Logger logger = LoggerFactory.getLogger(CostAnalysisBillController.class);
	@Autowired
	private CostAnalysisBillService service;
	@Autowired
	public TodayCostAnalysisService todayCostAnalysisService;

	/**
	 * 成本分析管理界面
	 * 
	 * 运输路线分析管理界面
	 *
	 */
	@RequestMapping("/manage")
	public String manage(int type) {
		if(type == 1){
			return "/report/costAnalysis/manage";
		}else if(type == 2){
			return "/report/transportAnalysis/manage";
		}else{
			return null;
		}
	}

	@RequestMapping(value = "/otherCompany")
	public String otherCompany() {
		return "/report/costAnalysis/otherCompany";
	}
	
	@RequestMapping(value = "/process")
	public String proccess() {
		return "/report/costAnalysis/process";
	}
	
	/**
	 * 新增、编辑页面
	 * @param id 仓库单据ID
	 * @param model
	 * @return
	 */
	@RequestMapping("/edit")
	public String edit(String id,Model model){
		CostAnalysisBillVo vo = service.findById(id);
		model.addAttribute("obj", vo);
		return "/report/costAnalysis/details";  
	}

	@RequestMapping(value = "/query")
	@ResponseBody
	public Page<CostAnalysisBillVo> query(CostAnalysisBillVo vo, PageParamater paramater) {
		Page<CostAnalysisBillVo> page = service.query(vo, paramater);
		return page;
	}

	/**
	 * 折线图
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "/chart")
	@ResponseBody
	public  List<CostAnalysisBillVo> findChart(CostAnalysisBillVo vo) {
		List<CostAnalysisBillVo> list = service.findChart(vo);
		return list;
	}

	/**
	 * 新增、编辑
	 * 
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/save")
	public RequestResult save(CostAnalysisBillVo vo) {
		return service.save(vo);
	}

	/**
	 * 删除
	 * 
	 * @param fid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public RequestResult delete(String fid) {
		return service.delete(fid);
	}

	/**
	 * 导出成本分析数据
	 * @param vo 页面参数
	 */
	@RequestMapping(value = "/export")
	public void export(CostAnalysisBillVo vo, HttpServletResponse response) throws Exception {
		PageParamater paramater = new PageParamater();
		paramater.setStart(0);
		paramater.setRows(Integer.MAX_VALUE);
		if(Strings.isNullOrEmpty(vo.getBillDate())){
			vo.setBillDate(DateUtils.getCurrentDate());
		}
		//查询成本分析表
		Page<CostAnalysisBillVo> page = service.query(vo,paramater);

		List<CostAnalysisBillVo> list = page.getContent();
		service.export(list, response);
	}

	/**
	 * 发布
	 * @param fid 主表id
	 */
	@RequestMapping(value = "/issue")
	@ResponseBody
	public RequestResult issue(String fid){
		return service.issue(fid);
	}


	/**
	 * 生成当天成本分析表
	 * @return
	 */
	@RequestMapping(value = "/todayCostAnalysis")
	@ResponseBody
	public RequestResult todayCostAnalysis(){
		todayCostAnalysisService.genRoute();

		String accId = SecurityUtil.getFiscalAccountId();
		todayCostAnalysisService.analysis(accId);
		return new RequestResult();
	}


	/**
	 * 生成当天成本分析表
	 * @return
	 */
//	@RequestMapping(value = "/testTodayCostAnalysis")
//	@ResponseBody
//	public RequestResult testTodayCostAnalysis(){
//		todayCostAnalysisService.genRoute();
//		todayCostAnalysisService.analysis();
//		return new RequestResult();
//	}
}
