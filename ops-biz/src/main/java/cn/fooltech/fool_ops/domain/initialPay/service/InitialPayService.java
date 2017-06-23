package cn.fooltech.fool_ops.domain.initialPay.service;

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
import cn.fooltech.fool_ops.domain.basedata.entity.Member;
import cn.fooltech.fool_ops.domain.basedata.entity.Supplier;
import cn.fooltech.fool_ops.domain.basedata.service.BillRuleService;
import cn.fooltech.fool_ops.domain.basedata.service.SupplierService;
import cn.fooltech.fool_ops.domain.capital.entity.CapitalPlan;
import cn.fooltech.fool_ops.domain.capital.service.CapitalPlanService;
import cn.fooltech.fool_ops.domain.capital.vo.CapitalPlanVo;
import cn.fooltech.fool_ops.domain.flow.entity.Task;
import cn.fooltech.fool_ops.domain.flow.service.TaskBillService;
import cn.fooltech.fool_ops.domain.flow.vo.TaskBillVo;
import cn.fooltech.fool_ops.domain.initialPay.repository.InitialPayRepository;
import cn.fooltech.fool_ops.domain.initialPay.vo.InitialPayVo;
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
 * <p>期初应付网页服务类</p>
 * @author xjh
 * @version 1.0
 * @date 2015-09-10 10:00:19
 */
@Service
public class InitialPayService extends BaseService<WarehouseBill,InitialPayVo,String> {
	@Autowired
	private InitialPayRepository initialPayRepository;
	/**
	 * 仓库单据服务类
	 */
	@Resource(name="ops.WarehouseBillService")
	protected WarehouseBillService billService;
	
	/**
	 * 服务类
	 */
	@Autowired
	private SupplierService supplierService;
	
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
	 * 收付款勾兑记录表
	 */
	@Autowired
	private PaymentCheckService payCheckService;
	
	/**
	 * 单据单号生成规则
	 */
	@Autowired
	private BillRuleService ruleWebService;
	
	/**
	 * 资金计划网页服务类
	 */
	@Autowired
	private CapitalPlanService capitalPlanService;
	
	@Autowired
	private TaskBillService taskBillService;


	/**
	 * 会计期间只能在已启用的状态下可以操作
	 * 期初应收/付都在会计期间已启动可以进行增删改，未启用，已结账不允许操作。
	 * 是否允许修改删除：true：允许；false：不允许
	 * @return
	 */
	public boolean enableEditOrDelete(){
		return checkStockPeriod();
	}
	
	/**
	 * 查询期初应收列表信息，按照期初应收主键降序排列<br>
	 * 默认为第一页，每页大小默认为10<br>
	 * @param vo
	 */
	public Page<WarehouseBill> query(InitialPayVo vo,PageParamater pageParamater){
		Sort sort = new Sort(Direction.DESC, "createTime");
		PageRequest request = getPageRequest(pageParamater,sort);
		return initialPayRepository.query(vo, request);
	}
	
	
	/**
	 * 单个期初应收实体转换为vo
	 * @param entity
	 * @return
	 */
	@Override
	public InitialPayVo getVo(WarehouseBill entity){
		if(entity == null)
			return null;
		InitialPayVo vo = new InitialPayVo();
		vo.setAmount(NumberUtil.stripTrailingZeros(entity.getTotalAmount()));
		vo.setDescribe(entity.getDescribe());
		vo.setCreateTime(entity.getCreateTime());
		vo.setUpdateTime(DateUtilTools.date2String(entity.getUpdateTime(), DATE_TIME));
		vo.setFid(entity.getFid());
		
		vo.setBillDate(DateUtilTools.date2String(entity.getBillDate(), DATE));
		vo.setFreeAmount(NumberUtil.stripTrailingZeros(entity.getFreeAmount()));
		vo.setCode(entity.getCode());
		
		vo.setFid(entity.getFid());
		
		if(entity.getInMember()!=null){
			vo.setMemberId(entity.getInMember().getFid());
			vo.setMemberName(entity.getInMember().getUsername());
			vo.setMemberCode(entity.getInMember().getUserCode());
		}
		if(entity.getSupplier()!=null){
			vo.setSupplierId(entity.getSupplier().getFid());
			vo.setSupplierName(entity.getSupplier().getName());
			vo.setSupplierCode(entity.getSupplier().getCode());
		}
		
		return vo;
	}
	
	/**
	 * 删除期初应付<br>
	 */
	@Transactional
	public RequestResult delete(String fid){
		RequestResult delete;
		try {
			//判断会计期间是否启用，才可以操作
			boolean result = enableEditOrDelete();
			if(result==false){
				new RequestResult(RequestResult.RETURN_FAILURE,"会计期间已启用才能删除");
			}
			//判断是否有付款数据，有则不能删除
			if(payCheckService.countByBillId(fid) > 0){
				return new RequestResult(RequestResult.RETURN_FAILURE, "已有付款记录，不能删除");
			}
			WarehouseBill bill = initialPayRepository.findOne(fid);
			if(bill==null){
				return buildFailRequestResult("该记录已不存在，请刷新界面");
			}
			TaskBillVo taskBillVo = taskBillService.queryByRelation(fid);
			if(taskBillVo!=null){
				String planName = taskBillVo.getPlanName();
				String taskName = taskBillVo.getTaskName();
				return buildFailRequestResult(planName+"计划中的"+taskName+"事件已关联数据不能删除期初应付!");
			}
			WarehouseBill warehouseBill = billService.findOne(fid);
			delete = billService.delete(warehouseBill);
			CapitalPlan capitalPlan = capitalPlanService.queryByRelation(fid);
			if(capitalPlan!=null){
				RequestResult del = capitalPlanService.del(capitalPlan.getId(),1);
				if(del.getReturnCode()==1){
					return del;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("删除期初应付出错!");
		}
		return delete;
	}
	
	/**
	 * 获取期初应收信息
	 * @param fid 期初应收ID
	 * @return
	 */
	public InitialPayVo getByFid(String fid) {
		Assert.notNull(fid);
		return getVo(billService.findOne(fid));
	}
	

	/**
	 * 新增/编辑期初应付
	 * @param vo
	 */
	@Transactional
	public RequestResult save(InitialPayVo vo) {

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
				return new RequestResult(RequestResult.RETURN_FAILURE, "会计期间已启用才能编辑期初应付");
			}

			Date billDate = period.getStartDate();
			
			Supplier supplier = null;
			if(StringUtils.isNotBlank(vo.getSupplierId())){
				supplier = supplierService.get(vo.getSupplierId());
			}else if(StringUtils.isNotBlank(vo.getSupplierCode())){
				supplier = supplierService.getByCode(SecurityUtil.getCurrentOrgId(), vo.getSupplierCode());
				if(supplier==null)return new RequestResult(RequestResult.RETURN_FAILURE, "供应商必填");
			}else{
				return new RequestResult(RequestResult.RETURN_FAILURE, "供应商必填");
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
			entity.setBillType(WarehouseBuilderCodeHelper.qcyf);
			
			if(billService.countByBillTypeSupplier(SecurityUtil.getCurrentOrgId(), SecurityUtil.getFiscalAccountId(), 
					entity.getBillType(), supplier.getFid(), vo.getFid())>0){
				return new RequestResult(RequestResult.RETURN_FAILURE, "同一供应商已存在期初应付");
			}
			
			if(StringUtils.isBlank(vo.getFid())){
				
				entity.setFreeAmount(vo.getFreeAmount()==null?BigDecimal.ZERO:vo.getFreeAmount());
				entity.setTotalAmount(vo.getAmount()==null?BigDecimal.ZERO:vo.getAmount());
				entity.setDescribe(vo.getDescribe());
				entity.setCreateTime(Calendar.getInstance().getTime());
				entity.setAuditor(SecurityUtil.getCurrentUser());
				entity.setAuditTime(now);
				entity.setBillDate(billDate);
				entity.setUpdateTime(now);
				entity.setCode(vo.getCode());
				if(StringUtils.isBlank(entity.getCode())){
					entity.setCode(getNewCode());
				}
				
				entity.setRecordStatus(WarehouseBill.STATUS_AUDITED);
				
				entity.setInMember(member);
				entity.setSupplier(supplier);
				entity.setStockPeriod(period);
				entity.setCreator(SecurityUtil.getCurrentUser());
				entity.setOrg(SecurityUtil.getCurrentOrg());
				entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
				
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
				
				Long count = payCheckService.countByBillId(vo.getFid());
				if(count!=null && count>0){
					return new RequestResult(RequestResult.RETURN_FAILURE, "数据已被勾对不能修改");
				}
				TaskBillVo taskBillVo = taskBillService.queryByRelation(vo.getFid());
				if(taskBillVo!=null){
					String planName = taskBillVo.getPlanName();
					String taskName = taskBillVo.getTaskName();
					return buildFailRequestResult(planName+"计划中的"+taskName+"事件已关联数据不能修改期初应付!");
				}
				entity.setBillDate(billDate);
				entity.setUpdateTime(now);
				
				entity.setFreeAmount(vo.getFreeAmount()==null?BigDecimal.ZERO:vo.getFreeAmount());
				entity.setTotalAmount(vo.getAmount()==null?BigDecimal.ZERO:vo.getAmount());
				entity.setDescribe(vo.getDescribe());
				entity.setStockPeriod(period);
				
				entity.setSupplier(supplier);
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
			RequestResult passAudit = capitalPlanService.capitalPassAudit(WarehouseBuilderCodeHelper.qcyf, entity.getFid());
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
			return buildFailRequestResult("期初应付保存出错!");
		}

		return buildSuccessRequestResult();
	}

	/**
	 * 获取单号
	 */
	public String getNewCode(){
		return ruleWebService.getNewCode(WarehouseBuilderCodeHelper.qcyf);
	}
	/**
	 * 
	 * 判断会计期间是否可用
	 * @return false：不可用；true：可用；
	 */
	public boolean checkStockPeriod(){
		//会计期间
		//找不到第一个会计期间
		StockPeriod period = periodService.getTheFristPeriod(SecurityUtil.getCurrentOrgId(), SecurityUtil.getFiscalAccountId());
		if(period == null){
			return false;
		}
		//会计期间已结账
		if(period.getCheckoutStatus() == StockPeriod.CHECKED){
			return false;
		}
		//会计期间未启用
		if(period.getCheckoutStatus() == StockPeriod.UN_USED){
			return false;
		}
		return true;
	}

	@Override
	public CrudRepository<WarehouseBill, String> getRepository() {
		
		return null;
	}

}
