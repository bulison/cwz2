package cn.fooltech.fool_ops.schedulejob.schedule;//package cn.fooltech.fool_ops.domain.schedule;
//
//import java.util.List;
//import java.util.Map;
//
//import cn.fooltech.fool_ops.domain.flow.entity.Plan;
//import cn.fooltech.fool_ops.domain.flow.repository.PlanRepository;
//import cn.fooltech.fool_ops.domain.flow.service.PlanService;
//import cn.fooltech.fool_ops.domain.message.entity.Message;
//import cn.fooltech.fool_ops.domain.message.utils.SendUtils;
//import cn.fooltech.fool_ops.domain.sysman.entity.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.google.common.collect.Maps;
//
///**
// * <p>计划调度服务类</p>
// * @author rqh
// * @version 1.0
// * @date 2016年5月31日
// */
//@Service("ops.PlanScheduleService")
//public class PlanScheduleService extends BaseScheduleService{
//
//	@Autowired
//	private PlanRepository planRepository;
//
//	@Autowired
//	private PlanService planService;
//
//	/**
//	 * 系统Debug
//	 */
//	private boolean debug = false;
//
//	/**
//	 * 发送消息
//	 * @param plan
//	 * @param triggerType
//	 * @throws Exception
//	 */
//	private void sendMessage(Plan plan, Integer triggerType) throws Exception{
//		String busClass = Plan.class.getName();
//		String busScene = String.valueOf(plan.getStatus());
//		List<User> receivers = settingService.queryPlanReceiver(plan, triggerType);
//		Map<String, Object> paramMap = buildMap(plan);
//		int tag = SendUtils.getTag(plan.getSendPhoneMsg(), plan.getSendEmail());
//		messageService.sendMessage(busClass, busScene, paramMap, receivers, triggerType, plan.getFiscalAccount(), tag);
//	}
//
//	/**
//	 * 创建发送消息的Map
//	 * @param plan
//	 * @return
//	 */
//	private Map<String, Object> buildMap(Plan plan){
//		Map<String, Object> paramMap = Maps.newHashMap();
//		paramMap.put("sender", null);
//		paramMap.put("plan", plan);
//		paramMap.put("initiater", plan.getInitiater());
//		return paramMap;
//	}
//
//	/**
//	 * 调度执行，发送计划延迟报警消息，已延迟的不再重复报警,并维护计划状态
//	 */
//	@Override
//	@Transactional
//	public void execute() {
//		List<String> ids = planRepository.getAllDelayPlanIds();
//		for(String id : ids){
//			try{
//				Plan plan = planService.get(id);
//				plan.setStatus(Plan.STATUS_DELAYED);
//				planService.save(plan);
//				sendMessage(plan, Message.TRIGGER_TYPE_DELAY_ALARM);
//			}catch(Exception e){
//				if(debug){
//					e.printStackTrace();
//				}
//			}
//		}
//
//	}
//
//}
