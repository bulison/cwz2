package cn.fooltech.fool_ops.web.analysis;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import cn.fooltech.fool_ops.domain.flow.vo.PlanVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.analysis.service.CostAnalysisBillService;
import cn.fooltech.fool_ops.domain.analysis.vo.CostAnalysisBillVo;

/**
 * <p>
 * 成本分析网页控制器
 * </p>
 * 
 * @author cwz
 * @date 2016-12-21
 */
@RestController
@RequestMapping(value = "/api/costAnalysisBill")
public class CostAnalysisBillApiController {
	private Logger logger = LoggerFactory.getLogger(CostAnalysisBillApiController.class);
	@Autowired
	private CostAnalysisBillService service;

	@ResponseBody
	@GetMapping("/query")
	public Page<CostAnalysisBillVo> query(CostAnalysisBillVo vo, PageParamater paramater) {
		Page<CostAnalysisBillVo> page = service.query(vo, paramater);
		return page;
	}

	/**
	 * 新增、编辑
	 * 
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@PostMapping("/save")
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
	@DeleteMapping("/delete")
	public RequestResult delete(String fid) {
		return service.delete(fid);
	}
	/**
	 * 发布
	 * @param fid 主表id
	 */
	@PutMapping(value = "/issue")
	public RequestResult issue(String fid){
		return service.issue(fid);
	};
	/**
	 * 导出成本分析数据
	 * @param vo
	 * @param response
	 * @throws Exception
	 */
	@GetMapping(value = "/export")
	@ResponseBody
	public RequestResult export(CostAnalysisBillVo vo, HttpServletResponse response) throws Exception {
		//查询成本分析表
		Page<CostAnalysisBillVo> page = service.query(vo, new PageParamater());
		List<CostAnalysisBillVo> list = page.getContent();
		if(list.size()>0){
			RequestResult result = service.export(list, response);
			return result;
		}else{
			return new RequestResult(1, "没有成本分析数据!");
		}
	}

	/**
	 * 生成流程
	 * @param planVo
	 * @param planGoodsJson
	 * @return
	 */
	@PutMapping(value = "/genFlow")
	public RequestResult genFlow(PlanVo planVo, String planGoodsJson) {
		return service.genFlow(planVo, planGoodsJson);
	}
}
