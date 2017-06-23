package cn.fooltech.fool_ops.domain.capital.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.CustomerSupplierView;
import cn.fooltech.fool_ops.domain.basedata.service.CustomerSupplierService;
import cn.fooltech.fool_ops.domain.capital.entity.CapitalPlan;
import cn.fooltech.fool_ops.domain.capital.entity.CapitalPlanBill;
import cn.fooltech.fool_ops.domain.capital.entity.CapitalPlanDetail;
import cn.fooltech.fool_ops.domain.capital.repository.CapitalPlanBillRepository;
import cn.fooltech.fool_ops.domain.capital.vo.CapitalPlanBillVo;
import cn.fooltech.fool_ops.domain.cost.entity.CostBill;
import cn.fooltech.fool_ops.domain.cost.repository.CostBillRepository;
import cn.fooltech.fool_ops.domain.payment.entity.PaymentBill;
import cn.fooltech.fool_ops.domain.payment.repository.PaymentBillRepository;
import cn.fooltech.fool_ops.domain.payment.service.PaymentCheckService;
import cn.fooltech.fool_ops.domain.payment.service.PaymentCheckService.BillType;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
import cn.fooltech.fool_ops.domain.warehouse.repository.WarehouseBillRepository;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.VoFactory;

/**
 * 
 * @Description: 资金计划关联单据 服务类
 * @author cwz
 * @date 2017年3月9日 下午2:11:03
 */
@Service
public class CapitalPlanBillService extends BaseService<CapitalPlanBill, CapitalPlanBillVo, String> {

	@Autowired
	private CapitalPlanBillRepository repository;
	
	
	@Autowired
	private CapitalPlanDetailService capitalPlanDetailService;
	
	/**
	 * 客户供应商服务类
	 */
	@Autowired
	private CustomerSupplierService csService;
	
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
	private CapitalPlanService capitalPlanService;

	/**
	 * 实体转换VO
	 * 
	 * @param entity
	 * @return
	 */
	@Override
	public CapitalPlanBillVo getVo(CapitalPlanBill entity) {
		CapitalPlanBillVo vo = VoFactory.createValue(CapitalPlanBillVo.class, entity);
		CustomerSupplierView csv = entity.getCsv();
		if(csv!=null){
			vo.setCsvId(csv.getFid());
			vo.setCsvName(csv.getName());
		}
		Integer relationSign = entity.getRelationSign();
		String relationId = entity.getRelationId();
		if(!Strings.isNullOrEmpty(relationId)&&relationSign!=null){
			BillType type = payCheckService.checkBillType(relationSign);
			if(type==BillType.Warehouse){//仓储单据的表
				WarehouseBill bill = billRepo.findOne(relationId);
				if(bill!=null){
					vo.setBillDate(bill.getBillDate());
				}
				
			}else if(type==BillType.Pay){//收付款单的表
				PaymentBill paymentBill = paymentRepo.findOne(relationId);
				if(paymentBill!=null){
					vo.setBillDate(paymentBill.getBillDate());
				}
				
			}else if(type==BillType.Cost){//费用单的表
				CostBill costBill2 = costBillRepo.findOne(relationId);
				if(costBill2!=null){
					vo.setBillDate(costBill2.getBillDate());
				}
			}
		}
		return vo;
	}

	@Override
	public CrudRepository<CapitalPlanBill, String> getRepository() {
		return repository;
	}

	/**
	 * 查找分页
	 * 
	 * @param vo
	 * @param paramater
	 * @return
	 */
	public Page<CapitalPlanBillVo> query(CapitalPlanBillVo vo, PageParamater paramater) {

		Sort sort = new Sort(Sort.Direction.DESC, "relationSign");
		PageRequest pageRequest = getPageRequest(paramater, sort);
		Page<CapitalPlanBill> page = repository.findPageBy(vo, pageRequest);
		return getPageVos(page, pageRequest);
	}

	/**
	 * 修改或新增
	 * 
	 * @param vo
	 * @return
	 */
	@Transactional
	public RequestResult save(CapitalPlanBillVo vo) {

		CapitalPlanBill entity = null;
		if (Strings.isNullOrEmpty(vo.getId())) {
			entity = new CapitalPlanBill();
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
			entity.setUpdateTime(new Date());
		}
		entity.setDetail(capitalPlanDetailService.findOne(vo.getDetailId()));
		if(StringUtils.isNotBlank(vo.getCsvId())){
			entity.setCsv(csService.get(vo.getCsvId()));
		}
		entity.setBindType(vo.getBindType());
		entity.setRelationSign(vo.getRelationSign());
		entity.setRelationId(vo.getRelationId());
		entity.setBillAmount(vo.getBillAmount());
		entity.setBindAmount(vo.getBindAmount());
		entity.setCode(vo.getCode());

		repository.save(entity);

		return buildSuccessRequestResult(getVo(entity));
	}
	

	/**
	 * 根据明细id查询关联单据
	 * @param detailId
	 * @return
	 */
	public List<CapitalPlanBill> queryByDetail(String detailId){
		List<CapitalPlanBill> list = repository.queryByDetail(detailId);
		return list;
		
	}
	/**
	 * 根据单据id查询关联单据
	 * @param relationId
	 * @return
	 */
	public List<CapitalPlanBill> queryByRelationId(String relationId){
		List<CapitalPlanBill> list = repository.queryByRelationId(relationId);
		return list;
	}
	/**
	 * 删除资金计划关联表
	 * @param fid
	 * @param bindType	类型 1-单据金额，2-收付款金额
	 * @return
	 */
	@Transactional
	public RequestResult delete(String fid,Integer bindType){
		try {
			CapitalPlanBill planBill = repository.findOne(fid);
			if(planBill==null){
				return buildFailRequestResult("数据已被删除，请刷新再试");
			}
			/*取消绑定操作：1.明细表单据金额-当前删除单据的绑定金额；2.删除当前绑定单据*/
			CapitalPlanDetail detail = planBill.getDetail();
			if(detail!=null){
				BigDecimal bindAmount = planBill.getBindAmount()==null?BigDecimal.ZERO:planBill.getBindAmount();//关联单据绑定金额
				BigDecimal billAmount = detail.getBillAmount()==null?BigDecimal.ZERO:detail.getBillAmount();//明细表单据金额
				BigDecimal paymentAmount = detail.getPaymentAmount()==null?BigDecimal.ZERO:detail.getPaymentAmount();//明细表收付款金额
				CapitalPlan capital = detail.getCapital();
				if(bindType==1){
					BigDecimal result = billAmount.subtract(bindAmount);
					detail.setBillAmount(result);
					if(capital!=null){
						BigDecimal billAmount2 = capital.getBillAmount();
						capital.setBillAmount(billAmount2.subtract(bindAmount));
					}
				}else{
					BigDecimal result = paymentAmount.subtract(bindAmount);
					detail.setPaymentAmount(result);
					if(capital!=null){
						BigDecimal paymentAmount2 = capital.getPaymentAmount();
						capital.setPaymentAmount(paymentAmount2.subtract(bindAmount));
					}
				}
				capitalPlanDetailService.save(detail);
//				CapitalPlan capital = detail.getCapital();
//				List<CapitalPlanDetail> details = capitalPlanDetailService.queryByCapitalId(capital.getId());
//				BigDecimal sumBillAmount = BigDecimal.ZERO;
//				BigDecimal sumPaymentAmount = BigDecimal.ZERO;
//				for (CapitalPlanDetail detail2 : details) {
//					BigDecimal amount = detail2.getBillAmount()==null?BigDecimal.ZERO:detail2.getBillAmount();
//					sumBillAmount = sumBillAmount.add(amount);
//					BigDecimal paymentAmount2 = detail2.getPaymentAmount()==null?BigDecimal.ZERO:detail2.getPaymentAmount();
//					sumPaymentAmount = sumBillAmount.add(paymentAmount2);
//				}
//				capital.setBillAmount(sumBillAmount);
//				capital.setPaymentAmount(sumPaymentAmount);
				capitalPlanService.save(capital);//更新主表单据金额和收付款金额
			}
			repository.delete(fid);
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("删除绑定单据出错!");
		}
		return buildSuccessRequestResult();
	}
}
