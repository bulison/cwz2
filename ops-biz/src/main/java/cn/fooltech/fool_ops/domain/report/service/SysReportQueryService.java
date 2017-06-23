package cn.fooltech.fool_ops.domain.report.service;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.common.entity.PageBean;
import cn.fooltech.fool_ops.domain.common.entity.ResultObject;
import cn.fooltech.fool_ops.domain.report.entity.SysReport;
import cn.fooltech.fool_ops.domain.report.entity.SysReportSql;
import cn.fooltech.fool_ops.domain.report.entity.UserTemplateDetail;
import cn.fooltech.fool_ops.domain.report.repository.SysReportSqlRepository;
import cn.fooltech.fool_ops.domain.report.script.ScriptProcessor;
import cn.fooltech.fool_ops.domain.report.vo.SysReportQueryVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;

/**
 * <p>系统报表查询网页服务类</p>
 * @version 1.0
 */
@Service
public class SysReportQueryService implements BaseReportQuery {
	
	private static final Logger logger = LoggerFactory.getLogger(SysReportQueryService.class);
	
	@Autowired
	private SysReportSqlRepository reportSqlRepo; 
	
	/**
	 * 系统报表服务类
	 */
	@Autowired
	private SysReportService reportService;
	
	
	/**
	 * 列表查询
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(readOnly=true)
	public ResultObject query(SysReportQueryVo vo, PageParamater paramater){
		
		int currentPage = paramater.getPage(); //当前页
		int pageSize = paramater.getRows(); //分页大小
		int start = (currentPage - 1) * pageSize; //起始位置
		
		String orgId = SecurityUtil.getCurrentOrgId();
		String accId = SecurityUtil.getFiscalAccountId();
		
		List<UserTemplateDetail> conditions = getConditions(vo);
		
		List<Object[]> datas = null;
		Integer totalRows = 0;
		SysReport sysReport = reportService.get(vo.getSysReportId());
		
		if(vo.getFlag() == 1 && (sysReport.getShowPage()==null || sysReport.getShowPage()==1)){
			datas = reportSqlRepo.queryByPage(orgId, accId, conditions, vo.getSysReportId(), start, pageSize);
			totalRows = reportSqlRepo.count(orgId, accId, conditions, vo.getSysReportId());
		}
		else{
			datas = reportSqlRepo.queryAll(orgId, accId, conditions, vo.getSysReportId());
			totalRows = datas.size();
		}
		
		PageBean pageBean = new PageBean(totalRows, pageSize, currentPage);
		ResultObject resultObject = new ResultObject(datas, pageBean);
		String javaScript = sysReport.getJavaScript();
		
		if(!Strings.isNullOrEmpty(javaScript)){
			try {
				Class clazz = Class.forName(javaScript);
				
				ScriptProcessor processor = (ScriptProcessor) clazz.newInstance();
				Map<String, Object> map = processor.process(datas);
				resultObject.setData(map);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		if(datas.size()>0){
			Object[] objects = datas.get(datas.size()-1);
			if(objects.length>0){
				String obj=(String) objects[0];
				if(obj.equals("试算")){
					Map<String, Object> map = Maps.newHashMap();
					map.put("message", objects[1]);
					resultObject.setData(map);
				}
			}
		}

		return resultObject;
	}
	
	/**
	 * 查询报表所有数据
	 * @param vo
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<Object[]> queryAll(SysReportQueryVo vo){
		List<UserTemplateDetail> conditions = getConditions(vo);
		String orgId = SecurityUtil.getCurrentOrgId();
		String accId = SecurityUtil.getFiscalAccountId();
		List<Object[]> datas = reportSqlRepo.queryAll(orgId, accId, conditions, vo.getSysReportId());
		return datas;
	}

	
}
