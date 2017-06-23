package cn.fooltech.fool_ops.domain.excelmap.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
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
import cn.fooltech.fool_ops.domain.common.entity.ImportVoBean;
import cn.fooltech.fool_ops.domain.excelmap.entity.ExcelException;
import cn.fooltech.fool_ops.domain.excelmap.repository.ExcelExceptionRepository;
import cn.fooltech.fool_ops.domain.excelmap.vo.ExcelExceptionVo;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.VoFactory;


/**
 * <p>ExcelMap网页服务类</p>
 * @author xjh
 * @version 1.0
 * @date 2015年10月26日
 */
@Service("ops.ExcelExceptionService")
public class ExcelExceptionService extends BaseService<ExcelException, ExcelExceptionVo, String> {

	@Autowired
	private ExcelExceptionRepository exceRepo;

	/**
	 * 查询分页, 默认每页10个元素
	 */
	public Page<ExcelExceptionVo> query(ExcelExceptionVo vo, PageParamater paramater){
		Sort sort = new Sort(Direction.DESC, "createTime");
		PageRequest request = getPageRequest(paramater, sort);
		String code = vo.getCode();
		String orgId = SecurityUtil.getCurrentOrgId();
		if(StringUtils.isNotBlank(code)){
			return getPageVos(exceRepo.findPageByOrgAndCode(code, orgId, request), request);
		}else{
			return getPageVos(exceRepo.findPageByOrg(orgId, request), request);
		}
	}
	
	/**
	 * 新增
	 */
	public void save(List<ImportVoBean> voResults, String code){

		User user = SecurityUtil.getCurrentUser();
		Organization org = SecurityUtil.getCurrentOrg();
		for(ImportVoBean voBean:voResults){
			if(!voBean.getVaild()){
				ExcelException entity = new ExcelException();
				entity.setCode(code);
				entity.setRowNum(voBean.getRow());
				entity.setDescribe(voBean.getMsg());
				entity.setCreateTime(new Date());
				entity.setCreator(user);
				entity.setOrg(org);
				exceRepo.save(entity);
			}
		}
		
	}
	
	/**
	 * 删除
	 */
	public RequestResult delete(String fid){
		exceRepo.delete(fid);
		return buildSuccessRequestResult();
	}
	
	@Override
	public ExcelExceptionVo getVo(ExcelException entity) {
		Assert.notNull(entity);
		ExcelExceptionVo vo = VoFactory.createValue(ExcelExceptionVo.class, entity);
		return vo;
	}


	@Override
	public CrudRepository<ExcelException, String> getRepository() {
		return this.exceRepo;
	}
	
}
