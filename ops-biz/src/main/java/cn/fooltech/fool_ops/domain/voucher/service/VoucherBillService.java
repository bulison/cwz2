package cn.fooltech.fool_ops.domain.voucher.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import cn.fooltech.fool_ops.domain.base.service.AbstractBaseService;
import cn.fooltech.fool_ops.domain.voucher.entity.Voucher;
import cn.fooltech.fool_ops.domain.voucher.entity.VoucherBill;
import cn.fooltech.fool_ops.domain.voucher.repository.VoucherBillRepository;

/**
 * <p>凭证、单据关联服务类</p>
 * @author rqh
 * @version 1.0
 * @date 2015年12月2日
 */
@Service
public class VoucherBillService extends AbstractBaseService<VoucherBill, String>{

	@Autowired
	private VoucherBillRepository voucherBillRepo;
	
	/**
	 * 获取单据关联的凭证
	 * @param billId 单据ID
	 * @param billType 单据类型
	 * @param fiscalAccountId 财务账套ID
	 * @return
	 */
	public Voucher getVoucher(String billId, Integer billType, String fiscalAccountId){
		Assert.isTrue(StringUtils.isNotBlank(fiscalAccountId));
		VoucherBill data = voucherBillRepo.getRecord(billId, billType, fiscalAccountId);
		if(data!=null){
			return data.getVoucher();
		}
		return null;
	}
	
	/**
	 * 获取单据关联的凭证的记录数目
	 * @param billIds 单据ID
	 * @param billType 单据类型
	 * @param fiscalAccountId 财务账套ID
	 * @return
	 */
	public long countVoucher(List<String> billIds, Integer billType, String fiscalAccountId){
		return voucherBillRepo.countVoucher(billIds, billType, fiscalAccountId);
	}

	/**
	 * 获取单据关联的凭证的记录数目
	 * @param billId 单据ID
	 * @return
	 */
	public long countVoucher(String billId){
		Long count = voucherBillRepo.countVoucher(billId);
		if(count==null)count = 0L;
		return count;
	}
		
	/**
	 * 获取关联记录
	 * @param billId 单据ID
	 * @param billType 单据类型
	 * @param fiscalAccountId 财务账套ID
	 * @return
	 */
	public VoucherBill getRecord(String billId, Integer billType, String fiscalAccountId){
		return voucherBillRepo.getRecord(billId, billType, fiscalAccountId);
	}
	
	/**
	 * 删除关联记录
	 * @param voucherId 凭证ID
	 */
	public void deleteByVoucherId(String voucherId){
		List<VoucherBill> list = voucherBillRepo.findByVoucherId(voucherId);
		for(VoucherBill entity : list){
			delete(entity);
		}
	}
	
	@Override
	public CrudRepository<VoucherBill, String> getRepository() {
		return voucherBillRepo;
	}
	
}
