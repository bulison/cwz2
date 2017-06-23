package cn.fooltech.fool_ops.domain.rate.service;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.rate.entity.RateProgramme;
import cn.fooltech.fool_ops.domain.rate.entity.RateProgrammeRecord;
import cn.fooltech.fool_ops.domain.rate.repository.RateProgrammeRepository;
import cn.fooltech.fool_ops.domain.rate.vo.RateProgrammeRecordVo;
import cn.fooltech.fool_ops.domain.rate.vo.RateProgrammeVo;
import cn.fooltech.fool_ops.domain.rate.vo.RateResult;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.rate.tools.ArithUtil;
import cn.fooltech.fool_ops.rate.tools.Calculate;
import cn.fooltech.fool_ops.rate.tools.EveryTradeBean;
import cn.fooltech.fool_ops.rate.tools.RateBean;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.validator.ValidatorUtils;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>收益率方案网页服务类</p>
 * @author xjh
 * @version 1.0
 * @date 2016-06-14 08:57:07
 */
@Service("ops.RateProgrammeService")
public class RateProgrammeService extends BaseService<RateProgramme, RateProgrammeVo, String> {
	
	/**
	 * 收益率方案服务类
	 */
	@Autowired
	private RateProgrammeRepository rateProgrammeRepo;
	
	/**
	 * 收益率方案明细服务类
	 */
	@Autowired
	private RateProgrammeRecordService detailService;
	@Autowired
	private LoanRateService loanRateService;
	
	/**
	 * 查询收益率方案列表信息，按照收益率方案主键降序排列<br>
	 * 默认为第一页，每页大小默认为10<br>
	 */
	public Page<RateProgrammeVo> query(RateProgrammeVo rateProgrammeVo,PageParamater pageParamater){
		
		Sort sort = new Sort(Direction.DESC, "createTime");
		PageRequest pageRequest = getPageRequest(pageParamater, sort);
		String searchKey = rateProgrammeVo.getSearchKey();
		String orgId = SecurityUtil.getCurrentOrgId();
		String accId = SecurityUtil.getFiscalAccountId();
		return getPageVos(rateProgrammeRepo.findPageBy(orgId, accId,searchKey, pageRequest), pageRequest);
	}
	
	@Override
	public RateProgrammeVo getVo(RateProgramme entity){
		return getVo(entity, false);
	}
	
	/**
	 * 单个收益率方案实体转换为vo
	 * @param entity
	 * @return
	 */
	public RateProgrammeVo getVo(RateProgramme entity, boolean transferDetail){
		if(entity == null)
			return null;
		RateProgrammeVo vo = new RateProgrammeVo();
		vo.setName(entity.getName());
		vo.setCreateTime(entity.getCreateTime());
		vo.setFid(entity.getFid());
		if(entity.getCycle()!=null){
			vo.setCycle(new BigDecimal(entity.getCycle()));
		}
		if(entity.getProfit()!=null){
			vo.setProfit(NumberUtil.scale(entity.getProfit(),4));
		}
		if(entity.getProfitRate()!=null){
			vo.setProfitRate(NumberUtil.scale(entity.getProfitRate(),4));
		}
		if(entity.getCycleProfitRate()!=null){
			vo.setCycleProfitRate(NumberUtil.scale(entity.getCycleProfitRate(),4));
		}
		User creator = entity.getCreator();
		vo.setCreatorId(creator.getFid());
		vo.setCreatorName(creator.getUserName());
		if(transferDetail){
			JSONArray array = JSONArray.fromObject(detailService.getVos(entity.getRecords()));
			vo.setDetails(array.toString());
		}
		
		return vo;
	}
	
	/**
	 * 删除收益率方案<br>
	 */
	public RequestResult delete(String fid){
		rateProgrammeRepo.delete(fid);
		detailService.deleteByRateProgrammeId(fid);
		return buildSuccessRequestResult();
	}
	/**
	 * 删除收益率方案明细<br>
	 */
	public RequestResult deleteDetail(String fid){
		detailService.delete(fid);
		return buildSuccessRequestResult();
	}
	/**
	 * 新增/编辑收益率方案
	 * @param vo
	 */
	@Transactional
	public RequestResult save(RateProgrammeVo vo) {
		String inValid = ValidatorUtils.inValidMsg(vo);
		if(inValid!=null){
			return buildFailRequestResult(inValid);
		}
		
		RateProgramme entity = new RateProgramme();
		if(StringUtils.isBlank(vo.getFid())){
			entity.setCreateTime(DateUtilTools.now());
			entity.setCreator(SecurityUtil.getCurrentUser());
			entity.setOrg(SecurityUtil.getCurrentOrg());
			entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
			if(vo.getDetailList()==null||vo.getDetailList().size()==0){
				return buildFailRequestResult("新增收益率方案时请先保存收益率方案再保存收益率明细");
			}
		}else {
			entity = rateProgrammeRepo.findOne(vo.getFid());
			detailService.deleteByRateProgrammeId(vo.getFid());
		}
		
		entity.setName(vo.getName());
		if(vo.getProfit()!=null){
			entity.setProfit(NumberUtil.scale(vo.getProfit(),4));
		}
		if(vo.getProfitRate()!=null){
			entity.setProfitRate(NumberUtil.scale(vo.getProfitRate(),4));
		}
		if(vo.getCycleProfitRate()!=null){
			entity.setCycleProfitRate(NumberUtil.scale(vo.getCycleProfitRate(),4));
		}else{
			entity.setCycleProfitRate(BigDecimal.ZERO);
		}
/*	周期用常量锁定	
 * if(vo.getCycle()!=null){
			entity.setCycle(vo.getCycle().intValue());
		}*/
		
		
		rateProgrammeRepo.save(entity);
		detailService.save(vo.getDetailList(), entity);
		entity=rateProgrammeRepo.findOne(entity.getFid());
		return buildSuccessRequestResult(getVo(entity));
	}
	/**
	 *  新增/编辑收益率方案明细
	 */
	public RequestResult saveDetail(RateProgrammeRecordVo vo){
		RateProgramme entity = new RateProgramme();
		if(!StringUtils.isBlank(vo.getRateProgrammeId())){
			entity = rateProgrammeRepo.findOne(vo.getRateProgrammeId());
		}else{
			return buildFailRequestResult("收益率方案必传");
		}
		return detailService.saveDetail(vo, entity);
	}
	/**
	 * 计算收益率
	 * @return
	 * @throws Exception 
	 */
	public RateResult calRate(String rateProId)  {
		RateProgramme pro = rateProgrammeRepo.findOne(rateProId);
		List<RateProgrammeRecord> records = pro.getRecords();

		double rate=loanRateService.getBankRate(SecurityUtil.getCurrentOrgId());

		RateBean real = new RateBean();
		real.setDayFundLostRate(rate);
		//利润计算
		BigDecimal profit=BigDecimal.ZERO;
		BigDecimal expend=BigDecimal.ZERO;
		for(RateProgrammeRecord record:records){
			EveryTradeBean trade = new EveryTradeBean();
			
			trade.setMoney(record.getAmount().doubleValue());
			trade.setTime(record.getTradeDate());
			
			if(record.getType()==RateProgrammeRecord.TYPE_INCOME){
				real.getIncomeList().add(trade);
				profit=profit.add(record.getAmount());
				
			}else{
				real.getExpendList().add(trade);
				profit=NumberUtil.subtract(profit, record.getAmount());
				expend=NumberUtil.add(expend,record.getAmount());
			}
		}

		Calculate.initRateBean(real);
		try {
			Calculate.outRateBean(real);
		} catch (Exception e) {
			return null;
		}
		pro.setCycleProfitRate(new BigDecimal(real.getCycleRate()/100));
		pro.setProfitRate(new BigDecimal(real.getRate()/100));
		pro.setProfit(profit);
		RateResult result = new RateResult();
		result.setRealRate(ArithUtil.round(real.getRate(),2));
		result.setCycleRate(ArithUtil.round(real.getCycleRate(),2));
		rateProgrammeRepo.save(pro);
		result.setProfit(pro.getProfit());
		result.setProfitRate(pro.getProfitRate());
		return result;
	}

	@Override
	public CrudRepository<RateProgramme, String> getRepository() {
		return rateProgrammeRepo;
	}
}
