package cn.fooltech.fool_ops.domain.report.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.component.exception.DataNotExistException;
import cn.fooltech.fool_ops.component.exception.ThrowException;
import cn.fooltech.fool_ops.domain.base.service.PageService;
import cn.fooltech.fool_ops.domain.report.entity.SysReport;
import cn.fooltech.fool_ops.domain.report.entity.SysReportSql;
import cn.fooltech.fool_ops.domain.report.repository.SysReportSqlRepository;
import cn.fooltech.fool_ops.domain.report.vo.SysReportSqlVo;


/**
 * <p>系统报表查询SQL网页服务类</p>
 * @version 1.0
 * @date 2015年10月12日
 */
@Service
public class SysReportSqlService  implements BaseReportQuery,PageService{
	
	/**
	 * 系统报表服务类
	 */
	@Autowired
	private SysReportService reportService;
	
	@Autowired
	private SysReportSqlRepository reportSqlRepo;
	
	/**
	 * 分页查询
	 * @param reportId
	 * @return
	 */
	public SysReportSqlVo findBySysReportId(String reportId){
		SysReportSql sql = reportSqlRepo.findTopBySysReportId(reportId);
		return getVo(sql);
	}
	
	
	/**
	 * 新增、编辑
	 * @param vo
	 * @return
	 */
	public RequestResult save(SysReportSqlVo vo){
		String fid = vo.getFid();
		String sql = vo.getSql();
		String sysReportId = vo.getSysReportId(); //系统报表ID
		
		//校验数据
		RequestResult result = checkByRule(vo);
		if(result.getReturnCode() == RequestResult.RETURN_FAILURE){
			return result;
		}
		
		SysReportSql entity = null;
		if(StringUtils.isBlank(fid)){
			entity = new SysReportSql();
		}
		else{
			entity = reportSqlRepo.findOne(fid);
		}
		entity.setSql(sql);
		//系统报表
		if(StringUtils.isNotBlank(sysReportId)){
			SysReport sysReport = reportService.get(sysReportId);
			entity.setSysReport(sysReport);
		}
		
		reportSqlRepo.save(entity);
		return result;
	}
	
	/**
	 * 校验数据
	 * @param vo
	 * @return
	 */
	public RequestResult checkByRule(SysReportSqlVo vo){
		if(StringUtils.isBlank(vo.getSql())){
			return buildFailRequestResult("查询语句不能为空!");
		}
		if(isSqlExist(vo.getSysReportId(), vo.getFid())){
			return buildFailRequestResult("该报表对应的SQL语句已存在!");
		}
		return buildSuccessRequestResult();
	}
	
	/**
	 * 判断
	 * @param reportId
	 * @param excludeId
	 * @return
	 */
	public boolean isSqlExist(String reportId, String excludeId){
		Long count = null;
		if(Strings.isNullOrEmpty(excludeId)){
			count = reportSqlRepo.countByReportId(reportId);
		}else{
			count = reportSqlRepo.countByReportId(reportId, excludeId);
		}
		if(count!=null && count>0){
			return true;
		}
		return false;
	}
	
	
	/**
	 * 多个实体转vo
	 * @param entities
	 * @return
	 */
	public List<SysReportSqlVo> getVos(List<SysReportSql> entities){
		List<SysReportSqlVo> list = new ArrayList<SysReportSqlVo>();
		if(CollectionUtils.isNotEmpty(entities)){
			for(SysReportSql entity : entities){
				list.add(getVo(entity));
			}
		}
		return list;
	}
	
	/**
	 * 单个实体转vo
	 * @param entity
	 * @return
	 */
	public SysReportSqlVo getVo(SysReportSql entity){
		if(entity==null)return new SysReportSqlVo();
		SysReportSqlVo vo = new SysReportSqlVo();
		vo.setFid(entity.getFid());
		vo.setSql(entity.getSql());
		//系统报表
		SysReport sysReport = entity.getSysReport();
		if(sysReport != null){
			vo.setSysReportId(sysReport.getFid());
			vo.setSysReportName(sysReport.getReportName());
		}
		return vo;
	}

	public SysReportSqlVo getById(String id, ThrowException thro){
		SysReportSql entity = reportSqlRepo.findOne(id);
		if(entity==null && thro==ThrowException.Throw){
			throw new DataNotExistException();
		}
		return getVo(entity);
	}
	
	/**
	 * 根据ID删除
	 * @param id
	 */
	public RequestResult delete(String id){
		reportSqlRepo.delete(id);
		return buildSuccessRequestResult();
	}
}
