package cn.fooltech.fool_ops.schedulejob.schedule;

import cn.fooltech.fool_ops.domain.basedata.service.GoodsPriceService;
import cn.fooltech.fool_ops.domain.bom.service.BomService;
import cn.fooltech.fool_ops.domain.flow.entity.Task;
import cn.fooltech.fool_ops.domain.flow.service.MsgWarnSettingService;
import cn.fooltech.fool_ops.domain.flow.service.TaskService;
import cn.fooltech.fool_ops.domain.message.service.MessageService;
import cn.fooltech.fool_ops.domain.message.utils.SendUtils;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.sysman.service.OrgService;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
import cn.fooltech.fool_ops.domain.warehouse.service.AuditService;
import cn.fooltech.fool_ops.domain.warehouse.service.WarehouseBillService;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 调度基类
 * @author xjh
 *
 */
@Service("ops.BaseScheduleService")
public class BaseScheduleService implements OpsSchedule {

	private static final Logger logger = LoggerFactory.getLogger(BaseScheduleService.class);
	
	@Autowired
	protected MessageService messageService;

	@Resource(name = "ops.WarehouseBillService")
	protected WarehouseBillService billService;
	
	@Autowired
	protected TaskService taskService;
	
	@Autowired
	protected MsgWarnSettingService settingService;
	
	@Autowired
	protected BomService bomService;
	
	@Autowired
	protected GoodsPriceService goodsPriceService;
	
	@Autowired
	protected AuditService auditUtil;
	
	@Autowired
	protected OrgService orgService;
	
	@Override
	public void execute() {
		logger.info("BaseScheduleService execute!");
	}

	/**
	 * 创建发送消息的Map
	 * @param task
	 * @param bill
	 * @return
	 */
	public Map<String, Object> buildMap(Task task, WarehouseBill bill){
		Map<String, Object> paramMap = Maps.newHashMap();
		paramMap.put("bill", bill);
		paramMap.put("task", task);
		paramMap.put("plan", task.getPlan());
		return paramMap;
	}
	
	/**
	 * 事件发送消息
	 * @param task
	 * @param bill
	 * @param triggerType
	 * @throws Exception
	 */
	public void sendMessage(Task task, WarehouseBill bill, Integer triggerType) throws Exception{
		String busClass = Task.class.getName();
		String busScene = task.getStatus()+"";
		List<User> receivers = settingService.queryTaskReceiver(task, triggerType);
		Map<String, Object> paramMap = buildMap(task, bill);
		int tag = SendUtils.getTag(task.getSendPhoneMsg(), task.getSendEmail());
		messageService.sendMessage(busClass, busScene, paramMap, receivers, null, triggerType, bill.getFiscalAccount(), tag);
	}


}
