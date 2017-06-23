package cn.fooltech.fool_ops.domain.warehouse.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.fooltech.fool_ops.domain.warehouse.entity.PeriodAmount;
import cn.fooltech.fool_ops.domain.warehouse.entity.PeriodStockAmount;
import cn.fooltech.fool_ops.domain.warehouse.strategy.CalMethod;
import cn.fooltech.fool_ops.domain.warehouse.strategy.CalMethodProcessor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.google.common.base.Strings;

import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.Goods;
import cn.fooltech.fool_ops.domain.basedata.entity.GoodsSpec;
import cn.fooltech.fool_ops.domain.warehouse.entity.StockStore;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBillDetail;
import cn.fooltech.fool_ops.domain.warehouse.repository.StockStoreRepository;
import cn.fooltech.fool_ops.domain.warehouse.repository.WarehouseBillRepository;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import cn.fooltech.fool_ops.domain.warehouse.vo.StockStoreVo;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;

@Service
public class StockStoreService {

	/**
	 * 入库
	 */
	private static final int FLAG_IN = 0;
	
	/**
	 * 出库
	 */
	private static final int FLAG_OUT = 1;
	
	private Logger logger = LoggerFactory.getLogger(StockStoreService.class);
	
	/**
	 * 即时分仓库存DAO类
	 */
	@Autowired
	private StockStoreRepository stockStoreRepo;
	
	/**
	 * 仓库单据服务类
	 */
	@Autowired
	private WarehouseBillRepository billRepo;

	//=================================================
	/**
	 * 根据策略处理即时分仓入仓字段
	 */
	@Transactional
	public void processIn(List<CalMethod> methods, WarehouseBill bill, WarehouseBillDetail detail, PeriodAmount total) {

		Goods goods = detail.getGoods();
		GoodsSpec goodsSpec = detail.getGoodsSpec();
		AuxiliaryAttr warehouse = detail.getInWareHouse();
		int billType = bill.getBillType();

		//货品不计算库存
		if (goods.getAccountFlag() == Goods.ACCOUNT_FLAG_NO) {
			return;
		}

		//即时分仓库存记录
		StockStore stockStore = null;
		if (goodsSpec == null) {
			stockStore = getStock(SecurityUtil.getFiscalAccountId(), warehouse.getFid(), goods.getFid());
		} else {
			stockStore = getStock(SecurityUtil.getFiscalAccountId(), warehouse.getFid(), goods.getFid(), goodsSpec.getFid());
		}

		if (stockStore == null) {
			stockStore = initStockStore(billType, detail, warehouse);
		}

		stockStore = CalMethodProcessor.process(stockStore, detail, total, methods);

		stockStoreRepo.save(stockStore);
	}
	/**
	 * 判断是否能作废
	 * @param methods
	 * @param bill
	 * @param detail
	 * @param total
	 */
	@Transactional
	public boolean JudgmentCancle( WarehouseBill bill, WarehouseBillDetail detail) {

		Goods goods = detail.getGoods();
		GoodsSpec goodsSpec = detail.getGoodsSpec();
		AuxiliaryAttr warehouse = detail.getInWareHouse();
		//即时分仓库存记录
		StockStore stockStore = null;
		if (goodsSpec == null) {
			stockStore = getStock(SecurityUtil.getFiscalAccountId(), warehouse.getFid(), goods.getFid());
		} else {
			stockStore = getStock(SecurityUtil.getFiscalAccountId(), warehouse.getFid(), goods.getFid(), goodsSpec.getFid());
		}
		if(stockStore==null){
			return false;
		}
		if(stockStore.getAccountQuentity().subtract(detail.getAccountQuentity()).compareTo(BigDecimal.ZERO)==-1){
			return false;
		}else{
			return true;
		}
	}
	/**
	 * 根据策略处理即时分仓入仓字段
	 */
	@Transactional
	public void processOut(List<CalMethod> methods, WarehouseBill bill, WarehouseBillDetail detail, PeriodAmount total) {

		Goods goods = detail.getGoods();
		GoodsSpec goodsSpec = detail.getGoodsSpec();
		AuxiliaryAttr warehouse = detail.getOutWareHouse();
		int billType = bill.getBillType();

		//货品不计算库存
		if (goods.getAccountFlag() == Goods.ACCOUNT_FLAG_NO) {
			return;
		}

		//即时分仓库存记录
		StockStore stockStore = null;
		if (goodsSpec == null) {
			stockStore = getStock(SecurityUtil.getFiscalAccountId(), warehouse.getFid(), goods.getFid());
		} else {
			stockStore = getStock(SecurityUtil.getFiscalAccountId(), warehouse.getFid(), goods.getFid(), goodsSpec.getFid());
		}

		if (stockStore == null) {
			stockStore = initStockStore(billType, detail, warehouse);
		}

		stockStore = CalMethodProcessor.process(stockStore, detail, total, methods);

		stockStoreRepo.save(stockStore);
	}

	/**
	 * 初始化库存记录
	 * @param billType 单据类型
	 * @param billDetail 单据明细
	 * @param warehouse 仓库
	 */
	private StockStore initStockStore(int billType, WarehouseBillDetail billDetail, AuxiliaryAttr warehouse){
		StockStore entity = new StockStore();
		entity.setOrg(billDetail.getOrg());
		entity.setWarehouse(warehouse);
		entity.setGoods(billDetail.getGoods());
		entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
		entity.setGoodsSpec(billDetail.getGoodsSpec());
		entity.setAccountUnit(billDetail.getAccountUint());
		return entity;
	}


	//=================================================
	
	/**
	 * 查询即时分仓库存
	 * @param vo
	 * @return
	 */
	public List<StockStoreVo> query(StockStoreVo vo){
		String warehouseId = vo.getWarehouseId();
		String goodsId = vo.getGoodsId();
		String specId = vo.getGoodsSpecId();
		String accId = SecurityUtil.getFiscalAccountId();
		List<StockStore> entities = stockStoreRepo.findBy(accId, warehouseId, goodsId, specId);
		return getVos(entities);
	}
	
	/**
	 * 多个实体转vo
	 * @param entities
	 * @return
	 */
	public List<StockStoreVo> getVos(List<StockStore> entities){
		List<StockStoreVo> vos = new ArrayList<StockStoreVo>();
		for(StockStore entity : entities){
			vos.add(getVo(entity));
		}
		return vos;
	}
	
	/**
	 * 单个实体转vo
	 * @param entity
	 * @return
	 */
	public StockStoreVo getVo(StockStore entity){
		StockStoreVo vo = new StockStoreVo();
		//仓库
		AuxiliaryAttr warehouse = entity.getWarehouse();
		if(warehouse != null){
			vo.setWarehouseId(warehouse.getFid());
			vo.setWarehouseName(warehouse.getName());
		}
		//货品
		Goods goods = entity.getGoods();
		if(goods != null){
			vo.setGoodsId(goods.getFid());
			vo.setGoodsCode(goods.getCode());
			vo.setGoodsName(goods.getName());
		}
		//货品属性
		GoodsSpec goodsSpec = entity.getGoodsSpec();
		if(goodsSpec != null){
			vo.setGoodsSpecId(goodsSpec.getFid());
			vo.setGoodsSpecName(goodsSpec.getName());
		}
		vo.setAccountQuentity(NumberUtil.bigDecimalToStr(entity.getAccountQuentity()));
		vo.setCheckoutQuentity(NumberUtil.bigDecimalToStr(entity.getCheckoutQuentity()));
		return vo;
	}
	
	/**
	 * 正向处理即时分仓库存
	 * @param bill
	 */
	public void handleStock(WarehouseBill bill){
		switch(bill.getBillType()){
			case WarehouseBuilderCodeHelper.qckc:
			case WarehouseBuilderCodeHelper.cgrk:
			case WarehouseBuilderCodeHelper.sctl:
			case WarehouseBuilderCodeHelper.cprk:
			case WarehouseBuilderCodeHelper.xsth:{
				//入库操作：期初库存、采购入库、生产退料、成品入库、销售退货
				handle(bill, FLAG_IN);
				break;
			}
			
			case WarehouseBuilderCodeHelper.cgth:
			case WarehouseBuilderCodeHelper.scll:
			case WarehouseBuilderCodeHelper.cptk:
			case WarehouseBuilderCodeHelper.xsch:
			case WarehouseBuilderCodeHelper.bsd:{
				//出库操作：采购退货、生产领料、成品退库、销售出库、报损单
				handle(bill, FLAG_OUT);
				break;
			}
			
			case WarehouseBuilderCodeHelper.pdd:{
				//盘点单(+-)
				List<WarehouseBillDetail> details = bill.getDetails();
				for(WarehouseBillDetail detail : details){
					if(detail.getAccountQuentity().compareTo(BigDecimal.ZERO) > 0){
						handle(detail, bill.getInWareHouse(), FLAG_IN);
					}
					else{
						handle(detail, bill.getInWareHouse(), FLAG_OUT);
					}
				}
				break;
			}
			
			case WarehouseBuilderCodeHelper.dcd:{
				//调仓单
				List<WarehouseBillDetail> details = bill.getDetails();
				for(WarehouseBillDetail detail : details){
					handle(detail, bill.getInWareHouse(), FLAG_IN);
					handle(detail, bill.getOutWareHouse(), FLAG_OUT);
				}
				break;
			}
			
			default:
				break;
		}
	}
	
	/**
	 * 反向处理即时分仓库存
	 * @param bill
	 */
	public void reverseHandleStock(WarehouseBill bill){
		switch(bill.getBillType()){
			case WarehouseBuilderCodeHelper.qckc:
			case WarehouseBuilderCodeHelper.cgrk:
			case WarehouseBuilderCodeHelper.sctl:
			case WarehouseBuilderCodeHelper.cprk:
			case WarehouseBuilderCodeHelper.xsth:{
				//入库操作：期初库存、采购入库、生产退料、成品入库、销售退货
				handle(bill, FLAG_OUT);
				break;
			}
			
			case WarehouseBuilderCodeHelper.cgth:
			case WarehouseBuilderCodeHelper.scll:
			case WarehouseBuilderCodeHelper.cptk:
			case WarehouseBuilderCodeHelper.xsch:
			case WarehouseBuilderCodeHelper.bsd:{
				//出库操作：采购退货、生产领料、成品退库、销售出库、报损单
				handle(bill, FLAG_IN);
				break;
			}
			
			case WarehouseBuilderCodeHelper.pdd:{
				//盘点单(+-)
				List<WarehouseBillDetail> details = bill.getDetails();
				for(WarehouseBillDetail detail : details){
					if(detail.getAccountQuentity().compareTo(BigDecimal.ZERO) > 0){
						handle(detail, bill.getInWareHouse(), FLAG_OUT);
					}
					else{
						handle(detail, bill.getInWareHouse(), FLAG_IN);
					}
				}
				break;
			}
			
			case WarehouseBuilderCodeHelper.dcd:{
				//调仓单
				List<WarehouseBillDetail> details = bill.getDetails();
				for(WarehouseBillDetail detail : details){
					handle(detail, bill.getInWareHouse(), FLAG_OUT);
					handle(detail, bill.getOutWareHouse(), FLAG_IN);
				}
				break;
			}
			
			default:
				break;
		}
	}
	
	/**
	 * 增减库存
	 * @param bill 仓储单据
	 * @param flag 出入库标识
	 */
	private void handle(WarehouseBill bill, int flag){
		List<WarehouseBillDetail> details = bill.getDetails();
		for(WarehouseBillDetail detail : details){
			handle(detail, detail.getInWareHouse(), flag);
		}
	}
	
	/**
	 * 增减库存
	 * @param billDetail 仓储单据明细
	 * @param warehouse 仓库
	 * @param flag 出入库标识
	 */
	private void handle(WarehouseBillDetail billDetail, AuxiliaryAttr warehouse, int flag){
		WarehouseBill bill = billDetail.getBill();
		Goods goods = billDetail.getGoods();
		GoodsSpec goodsSpec = billDetail.getGoodsSpec();
		int billType = bill.getBillType();
		
		//货品不计算库存
		if(goods.getAccountFlag() == Goods.ACCOUNT_FLAG_NO){
			return;
		}
		
		//即时分仓库存记录
		StockStore stockStore = null;
		if(goodsSpec == null){
			stockStore = getStock(SecurityUtil.getFiscalAccountId(), warehouse.getFid(), goods.getFid());	
		}
		else{
			stockStore = getStock(SecurityUtil.getFiscalAccountId(), warehouse.getFid(), goods.getFid(), goodsSpec.getFid());
		}
		
		if(stockStore == null && flag == FLAG_OUT){
			throw new RuntimeException("库存不足!");
		}
		
		if(stockStore == null && flag == FLAG_IN){
			save(billType, billDetail, warehouse);
		}
		else{
			BigDecimal quentity = stockStore.getAccountQuentity();
			if(flag == FLAG_IN){
				quentity = quentity.add(billDetail.getAccountQuentity().abs());
			}
			else if(flag == FLAG_OUT){
				quentity = quentity.subtract(billDetail.getAccountQuentity().abs());					
			}
			if(quentity.compareTo(BigDecimal.ZERO) < 0){
				throw new RuntimeException("库存不足!");
			}
			
			if(billType == WarehouseBuilderCodeHelper.cgth || billType == WarehouseBuilderCodeHelper.xsch){
				//TODO 采购退货和销售出库按了出库功能才写入出库结存数量
			}
			else{
				stockStore.setCheckoutQuentity(billDetail.getAccountQuentity());
			}
				
			stockStore.setAccountQuentity(quentity);
			stockStoreRepo.save(stockStore);
		}
	}
	
	/**
	 * 保存库存记录
	 * @param billType 单据类型
	 * @param billDetail 单据明细
	 * @param warehouse 仓库
	 */
	private void save(int billType, WarehouseBillDetail billDetail, AuxiliaryAttr warehouse){
		StockStore entity = new StockStore();
		entity.setOrg(SecurityUtil.getCurrentOrg());
		entity.setWarehouse(warehouse);
		entity.setGoods(billDetail.getGoods());
		entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
		entity.setGoodsSpec(billDetail.getGoodsSpec());
		entity.setAccountUnit(billDetail.getAccountUint());
		entity.setAccountQuentity(billDetail.getAccountQuentity());
		if(billType == WarehouseBuilderCodeHelper.cgth || billType == WarehouseBuilderCodeHelper.xsch){
			//TODO 采购退货和销售出库按了出库功能才写入出库结存数量
		}
		else{
			entity.setCheckoutQuentity(billDetail.getAccountQuentity());
		}
		stockStoreRepo.save(entity);
	}
	
	/**
	 * 获取库存
	 * @param fiscalAccountId 财务账套ID
	 * @param warehouseId 仓库ID
	 * @param goodsId 货品ID
	 * @param goodsSpecId 货品属性ID
	 * @return
	 */
	public StockStore getStock(String fiscalAccountId, String warehouseId, String goodsId, String goodsSpecId){
		Assert.isTrue(StringUtils.isNotBlank(warehouseId));
		Assert.isTrue(StringUtils.isNotBlank(goodsId));
		Assert.isTrue(StringUtils.isNotBlank(goodsSpecId));
		
		return stockStoreRepo.findTopBy(fiscalAccountId, warehouseId, goodsId, goodsSpecId); 
	}
	
	/**
	 * 获取库存
	 * @param fiscalAccountId 财务账套ID
	 * @param warehouseId 仓库ID
	 * @param goodsId 货品ID
	 * @return
	 */
	public StockStore getStock(String fiscalAccountId, String warehouseId, String goodsId){
		Assert.isTrue(StringUtils.isNotBlank(warehouseId));
		Assert.isTrue(StringUtils.isNotBlank(goodsId));
		
		return stockStoreRepo.findTopBy(fiscalAccountId, warehouseId, goodsId);
	}
	
	/**
	 * 根据账套查找
	 * @param fiscalAccountId
	 * @return
	 */
	public List<StockStore> queryByAccountId(String fiscalAccountId) {
		return stockStoreRepo.findByAccId(fiscalAccountId);
	}
	
	/**
	 * 检查即时分仓库存是否足够
	 * @param orgId 机构ID
	 * @param fiscalAccountId 财务账套ID
	 * @param billId 仓库单据ID
	 * @return 库存不足的货品ID，货品属性ID集合
	 */
	public List<Object[]> checkStock(String orgId, String fiscalAccountId, String billId){
		WarehouseBill bill = billRepo.findOne(billId);
		if(bill.getBillType() == WarehouseBuilderCodeHelper.pdd){
			//盘点单
			return this.stockStoreRepo.checkStockByPdd(orgId, fiscalAccountId, billId);
		}
		else if(bill.getBillType() == WarehouseBuilderCodeHelper.dcd){
			//调仓单
			return this.stockStoreRepo.checkStockByDcd(orgId, fiscalAccountId, billId);
		}
		else{
			return this.stockStoreRepo.checkStock(orgId, fiscalAccountId, billId);
		}
	}
	/**
	 * 根据仓库id+货品id+货品属性id查询即时分仓库存
	 * @param warehouseId
	 * @param goodsId
	 * @param goodsSpecId
	 * @return
	 */
	public StockStore queryLastStock(String accId,String warehouseId,String goodsId,String goodsSpecId){
		if(Strings.isNullOrEmpty(goodsSpecId)){
			return stockStoreRepo.queryLastStock(accId,warehouseId, goodsId);
		}else{
			return stockStoreRepo.queryLastStock(accId,warehouseId, goodsId, goodsSpecId);
		}
	}
}
