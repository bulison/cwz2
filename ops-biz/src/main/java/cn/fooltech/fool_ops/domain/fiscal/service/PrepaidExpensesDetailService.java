package cn.fooltech.fool_ops.domain.fiscal.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.fiscal.entity.PrepaidExpenses;
import cn.fooltech.fool_ops.domain.fiscal.entity.PrepaidExpensesDetail;
import cn.fooltech.fool_ops.domain.fiscal.repository.PrepaidExpensesDetailRepository;
import cn.fooltech.fool_ops.domain.fiscal.vo.PrepaidExpensesDetailVo;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.voucher.entity.Voucher;
import cn.fooltech.fool_ops.domain.voucher.service.VoucherBillService;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;


/**
 * <p>待摊费用明细网页服务类</p>
 * @author xjh
 * @version 1.0
 * @date 2016-04-14 09:02:47
 */
@Service
public class PrepaidExpensesDetailService extends BaseService<PrepaidExpensesDetail,PrepaidExpensesDetailVo,String> {
	
	/**
	 * 待摊费用明细持久类
	 */
	@Autowired
	private PrepaidExpensesDetailRepository expensesDetailRepository;
	
	/**
	 * 凭证、单据关联服务类
	 */
	@Autowired
	private VoucherBillService voucherBillService;
	
	/**
	 * 查询明细
	 * @param vo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageJson query(PrepaidExpensesDetailVo vo, PageParamater paramater){
		Sort sort = new Sort(Direction.ASC, "date");
		PageRequest request = getPageRequest(paramater,sort);
		Page<PrepaidExpensesDetail> query = expensesDetailRepository.query(vo,request);

		List<PrepaidExpensesDetail> entities = query.getContent();
		List<PrepaidExpensesDetailVo> vos = getVos(entities);
		Long count = expensesDetailRepository.queryByCount(vo);

		PrepaidExpensesDetailVo sum = new PrepaidExpensesDetailVo();
		sum.setExpensesCode("");
		sum.setExpensesName("合计");
		sum.setDate("");
		if(count==null){
			sum.setAmount(BigDecimal.ZERO);
		}else{
			sum.setAmount(new BigDecimal(count));
		}
		
		//凭证信息
		loadVoucherMsg(vos);
		
		vos.add(sum);
		return new PageJson(query.getTotalElements(), vos);
	}
	
	/**
	 * 获得查询接口
	 * @param vo
	 * @return
	 */
//	private Criteria getCriteria(PrepaidExpensesDetailVo vo){
//		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PrepaidExpensesDetail.class);
//		Criteria criteria = detachedCriteria.getExecutableCriteria(((CommonDAO)expensesDetailRepository.getDefaultDAO()).getSession());
//		criteria.createAlias("expenses", "expenses", JoinType.INNER_JOIN);
//		criteria.add(Restrictions.eq("org.fid", getCurrentOrgFid()));
//		criteria.add(Restrictions.eq("fiscalAccount.fid", getFiscalAccountId()));
//		//费用编号
//		if(StringUtils.isNotBlank(vo.getExpensesCode())){
//			criteria.add(Restrictions.like("expenses.expensesCode", vo.getExpensesCode(), MatchMode.ANYWHERE));
//		}
//		//费用编号
//		if(StringUtils.isNotBlank(vo.getExpensesName())){
//			criteria.add(Restrictions.like("expenses.expensesName", vo.getExpensesName(), MatchMode.ANYWHERE));
//		}
//		//单据
//		if(vo.getStartDate() != null){
//			criteria.add(Restrictions.ge("date", vo.getStartDate()));
//		}
//		if(vo.getEndDate() != null){
//			criteria.add(Restrictions.le("date", vo.getEndDate()));
//		}
//		return criteria;
//	}
//	
	/**
	 * 根据expensesId获取明细
	 * @return
	 */
	public List<PrepaidExpensesDetailVo> queryByExpensesId(String expensesId){
		Sort sort = new Sort(Direction.DESC, "createTime");
		List<PrepaidExpensesDetail> list = expensesDetailRepository.queryByExpensesId(expensesId,sort);
		return getVos(list);
	}
	
	/**
	 * 单个待摊费用明细实体转换为vo
	 * @param entity
	 * @return
	 */
	@Override
	public PrepaidExpensesDetailVo getVo(PrepaidExpensesDetail entity){
		if(entity == null)
			return null;
		PrepaidExpensesDetailVo vo = new PrepaidExpensesDetailVo();
		vo.setDate(DateUtilTools.date2String(entity.getDate(), DATE));
		vo.setAmount(entity.getAmount());
		vo.setRemark(entity.getRemark());
		vo.setCreateTime(DateUtilTools.date2String(entity.getCreateTime(), DATE_TIME));
		vo.setUpdateTime(DateUtilTools.date2String(entity.getUpdateTime(), DATE_TIME));
		vo.setFid(entity.getFid());
		
		PrepaidExpenses expenses = entity.getExpenses();
		vo.setExpensesId(expenses.getFid());
		vo.setExpensesCode(expenses.getExpensesCode());
		vo.setExpensesName(expenses.getExpensesName());
		
		User user = entity.getCreator();
		vo.setCreatorId(user.getFid());
		vo.setCreatorName(user.getUserName());
		
		return vo;
	}
	
	/**
	 * 获取待摊费用明细信息
	 * @param fid 待摊费用明细ID
	 * @return
	 */
	public PrepaidExpensesDetailVo getByFid(String fid) {
		return getVo(expensesDetailRepository.findOne(fid));
	}
	
	/**
	 * 根据待摊费用计提表保存明细
	 * @param expenses
	 */
	public void saveByExpenses(PrepaidExpenses expenses, Date firstDate){
		
		User creator = SecurityUtil.getCurrentUser();
		FiscalAccount acount = SecurityUtil.getFiscalAccount();
		Organization org = SecurityUtil.getCurrentOrg();
		Date nextDate = DateUtilTools.changeDateTime(firstDate, 1, 0, 0, 0, 0);
		Integer discountPeriod = expenses.getDiscountPeriod();
		BigDecimal perMonth = NumberUtil.divide(expenses.getExpensesAmount(), new BigDecimal(discountPeriod), 2);
		
		BigDecimal temp = NumberUtil.multiply(perMonth, new BigDecimal(discountPeriod-1));
		BigDecimal lastMonth = NumberUtil.subtract(expenses.getExpensesAmount(), temp);
		
		for(int i=0;i<discountPeriod;i++){
			PrepaidExpensesDetail detail = new PrepaidExpensesDetail();
			detail.setCreateTime(Calendar.getInstance().getTime());
			detail.setCreator(creator);
			if(i==0){
				detail.setDate(firstDate);
				nextDate = firstDate;
			}else{
				nextDate = DateUtilTools.changeDateTime(nextDate, 1, 0, 0, 0, 0);
				detail.setDate(nextDate);
			}
			detail.setExpenses(expenses);
			detail.setFiscalAccount(acount);
			detail.setOrg(org);
			detail.setUpdateTime(Calendar.getInstance().getTime());
			
			if(i==expenses.getDiscountPeriod()-1){
				detail.setAmount(lastMonth);
			}else{
				detail.setAmount(perMonth);
			}
			expensesDetailRepository.save(detail);
		}
	}
	
	/**
	 * 加载关联的凭证信息
	 * @param detail
	 */
	public void loadVoucherMsg(List<PrepaidExpensesDetailVo> details){
		if(CollectionUtils.isNotEmpty(details)){
			for(PrepaidExpensesDetailVo detail : details){
				Voucher voucher = voucherBillService.getVoucher(detail.getFid(), WarehouseBuilderCodeHelper.dtfy, SecurityUtil.getFiscalAccountId());
				if(voucher != null){
					detail.setVoucherId(voucher.getFid());
					detail.setVoucherWordNumber(voucher.getVoucherWordNumber());
				}
			}
		}
	}

	@Override
	public CrudRepository<PrepaidExpensesDetail, String> getRepository() {
		return expensesDetailRepository;
	}
	/**
	 * 根据待摊费用计提表ID删除明细
	 * @param expenseId
	 */
	public void deleteByExpensesId(String expensesId) {
		List<PrepaidExpensesDetail> details = expensesDetailRepository.findByExpensesId(expensesId);
		for(PrepaidExpensesDetail detail:details){
			delete(detail.getFid());
		}
	}
}
