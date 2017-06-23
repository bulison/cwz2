package cn.fooltech.fool_ops.domain.rate.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import cn.fooltech.fool_ops.domain.rate.vo.LoanRateLogVo;
import cn.fooltech.fool_ops.utils.*;
import cn.fooltech.fool_ops.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.rate.entity.LoanRate;
import cn.fooltech.fool_ops.domain.rate.repository.LoanRateRepository;
import cn.fooltech.fool_ops.domain.rate.vo.LoanRateVo;
import cn.fooltech.fool_ops.validator.ValidatorUtils;


/**
 * <p>央行贷款利率网页服务类</p>
 * @author rqh
 * @version 1.0
 * @date 2016年6月14日
 */
@Service("ops.LoanRateService")
public class LoanRateService extends BaseService<LoanRate,LoanRateVo,String> {
	
	private final static double DEFAULT_BANK_RATE = 0;
	
	@Autowired
	private LoanRateRepository loanRateRepository;
	
	/**
	 * 分页查询
	 * @return
	 */
	public Page<LoanRateVo> query(LoanRateVo vo, PageParamater paramater){
		String orgId = SecurityUtil.getCurrentOrgId();
		//Date date = DateUtilTools.string2Date(vo.getDate());
		Sort sort = new Sort(Direction.DESC, "createTime");
		PageRequest pageRequest = getPageRequest(paramater, sort);
		Page<LoanRate> page = loanRateRepository.findPageBy(orgId, pageRequest);
		for(LoanRate rate:page)
			rate.setRate(rate.getRate().multiply(BigDecimal.valueOf(100)));//取出的数据乘以100
		return getPageVos(page, pageRequest);
	}
	
	/**
	 * 新增、编辑
	 * @param vo
	 * @return
	 */
	@Transactional
	public RequestResult save(LoanRateVo vo){
		String inValid = ValidatorUtils.inValidMsg(vo);
		if(inValid!=null){
			return buildFailRequestResult(inValid);
		}
		String fid = vo.getFid();
		//String date = vo.getDate();
		String rate = vo.getRate();
		String increase = vo.getIncrease();
		
		String orgId = SecurityUtil.getCurrentOrgId();
		/*if(isExisted(orgId, DateUtils.getDateFromString(date), fid)){
			return new RequestResult(RequestResult.RETURN_FAILURE, "该日期的央行贷款利率已存在!");
		}*/
		LoanRate entity = null;
		if(StringUtils.isBlank(fid)){
			entity = new LoanRate();
			entity.setOrg(SecurityUtil.getCurrentOrg());
			entity.setCreator(SecurityUtil.getCurrentUser());
			entity.setCreateTime(new Date());
		}
		else{
			entity = loanRateRepository.findOne(fid);
			if(entity == null){
				return new RequestResult(RequestResult.RETURN_FAILURE, "记录不存在或已被删除!");
			}
		}
		if(NumberUtil.toBigDeciaml(rate).doubleValue()>100){
			return new RequestResult(RequestResult.RETURN_FAILURE, "利率不能超过100%！");
		}
		//entity.setDate(DateUtils.getDateFromString(date));
		entity.setRate(NumberUtil.toBigDeciaml(rate).divide(BigDecimal.valueOf(100)));//取出的数据除以100
		entity.setIncrease(NumberUtil.toBigDeciaml(increase));
		entity.setCreator(SecurityUtil.getCurrentUser());
		entity.setUpdateTime(new Date());
		loanRateRepository.save(entity);
		
		return buildSuccessRequestResult(entity.getFid());
	}
	
	/**
	 * 根据日期判断记录是否存在
	 * @param orgId 机构ID
	 * @param date 日期
	 * @param excludeId 排除的ID
	 * @return
	 */
	/*public boolean isExisted(String orgId, Date date, String excludeId){
		Long count = 0L;
		if(StringUtils.isNotBlank(excludeId)){
			count = loanRateRepository.countByDate(orgId, date, excludeId);
		}else{
			count = loanRateRepository.countByDate(orgId, date);
		}
		if(count!=null && count>0)
			return true;
		else
			return false;
	}*/
	
	/**
	 * 单个实体转vo
	 */
	@Override
	public LoanRateVo getVo(LoanRate entity) {
		if(entity==null)return null;
		LoanRateVo vo = new LoanRateVo();
		vo.setFid(entity.getFid());
		//vo.setDate(DateUtils.getDateString(entity.getDate()));
		vo.setRate(NumberUtil.bigDecimalToStr(entity.getRate(), 8));
		vo.setIncrease(NumberUtil.bigDecimalToStr(entity.getIncrease(), 8));
		vo.setCreateTime(DateUtils.getDateString(entity.getCreateTime()));
		if (entity.getCreator()!=null){
			vo.setCreatorId(entity.getCreator().getFid());
			vo.setCreatorName(entity.getCreator().getUserName());
		}
		vo.setUpdateTime(DateUtils.getDateString(entity.getUpdateTime()));
		return vo;
	}

	@Override
	public CrudRepository<LoanRate, String> getRepository() {
		return loanRateRepository;
	}
	/**
	 * 获取date日期的央行贷款利率
	 * @param orgId
	 * @return
	 */
	/*public double getBankRate(String orgId, Date date){
		LoanRate top = loanRateRepository.findTopBy(orgId, date);
		if(top!=null)return ((LoanRate)top).getRate().doubleValue();
		return DEFAULT_BANK_RATE;
	}*/
	public double getBankRate(String orgId){
		LoanRate top = loanRateRepository.findTopBy(orgId);
		if(top!=null)return ((LoanRate)top).getRate().doubleValue();
		return DEFAULT_BANK_RATE;
	}
	/**
	 * 查询当前机构的央行贷款利率
	 * @param orgId
	 * @return
	 */
	public LoanRateVo findTopBy(String orgId){
		LoanRate top = loanRateRepository.findTopBy(orgId);
		LoanRateVo vo = getVo(top);
		return vo;
	}

	/**
	 * LoanRateLogVo转换成LoanRateVo，LoanRate对应表永远只有ID为001的唯一一条记录
	 * @param logVo
	 * @return
	 */
	public LoanRateVo getVoByLogVo(LoanRateLogVo logVo){
		LoanRateVo loanRateVo=new LoanRateVo();
//		loanRateVo.setDate(logVo.getDate());
		loanRateVo.setRate(logVo.getRate());
		loanRateVo.setIncrease(logVo.getIncrease());
		loanRateVo.setCreatorId(logVo.getCreatorId());
		loanRateVo.setCreatorName(logVo.getCreatorName());
		loanRateVo.setUpdateTime(logVo.getUpdateTime());
		return  loanRateVo;
	}

	/**
	 * 更新LoanRate，没有几率就添加，有一条记录则update，多条记录则删除所有再添加，保证唯一
	 * @param vo
	 */
	@Transactional
	public RequestResult update(LoanRateVo vo){
		String orgId=SecurityUtil.getCurrentOrgId();
		long count=loanRateRepository.countByOrgFid(orgId);
		if(count==1){
			List<LoanRate> list = loanRateRepository.findByOrgFid(orgId);
			String fid=list.get(0).getFid();
			if(StringUtils.isNotEmpty(fid))
				vo.setFid(fid);
		}else if(count>0){
			loanRateRepository.deleteByOrgFid(orgId);
		}
		return save(vo);
	}
}
