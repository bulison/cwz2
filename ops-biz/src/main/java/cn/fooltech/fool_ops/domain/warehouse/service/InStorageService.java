package cn.fooltech.fool_ops.domain.warehouse.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import cn.fooltech.fool_ops.domain.base.service.AbstractBaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.Goods;
import cn.fooltech.fool_ops.domain.basedata.entity.GoodsSpec;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.warehouse.entity.InStorage;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBillDetail;
import cn.fooltech.fool_ops.domain.warehouse.repository.InStorageRepository;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.utils.NumberUtil;

@Service
public class InStorageService extends AbstractBaseService<InStorage, String>{

	private Logger logger = LoggerFactory.getLogger(InStorageService.class);

	@Autowired
	private InStorageRepository inStorageRepo;;
	
	/**
	 * 仓库单据明细服务类
	 */
	@Autowired
	private WarehouseBillDetailService detailService;
	
	
	/**
	 * 转换得到入库记录
	 * @param billDetail 仓库单据明细
	 * @return
	 */
	private InStorage convertInStorage(WarehouseBillDetail billDetail){
		WarehouseBill bill = billDetail.getBill();
		BigDecimal accountQuentity = billDetail.getAccountQuentity(); //记账数量
		BigDecimal accountUintPrice = billDetail.getAccountUintPrice(); //记账单价
		Assert.notNull(accountQuentity, "记账数量不能为空!");
		Assert.notNull(accountUintPrice, "记账单价不能为空!");
		BigDecimal accountAmount = detailService.calculateAccountAmount(accountQuentity, accountUintPrice); //记账金额
		
		InStorage inStorage = new InStorage();
		inStorage.setBillType(bill.getBillType());
		inStorage.setBillDetail(billDetail);
		inStorage.setGoods(billDetail.getGoods());
		inStorage.setGoodsSpec(billDetail.getGoodsSpec());
		inStorage.setAccountUint(billDetail.getAccountUint());
		if(bill.getBillType()==WarehouseBuilderCodeHelper.xsth||bill.getBillType()==WarehouseBuilderCodeHelper.sctl){
			inStorage.setAccountUintPrice(NumberUtil.divide(billDetail.getCostPrice(), billDetail.getScale(), 8));
			inStorage.setAccountAmount(NumberUtil.scale(NumberUtil.multiply(billDetail.getCostPrice(), billDetail.getQuentity()), 2));
		}else{
			inStorage.setAccountUintPrice(billDetail.getAccountUintPrice());
			inStorage.setAccountAmount(accountAmount);
		}
		inStorage.setAccountQuentity(billDetail.getAccountQuentity());
		inStorage.setInDate(bill.getBillDate());
		inStorage.setTotalOut(new BigDecimal(0));
		inStorage.setCreateTime(bill.getCreateTime());
		inStorage.setUpdateTime(new Date());
		inStorage.setCreator(bill.getCreator());
		inStorage.setOrg(billDetail.getOrg());
		
		inStorage.setFiscalAccount(bill.getFiscalAccount());
		return inStorage;
	}
	
	/**
	 * 添加入库记录
	 * @param billDetails 仓库单据明细
	 */
	public void addInStorage(List<WarehouseBillDetail> billDetails){
		if(CollectionUtils.isNotEmpty(billDetails)){
			for(WarehouseBillDetail billDetail : billDetails){
				addInStorage(billDetail);
			}
		}
	}
	
	/**
	 * 添加入库记录
	 * @param billDetail 仓库单据明细
	 */
	public void addInStorage(WarehouseBillDetail billDetail){
		//根据货品的记账标识进行过滤
		Goods goods = billDetail.getGoods();
		if(goods.getAccountFlag() == Goods.ACCOUNT_FLAG_YES){
			InStorage inStorage = convertInStorage(billDetail);
			inStorageRepo.save(inStorage);
		}
		
	}
	
	/**
	 * 获取货品的入库记录<br>
	 * 且记录中的记账数量减累计出库数量大于零<br>
	 * @param goods 货品
	 * @param goodsSpec 货品属性
	 * @param fiscalAccountId 账套ID
	 * @return
	 */
	public List<InStorage> getByGoods(Goods goods, GoodsSpec goodsSpec, String fiscalAccountId){
		return inStorageRepo.findByGoods(goods, goodsSpec, fiscalAccountId);
	}
	
	/**
	 * 删除入库记录
	 * @param billDetail 仓库单据明细
	 */
	public void deleteByBillDetail(WarehouseBillDetail billDetail){
		List<InStorage> datas = inStorageRepo.findByBillDetailId(billDetail.getFid());
		for(InStorage record : datas){
			inStorageRepo.delete(record);
		}
	}
	
	/**
	 * 获取入库记录
	 * @param billDetailId 仓库单据明细ID
	 * @return
	 */
	public InStorage findTopByDeatilId(String billDetailId){
		return inStorageRepo.findTopByBillDetailId(billDetailId);
	}
	
	/**
	 * 检查库存是否足够
	 * @param billDetails
	 * @return 库存不足的货品ID，货品属性ID集合
	 */
	public List<Object[]> checkStock(List<WarehouseBillDetail> billDetails){
		if(CollectionUtils.isEmpty(billDetails)) {
			return Collections.emptyList();
		}
		
		WarehouseBill bill = billDetails.get(0).getBill();
		Organization org = bill.getOrg();
		FiscalAccount fiscalAccount = bill.getFiscalAccount();
		if(bill.getBillType() == WarehouseBuilderCodeHelper.pdd)
		{
			return inStorageRepo.checkStockByPdd(org.getFid(), fiscalAccount.getFid(), bill.getFid());
		}
		else{
			return inStorageRepo.checkStock(org.getFid(), fiscalAccount.getFid(), bill.getFid());
		}
	}
	
	/**
	 * 获取入库记录
	 * @param billDetailId 单据明细ID
	 * @param goodsId 货品ID
	 * @param goodsSpecId 货品属性ID
	 * @return
	 */
	public InStorage getRecord(String billDetailId, String goodsId, String goodsSpecId){
		return inStorageRepo.findTopBy(billDetailId, goodsId, goodsSpecId);
	}

	@Override
	public CrudRepository<InStorage, String> getRepository() {
		return inStorageRepo;
	}
	
	/**
	 * 获取入库记录
	 * @param billDetailId 仓库单据明细ID
	 * @return
	 */
	public InStorage getByDeatil(String billDetailId){
		InStorage byDeatil = inStorageRepo.getByDeatil(billDetailId);
		return byDeatil;
	}
}
