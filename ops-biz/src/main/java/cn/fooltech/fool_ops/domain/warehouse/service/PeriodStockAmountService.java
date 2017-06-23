package cn.fooltech.fool_ops.domain.warehouse.service;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.Goods;
import cn.fooltech.fool_ops.domain.period.entity.StockPeriod;
import cn.fooltech.fool_ops.domain.period.service.StockPeriodService;
import cn.fooltech.fool_ops.domain.warehouse.entity.*;
import cn.fooltech.fool_ops.domain.warehouse.repository.PeriodStockAmountRepository;
import cn.fooltech.fool_ops.domain.warehouse.strategy.CalMethod;
import cn.fooltech.fool_ops.domain.warehouse.strategy.CalMethodProcessor;
import cn.fooltech.fool_ops.domain.warehouse.vo.PeriodStockAmountVo;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.VoFactory;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 期间分仓库存服务类
 * </p>
 * 
 * @author xjh
 * @version 1.0
 * @date 2015年9月22日
 */
@Service("ops.PeriodStockAmountService")
public class PeriodStockAmountService extends BaseService<PeriodStockAmount,
		PeriodStockAmountVo, String> {

	private final int SCALE_AMOUNT = 2;
	private final int TYPE_DCD = 21;
	private final int TYPE_PDD = 20;
	private final int TYPE_XSTH = 42;

	/**
	 * 会计期间服务类
	 */
	@Autowired
	private StockPeriodService periodService;

	/**
	 * 出库服务类
	 */
	@Autowired
	private OutStorageService outStorageService;
	
	@Autowired
	private PeriodStockAmountRepository stockAmountRepo;


	//===============================================

	/**
	 * 根据策略处理分仓入仓字段
	 */
	@Transactional
	public void processIn(List<CalMethod> methods, WarehouseBill bill, WarehouseBillDetail detail, PeriodAmount total){

		String peridId = bill.getStockPeriod().getFid();
		String goodsId = detail.getGoods().getFid();
		AuxiliaryAttr warehouse = detail.getInWareHouse();
		String goodsSpecId = null;

		if (detail.getGoodsSpec() != null) {
			goodsSpecId = detail.getGoodsSpec().getFid();
		}


		PeriodStockAmount exist = existRecord(peridId, warehouse.getFid(), goodsId, goodsSpecId);
		if (exist == null) {
			exist = initPeriodAmount(bill, detail, warehouse);
		}
		exist = CalMethodProcessor.process(exist, detail, total, methods);

		stockAmountRepo.save(exist);
	}


	/**
	 * 根据策略处理分仓出仓字段
	 */
	@Transactional
	public void processOut(List<CalMethod> methods, WarehouseBill bill, WarehouseBillDetail detail, PeriodAmount total){

		String peridId = bill.getStockPeriod().getFid();
		String goodsId = detail.getGoods().getFid();
		AuxiliaryAttr warehouse = detail.getOutWareHouse();
		String goodsSpecId = null;

		if (detail.getGoodsSpec() != null) {
			goodsSpecId = detail.getGoodsSpec().getFid();
		}


		PeriodStockAmount exist = existRecord(peridId, warehouse.getFid(), goodsId, goodsSpecId);
		if (exist == null) {
			exist = initPeriodAmount(bill, detail, warehouse);
		}
		exist = CalMethodProcessor.process(exist, detail, total, methods);

		stockAmountRepo.save(exist);

	}
	//===============================================


	/**
	 * 通过Bill和Detials维护期间总存金额表
	 */
	public void refreshPeriodStockAmount(WarehouseBill bill,
			List<WarehouseBillDetail> details, List<OutStorage> outList, int inout, String accountId) {

		for (WarehouseBillDetail detail : details) {
			if (detail.getGoods().getAccountFlag()
					.equals(Goods.ACCOUNT_FLAG_YES)) {
				refreshPeriodStockAmount(bill, detail, outList, inout, accountId);
			}
		}
	}

	/**
	 * 通过Bill和Detials维护期间总存金额表(反向操作)
	 */
	public void reversePeriodStockAmount(WarehouseBill bill,
			List<WarehouseBillDetail> details, List<OutStorage> outList, int inout, String accountId) {

		for (WarehouseBillDetail detail : details) {
			if (detail.getGoods().getAccountFlag()
					.equals(Goods.ACCOUNT_FLAG_YES)) {
				reversePeriodStockAmount(bill, detail, outList, inout, accountId);
			}
		}
	}

	/**
	 * 通过Bill和Detial维护期间分仓库存表
	 */
	public void refreshPeriodStockAmount(WarehouseBill bill,
			WarehouseBillDetail detail, List<OutStorage> outList, int inout, String accountId) {

		if (detail.getGoods().getAccountFlag().equals(Goods.ACCOUNT_FLAG_NO)) {
			return;
		}
		String peridId = bill.getStockPeriod().getFid();
		String goodsId = detail.getGoods().getFid();
		String goodsSpecId = null;
		if (detail.getGoodsSpec() != null) {
			goodsSpecId = detail.getGoodsSpec().getFid();
		}
		AuxiliaryAttr warehouse = detail.getInWareHouse();
		if (inout == WarehouseBill.MOVE_OUT) {
			warehouse = detail.getOutWareHouse();
		}
		String warehouseId = warehouse.getFid();
		PeriodStockAmount exist = existRecord(peridId, warehouseId,
				goodsId, goodsSpecId);
		if (exist == null) {
			exist = initPeriodAmount(bill, detail, warehouse);
		}

		// >0 & 出仓
		if (inout == WarehouseBill.OUT
				&& detail.getAccountQuentity().compareTo(BigDecimal.ZERO) > 0) {
			exist.setOutQuentity(exist.getOutQuentity().add(
					detail.getAccountQuentity()));
			exist.setAccountQuentity(exist.getAccountQuentity().subtract(
					detail.getAccountQuentity()));

			BigDecimal outAmount = getOutAmount(warehouseId, detail, outList);
			exist.setOutAmount(exist.getOutAmount().add(outAmount));
			exist.setAccountAmount(exist.getAccountAmount().subtract(outAmount));

			// <0 & 出仓
		} else if (inout == WarehouseBill.OUT
				&& detail.getAccountQuentity().compareTo(BigDecimal.ZERO) < 0) {
			exist.setOutQuentity(exist.getOutQuentity().add(
					detail.getAccountQuentity()));
			exist.setAccountQuentity(exist.getAccountQuentity().subtract(
					detail.getAccountQuentity()));

			BigDecimal outAmount = getOutAmount(warehouseId, detail, outList);
			exist.setOutAmount(exist.getOutAmount().subtract(outAmount));
			exist.setAccountAmount(exist.getAccountAmount().add(outAmount));

			// >0 & 入仓
		} else if (inout == WarehouseBill.IN
				&& detail.getAccountQuentity().compareTo(BigDecimal.ZERO) > 0) {
			exist.setInQuentity(exist.getInQuentity().add(
					detail.getAccountQuentity()));
			exist.setAccountQuentity(exist.getAccountQuentity().add(
					detail.getAccountQuentity()));

			BigDecimal outAmount = detail.getType();
			outAmount = outAmount.setScale(SCALE_AMOUNT,
					BigDecimal.ROUND_HALF_UP);
			exist.setInAmount(exist.getInAmount().add(outAmount));
			exist.setAccountAmount(exist.getAccountAmount().add(outAmount));
			// <0 & 入仓
		} else if (inout == WarehouseBill.IN
				&& detail.getAccountQuentity().compareTo(BigDecimal.ZERO) < 0) {
			exist.setInQuentity(exist.getInQuentity().add(
					detail.getAccountQuentity()));
			exist.setAccountQuentity(exist.getAccountQuentity().add(
					detail.getAccountQuentity()));

			BigDecimal outAmount = getOutAmount(warehouseId, detail, outList);
			exist.setInAmount(exist.getInAmount().subtract(outAmount));
			exist.setAccountAmount(exist.getAccountAmount().subtract(outAmount));
		} else if (inout == WarehouseBill.MOVE_IN) {
			exist.setInQuentity(exist.getInQuentity().add(
					detail.getAccountQuentity()));
			exist.setMoveInQuentity(exist.getMoveInQuentity().add(
					detail.getAccountQuentity()));
			exist.setAccountQuentity(exist.getAccountQuentity().add(
					detail.getAccountQuentity()));
		} else if (inout == WarehouseBill.MOVE_OUT) {
			exist.setOutQuentity(exist.getOutQuentity().add(
					detail.getAccountQuentity()));
			exist.setMoveOutQuentity(exist.getMoveOutQuentity().add(
					detail.getAccountQuentity()));
			exist.setAccountQuentity(exist.getAccountQuentity().subtract(
					detail.getAccountQuentity()));
			// >0 & 入仓-退货
		} else if (inout == WarehouseBill.REVERSE_IN
				&& detail.getAccountQuentity().compareTo(BigDecimal.ZERO) > 0) {
			exist.setInQuentity(exist.getInQuentity().subtract(
					detail.getAccountQuentity()));
			exist.setAccountQuentity(exist.getAccountQuentity().subtract(
					detail.getAccountQuentity()));

			BigDecimal outAmount = getOutAmount(warehouseId, detail, outList);
			exist.setInAmount(exist.getInAmount().subtract(outAmount));
			exist.setAccountAmount(exist.getAccountAmount().subtract(outAmount));
			// >0 & 出仓-退货
		} else if (inout == WarehouseBill.REVERSE_OUT
				&& detail.getAccountQuentity().compareTo(BigDecimal.ZERO) > 0) {
			exist.setOutQuentity(exist.getOutQuentity().subtract(
					detail.getAccountQuentity()));
			exist.setAccountQuentity(exist.getAccountQuentity().add(
					detail.getAccountQuentity()));

			BigDecimal outAmount = getOutAmount(warehouseId, detail, outList);
			exist.setOutAmount(exist.getOutAmount().subtract(outAmount));
			exist.setAccountAmount(exist.getAccountAmount().add(outAmount));
		}
		stockAmountRepo.save(exist);
	}

	public PeriodStockAmount initPeriodAmount(WarehouseBill bill,
			WarehouseBillDetail detail, AuxiliaryAttr warehouse) {

		PeriodStockAmount exist = new PeriodStockAmount();

		exist.setGoods(detail.getGoods());
		exist.setGoodsSpec(detail.getGoodsSpec());
		exist.setStockPeriod(bill.getStockPeriod());
		exist.setAccountUnit(detail.getAccountUint());
		exist.setOrg(detail.getOrg());
		exist.setStockPeriod(bill.getStockPeriod());
		exist.setWarehouse(warehouse);

		// 查找上一期的记录
		StockPeriod lastPeriod = periodService.getPrePeriod(bill.getStockPeriod().getFid());

		// 没有上一期的会计期间
		if (lastPeriod != null) {
			String specId = detail.getGoodsSpec() == null ? null : detail
					.getGoodsSpec().getFid();

			PeriodStockAmount last = existRecord(
					lastPeriod.getFid(), warehouse.getFid(), detail.getGoods()
							.getFid(), specId);

			// 没有上一期的记录
			if (last != null) {
				exist.setPreAmount(last.getAccountAmount());
				exist.setPreQuentity(last.getAccountQuentity());
				exist.setAccountQuentity(exist.getAccountQuentity().add(
						last.getAccountQuentity()));
				exist.setAccountAmount(exist.getAccountAmount().add(
						last.getAccountAmount()));
			}
		}

		return exist;
	}

	/**
	 * 计算成本金额
	 */
	private BigDecimal getOutAmount(String warehouseId,
			WarehouseBillDetail detail, List<OutStorage> outList) {
		
		if(detail.getBill().getBillType() == TYPE_XSTH){
			return detail.getCostPrice().multiply(detail.getAccountQuentity());
		}
		
		// 计算出库的总金额
		BigDecimal outAmount = BigDecimal.ZERO;
		if (outList != null && outList.size() > 0) {
			outAmount = outStorageService.findOutStorageSum(warehouseId,
					detail.getFid(), outList);
		} else {
			outAmount = outStorageService.findOutStorageSum(warehouseId,
					detail.getFid());
		}
		return outAmount;
	}

	/**
	 * 通过Bill和Detial维护期间分仓库存表(反向操作)
	 */
	public void reversePeriodStockAmount(WarehouseBill bill,
			WarehouseBillDetail detail, List<OutStorage> outList, int inout, String accountId) {

		if (detail.getGoods().getAccountFlag().equals(Goods.ACCOUNT_FLAG_NO)) {
			return;
		}
		String peridId = bill.getStockPeriod().getFid();
		String goodsId = detail.getGoods().getFid();
		String goodsSpecId = null;
		if (detail.getGoodsSpec() != null) {
			goodsSpecId = detail.getGoodsSpec().getFid();
		}
		AuxiliaryAttr warehouse = detail.getInWareHouse();
		if (inout == WarehouseBill.MOVE_OUT) {
			warehouse = detail.getOutWareHouse();
		}
		String warehouseId = warehouse.getFid();
		PeriodStockAmount exist = existRecord(peridId, warehouseId,
				goodsId, goodsSpecId);
		if (exist == null) {
			exist = initPeriodAmount(bill, detail, warehouse);
		}

		// >0 & 出仓
		if (inout == WarehouseBill.OUT
				&& detail.getAccountQuentity().compareTo(BigDecimal.ZERO) > 0) {
			exist.setOutQuentity(exist.getOutQuentity().subtract(
					detail.getAccountQuentity()));
			exist.setAccountQuentity(exist.getAccountQuentity().add(
					detail.getAccountQuentity()));

			BigDecimal outAmount = getOutAmount(warehouseId, detail, outList);
			exist.setOutAmount(exist.getOutAmount().subtract(outAmount));
			exist.setAccountAmount(exist.getAccountAmount().add(outAmount));

			// <0 & 出仓
		} else if (inout == WarehouseBill.OUT
				&& detail.getAccountQuentity().compareTo(BigDecimal.ZERO) < 0) {
			exist.setOutQuentity(exist.getOutQuentity().subtract(
					detail.getAccountQuentity()));
			exist.setAccountQuentity(exist.getAccountQuentity().add(
					detail.getAccountQuentity()));

			BigDecimal outAmount = getOutAmount(warehouseId, detail, outList);
			exist.setOutAmount(exist.getOutAmount().add(outAmount));
			exist.setAccountAmount(exist.getAccountAmount().subtract(outAmount));

			// >0 & 入仓
		} else if (inout == WarehouseBill.IN
				&& detail.getAccountQuentity().compareTo(BigDecimal.ZERO) > 0) {
			exist.setInQuentity(exist.getInQuentity().subtract(
					detail.getAccountQuentity()));
			exist.setAccountQuentity(exist.getAccountQuentity().subtract(
					detail.getAccountQuentity()));

			BigDecimal outAmount = detail.getType();
			outAmount = outAmount.setScale(SCALE_AMOUNT,
					BigDecimal.ROUND_HALF_UP);
			exist.setInAmount(exist.getInAmount().subtract(outAmount));
			exist.setAccountAmount(exist.getAccountAmount().subtract(outAmount));
			// <0 & 入仓
		} else if (inout == WarehouseBill.IN
				&& detail.getAccountQuentity().compareTo(BigDecimal.ZERO) < 0) {
			exist.setInQuentity(exist.getInQuentity().subtract(
					detail.getAccountQuentity()));
			exist.setAccountQuentity(exist.getAccountQuentity().subtract(
					detail.getAccountQuentity()));

			BigDecimal outAmount = getOutAmount(warehouseId, detail, outList);
			exist.setInAmount(exist.getInAmount().add(outAmount));
			exist.setAccountAmount(exist.getAccountAmount().add(outAmount));
		} else if (inout == WarehouseBill.MOVE_IN) {
			exist.setInQuentity(exist.getInQuentity().subtract(
					detail.getAccountQuentity()));
			exist.setMoveInQuentity(exist.getMoveInQuentity().subtract(
					detail.getAccountQuentity()));
			exist.setAccountQuentity(exist.getAccountQuentity().subtract(
					detail.getAccountQuentity()));
		} else if (inout == WarehouseBill.MOVE_OUT) {
			exist.setOutQuentity(exist.getOutQuentity().subtract(
					detail.getAccountQuentity()));
			exist.setMoveOutQuentity(exist.getMoveOutQuentity().subtract(
					detail.getAccountQuentity()));
			exist.setAccountQuentity(exist.getAccountQuentity().add(
					detail.getAccountQuentity()));
			// >0 & 入仓-退货
		} else if (inout == WarehouseBill.REVERSE_IN
				&& detail.getAccountQuentity().compareTo(BigDecimal.ZERO) > 0) {
			exist.setInQuentity(exist.getInQuentity().add(
					detail.getAccountQuentity()));
			exist.setAccountQuentity(exist.getAccountQuentity().add(
					detail.getAccountQuentity()));

			BigDecimal outAmount = getOutAmount(warehouseId, detail, outList);
			exist.setInAmount(exist.getInAmount().add(outAmount));
			exist.setAccountAmount(exist.getAccountAmount().add(outAmount));
			// >0 & 出仓-退货
		} else if (inout == WarehouseBill.REVERSE_OUT
				&& detail.getAccountQuentity().compareTo(BigDecimal.ZERO) > 0) {
			exist.setOutQuentity(exist.getOutQuentity().add(
					detail.getAccountQuentity()));
			exist.setAccountQuentity(exist.getAccountQuentity().subtract(
					detail.getAccountQuentity()));

			BigDecimal outAmount = getOutAmount(warehouseId, detail, outList);
			exist.setOutAmount(exist.getOutAmount().add(outAmount));
			exist.setAccountAmount(exist.getAccountAmount().subtract(outAmount));
		}

		stockAmountRepo.save(exist);
	}

	/**
	 * 处理盘点单
	 */
	public void checkPeriodStockAmount(WarehouseBill bill,
			List<WarehouseBillDetail> details, List<OutStorage> outList,
			boolean reverse) {
		for (WarehouseBillDetail detail : details) {
			if (detail.getGoods().getAccountFlag()
					.equals(Goods.ACCOUNT_FLAG_YES)) {

				String peridId = bill.getStockPeriod().getFid();
				String goodsId = detail.getGoods().getFid();
				String warehouseId = detail.getInWareHouse().getFid();
				String goodsSpecId = null;
				if (detail.getGoodsSpec() != null) {
					goodsSpecId = detail.getGoodsSpec().getFid();
				}

				// 计算成本金额
				BigDecimal outAmount = BigDecimal.ZERO;
				if (detail.getAccountQuentity().compareTo(BigDecimal.ZERO) > 0) {
					outAmount = detail.getAccountQuentity().multiply(
							detail.getAccountUintPrice());
				} else {
					outAmount = BigDecimal.ZERO.subtract(getOutAmount(
							warehouseId, detail, outList));
				}

				// 先查记录是否存在
				PeriodStockAmount exist = existRecord(peridId,
						warehouseId, goodsId, goodsSpecId);
				if (exist == null) {
					exist = new PeriodStockAmount();
					exist.setAccountUnit(detail.getAccountUint());
					exist.setGoods(detail.getGoods());
					exist.setGoodsSpec(detail.getGoodsSpec());
					exist.setStockPeriod(bill.getStockPeriod());
					exist.setWarehouse(detail.getInWareHouse());
					exist.setOrg(detail.getOrg());

					exist.setAccountQuentity(detail.getAccountQuentity());
					exist.setAccountAmount(outAmount);
				} else {
					if (reverse) {
						exist.setAccountQuentity(exist.getAccountQuentity()
								.subtract(detail.getAccountQuentity()));
						exist.setAccountAmount(exist.getAccountAmount()
								.subtract(outAmount));
					} else {
						exist.setAccountQuentity(exist.getAccountQuentity()
								.add(detail.getAccountQuentity()));
						exist.setAccountAmount(exist.getAccountAmount().add(
								outAmount));
					}
				}
				if (reverse) {
					exist.setProfitAmount(exist.getProfitAmount().subtract(
							outAmount));
					exist.setProfitQuentity(exist.getProfitQuentity().subtract(
							detail.getAccountQuentity()));
				} else {
					exist.setProfitAmount(exist.getProfitAmount()
							.add(outAmount));
					exist.setProfitQuentity(exist.getProfitQuentity().add(
							detail.getAccountQuentity()));
				}

				stockAmountRepo.save(exist);
			}
		}
	}

	/**
	 * 获取库存记录
	 * @param periodId 仓储会计期间ID
	 * @param warehouseId 仓库ID
	 * @param goodsId 货品ID
	 * @param goodsSpecId 货品属性ID
	 * @return
	 */
	public PeriodStockAmount existRecord(String periodId, String warehouseId, String goodsId, 
			String goodsSpecId){
		return stockAmountRepo.findTopBy(periodId, warehouseId, goodsId, goodsSpecId);
	}

	@Override
	public CrudRepository<PeriodStockAmount, String> getRepository() {
		return stockAmountRepo;
	}
	
	/**
	 * 判断是否有足够库存
	 * 不够则返回不够库存的明细列表，都足够则返回空列表
	 */
	public List<WarehouseBillDetail> checkEnoughStock(List<WarehouseBillDetail> billDetails) {
		List<WarehouseBillDetail> notEnough = Lists.newArrayList();
		if(CollectionUtils.isEmpty(billDetails)){
			return notEnough;
		}
		
		WarehouseBill bill = billDetails.get(0).getBill();
		for (WarehouseBillDetail detail : billDetails) {
			if (detail.getAccountQuentity().compareTo(BigDecimal.ZERO) > 0
					&& bill.getBillType() == TYPE_PDD) {
				continue;
			}
			List<PeriodStockAmount> list = stockAmountRepo.findBy(bill,detail);
			if (list.size() < 1){
				notEnough.add(detail);
			}else{
				PeriodStockAmount exist = list.get(0);
				if (exist == null
						|| exist.getAccountQuentity().compareTo(
								detail.getAccountQuentity().abs()) < 0){
					notEnough.add(detail);
				}
			}
		}
		
		return notEnough;
	}

	@Override
	public PeriodStockAmountVo getVo(PeriodStockAmount entity) {

		if(entity==null)return null;

		PeriodStockAmountVo vo = VoFactory.createValue(PeriodStockAmountVo.class, entity);

		vo.setAccountAmount(NumberUtil.scale(vo.getAccountAmount(),2));
		vo.setAccountPrice(NumberUtil.scale(vo.getAccountPrice(),2));
		vo.setAccountQuentity(NumberUtil.scale(vo.getAccountQuentity(),2));

		vo.setInAmount(NumberUtil.scale(vo.getInAmount(),2));
		vo.setLossAmount(NumberUtil.scale(vo.getLossAmount(),2));
		vo.setInQuentity(NumberUtil.scale(vo.getInQuentity(),2));
		vo.setLossQuantity(NumberUtil.scale(vo.getLossQuantity(),2));

		vo.setMaterialsAmount(NumberUtil.scale(vo.getMaterialsAmount(),2));
		vo.setMaterialsQuantity(NumberUtil.scale(vo.getMaterialsQuantity(),2));
		vo.setMaterialsReturnAmount(NumberUtil.scale(vo.getMaterialsReturnAmount(),2));
		vo.setMaterialsReturnQuantity(NumberUtil.scale(vo.getMaterialsReturnQuantity(),2));
		vo.setMoveInAmount(NumberUtil.scale(vo.getMoveInAmount(),2));

		vo.setMoveInQuentity(NumberUtil.scale(vo.getMoveInQuentity(),2));
		vo.setMoveOutAmount(NumberUtil.scale(vo.getMoveOutAmount(),2));
		vo.setMoveOutQuentity(NumberUtil.scale(vo.getMoveOutQuentity(),2));
		vo.setOutAmount(NumberUtil.scale(vo.getOutAmount(),2));
		vo.setOutQuentity(NumberUtil.scale(vo.getOutQuentity(),2));
		vo.setInAmount(NumberUtil.scale(vo.getInAmount(),2));

		vo.setInQuentity(NumberUtil.scale(vo.getInQuentity(),2));
		vo.setMovePrice(NumberUtil.scale(vo.getMovePrice(),2));
		vo.setPreAmount(NumberUtil.scale(vo.getPreAmount(),2));
		vo.setPrePrice(NumberUtil.scale(vo.getPrePrice(),2));
		vo.setPreQuentity(NumberUtil.scale(vo.getPreQuentity(),2));
		vo.setProductAmount(NumberUtil.scale(vo.getProductAmount(),2));
		vo.setProductQuantity(NumberUtil.scale(vo.getProductQuantity(),2));
		vo.setProductReturnAmount(NumberUtil.scale(vo.getProductReturnAmount(),2));
		vo.setProductReturnQuantity(NumberUtil.scale(vo.getProductReturnQuantity(),2));
		vo.setProfitAmount(NumberUtil.scale(vo.getProfitAmount(),2));

		vo.setProfitQuentity(NumberUtil.scale(vo.getProfitQuentity(),2));
		vo.setPurchaseAmount(NumberUtil.scale(vo.getPurchaseAmount(),2));
		vo.setPurchaseQuantity(NumberUtil.scale(vo.getPurchaseQuantity(),2));
		vo.setPurchaseReturnAmount(NumberUtil.scale(vo.getPurchaseReturnAmount(),2));
		vo.setPurchaseReturnQuantity(NumberUtil.scale(vo.getPurchaseReturnQuantity(),2));
		vo.setSaleAmount(NumberUtil.scale(vo.getSaleAmount(),2));
		vo.setSaleQuantity(NumberUtil.scale(vo.getSaleQuantity(),2));

		vo.setSaleReturnAmount(NumberUtil.scale(vo.getSaleReturnAmount(),2));
		vo.setSaleReturnQuantity(NumberUtil.scale(vo.getSaleReturnQuantity(),2));
		vo.setTransportInAmount(NumberUtil.scale(vo.getTransportInAmount(),2));
		vo.setTransportInQuantity(NumberUtil.scale(vo.getTransportInQuantity(),2));
		vo.setTransportOutAmount(NumberUtil.scale(vo.getTransportOutAmount(),2));
		vo.setTransportOutQuantity(NumberUtil.scale(vo.getTransportOutQuantity(),2));

		if(entity.getGoods()!=null){
			vo.setGoodsId(entity.getGoods().getFid());
			vo.setGoodsName(entity.getGoods().getName());
			vo.setGoodsCode(entity.getGoods().getCode());
		}
		if(entity.getGoodsSpec()!=null){
			vo.setGoodsSpecId(entity.getGoodsSpec().getFid());
			vo.setGoodsSpecName(entity.getGoodsSpec().getName());
		}
		if(entity.getWarehouse()!=null){
			vo.setWarehouseId(entity.getWarehouse().getFid());
			vo.setWarehouseName(entity.getWarehouse().getName());
		}
		if(entity.getAccountUnit()!=null){
			vo.setAccountUnitId(entity.getAccountUnit().getFid());
			vo.setAccountUnitName(entity.getAccountUnit().getName());
		}

		return vo;
	}

	/**
	 * 根据会计期间ID，货品ID，属性ID查询库存分仓记录
	 * @param vo
	 * @param pageParamater
	 * @return
	 */
	public Page<PeriodStockAmountVo> query(PeriodStockAmountVo vo, PageParamater pageParamater){

		Sort sort = new Sort(Sort.Direction.ASC, "goods.code");
		PageRequest request = getPageRequest(pageParamater, sort);
		Page<PeriodStockAmount> page = stockAmountRepo.queryBy(vo, request);
		return getPageVos(page, request);
	}
}
