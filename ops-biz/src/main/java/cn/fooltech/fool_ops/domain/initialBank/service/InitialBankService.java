package cn.fooltech.fool_ops.domain.initialBank.service;

import java.util.Calendar;

import cn.fooltech.fool_ops.validator.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.base.Strings;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.Bank;
import cn.fooltech.fool_ops.domain.basedata.service.BankService;
import cn.fooltech.fool_ops.domain.initialBank.entity.InitialBank;
import cn.fooltech.fool_ops.domain.initialBank.repository.InitialBankRepository;
import cn.fooltech.fool_ops.domain.initialBank.vo.InitialBankVo;
import cn.fooltech.fool_ops.domain.member.vo.MemberVo;
import cn.fooltech.fool_ops.domain.period.entity.StockPeriod;
import cn.fooltech.fool_ops.domain.period.service.StockPeriodService;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.ErrorCode;
import cn.fooltech.fool_ops.utils.SecurityUtil;

/**
 * <p>现金银行期初网页服务类</p>
 * @author xjh
 * @version 1.0
 * @date 2015年9月8日
 */
@Service
public class InitialBankService extends BaseService<InitialBank,InitialBankVo,String>{

	/**
	 * 现金银行期初持久层
	 */
	@Autowired
	private InitialBankRepository initialBankRepository;
	
	/**
	 * 会计期间服务类
	 */
	@Autowired
	private StockPeriodService periodService;
	
	/**
	 * 银行服务类
	 */
	@Autowired
	private BankService bankService;
	
	
	/**
	 * 查询现金银行期初列表信息<br>
	 * 默认为第一页，每页大小默认为10<br>
	 * @param vo
	 */
	public Page<InitialBankVo> query(InitialBankVo initialBankVo, PageParamater pageParamater) {
		String orgId = SecurityUtil.getCurrentOrgId();
		String faccId = SecurityUtil.getFiscalAccountId();
		Sort sort = new Sort(Direction.DESC, "createTime");
		PageRequest request = getPageRequest(pageParamater, sort);
		Page<InitialBank> page = initialBankRepository.query(initialBankVo, request, orgId, faccId);
		
		if (page.getContent().size() > 0) {
			long count = periodService.countCheckedPeriod(orgId, faccId);
			for (InitialBank iter : page.getContent()) {
				iter.setCheckoutStatus(count > 0 ? StockPeriod.CHECKED : StockPeriod.USED);
			}
		}
		Page<InitialBankVo> pageVos = getPageVos(page, request);
		return pageVos;
	}
	
	
	/**
	 * 单个现金银行期初实体转换为vo
	 * @param entity
	 * @return
	 */
	@Override
	public InitialBankVo getVo(InitialBank entity){
		if(entity == null)
			return null;
		InitialBankVo vo = new InitialBankVo();
		vo.setAmount(entity.getAmount());
		vo.setDescribe(entity.getDescribe());
		vo.setCreateTime(DateUtilTools.date2String(entity.getCreateTime(),DATE_TIME));
		vo.setUpdateTime(DateUtilTools.date2String(entity.getUpdateTime(), DATE_TIME));
		vo.setFid(entity.getFid());
		vo.setPeriodId(entity.getPeriod().getFid());
		vo.setPeriodName(entity.getPeriod().getPeriod());
		vo.setBankAccount(entity.getBank().getAccount());
		vo.setBankCode(entity.getBank().getCode());
		vo.setBankId(entity.getBank().getFid());
		vo.setBankName(entity.getBank().getName());
		vo.setCheckoutStatus(entity.getCheckoutStatus());
		return vo;
	}
	
	/**
	 * 删除现金银行期初<br>
	 */
	public RequestResult delete(String fid){
		InitialBank initialBank = initialBankRepository.findOne(fid);
		
		if(initialBank.getPeriod().getCheckoutStatus() != StockPeriod.USED){
			return new RequestResult(RequestResult.RETURN_FAILURE, "会计期间已启用才能删除");
		}else{
			return super.delete(fid);
		}
	}
	
	/**
	 * 获取现金银行期初信息
	 * @param fid 现金银行期初ID
	 * @return
	 */
	public InitialBankVo getByFid(String fid) {
		Assert.notNull(fid);
		return getVo(super.findOne(fid));
	}
	

	/**
	 * 新增/编辑现金银行期初
	 * @param vo
	 */
	public RequestResult save(InitialBankVo vo) {

		if(Strings.isNullOrEmpty(vo.getBankId())){
			return new RequestResult(RequestResult.RETURN_FAILURE, "银行必填");
		}
		
		if(vo.getAmount()==null){
			return new RequestResult(RequestResult.RETURN_FAILURE, "结余金额必填");
		}
		
		if(initialBankRepository.existInitialBank(SecurityUtil.getFiscalAccountId(), vo.getBankId(), vo.getFid())){
			return new RequestResult(RequestResult.RETURN_FAILURE, "当前银行期初账已存在");
		}
		
		StockPeriod period = periodService.getTheFristPeriod(SecurityUtil.getCurrentOrgId(), SecurityUtil.getFiscalAccountId());
		if(period == null){
			return new RequestResult(RequestResult.RETURN_FAILURE, ErrorCode.STOCK_PERIOD_NOT_EXIST, "找不到第一个会计期间");
		}
		if(period.getCheckoutStatus() == StockPeriod.UN_USED){
			return new RequestResult(RequestResult.RETURN_FAILURE, ErrorCode.STOCK_PERIOD_UN_USED, "第一个会计期间未启用");
		}
		if(period.getCheckoutStatus() == StockPeriod.CHECKED){
			return new RequestResult(RequestResult.RETURN_FAILURE, ErrorCode.STOCK_PERIOD_CHECKED, "第一个会计期间已结账");
		}
		
		Bank bank = bankService.get(vo.getBankId());
		if(bank==null){
			return new RequestResult(RequestResult.RETURN_FAILURE, ErrorCode.STOCK_PERIOD_CHECKED, "银行信息不存在或已被删除");
		}
		
		InitialBank entity = new InitialBank();
		if(StringUtils.isBlank(vo.getFid())){
			
			entity.setAmount(vo.getAmount());
			entity.setDescribe(vo.getDescribe());
			entity.setCreateTime(Calendar.getInstance().getTime());
			
			entity.setPeriod(period);
			entity.setBank(bank);
			entity.setCreator(SecurityUtil.getCurrentUser());
			entity.setOrg(SecurityUtil.getCurrentOrg());
			entity.setDept(SecurityUtil.getCurrentDept());
			entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
			
			initialBankRepository.save(entity);
		}else {
			entity = initialBankRepository.findOne(vo.getFid());
			
			if(entity == null){
				return new RequestResult(RequestResult.RETURN_FAILURE, "该记录不存在或已被删除!");
			}
			
			String updateTime = DateUtilTools.date2String(entity.getUpdateTime(), DATE_TIME);
			if(!vo.getUpdateTime().equals(updateTime)){
				return new RequestResult(RequestResult.RETURN_FAILURE, "数据已被其他用户修改");
			}
			
			entity.setAmount(vo.getAmount());
			entity.setDescribe(vo.getDescribe());
			
			entity.setPeriod(period);
			entity.setBank(bank);
			
			initialBankRepository.save(entity);
		}
		
		return new RequestResult(RequestResult.RETURN_SUCCESS,"保存成功", getVo(entity));
	}
	/**
	 * 根据银行ID统计
	 * @param bankId
	 * @return
	 */
	public Long countByBankId(String bankId){
		return initialBankRepository.countByBankId(bankId);
	}

	@Override
	public CrudRepository<InitialBank, String> getRepository() {
		return initialBankRepository;
	}
	

}
