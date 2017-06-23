package cn.fooltech.fool_ops.domain.warehouse.service;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.base.service.AbstractBaseService;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.Goods;
import cn.fooltech.fool_ops.domain.period.entity.StockPeriod;
import cn.fooltech.fool_ops.domain.period.service.StockPeriodService;
import cn.fooltech.fool_ops.domain.warehouse.entity.*;
import cn.fooltech.fool_ops.domain.warehouse.repository.PeriodAmountRepository;
import cn.fooltech.fool_ops.domain.warehouse.strategy.CalMethod;
import cn.fooltech.fool_ops.domain.warehouse.strategy.CalMethodProcessor;
import cn.fooltech.fool_ops.domain.warehouse.vo.PeriodAmountVo;
import cn.fooltech.fool_ops.domain.warehouse.vo.PeriodStockAmountVo;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.ReflectionUtils;
import cn.fooltech.fool_ops.utils.VoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 期间总库存金额服务类
 * </p>
 * 
 * @author xjh
 * @version 1.0
 * @date 2015年9月22日
 */
@Service("ops.PeriodAmountService")
public class PeriodAmountService extends BaseService<PeriodAmount, PeriodAmountVo, String> {

	private final int SCALE_AMOUNT = 2;
	private final int TYPE_XSTH = 42;//销售退货
	private final int TYPE_SCTL = 32;//生产退料

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
	private PeriodAmountRepository periodAmountRepo;


	//===============================================

	/**
	 * 根据策略处理总仓字段
	 */
	@Transactional
	public PeriodAmount process(List<CalMethod> methods, WarehouseBill bill, WarehouseBillDetail detail){

		String peridId = bill.getStockPeriod().getFid();
		String goodsId = detail.getGoods().getFid();
		String goodsSpecId = null;
		if (detail.getGoodsSpec() != null) {
			goodsSpecId = detail.getGoodsSpec().getFid();
		}
		PeriodAmount exist = existRecord(peridId, goodsId, goodsSpecId);
		if (exist == null) {
			exist = initPeriodAmount(bill, detail);
		}
		exist = CalMethodProcessor.process(exist, detail, exist, methods);
		periodAmountRepo.save(exist);

		return exist;
	}

	//===============================================

	/**
	 * 通过Bill和Detials维护期间总存金额表
	 */
	@Transactional
	public void refreshPeriodAmount(WarehouseBill bill,
			List<WarehouseBillDetail> details, List<OutStorage> outList, int inout, String accountId) {

		for (WarehouseBillDetail detail : details) {
			if (detail.getGoods().getAccountFlag()
					.equals(Goods.ACCOUNT_FLAG_YES)) {
				refreshPeriodAmount(bill, detail, outList, inout, accountId);
			}
		}
	}

	/**
	 * 通过Bill和Detials维护期间总存金额表(反向操作)
	 */
	@Transactional
	public void reversePeriodAmount(WarehouseBill bill,
			List<WarehouseBillDetail> details, List<OutStorage> outList, int inout, String accountId) {

		for (WarehouseBillDetail detail : details) {
			if (detail.getGoods().getAccountFlag()
					.equals(Goods.ACCOUNT_FLAG_YES)) {
				reversePeriodAmount(bill, detail, outList, inout, accountId);
			}
		}
	}

	/**
	 * 通过Bill和Detial维护期间总存金额表
	 */
	@Transactional
	private void refreshPeriodAmount(WarehouseBill bill,
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
		PeriodAmount exist = existRecord(peridId, goodsId, goodsSpecId);
		if (exist == null) {
			exist = initPeriodAmount(bill, detail);
		}

		// >0 & 出仓
		if (inout == WarehouseBill.OUT
				&& detail.getAccountQuentity().compareTo(BigDecimal.ZERO) > 0) {
			exist.setOutQuentity(exist.getOutQuentity().add(
					detail.getAccountQuentity()));
			exist.setAccountQuentity(exist.getAccountQuentity().subtract(
					detail.getAccountQuentity()));

			BigDecimal outAmount = getOutAmount(detail, outList);
			exist.setOutAmount(exist.getOutAmount().add(outAmount));
			exist.setAccountAmount(exist.getAccountAmount().subtract(outAmount));

			// <0 & 出仓
		} else if (inout == WarehouseBill.OUT
				&& detail.getAccountQuentity().compareTo(BigDecimal.ZERO) < 0) {
			exist.setOutQuentity(exist.getOutQuentity().add(
					detail.getAccountQuentity()));
			exist.setAccountQuentity(exist.getAccountQuentity().subtract(
					detail.getAccountQuentity()));

			BigDecimal outAmount = getOutAmount(detail, outList);
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

			BigDecimal outAmount = getOutAmount(detail, outList);
			exist.setInAmount(exist.getInAmount().subtract(outAmount));
			exist.setAccountAmount(exist.getAccountAmount().subtract(outAmount));
			// >0 & 入仓-退货
		} else if (inout == WarehouseBill.REVERSE_IN
				&& detail.getAccountQuentity().compareTo(BigDecimal.ZERO) > 0) {
			exist.setInQuentity(exist.getInQuentity().subtract(
					detail.getAccountQuentity()));
			exist.setAccountQuentity(exist.getAccountQuentity().subtract(
					detail.getAccountQuentity()));

			BigDecimal outAmount = getOutAmount(detail, outList);
			exist.setInAmount(exist.getInAmount().subtract(outAmount));
			exist.setAccountAmount(exist.getAccountAmount().subtract(outAmount));
			// >0 & 出仓-退货
		} else if (inout == WarehouseBill.REVERSE_OUT
				&& detail.getAccountQuentity().compareTo(BigDecimal.ZERO) > 0) {
			exist.setOutQuentity(exist.getOutQuentity().subtract(
					detail.getAccountQuentity()));
			exist.setAccountQuentity(exist.getAccountQuentity().add(
					detail.getAccountQuentity()));

			BigDecimal outAmount = getOutAmount(detail, outList);
			/*BigDecimal outAmount = detail.getType();
			outAmount = outAmount.setScale(SCALE_AMOUNT,
					BigDecimal.ROUND_HALF_UP);*/
			exist.setOutAmount(exist.getOutAmount().subtract(outAmount));
			exist.setAccountAmount(exist.getAccountAmount().add(outAmount));
		}
		periodAmountRepo.save(exist);
	}

	public PeriodAmount initPeriodAmount(WarehouseBill bill, WarehouseBillDetail detail) {
		PeriodAmount periodAmount = new PeriodAmount();
		periodAmount.setGoods(detail.getGoods());
		periodAmount.setGoodsSpec(detail.getGoodsSpec());
		periodAmount.setStockPeriod(bill.getStockPeriod());
		periodAmount.setAccountUnit(detail.getAccountUint());
		periodAmount.setOrg(detail.getOrg());
		periodAmount.setStockPeriod(bill.getStockPeriod());

		// 查找上一期的记录
		StockPeriod lastPeriod = periodService.getPrePeriod(periodAmount.getStockPeriod().getFid());

		// 没有上一期的会计期间
		if (lastPeriod != null) {

			String specId = detail.getGoodsSpec() == null ? null : detail
					.getGoodsSpec().getFid();
			PeriodAmount last = existRecord(lastPeriod.getFid(), detail
					.getGoods().getFid(), specId);

			// 没有上一期的记录
			if (last != null) {
				periodAmount.setPreAmount(last.getAccountAmount());
				periodAmount.setPreQuentity(last.getAccountQuentity());
				periodAmount.setAccountQuentity(periodAmount
						.getAccountQuentity().add(last.getAccountQuentity()));
				periodAmount.setAccountAmount(periodAmount.getAccountAmount()
						.add(last.getAccountAmount()));
			}
		}

		return periodAmount;
	}

	/**
	 * 计算成本金额
	 */
	private BigDecimal getOutAmount(WarehouseBillDetail detail,
			List<OutStorage> outList) {
		
		if(detail.getBill().getBillType() == TYPE_XSTH || 
				detail.getBill().getBillType() == TYPE_SCTL){
			return detail.getCostPrice().multiply(detail.getQuentity());
		}
		
		// 计算出库的总金额
		BigDecimal outAmount = BigDecimal.ZERO;
		if (outList != null && outList.size() > 0) {
			outAmount = outStorageService.findOutStorageSum(null,
					detail.getFid(), outList);
		} else {
			outAmount = outStorageService.findOutStorageSum(null,
					detail.getFid());
		}
		return outAmount;
	}

	/**
	 * 通过Bill和Detial维护期间总存金额表(反向操作)
	 */
	@Transactional
	private void reversePeriodAmount(WarehouseBill bill,
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
		PeriodAmount exist = existRecord(peridId, goodsId, goodsSpecId);
		if (exist == null) {
			exist = initPeriodAmount(bill, detail);
		}

		// >0 & 出仓
		if (inout == WarehouseBill.OUT
				&& detail.getAccountQuentity().compareTo(BigDecimal.ZERO) > 0) {
			exist.setOutQuentity(exist.getOutQuentity().subtract(
					detail.getAccountQuentity()));
			exist.setAccountQuentity(exist.getAccountQuentity().add(
					detail.getAccountQuentity()));

			BigDecimal outAmount = getOutAmount(detail, outList);
			exist.setOutAmount(exist.getOutAmount().subtract(outAmount));
			exist.setAccountAmount(exist.getAccountAmount().add(outAmount));

			// <0 & 出仓
		} else if (inout == WarehouseBill.OUT
				&& detail.getAccountQuentity().compareTo(BigDecimal.ZERO) < 0) {
			exist.setOutQuentity(exist.getOutQuentity().subtract(
					detail.getAccountQuentity()));
			exist.setAccountQuentity(exist.getAccountQuentity().add(
					detail.getAccountQuentity()));

			BigDecimal outAmount = getOutAmount(detail, outList);
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

			BigDecimal outAmount = getOutAmount(detail, outList);
			exist.setInAmount(exist.getInAmount().add(outAmount));
			exist.setAccountAmount(exist.getAccountAmount().add(outAmount));
			// >0 & 入仓-退货
		} else if (inout == WarehouseBill.REVERSE_IN
				&& detail.getAccountQuentity().compareTo(BigDecimal.ZERO) > 0) {
			exist.setInQuentity(exist.getInQuentity().add(
					detail.getAccountQuentity()));
			exist.setAccountQuentity(exist.getAccountQuentity().add(
					detail.getAccountQuentity()));

			BigDecimal outAmount = getOutAmount(detail, outList);
			exist.setInAmount(exist.getInAmount().add(outAmount));
			exist.setAccountAmount(exist.getAccountAmount().add(outAmount));
			// >0 & 出仓-退货
		} else if (inout == WarehouseBill.REVERSE_OUT
				&& detail.getAccountQuentity().compareTo(BigDecimal.ZERO) > 0) {
			exist.setOutQuentity(exist.getOutQuentity().add(
					detail.getAccountQuentity()));
			exist.setAccountQuentity(exist.getAccountQuentity().subtract(
					detail.getAccountQuentity()));

			BigDecimal outAmount = getOutAmount(detail, outList);
			/*BigDecimal outAmount = detail.getType();
			outAmount = outAmount.setScale(SCALE_AMOUNT,
					BigDecimal.ROUND_HALF_UP);*/
			exist.setOutAmount(exist.getOutAmount().add(outAmount));
			exist.setAccountAmount(exist.getAccountAmount().subtract(outAmount));
		}
		periodAmountRepo.save(exist);
	}

	/**
	 * 处理盘点单
	 */
	@Transactional
	public void checkPeriodAmount(WarehouseBill bill,
			List<WarehouseBillDetail> details, List<OutStorage> outList,
			boolean reverse) {
		for (WarehouseBillDetail detail : details) {
			if (detail.getGoods().getAccountFlag()
					.equals(Goods.ACCOUNT_FLAG_YES)) {

				String peridId = bill.getStockPeriod().getFid();
				String goodsId = detail.getGoods().getFid();
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
					outAmount = BigDecimal.ZERO.subtract(this.getOutAmount(
							detail, outList));
				}

				// 先查记录是否存在
				PeriodAmount exist = existRecord(peridId, goodsId,
						goodsSpecId);
				if (exist == null) {
					exist = new PeriodAmount();
					exist.setAccountUnit(detail.getAccountUint());
					exist.setGoods(detail.getGoods());
					exist.setGoodsSpec(detail.getGoodsSpec());
					exist.setStockPeriod(bill.getStockPeriod());
					exist.setOrg(detail.getOrg());
					exist.setAccountQuentity(detail.getAccountQuentity());
					exist.setAccountAmount(outAmount);
				} else {
					if (exist.getProfitAmount() == null)
						exist.setProfitAmount(BigDecimal.ZERO);
					if (exist.getProfitQuentity() == null)
						exist.setProfitQuentity(BigDecimal.ZERO);
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
				periodAmountRepo.save(exist);
			}
		}
	}

	
	/**
	 * 获取库存记录
	 * @param periodId 仓储会计期间ID
	 * @param goodsId 货品ID
	 * @param goodsSpecId 货品属性ID
	 * @return
	 */
	public PeriodAmount existRecord(String periodId, String goodsId, String goodsSpecId){
		return periodAmountRepo.findTopBy(periodId, goodsId, goodsSpecId);
	}


	@Override
	public CrudRepository<PeriodAmount, String> getRepository() {
		return periodAmountRepo;
	}

	@Override
	public PeriodAmountVo getVo(PeriodAmount entity) {

		if(entity==null)return null;

		PeriodAmountVo vo = VoFactory.createValue(PeriodAmountVo.class, entity);

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

		if(entity.getAccountUnit()!=null){
			vo.setAccountUnitId(entity.getAccountUnit().getFid());
			vo.setAccountUnitName(entity.getAccountUnit().getName());
		}

		return vo;
	}

	/**
	 * 根据会计期间ID，货品ID，属性ID查询库存总仓记录
	 * @param vo
	 * @param pageParamater
	 * @return
	 */
	public Page<PeriodAmountVo> query(PeriodAmountVo vo, PageParamater pageParamater){

		Sort sort = new Sort(Sort.Direction.ASC, "goods.code");
		PageRequest request = getPageRequest(pageParamater, sort);
		Page<PeriodAmount> page = periodAmountRepo.queryBy(vo, request);
		return getPageVos(page, request);
	}
}
