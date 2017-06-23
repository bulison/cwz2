package cn.fooltech.fool_ops.web.report;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.report.service.FiscalMultiColumnService;
import cn.fooltech.fool_ops.domain.report.vo.AuxiliaryAttrSimpleVo;
import cn.fooltech.fool_ops.domain.report.vo.FiscalAccountingSubjectSimpleVo;
import cn.fooltech.fool_ops.domain.report.vo.FiscalMultiColumnVo;


/**
 * <p>多栏明细账设置网页控制器</p>
 * @author rqh
 * @version 1.0
 * @date 2016年1月27日
 */
@Controller
@RequestMapping("/multiColumnSetting")
public class FiscalMultiColumnController {
	
	/**
	 * 多栏明细账设置网页服务类
	 */
	@Autowired
	private FiscalMultiColumnService multiColumnService;
	
	/**
	 * 获取科目信息(核算科目类别)
	 * @param subjectId 科目ID
	 */
	@ResponseBody
	@RequestMapping("/getSubjectMsg")
	public FiscalAccountingSubjectSimpleVo getSubjectMsg(String subjectId){
		return multiColumnService.getSubjectMsg(subjectId);
	}
	
	/**
	 * 获取设置的详细信息
	 * @param fid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getById")
	public FiscalMultiColumnVo getById(String fid){
		return multiColumnService.getById(fid);
	}
	
	/**
	 * 获取设置列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/list")
	public List<FiscalMultiColumnVo> list(){
		 return multiColumnService.list();
	}
	
	/**
	 * 新增、编辑
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/save")
	public RequestResult save(FiscalMultiColumnVo vo){
		multiColumnService.save(vo);
		return new RequestResult();
	}
	
	/**
	 * 删除
	 * @param fid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public RequestResult delete(String fid){
		multiColumnService.delete(fid);
		return new RequestResult();
	}
	
	/**
	 * 获取自动编排的数据
	 * @param subjectId 科目ID
	 * @param auxiliaryType 辅助属性类别: 1银行、2供应商、3销售商、4部门、5职员、6仓库、7项目，8货品
	 * @param level 层级数
	 * @param flag 是否自动编排  1 是 0 否
	 */
	@ResponseBody
	@RequestMapping("/autoArrange")
	public List<AuxiliaryAttrSimpleVo> autoArrange(String subjectId, Integer auxiliaryType, 
			@RequestParam(defaultValue="1", required = false) Integer level,  
			@RequestParam(defaultValue = "0", required = false) int flag){
		return multiColumnService.autoArrange(subjectId, auxiliaryType, level, flag);
	}
	
}
