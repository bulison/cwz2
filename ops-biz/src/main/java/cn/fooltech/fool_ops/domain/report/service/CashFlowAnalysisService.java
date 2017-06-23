package cn.fooltech.fool_ops.domain.report.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.report.repository.CashFlowAnalysisRepository;
import cn.fooltech.fool_ops.domain.report.vo.CashFlowAnalysisVo;
import cn.fooltech.fool_ops.domain.report.vo.GoodsStockVo;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;

/** 
* <p>现金流量汇总分析<p> 
* @author cwz 
* @version 创建时间：2017年3月21日 上午9:07:40 
* 
*/
@Service
public class CashFlowAnalysisService {

	@Autowired
	private CashFlowAnalysisRepository analysisRepository;
	
	/**
	 * 获取列表
	 */
	public List<CashFlowAnalysisVo> getList(CashFlowAnalysisVo vo,String orgId,String accId, PageParamater paramater) throws Exception {

		// 分页
		int pageSize = paramater.getRows();
		int pageNo = paramater.getPage();
		int first = (pageNo - 1) * pageSize;
		List<Object[]> list = null;
	    /**
	     * 分页标识，默认分页<br>
	     * 0 不分页  1 分页
	     */
		if(vo.getFlag() == 1){
			list = analysisRepository.queryDetail(orgId, accId, vo.getStartDay(),vo.getEndDay(), first, pageSize);
		}
		else{
			list = analysisRepository.queryDetail(orgId, accId, vo.getStartDay(),vo.getEndDay(), 0, Integer.MAX_VALUE);
		}
		return getVos(list);
	}
	public long countList(CashFlowAnalysisVo vo){
		String orgId = SecurityUtil.getCurrentOrgId();
		String accId = SecurityUtil.getFiscalAccountId();
		return analysisRepository.count(orgId, accId, vo.getStartDay(), vo.getEndDay());
	}
	/**
	 * 获得Vo的list
	 */
	public List<CashFlowAnalysisVo> getVos(List<Object[]> list) {
		List<CashFlowAnalysisVo> vos = Lists.newArrayList();
		if (list != null){
			for (Object[] objects : list) {
				CashFlowAnalysisVo vo = getVo(objects);
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
	public CashFlowAnalysisVo getVo(Object[] obj) {
		if (obj == null)
			return null;
		CashFlowAnalysisVo vo = new CashFlowAnalysisVo();
		try {
			vo.setColour(obj[0]!=null?obj[0].toString():"");
			vo.setPaymentDate(obj[1]!=null?obj[1].toString():"");
			vo.setIncome(NumberUtil.stripTrailingZeros(obj[2], 2));
			vo.setExpenditure(NumberUtil.stripTrailingZeros(obj[3], 2));
			vo.setAmount(NumberUtil.stripTrailingZeros(obj[4], 2));
			vo.setWarningQuota(NumberUtil.stripTrailingZeros(obj[5], 2));
			return vo;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
