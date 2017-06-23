package cn.fooltech.fool_ops.web.warehouse;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.cost.service.CostBillService;
import cn.fooltech.fool_ops.domain.payment.service.PaymentBillService;
import cn.fooltech.fool_ops.domain.warehouse.service.WarehouseBillService;

@Controller
@RequestMapping("/billCheck")
public class BillCheckController {
	@Resource(name = "ops.WarehouseBillService")
	private WarehouseBillService warehouseBillService;
	@Autowired
	private PaymentBillService paymentBillService;
	@Autowired
	private CostBillService costBillService;
	@RequestMapping(value="/wareHouseBillCheck")
	@ResponseBody
	public RequestResult wareHouseBillCheck(String id,short check){
		return warehouseBillService.wareHouseBillCheck(id, check);
	}
	@RequestMapping(value="/paymentBillCheck")
	@ResponseBody
	public RequestResult paymentBillCheck(String id,short check){
		return paymentBillService.paymentBillCheck(id, check);
	}
	@RequestMapping(value="/costBillCheck")
	@ResponseBody
	public RequestResult costBillCheck(String id,short check){
		return costBillService.costBillCheck(id, check);
	}
	
}
