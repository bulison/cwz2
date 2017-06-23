package cn.fooltech.fool_ops.domain.report.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.report.entity.SysReport;
import cn.fooltech.fool_ops.domain.report.entity.UserTemplate;
import cn.fooltech.fool_ops.domain.report.repository.UserTemplateRepository;
import cn.fooltech.fool_ops.domain.report.vo.UserTemplateDetailVo;
import cn.fooltech.fool_ops.domain.report.vo.UserTemplateVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.StrUtil;


/**
 * @author tjr
 */
@Service
public class UserTemplateService extends BaseService<UserTemplate, UserTemplateVo, String> {
	
	@Autowired
	private UserTemplateRepository userTemplateRepo;

	@Autowired
	private  UserTemplateDetailService utdService;

	public RequestResult save(UserTemplateVo vo) throws Exception {
		Assert.notNull(vo, "保存参数不正确");
		
		List<UserTemplateDetailVo> conditionList = vo.getConditionList();
		Assert.notNull(conditionList, "模板详情不能为空");

		UserTemplate userTemplate = null;
		if (StrUtil.isEmpty(vo.getFid())) {
			userTemplate = new UserTemplate();
			userTemplate.setCreateTime(new Date());
			userTemplate.setCreator(SecurityUtil.getCurrentUser());
			userTemplate.setOrg(SecurityUtil.getCurrentOrg());
			userTemplate.setReport(new SysReport(vo.getReportId()));
		} else {
			userTemplate = userTemplateRepo.findOne(vo.getFid());
		}
		
		userTemplate.setTemplatename(vo.getTemplateName());

		userTemplateRepo.save(userTemplate);

		utdService.saveUserTemplateDetails(userTemplate.getFid(), conditionList);

		RequestResult result = new RequestResult();
		result.setData(userTemplate.getFid());
		return result;
	}

	public Page<UserTemplateVo> query(UserTemplateVo vo, PageParamater paramater) {
		String orgId = SecurityUtil.getCurrentOrgId();
		String reportId = vo.getReportId();
		Sort sort = new Sort(Direction.DESC, "createTime");
		PageRequest pageRequest = getPageRequest(paramater, sort);

		return getPageVos(userTemplateRepo.findPageBy(orgId, reportId, pageRequest), pageRequest);
	}

	/**
	 * 查找列表
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public List<UserTemplateVo> list(UserTemplateVo vo) throws Exception {

		String orgId = SecurityUtil.getCurrentOrgId();
		String reportId = vo.getReportId();
		Sort sort = new Sort(Direction.DESC, "createTime");
		List<UserTemplate> userTemplates = userTemplateRepo.findBy(orgId, reportId, sort);
		return getVos(userTemplates);
	}


	@Override
	public UserTemplateVo getVo(UserTemplate entity) {
		if (entity == null)
			return null;
		UserTemplateVo vo = new UserTemplateVo();
		vo.setCreateTime(entity.getCreateTime());
		vo.setCreatorId(entity.getCreator().getFid());
		vo.setCreatorName(entity.getCreator().getUserName());
		vo.setFid(entity.getFid());
		vo.setOrgId(entity.getOrg().getFid());
		vo.setOrgName(entity.getOrg().getOrgName());
		vo.setReportId(entity.getReport().getFid());
		vo.setReportName(entity.getReport().getReportName());
		vo.setTemplateName(entity.getTemplatename());
		return vo;
	}

	@Override
	public CrudRepository<UserTemplate, String> getRepository() {
		return userTemplateRepo;
	}
}
