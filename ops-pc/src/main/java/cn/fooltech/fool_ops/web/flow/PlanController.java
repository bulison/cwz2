package cn.fooltech.fool_ops.web.flow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cn.fooltech.fool_ops.domain.analysis.vo.CostAnalysisBillVo;
import cn.fooltech.fool_ops.domain.base.service.PageService;
import cn.fooltech.fool_ops.domain.common.vo.SimpleAttachVo;
import cn.fooltech.fool_ops.domain.flow.service.*;
import cn.fooltech.fool_ops.domain.flow.vo.*;
import cn.fooltech.fool_ops.domain.payment.entity.PaymentBill;
import cn.fooltech.fool_ops.domain.payment.vo.PaymentBillVo;
import cn.fooltech.fool_ops.utils.JsonUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import com.github.pagehelper.PageHelper;
import javassist.expr.NewArray;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.codec.net.URLCodec;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.component.core.filesystem.FileSystem;
import cn.fooltech.fool_ops.domain.common.entity.Attach;
import cn.fooltech.fool_ops.domain.common.service.AttachService;
import cn.fooltech.fool_ops.domain.flow.entity.FlowOperationRecord;
import cn.fooltech.fool_ops.domain.flow.entity.Plan;
import cn.fooltech.fool_ops.utils.StrUtil;

/**
 * <p>计划控制器</p>
 * @author rqh
 * @version 1.0
 * @date 2016年5月20日
 */
@Controller
@RequestMapping("/flow/plan")
public class PlanController implements PageService {

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
	 * 计划列表页面
	 * @return
	 */
	@RequestMapping("/listPage")
	public String listPage() throws Exception{
		return "/timeaxis/planList";
	}
	
	/**
	 * 消息中心查看计划或事件界面
	 * @param busType=0是查看计划，=1是事件, rankFlag=1是查看评论
	 * 如果查看事件，则返回ArrayList<OperateRecordVo> 和taskVo
	 * @return
	 */
	@RequestMapping("/planView")
	public String planView(String id, @RequestParam(defaultValue = "1") int busType, HttpServletRequest request, int rankFlag){
		if(StrUtil.notEmpty(id)){
			PlanVo plan = null;
			if(busType == 0){
				plan = planWebService.getById(id);
				if(rankFlag == 1) {
					RankVo rankVo = rankWebService.findByBusinessId(id);
					request.setAttribute("rank", rankVo);
					request.setAttribute("busName", plan.getName());
					return "/flow/planView";
				}
				//搜索某个计划下的所有流程，如果流程带有多个附件，把附件信息也查出，点击附件名称把附件id传到download方法下载
				List<FlowOperationRecord> recordList = frService.getByPlan(id);
				ArrayList<OperateRecordVo> recordVoList = null;
				if(recordList != null && recordList.size() > 0) {
					recordVoList = new ArrayList<OperateRecordVo>();
					for(FlowOperationRecord record : recordList) {
						String recordId = record.getFid();
						Date createTime = record.getCreateTime();
						String describe = record.getDescribe();
						int triggerType = record.getTriggerType();
						//查询该流程下的所有附件，以map格式返回，键是文件名，值是文件id是attach实体的主键
						List<SimpleAttachVo> fileMap = flowAttachService.getFileMapByOperRecord(recordId);
						OperateRecordVo recordVo = new OperateRecordVo();
						recordVo.setOperateName(planWebService.getOperate(triggerType));
						recordVo.setCreateTime(createTime + "");
						recordVo.setDescribe(describe);
						recordVo.setFileMap(fileMap);
						recordVoList.add(recordVo);
					}
					request.setAttribute("recordVoList", recordVoList);
				}
			}
			else{
				TaskVo task = taskWebService.getById(id);
				if(rankFlag == 1) {
					RankVo rankVo = rankWebService.findByBusinessId(id);
					request.setAttribute("rank", rankVo);
					request.setAttribute("busName", task.getName());
					return "/flow/planView";
				}
				//搜索某个事件下的所有流程，如果流程带有多个附件，把附件信息也查出，点击附件名称把附件id传到download方法下载
				List<FlowOperationRecord> recordList = frService.getByTask(id);
				ArrayList<OperateRecordVo> recordVoList = null;
				if(recordList != null && recordList.size() > 0) {
					recordVoList = new ArrayList<OperateRecordVo>();
					for(FlowOperationRecord record : recordList) {
						String recordId = record.getFid();
						Date createTime = record.getCreateTime();
						String describe = record.getDescribe();
						int triggerType = record.getTriggerType();
						//查询该流程下的所有附件，以map格式返回，键是文件名，值是文件id是attach实体的主键
						List<SimpleAttachVo> fileMap = flowAttachService.getFileMapByOperRecord(recordId);
						OperateRecordVo recordVo = new OperateRecordVo();
						recordVo.setOperateName(planWebService.getOperate(triggerType));
						recordVo.setCreateTime(createTime + "");
						recordVo.setDescribe(describe);
						recordVo.setFileMap(fileMap);
						recordVoList.add(recordVo);
					}
					request.setAttribute("recordVoList", recordVoList);
				}
				request.setAttribute("task", task);
				
				return "/flow/planView";
//				if(task != null){
//					plan = planWebService.getById(task.getPlanId());
//				}
			}
			if(plan != null){
				request.setAttribute("plan", plan);
			}
		}
		
		return "/flow/planView";
	}
	
	/**
	 * 文件下载
	 * @param fileId 文件id
	 * @throws IOException 
	 */
	@RequestMapping("/download")
	public void download(HttpServletResponse response, String fileId){
		FileInputStream inputStream = null;
		ServletOutputStream out = null;
		try {
			Attach attach = attachService.get(fileId);
			String fileName = attach.getFileName();
			fileName = URLEncoder.encode(fileName, "utf-8");
			String root = fs.getRoot();
			String path = attach.getFilePath();
			path.substring(path.lastIndexOf("/") + 1);
			path = root + path;
			File file = new File(path);
			response.setContentType("multipart/form-data");
			response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
			response.setHeader("Content_Length", file.length() + "");  
			inputStream = new FileInputStream(file);
			out = response.getOutputStream();
			
			byte[] b = new byte[2048];
	        int length;
	        while ((length = inputStream.read(b)) > 0) {
	            out.write(b, 0, length);
	        } 
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(out != null) {
					out.flush();
					out.close();  
				}
				if(inputStream != null) {
					inputStream.close();  
				}
			} catch (IOException e) {
				e.printStackTrace();
			} 
			
		}
	}
	
	
	/**
	 * 计划管理界面查看该计划可下载的附件
	 * @return
	 */
	@RequestMapping("/showAttach")
	@ResponseBody
	public List<SimpleAttachVo> showAttach(String planId, HttpServletRequest request) {
		return flowAttachService.getFileMapByPlanId(planId);
	}
	
	
	/**
	 * 事件轴页面
	 * @param id 业务ID
	 * @param busType 业务类型：1-计划   2-事件
	 * @param request
	 * @return
	 */
	@RequestMapping("/axis")
	public String axis(String id, @RequestParam(defaultValue = "1") int busType, HttpServletRequest request){
		if(StrUtil.notEmpty(id)){
			PlanVo plan = null;
			if(busType == 1){
				plan = planWebService.getById(id);
			}
			else{
				TaskVo task = taskWebService.getById(id);
				if(task != null){
					plan = planWebService.getById(task.getPlanId());
				}
			}
			if(plan != null){
				request.setAttribute("plan", plan);
			}
		}
		return "/timeaxis/newAxis";
	}
	
	/**
	 * 获取计划信息
	 * @param fid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getById")
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
	@ResponseBody
	@RequestMapping("/query")
	public PageJson query(PlanVo vo, PageParamater paramater, String excludeId) throws Exception{
		return planWebService.query(vo, paramater,excludeId);
	}
	
	/**
	 * 新增、编辑
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/save")
	public RequestResult save(PlanVo vo, HttpServletRequest request) throws Exception{
		RequestResult result = planWebService.save(vo, true);
		return result;
	}
	
	/**
	 * 删除
	 * @param fid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public RequestResult delete(String fid) throws Exception{
		return planWebService.delete(fid);
	}
	
	/**
	 * 提交	
	 * @param id			计划id
	 * @param describe		描述
	 * @param type			是否处理：0-处理，1-不处理
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/submit")
	public RequestResult submit(@RequestParam String id, String describe,Integer type) throws Exception{
		return planWebService.submit(id, describe,type);
	}
	
	/**
	 * 审核
	 * @param id 计划ID
	 * @param auditResult：0.审核通过 1.拒绝，返回发起人  3.终止
	 * @param rejectUserId：暂时不用
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/audit")
	public RequestResult audit(@RequestParam String id, Integer auditResult, String rejectUserId, String describe) throws Exception{
		return planWebService.audit(id, auditResult, rejectUserId, describe);
	}
	
	/**
	 * 终止
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("/terminate")
	public RequestResult terminate(@RequestParam String id, String describe) throws Exception{
		return planWebService.terminate(id, describe);
	}
	
	/**
	 * 完成计划
	 * @param id		计划id
	 * @param describe	完成描述
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/complete")
	public RequestResult complete(String id,String describe) throws Exception{
		return planWebService.completePlan(id,describe);
	}
	
	/**
	 * 查询计划类型
	 */
	@ResponseBody
	@RequestMapping("/queryPlanTypes")
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
	/*
	@Autowired
	private PlanScheduleService planScheduleService;
	
	@Autowired
	private TaskScheduleService taskScheduleService;
	
	@Autowired
	private PurchaseScheduleService purchaseScheduleService;
	
	@ResponseBody
	@RequestMapping("/executeSchedule")
	public RequestResult executeSchedule(){
		taskScheduleService.execute();
		planScheduleService.execute();
		return new RequestResult();
	}
	
	@ResponseBody
	@RequestMapping("/executeSchedule2")
	public RequestResult executeSchedule2(){
		purchaseScheduleService.execute();
		return new RequestResult();
	}*/

	/**
	 * 把计划保存到模板
	 * @param planId 计划ID
	 * @param planTplId 计划模板ID,可能是空值，即没有原模板
	 * @param taskIds 计划事件ID,是一个逗号字符串，按事件顺序排列
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/savePlanToTemplate")
	public RequestResult savePlanToTemplate(@RequestParam String planId, String planTplId, String taskIds){
		return planWebService.savePlanToTemplate(planId, planTplId, taskIds);
	}

	/**
	 * 流程汇总预计成本查询
	 */
	@RequestMapping("/fee/list")
	public String queryPlanFeeAnalysis(@RequestParam String planId, String deliveryPlace, String goodsId,
												  String receiptPlace, String goodsSpecId, PageParamater page, Model model){
		PageJson pageJson = planManagerService.queryPlanTransportFee(planId, deliveryPlace, receiptPlace, goodsId, goodsSpecId, page);
		model.addAttribute("result", JsonUtil.toJsonString(pageJson));
		return "/report/reportResult";
	}

	/**
	 * 流程汇总预计成本导出
	 */
	@RequestMapping("/fee/export")
	public void exportPlanFeeAnalysis(String planId, String deliveryPlace, String goodsId, String receiptPlace,
                                        String goodsSpecId, PageParamater page, HttpServletResponse response){
        planManagerService.exportPlanTransportFee(planId, deliveryPlace, receiptPlace, goodsId, goodsSpecId, response, page);
	}
	/**
	 * 获取计划的操作记录
	 * @param planId 计划ID
	 * @param triggerType 触发动作类型【完成:41】
	 * @return
	 */
	@RequestMapping("/getRecordByPlan")
	@ResponseBody
	public FlowOperationRecordVo getByPlan(String planId, int triggerType){
		List<FlowOperationRecord> list = operationRecordService.getByPlan(planId,triggerType);
		if(list.size()>0){
			FlowOperationRecordVo vo = operationRecordService.getVo(list.get(0));
			return vo;
		}
		return null;
	}
	
	/**
	 * 收益率轮询
	 */
	@RequestMapping("/yieldPolling")
	@ResponseBody
	public void yieldPolling(){
		try {
			planWebService.yieldPolling();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 根据计划计算流程计划每天收益率
	 * @param planId
	 * @param orgId
	 * @param fiscalAccount
	 * @return
	 */
	@RequestMapping("/stockInOut")
	@ResponseBody
	public RequestResult stockInOut(String planId,String orgId,String fiscalAccount){
		return planWebService.stockInOut(planId, orgId, fiscalAccount,2);
	}
	
}
