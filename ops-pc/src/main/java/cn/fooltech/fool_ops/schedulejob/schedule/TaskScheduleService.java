package cn.fooltech.fool_ops.schedulejob.schedule;

import cn.fooltech.fool_ops.domain.flow.entity.Task;
import cn.fooltech.fool_ops.domain.flow.repository.TaskRepository;
import cn.fooltech.fool_ops.domain.message.entity.Message;
import cn.fooltech.fool_ops.domain.message.utils.SendUtils;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.utils.DateUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * <p>事件调度服务类</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2016年5月30日
 */

@Service("ops.TaskScheduleService")
public class TaskScheduleService extends BaseScheduleService {

    /**
     * 事件DAO类
     */

    @Autowired
    private TaskRepository taskRepository;

    /**
     * //系统Debug
     * //* 调度执行，发送事件延迟报警消息，已延迟的不再重复报警,并维护事件状态
     * //	 * 发送消息
     * //	 * @param task
     * //	 * @param triggerType
     * //	 * @throws Exception
     * //
     * //	 * 创建发送消息的Map
     * //	 * @param task
     * //	 * @return
     * //
     * //	 * 判断事件是否已经延迟办理
     * //	 * @param task
     * //	 * @return
     * //
     * //	 * 判断几天后，事件是否延迟办理
     * //	 * @param task
     * //	 * @param days
     * //	 * @return
     * //
     * //	 * 事件被分派后，提前一天提示有事件需办理
     * //	 * @throws Exception
     * //
     * //	 * 发送预告办理消息
     * //	 * @param task
     * //	 * @param triggerType
     * //	 * @throws Exception
     * //
     */
    private boolean debug = false;

    /**
     * 调度执行，发送事件延迟报警消息，已延迟的不再重复报警,并维护事件状态
     */
    @Override
    @Transactional
    public void execute() {
        Date currentDate = DateUtil.getSimpleDate(Calendar.getInstance().getTime());

        List<Integer> statusList = Lists.newArrayList(Task.STATUS_FINISHED, Task.STATUS_STOPED);
        List<Task> tasks = taskRepository.findNotComplete(statusList);
        for (Task task : tasks) {
            try {
                switch (task.getStatus()) {
                    //草稿
                    case Task.STATUS_DRAFT: {
                        String busScene = Task.STATUS_DRAFT + "";
                        Integer forwardDays = settingService.getForwardDays(task, Message.TRIGGER_TYPE_EARLY_REMIND, busScene); //提前提醒天数
                        if (isDelayed(task)) {
                            task.setStatus(Task.STATUS_DELAYED_UNSTART);
                            task.setLastDelayAlarmDate(currentDate);
                            taskService.save(task);
                            sendMessage(task, Message.TRIGGER_TYPE_DELAY_ALARM);
                        } else if (forwardDays != null && isDelayed(task, forwardDays)) {
                            sendMessage(task, Message.TRIGGER_TYPE_EARLY_REMIND);
                        }
                        //三级事件可能同时被一级和二级事件递归引用到，防止重复提醒，需从一级事件开始
                        if (task.getLevel() == 1) {
                            isAdvance(task);
                        }
                        break;
                    }

                    //办理中
                    case Task.STATUS_EXECUTING: {
                        String busScene = Task.STATUS_EXECUTING + "";
                        Integer forwardDays = settingService.getForwardDays(task, Message.TRIGGER_TYPE_EARLY_REMIND, busScene); //提前提醒天数
                        if (isDelayed(task)) {
                            task.setStatus(Task.STATUS_DELAYED_UNFINISH);
                            task.setLastDelayAlarmDate(currentDate);
                            taskService.save(task);
                            sendMessage(task, Message.TRIGGER_TYPE_DELAY_ALARM);
                        } else if (forwardDays != null && isDelayed(task, forwardDays)) {
                            sendMessage(task, Message.TRIGGER_TYPE_EARLY_REMIND);
                        }
                        break;
                    }

                    //已延迟
                    case Task.STATUS_DELAYED_UNSTART:
                    case Task.STATUS_DELAYED_UNFINISH: {
                        Date lastAlarmDate = task.getLastDelayAlarmDate();
                        Integer retryDays = settingService.getRetryDays(task, Message.TRIGGER_TYPE_DELAY_ALARM); //预警消息重发天数
                        if (lastAlarmDate == null || (retryDays != null && DateUtil.daysBetween(lastAlarmDate, currentDate) >= retryDays)) {
                            task.setLastDelayAlarmDate(currentDate);
                            taskService.save(task);
                            sendMessage(task, Message.TRIGGER_TYPE_DELAY_ALARM);
                        }
                        break;
                    }

                    default:
                        break;
                }
            } catch (Exception e) {
                if (debug) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 发送消息
     *
     * @param task
     * @param triggerType
     * @throws Exception
     */

    private void sendMessage(Task task, Integer triggerType) throws Exception {
        String busClass = Task.class.getName();
        String busScene = String.valueOf(task.getStatus());
        List<User> receivers = settingService.queryTaskReceiver(task, triggerType);
        Map<String, Object> paramMap = buildMap(task);
        int tag = SendUtils.getTag(task.getSendPhoneMsg(), task.getSendEmail());
        messageService.sendMessage(busClass, busScene, paramMap, receivers, triggerType, task.getFiscalAccount(), tag);
    }

    /**
     * 创建发送消息的Map
     *
     * @param task
     * @return
     */

    private Map<String, Object> buildMap(Task task) {
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("sender", null);
        paramMap.put("task", task);
        paramMap.put("plan", task.getPlan());
        return paramMap;
    }


    /**
     * 判断事件是否已经延迟办理
     *
     * @param task
     * @return
     */

    private boolean isDelayed(Task task) {
        Date now = Calendar.getInstance().getTime();
        Date antipateEndTime = task.getAntipateEndTime();
        return DateUtil.compareDate(now, antipateEndTime) > 0;
    }

    /**
     * 判断几天后，事件是否延迟办理
     *
     * @param task
     * @param days
     * @return
     */

    private boolean isDelayed(Task task, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, days);
        Date antipateEndTime = task.getAntipateEndTime();
        return calendar.getTime().getTime() > antipateEndTime.getTime();
    }

    /**
     * 事件被分派后，提前一天提示有事件需办理
     *
     * @throws Exception
     */

    private void isAdvance(Task task) throws Exception {

        //提前提醒天数
        Integer forwardDays = settingService.getForwardDays(task, Message.TRIGGER_TYPE_ADVANCE_WARN, task.getStatus() + "");
        if (task.getAntipateStartTime() == null || task.getLevel() == null || task.getAssignFlag() == null || forwardDays == null)
            return;
        int daysBetween = DateUtil.daysBetween(new Date(), task.getAntipateStartTime());
        //一级事件可提前一天提醒，本事件已经确认分派不提醒，二级事件需要待父事件确认分派后才可以提醒
        if (daysBetween == forwardDays && task.getLevel() == 1 && task.getAssignFlag() == Task.ASSIGN_FLAG_NO) {
            sendMessage(task, Message.TRIGGER_TYPE_ADVANCE_WARN);
        }
        Set<Task> childs = task.getChilds();
        //遍历子事件
        for (Task childTask : childs) {
            daysBetween = DateUtil.daysBetween(new Date(), childTask.getAntipateStartTime());
            //本事件已经确认分派不提醒,本事件确认分派下级事件提醒
            if (daysBetween == forwardDays && task.getAssignFlag() == Task.ASSIGN_FLAG_YES && childTask.getAssignFlag() == Task.ASSIGN_FLAG_NO) {
                sendMessage(childTask, Message.TRIGGER_TYPE_ADVANCE_WARN);
            }
            isAdvance(childTask);
        }
    }

    /**
     * 发送预告办理消息
     *
     * @param task
     * @param triggerType
     * @throws Exception
     */

    private void sendAdvanceMessage(Task task, Integer triggerType) throws Exception {
        String busClass = Task.class.getName();
        String busScene = String.valueOf(task.getStatus());
        List<User> receivers = new ArrayList<User>();
        receivers.add(task.getUndertaker());
        Map<String, Object> paramMap = buildMap(task);
        int tag = SendUtils.getTag(task.getSendPhoneMsg(), task.getSendEmail());
        messageService.sendMessage(busClass, busScene, paramMap, receivers, triggerType, task.getFiscalAccount(), tag);
    }
}

