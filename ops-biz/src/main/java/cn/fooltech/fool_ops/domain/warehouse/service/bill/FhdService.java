package cn.fooltech.fool_ops.domain.warehouse.service.bill;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.transport.entity.TransportBilldetail;
import cn.fooltech.fool_ops.domain.transport.service.TransportBilldetailService;
import cn.fooltech.fool_ops.domain.transport.vo.TransportBilldetailVo;
import cn.fooltech.fool_ops.domain.warehouse.builder.BaseWarehouseWebServiceBuilder;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBillDetail;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCode;
import cn.fooltech.fool_ops.domain.warehouse.vo.StockLockingVo;
import cn.fooltech.fool_ops.domain.warehouse.vo.WarehouseBillDetailVo;
import cn.fooltech.fool_ops.domain.warehouse.vo.WarehouseBillVo;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * <p>发货单服务类</p>
 * @author rqh
 * @version 1.0
 * @date 2015年10月6日
 */
@Service("ops.FhdService")
public class FhdService extends BaseWarehouseWebServiceBuilder{

	@Autowired
	private TransportBilldetailService transportBilldetailService;

	@Override
	@Value("#{T(cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCode).fhd}")
	public void setBuilderCode(WarehouseBuilderCode builderCode) {
		super.setBuilderCode(builderCode);
	}

	@Override
	public RequestResult save(WarehouseBillVo vo) {
		RequestResult result = super.save(vo);
		if(!result.isSuccess())return result;
		String billId = result.getData().toString();
		List<WarehouseBillDetailVo> details = vo.getDetailList();

		BigDecimal sumDeductionAmount = sumDeductionAmount(details);
		WarehouseBill bill = billRepo.findOne(billId);
		bill.setDeductionAmount(sumDeductionAmount);
		billRepo.save(bill);

		saveTransportDetails(bill, vo);

		return result;
	}

	/**
	 * 汇总扣费金额
	 * @param vos
	 * @return
	 */
	private BigDecimal sumDeductionAmount(List<WarehouseBillDetailVo> vos){
		BigDecimal total = BigDecimal.ZERO;
		for(WarehouseBillDetailVo vo:vos){
			total = NumberUtil.add(total, vo.getDeductionAmount());
		}
		return total;
	}

	/**
	 * 保存发货单
	 */
	@Transactional
	private void saveTransportDetails(WarehouseBill bill, WarehouseBillVo billvo){
		List<TransportBilldetailVo> vos = billvo.getTransportBilldetailList();

		List<TransportBilldetail> details = transportBilldetailService.getTransportBillDetails(bill.getFid());
		transportBilldetailService.deleteAll(details);

		for(TransportBilldetailVo vo:vos){
			transportBilldetailService.save(vo, bill);
		}
	}
}
