package cn.fooltech.fool_ops.domain.capital.service;

import java.util.Calendar;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.capital.entity.CapitalPlanChangeLog;
import cn.fooltech.fool_ops.domain.capital.repository.CapitalPlanChangeLogRepository;
import cn.fooltech.fool_ops.domain.capital.repository.CapitalPlanDetailRepository;
import cn.fooltech.fool_ops.domain.capital.vo.CapitalPlanChangeLogVo;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.VoFactory;
import cn.fooltech.fool_ops.validator.ValidatorUtils;
/**
 * <p>资金计划日志服务类</p>
 * @author hjr
 * @date 2017/2/28
 */
@Service
public class CapitalPlanChangeLogService extends BaseService<CapitalPlanChangeLog,CapitalPlanChangeLogVo,String>{
	@Autowired
	CapitalPlanChangeLogRepository capitalPlanChangeLogRepository;
	@Autowired
	CapitalPlanDetailRepository capitalPlanDetailRepository;
	/**
	 * 查找所有资金计划日志变动
	 */
	public Page<CapitalPlanChangeLogVo> queryAll(PageParamater pageParamater){
		PageRequest request = getPageRequest(pageParamater);
		Page<CapitalPlanChangeLogVo> pageVo=getPageVos(capitalPlanChangeLogRepository.query(request),request);
		return pageVo;
	}

	
	/**
	 * 把VO转换成实体
	 */
	/**
	 * 新增/编辑
	 */
	@Transactional
	public RequestResult save(CapitalPlanChangeLogVo vo){
		String inValid = ValidatorUtils.inValidMsg(vo);
		if(inValid!=null){
			return buildFailRequestResult(inValid);
		}
		CapitalPlanChangeLog entity =new CapitalPlanChangeLog();
		if(StringUtils.isBlank(vo.getId())){
			entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
			entity.setCreate(SecurityUtil.getCurrentUser());
			entity.setOrg(SecurityUtil.getCurrentOrg());
			entity.setCreateTime(Calendar.getInstance().getTime());
		}else{
			entity=capitalPlanChangeLogRepository.findOne(vo.getId());
			if(entity == null){
				return new RequestResult(RequestResult.RETURN_FAILURE, "该记录不存在或已被删除!");
			}
		}
		entity.setChangeType(vo.getChangeType());
		
		entity.setDetail(capitalPlanDetailRepository.querybyfid(vo.getDetailId(), SecurityUtil.getCurrentOrg().getOrgId()));
		
		entity.setId(vo.getId());
		entity.setPaymentDate(DateUtils.getDateFromString(vo.getPaymentDate()));
		entity.setPaymentAmount(vo.getPaymentAmount());
		entity.setPrePaymentAmount(vo.getPrePaymentAmount());
		entity.setPrePaymentDate(DateUtils.getDateFromString(vo.getPrePaymentDate()));
		entity.setRemark(vo.getRemark());
	//	entity.setUpdateTime(Calendar.getInstance().getTime());
		capitalPlanChangeLogRepository.save(entity);
		return  buildSuccessRequestResult(getVo(entity));
		 
		
	}

	@Override
	public CrudRepository getRepository() {
		// TODO Auto-generated method stub
		return capitalPlanChangeLogRepository;
	}

	/**
	 * 把实体转换成Vo
	 */
	@Override
	public CapitalPlanChangeLogVo getVo(CapitalPlanChangeLog entity) {
		if(entity==null){
			return null;
		}
		CapitalPlanChangeLogVo vo=VoFactory.createValue(CapitalPlanChangeLogVo.class,entity);
		
		return vo;
	}
	/**
	 * 根据ID删除相关计划
	 */
	@Transactional
	public RequestResult delete(String id){
		CapitalPlanChangeLog entity=capitalPlanChangeLogRepository.findOne(id);
		if(entity==null){
			return buildFailRequestResult("该记录不存在");
		}
		capitalPlanChangeLogRepository.delete(entity);
		return buildSuccessRequestResult();
	}
	/**
	 *  根据明细id查询日志
	 * @param detailId 明细id
	 * @return
	 */
	public List<CapitalPlanChangeLog> queryByDetailId(String detailId){
		return capitalPlanChangeLogRepository.queryByDetailId(detailId);
	}
}
