package cn.fooltech.fool_ops.domain.report.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.report.entity.SysReport;
import cn.fooltech.fool_ops.domain.report.entity.UserTemplate;
import cn.fooltech.fool_ops.domain.report.entity.UserTemplateDetail;
import cn.fooltech.fool_ops.domain.report.repository.UserTemplateDetailRepository;
import cn.fooltech.fool_ops.domain.report.vo.UserTemplateDetailVo;
import cn.fooltech.fool_ops.utils.StrUtil;


/**
 * @author tjr
 */
@Service
public class UserTemplateDetailService extends BaseService<UserTemplateDetail, 
	UserTemplateDetailVo, String> {

	@Autowired
	private UserTemplateDetailRepository utdRepo;
	
	/**
	 * 根据模板ID 获取 模板查询条件
	 * @param templateId
	 * @return
	 * @throws Exception
	 */
	public List<UserTemplateDetailVo> getByUserTemplateId(String templateId)throws Exception{
		Assert.notNull(templateId, "模板ID不存在");
		
		List<UserTemplateDetail> list = utdRepo.findByUserTemplateId(templateId);
		return getVos(list);
	}
	
	/**
	 * 保存多个模板详情
	 * @param userTemplateId
	 * @param vo
	 * @throws Exception
	 */
	public void saveUserTemplateDetails(String userTemplateId,List<UserTemplateDetailVo> lists)throws Exception{
		Assert.notNull(userTemplateId, "模板ID不存在");
		delByUserTemplateId(userTemplateId);
		if(CollectionUtils.isNotEmpty(lists)){
			for (UserTemplateDetailVo userTemplateDetailVo : lists) {
				userTemplateDetailVo.setTemplateId(userTemplateId);
				save(userTemplateDetailVo);
			}
		}
	}
	
	/**
	 * 根据模板ID删除 模板查询条件
	 * @param templateId
	 * @throws Exception
	 */
	public void delByUserTemplateId(String templateId)throws Exception{
		List<UserTemplateDetail> list = utdRepo.findByUserTemplateId(templateId);
		utdRepo.delete(list);
	}
	
	public RequestResult save(UserTemplateDetailVo vo)throws Exception{
		Assert.notNull(vo,"查询条件模板详情不能为空");
		
		UserTemplateDetail userTemplateDetail = null;
		
		if(StrUtil.isEmpty(vo.getFid())){
			userTemplateDetail = new UserTemplateDetail();			
		}else{
			userTemplateDetail = utdRepo.findOne(vo.getFid());
		}
		
		userTemplateDetail.setReport(new SysReport(vo.getReportId()));
		userTemplateDetail.setTemplate(new UserTemplate(vo.getTemplateId()));
		userTemplateDetail.setValue(vo.getValue());
		userTemplateDetail.setTableName(vo.getTableName());
		userTemplateDetail.setMark(vo.getMark());
		userTemplateDetail.setFieldName(vo.getFieldName());
		userTemplateDetail.setAliasName(vo.getAliasName());
		userTemplateDetail.setDisplayName(vo.getDisplayName());
		
		if(StrUtil.notEmpty(vo.getOrder())){
			userTemplateDetail.setOrder(Integer.parseInt(vo.getOrder()));
		}
		
		if(StrUtil.notEmpty(vo.getInputType())){
			userTemplateDetail.setInputType(Integer.parseInt(vo.getInputType()));
		}else{
			userTemplateDetail.setInputType(0);
		}
		
		if(StrUtil.notEmpty(vo.getCompare())){
			userTemplateDetail.setCompare(Integer.parseInt(vo.getCompare()));
		}
		
		utdRepo.save(userTemplateDetail);
		
		return buildSuccessRequestResult();
	}
	
	@Override
	public UserTemplateDetailVo getVo(UserTemplateDetail entity){
		if(entity==null)return null;
		UserTemplateDetailVo vo = new UserTemplateDetailVo();
		vo.setCompare(entity.getCompare()+"".intern());
		vo.setDisplayName(entity.getDisplayName());
		vo.setFid(entity.getFid());
		vo.setFieldName(entity.getFieldName());
		vo.setAliasName(entity.getAliasName());
		vo.setInputType(entity.getInputType()+"".intern());
		vo.setMark(entity.getMark());
		vo.setOrder(entity.getOrder()+"".intern());
		vo.setReportId(entity.getReport().getFid());
		vo.setReportName(entity.getReport().getReportName());
		vo.setTableName(entity.getTableName());
		vo.setTemplateId(entity.getTemplate().getFid());
		vo.setTemplateName(entity.getTemplate().getTemplatename());
		vo.setValue(entity.getValue());
		return vo;
	}

	@Override
	public CrudRepository<UserTemplateDetail, String> getRepository() {
		return utdRepo;
	}
}
