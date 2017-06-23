package cn.fooltech.fool_ops.web.rest;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.common.entity.Attach;
import cn.fooltech.fool_ops.domain.common.service.AttachService;
import cn.fooltech.fool_ops.domain.common.vo.SimpleAttachVo;
import cn.fooltech.fool_ops.domain.flow.entity.FlowOperationRecord;
import cn.fooltech.fool_ops.domain.flow.entity.Rank;
import cn.fooltech.fool_ops.domain.flow.entity.Task;
import cn.fooltech.fool_ops.domain.flow.service.FlowAttachService;
import cn.fooltech.fool_ops.domain.flow.service.FlowOperationRecordService;
import cn.fooltech.fool_ops.domain.flow.service.RankService;
import cn.fooltech.fool_ops.domain.flow.service.TaskService;
import cn.fooltech.fool_ops.domain.flow.vo.RankVo;
import cn.fooltech.fool_ops.domain.flow.vo.TaskVo;
import cn.fooltech.fool_ops.domain.member.service.MemberService;
import cn.fooltech.fool_ops.domain.member.vo.MemberVo;
import cn.fooltech.fool_ops.utils.WebUtils;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

/**
 * 
 * <p>
 * 事件
 * </p>
 * 
 * @author cwz
 * @date 2017年6月13日
 */
@RestController
@RequestMapping(value = "/api/task")
public class TaskResource extends AbstractBaseResource {

	private static final String NameSpace = "task";

	/**
	 * 事件网页服务类
	 */
	@Autowired
	private TaskService taskWebService;

	@Autowired
	private AttachService attachService;

	@Autowired
	FlowOperationRecordService recordService;

	@Autowired
	RankService rankWebService;

	@Autowired
	FlowAttachService flowAttachService;

	@Autowired
	FlowOperationRecordService frService;


	/**
	 * 获取事件信息
	 * 
	 * @param fid
	 *            事件ID
	 * @return
	 */
	
	@GetMapping("/getById")
	public TaskVo getById(String fid) throws Exception {
		Task task = taskWebService.get(fid);
		return taskWebService.getVo(task, true);
	}

	/**
	 * 查询计划下的所有事件（树状列表结构）
	 * 
	 * @param planId
	 * @return
	 * @throws Exception
	 */
	
	@GetMapping("/queryAllByPlan")
	public List<TaskVo> queryAllByPlan(String planId) throws Exception {
		List<TaskVo> plan = taskWebService.queryAllByPlan(planId);
		return plan;
	}

	/**
	 * 新增、编辑
	 * 
	 * @param vo
	 * @return
	 */
	
	@PutMapping("/save")
	public ResponseEntity save(TaskVo vo, HttpServletRequest request) throws Exception {
		RequestResult result = taskWebService.save(vo);
		return reponse(result);
	}

	/**
	 * 删除
	 * 
	 * @param fid
	 *            事件ID
	 * @return
	 */
	
	@DeleteMapping("/delete")
	public ResponseEntity delete(String fid) throws Exception {
		RequestResult result = taskWebService.delete(fid);
		return reponse(result);
	}

	/**
	 * 办理开始
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	
	@PutMapping("/startExcute")
	public ResponseEntity startExcute(TaskVo vo) throws Exception {
		RequestResult result = taskWebService.startExcute(vo);
		return reponse(result);
	}

	/**
	 * 办理结束
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	
	@PutMapping("/endExecute")
	public ResponseEntity endExecute(TaskVo vo) throws Exception {
		RequestResult result = taskWebService.endExecute(vo);
		return reponse(result);
	}

	/**
	 * 审核办理<br>
	 * 0-审核通过 , 1-拒绝<br>
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	
	@PutMapping("/checkExcute")
	public ResponseEntity checkExcute(TaskVo vo) throws Exception {
		RequestResult result = taskWebService.checkExcute(vo);
		return reponse(result);
	}

	/**
	 * 申请延迟
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@PutMapping("/applyDelay")
	public ResponseEntity applyDelay(TaskVo vo) throws Exception {
		RequestResult result = taskWebService.applyDelay(vo);
		return reponse(result);
	}

	/**
	 * 审核申请延迟<br>
	 * 0-审核通过 , 1-拒绝<br>
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@PutMapping("/checkApplyDelay")
	public ResponseEntity checkApplyDelay(TaskVo vo) throws Exception {
		RequestResult result = taskWebService.checkApplyDelay(vo);
		return reponse(result);
	}

	/**
	 * 终止
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	
	@PutMapping("/stop")
	public ResponseEntity stop(TaskVo vo) throws Exception {
		RequestResult result = taskWebService.stop(vo);
		return reponse(result);
	}

	/**
	 * 关联
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	
	@PutMapping("/relevance")
	public ResponseEntity relevance(TaskVo vo) throws Exception {
		RequestResult result = taskWebService.relevance(vo);
		return reponse(result);
	}

	/**
	 * 获取事件申请延迟的信息
	 * 
	 * @param fid
	 *            事件ID
	 * @return
	 */
	
	@GetMapping("/getApplyDelayMsg")
	public TaskVo getApplyDelayMsg(String fid) throws Exception {
		return taskWebService.getApplyDelayMsg(fid);
	}

	/**
	 * 获取事件办理结束的信息
	 * 
	 * @param fid
	 *            事件ID
	 * @return
	 */
	
	@GetMapping("/getEndExcuteMsg")
	public TaskVo getEndExcuteMsg(String fid) throws Exception {
		return taskWebService.getEndExcuteMsg(fid);
	}

	/**
	 * 根据某个事件，获取符合条件的前置关联事件
	 * 
	 * @param fid
	 *            事件ID
	 * @return
	 */
	
	@GetMapping("/getFrontRelevanceTask")
	public List<TaskVo> getFrontRelevanceTask(String fid) throws Exception {
		return taskWebService.getFrontRelevanceTask(fid);
	}

	/**
	 * 根据某个事件，获取符合条件的后置关联事件
	 * 
	 * @param fid
	 *            事件ID
	 * @return
	 */
	
	@GetMapping("/getBehindRelevanceTask")
	public List<TaskVo> getBehindRelevanceTask(String fid) throws Exception {
		return taskWebService.getBehindRelevanceTask(fid);
	}

	/**
	 * 获取所有已关联的前置事件
	 * 
	 * @param fid
	 *            事件ID
	 * @return
	 */
	
	@GetMapping("/getAllRelevanceTask")
	public List<TaskVo> getAllRelevanceTask(String fid) throws Exception {
		return taskWebService.getAllRelevanceTask(fid);
	}

	/**
	 * 分派任务
	 * 
	 * @param fid
	 *            需要确认分派完成的事件ID
	 * @return
	 */
	
	@PutMapping("/assignTask")
	public ResponseEntity assignTask(String fid) throws Exception {
		RequestResult result = taskWebService.assignTask(fid);
		return reponse(result);
	}

	/**
	 * 变更事件责任人、承办人
	 * 
	 * @param vo
	 * @return
	 */
	
	@PutMapping("/change")
	public ResponseEntity change(TaskVo vo) throws Exception {
		RequestResult result = taskWebService.change(vo);
		return reponse(result);
	}

	/**
	 * 上传附件,计划与事件通用
	 * 
	 * @return
	 */
	
	@GetMapping("/upload")
	public void upload(MultipartFile file, HttpServletResponse response, String triggerType) {
		RequestResult result = new RequestResult();
		// 文件类型判定
		String fileName = file.getOriginalFilename();
		String fileSuffix = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
		List<String> suffixList = Lists.newArrayList();
		String[] suffixArr = { "jpeg", "jpg", "png", "bmp", "xls", "xlsx", "doc", "docx", "zip", "rar", "txt", "pdf",
				"ppt" };
		Collections.addAll(suffixList, suffixArr);
		String suffix = "";
		for (String s : suffixArr) {
			suffix += "," + s;
		}
		if (!suffixList.contains(fileSuffix)) {
			result.setReturnCode(RequestResult.RETURN_FAILURE);
			result.setMessage("上传文件类型错误,只允许上传jpeg,jpg,png,bmp,xls,xlsx,doc,docx,zip,rar,txt");
			WebUtils.writeJsonToHtml(response, result);
			return;
		}
		if (file.getSize() > Math.pow(2, 20) * 10) {
			result.setReturnCode(RequestResult.RETURN_FAILURE);
			result.setMessage("上传文件大小不能超过10M");
			WebUtils.writeJsonToHtml(response, result);
			return;
		}
		// file类型转attach实体，但此实体不带busId属性，需要调用结束流程的方法后生成一个流程id，再把新的流程id插入到每个attach实体中
		Attach attach = taskWebService.uploadAttach(file);

		JSONObject json = new JSONObject();
		json.put("downUrl",
				"/flow/plan/downloadByPath?path=" + attach.getFilePath() + "&attachName=" + attach.getFileName());
		json.put("attachId", attach.getFid());

		result.setData(json.toString());
		WebUtils.writeJsonToHtml(response, result);
	}

	/**
	 * 添加评分评论
	 * 
	 * @return
	 * @throws Exception
	 */
	
	@PutMapping("/addRank")
	public ResponseEntity addRank(RankVo rankVo) throws Exception {
		RequestResult result = rankWebService.save(rankVo);
		return reponse(result);
	}

	/**
	 * 根据业务ID和类型获取评分评论
	 * 
	 * @return
	 * @throws Exception
	 */
	
	@PutMapping("/queryRank")
	public ResponseEntity queryRank(RankVo rankVo) throws Exception {
		RequestResult result = new RequestResult();
		Rank rank = rankWebService.findOneByTypeAndBusinessId(rankVo);
		result.setData(rank);
		return reponse(result);
	}

	/**
	 * 删除评分评论
	 * 
	 * @return
	 * @throws Exception
	 */
	
	@DeleteMapping("/delRank")
	public ResponseEntity delRank(RankVo rankVo) throws Exception {
		 RequestResult result = rankWebService.delete(rankVo.getFid());
		 return reponse(result);
	}

	/**
	 * 维护树节点标识
	 * 
	 * @return
	 */
	
	@PutMapping("/updateTaskTreeFlag")
	public ResponseEntity updateTaskTreeFlag() {
		RequestResult result = taskWebService.updateTaskTreeFlag();
		return reponse(result);
	}

	/**
	 * 计划管理界面查看该事件可下载的附件
	 * 
	 * @return
	 */
	@GetMapping("/showAttach")
	public List<SimpleAttachVo> showAttach(String taskId) {
		return flowAttachService.getFileMapByTaskId(taskId);
	}

	/**
	 * 计划管理界面查看该事件可下载的附件（最后操作上传的）
	 * 
	 * @return
	 */
	@GetMapping("/showLastAttach")
	public List<SimpleAttachVo> showLastAttach(@RequestParam String taskId, Integer triggerType) {
		FlowOperationRecord foRecord = recordService.getLastByTask(taskId, triggerType);
		return flowAttachService.getFileMapByOperRecord(foRecord.getFid());
	}

	/**
	 * 多计划流程合并
	 * 
	 * @param vo
	 *            操作计划vo
	 * @param ids
	 *            需要合并计划(事件)id集合
	 * @param type
	 *            1.计划；2.事件
	 * @return
	 */
	
	@PutMapping("/mergePlan")
	public ResponseEntity mergePlan(TaskVo vo, String ids, int type) {
		 RequestResult result = taskWebService.mergePlan(vo, ids, type);
		 return reponse(result);
		 
	}

	/**
	 * 根据计划ID查询事件列表
	 * 
	 * @param planId
	 *            计划ID
	 * @return
	 */
	
	@GetMapping("/tree")
	public List<TaskVo> taskTree(String planId) {
		return taskWebService.queryTreeByPlanId(planId);
	}

	/**
	 * 删除临时附件,计划与事件通用
	 * 
	 * @param attachId:附件id
	 * @return
	 */
	
	@DeleteMapping("/deleteAttach")
	public ResponseEntity deleteAttach(String attachId) {
		attachService.deleteById(attachId);
		return reponse(new RequestResult());
	}

	/**
	 * 修正任务数据
	 * 
	 * @return
	 */
	
	@PutMapping("/fixTaskData")
	public ResponseEntity fixTaskData() {
		taskWebService.fixTaskData();
		return reponse(new RequestResult());
	}

	/**
	 * 根据计划id查询事件
	 * 
	 * @param planId
	 * @return
	 */
	
	@GetMapping("/queryByPlan")
	public List<TaskVo> queryByPlan(String planId) {
		List<TaskVo> list = taskWebService.queryDetail(planId);
		return list;
	}
}
