package cn.fooltech.fool_ops.domain.warehouse.service.bill;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.capital.entity.CapitalPlan;
import cn.fooltech.fool_ops.domain.capital.entity.CapitalPlanDetail;
import cn.fooltech.fool_ops.domain.capital.service.CapitalPlanDetailService;
import cn.fooltech.fool_ops.domain.capital.service.CapitalPlanService;
import cn.fooltech.fool_ops.domain.warehouse.builder.BaseWarehouseWebServiceBuilder;
import cn.fooltech.fool_ops.domain.warehouse.entity.BillRelation;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCode;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.domain.warehouse.vo.StockLockingVo;
import cn.fooltech.fool_ops.domain.warehouse.vo.WarehouseBillVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;


/**
 * <p>采购订单网页服务类</p>
 * @author rqh
 * @version 1.0
 * @date 2015年9月29日
 */
@Service
public class PurchaseBillWebService extends BaseWarehouseWebServiceBuilder{
	
	
	@Autowired
	private CapitalPlanService capitalPlanService;

	@Autowired
	private CapitalPlanDetailService detailService;
	@Override
	@Value("#{T(cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCode).cgdd}")
	public void setBuilderCode(WarehouseBuilderCode builderCode) {
		super.setBuilderCode(builderCode);
	}

	@Override
	public RequestResult save(WarehouseBillVo vo) {
		RequestResult result = super.save(vo);
		if(result.getReturnCode() == RequestResult.RETURN_SUCCESS){
			String billId = result.getData().toString();
			int refType = WarehouseBuilderCodeHelper.getBillType(builderCode);
			super.insertBillRelation(billId, vo.getRelationId(), vo.getBillType(), refType);
		}
		return result;
	}
	
	
	@Override
	public RequestResult delete(String id) {
		WarehouseBill entity = billRepo.findOne(id);
		//会计期间
		RequestResult periodCheck = canOperateByPeriod(entity.getBillDate());
		if(periodCheck.getReturnCode() == RequestResult.RETURN_FAILURE){
			return periodCheck;
		}
		
		RequestResult result = new RequestResult();
		if(entity.getRecordStatus() == WarehouseBill.STATUS_AUDITED){
			result.setMessage("无效操作，单据处于已审核状态!");
			result.setReturnCode(RequestResult.RETURN_FAILURE);
		}
		else if(relationService.isAssociated(id)){
			result.setMessage("无效操作，该单据已被关联!");
			result.setReturnCode(RequestResult.RETURN_FAILURE);
		}
		else{
			billDetailService.deleteByBill(id);
			billRepo.delete(id);
			relationService.deleteRelation(id);
		}
		return result;
	}

	@Override
	public RequestResult passAudit(String id, StockLockingVo vo) {
		WarehouseBill bill = billRepo.findOne(id);
		//会计期间
		RequestResult periodCheck = canOperateByPeriod(bill.getBillDate());
		if(periodCheck.getReturnCode() == RequestResult.RETURN_FAILURE){
			return periodCheck;
		}
		
		//根据权限校验货品价格
		RequestResult verifyResult = priceVerifyService.verify(SecurityUtil.getCurrentUserId(), bill);
		if(verifyResult.getReturnCode() == RequestResult.RETURN_FAILURE){
			return verifyResult;
		}
		
		RequestResult result = new RequestResult();
		if(bill.getRecordStatus() != WarehouseBill.STATUS_UNAUDITED){
			result.setMessage("无效操作，非待审核状态!");
			result.setReturnCode(RequestResult.RETURN_FAILURE);
		}
		else{
			bill.setAuditor(SecurityUtil.getCurrentUser());
			bill.setAuditTime(Calendar.getInstance().getTime());
			bill.setRecordStatus(WarehouseBill.STATUS_AUDITED);
			billRepo.save(bill);
			
			//更新仓库单据关联
			relationService.updateStatus(id, BillRelation.AUTH);
		}
		return result;
	}

	@Override
	public RequestResult cancel(String id) {
		RequestResult result = new RequestResult();
		try {
			WarehouseBill bill = billRepo.findOne(id);
			//会计期间
			RequestResult periodCheck = canOperateByPeriod(bill.getBillDate());
			if(periodCheck.getReturnCode() == RequestResult.RETURN_FAILURE){
				return periodCheck;
			}
			
			if(bill.getRecordStatus() == WarehouseBill.STATUS_CANCELED){
				result.setMessage("无效操作，单据已被取消!");
				result.setReturnCode(RequestResult.RETURN_FAILURE);
			}
			else if(relationService.isAssociated(id)){
				result.setMessage("无效操作，该单据已被关联!");
				result.setReturnCode(RequestResult.RETURN_FAILURE);
			}
			else{
				bill.setCancelTime(new Date());
				bill.setCancelor(SecurityUtil.getCurrentUser());
				bill.setRecordStatus(WarehouseBill.STATUS_CANCELED);
				billRepo.save(bill);
			}
			CapitalPlan capitalPlan = capitalPlanService.queryByRelation(id);
			if(capitalPlan!=null){
				capitalPlan.setRecordStatus(CapitalPlan.STATUS_CANCEL);
				capitalPlan.setCancelor(SecurityUtil.getCurrentUser());
				capitalPlan.setCancelTime(new Date());
				capitalPlanService.save(capitalPlan);
				// 修改明细表状态
				List<CapitalPlanDetail> capitalPlanDetails = detailService.queryByCapitalId(capitalPlan.getId());
				for (CapitalPlanDetail detail : capitalPlanDetails) {
					detail.setRecordStatus(CapitalPlan.STATUS_CANCEL);
					detail.setCancelor(SecurityUtil.getCurrentUser());
					detail.setCancelTime(new Date());
					detailService.save(detail);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailRequestResult("采购订单作废出错!");
		}
		return result;
	}
	
}
