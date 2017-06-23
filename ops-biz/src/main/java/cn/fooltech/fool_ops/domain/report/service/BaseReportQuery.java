package cn.fooltech.fool_ops.domain.report.service;

import java.util.ArrayList;
import java.util.List;

import cn.fooltech.fool_ops.domain.report.entity.UserTemplateDetail;
import cn.fooltech.fool_ops.domain.report.vo.SysReportQueryVo;
import cn.fooltech.fool_ops.domain.report.vo.UserTemplateDetailVo;


/**
 * <p>报表查询抽象服务类</p>
 * @author rqh
 * @version 1.0
 * @date 2016年2月3日
 */
public interface BaseReportQuery{
	
	/**
	 * 获取报表查询条件
	 * @param vo
	 * @return
	 */
	public default List<UserTemplateDetail> getConditions(SysReportQueryVo queryVo){
		List<UserTemplateDetail> result = new ArrayList<UserTemplateDetail>();
		List<UserTemplateDetailVo> details = queryVo.getConditionDetail();
		for(UserTemplateDetailVo detail : details){
			result.add(convert(detail));
		}
		return result;
	}
	
	/**
	 * 查询条件转换
	 * @param vo
	 * @return
	 */
	public default UserTemplateDetail convert(UserTemplateDetailVo vo){
		UserTemplateDetail detail = new UserTemplateDetail();
		detail.setTableName(vo.getTableName());
		detail.setFieldName(vo.getFieldName());
		detail.setAliasName(vo.getAliasName());
		detail.setValue(vo.getValue());
		return detail;
	}
	
}
