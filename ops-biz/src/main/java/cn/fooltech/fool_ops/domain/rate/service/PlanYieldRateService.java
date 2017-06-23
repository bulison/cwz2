package cn.fooltech.fool_ops.domain.rate.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.rate.repository.PlanYieldRateRepository;
import cn.fooltech.fool_ops.domain.rate.vo.PlanYieldRateVo;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;

/**
 * 
 * <p>流程收益率分析【报表】 </p>  
 * @author cwz
 * @date 2017年4月17日
 */
@Service
public class PlanYieldRateService {

	@Autowired
	private PlanYieldRateRepository rateRepository;
	
	/**
	 * 获取列表
	 */
	public List<PlanYieldRateVo> getList(PlanYieldRateVo vo,String orgId,String accId, PageParamater paramater) throws Exception {

		// 分页
		int pageSize = paramater.getRows();
		int pageNo = paramater.getPage();
		int first = (pageNo - 1) * pageSize;
		List<Object[]> list = null;
		String startDay = vo.getStartDay();
		String endDay = vo.getEndDay();
		String planCode = vo.getPlanCode();
		String planName = vo.getPlanName();
		String initiate = vo.getInitiate();
		String principal = vo.getPrincipal();
		String status = vo.getStatus();
		Integer sidx = vo.getSidx();
		Integer sord = vo.getSord();
	    /**
	     * 分页标识，默认分页<br>
	     * 0 不分页  1 分页
	     */
		if(vo.getFlag() == 1){
			list = rateRepository.queryDetail(orgId, accId, startDay, endDay, planCode, planName, initiate, principal, status, sidx, sord, first, pageSize);
		}
		else{
			list = rateRepository.queryDetail(orgId, accId, startDay, endDay, planCode, planName, initiate, principal, status, sidx, sord, 0, Integer.MAX_VALUE);
		}
		return getVos(list);
	}
	public long countList(PlanYieldRateVo vo){
		String orgId = SecurityUtil.getCurrentOrgId();
		String accId = SecurityUtil.getFiscalAccountId();
		String startDay = vo.getStartDay();
		String endDay = vo.getEndDay();
		String planCode = vo.getPlanCode();
		String planName = vo.getPlanName();
		String initiate = vo.getInitiate();
		String principal = vo.getPrincipal();
		String status = vo.getStatus();
		Integer sidx = vo.getSidx();
		Integer sord = vo.getSord();
		return rateRepository.count(orgId, accId, startDay, endDay, planCode, planName, initiate, principal, status, sidx, sord);
	}
	/**
	 * 获得Vo的list
	 */
	public List<PlanYieldRateVo> getVos(List<Object[]> list) {
		List<PlanYieldRateVo> vos = Lists.newArrayList();
		if (list != null){
			for (Object[] objects : list) {
				PlanYieldRateVo vo = getVo(objects);
				if(vo!=null){
					vos.add(vo);
				}
			}
		}
		return vos;
	}
	/**
	 * 获得Vo
	 */
	public PlanYieldRateVo getVo(Object[] obj) {
		if (obj == null)
			return null;
		PlanYieldRateVo vo = new PlanYieldRateVo();
		try {
			vo.setFid(obj[0]!=null?obj[0].toString():"");//流程计划ID
			vo.setPlanCode(obj[1]!=null?obj[1].toString():"");//流程编号
			vo.setPlanName(obj[2]!=null?obj[2].toString():"");//流程名称
			vo.setOutAmount(NumberUtil.stripTrailingZeros(obj[3], 2));//支出金额
			vo.setInAmount(NumberUtil.stripTrailingZeros(obj[4], 2));//收入金额
			vo.setProfit(NumberUtil.stripTrailingZeros(obj[5], 2));//利润
			vo.setEstimatedYieldrate(NumberUtil.stripTrailingZeros(obj[6], 2));//预计收益率
			vo.setEffectiveYieldrate(NumberUtil.stripTrailingZeros(obj[7], 2));//实际收益率
			vo.setReferenceYieldrate(NumberUtil.stripTrailingZeros(obj[8], 2));//参考收益率
			vo.setCurrentYieldRate(NumberUtil.stripTrailingZeros(obj[9], 2));//当前预计收益率
			vo.setPlantStartDate(obj[10]!=null?obj[10].toString():"");//当前预计收益率
			vo.setFantipateEndTime(obj[11]!=null?obj[11].toString():"");//计划结束日期
			vo.setFactualEndTime(obj[12]!=null?obj[12].toString():"");//实际完成日期	
			vo.setFextensionDays(obj[13]!=null?obj[13].toString():"");//延期天数
			vo.setFextensionCount(obj[14]!=null?obj[14].toString():"");//延期次数
			vo.setContractorsEfficiency(obj[15]!=null?obj[15].toString():"");//承办效率
			return vo;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
