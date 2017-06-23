package cn.fooltech.fool_ops.domain.flow.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.flow.entity.Plan;
import cn.fooltech.fool_ops.domain.flow.entity.YieldRate;
import cn.fooltech.fool_ops.domain.flow.repository.YieldRateRepository;
import cn.fooltech.fool_ops.domain.flow.vo.YieldRateVo;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.VoFactory;

/**
 * 
 * <p>
 * 流程计划每天收益率 服务层
 * </p>
 * 
 * @author cwz
 * @date 2017年4月12日
 */
@Service
public class YieldRateService extends BaseService<YieldRate, YieldRateVo, String> {

	@Autowired
	private YieldRateRepository repository;

	@Autowired
	private PlanService planService;

	/**
	 * 实体转换VO
	 * 
	 * @param entity
	 * @return
	 */
	@Override
	public YieldRateVo getVo(YieldRate entity) {
		YieldRateVo vo = VoFactory.createValue(YieldRateVo.class, entity);
		FiscalAccount fiscalAccount = entity.getFiscalAccount();
		if (fiscalAccount != null) {
			vo.setAccId(fiscalAccount.getFid());
			vo.setAccName(fiscalAccount.getName());
		}
		Organization organization = entity.getOrganization();
		if (organization != null) {
			vo.setOrgId(organization.getFid());
			vo.setOrgName(organization.getOrgName());
		}
		Plan plan = entity.getPlan();
		if (plan != null) {
			vo.setPlanId(plan.getFid());
			vo.setPlanName(plan.getName());
		}

		return vo;
	}

	@Override
	public CrudRepository<YieldRate, String> getRepository() {
		return repository;
	}

	/**
	 * 查找分页
	 * 
	 * @param vo
	 * @param paramater
	 * @return
	 */
	public Page<YieldRateVo> query(YieldRateVo vo, PageParamater paramater) {

		String accId = SecurityUtil.getFiscalAccountId();
		Sort sort = new Sort(Sort.Direction.DESC, "date");
		PageRequest pageRequest = getPageRequest(paramater, sort);
		Page<YieldRate> page = repository.findPageBy(accId, vo, pageRequest);
		return getPageVos(page, pageRequest);
	}

	/**
	 * 修改或新增
	 * 
	 * @param vo
	 * @return
	 */
	@Transactional
	public RequestResult save(YieldRateVo vo) {

		YieldRate entity = null;
		if (Strings.isNullOrEmpty(vo.getId())) {
			entity = new YieldRate();
			entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
			entity.setUpdateTime(new Date());
			entity.setOrganization(SecurityUtil.getCurrentOrg());
		} else {
			entity = repository.findOne(vo.getId());

			if (entity.getUpdateTime().compareTo(vo.getUpdateTime()) != 0) {
				return buildFailRequestResult("数据已被其他用户修改，请刷新再试");
			}
		}
		entity.setPlan(planService.findOne(vo.getPlanId()));
		entity.setDate(vo.getDate());
		entity.setEffectiveYieldrate(vo.getEffectiveYieldrate());
		entity.setCurrentYieldRate(vo.getCurrentYieldRate());
		entity.setReferenceYieldrate(vo.getReferenceYieldrate());
		entity.setUpdateTime(vo.getUpdateTime());
		repository.save(entity);
		return buildSuccessRequestResult(getVo(entity));
	}
	/**
	 * 根据流程ID和时间查询日收益率
	 * @param planId
	 * @param currDate
	 * @return
	 */
	public YieldRate queryByPlanAndDate(String planId,Date currentDate){
		return repository.queryByPlanAndDate(planId, currentDate);
	}
	public List<YieldRateVo> queryByPlan(String planId){ 
		List<YieldRate> list = repository.queryByPlan(planId);
		List<YieldRateVo> vos = getVos(list);
		List<YieldRateVo> vos2 = Lists.newArrayList();
		Plan plan = planService.findOne(planId);
		BigDecimal estimatedYieldrate = plan.getEstimatedYieldrate()==null?BigDecimal.ZERO:plan.getEstimatedYieldrate();
		for (YieldRateVo yieldRateVo : vos) {
			yieldRateVo.setEstimatedYieldrate(estimatedYieldrate);
			vos2.add(yieldRateVo);
		}
		return vos2;
	}
}
