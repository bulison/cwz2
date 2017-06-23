package cn.fooltech.fool_ops.domain.flow.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.capital.entity.CapitalPlan;
import cn.fooltech.fool_ops.domain.capital.entity.CapitalPlanBill;
import cn.fooltech.fool_ops.domain.capital.service.CapitalPlanBillService;
import cn.fooltech.fool_ops.domain.capital.service.CapitalPlanService;
import cn.fooltech.fool_ops.domain.cost.entity.CostBill;
import cn.fooltech.fool_ops.domain.cost.repository.CostBillRepository;
import cn.fooltech.fool_ops.domain.flow.entity.Plan;
import cn.fooltech.fool_ops.domain.flow.entity.Task;
import cn.fooltech.fool_ops.domain.flow.entity.TaskBill;
import cn.fooltech.fool_ops.domain.flow.repository.TaskBillRepository;
import cn.fooltech.fool_ops.domain.flow.vo.TaskBillVo;
import cn.fooltech.fool_ops.domain.payment.entity.PaymentBill;
import cn.fooltech.fool_ops.domain.payment.repository.PaymentBillRepository;
import cn.fooltech.fool_ops.domain.payment.service.PaymentCheckService;
import cn.fooltech.fool_ops.domain.payment.service.PaymentCheckService.BillType;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
import cn.fooltech.fool_ops.domain.warehouse.repository.WarehouseBillRepository;
import cn.fooltech.fool_ops.domain.warehouse.service.WarehouseBillService;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.utils.JsonUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.VoFactory;

/**
 * 计划事件关联单据 服务类
 */
@Service
public class TaskBillService extends BaseService<TaskBill, TaskBillVo, String> {

	@Autowired
	private TaskBillRepository repository;

	@Resource(name = "ops.WarehouseBillService")
	private WarehouseBillService billService;

	@Autowired
	private PlanService planService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private PaymentCheckService payCheckService;
	
	@Autowired
	private WarehouseBillRepository billRepo;
	/**
	 * 收付款单服务类
	 */
	@Autowired
	private PaymentBillRepository paymentRepo;
	
	@Autowired
	private CostBillRepository costBillRepo;
	
	@Autowired
	private CapitalPlanBillService planBillService;
	
	@Autowired
	private CapitalPlanService capitalPlanService ;
	/**
	 * 实体转换VO
	 * 
	 * @param entity
	 * @return
	 */
	@Override
	public TaskBillVo getVo(TaskBill entity) {
		TaskBillVo vo = VoFactory.createValue(TaskBillVo.class, entity);
		if(!Strings.isNullOrEmpty(entity.getBill())){
			BillType type = payCheckService.checkBillType(entity.getBillSign());
			if(type==BillType.Warehouse){//仓储单据的表
				WarehouseBill bill = billRepo.findOne(entity.getBill());
				if(bill!=null){
					vo.setBillId(bill.getFid());
					vo.setBillName(bill.getCode());
					vo.setTotalAmount(bill.getTotalAmount());
				}
	
			}else if(type==BillType.Pay){//收付款单的表
				PaymentBill paymentBill = paymentRepo.findOne(entity.getBill());
				if(paymentBill!=null){
					vo.setBillId(paymentBill.getFid());
					vo.setBillName(paymentBill.getCode());
					vo.setTotalAmount(paymentBill.getAmount());
				}

				
			}else if(type==BillType.Cost){//费用单的表
				CostBill costBill2 = costBillRepo.findOne(entity.getBill());
				if(costBill2!=null){
					vo.setBillId(costBill2.getFid());
					vo.setBillName(costBill2.getCode());
					vo.setTotalAmount(costBill2.getAmount());
				}
			}
		}
//		WarehouseBill bill = entity.getBill();
//		if (bill != null) {
//			vo.setBillId(bill.getFid());
//			vo.setBillName(bill.getCode());
//		}
		Plan plan = entity.getPlan();
		if (plan != null) {
			vo.setPlanId(plan.getFid());
			vo.setPlanName(plan.getName());
		}
		Task task = entity.getTask();
		if (task != null) {
			vo.setTaskId(task.getFid());
			vo.setTaskName(task.getName());
		}
		return vo;
	}

	@Override
	public CrudRepository<TaskBill, String> getRepository() {
		return repository;
	}

	/**
	 * 查找分页
	 * 
	 * @param vo
	 * @param paramater
	 * @return
	 */
	public Page<TaskBillVo> query(TaskBillVo vo, PageParamater paramater) {
		Sort sort = new Sort(Sort.Direction.DESC, "code");
		PageRequest pageRequest = getPageRequest(paramater, sort);
		Page<TaskBill> page = repository.findPageBy(vo, pageRequest);
		return getPageVos(page, pageRequest);
	}

	/**
	 * 修改或新增
	 * 
	 * @param vo
	 * @return
	 */
	@Transactional
	public RequestResult save(TaskBillVo vo) {

		TaskBill entity = null;
		if (Strings.isNullOrEmpty(vo.getId())) {
			entity = new TaskBill();
			entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
			entity.setCreator(SecurityUtil.getCurrentUser());
			entity.setCreateTime(new Date());
			entity.setUpdateTime(new Date());
			entity.setOrg(SecurityUtil.getCurrentOrg());
		} else {
			entity = repository.findOne(vo.getId());

			if (entity.getUpdateTime().compareTo(vo.getUpdateTime()) != 0) {
				return buildFailRequestResult("数据已被其他用户修改，请刷新再试");
			}
		}
		entity.setBill(vo.getBillId());
		entity.setPlan(planService.get(vo.getPlanId()));
		entity.setTask(taskService.get(vo.getTaskId()));
		entity.setBillSign(vo.getBillSign());

		repository.save(entity);

		return buildSuccessRequestResult(getVo(entity));
	}

	@Transactional
	public RequestResult delete(String fid) {
		try {
			TaskBill taskBill = repository.findOne(fid);
			if(taskBill==null){
				return buildFailRequestResult("该数据已不存在，请刷新页面!");
			}
			Integer billSign = taskBill.getBillSign();
			String billId = taskBill.getBill();
			BigDecimal sumIncome = BigDecimal.ZERO;//合计流程计划收付款金额收入总和
			BigDecimal sumExpend = BigDecimal.ZERO;//合计流程计划收付款金额支出总和
			//删除时，把单据的事件设置为空，否则单据不能关联。
			if(billSign!=null){
				BillType type = payCheckService.checkBillType(billSign);
				if(type==BillType.Warehouse){//仓储单据的表
					WarehouseBill bill = billRepo.findOne(billId);
					if(bill!=null){
						bill.setTask(null);
						billRepo.save(bill);
					}
					
				}else if(type==BillType.Pay){//收付款单的表
					PaymentBill paymentBill = paymentRepo.findOne(billId);
					if(paymentBill!=null){
						paymentBill.setTask(null);
						paymentRepo.save(paymentBill);
						Integer billType = paymentBill.getBillType();
						
						if(billType==PaymentBill.TYPE_INCOME){
							sumIncome = paymentBill.getAmount();
						}
						else if(billType==PaymentBill.TYPE_EXPEND){
							sumExpend =paymentBill.getAmount();
						}
					}
					
				}else if(type==BillType.Cost){//费用单的表
					CostBill costBill2 = costBillRepo.findOne(billId);
					if(costBill2!=null){
						costBill2.setTask(null);
						costBillRepo.save(costBill2);
					}
				}
			}
			List<CapitalPlanBill> relationId = planBillService.queryByRelationId(billId);
			if(relationId.size()>0){
				CapitalPlanBill capitalPlanBill = relationId.get(0);
				CapitalPlan capital = capitalPlanBill.getDetail().getCapital();
				String code = capital.getCode();
				return buildFailRequestResult("资金计划单号为:"+code+"的资金计划明细表绑定单据不能取消关联单据!");
			}
			if (billSign == WarehouseBuilderCodeHelper.cgrk || billSign == WarehouseBuilderCodeHelper.cgth
					|| billSign == WarehouseBuilderCodeHelper.cgfp || billSign == WarehouseBuilderCodeHelper.cgfld
					|| billSign == WarehouseBuilderCodeHelper.xsch || billSign == WarehouseBuilderCodeHelper.xsth
					|| billSign == WarehouseBuilderCodeHelper.xsfp || billSign == WarehouseBuilderCodeHelper.xsfld
					|| billSign == WarehouseBuilderCodeHelper.shd || billSign == WarehouseBuilderCodeHelper.fyd
					|| billSign == WarehouseBuilderCodeHelper.qcys || billSign == WarehouseBuilderCodeHelper.qcyf) {
				CapitalPlan capitalPlan = capitalPlanService.queryByRelation(billId);
				if (capitalPlan != null) {
					capitalPlan.setCalculation(1);
					capitalPlanService.save(capitalPlan);
				}
			}
			String planId = taskBill.getPlan().getFid();
			Plan plan = planService.findOne(planId);
			/*维护流程计划的收入金额和支出金额*/
			plan.setInAmount(plan.getInAmount().subtract(sumIncome));
			plan.setOutAmount(plan.getOutAmount().subtract(sumExpend));
			repository.delete(fid);
			planService.save(plan);
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("删除计划事件关联单据失败!");
		}
		return buildSuccessRequestResult();
	}

	/**
	 * 关联单据，挂多张单据
	 * 
	 * @param vo
	 *            关联单据VO
	 * @return
	 */
	@Transactional
	public RequestResult relateTaskBill(String vos) {

		try {
			List<TaskBillVo> details = getDetails(vos);
			String taskId = details.get(0).getTaskId();
			if (Strings.isNullOrEmpty(taskId)){
				return buildFailRequestResult("事件id不能为空");
			}
			Task task = taskService.findOne(taskId);
			String planId="";
			if(task!=null){
				planId = task.getPlan().getFid();
			}
			delByTaskId(taskId);
			for (TaskBillVo taskBillVo : details) {
				Integer billSign = taskBillVo.getBillSign();
				String billId = taskBillVo.getBillId();
				/*
				 * 关联单据是采购入库、采购退货、采购发票、采购返利、销售出货、销售退货、销售发票、销售返利、收货单、
				 * 费用单、期初应收、期初应付，则查找资金计划表关联的该单据，把是否计算标识设置为不计算；
				 */
				if (billSign != null) {
					BillType type = payCheckService.checkBillType(billSign);
					if (type == BillType.Warehouse) {// 仓储单据的表
						WarehouseBill bill = billRepo.findOne(billId);
						if (bill != null) {
							// Task task = bill.getTask();
							// if(task!=null&&task.getFid()!=taskId){
							// return buildFailRequestResult("仓储单据已关联事件不能重复关联");
							// }
							bill.setTask(taskService.findOne(taskBillVo.getTaskId()));
							billRepo.save(bill);
						}

					} else if (type == BillType.Pay) {// 收付款单的表
						PaymentBill paymentBill = paymentRepo.findOne(billId);
						if (paymentBill != null) {
							// Task task = paymentBill.getTask();
							// if(task!=null&&task.getFid()!=taskId){
							// return buildFailRequestResult("收付款单已关联事件不能重复关联");
							// }
							paymentBill.setTask(taskService.findOne(taskBillVo.getTaskId()));
							paymentRepo.save(paymentBill);
						}

					} else if (type == BillType.Cost) {// 费用单的表
						CostBill costBill2 = costBillRepo.findOne(billId);
						if (costBill2 != null) {
							// Task task = costBill2.getTask();
							// if(task!=null&&task.getFid()!=taskId){
							// return buildFailRequestResult("费用单已关联事件不能重复关联");
							// }
							costBill2.setTask(taskService.findOne(taskBillVo.getTaskId()));
							costBillRepo.save(costBill2);
						}
					}
				}
				CapitalPlan capitalPlan = capitalPlanService.queryByRelation(billId);
				if (capitalPlan != null) {
					capitalPlan.setCalculation(0);
					capitalPlanService.save(capitalPlan);
				}
				// }
				save(taskBillVo);
			}
			Plan plan = planService.findOne(planId);
			
			if(plan!=null){
				BigDecimal sumIncome = BigDecimal.ZERO;//合计流程计划收付款金额收入总和
				BigDecimal sumExpend = BigDecimal.ZERO;//合计流程计划收付款金额支出总和
				List<TaskBillVo> list = queryByPlanId(planId);
				for (TaskBillVo taskBillVo : list) {
					Integer billSign = taskBillVo.getBillSign();
					String billId = taskBillVo.getBillId();
					if (billSign != null) {
						BillType type = payCheckService.checkBillType(billSign);
						 if (type == BillType.Pay) {// 收付款单的表
							PaymentBill paymentBill = paymentRepo.findOne(billId);
							if (paymentBill != null) {
								Integer billType = paymentBill.getBillType();
								if(billType==PaymentBill.TYPE_INCOME){
									sumIncome = sumIncome.add(paymentBill.getAmount());
								}
								else if(billType==PaymentBill.TYPE_EXPEND){
									sumExpend = sumExpend.add(paymentBill.getAmount());
								}
							}

						} 
					}
				}
				plan.setInAmount(sumIncome);
				plan.setOutAmount(sumExpend);
				planService.save(plan);
			}

			

		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("关联单据，挂多张单据操作失败!");
		}
		return buildSuccessRequestResult();

	}

	public List<TaskBillVo> queryByTaskId(String taskId) {
		List<TaskBill> list = repository.queryByTaskId(taskId);
		List<TaskBillVo> vos = getVos(list);
		return vos;
	}
	public List<TaskBillVo> queryByPlanId(String planId) {
		List<TaskBill> list = repository.queryByPlanId(planId);
		List<TaskBillVo> vos = getVos(list);
		return vos;
	}
	public List<TaskBill> queryByPlanId2(String planId) {
		List<TaskBill> list = repository.queryByPlanId(planId);
		return list;
	}

	@Transactional
	public void delByTaskId(String taskId) {
		repository.delByTaskId(taskId);
	}

	/**
	 * json转list
	 * 
	 * @param details
	 *            json数据
	 * @return
	 */
	public List<TaskBillVo> getDetails(String details) {
		List<TaskBillVo> list = JsonUtil.toObjectList(details, TaskBillVo.class);
		return list;
	}
	/**
	 * 根据单据id查询计划事件关联单据
	 * @param bill
	 * @return
	 */
	public TaskBillVo queryByRelation(String bill){
		TaskBill taskBill = repository.queryByRelation(bill);
		if(taskBill!=null){
			TaskBillVo vo = getVo(taskBill);
			return vo;
		}
		return null;
	}
}
