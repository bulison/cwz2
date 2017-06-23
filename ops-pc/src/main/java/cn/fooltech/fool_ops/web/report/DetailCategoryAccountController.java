package cn.fooltech.fool_ops.web.report;

import cn.fooltech.fool_ops.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.common.entity.ResultObject;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalPeriod;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalAccountingSubjectService;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalPeriodService;
import cn.fooltech.fool_ops.domain.fiscal.vo.FiscalAccountingSubjectVo;
import cn.fooltech.fool_ops.domain.report.service.DetailCategoryAccountService;
import cn.fooltech.fool_ops.domain.report.vo.DetailCategoryAccountVo;
import cn.fooltech.fool_ops.domain.report.vo.SysReportQueryVo;
import net.sf.json.JSONObject;

/**
 * <p>明细分类账报表-网页控制器</p>
 * @author rqh
 * @version 1.0
 * @date 2016年2月3日
 */
@Controller
@RequestMapping("/detailCategoryAccount/report")
public class DetailCategoryAccountController {

	/**
	 * 明细分类账报表网页服务类
	 */
	@Autowired
	private DetailCategoryAccountService accountWebService;
	
	@Autowired
	private FiscalAccountingSubjectService subjectService;
	
	@Autowired
	private FiscalPeriodService periodService;
	
	/**
	 * 管理界面
	 * @return
	 */
	@RequestMapping("/manage")
	public String manage(Model model){
		FiscalAccountingSubjectVo subject = subjectService.findDefaultSubjectVo();
		model.addAttribute("subject", subject);
		
		FiscalPeriod period = periodService.getFirstNotCheck();
		if(period != null){
			model.addAttribute("period", periodService.getVo(period));
		}
		return "/report/fiscal/subsidiaryLedger/report";
	}
	
	/**
	 * 分页查询
	 * @param vo
	 * @param accountVo
	 * @param paramater
	 * @param model
	 * @return
	 */
	@RequestMapping("/query")
	@SuppressWarnings("rawtypes")
	public String query(SysReportQueryVo vo, DetailCategoryAccountVo accountVo, PageParamater paramater, Model model){
		ResultObject result = accountWebService.qurey(vo, accountVo, paramater);
		model.addAttribute("result", JsonUtil.toJsonString(result));
		return "/report/fiscal/subsidiaryLedger/reportResult";
	}
	
	/**
	 * 调试界面
	 * @return
	 */
	@RequestMapping("/test")
	public String test(Model model) {
		FiscalAccountingSubjectVo subject = subjectService.findDefaultSubjectVo();
		model.addAttribute("subject", subject);
		return "/report/reportTest";
	}
	
	/**
	 * 获取上一个、下一个科目(用于调试)
	 * @param accountVo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getOtherSubject")
	public JSONObject getOtherSubject(DetailCategoryAccountVo accountVo){
		FiscalAccountingSubject subject = null;
		
		String curSubjectId = accountVo.getCurSubjectId();
		String startCode = accountVo.getSubjectStartCode();
		String endCode = accountVo.getSubjectEndCode();
		Integer level = accountVo.getSubjectLevel();
		
		if(accountVo.getOperationFlag() == 1){
			subject = subjectService.getNextSubject(curSubjectId, startCode, endCode, level);
		}
		else{
			subject = subjectService.getLastSubject(curSubjectId, startCode, endCode, level);
		}
		JSONObject jsonObject = new JSONObject();
		if(subject != null){
			jsonObject.accumulate("subjectId", subject.getFid());
			jsonObject.accumulate("subjectName", subject.getName());
			jsonObject.accumulate("subjectCode", subject.getCode());
		}
		return jsonObject;
	}
	
}
