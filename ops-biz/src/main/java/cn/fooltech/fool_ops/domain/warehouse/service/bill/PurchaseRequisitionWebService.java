package cn.fooltech.fool_ops.domain.warehouse.service.bill;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.base.Splitter;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.warehouse.builder.BaseWarehouseWebServiceBuilder;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCode;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.domain.warehouse.vo.StockLockingVo;
import cn.fooltech.fool_ops.domain.warehouse.vo.WarehouseBillVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;

/**
 * <p>采购申请单网页服务类</p>
 * @author lgk
 * @date 2016年3月30日下午03:01:32
 * @version V1.0
 */
@Service("ops.PurchaseRequisitionWebService")
public class PurchaseRequisitionWebService extends BaseWarehouseWebServiceBuilder{
	
	@Override
	@Value("#{T(cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCode).cgsqd}")
	public void setBuilderCode(WarehouseBuilderCode builderCode) {
		super.setBuilderCode(builderCode);
	}
	
	/**
	 * 采购订单生成
	 * @param relationIds
	 * @return
	 */
	
	public WarehouseBillVo generats(String relationIds) {
        Splitter splitter = Splitter.on(",").trimResults().omitEmptyStrings();
		List<String> relationList = splitter.splitToList(relationIds);
		WarehouseBillVo billVo = new WarehouseBillVo();
		
		Date endDate = null;
		StringBuilder rIds = new StringBuilder();
		StringBuilder rNames = new StringBuilder();
		//整合货品信息
		for (String id : relationList) {
			WarehouseBill bill =  billRepo.findOne(id);
			if(endDate==null){
				endDate = bill.getEndDate();
			}else{
				endDate = endDate.compareTo(bill.getEndDate())<0?endDate:bill.getEndDate();
			}
			
			rIds.append(bill.getFid()).append(",");
			rNames.append(bill.getCode()).append(",");
		}
		billVo.setBillType(WarehouseBuilderCodeHelper.cgdd);
		billVo.setEndDate(endDate);
		
		billVo.setApplyIds(rIds.toString());
		billVo.setApplyCodes(rNames.toString());
		billVo.setRelationId(rIds.toString());
		billVo.setRelationName(rNames.toString());
		return billVo;
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
		}
		return periodCheck;
	}
	
	@Override
	public RequestResult save(WarehouseBillVo vo) {
		RequestResult parentResult = super.save(vo);
		if(parentResult.getReturnCode()==RequestResult.RETURN_SUCCESS && 
				StringUtils.isNotBlank(vo.getRelationId())){
			
			String billId = parentResult.getData().toString();
			int refType = WarehouseBuilderCodeHelper.getBillType(builderCode);
			super.insertBillRelation(billId, vo.getRelationId(), vo.getBillType(), refType);
		}
		return parentResult;
	}
	
}
