package cn.fooltech.fool_ops.domain.report.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.report.entity.FiscalMultiColumnDetail;
import cn.fooltech.fool_ops.domain.report.repository.MultiColumnReportRepository;
import cn.fooltech.fool_ops.domain.report.vo.FiscalMultiColumnDetailVo;
import cn.fooltech.fool_ops.domain.report.vo.MultiColumnReportVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import net.sf.json.JSONArray;


/**
 * <p>多栏明细账报表网页服务类</p>
 * @author rqh
 * @version 1.0
 * @date 2016年1月26日
 */
@Service
public class MultiColumnReportService  {
	
	private static final int MAX = 65535;
	
	@Autowired
	private MultiColumnReportRepository reportRepo;
	
	@Autowired
	private FiscalMultiColumnDetailService multiColumnDetailService;
	
	/**
	 * 报表结果查询
	 * @param vo
	 * @return
	 */
	public PageJson query(MultiColumnReportVo vo, PageParamater paramater){
		String orgId = SecurityUtil.getCurrentOrgId();
		String accountId = SecurityUtil.getFiscalAccountId();
		String startFiscalPeriodId = vo.getStartFiscalPeriodId();
		String endFiscalPeriodId = vo.getEndFiscalPeriodId();
		String settingId = vo.getSettingId();
		String voucherStatus = vo.getStatus();
		if(voucherStatus == null){
			voucherStatus = "";
		}

		if(StringUtils.isBlank(settingId)
				|| StringUtils.isBlank(startFiscalPeriodId)
				|| StringUtils.isBlank(endFiscalPeriodId)){
			return new PageJson();
		}

		int currentPage = paramater.getPage(); //当前页
		int pageSize = paramater.getRows(); //分页大小
		int start = (currentPage - 1) * pageSize; //起始位置
		
		List<Object[]> datas = null;
		if(vo.getFlag() == 1){
			datas = reportRepo.query(orgId, accountId, startFiscalPeriodId, endFiscalPeriodId, settingId, voucherStatus, start, pageSize);
		}
		else{
			datas = reportRepo.query(orgId, accountId, startFiscalPeriodId, endFiscalPeriodId, settingId, voucherStatus, 0, MAX);
		}
		Long totalRows = reportRepo.count(orgId, accountId, startFiscalPeriodId, endFiscalPeriodId, settingId, voucherStatus);
		
		PageJson pageJson = new PageJson();
		pageJson.setTotal(totalRows);
		pageJson.setRows(JSONArray.fromObject(getVos(datas)));
		return pageJson;
	}
	
	/**
	 * 获取多个vo
	 * @param datas
	 * @return
	 */
	public String getVos(List<Object[]> datas){
		StringBuffer jsonData = new StringBuffer();
		jsonData.append("[");
		for(int n = 0; n<datas.size(); n++){
			jsonData.append("{");
			Object[] data = datas.get(n);
			for(int i=0; i<data.length; i++){
				if(data[i] == null){
					jsonData.append("\"field" + i + "\":\" \"");
				}
				else{
					jsonData.append("\"field" + i + "\":\"" + data[i] + "\"");
				}
				if(i != data.length - 1){
					jsonData.append(",");
				}
			}
			jsonData.append("}");
			if(n != datas.size() - 1){
				jsonData.append(",");
			}
		}
		jsonData.append("]");
		return jsonData.toString();
	}
	
	/**
	 * 获取固定列标题
	 * @return
	 */
	public Map<String, String> getFixTitiles(){
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("field0", "日期");
		map.put("field1", "凭证字号");
		map.put("field2", "摘要");
		map.put("field3", "借方金额");
		map.put("field4", "贷方金额");
		map.put("field5", "余额方向");
		map.put("field6", "余额");
		return map;
	}
	
	/**
	 * 获取贷方动态列标题
	 * @param multiColumnSettingId 多栏明细设置ID
	 * @return
	 */
	public Map<String, String> getDynamicCreditTitles(String multiColumnSettingId, int startIndex){
		List<FiscalMultiColumnDetailVo> details = multiColumnDetailService
				.getDynamicTitles(multiColumnSettingId, FiscalAccountingSubject.DIRECTION_LOAN);
		
		Map<String, String> map = new LinkedHashMap<String, String>();
		for(FiscalMultiColumnDetailVo detail : details){
			map.put("field" + startIndex++, detail.getAuxiliaryAttrName());
		}
		return map;
	}
	
	/**
	 * 获取借方动态列标题
	 * @param multiColumnSettingId 多栏明细设置ID
	 * @return
	 */
	public Map<String, String> getDynamicDebitTitles(String multiColumnSettingId, int startIndex){
		List<FiscalMultiColumnDetailVo> details = multiColumnDetailService
				.getDynamicTitles(multiColumnSettingId, FiscalAccountingSubject.DIRECTION_BORROW);
		
		Map<String, String> map = new LinkedHashMap<String, String>();
		for(FiscalMultiColumnDetailVo detail : details){
			map.put("field" + startIndex++, detail.getAuxiliaryAttrName());
		}
		return map;
	}
	
}
