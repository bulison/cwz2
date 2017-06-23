package cn.fooltech.fool_ops.domain.rate.service;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.rate.entity.LoanRate;
import cn.fooltech.fool_ops.domain.rate.entity.LoanRateLog;
import cn.fooltech.fool_ops.domain.rate.repository.LoanRateLogRepository;
import cn.fooltech.fool_ops.domain.rate.vo.LoanRateLogVo;
import cn.fooltech.fool_ops.domain.rate.vo.LoanRateVo;
import cn.fooltech.fool_ops.utils.*;
import cn.fooltech.fool_ops.validator.ValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;


/**
 * <p>央行贷款利率网页服务类</p>
 * @author rqh
 * @version 1.0
 * @date 2016年6月14日
 */
@Service("ops.LoanRateLogService")
public class LoanRateLogService extends BaseService<LoanRateLog,LoanRateLogVo,String> {
	
	private final static double DEFAULT_BANK_RATE = 0;
	
	@Autowired
	private LoanRateLogRepository LoanRateLogRepository;
	
	/**
	 * 分页查询
	 * @return
	 */
	public Page<LoanRateLogVo> query(LoanRateLogVo vo, PageParamater paramater){
		String orgId = SecurityUtil.getCurrentOrgId();
//		Date date = DateUtilTools.string2Date(vo.getDate());
		Sort sort = new Sort(Direction.DESC, "createTime");
		PageRequest pageRequest = getPageRequest(paramater, sort);
		Page<LoanRateLog> page = LoanRateLogRepository.findPageBy(orgId, pageRequest);
		for(LoanRateLog log:page)
			log.setRate(log.getRate().multiply(BigDecimal.valueOf(100)));//取出的数据乘以100
		return getPageVos(page, pageRequest);
	}
	
	/**
	 * 新增、编辑
	 * @param vo
	 * @return
	 */
	@Transactional
	public RequestResult save(LoanRateLogVo vo){
		String inValid = ValidatorUtils.inValidMsg(vo);
		if(inValid!=null){
			return buildFailRequestResult(inValid);
		}
		String fid = vo.getFid();
		String rate = vo.getRate();
		String increase = vo.getIncrease();
		
		String orgId = SecurityUtil.getCurrentOrgId();
		
		LoanRateLog entity = null;
		if(StringUtils.isBlank(fid)){
			entity = new LoanRateLog();
			entity.setOrg(SecurityUtil.getCurrentOrg());
			entity.setCreator(SecurityUtil.getCurrentUser());
			entity.setCreateTime(new Date());
		}
		else{
			entity = LoanRateLogRepository.findOne(fid);
			if(entity == null){
				return new RequestResult(RequestResult.RETURN_FAILURE, "记录不存在或已被删除!");
			}
		}
		if(NumberUtil.toBigDeciaml(rate).doubleValue()>100){
			return new RequestResult(RequestResult.RETURN_FAILURE, "利率不能超过100%！");
		}
		entity.setRate(NumberUtil.toBigDeciaml(rate).divide(BigDecimal.valueOf(100)));//取出的数据除以100
		entity.setIncrease(NumberUtil.toBigDeciaml(increase));
		entity.setCreator(SecurityUtil.getCurrentUser());
		entity.setUpdateTime(new Date());
		LoanRateLogRepository.save(entity);
		return buildSuccessRequestResult(entity.getFid());
	}
	
	/**
	 * 单个实体转vo
	 */
	@Override
	public LoanRateLogVo getVo(LoanRateLog entity) {
		LoanRateLogVo vo = new LoanRateLogVo();
		vo.setFid(entity.getFid());
		vo.setRate(NumberUtil.bigDecimalToStr(entity.getRate(), 4));
		vo.setIncrease(NumberUtil.bigDecimalToStr(entity.getIncrease(), 4));
		vo.setCreateTime(DateUtils.getDateString(entity.getCreateTime()));
		if (entity.getCreator()!=null){
			vo.setCreatorId(entity.getCreator().getFid());
			vo.setCreatorName(entity.getCreator().getUserName());
		}
		vo.setUpdateTime(DateUtils.getDateString(entity.getUpdateTime()));
		return vo;
	}

	@Override
	public CrudRepository<LoanRateLog, String> getRepository() {
		return LoanRateLogRepository;
	}
}
