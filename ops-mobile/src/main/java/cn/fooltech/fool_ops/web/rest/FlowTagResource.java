package cn.fooltech.fool_ops.web.rest;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cn.fooltech.fool_ops.component.core.SpringBeanUtils;
import cn.fooltech.fool_ops.domain.flow.entity.Plan;
import cn.fooltech.fool_ops.domain.flow.entity.Rank;
import cn.fooltech.fool_ops.domain.flow.entity.Task;
import cn.fooltech.fool_ops.domain.flow.service.PlanService;
import cn.fooltech.fool_ops.domain.flow.service.RankService;
import cn.fooltech.fool_ops.domain.flow.service.TaskService;
import cn.fooltech.fool_ops.domain.flow.vo.FlowOperation;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.web.service.FlowMobileOperationService;
import io.swagger.annotations.ApiOperation;

/**
 * 流程管理权限控制
 * 
 * @author xjh
 *
 */
@RestController
@RequestMapping(value = "/api/flowTag")
public class FlowTagResource extends AbstractBaseResource {

	private static final long serialVersionUID = 3869773123258735726L;
	@Autowired
	private FlowMobileOperationService flowOperService;
	@Autowired
	private PlanService planService;
	@Autowired
	private TaskService taskService;

	/**
	 * 计划ID
	 */
	// private String planId;

	/**
	 * 计划ID
	 */
	// private String taskId;

	/**
	 * 计划或者事件的类型
	 */
	// private String type;

	private static final String TYPE_PLAN = "plan";
	private static final String TYPE_TASK = "task";
	private static final String ANY = "any";

	/**
	 * 计划Key：Plan#状态#是否所有事件已完成#当前用户是否已评分
	 * 事件Key：Task#计划状态#事件状态#是否为一级事件#父事件是否分派完成#是否有子事件#是否有delayEndTime#当前用户是否已评分#本事件是否已确认分派#是否有草稿子事件
	 */
	/**
	 * 
	 * @param type	 	类型：plan-计划，task-事件
	 * @param planId	计划id
	 * @param taskId	事件id（查询事件时也要传计划id）
	 * @return
	 */
	@ApiOperation("获取计划组件按钮")
    @GetMapping("/getFlowTag")
	public ResponseEntity getFlowTag(String type, String planId, String taskId) {

		// FlowOperationService flowOperService =
		// (FlowOperationService)SpringBeanUtils.getBean(FlowOperationService.class);
		// PlanService planService =
		// (PlanService)SpringBeanUtils.getBean(PlanService.class);
		// TaskService taskService =
		// (TaskService)SpringBeanUtils.getBean(TaskService.class);
		Map<String, List<FlowOperation>> operationMap = flowOperService.getOperationMap();
		Set<String> keySets = operationMap.keySet();
		Set<String> cache = Sets.newHashSet();
		List<FlowOperation> operBtns = null;
		List<List<FlowOperation>> list=Lists.newArrayList();
//		 String html = "";

		if (TYPE_PLAN.equalsIgnoreCase(type)) {

			if (!Strings.isNullOrEmpty(planId)) {
				Plan plan = planService.get(planId);

				if (plan != null) {
					for (String keySet : keySets) {
						if (mapPlanKey(keySet, plan)) {
							operBtns = operationMap.get(keySet);
							operBtns = catchNotExistData(operBtns, cache);
							operBtns = checkPlanAuth(operBtns, plan);
//							 html += genHtml(operBtns);
							 if(operBtns!=null&&operBtns.size()>0){
								 list.add(operBtns);
							 }
							
						}
					}
				}
			}

		} else if (TYPE_TASK.equalsIgnoreCase(type)) {

			if (!Strings.isNullOrEmpty(planId) && !Strings.isNullOrEmpty(taskId)) {
				Task task = taskService.get(taskId);
				Plan plan = planService.get(planId);

				if (task != null && plan != null) {
					for (String keySet : keySets) {
						if (mapTaskKey(keySet, plan, task)) {
							operBtns = operationMap.get(keySet);
							operBtns = catchNotExistData(operBtns, cache);
							operBtns = checkTaskAuth(operBtns, task);
//							 html += genHtml(operBtns);
							 if(operBtns!=null&&operBtns.size()>0){
								 list.add(operBtns);
							 }
						}
					}
				}
			}
		}

		return listReponse(list);
	}

	/**
	 * 判断是否所有权限
	 * 
	 * @param operBtn
	 * @return
	 */
	private boolean checkAnyAuth(FlowOperation operBtn) {
		String auths = operBtn.getAuth();
		if (!Strings.isNullOrEmpty(auths) && auths.contains(ANY)) {
			return true;
		}
		return false;
	}

	/**
	 * 检查计划是否满足权限条件
	 * 
	 * @param operBtns
	 * @return
	 */
	private List<FlowOperation> checkPlanAuth(List<FlowOperation> operBtns, Plan plan) {

		Splitter splitter = Splitter.on(",").trimResults().omitEmptyStrings();

		String userId = SecurityUtil.getCurrentUserId();

		PlanService planService = (PlanService) SpringBeanUtils.getBean(PlanService.class);
		List<FlowOperation> btns = Lists.newArrayList();

		for (FlowOperation fo : operBtns) {
			String auth = fo.getAuth();
			if (!Strings.isNullOrEmpty(auth)) {
				if (checkAnyAuth(fo)) {
					btns.add(fo);
					continue;
				}
				List<String> authStrs = splitter.splitToList(auth);
				Set<String> authIds = planService.getUserIds(plan, authStrs);
				if (authIds.contains(userId)) {
					btns.add(fo);
				}
			}

		}
		return btns;
	}

	/**
	 * 检查权限是否满足事件条件
	 * 
	 * @param operBtns
	 * @return
	 */
	private List<FlowOperation> checkTaskAuth(List<FlowOperation> operBtns, Task task) {

		Splitter splitter = Splitter.on(",").trimResults().omitEmptyStrings();

		String userId = SecurityUtil.getCurrentUserId();

		TaskService taskService = (TaskService) SpringBeanUtils.getBean(TaskService.class);
		List<FlowOperation> btns = Lists.newArrayList();

		for (FlowOperation fo : operBtns) {
			String auth = fo.getAuth();
			if (!Strings.isNullOrEmpty(auth)) {
				if (checkAnyAuth(fo)) {
					btns.add(fo);
					continue;
				}
				List<String> authStrs = splitter.splitToList(auth);
				Set<String> authIds = taskService.getUserIds(task, authStrs);
				if (authIds.contains(userId)) {
					btns.add(fo);
				}
			}

		}
		return btns;
	}

	/**
	 * 移除重复数据
	 * 
	 * @return
	 */
	private List<FlowOperation> catchNotExistData(List<FlowOperation> operBtns, Set<String> cache) {

		List<FlowOperation> btns = Lists.newArrayList();
		for (FlowOperation oper : operBtns) {
			if (!cache.contains(oper.getId())) {
				btns.add(oper);
				cache.add(oper.getId());
			}
		}
		return btns;
	}

	/**
	 * 根据按钮数据生成html
	 * 
	 * @param operBtns
	 * @return
	 */
	private String genHtml(List<FlowOperation> operBtns) {
		if (operBtns == null)
			return "";
		StringBuffer buffer = new StringBuffer();
		for (FlowOperation oper : operBtns) {
			buffer.append("<input id=\"");
			buffer.append(oper.getId());
			buffer.append("\" type=\"");
			buffer.append(oper.getType());
			buffer.append("\" class=\"");
			buffer.append(oper.getClazz());
			buffer.append("\" value=\"");
			buffer.append(oper.getName());
			buffer.append("\"/>&nbsp;");
		}

		return buffer.toString();
	}

	/**
	 * 是否所有的事件都完成
	 * 
	 * @param plan
	 * @return
	 */
	private boolean isTasksComplete(Plan plan) {
		TaskService taskService = (TaskService) SpringBeanUtils.getBean(TaskService.class);
		if (taskService.countNotCompleteByPlanId(plan.getFid()) > 0) {
			return Boolean.FALSE;
		} else {
			return Boolean.TRUE;
		}
	}

	/**
	 * 是否一级事件
	 * 
	 * @param task
	 * @return
	 */
	private boolean isLevelOne(Task task) {
		if (task.getLevel() != null && task.getLevel() == 1) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * 是否有子事件
	 * 
	 * @param task
	 * @return
	 */
	private boolean hasChildren(Task task) {
		if (task.getChilds().size() > 0) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	/**
	 * 父事件是否分派完成
	 * 
	 * @param task
	 * @return
	 */
	private boolean isParentAssigned(Task task) {
		Task parent = task.getParent();
		if (parent != null && parent.getAssignFlag() == Task.ASSIGN_FLAG_YES) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	/**
	 * 是否有延迟结束时间
	 * 
	 * @param task
	 */
	private boolean hasDelayedEndTime(Task task) {
		if (task.getDelayedEndTime() != null) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	/**
	 * 是否分派完成
	 * 
	 * @return
	 */
	private boolean isAssigned(Task task) {
		if (task.getAssignFlag() == Task.ASSIGN_FLAG_YES) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	/**
	 * 是否评过分
	 * 
	 * @return
	 */
	private boolean isRanked(Short type, String dataId) {

		RankService rankService = (RankService) SpringBeanUtils.getBean(RankService.class);
		String userId = SecurityUtil.getCurrentUserId();

		if (rankService.isRanked(type, dataId, userId)) {
			return true;
		}
		return false;
	}

	/**
	 * 是否有草稿或以延迟未开始办理的子事件
	 * 
	 * @return
	 */
	private boolean hasDraftChildEvent(Task task) {
		Set<Task> childs = task.getChilds();
		for (Task child : childs) {
			if (child.getStatus() == Task.STATUS_DRAFT || child.getStatus() == Task.STATUS_DELAYED_UNSTART)
				return true;
		}
		return false;
	}

	/**
	 * 是否所有孩子都完成
	 * 
	 * @param task
	 * @return
	 */
	private boolean childAllComplete(Task task) {
		Set<Task> childs = task.getChilds();
		for (Task child : childs) {
			if (child.getStatus() != Task.STATUS_FINISHED && child.getStatus() != Task.STATUS_STOPED)
				return false;
		}
		return true;
	}

	/**
	 * 是否匹配计划Key
	 * 
	 * @return
	 */
	private boolean mapPlanKey(String xmlKey, Plan plan) {
		Splitter splitter = Splitter.on("#").trimResults().omitEmptyStrings();

		List<String> xmlStrs = splitter.splitToList(xmlKey);

		for (int i = 0; i < xmlStrs.size(); i++) {

			String xmlStr = xmlStrs.get(i);
			if (ANY.equals(xmlStr))
				continue;

			// 计划Key：Plan#状态#是否所有事件已完成#是否已评分
			if (i == 0) {
				if (!xmlStr.equalsIgnoreCase(TYPE_PLAN))
					return false;
			} else if (i == 1) {
				String planStatus = plan.getStatus() + "";
				if (!mapMultiKey(xmlStr, planStatus)) {
					return false;
				}
			} else if (i == 2) {
				String isTasksComplete = isTasksComplete(plan) + "";
				if (!mapMultiKey(xmlStr, isTasksComplete)) {
					return false;
				}
			} else if (i == 3) {
				String isRank = isRanked(Rank.TYPE_PLAN, plan.getFid()) + "";
				if (!mapMultiKey(xmlStr, isRank)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 是否匹配事件Key
	 * 
	 * @return
	 */
	private boolean mapTaskKey(String xmlKey, Plan plan, Task task) {
		Splitter splitter = Splitter.on("#").trimResults().omitEmptyStrings();

		List<String> xmlStrs = splitter.splitToList(xmlKey);

		for (int i = 0; i < xmlStrs.size(); i++) {

			String xmlStr = xmlStrs.get(i);
			if (ANY.equals(xmlStr))
				continue;

			// Task#计划状态#事件状态#是否为一级事件#父事件是否分派完成#是否有子事件#是否有delayEndTime#是否已评分
			if (i == 0) {
				if (!xmlStr.equalsIgnoreCase(TYPE_TASK))
					return false;
			} else if (i == 1) {// 判断计划状态
				String planStatus = plan.getStatus() + "";
				if (!mapMultiKey(xmlStr, planStatus)) {
					return false;
				}
			} else if (i == 2) {// 判断事件状态
				String taskStatus = task.getStatus() + "";
				if (!mapMultiKey(xmlStr, taskStatus)) {
					return false;
				}
			} else if (i == 3) {// 判断是否一级事件
				String isLevelOne = isLevelOne(task) + "";
				if (!mapMultiKey(xmlStr, isLevelOne)) {
					return false;
				}
			} else if (i == 4) {// 判断父事件是否已分派
				String isParentAssigned = isParentAssigned(task) + "";
				if (!mapMultiKey(xmlStr, isParentAssigned)) {
					return false;
				}
			} else if (i == 5) {// 判断是否有子事件
				String hasChildren = hasChildren(task) + "";
				if (!mapMultiKey(xmlStr, hasChildren)) {
					return false;
				}
			} else if (i == 6) {// 判断是否有延迟结束时间
				String hasDelayedEndTime = hasDelayedEndTime(task) + "";
				if (!mapMultiKey(xmlStr, hasDelayedEndTime)) {
					return false;
				}
			} else if (i == 7) {// 判断是否已评分
				String isRank = isRanked(Rank.TYPE_TASK, task.getFid()) + "";
				if (!mapMultiKey(xmlStr, isRank)) {
					return false;
				}
			} else if (i == 8) {// 判断是否已分派
				String isAssigned = isAssigned(task) + "";
				if (!mapMultiKey(xmlStr, isAssigned)) {
					return false;
				}
			} else if (i == 9) {// 判断是否有草稿状态的子事件
				String hasDraftChildEvent = hasDraftChildEvent(task) + "";
				if (!mapMultiKey(xmlStr, hasDraftChildEvent)) {
					return false;
				}
			} else if (i == 10) {// 判断所有子事件是否都完成
				String childAllComplete = childAllComplete(task) + "";
				if (!mapMultiKey(xmlStr, childAllComplete)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 是否multiKey包含key
	 * 
	 * @param multiKey
	 *            多个用法逗号隔开
	 * @param key
	 * @return
	 */
	private boolean mapMultiKey(String multiKey, String key) {

		Splitter splitter = Splitter.on(",").trimResults().omitEmptyStrings();
		List<String> multiKeyItems = splitter.splitToList(multiKey);
		for (String item : multiKeyItems) {
			if (item.equals(key)) {
				return true;
			}
		}
		return false;
	}

}
