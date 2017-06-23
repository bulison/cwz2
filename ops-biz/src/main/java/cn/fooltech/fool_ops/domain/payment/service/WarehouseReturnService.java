package cn.fooltech.fool_ops.domain.payment.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import cn.fooltech.fool_ops.domain.base.service.AbstractBaseService;
import cn.fooltech.fool_ops.domain.payment.entity.WarehouseReturn;
import cn.fooltech.fool_ops.domain.payment.repository.WarehouseReturnRepository;
/**
 * <p>返利单服务类</p>
 * @author lgk
 * @date 2016年4月8日下午02:41:43
 * @version V1.0
 */
@Service
public class WarehouseReturnService extends AbstractBaseService<WarehouseReturn, String>{

	@Autowired
	private WarehouseReturnRepository warehouseReturnRepo;
	
	/**
	 * 根据单据ID删除
	 * @param id
	 */
	public void deleteByBillId(String id) {
		List<WarehouseReturn> list = warehouseReturnRepo.findBillIdsByPaymentId(id);
		for (WarehouseReturn warehouseReturn : list) {
			warehouseReturnRepo.delete(warehouseReturn);
		}
	}

	@Override
	public CrudRepository<WarehouseReturn, String> getRepository() {
		return warehouseReturnRepo;
	}
}
