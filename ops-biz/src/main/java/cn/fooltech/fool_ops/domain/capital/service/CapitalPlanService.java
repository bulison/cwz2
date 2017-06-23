package cn.fooltech.fool_ops.domain.capital.service;

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

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.CustomerSupplierView;
import cn.fooltech.fool_ops.domain.basedata.service.BillRuleService;
import cn.fooltech.fool_ops.domain.capital.entity.CapitalPlan;
import cn.fooltech.fool_ops.domain.capital.entity.CapitalPlanDetail;
import cn.fooltech.fool_ops.domain.capital.repository.CapitalPlanRepository;
import cn.fooltech.fool_ops.domain.capital.vo.CapitalPlanDetailVo;
import cn.fooltech.fool_ops.domain.capital.vo.CapitalPlanVo;
import cn.fooltech.fool_ops.domain.cost.entity.CostBill;
import cn.fooltech.fool_ops.domain.cost.repository.CostBillRepository;
import cn.fooltech.fool_ops.domain.payment.entity.PaymentBill;
import cn.fooltech.fool_ops.domain.payment.repository.PaymentBillRepository;
import cn.fooltech.fool_ops.domain.payment.service.PaymentCheckService;
import cn.fooltech.fool_ops.domain.payment.vo.PaymentCheckVo;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
import cn.fooltech.fool_ops.domain.warehouse.repository.WarehouseBillRepository;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.DateUtils;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.VoFactory;
import net.sf.json.JSONArray;

/**
 * 
 * @Description: 资金计划 服务类
 * @author cwz
 * @date 2017年3月1日 下午2:08:46
 */
@Service
public class CapitalPlanService extends BaseService<CapitalPlan, CapitalPlanVo, String> {

	@Autowired
	private CapitalPlanRepository repository;
	@Autowired
	private CapitalPlanDetailService detailService;
	/* 收付款对单网页服务类 */
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
	/**
	 * 单据规则服务类
	 */
	@Autowired
	private BillRuleService billRuleService;

	/**
	 * 实体转换VO
	 * 
	 * @param entity
	 * @return
	 */
	@Override
	public CapitalPlanVo getVo(CapitalPlan entity) {
		CapitalPlanVo vo = VoFactory.createValue(CapitalPlanVo.class, entity);
		User auditor = entity.getAuditor();
		if (auditor != null) {
			vo.setAuditorId(auditor.getFid());
			vo.setAuditorName(auditor.getUserName());
		}
		User badDebt = entity.getBadDebt();
		if (badDebt != null) {
			vo.setBadDebtId(badDebt.getFid());
			vo.setBadDebtName(badDebt.getUserName());
		}
		User cancelor = entity.getCancelor();
		if (cancelor != null) {
			vo.setCancelorId(cancelor.getFid());
			vo.setCancelorName(cancelor.getUserName());
		}
		User complete = entity.getComplete();
		if (complete != null) {
			vo.setCompleteId(complete.getFid());
			vo.setCompleteName(complete.getUserName());
		}
		User creator = entity.getCreator();
		if (creator != null) {
			vo.setCreatorId(creator.getFid());
			vo.setCreatorName(creator.getUserName());
		}
		return vo;
	}

	@Override
	public CrudRepository<CapitalPlan, String> getRepository() {
		return repository;
	}

	/**
	 * 查找分页
	 * 
	 * @param vo
	 * @param paramater
	 * @return
	 */
	public Page<CapitalPlanVo> query(CapitalPlanVo vo, PageParamater paramater) {

		Sort sort = new Sort(Sort.Direction.DESC, "code");
		PageRequest pageRequest = getPageRequest(paramater, sort);
		Page<CapitalPlan> page = repository.findPageBy(vo, pageRequest);
		return getPageVos(page, pageRequest);
	}

	/**
	 * 修改或新增
	 * @param vo
	 * @param editType	可否限制：0-不限制，1-限制【期初应收，期初应付放开限制】；
	 * @return
	 */
	@Transactional
	public RequestResult save(CapitalPlanVo vo,int editType) {
		CapitalPlan entity;
		CapitalPlanVo vo2 = null;
		try {
//			Integer relationSign = vo.getRelationSign();
			// 判断主表关联类型是否为关联计划，是就不能新增；
//			if (relationSign != 0 && relationSign == 71) {
//				return buildFailRequestResult("关联类型是关联计划不能新增记录!");
//			}
			entity = null;
			if (Strings.isNullOrEmpty(vo.getId())) {
				entity = new CapitalPlan();
				entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
				entity.setCreator(SecurityUtil.getCurrentUser());
				entity.setCreateTime(new Date());
				entity.setUpdateTime(new Date());
				entity.setOrg(SecurityUtil.getCurrentOrg());
				entity.setRecordStatus(0);
			} else {
				entity = repository.findOne(vo.getId());
				Integer recordStatus = entity.getRecordStatus();
				Integer relationSign = entity.getRelationSign();
				if (recordStatus != 0 && editType==1&&
					relationSign!=WarehouseBuilderCodeHelper.qcys
					&&relationSign!=WarehouseBuilderCodeHelper.qcyf) {
					return buildFailRequestResult("只能在未审核状态下才能修改记录!");
				}
				if (entity.getUpdateTime().compareTo(vo.getUpdateTime()) != 0) {
					return buildFailRequestResult("数据已被其他用户修改，请刷新再试");
				}
				entity.setCreator(SecurityUtil.getCurrentUser());
				entity.setRecordStatus(vo.getRecordStatus());
				entity.setUpdateTime(new Date());
			}
			entity.setCode(vo.getCode());
			entity.setExplain(vo.getExplain());
			entity.setRelationSign(vo.getRelationSign());
			entity.setRelationId(vo.getRelationId());
			entity.setPaymentDate(vo.getPaymentDate());
			entity.setPlanAmount(vo.getPlanAmount());
			entity.setBillAmount(vo.getBillAmount());
			entity.setPaymentAmount(vo.getPaymentAmount());
			entity.setRemark(vo.getRemark());
			Integer calculation = vo.getCalculation()==null?1:vo.getCalculation();
			entity.setCalculation(calculation);
			// entity.setBadDebtSign(vo.getBadDebtSign());
			// entity.setBadDebtDate(vo.getBadDebtDate());
			// entity.setCompleteSign(vo.getCompleteSign());
			// entity.setCompleteDate(vo.getCompleteDate());

			repository.save(entity);
			detailService.delByCapitalId(entity.getId());
			String vos = vo.getDetails();
			if(!Strings.isNullOrEmpty(vos)){
				BigDecimal sumBillAmount = BigDecimal.ZERO;
				BigDecimal sumPaymentAmount = BigDecimal.ZERO;
				BigDecimal sumPlantAmount = BigDecimal.ZERO;
				List<CapitalPlanDetailVo> details = detailService.getDetails(vos);
				for (CapitalPlanDetailVo detailVo : details) {
					detailVo.setCapitalId(entity.getId());
					BigDecimal billAmount = detailVo.getBillAmount()==null?BigDecimal.ZERO:detailVo.getBillAmount();
					sumBillAmount = sumBillAmount.add(billAmount);
					BigDecimal plantAmount = detailVo.getPlanAmount()==null?BigDecimal.ZERO:detailVo.getPlanAmount();
					sumPlantAmount = sumPlantAmount.add(plantAmount);
					BigDecimal paymentAmount = detailVo.getPaymentAmount()==null?BigDecimal.ZERO:detailVo.getPaymentAmount();
					sumPaymentAmount = sumPaymentAmount.add(paymentAmount);
					detailService.save(detailVo);
				}
				entity.setBillAmount(sumBillAmount);
				entity.setPlanAmount(sumPlantAmount);
				entity.setPaymentAmount(sumPaymentAmount);
				// 主表的单据金额等于明细表的合计；
				repository.save(entity);
			}
			
			vo2 = getVo(entity);
			vo2.setId(entity.getId());
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("资金计划保存失败!");
		}

		return buildSuccessRequestResult(vo2);
	}

	/**
	 * 删除资金计划
	 * @param fid
	 * @param type 是否判断状态才删除：0-判断，1-不判断
	 * @return
	 */
	@Transactional
	public RequestResult del(String fid,int type) {
		try {
			CapitalPlan plan = repository.findOne(fid);
			if (plan == null) {
				return buildFailRequestResult("该记录已不删除,请刷新页面");
			}
			Integer recordStatus = plan.getRecordStatus();
			if (recordStatus != 0 && type==0) {
				return buildFailRequestResult("只能在未审核状态下才能删除记录!");
			}
			detailService.delByCapitalId(fid);
			repository.delete(fid);
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("删除资金计划出错!");
		}

		return buildSuccessRequestResult();
	}
	/**
	 * 根据仓库单据删除资金计划
	 * @param fid
	 * @param type 是否判断状态才删除：0-判断，1-不判断
	 * @return
	 */
	@Transactional
	public RequestResult delByWarehouseBill(String billId) {
		//TODO
		List<CapitalPlan> list=repository.queryByWarehouseId(billId);
		try {
			for(CapitalPlan plan:list){
			if (plan == null) {
				return buildFailRequestResult("该记录已不删除,请刷新页面");
			}
			detailService.delByCapitalId(plan.getId());
			repository.delete(plan.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("删除资金计划出错!");
		}

		return buildSuccessRequestResult();
	}
	/**
	 * 根据计划id修改状态【状态 0-草稿，1-审核，2-坏账，3-完成，4-取消】
	 * @param id			主键
	 * @param recordStatus	修改后的状态
	 * @param updateTime	修改时间
	 * @param editType		可否限制：0-不限制，1-限制
	 * @return
	 */
	@Transactional
	public RequestResult changeByCapitalId(String id, int recordStatus,String updateTime,Integer editType) {
		try {
			CapitalPlan entity = repository.findOne(id);
			if (entity == null) {
				return buildFailRequestResult("该记录已被删除,请刷新页面");
			}
			if (entity.getUpdateTime().compareTo(DateUtils.getDateFromString(updateTime)) != 0) {
				return buildFailRequestResult("数据已被其他用户修改，请刷新再试");
			}
			if(editType==null){
				editType = 1;//设置默认限制
			}
			Integer relationSign = entity.getRelationSign();
			Integer status = entity.getRecordStatus();
			// 审核
			// 自动写入审核人与审核时间,明细表记录也自动写入审核人与审核时间
			if (recordStatus == 1 && entity.getBadDebt() == null) {
				if (relationSign != 0) {
					return buildFailRequestResult("不是用户手动新增的记录不能审核!");
				}
				if (status != 0) {
					return buildFailRequestResult("只能在未审核状态下才能审核记录!");
				}
				entity.setRecordStatus(recordStatus);
				entity.setAuditor(SecurityUtil.getCurrentUser());
				entity.setAuditTime(new Date());
				entity.setUpdateTime(new Date());
				repository.save(entity);
				// 修改明细表状态
				List<CapitalPlanDetail> details = detailService.queryByCapitalId(id);
				for (CapitalPlanDetail detail : details) {
					Integer recordStatus2 = detail.getRecordStatus();
					if(recordStatus2==2) continue;
					detailService.changeByCapitalId(detail.getId(),recordStatus,DateUtils.getStringByFormat(detail.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"),editType);
					
				}
			}
			// 反审核
			if (recordStatus == 0) {
				if (relationSign != 0) {
					return buildFailRequestResult("不是用户手动新增的记录不能反审核!");
				}
				if (status != 1 && entity.getRelationSign()!=71) {
					return buildFailRequestResult("必须是审核状态下的记录才能反审核!");
				}
				entity.setRecordStatus(recordStatus);
				entity.setAuditor(null);
				entity.setAuditTime(null);
				entity.setUpdateTime(new Date());
				repository.save(entity);
				// 修改明细表状态
				List<CapitalPlanDetail> details = detailService.queryByCapitalId(id);
				for (CapitalPlanDetail detail : details) {
//					detail.setRecordStatus(recordStatus);
//					detail.setAuditor(null);
//					detail.setAuditTime(null);
//					detailService.save(detail);
					detailService.changeByCapitalId(detail.getId(),recordStatus,DateUtils.getStringByFormat(detail.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"),editType);
				}
			}
			// 取消
			if (recordStatus == 4) {
				// 判断状态是否为坏账（2）或完成（3），是就不能取消；
				if (status == 2 || status == 3) {
					return buildFailRequestResult("已完成/坏账不能取消!");
				}
				// 判断关联类型是否单据类型，是单据类型的不能取消；要在单据点作废才更改这个状态；
				if (relationSign != 0 && relationSign != 71) {
					return buildFailRequestResult("不是用户手动新增的记录不能取消");
				}
				entity.setRecordStatus(recordStatus);
				entity.setCancelor(SecurityUtil.getCurrentUser());
				entity.setCancelTime(new Date());
				entity.setUpdateTime(new Date());
				repository.save(entity);
				// 修改明细表状态
				List<CapitalPlanDetail> details = detailService.queryByCapitalId(id);
				for (CapitalPlanDetail detail : details) {
					Integer recordStatus2 = detail.getRecordStatus();
					if(recordStatus2==2||recordStatus2==3) continue;
					detail.setRecordStatus(recordStatus);
					detail.setCancelor(SecurityUtil.getCurrentUser());
					detail.setCancelTime(new Date());
					detailService.save(detail);
//					detailService.changeByCapitalId(detail.getId(),recordStatus,DateUtils.getStringByFormat(detail.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"),editType);
				}
			}
			// 准备坏账
			if (recordStatus == 2) {
				if (status != 1) {
					return buildFailRequestResult("必须是审核状态下的记录才能准备坏账!");
				}
				entity.setRecordStatus(recordStatus);
				entity.setBadDebt(SecurityUtil.getCurrentUser());
				entity.setBadDebtDate(new Date());
				entity.setUpdateTime(new Date());
				repository.save(entity);
				// 修改明细表状态
				List<CapitalPlanDetail> details = detailService.queryByCapitalId(id);
				for (CapitalPlanDetail detail : details) {
//					detail.setRecordStatus(recordStatus);
//					detail.setBadDebt(SecurityUtil.getCurrentUser());
//					detail.setBadDebtDate(new Date());
//					detailService.save(detail);
					detailService.changeByCapitalId(detail.getId(),recordStatus,DateUtils.getStringByFormat(detail.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"),editType);
				}
			}
			// 取消坏账
			if (recordStatus == 1 && entity.getBadDebt() != null) {
				if (status != 2) {
					return buildFailRequestResult("必须是坏账状态下的记录才能取消坏账!");
				}
				entity.setRecordStatus(recordStatus);
				entity.setBadDebt(null);
				entity.setBadDebtDate(null);
				entity.setUpdateTime(new Date());
				repository.save(entity);
				// 修改明细表状态
				List<CapitalPlanDetail> details = detailService.queryByCapitalId(id);
				for (CapitalPlanDetail detail : details) {
//					detail.setRecordStatus(recordStatus);
//					detail.setBadDebt(null);
//					detail.setBadDebtDate(null);
//					detailService.save(detail);
					detailService.changeByCapitalId(detail.getId(),recordStatus,DateUtils.getStringByFormat(detail.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"),editType);
				}
			}
			// 完成：判断状态是否为审核（1），不是就不能完成；
			if (recordStatus == 3) {
				if (status != 1) {
					return buildFailRequestResult("必须是审核状态下的记录才能完成!");
				}
				BigDecimal planAmount = entity.getPlanAmount()==null?BigDecimal.ZERO:entity.getPlanAmount();
				BigDecimal paymentAmount = entity.getPaymentAmount()==null?BigDecimal.ZERO:entity.getPaymentAmount();
				BigDecimal billAmount = entity.getBillAmount()==null?BigDecimal.ZERO:entity.getBillAmount();
				
				/*如果主表关联类型为计划，要判断计划是否已经绑定单据，已处理好收付款，如果未处理好，提示未绑定单据或未收付款，是否完成？不限制完成操作*/
				if (relationSign == 71 && editType ==1) {
					if(billAmount.compareTo(BigDecimal.ZERO)==0){
						return buildFailRequestResult("该记录未绑定单据,是否继续完成!");
					}
					if(paymentAmount.compareTo(BigDecimal.ZERO)==0){
						return buildFailRequestResult("该记录没有收付款操作,是否继续完成!");
					}
					int compareTo = planAmount.compareTo(billAmount);
					if (compareTo != 0){
						return buildFailRequestResult("单据绑定金额与计划金额不一致,是否继续完成!");
					}
					int compareTo2 = planAmount.compareTo(paymentAmount);
					if (compareTo2 != 0){
						return buildFailRequestResult("收/付款操作还未完成,是否继续完成!");
					}
				} else if (relationSign > 0 && relationSign!=71) {
					/*如果主表关联类型为单据，要判断单据是否已经收/付完款，如果没有，不能完成；*/
					int compareTo = billAmount.compareTo(paymentAmount);
					if (compareTo != 0)
						return buildFailRequestResult("请先完成收/付款操作!");
				}
				entity.setRecordStatus(recordStatus);
				entity.setComplete(SecurityUtil.getCurrentUser());
				entity.setCompleteDate(new Date());
				entity.setUpdateTime(new Date());
				repository.save(entity);
				// 修改明细表状态
				List<CapitalPlanDetail> details = detailService.queryByCapitalId(id);
				for (CapitalPlanDetail detail : details) {
//					detail.setRecordStatus(recordStatus);
//					detail.setComplete(SecurityUtil.getCurrentUser());
//					detail.setCompleteDate(new Date());
//					detailService.save(detail);
					detailService.changeByCapitalId(detail.getId(),recordStatus,DateUtils.getStringByFormat(detail.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"),editType);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("计划状态修改出错!");
		}
		return buildSuccessRequestResult();
	}

	/**
	 * 根据关联id返回资金计划和明细
	 * 
	 * @param relationId
	 *            关联id
	 * @return
	 */
	public CapitalPlanVo queryByWarehouse(String relationId) {
		CapitalPlan entity = repository.queryByRelation(relationId);
		if(entity!=null){
			List<CapitalPlanDetail> details = detailService.queryByCapitalId(entity.getId());
			if(details.size()>0){
				List<CapitalPlanDetailVo> vos = detailService.getVos(details);
				String jsonDetails = JSONArray.fromObject(vos).toString();
				CapitalPlanVo vo = getVo(entity);
				vo.setDetails(jsonDetails);
				return vo;
			}
		}
		return null;
		
	}
	/*
	 * 判断资金计划是否有记录，如果没有记录，弹出询问框，询问用户是否编辑资金计划，如果不编辑，系统会自动增加资金计划，如果用户点编辑，
	 * 则同点击资金计划处理，如果点不编辑，则按以下规则，系统自动在资金计划中增加记录；
	 * 采购入库、采购退货、采购发票、采购返利、销售发票、销售返利、收货单、费用单以单据日期作为预计收付款日期，金额为单据金额，插入一条记录；
	 * 销售出货、销售退货以单据的计划收款日期作为预计收付款日期，金额为单据金额，插入一条记录；
	 * 期初应收、期初应付以当前日期作为预计收付款日期，金额为单据金额，插入一条记录； 审核时对资金计划的金额的正负值做相应的修改，
	 * 采购入库、采购发票、销售退货、销售返利、收货单、期初应付作负数处理， 采购退货、采购返利、销售出货、销售发票、期初应收作正数处理，
	 * 费用单的费用标识为不处理的按费用金额为准，即以费用金额的正负数来确定资金计划的正负数，
	 * 费用标识为增加应收/应付，而且关联的收支单位是供应商，作负数处理，收支单位为客户，作正数处理，
	 * 费用标识为减少应收/应付，而且关联的收支单位是供应商，作正数处理，收支单位为客户，作负数处理；
	 * 审核后把相应的资金计划表和资金计划明细表的记录状态改为审核状态，更新审核人与审核时间；
	 */
	
	/**
	 * 资金计划自动添加记录（仓储审核时调用）
	 * @param relationSign	关联类型
	 * @param relationId	关联id
	 * @return
	 */
	@Transactional
	public RequestResult capitalPassAudit(int relationSign, String relationId) {
		RequestResult save = new RequestResult();
		try {
			CapitalPlanVo queryByWarehouse = queryByWarehouse(relationId);
			if(queryByWarehouse!=null){
				return buildFailRequestResult("该单据已存在资金计划记录!");
			}
			CapitalPlanVo vo = new CapitalPlanVo();
			/*
			 * 采购入库、采购退货、采购发票、采购返利、销售发票、销售返利、收货单、费用单以单据日期作为预计收付款日期，金额为单据金额，插入一条记录；
			 */
			if (relationSign == WarehouseBuilderCodeHelper.cgrk || relationSign == WarehouseBuilderCodeHelper.cgth
					|| relationSign == WarehouseBuilderCodeHelper.cgfp || relationSign == WarehouseBuilderCodeHelper.cgfld
					|| relationSign == WarehouseBuilderCodeHelper.xsfp || relationSign == WarehouseBuilderCodeHelper.xsfld
					|| relationSign == WarehouseBuilderCodeHelper.shd || relationSign == WarehouseBuilderCodeHelper.fyd) {
				if (relationSign == WarehouseBuilderCodeHelper.fyd) {
					CostBill bill = costBillRepo.findOne(relationId);
				    /**费用标识:0--不处理,1--增加往来单位应收/应付款项,2--减少往来单位应收/应付款项*/
					Integer expenseType = bill.getExpenseType();
					vo.setPaymentDate(bill.getBillDate());
					vo.setCode(bill.getCode());
					CustomerSupplierView csv = bill.getCsv();
					if(expenseType==0){//不处理*-1
						vo.setBillAmount(bill.getAmount().multiply(new BigDecimal(-1)));
					}else if(expenseType==1){//增加供应商*-1
						if(csv!=null){
							/*费用标识为减少应收/应付，而且关联的收支单位是供应商，作正数处理，收支单位为客户，作负数处理；*/
							if(bill.getCsv().getType()==CustomerSupplierView.TYPE_SUPPLIER){
								vo.setBillAmount(bill.getAmount().multiply(new BigDecimal(-1)));
							}else{
								vo.setBillAmount(bill.getAmount());
							}
						}
					}else if(expenseType==2){//减少客户*-1
						if(csv!=null){
							/*费用标识为减少应收/应付，而且关联的收支单位是供应商，作正数处理，收支单位为客户，作负数处理；*/
							if(bill.getCsv().getType()==CustomerSupplierView.TYPE_CUSTOMER){
								vo.setBillAmount(bill.getAmount().multiply(new BigDecimal(-1)));
							}else{
								vo.setBillAmount(bill.getAmount());
							}
						}
					}
					
				} else if (relationSign == WarehouseBuilderCodeHelper.cgfld
						|| relationSign == WarehouseBuilderCodeHelper.xsfld) {
					PaymentBill paymentBill = paymentRepo.findOne(relationId);
					vo.setCode(paymentBill.getCode());
					vo.setPaymentDate(paymentBill.getBillDate());
					vo.setBillAmount(paymentBill.getAmount());
				} else if (relationSign == WarehouseBuilderCodeHelper.shd) {
					WarehouseBill bill = billRepo.findOne(relationId);
					vo.setCode(bill.getCode());
					vo.setPaymentDate(bill.getBillDate());
					BigDecimal totalAmount = bill.getTotalAmount()==null?BigDecimal.ZERO:bill.getTotalAmount();
					BigDecimal deductionAmount = bill.getDeductionAmount()==null?BigDecimal.ZERO:bill.getDeductionAmount();
					vo.setBillAmount(totalAmount.subtract(deductionAmount));
				}else{
					WarehouseBill bill = billRepo.findOne(relationId);
					vo.setCode(bill.getCode());
					vo.setPaymentDate(bill.getBillDate());
					vo.setBillAmount(bill.getTotalAmount());
				}

			}
			/* 销售出货、销售退货以单据的计划收款日期作为预计收付款日期，金额为单据金额，插入一条记录； */
			else if (relationSign == WarehouseBuilderCodeHelper.xsch || relationSign == WarehouseBuilderCodeHelper.xsth) {
				WarehouseBill bill = billRepo.findOne(relationId);
				vo.setCode(bill.getCode());
				vo.setPaymentDate(bill.getEndDate());
				vo.setBillAmount(bill.getTotalAmount());
			}
			/* 期初应收、期初应付以当前日期作为预计收付款日期，金额为单据金额，插入一条记录； */
			else if (relationSign == WarehouseBuilderCodeHelper.qcys || relationSign == WarehouseBuilderCodeHelper.qcyf) {
				WarehouseBill bill = billRepo.findOne(relationId);
				String currentDate = DateUtils.getCurrentDate();
				Date dateFromString = DateUtils.getDateFromString(currentDate);
				vo.setCode(bill.getCode());
				vo.setPaymentDate(dateFromString);
				vo.setBillAmount(bill.getTotalAmount());
			}
			/* 采购入库、采购发票、销售退货、销售返利、收货单、期初应付作负数处理， */
			if (relationSign == WarehouseBuilderCodeHelper.cgrk || relationSign == WarehouseBuilderCodeHelper.cgfp
					|| relationSign == WarehouseBuilderCodeHelper.xsth || relationSign == WarehouseBuilderCodeHelper.xsfld
					|| relationSign == WarehouseBuilderCodeHelper.shd || relationSign == WarehouseBuilderCodeHelper.qcyf) {

				vo.setBillAmount(vo.getBillAmount().multiply(new BigDecimal(-1)));
			}
			vo.setRelationSign(relationSign);
			vo.setRelationId(relationId);
//			//新增获取单号
//			String code = billRuleService.getNewCode(100);
//			vo.setCode(code);
			save = save(vo,0);
			Object data = save.getData();
			CapitalPlanVo capitalPlanVo = (CapitalPlanVo) data;
			CapitalPlanDetailVo capitalPlanDetailVo = new CapitalPlanDetailVo();
			CapitalPlan capitalPlan = findOne(capitalPlanVo.getId());
			if(capitalPlan!=null){
				//添加明细记录
				capitalPlanDetailVo.setCapitalId(capitalPlanVo.getId());
				capitalPlanDetailVo.setExplain(capitalPlanVo.getExplain());
				capitalPlanDetailVo.setRelationSign(relationSign);
				capitalPlanDetailVo.setRelationId(relationId);
				capitalPlanDetailVo.setPaymentDate(capitalPlanVo.getPaymentDate());
				capitalPlanDetailVo.setPlanAmount(capitalPlanVo.getPlanAmount());
				capitalPlanDetailVo.setBillAmount(capitalPlanVo.getBillAmount());
				capitalPlanDetailVo.setPaymentAmount(capitalPlanVo.getPaymentAmount());
				capitalPlanDetailVo.setRemark(capitalPlanVo.getRemark());
				capitalPlanDetailVo.setRecordStatus(capitalPlanVo.getRecordStatus());
				capitalPlanDetailVo.setCompleteSign(capitalPlanVo.getCompleteSign());
				RequestResult result = detailService.save(capitalPlanDetailVo);
				if(result.getReturnCode()==1){
					return result;
				}
//				capitalPlan.setRecordStatus(CapitalPlan.STATUS_EXECUTING);
//				capitalPlan.setAuditTime(new Date());
//				capitalPlan.setAuditor(SecurityUtil.getCurrentUser());
//				save(capitalPlan);
//				// 修改明细表状态
//				List<CapitalPlanDetail> details = detailService.queryByCapitalId(capitalPlan.getId());
//				for (CapitalPlanDetail detail : details) {
//					detail.setRecordStatus(CapitalPlan.STATUS_EXECUTING);
//					detail.setAuditor(SecurityUtil.getCurrentUser());
//					detail.setAuditTime(new Date());
//					detail.setOrgPaymentDate(new Date());
//					detailService.save(detail);
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("资金计划自动添加出错!");
		}
		return buildSuccessRequestResult(save.getData());
	}

	/**
	 * 判断资金计划的金额是否等于单据金额
	 * @param relationId	单据id
	 * @param billAmount	仓储单据金额
	 * @return
	 */
	public RequestResult checkPlanAmount(String relationId,BigDecimal billAmount) {
		/*
		 * 如果有资金计划记录，判断资金计划的金额合计是否等于单据金额，如果不等，提示用户资金计划的金额不等于单据的金额，是否进行编辑，如果点编辑，
		 * 则同点击资金计划处理，如果点不编辑，则退出审核流程；
		 */
		CapitalPlan entity = repository.queryByRelation(relationId);
		BigDecimal capitailBillAmount = entity.getBillAmount();
		Integer relationSign = entity.getRelationSign();
		/* 采购入库、采购发票、销售退货、销售返利、收货单、期初应付作负数处理， */
		if (relationSign == WarehouseBuilderCodeHelper.cgrk || relationSign == WarehouseBuilderCodeHelper.cgfp
				|| relationSign == WarehouseBuilderCodeHelper.xsth || relationSign == WarehouseBuilderCodeHelper.xsfld
				|| relationSign == WarehouseBuilderCodeHelper.shd || relationSign == WarehouseBuilderCodeHelper.qcyf) {

			capitailBillAmount = capitailBillAmount.multiply(new BigDecimal(-1));
		}
		if (capitailBillAmount.compareTo(billAmount) != 0) {
			return buildFailRequestResult("资金计划的金额不等于单据金额,请编辑后在审核!");
		}
		CapitalPlanVo vo = getVo(entity);
		return buildSuccessRequestResult(vo);

	}
	
	/**
	 * 收付款单对单、取消对单时处理：
	 * @param vo	勾对表单VO
	 * @param type  1-对单操作，2-取消对单操作
	 * @return
	 */
	@Transactional
	public RequestResult changePaymentAmount(PaymentCheckVo vo, int type) {
		/*
		 * 重新计算本次对单的单据，计算单据的累计收付款金额+优惠金额，写入资金计划明细表的收付款金额 更新主表的收付款金额； 
		 * 如果主表的单据金额=收付款金额，则自动把状态变为完成，把明细的记录也标为完成；
		 * 取消对单时执行反操作，把完成状态变为审核状态；
		 */
		try {
			String billId = vo.getBillId();
			// 单据累计勾对金额
			BigDecimal billTotalPayAmount = vo.getBillTotalPayAmount() == null ? BigDecimal.ZERO:vo.getBillTotalPayAmount();
			// 单据免单金额
			BigDecimal freeAmount = vo.getBillFreeAmount() == null ? BigDecimal.ZERO : vo.getBillFreeAmount();
//			BigDecimal billTotalAmount = vo.getBillTotalAmount();// 单据开单金额
//			BigDecimal amount = vo.getAmount();// 本次勾对金额
			BigDecimal resultAmout = BigDecimal.ZERO;
			resultAmout = billTotalPayAmount.add(freeAmount);// 总收付款金额+优惠金额
//			if (type == 1) {
//				
//			} else if (type == 2) {
//				resultAmout = billTotalPayAmount.multiply(new BigDecimal(-1)).add(freeAmount);// 总收付款金额+优惠金额
//			}
			CapitalPlan capitalPlan = queryByRelation(billId);
			if(capitalPlan!=null){
				BigDecimal billAmount2 = capitalPlan.getBillAmount();
				Integer relationSign = capitalPlan.getRelationSign();
				
				int zhengfu = 1;
				/*   如果单据金额小于0,单据金额*-1  */
				if (billAmount2.compareTo(BigDecimal.ZERO) < 0 && relationSign != WarehouseBuilderCodeHelper.fyd) {
					zhengfu = -1;
				}
				/*采购退货、采购入库、采购发票、期初应收作负数处理*/
				if (relationSign == WarehouseBuilderCodeHelper.cgth || relationSign == WarehouseBuilderCodeHelper.cgrk ||
					relationSign == WarehouseBuilderCodeHelper.cgfp|| relationSign == WarehouseBuilderCodeHelper.qcyf) {
					zhengfu = -1;
				}
				BigDecimal operAmount = resultAmout.multiply(new BigDecimal(zhengfu));// 对单可操作金额
				List<CapitalPlanDetail> details = detailService.queryByCapitalId(capitalPlan.getId());

				for (CapitalPlanDetail detail : details) {
					BigDecimal billAmount = detail.getBillAmount();
					if (operAmount.abs().compareTo(billAmount.abs()) >= 0) {
							detail.setPaymentAmount(billAmount);
							operAmount = operAmount.subtract(billAmount);
							
					} else if (operAmount.abs().compareTo(BigDecimal.ZERO) > 0 && operAmount.abs().compareTo(billAmount.abs())<=0) {
						detail.setPaymentAmount(operAmount);
						operAmount = operAmount.subtract(operAmount);
					}else{
						detail.setPaymentAmount(BigDecimal.ZERO);
					}
					detailService.save(detail);
					if(detail.getPaymentAmount().compareTo(detail.getBillAmount())==0){
						CapitalPlanDetail findOne = detailService.findOne(detail.getId());
						RequestResult result = detailService.detailComplete(detail.getId(), CapitalPlan.STATUS_COMPLETE, DateUtils.getStringByFormat(findOne.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
						if(result.getReturnCode()==1){
							return result;
						}
					}else{
						detail.setRecordStatus(CapitalPlan.STATUS_EXECUTING);
						detail.setAuditor(SecurityUtil.getCurrentUser());
						detail.setAuditTime(new Date());
						detail.setUpdateTime(new Date());
						detail.setComplete(null);
						detail.setCompleteDate(null);
						detailService.save(detail);
					}
					
				}
				if (relationSign == WarehouseBuilderCodeHelper.cgth||relationSign == WarehouseBuilderCodeHelper.cgfld) {
					capitalPlan.setPaymentAmount(resultAmout);
				}else{
					capitalPlan.setPaymentAmount(resultAmout.multiply(new BigDecimal(zhengfu)));
				}
				save(capitalPlan);
				/* 如果主表的单据金额 = 收付款金额，则自动把状态变为完成，把明细的记录也标为完成； */
				if (capitalPlan.getBillAmount().compareTo(capitalPlan.getPaymentAmount()) == 0) {
					CapitalPlan findOne = findOne(capitalPlan.getId());
					RequestResult capitalId = complete(capitalPlan.getId(), CapitalPlan.STATUS_COMPLETE,DateUtils.getStringByFormat(findOne.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
					if(capitalId.getReturnCode()==1){
						return capitalId;
					}
				}else{
					capitalPlan.setRecordStatus(CapitalPlan.STATUS_EXECUTING);
					capitalPlan.setAuditor(SecurityUtil.getCurrentUser());
					capitalPlan.setAuditTime(new Date());
					capitalPlan.setUpdateTime(new Date());
					capitalPlan.setComplete(null);
					capitalPlan.setCompleteDate(null);
					repository.save(capitalPlan);

				}
				if (capitalPlan.getPaymentAmount().compareTo(BigDecimal.ZERO) == 0) {
					CapitalPlan findOne = findOne(capitalPlan.getId());
					RequestResult capitalId = passAudit(capitalPlan.getId(), CapitalPlan.STATUS_EXECUTING,DateUtils.getStringByFormat(findOne.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
					if(capitalId.getReturnCode()==1){
						return capitalId;
					}
				}
			}
			

		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("收付款单对单/取消对单时处理出错!");
		}
		return buildSuccessRequestResult();
	}

	/**
	 * 根据关联id查询资金计划
	 * @param relationId	关联id
	 * @return
	 */
	public CapitalPlan queryByRelation(String relationId){
		CapitalPlan entity = repository.queryByRelation(relationId);
		return entity;
	}
	/**
	 * 资金计划和明细审核（仓储审核调用）
	 * @param fid
	 * @return
	 */
	public RequestResult storagePassAudit(String fid){
		try {
			CapitalPlan capitalPlan = findOne(fid);
			if(capitalPlan!=null){
				capitalPlan.setRecordStatus(CapitalPlan.STATUS_EXECUTING);
				capitalPlan.setAuditTime(new Date());
				capitalPlan.setAuditor(SecurityUtil.getCurrentUser());
				capitalPlan.setUpdateTime(new Date());
				save(capitalPlan);
				// 修改明细表状态
				List<CapitalPlanDetail> details = detailService.queryByCapitalId(capitalPlan.getId());
				for (CapitalPlanDetail detail : details) {
//					detailService.changeByCapitalId(detail.getId(), CapitalPlan.STATUS_EXECUTING,DateUtils.getStringByFormat(detail.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
					detail.setRecordStatus(CapitalPlan.STATUS_EXECUTING);
					detail.setAuditor(SecurityUtil.getCurrentUser());
					detail.setAuditTime(new Date());
					detail.setOrgPaymentDate(new Date());
					detailService.save(detail);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("资金计划审核出错!");
		}
		return buildSuccessRequestResult();

	}

	/**
	 * 资金计划 审核
	 * @param id
	 * @param recordStatus
	 * @param updateTime
	 * @return
	 */
	public RequestResult passAudit(String id, int recordStatus,String updateTime) {
		try {
			CapitalPlan entity = repository.findOne(id);
			if (entity == null) {
				return buildFailRequestResult("该记录已被删除,请刷新页面");
			}
			String date2String = DateUtilTools.date2String(entity.getUpdateTime(), "yyyy-MM-dd HH:mm:ss");
			if (!date2String.equals(updateTime)) {
				return buildFailRequestResult("数据已被其他用户修改，请刷新再试");
			}
//			Integer status = entity.getRecordStatus();
			// 审核
			// 自动写入审核人与审核时间,明细表记录也自动写入审核人与审核时间
//			if (recordStatus == 1 && entity.getBadDebt() == null) {
//				if (status != 0) {
//					return buildFailRequestResult("只能在未审核状态下才能审核记录!");
//				}
				entity.setRecordStatus(recordStatus);
				entity.setAuditor(SecurityUtil.getCurrentUser());
				entity.setAuditTime(new Date());
				entity.setUpdateTime(new Date());
				entity.setComplete(null);
				entity.setCompleteDate(null);
				repository.save(entity);
				// 修改明细表状态
				List<CapitalPlanDetail> details = detailService.queryByCapitalId(id);
				for (CapitalPlanDetail detail : details) {
					// 审核
					// 自动写入审核人与审核时间,明细表记录也自动写入审核人与审核时间
//					if (recordStatus == 1 && detail.getBadDebt() == null) {
//						if (status != 0) {
//							return buildFailRequestResult("只能在未审核状态下才能审核记录!");
//						}
						detail.setRecordStatus(recordStatus);
						detail.setAuditor(SecurityUtil.getCurrentUser());
						detail.setAuditTime(new Date());
						detail.setUpdateTime(new Date());
						detail.setComplete(null);
						detail.setCompleteDate(null);
						Date paymentDate = detail.getPaymentDate();
						Date orgPaymentDate = detail.getOrgPaymentDate();
						if(orgPaymentDate==null){
							detail.setOrgPaymentDate(paymentDate);
						}
						detailService.save(detail);
//					}
					
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("资金计划 审核出错!");
		}
		return buildSuccessRequestResult();
	}
	/**
	 * 资金计划反审核
	 * @param id
	 * @param recordStatus
	 * @param updateTime
	 * @return
	 */
	public RequestResult returnPassAudit(String id, int recordStatus, String updateTime) {
		try {
			CapitalPlan entity = repository.findOne(id);
			if (entity == null) {
				return buildFailRequestResult("该记录已被删除,请刷新页面");
			}
			if (entity.getUpdateTime().compareTo(DateUtils.getDateFromString(updateTime)) != 0) {
				return buildFailRequestResult("数据已被其他用户修改，请刷新再试");
			}
			Integer status = entity.getRecordStatus();
			// 反审核
			if (status != 1&&entity.getRelationSign()!=71) {
				return buildFailRequestResult("必须是审核状态下的记录才能反审核!");
			}
			entity.setRecordStatus(recordStatus);
			entity.setAuditor(null);
			entity.setAuditTime(null);
			entity.setUpdateTime(new Date());
			repository.save(entity);
			// 修改明细表状态
			List<CapitalPlanDetail> details = detailService.queryByCapitalId(id);
			for (CapitalPlanDetail detail : details) {
				detail.setRecordStatus(recordStatus);
				detail.setAuditor(null);
				detail.setAuditTime(null);
				detail.setUpdateTime(new Date());
				detailService.save(detail);

			}

		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("资金计划 反审核出错!");
		}
		return buildSuccessRequestResult();
	}
	/**
	 * 资金计划取消
	 * @param id
	 * @param recordStatus
	 * @param updateTime
	 * @return
	 */
	public RequestResult cancel(String id, int recordStatus,String updateTime) {
		try {
			CapitalPlan entity = repository.findOne(id);
			if (entity == null) {
				return buildFailRequestResult("该记录已被删除,请刷新页面");
			}
			if (entity.getUpdateTime().compareTo(DateUtils.getDateFromString(updateTime)) != 0) {
				return buildFailRequestResult("数据已被其他用户修改，请刷新再试");
			}
			Integer status = entity.getRecordStatus();
			// 取消
			if (recordStatus == 4) {
				// 判断状态是否为坏账（2）或完成（3），是就不能取消；
				if (status == 2 || status == 3) {
					return buildFailRequestResult("已完成/坏账不能取消!");
				}
				entity.setRecordStatus(recordStatus);
				entity.setCancelor(SecurityUtil.getCurrentUser());
				entity.setCancelTime(new Date());
				entity.setUpdateTime(new Date());
				repository.save(entity);
				// 修改明细表状态
				List<CapitalPlanDetail> details = detailService.queryByCapitalId(id);
				for (CapitalPlanDetail detail : details) {
					detail.setRecordStatus(recordStatus);
					detail.setCancelor(SecurityUtil.getCurrentUser());
					detail.setCancelTime(new Date());
					detail.setUpdateTime(new Date());
					detailService.save(detail);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("资金计划取消出错!");
		}
		return buildSuccessRequestResult();
	}
	
	/**
	 * 修改为完成状态
	 * 
	 * @param id
	 *            主键
	 * @param recordStatus
	 *            完成状态标识
	 * @param updateTime
	 *            修改时间
	 * @return
	 */
	public RequestResult complete(String id, int recordStatus, String updateTime) {
		try {
			CapitalPlan entity = repository.findOne(id);
			if (entity == null) {
				return buildFailRequestResult("该记录已被删除,请刷新页面");
			}
			if (entity.getUpdateTime().compareTo(DateUtils.getDateFromString(updateTime)) != 0) {
				return buildFailRequestResult("数据已被其他用户修改，请刷新再试");
			}
			entity.setRecordStatus(recordStatus);
			entity.setComplete(SecurityUtil.getCurrentUser());
			entity.setCompleteDate(new Date());
			entity.setUpdateTime(new Date());
			repository.save(entity);
			// 修改明细表状态
			List<CapitalPlanDetail> details = detailService.queryByCapitalId(id);
			for (CapitalPlanDetail detail : details) {
				detailService.detailComplete(detail.getId(), recordStatus,
						DateUtils.getStringByFormat(detail.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("资金计划完成出错!");
		}
		return buildSuccessRequestResult();
	}
}
