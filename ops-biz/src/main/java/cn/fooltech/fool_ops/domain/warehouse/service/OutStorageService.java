package cn.fooltech.fool_ops.domain.warehouse.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.base.Strings;

import cn.fooltech.fool_ops.domain.base.service.AbstractBaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.Goods;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.warehouse.entity.InStorage;
import cn.fooltech.fool_ops.domain.warehouse.entity.OutStorage;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBillDetail;
import cn.fooltech.fool_ops.domain.warehouse.repository.InStorageRepository;
import cn.fooltech.fool_ops.domain.warehouse.repository.OutStorageRepository;

@Service
public class OutStorageService extends AbstractBaseService<OutStorage, String>{

	private Logger logger = LoggerFactory.getLogger(OutStorageService.class);

	@Autowired
	private OutStorageRepository outStorageRepo;
	
	@Autowired
	private InStorageRepository inStorageRepo;
	
	@Override
	public CrudRepository<OutStorage, String> getRepository() {
		return outStorageRepo;
	}
	
	/**
	 * 获取入库的累计记账金额
	 * @param inStorgeId 入库记录ID
	 */
	private BigDecimal getTotalAmountIn(String inStorgeId){
		BigDecimal amountIn = outStorageRepo.getTotalAmountInByInStorageId(inStorgeId);
		if(amountIn==null)amountIn = BigDecimal.ZERO;
		return amountIn;
	}
	
	/**
	 * 添加出库记录
	 * @param billDetail 仓库单据明细
	 * @param returnResult 返回的出库记录
	 * @return
	 */
	public void addOutStorage(WarehouseBillDetail billDetail, List<OutStorage> returnResult){
		//根据货品的记账标识进行过滤
		Goods goods = billDetail.getGoods();
		if(goods.getAccountFlag() == Goods.ACCOUNT_FLAG_NO){
			return;
		}
		
		WarehouseBill bill = billDetail.getBill();
		FiscalAccount fiscalAccount = bill.getFiscalAccount(); //财务账套
		
		//入库记录
		List<InStorage> inList = inStorageRepo.findByGoods(billDetail.getGoods(), billDetail.getGoodsSpec(), fiscalAccount.getFid());
		Assert.isTrue(CollectionUtils.isNotEmpty(inList), "库存不足，出库操作失败!");
		
		BigDecimal q1 = billDetail.getAccountQuentity();
		if(q1.compareTo(BigDecimal.ZERO) == -1) q1 = q1.abs();
		
		BigDecimal q2 = BigDecimal.ZERO;
		BigDecimal amountIn = BigDecimal.ZERO; //入库记账金额
		for(int i=0; q1.compareTo(BigDecimal.ZERO) > 0 && i<inList.size(); i++){
			InStorage inStorage = inList.get(i);
			BigDecimal totalout = inStorage.getTotalOut(); //累计出库数量
			BigDecimal accountQuentity = inStorage.getAccountQuentity(); //记账数量
			BigDecimal validQuentity = accountQuentity.subtract(totalout); //有效库存数量
			if(q1.compareTo(validQuentity) > 0){
				q2 = validQuentity;
			}
			else{
				q2 = q1;
			}
			
			BigDecimal newTotalOut = inStorage.getTotalOut().add(q2);
			inStorage.setTotalOut(newTotalOut);
			inStorageRepo.save(inStorage);
			
			if(inStorage.getTotalOut() == inStorage.getAccountQuentity()){
				amountIn = inStorage.getAccountAmount().subtract(getTotalAmountIn(inStorage.getFid()));
			}
			else{
				amountIn = q2.multiply(inStorage.getAccountUintPrice());
			}
			
			//出库记录
			OutStorage outStorage = new OutStorage();
			outStorage.setBillType(bill.getBillType());
			outStorage.setInStorage(inStorage);
			outStorage.setBillDetailIn(inStorage.getBillDetail());
			outStorage.setBillDetailOut(billDetail);
			outStorage.setGoods(billDetail.getGoods());
			outStorage.setGoodsSpec(billDetail.getGoodsSpec());
			outStorage.setAccountUint(billDetail.getAccountUint());
			outStorage.setAccountUintPrice(billDetail.getAccountUintPrice());
			outStorage.setInPrice(inStorage.getAccountUintPrice());
			outStorage.setInDate(inStorage.getInDate());
			outStorage.setOrg(bill.getOrg());
			outStorage.setUpdateTime(new Date());
			outStorage.setOutDate(bill.getBillDate());
			outStorage.setCreateTime(bill.getCreateTime());
			outStorage.setCreator(bill.getCreator());
			outStorage.setAmountIn(amountIn);
			outStorage.setAccountQuentity(q2);
			//记账金额
			BigDecimal accountAmount = q2.multiply(billDetail.getAccountUintPrice());
			accountAmount = accountAmount.setScale(WarehouseBillDetailService.ACCOUNT_AMOUNT_SCALE, BigDecimal.ROUND_HALF_UP);
			outStorage.setAccountAmount(accountAmount);
			outStorage.setFiscalAccount(fiscalAccount);
			
			save(outStorage);
			returnResult.add(outStorage);
			q1 = q1.subtract(q2);
		}	
	}
	
	/**
	 * 统计某单据明细的出库记录数
	 * @param billDetailId
	 * @return
	 */
	public Long countByBillDetail(String billDetailId){
		Long count = outStorageRepo.countByBillDetailInId(billDetailId);
		if(count!=null)return count;
		return 0L;
	}
	
	/**
	 * 获取某仓库单据明细的出库记录
	 * @param billDetailId 仓库单据明细ID
	 * @return
	 */
	public List<OutStorage> getByBillDetail(String billDetailId){
		return outStorageRepo.findByBillDetailOutId(billDetailId);
	}

	/**
	 * 计算成本金额
	 * add by xjh
	 */
	public BigDecimal findOutStorageSum(String warehouseId, String billDetailId) {
		BigDecimal result = null;
		if(Strings.isNullOrEmpty(warehouseId)){
			result = outStorageRepo.getTotalAmountInByBillDetailOutId(billDetailId);
		}else{
			result = outStorageRepo.getTotalAmountInByBillDetailOutId(billDetailId, warehouseId);
		}
		if(result==null) return BigDecimal.ZERO;
		return result;
	}
	
	/**
	 * 计算成本金额
	 * add by xjh
	 */
	public BigDecimal findOutStorageSum(List<String> billDetailIds) {
		BigDecimal result = outStorageRepo.getTotalAmountInByBillDetailOutIds(billDetailIds);
		if(result==null) return BigDecimal.ZERO;
		return (BigDecimal) result;
	}
	
	/**
	 * 通过传入的List<OutStorage>计算成本金额
	 * add by xjh
	 */
	public BigDecimal findOutStorageSum(String warehouseId, String billDetailId, List<OutStorage> oStorages) {
		BigDecimal total = BigDecimal.ZERO;
		if(oStorages == null) return total;
		for(OutStorage out:oStorages){
			
			if(out.getBillDetailOut().getFid().equals(billDetailId)){
				if(StringUtils.isNotBlank(warehouseId)){
					if(warehouseId.equals(out.getBillDetailOut().getInWareHouse().getFid())){
						if(out.getAmountIn()!=null){
							total = total.add(out.getAmountIn());
						}
					}
				}else{
					if(out.getAmountIn()!=null){
						total = total.add(out.getAmountIn());
					}
				}
			}
		}
		return total;
	}
	
	/**
	 * 获取某张单据的出库记录
	 * @param billId 仓库单据ID
	 * @return
	 */
	public List<OutStorage> getByBillId(String billId){
		return outStorageRepo.findByBillId(billId);
	}
	
	/**
	 * 获取出库记录
	 * @param billDetailId 单据明细ID(入库)
	 * @param goodsId 货品ID
	 * @param goodsSpecId 货品属性ID
	 * @return
	 */
	public List<OutStorage> getRecordByDetailIn(String billDetailId, String goodsId, String goodsSpecId){
		Sort sort = new Sort(Direction.ASC, "createTime");
		return outStorageRepo.findByDetailIn(billDetailId, goodsId, goodsSpecId, sort);
	}
	
	/**
	 * 获取出库记录
	 * @param billDetailId 单据明细ID(入库)
	 * @return
	 */
	public List<OutStorage> getRecordByDetailIn(String billDetailId){
		return outStorageRepo.findByBillDetailInId(billDetailId);
	}

	/**
	 * 获取出库记录
	 * @param billDetailId 单据明细ID(出库)
	 * @param goodsId 货品ID
	 * @param goodsSpecId 货品属性ID
	 * @return
	 */
	public List<OutStorage> getRecordByDetailOut(String billDetailId, String goodsId, String goodsSpecId){
		Sort sort = new Sort(Direction.ASC, "createTime");
		return outStorageRepo.findByDetailOut(billDetailId, goodsId, goodsSpecId, sort);
	}
	
}
