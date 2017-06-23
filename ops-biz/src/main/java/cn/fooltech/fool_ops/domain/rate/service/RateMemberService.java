package cn.fooltech.fool_ops.domain.rate.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.flow.entity.Task;
import cn.fooltech.fool_ops.domain.rate.entity.RateMember;
import cn.fooltech.fool_ops.domain.rate.entity.RateMemberSum;
import cn.fooltech.fool_ops.domain.rate.repository.RateMemberRepository;
import cn.fooltech.fool_ops.domain.rate.vo.RateMemberSumVo;
import cn.fooltech.fool_ops.domain.rate.vo.RateMemberVo;
import cn.fooltech.fool_ops.utils.DateUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;
/**
 * 
 * <p>执行人效益分析</p>
 * @author yrl
 * @version 1.0
 * @date 2016年6月29日
 */
@Service
public class RateMemberService extends BaseService<RateMember, RateMemberVo, String>{
	 @Autowired
	 private RateMemberRepository  repository;

	/**
	 * 事件开始办理
	 * @throws Exception 
	 */
	public void startExcute(Task task, Object ...params) {
		RateMember rm = new RateMember();
		Date antipateEndTime = formatDate(task.getAntipateEndTime());
		Date antipateStartTime = formatDate(task.getAntipateStartTime());
		
		long totalTime = subDate(antipateEndTime, antipateStartTime) + 1;
		rm.setEvent(task);
		rm.setUser(task.getUndertaker());
		rm.setEventBeginDate(task.getActualStartTime());
		rm.setEventPlanStartDate(task.getAntipateStartTime());
		rm.setEventPlanEndDate(task.getAntipateEndTime());
		rm.setPlanTotalTime(new BigDecimal(subDate(task.getAntipateStartTime(), task.getAntipateEndTime())));
		rm.setEventsNumber(0);
		rm.setDelayNumber(0);
		rm.setIsComplete(0);
		rm.setIsCalc(0);
		rm.setAccId(task.getFiscalAccount().getFid());
		rm.setOrgId(task.getOrg().getFid());
		rm.setPlanTotalTime(new BigDecimal(totalTime));
		if(task.getStatus() == Task.STATUS_DELAYED_UNFINISH) {
			rm.setIsComplete(RateMember.NOT_COMPLETE);
			rm.setDelayNumber(RateMember.DELAY);
			Date today = formatDate(new Date());
			long delayDay = subDate(today, antipateEndTime) + 1;
			rm.setDelayTime(new BigDecimal(delayDay));
		}
		save(rm);
	}
	
	/**
	 * 事件结束
	 */
	public void endExcute(Task task) {
		
		List<RateMember> list = repository.queryByEventId(task.getFid());
		Date originalEndTime = formatDate(task.getOriginalEndTime());
		Date actualEndTime = formatDate(task.getActualEndTime());
		int status = task.getStatus();
		for(RateMember rm : list) {
			if(task.getStatus().equals(Task.STATUS_FINISHED)) {
				rm.setIsComplete(RateMember.IS_COMPLETE);
				rm.setEventCompleteDate(task.getActualEndTime());
				rm.setEventsNumber(1);
				long delayDay = subDate(actualEndTime, originalEndTime);
				rm.setDelayTime(new BigDecimal(delayDay));
				if(delayDay>0&&rm.getDelayTime()==BigDecimal.ZERO){
					rm.setCalcDelayNumber(1);
				}else if(delayDay<=0){
					rm.setCalcDelayNumber(0);
				}else{
					rm.setCalcDelayNumber(rm.getDelayNumber());
				}
				rm.setDelayTime(new BigDecimal(subDate(rm.getEventCompleteDate(),rm.getEventPlanEndDate())));
				double calcDelayNumber=rm.getCalcDelayNumber()+6d;
				 BigDecimal efficiency=new BigDecimal(Math.pow((calcDelayNumber/7d), rm.getCalcDelayNumber()-1)).multiply(rm.getDelayTime());
				 rm.setEfficiency(efficiency);
			}else if(task.getStatus().equals(Task.STATUS_STOPED)){
				rm.setIsComplete(RateMember.STOP_TASK);
				rm.setEventCompleteDate(task.getActualEndTime());
				rm.setEventsNumber(1);
				long delayDay = subDate(actualEndTime, originalEndTime);
				rm.setDelayTime(new BigDecimal(delayDay));
				if(delayDay>0&&rm.getDelayTime()==BigDecimal.ZERO){
					rm.setCalcDelayNumber(1);
				}else if(delayDay<=0){
					rm.setCalcDelayNumber(0);
				}
				else{
					rm.setCalcDelayNumber(rm.getDelayNumber());
				}
				rm.setDelayTime(new BigDecimal(subDate(rm.getEventCompleteDate(),rm.getEventPlanEndDate())));
				double calcDelayNumber=rm.getCalcDelayNumber()+6d;
				BigDecimal efficiency=new BigDecimal(Math.pow((calcDelayNumber/7d), rm.getCalcDelayNumber()-1)).multiply(rm.getDelayTime());
				rm.setEfficiency(efficiency);
			}

			Date c = rm.getEventCompleteDate();
			save(rm);
		}
		
	}
	
	/**
	 * 将date转为yyyy-MM-dd格式
	 */
	public Date formatDate(Date date) {
		Date newDate = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
			String format = sdf.format(date);
			newDate = sdf.parse(format);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newDate;
	}
	
	/**
	 * 日期相减，得到相差的天数
	 * @return long天数
	 */
	public long subDate(Date date1, Date date2) {
		return (date1.getTime() - date2.getTime()) / (24*3600*1000);
	}

	@Override
	public RateMemberVo getVo(RateMember entity) {
		int delayNumber = entity.getDelayNumber();
		int eventsNumber = entity.getEventsNumber();
		BigDecimal delayTime = entity.getDelayTime();
		BigDecimal totalTime = entity.getPlanTotalTime();
		Date eventPlanEndDate = entity.getEventPlanEndDate();
		Date eventCompleteDate = entity.getEventCompleteDate();
		Integer comTime = null;
		Date calculateComDate = eventCompleteDate;//如果事件还没完成就当延误算
		if(entity.getIsComplete() == RateMember.NOT_COMPLETE) {
			calculateComDate = new Date();
		}
		if(eventPlanEndDate != null) {
			try {
				comTime = DateUtil.daysBetween(calculateComDate, eventPlanEndDate);
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		RateMemberVo vo = new RateMemberVo();
		vo.setFid(entity.getFid());
		vo.setUserId(entity.getUser().getFid());
		vo.setUserName(entity.getUser().getUserName());
		vo.setEventName(entity.getEvent().getName());
		vo.setEventPlanStartDate(entity.getEventPlanStartDate());
		vo.setEventPlanEndDate(eventPlanEndDate);
		vo.setEventBeginDate(entity.getEventBeginDate());
		vo.setEventCompleteDate(eventCompleteDate);
		vo.setEventsNumber(eventsNumber);
		vo.setTotalTime(totalTime);
		vo.setDelayTime(delayTime);
		vo.setDelayNumber(delayNumber);
		vo.setIsComplete(entity.getIsComplete());
		if(comTime != null) {
			if(comTime > 0) {
				vo.setIsDelay(RateMember.DELAY);
			}else if(comTime < 0) {
				vo.setIsDelay(RateMember.ADVANCE);
			}else if(comTime == 0) {
				vo.setIsDelay(RateMember.ONTIME);
			}
		}
//		if(delayTime != null && totalTime != null) {
//			BigDecimal p1 = new BigDecimal(delayNumber / eventsNumber * 0.6);
//			BigDecimal parm = new BigDecimal(new Double(0.4));
//			BigDecimal parm2 = new BigDecimal(1);
//			BigDecimal parm3 = new BigDecimal(100);
//			//{1–[(延误次数/事件总数)*0.6 ＋ (延误时间/总时间)*0.4]}*100%
//			BigDecimal rate = parm3.multiply(parm2.subtract(p1.add(delayTime.divide(totalTime).multiply(parm))));
//			String rateStr = rate.setScale(0, BigDecimal.ROUND_HALF_UP) + "";
//			
//			vo.setRate(rateStr);
//		}
		return vo;
	}

	@Override
	public CrudRepository<RateMember, String> getRepository() {
		return repository;
	}
	
	public List<RateMemberSumVo> getSumVos(List<RateMemberSum> entities) {
		List<RateMemberSumVo> vos = Lists.newArrayList();
		if (entities != null && !entities.isEmpty()) {
			for (RateMemberSum e : entities) {
				vos.add(getSumVo(e));
			}
		}
		return vos;
	}
	
	public RateMemberSumVo getSumVo(RateMemberSum rmSum) {
		RateMemberSumVo vo = new RateMemberSumVo();
		Double allTime = rmSum.getAllTime().doubleValue();
		Double delayTime = rmSum.getDelayTime().doubleValue();
		Double delayNum = rmSum.getDelayNum().doubleValue();
		Double eventsNum = rmSum.getEventsNum().doubleValue();
//		if(delayTime < 0) {
//			delayTime = 0.0;
//		}
		vo.setUserId(rmSum.getUserId());
		vo.setUserName(rmSum.getUserName());
		vo.setAllTime(Math.ceil(allTime));
		vo.setDelayTime(Math.ceil(delayTime));
		vo.setDelayNum(Math.ceil(delayNum));
		vo.setEventsNum(Math.ceil(eventsNum));
		//{1–[(延误次数/事件总数)*0.6 ＋ (延误时间/总时间)*0.4]}*100%
		Double rate = (1 - ((delayNum / eventsNum) * 0.6 + (delayTime / allTime) * 0.4)) * 100;
		vo.setRate(Math.ceil(rate));
		return vo;
	}
	
	public Page<RateMember> queryUserEvents(Date startDate, Date endDate, String userId, PageParamater pageParamater) {
		
		Sort sort = new Sort(Direction.DESC, "eventBeginDate");
		PageRequest pageRequest = getPageRequest(pageParamater, sort);
		String accId = SecurityUtil.getFiscalAccountId();
		return repository.queryUserEvents(accId, userId, startDate, endDate, pageRequest);
	}
	@Deprecated
	public Page<RateMemberSum> query(Date startDate, Date endDate, String memberIds, PageParamater pageParamater){
		PageRequest pageRequest = getPageRequest(pageParamater);
		String accId = SecurityUtil.getFiscalAccountId();
		Splitter splitter = Splitter.on(",").trimResults().omitEmptyStrings();
		List<String> memberIdList = splitter.splitToList(memberIds);
		Page<RateMemberSum> page = repository.query(startDate, endDate, memberIdList, accId, pageRequest);
		return page;
	}
	/**
	 * 根据事件ID,人员ID查找人员效率
	 */
	public RateMember queryByUserAndTask(String userId,String taskId,int isComplete){
		return repository.queryByUserAndTask(userId,taskId,isComplete,SecurityUtil.getCurrentOrgId());
	}
	
	
	
	
	
	
	/**
	 * 数据预处理
	 */
	@Transactional
	public RequestResult rateMemberDataPreprocess(){
				repository.dataPreprocessIsCalc(SecurityUtil.getCurrentOrgId());
				repository.updateIsCalcByPlanEndDate(DateUtil.getSimpleDate(new Date()),SecurityUtil.getCurrentOrgId());
				List<RateMember> rateMembers=repository.findByisCalcY(SecurityUtil.getCurrentOrgId());
				for(RateMember  rateMember:rateMembers){
					BigDecimal delayTime=new BigDecimal(subDate(rateMember.getEventCompleteDate(),rateMember.getEventPlanEndDate()));
					BigDecimal efficiency=BigDecimal.ZERO;
					rateMember.setDelayTime(delayTime);
					if((rateMember.getDelayTime().compareTo(BigDecimal.ZERO))==1){
						if(rateMember.getDelayNumber()==0){
							rateMember.setCalcDelayNumber(1);
						}else{
							rateMember.setCalcDelayNumber(rateMember.getDelayNumber());
						}
					}else{
						rateMember.setCalcDelayNumber(0);
					}
					double calcDelayNumber=rateMember.getCalcDelayNumber()+6d;
					efficiency=new BigDecimal(Math.pow((calcDelayNumber/7d), rateMember.getCalcDelayNumber()-1)).multiply(rateMember.getDelayTime());
					rateMember.setEfficiency(efficiency);
					repository.save(rateMember);
				}
				List<RateMember> entitys=repository.findByIsComplete(SecurityUtil.getCurrentOrgId());
				for(RateMember  entity:entitys){
					entity.setIsCalc(1);
					repository.save(entity);	
				}
		return buildSuccessRequestResult("成功");
	}
	
}
