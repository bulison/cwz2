package cn.fooltech.fool_ops.domain.rate.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.rate.entity.RateProgramme;
import cn.fooltech.fool_ops.domain.rate.entity.RateProgrammeRecord;
import cn.fooltech.fool_ops.domain.rate.repository.RateProgrammeRecordRepository;
import cn.fooltech.fool_ops.domain.rate.vo.RateProgrammeRecordVo;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.validator.ValidatorUtils;

/**
 * <p>收益率方案明细网页服务类</p>
 * @author xjh
 * @version 1.0
 * @date 2016-06-14 08:58:29
 */
@Service("ops.RateProgrammeRecordService")
public class RateProgrammeRecordService extends BaseService<RateProgrammeRecord, RateProgrammeRecordVo, String> {
	
	/**
	 * 收益率方案明细服务类
	 */
	@Autowired
	private RateProgrammeRecordRepository repository;
	
	
	/**
	 * 单个收益率方案明细实体转换为vo
	 * @param entity
	 * @return
	 */
	@Override
	public RateProgrammeRecordVo getVo(RateProgrammeRecord entity){
		if(entity == null)
			return null;
		RateProgrammeRecordVo vo = new RateProgrammeRecordVo();
		vo.setTradeDate(DateUtilTools.date2String(entity.getTradeDate()));
		vo.setType(entity.getType());
		vo.setAmount(entity.getAmount());
		vo.setRateProgrammeId(entity.getRateProgramme().getFid());
		vo.setFid(entity.getFid());
		
		return vo;
	}
	
	/**
	 * 根据收益率新增/编辑收益率方案明细
	 * @param vo
	 */
	@Transactional
	public RequestResult save(List<RateProgrammeRecordVo> vos, RateProgramme rateProgramme) {
		
		for(RateProgrammeRecordVo vo:vos){
			String inValid = ValidatorUtils.inValidMsg(vo);
			if(inValid!=null){
				return buildFailRequestResult(inValid);
			}
			RateProgrammeRecord entity = new RateProgrammeRecord();
			entity.setRateProgramme(rateProgramme);
			entity.setTradeDate(DateUtilTools.string2Date(vo.getTradeDate(), DATE));
			entity.setType(vo.getType());
			entity.setAmount(vo.getAmount());
			
			repository.save(entity);
		}
		return buildSuccessRequestResult();
	}
	/**
	 * 新增/编辑收益率方案明细
	 * @param vo
	 */
	@Transactional
	public RequestResult saveDetail(RateProgrammeRecordVo vo, RateProgramme rateProgramme) {
		
			String inValid = ValidatorUtils.inValidMsg(vo);
			if(inValid!=null){
				return buildFailRequestResult(inValid);
			}
			RateProgrammeRecord entity = new RateProgrammeRecord();
			if(!StringUtils.isBlank(vo.getFid())){
				entity=repository.findOne(vo.getFid());
			}
			entity.setRateProgramme(rateProgramme);
			entity.setTradeDate(DateUtilTools.string2Date(vo.getTradeDate(), DATE));
			entity.setType(vo.getType());
			entity.setAmount(vo.getAmount());
			
			repository.save(entity);
		
		return buildSuccessRequestResult(getVo(entity));
	}

	/**
	 * 根据方案ID删除明细ID
	 */
	public void deleteByRateProgrammeId(String rateId) {
		List<RateProgrammeRecord> datas = repository.findByRateId(rateId);
		repository.delete(datas);
	}


	@Override
	public CrudRepository<RateProgrammeRecord, String> getRepository() {
		return repository;
	}
}
