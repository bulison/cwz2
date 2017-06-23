package cn.fooltech.fool_ops.domain.warehouse.service.bill;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.transport.entity.TransportBilldetail;
import cn.fooltech.fool_ops.domain.transport.service.TransportBilldetailService;
import cn.fooltech.fool_ops.domain.transport.vo.TransportBilldetailVo;
import cn.fooltech.fool_ops.domain.warehouse.builder.BaseWarehouseWebServiceBuilder;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCode;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.domain.warehouse.vo.WarehouseBillDetailVo;
import cn.fooltech.fool_ops.domain.warehouse.vo.WarehouseBillVo;
import cn.fooltech.fool_ops.utils.NumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
 * <p>收货单服务类</p>
 * @author rqh
 * @version 1.0
 * @date 2015年10月6日
 */
@Service("ops.ShdService")
public class ShdService extends BaseWarehouseWebServiceBuilder{

	@Autowired
	private TransportBilldetailService transportBilldetailService;

	@Override
	@Value("#{T(cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCode).shd}")
	public void setBuilderCode(WarehouseBuilderCode builderCode) {
		super.setBuilderCode(builderCode);
	}

	@Override
	@Transactional
	public RequestResult save(WarehouseBillVo vo) {
		RequestResult result = super.save(vo);
		if(!result.isSuccess())return result;
		String billId = result.getData().toString();
		List<WarehouseBillDetailVo> details = vo.getDetailList();

		BigDecimal sumDeductionAmount = sumDeductionAmount(details);
		WarehouseBill bill = billRepo.findOne(billId);
		bill.setDeductionAmount(sumDeductionAmount);
		billRepo.save(bill);

		int refType = WarehouseBuilderCodeHelper.getBillType(builderCode);
		super.insertBillRelation(billId, vo.getRelationId(), vo.getBillType(), refType);

		saveTransportDetails(bill, vo);
		Map<String, Object> map = result.getDataExt();
		map.put("totalAmount", bill.getTotalAmount());
		map.put("freeAmount", bill.getFreeAmount());
		map.put("totalPayAmount",bill.getTotalPayAmount());
		map.put("deductionAmount",bill.getDeductionAmount());
		result.setDataExt(map);
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
		List<TransportBilldetail> details = transportBilldetailService.getTransportBillDetails(bill.getFid());
		transportBilldetailService.deleteAll(details);

		List<TransportBilldetailVo> vos = billvo.getTransportBilldetailList();
		for(TransportBilldetailVo vo:vos){
			transportBilldetailService.save(vo, bill);
		}
	}
}
