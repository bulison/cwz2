package cn.fooltech.fool_ops.web.fiscal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.fiscal.entity.CarryForwardProfitLoss;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.fiscal.service.CarryForwardProfitLossService;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalAccountingSubjectService;
import cn.fooltech.fool_ops.domain.fiscal.vo.CarryForwardProfitLossVo;
import cn.fooltech.fool_ops.domain.fiscal.vo.FiscalAccountingSubjectVo;


/**
 * <p>结转损益科目网页控制器</p>
 * @author rqh
 * @version 1.0
 * @date 2016年2月16日
 */
@Controller
@RequestMapping("/profitLoss")
public class CarryForwardProfitLossController {
	
	@Autowired
	private FiscalAccountingSubjectService subjectService;
	
	/**
	 * 结转损益科目网页服务类
	 */
	@Autowired
	private CarryForwardProfitLossService profitLossService;
	
	
	/**
	 * 结转损益管理界面
	 * @return
	 */
	@RequestMapping(value = "/manage")
	public String manage(Model model){
		FiscalAccountingSubject dfInSubject=subjectService.getFirstLeafByCode("4103"); //本年利润科目
		if(dfInSubject != null ){
			model.addAttribute("dfInSubject", dfInSubject);
		}
		
		StringBuffer syIds = new StringBuffer(); //损益科目的ID
		StringBuffer syNames = new StringBuffer(); //损益科目的名称
		List<FiscalAccountingSubjectVo> sySubjects = subjectService.getSubjectByType(
				FiscalAccountingSubject.TYPE_SY, FiscalAccountingSubject.FLAG_CHILD); //损益科目集合
		for(int i=0; i<sySubjects.size(); i++){
			FiscalAccountingSubjectVo sySubject = sySubjects.get(i);
			syIds.append(sySubject.getFid());
			syNames.append(sySubject.getName());
			if(i < sySubjects.size() - 1){
				syIds.append(",");
				syNames.append(",");
			}
		}
		model.addAttribute("syIds", syIds.toString());
		model.addAttribute("syNames", syNames.toString());
		return "/report/carryForwardProfitLoss/manage";
	}
	
	/**
	 * 分页查询
	 * @param paramater
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/query")
	public PageJson query(PageParamater paramater){
		Page<CarryForwardProfitLossVo> page = profitLossService.query(paramater, CarryForwardProfitLoss.TYPE_PROFIT_LOSS);
		return new PageJson(page);
	}
	
	/**
	 * 新增
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/save")
	public RequestResult save(CarryForwardProfitLossVo vo){
		vo.setType(CarryForwardProfitLoss.TYPE_PROFIT_LOSS);
		return profitLossService.save(vo);
	}
	
	/**
	 * 删除
	 * @param fid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public RequestResult delete(String fid){
		return profitLossService.delete(fid);
	}
	
	/**
	 * 删除所有记录
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteAll")
	public RequestResult deleteAll(){
		return profitLossService.deleteAll(CarryForwardProfitLoss.TYPE_PROFIT_LOSS);
	}
	
	/**
	 * 制作凭证
	 * @param vo
	 * @param flag 是否必须结转  1 是  0 否
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/makeVoucher")
	public RequestResult makeVoucher(CarryForwardProfitLossVo vo, @RequestParam(defaultValue = "0", required = false) int flag){
		vo.setType(CarryForwardProfitLoss.TYPE_PROFIT_LOSS);
		return profitLossService.makeVoucher(vo, flag);
	}
	
}
