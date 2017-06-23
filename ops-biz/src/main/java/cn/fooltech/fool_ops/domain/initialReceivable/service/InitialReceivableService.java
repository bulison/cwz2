package cn.fooltech.fool_ops.domain.initialReceivable.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import cn.fooltech.fool_ops.validator.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.Customer;
import cn.fooltech.fool_ops.domain.basedata.entity.Member;
import cn.fooltech.fool_ops.domain.basedata.service.BillRuleService;
import cn.fooltech.fool_ops.domain.basedata.service.CustomerService;
import cn.fooltech.fool_ops.domain.capital.entity.CapitalPlan;
import cn.fooltech.fool_ops.domain.capital.service.CapitalPlanService;
import cn.fooltech.fool_ops.domain.capital.vo.CapitalPlanVo;
import cn.fooltech.fool_ops.domain.flow.entity.Task;
import cn.fooltech.fool_ops.domain.flow.service.TaskBillService;
import cn.fooltech.fool_ops.domain.flow.vo.TaskBillVo;
import cn.fooltech.fool_ops.domain.initialPay.service.InitialPayService;
import cn.fooltech.fool_ops.domain.initialReceivable.repository.InitialReceivableRepository;
import cn.fooltech.fool_ops.domain.initialReceivable.vo.InitialReceivableVo;
import cn.fooltech.fool_ops.domain.member.service.MemberService;
import cn.fooltech.fool_ops.domain.payment.service.PaymentCheckService;
import cn.fooltech.fool_ops.domain.period.entity.StockPeriod;
import cn.fooltech.fool_ops.domain.period.service.StockPeriodService;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
import cn.fooltech.fool_ops.domain.warehouse.service.WarehouseBillService;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.utils.ErrorCode;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;

/**
 * <p>期初应收网页服务类</p>
 * @author xjh
 * @version 1.0
 * @date 2015-09-10 10:00:19
 */
@Service("ops.InitialReceivableWebService")
public class InitialReceivableService extends BaseService<WarehouseBill,InitialReceivableVo,String> {
	@Autowired
	private InitialReceivableRepository initialReceivableRepository;
	
	/**
	 * 仓库单据服务类
	 */
	@Resource(name="ops.WarehouseBillService")
	protected WarehouseBillService billService;
	
	/**
	 * 客户服务类
	 */
	@Autowired
	private CustomerService customerService;
	
	/**
	 * 人员服务类
	 */
	@Autowired
	private MemberService memberService;
	
	/**
	 * 会计期间服务类
	 */
	@Autowired
	private StockPeriodService periodService;
	
	/**
	 * 收付款单勾对服务类
	 */
	@Autowired
	private PaymentCheckService checkService;
	
	/**
	 * 单据单号生成规则
	 */
	@Autowired
	private BillRuleService ruleWebService;
	
	/**
	 * 期初应付网页服务类
	 */
	@Autowired
	private InitialPayService initialPayWebService;
	/**
	 * 资金计划网页服务类
	 */
	@Autowired
	private CapitalPlanService capitalPlanService;
	
	@Autowired
	private TaskBillService taskBillService;
	
	/**
	 * 是否允许修改删除：true：允许；false：不允许
	 * @return
	 */
	public boolean enableEditOrDelete(){
//		long count = checkService.countByColumn(Restrictions.eq("fiscalAccount.fid", getFiscalAccountId()));
//		return count>0?false:true;
		return initialPayWebService.checkStockPeriod();
	}
	/**
	 * 返回当前会计期间状态(没有会计期间返回-2)
	 * @return
	 */
	public Integer getTheFristPeriod(){
		//会计期间
		//找不到第一个会计期间
		StockPeriod period = periodService.getTheFristPeriod(SecurityUtil.getCurrentOrgId(), SecurityUtil.getFiscalAccountId());
		if (period==null) {
			return -2;
		}else{
			return period.getCheckoutStatus();
		}
	}
	/**
	 * 查询期初应收列表信息，按照期初应收主键降序排列<br>
	 * 默认为第一页，每页大小默认为10<br>
	 * @param vo
	 */
	public Page<WarehouseBill> query(InitialReceivableVo vo,PageParamater pageParamater){
		Sort sort = new Sort(Direction.DESC, "createTime");
		PageRequest request = getPageRequest(pageParamater,sort);
		return initialReceivableRepository.query(vo, request);

	}
	
	
	/**
	 * 单个期初应收实体转换为vo
	 * @param entity
	 * @return
	 */
	@Override
	public InitialReceivableVo getVo(WarehouseBill entity){
		if(entity == null)
			return null;
		InitialReceivableVo vo = new InitialReceivableVo();
		vo.setAmount(NumberUtil.stripTrailingZeros(entity.getTotalAmount()));
		vo.setDescribe(entity.getDescribe());
		vo.setCreateTime(entity.getCreateTime());
		vo.setUpdateTime(DateUtilTools.date2String(entity.getUpdateTime(), DATE_TIME));
		vo.setFid(entity.getFid());
		
		vo.setBillDate(DateUtilTools.date2String(entity.getBillDate(), DATE));
		vo.setFreeAmount(NumberUtil.stripTrailingZeros(entity.getFreeAmount()));
		vo.setCode(entity.getCode());
		
		if(entity.getInMember()!=null){
			vo.setMemberId(entity.getInMember().getFid());
			vo.setMemberName(entity.getInMember().getUsername());
			vo.setMemberCode(entity.getInMember().getUserCode());
		}
		if(entity.getCustomer()!=null){
			vo.setCustomerId(entity.getCustomer().getFid());
			vo.setCustomerName(entity.getCustomer().getName());
			vo.setCustomerCode(entity.getCustomer().getCode());
		}
		
		return vo;
	}
	
	/**
	 * 删除期初应收<br>
	 */
	@Transactional
	public RequestResult delete(String fid){
		try {
			//判断会计期间是否启用，才可以操作
			boolean result = enableEditOrDelete();
			if(result==false){
				new RequestResult(RequestResult.RETURN_FAILURE,"会计期间已启用才能删除");
			}
			//判断是否有付款数据，有则不能删除
			if(checkService.countByBillId(fid) > 0){
				return new RequestResult(RequestResult.RETURN_FAILURE, "已有收款记录，不能删除");
			}
			WarehouseBill bill = initialReceivableRepository.findOne(fid);
			if(bill==null){
				return buildFailRequestResult("该记录已不存在，请刷新界面");
			}
			TaskBillVo taskBillVo = taskBillService.queryByRelation(fid);
			if(taskBillVo!=null){
				
				String planName = taskBillVo.getPlanName();
				String taskName = taskBillVo.getTaskName();
				String msg = planName+"计划中的"+taskName+"事件已关联数据不能删除期初应收!";
				return buildFailRequestResult(msg);
			}
			WarehouseBill warehouseBill = billService.findOne(fid);
			RequestResult delete = billService.delete(warehouseBill);
			if(delete.getReturnCode()==1){
				return delete;
			}
//			billService.delete(fid);
			CapitalPlan capitalPlan = capitalPlanService.queryByRelation(fid);
			if(capitalPlan!=null){
				RequestResult del = capitalPlanService.del(capitalPlan.getId(),1);
				if(del.getReturnCode()==1){
					return del;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("删除期初应收出错!");
		}
		return new RequestResult(RequestResult.RETURN_SUCCESS,"刪除成功");
	}
	
	/**
	 * 获取期初应收信息
	 * @param fid 期初应收ID
	 * @return
	 */
	public InitialReceivableVo getByFid(String fid) {
		Assert.notNull(fid);
		return getVo(billService.findOne(fid));
	}
	

	/**
	 * 新增/编辑期初应收
	 * @param vo
	 */
	@Transactional
	public RequestResult save(InitialReceivableVo vo) {

		try {
			String inValid = ValidatorUtils.inValidMsg(vo);
			if (inValid != null) {
				return buildFailRequestResult(inValid);
			}

			Date now = new Date();
			
			if(vo.getAmount()==null){
				return new RequestResult(RequestResult.RETURN_FAILURE, "应付金额必填");
			}
			
			//会计期间
			StockPeriod period = periodService.getTheFristPeriod(SecurityUtil.getCurrentOrgId(), SecurityUtil.getFiscalAccountId());
			if(period == null){
				return new RequestResult(RequestResult.RETURN_FAILURE, ErrorCode.STOCK_PERIOD_NOT_EXIST, "找不到第一个会计期间");
			}
			if(period.getCheckoutStatus() == StockPeriod.CHECKED){
				return new RequestResult(RequestResult.RETURN_FAILURE, ErrorCode.STOCK_PERIOD_CHECKED, "会计期间已结账");
			}
			if(period.getCheckoutStatus() != StockPeriod.USED){
				return new RequestResult(RequestResult.RETURN_FAILURE, "会计期间已启用才能编辑期初应收");
			}
			Date billDate = period.getStartDate();
			
			Customer customer = null;
			if(StringUtils.isNotBlank(vo.getCustomerId())){
				customer = customerService.get(vo.getCustomerId());
			}else if(StringUtils.isNotBlank(vo.getCustomerCode())){
				customer = customerService.getByCode(SecurityUtil.getCurrentOrgId(), vo.getCustomerCode());
				if(customer==null)return new RequestResult(RequestResult.RETURN_FAILURE, "客户必填");
			}else{
				return new RequestResult(RequestResult.RETURN_FAILURE, "客户必填");
			}
			
			Member member = null;
			if(StringUtils.isNotBlank(vo.getMemberId())){
				member = memberService.get(vo.getMemberId());
			}else if(StringUtils.isNotBlank(vo.getMemberCode())){
				member = memberService.getByCode(SecurityUtil.getCurrentOrgId(), vo.getMemberCode());
				if(member ==null)return new RequestResult(RequestResult.RETURN_FAILURE, "负责人必填");
			}else{
				return new RequestResult(RequestResult.RETURN_FAILURE, "负责人必填");
			}
			
			
			WarehouseBill entity = new WarehouseBill();
			entity.setBillType(WarehouseBuilderCodeHelper.qcys);
			
			if(billService.countByBillTypeCustomer(SecurityUtil.getCurrentOrgId(), SecurityUtil.getFiscalAccountId(),
					entity.getBillType(), customer.getFid(), vo.getFid())>0){
				return new RequestResult(RequestResult.RETURN_FAILURE, "同一客户已存在期初应收");
			}
			
			if(StringUtils.isBlank(vo.getFid())){
				
				entity.setFreeAmount(vo.getFreeAmount()==null?BigDecimal.ZERO:vo.getFreeAmount());
				entity.setTotalAmount(vo.getAmount()==null?BigDecimal.ZERO:vo.getAmount());
				entity.setDescribe(vo.getDescribe());
				entity.setCreateTime(Calendar.getInstance().getTime());
				entity.setUpdateTime(new Date());
				
				entity.setBillDate(billDate);
				entity.setAuditor(SecurityUtil.getCurrentUser());
				entity.setAuditTime(now);
				
				entity.setCode(vo.getCode());
				if(StringUtils.isBlank(entity.getCode())){
					entity.setCode(getNewCode());
				}
				
				entity.setCustomer(customer);
				entity.setInMember(member);
				entity.setStockPeriod(period);
				entity.setCreator(SecurityUtil.getCurrentUser());
				entity.setOrg(SecurityUtil.getCurrentOrg());
				entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
				entity.setRecordStatus(WarehouseBill.STATUS_AUDITED);
				
				billService.save(entity);
			}else {
				entity = billService.get(vo.getFid());
				if(entity == null){
					return new RequestResult(RequestResult.RETURN_FAILURE, "该记录不存在或已被删除!");
				}
				String updateTime = DateUtilTools.date2String(entity.getUpdateTime(), DATE_TIME);
				if(!vo.getUpdateTime().equals(updateTime)){
					return new RequestResult(RequestResult.RETURN_FAILURE, "数据已被其他用户修改");
				}
				
				Long count = checkService.countByBillId(vo.getFid());
				if(count!=null && count>0){
					return new RequestResult(RequestResult.RETURN_FAILURE, "数据已被勾对不能修改");
				}
				TaskBillVo taskBillVo = taskBillService.queryByRelation(vo.getFid());
				if(taskBillVo!=null){
					String planName = taskBillVo.getPlanName();
					String taskName = taskBillVo.getTaskName();
					String msg = planName+"计划中的"+taskName+"事件已关联数据不能修改期初应收!";
					return buildFailRequestResult(msg);
				}
				
				entity.setBillDate(billDate);
				entity.setUpdateTime(now);
				entity.setFreeAmount(vo.getFreeAmount()==null?BigDecimal.ZERO:vo.getFreeAmount());
				entity.setTotalAmount(vo.getAmount()==null?BigDecimal.ZERO:vo.getAmount());
				entity.setDescribe(vo.getDescribe());
				entity.setStockPeriod(period);
				
				entity.setCustomer(customer);
				entity.setInMember(member);
				
				billService.save(entity);

			}
			/*资金计划新增策略:1.先删除资金计划；2.然后自动添加资金计划；3.审核资金计划*/
			CapitalPlan capitalPlan = capitalPlanService.queryByRelation(entity.getFid());
			if(capitalPlan!=null){
				RequestResult del = capitalPlanService.del(capitalPlan.getId(),1);
				if(del.getReturnCode()==1){
					return del;
				}
			}
			RequestResult passAudit = capitalPlanService.capitalPassAudit(WarehouseBuilderCodeHelper.qcys, entity.getFid());
			if(passAudit.getReturnCode()==1){
				return passAudit;
			}
			Object data = passAudit.getData();
			CapitalPlanVo capitalPlanVo = (CapitalPlanVo) data;
			CapitalPlan capitalPlan2 = capitalPlanService.findOne(capitalPlanVo.getId());
			if(capitalPlan2!=null){
				RequestResult passAudit2 = capitalPlanService.passAudit(capitalPlan2.getId(), CapitalPlan.STATUS_EXECUTING,DateUtils.getStringByFormat(capitalPlan2.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
				if(passAudit2.getReturnCode()==1){
					return passAudit2;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("期初应收保存出错!");
		}

		return new RequestResult(RequestResult.RETURN_SUCCESS,"保存成功");
	}
	
	/**
	 * 获取单号
	 */
	public String getNewCode(){
		return ruleWebService.getNewCode(WarehouseBuilderCodeHelper.qcys);
	}
	@Override
	public CrudRepository<WarehouseBill, String> getRepository() {
		// TODO Auto-generated method stub
		return null;
	}
}
