package cn.fooltech.fool_ops.domain.report.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.common.entity.PageBean;
import cn.fooltech.fool_ops.domain.common.entity.ResultObject;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalAccountingSubjectService;
import cn.fooltech.fool_ops.domain.fiscal.vo.FiscalAccountingSubjectVo;
import cn.fooltech.fool_ops.domain.report.entity.UserTemplateDetail;
import cn.fooltech.fool_ops.domain.report.repository.SysReportSqlRepository;
import cn.fooltech.fool_ops.domain.report.vo.DetailCategoryAccountVo;
import cn.fooltech.fool_ops.domain.report.vo.SysReportQueryVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;


/**
 * <p>明细分类账报表-网页服务类</p>
 * @author rqh
 * @version 1.0
 * @date 2016年2月3日
 */
@Service
public class DetailCategoryAccountService implements BaseReportQuery{
	
	@Autowired
	private SysReportSqlRepository reportSqlRepo;
	
	@Autowired
	private FiscalAccountingSubjectService subjectService;
	
	/**
	 * 列表查询
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ResultObject qurey(SysReportQueryVo vo, DetailCategoryAccountVo accountVo, PageParamater paramater){
		int currentPage = paramater.getPage(); //当前页
		int pageSize = paramater.getRows(); //分页大小
		int start = (currentPage - 1) * pageSize; //起始位置
		
		UserTemplateDetail condition = getOtherCondition(accountVo);
		List<UserTemplateDetail> conditions = getConditions(vo);
		conditions.add(condition);
		
		List<Object[]> datas = null;
		String orgId = SecurityUtil.getCurrentOrgId();
		String accId = SecurityUtil.getFiscalAccountId();
		
		if(vo.getFlag() == 1){
			datas = reportSqlRepo.queryByPage(orgId, accId, conditions, vo.getSysReportId(), start, pageSize);
		}
		else{
			datas = reportSqlRepo.queryAll(orgId, accId, conditions, vo.getSysReportId());
		}
		
		int totalRows = reportSqlRepo.count(orgId, accId, conditions, vo.getSysReportId());
		PageBean pageBean = new PageBean(totalRows, pageSize, currentPage);
		ResultObject result = new ResultObject(datas, pageBean);
		
		String subjectId = condition.getValue();
		if(StringUtils.isNotBlank(subjectId)){
			FiscalAccountingSubject subject = subjectService.get(subjectId);
			result.getData().put("subject", subjectService.getVo(subject));
		}
		return result;
	}
	
	/**
	 * 获取科目ID的查询条件
	 * @param accountVo
	 * @return
	 */
	public UserTemplateDetail getOtherCondition(DetailCategoryAccountVo accountVo){
		FiscalAccountingSubject subject = null;
		
		String curSubjectId = accountVo.getCurSubjectId();
		String startCode = accountVo.getSubjectStartCode();
		String endCode = accountVo.getSubjectEndCode();
		Integer level = accountVo.getSubjectLevel();
		
		if(accountVo.getOperationFlag() == 0 && StringUtils.isNotBlank(accountVo.getCurSubjectId())){
			//当前科目
			subject = subjectService.get(accountVo.getCurSubjectId());
		}
		else if(accountVo.getOperationFlag() == 1){
			//下一个科目
			subject = subjectService.getNextSubject(curSubjectId, startCode, endCode, level);
		}
		else if(accountVo.getOperationFlag() == -1){
			//上一个科目
			subject = subjectService.getLastSubject(curSubjectId, startCode, endCode, level);
		}
		
		if(subject == null && StringUtils.isNotBlank(accountVo.getCurSubjectId())){
			//默认科目
			subject = subjectService.get(accountVo.getCurSubjectId());
		}
		
		String subjectId = subject == null ? "" : subject.getFid();
		
		UserTemplateDetail detail = new UserTemplateDetail();
		detail.setTableName("tbd_fiscal_accounting_subject");
		detail.setFieldName("fid");
		detail.setAliasName("FSUBJECTID");
		detail.setValue(subjectId);
		return detail;
	}
	

}
