package cn.fooltech.fool_ops.web.rest;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.component.core.filesystem.FileSystem;
import cn.fooltech.fool_ops.domain.common.service.AttachService;
import cn.fooltech.fool_ops.domain.common.vo.SimpleAttachVo;
import cn.fooltech.fool_ops.domain.flow.entity.FlowOperationRecord;
import cn.fooltech.fool_ops.domain.flow.entity.Plan;
import cn.fooltech.fool_ops.domain.flow.service.FlowAttachService;
import cn.fooltech.fool_ops.domain.flow.service.FlowOperationRecordService;
import cn.fooltech.fool_ops.domain.flow.service.PlanManagerService;
import cn.fooltech.fool_ops.domain.flow.service.PlanService;
import cn.fooltech.fool_ops.domain.flow.service.RankService;
import cn.fooltech.fool_ops.domain.flow.service.TaskService;
import cn.fooltech.fool_ops.domain.flow.vo.FlowOperationRecordVo;
import cn.fooltech.fool_ops.domain.flow.vo.PlanVo;
import cn.fooltech.fool_ops.utils.JsonUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * <p>计划控制层 </p>
 * @author cwz
 * @date 2017年6月13日
 */
@RestController
@RequestMapping(value = "/api/plan")
public class PlanResource extends AbstractBaseResource {

	private static final String NameSpace = "plan";
	@Autowired
	private PlanService planWebService;
	
	@Autowired
	private TaskService taskWebService;
	
	@Autowired
	FlowAttachService flowAttachService;
	
	@Autowired
	AttachService attachService;
	
	@Autowired
	FlowOperationRecordService frService;
	
	@Autowired
	RankService rankWebService;

	@Autowired
	private PlanManagerService planManagerService;
	
	@Autowired
	private FlowOperationRecordService operationRecordService;
	
//	@Qualifier("GFileSystem")
	@Autowired
	FileSystem fs;


	
	/**
	 * 计划管理界面查看该计划可下载的附件
	 * @return
	 */
	@GetMapping("/showAttach")
	public ResponseEntity<SimpleAttachVo> showAttach(String planId, HttpServletRequest request) {
		 List<SimpleAttachVo> list = flowAttachService.getFileMapByPlanId(planId);
		 return listReponse(list);
	}
	
	/**
	 * 获取计划信息
	 * @param fid
	 * @return
	 */
	
	@GetMapping("/getById")
	public PlanVo getById(String fid) throws Exception{
		Plan plan  = planWebService.get(fid);
		return planWebService.getVo(plan);
	}
	
	/**
	 * 分页查询
	 * @param vo
	 * @param paramater
	 * @return
	 */
	
	@GetMapping("/query")
	public ResponseEntity query(PlanVo vo, PageParamater paramater, String excludeId) throws Exception{
		 PageJson query = planWebService.query(vo, paramater,excludeId);
		 return ResponseEntity.ok().body(query);
	}
	
	/**
	 * 新增、编辑
	 * @param vo
	 * @return
	 */
	
	@PutMapping("/save")
	public ResponseEntity save(PlanVo vo, HttpServletRequest request) throws Exception{
		RequestResult result = planWebService.save(vo, true);
		return reponse(result);
	}
	
	/**
	 * 删除
	 * @param fid
	 * @return
	 */
	
	@DeleteMapping("/delete")
	public ResponseEntity delete(String fid) throws Exception{
		RequestResult result = planWebService.delete(fid);
		 return reponse(result);
	}
	
	/**
	 * 提交	
	 * @param id			计划id
	 * @param describe		描述
	 * @param type			是否处理：0-处理，1-不处理
	 * @return
	 * @throws Exception
	 */
	
	@PutMapping("/submit")
	public ResponseEntity submit(@RequestParam String id, String describe,Integer type) throws Exception{
		RequestResult result = planWebService.submit(id, describe,type);
		 return reponse(result);
	}
	
	/**
	 * 审核
	 * @param id 计划ID
	 * @param auditResult：0.审核通过 1.拒绝，返回发起人  3.终止
	 * @param rejectUserId：暂时不用
	 * @return
	 * @throws Exception
	 */
	
	@PutMapping("/audit")
	public ResponseEntity audit(@RequestParam String id, Integer auditResult, String rejectUserId, String describe) throws Exception{
		RequestResult result = planWebService.audit(id, auditResult, rejectUserId, describe);
		return reponse(result);
	}
	
	/**
	 * 终止
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	
	@PutMapping("/terminate")
	public ResponseEntity terminate(@RequestParam String id, String describe) throws Exception{
		RequestResult result = planWebService.terminate(id, describe);
		return reponse(result);
	}
	
	/**
	 * 完成计划
	 * @param id		计划id
	 * @param describe	完成描述
	 * @return
	 * @throws Exception
	 */
	
	@PutMapping("/complete")
	public ResponseEntity complete(String id,String describe) throws Exception{
		RequestResult result = planWebService.completePlan(id,describe);
		return reponse(result);
	}
	
	/**
	 * 查询计划类型
	 */
	
	@GetMapping("/queryPlanTypes")
	public String queryPlanTypes(){
		
		Map<String, String> map = Maps.newLinkedHashMap();
		map.put(Plan.TYPE_BTSW, "普通计划");
		map.put(Plan.TYPE_CGJH, "采购计划");
		map.put(Plan.TYPE_XSJH, "销售计划");
		map.put(Plan.TYPE_SCJH, "生产计划");
		map.put(Plan.TYPE_PDJH, "盘点计划");
		map.put(Plan.TYPE_BSJH, "报损计划");
		map.put(Plan.TYPE_CGTH, "采购退货计划");
		map.put(Plan.TYPE_XSTH, "销售退货计划");
		map.put(Plan.TYPE_DCJH, "调仓计划");
		map.put(Plan.TYPE_SCLL, "生产领料计划");
		map.put(Plan.TYPE_XSFL, "销售返利计划");
		map.put(Plan.TYPE_CGFL, "采购返利计划");
		map.put(Plan.TYPE_SCTL, "生产退料计划");
		map.put(Plan.TYPE_CPTK, "成品退库计划");
		
		JSONArray array = new JSONArray();
		for(String key:map.keySet()){
			JSONObject json = new JSONObject();
			json.put("id", key);
			json.put("text", map.get(key));
			array.add(json);
		}
		
		return array.toString();
	}


	/**
	 * 把计划保存到模板
	 * @param planId 计划ID
	 * @param planTplId 计划模板ID,可能是空值，即没有原模板
	 * @param taskIds 计划事件ID,是一个逗号字符串，按事件顺序排列
	 * @return
	 */
	
	@PutMapping("/savePlanToTemplate")
	public ResponseEntity savePlanToTemplate(@RequestParam String planId, String planTplId, String taskIds){
		RequestResult result = planWebService.savePlanToTemplate(planId, planTplId, taskIds);
		return reponse(result);
	}

	/**
	 * 流程汇总预计成本查询
	 */
	@GetMapping("/fee/list")
	public ResponseEntity queryPlanFeeAnalysis(@RequestParam String planId, String deliveryPlace, String goodsId,
												  String receiptPlace, String goodsSpecId, PageParamater page, Model model){
		PageJson pageJson = planManagerService.queryPlanTransportFee(planId, deliveryPlace, receiptPlace, goodsId, goodsSpecId, page);
//		model.addAttribute("result", JsonUtil.toJsonString(pageJson));
//		return "/report/reportResult";
		return ResponseEntity.ok().body(pageJson);
	}

	/**
	 * 流程汇总预计成本导出
	 */
//	@GetMapping("/fee/export")
//	public void exportPlanFeeAnalysis(String planId, String deliveryPlace, String goodsId, String receiptPlace,
//                                        String goodsSpecId, PageParamater page, HttpServletResponse response){
//        planManagerService.exportPlanTransportFee(planId, deliveryPlace, receiptPlace, goodsId, goodsSpecId, response, page);
//	}
	/**
	 * 获取计划的操作记录
	 * @param planId 计划ID
	 * @param triggerType 触发动作类型【完成:41】
	 * @return
	 */
	@GetMapping("/getRecordByPlan")
	public FlowOperationRecordVo getByPlan(String planId, int triggerType){
		List<FlowOperationRecord> list = operationRecordService.getByPlan(planId,triggerType);
		if(list.size()>0){
			FlowOperationRecordVo vo = operationRecordService.getVo(list.get(0));
			return vo;
		}
		return null;
	}
	

}
