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
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCode;
import cn.fooltech.fool_ops.domain.warehouse.vo.StockLockingVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;


/**
 * <p>销售报价单网页服务类</p>
 * @author rqh
 * @version 1.0
 * @date 2015年10月6日
 */
@Service("ops.SalesQuoteWebService")
public class SalesQuoteWebService extends BaseWarehouseWebServiceBuilder{
	@Autowired
	private CapitalPlanService capitalPlanService;

	@Autowired
	private CapitalPlanDetailService detailService;
	@Override
	@Value("#{T(cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCode).xsbjd}")
	public void setBuilderCode(WarehouseBuilderCode builderCode) {
		super.setBuilderCode(builderCode);
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
			return buildFailRequestResult("销售报价作废出错!");
		}
		return result;
	}
	
}
