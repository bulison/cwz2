package cn.fooltech.fool_ops.domain.warehouse.service;

import java.math.BigDecimal;
import java.util.*;

import cn.fooltech.fool_ops.domain.basedata.entity.Goods;
import cn.fooltech.fool_ops.domain.warehouse.entity.*;
import cn.fooltech.fool_ops.domain.warehouse.strategy.BillStrategy;
import cn.fooltech.fool_ops.domain.warehouse.strategy.CalMethod;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;


/**
 * <p>仓库单据审核工具类</p>
 * @author rqh
 * @version 1.0
 * @date 2015年9月30日
 */
@Service
public class AuditService {
	
	private Logger logger = LoggerFactory.getLogger(AuditService.class);
	
	/**
	 * 进库记录服务类
	 */
	@Autowired
	private InStorageService inStorageService;
	
	/**
	 * 出库记录服务类
	 */
	@Autowired
	private OutStorageService outStorageService;
	
	/**
	 * 即时分仓服务类
	 */
	@Autowired
	private StockStoreService stockStoreService;

	@Autowired
	private Set<BillStrategy> strategies;

	/**
	 * 期间总库存金额表服务类
	 */
	@Autowired
	protected PeriodAmountService periodAmountService;

	/**
	 * 期间分仓库存服务类
	 */
	@Autowired
	protected PeriodStockAmountService periodStockAmountService;
	@Autowired
	protected WarehouseBillDetailService warehouseBillDetailService;
	//=========================================

	/**
	 * 通过审核
	 * @param bill
	 * @param details
	 */
	
	public void passAduit(WarehouseBill bill, List<WarehouseBillDetail> details){
		int billType = bill.getBillType();
		BillStrategy startegy = getSupportStrategy(billType);

		if(startegy == null) {
			return;
			//throw new RuntimeException("没有该类型单据的策略！");
		}
		for (WarehouseBillDetail detail : details) {

			if (detail.getGoods().getAccountFlag()
					.equals(Goods.ACCOUNT_FLAG_YES)) {
				//审核
				warehouseBillDetailService.processDetail(detail,bill);
				//总仓
                List<CalMethod> auditTotalMethods = startegy.getAuditTotalStrategy();
				PeriodAmount total = periodAmountService.process(auditTotalMethods, bill, detail);

				//分仓
                List<CalMethod> methods1 = startegy.getAuditInBranchStrategy();
				periodStockAmountService.processIn(methods1, bill, detail, total);

                List<CalMethod> methods2 = startegy.getAuditOutBranchStrategy();
				if (methods2 != null && methods2.size() > 0) {
					periodStockAmountService.processOut(methods2, bill, detail, total);
				}

				//即时分仓
                List<CalMethod> methods3 = startegy.getAuditInRealTimeStrategy();
				stockStoreService.processIn(methods3, bill, detail, total);

                List<CalMethod> methods4 = startegy.getAuditOutRealTimeStrategy();
				if (methods4 != null && methods4.size() > 0) {
					stockStoreService.processOut(methods4, bill, detail, total);
				}
			}
		}
	}

	/**
	 * 作废
	 * @param bill
	 * @param details
	 */
	public boolean cancleBill(WarehouseBill bill, List<WarehouseBillDetail> details){
		int billType = bill.getBillType();
		BillStrategy startegy = getSupportStrategy(billType);

		if(startegy == null) {
			return true;
			//throw new RuntimeException("没有该类型单据的策略！");
		}
		//判断是否可以作废
		for (WarehouseBillDetail detail : details) {
			if(!startegy.getJudgmentCancle()){
				if (detail.getGoods().getAccountFlag()
						.equals(Goods.ACCOUNT_FLAG_YES)) {
					if(!stockStoreService.JudgmentCancle(bill, detail)){;
					return false;
					}
				}
			}
		}
		for (WarehouseBillDetail detail : details) {

			if (detail.getGoods().getAccountFlag()
					.equals(Goods.ACCOUNT_FLAG_YES)) {

				//总仓
				List<CalMethod> auditTotalMethods = startegy.getCancleTotalStrategy();
				PeriodAmount total = periodAmountService.process(auditTotalMethods, bill, detail);

				List<CalMethod> methods1 = startegy.getCancleInBranchStrategy();
				periodStockAmountService.processIn(methods1, bill, detail, total);

				List<CalMethod> methods2 = startegy.getCancleOutBranchStrategy();
				if (methods2 != null && methods2.size() > 0) {
					periodStockAmountService.processOut(methods2, bill, detail, total);
				}

				List<CalMethod> methods3 = startegy.getCancleInRealTimeStrategy();
				stockStoreService.processIn(methods3, bill, detail, total);

				List<CalMethod> methods4 = startegy.getCancleOutRealTimeStrategy();
				if (methods4 != null && methods4.size() > 0) {
					stockStoreService.processOut(methods4, bill, detail, total);
				}
			}
		}
		return true;
	}

	/**
	 * 不同单据获取不同的策略
	 * @param billType
	 * @return
	 */
	public BillStrategy getSupportStrategy(int billType){
		for(BillStrategy strategy:strategies){
			if(strategy.isSupport(billType)){
				return strategy;
			}
		}
		return null;
	}




















	//========================================

	
	/**
	 * 判断单据类型是否出库(不处理盘点单和调仓单)
	 * @return
	 */
	public boolean isOutStorage(int billType){
		boolean flag = false;
		switch (billType){
		
		//采购退货
		case WarehouseBuilderCodeHelper.cgth:
			flag = true;
			break;
		
		//销售出库
		case WarehouseBuilderCodeHelper.xsch:
			flag = true;
			break;
		
		//生产领料
		case WarehouseBuilderCodeHelper.scll:
			flag = true;
			break;
		
		//成品退库
		case WarehouseBuilderCodeHelper.cptk:
			flag = true;
			break;
		
		//报损单
		case WarehouseBuilderCodeHelper.bsd:
			flag = true;
			break;
		}
			
		return flag;
	
	}
	
	/**
	 * 处理审核操作
	 * @param bill 仓库单据
	 * @param details 仓库单据明细
	 * @return
	 */
	public List<OutStorage> handleAudit(WarehouseBill bill, List<WarehouseBillDetail> details){
		if(CollectionUtils.isEmpty(details)) return Collections.emptyList();
		
		List<OutStorage> outStorages = new ArrayList<OutStorage>(); //审核之后的出库记录
		for(WarehouseBillDetail detail : details){
			switch (bill.getBillType()){
				//期初库存
				case WarehouseBuilderCodeHelper.qckc:
					inStorageService.addInStorage(detail);
					break; 
				
				//采购入库
				case WarehouseBuilderCodeHelper.cgrk:
					inStorageService.addInStorage(detail);
					break;
				
				//采购退货
				case WarehouseBuilderCodeHelper.cgth:
					outStorageService.addOutStorage(detail, outStorages);
					break;
				
				//销售出库
				case WarehouseBuilderCodeHelper.xsch:
					outStorageService.addOutStorage(detail, outStorages);
					break;
				
				//销售退货
				case WarehouseBuilderCodeHelper.xsth:
					inStorageService.addInStorage(detail);
					break;
					
				//生产领料
				case WarehouseBuilderCodeHelper.scll:
					outStorageService.addOutStorage(detail, outStorages);
					break;
				
				//生产退料
				case WarehouseBuilderCodeHelper.sctl:
					inStorageService.addInStorage(detail);
					break;
				
				//成品入库
				case WarehouseBuilderCodeHelper.cprk:
					inStorageService.addInStorage(detail);
					break;
					
				//成品退库
				case WarehouseBuilderCodeHelper.cptk:
					outStorageService.addOutStorage(detail, outStorages);
					break;
				
				//报损单
				case WarehouseBuilderCodeHelper.bsd:
					outStorageService.addOutStorage(detail, outStorages);
					break;
				
				//盘点单（+-）
				case WarehouseBuilderCodeHelper.pdd:
					if(isPositive(detail.getAccountQuentity())){
						inStorageService.addInStorage(detail);
					}
					else{
						outStorageService.addOutStorage(detail, outStorages);
					}
					break;
				
				//调仓单
				case WarehouseBuilderCodeHelper.dcd:
					//不处理
					break;
			}
		}
		return outStorages;
	}
	
	/**
	 * 处理作废操作
	 * @param bill
	 * @param details
	 * @return
	 */
	public void cancel(WarehouseBill bill, List<WarehouseBillDetail> details){
		if(CollectionUtils.isEmpty(details))
			return;
		
		for(WarehouseBillDetail detail : details){
			switch (bill.getBillType()){
				//期初库存
				case WarehouseBuilderCodeHelper.qckc:
					inStorageService.deleteByBillDetail(detail);
					break;

				//采购入库
				case WarehouseBuilderCodeHelper.cgrk:
					inStorageService.deleteByBillDetail(detail);
					break;

				//采购退货
				case WarehouseBuilderCodeHelper.cgth:
					processReverseOut(detail);
					break;

				//销售出库
				case WarehouseBuilderCodeHelper.xsch:
					processReverseOut(detail);
					break;

				//销售退货
				case WarehouseBuilderCodeHelper.xsth:
					inStorageService.deleteByBillDetail(detail);
					break;

				//生产领料
				case WarehouseBuilderCodeHelper.scll:
					processReverseOut(detail);
					break;

				//生产退料
				case WarehouseBuilderCodeHelper.sctl:
					inStorageService.deleteByBillDetail(detail);
					break;

				//成品入库
				case WarehouseBuilderCodeHelper.cprk:
					inStorageService.deleteByBillDetail(detail);
					break;

				//成品退库
				case WarehouseBuilderCodeHelper.cptk:
					processReverseOut(detail);
					break;

				//报损单
				case WarehouseBuilderCodeHelper.bsd:
					processReverseOut(detail);
					break;

				//盘点单（+-）
				case WarehouseBuilderCodeHelper.pdd:
					if(isPositive(detail.getAccountQuentity())){
						inStorageService.deleteByBillDetail(detail);
					}
					else{
						processReverseOut(detail);
					}
					break;

				//调仓单
				case WarehouseBuilderCodeHelper.dcd:
					//不处理
					break;
			}
		}
	}
	
	/**
	 * 处理出库反操作
	 * @param billDetail
	 */
	public void processReverseOut(WarehouseBillDetail billDetail){
		List<OutStorage> list = outStorageService.getByBillDetail(billDetail.getFid());
		for(OutStorage outStorage : list){
			WarehouseBillDetail inDetail = outStorage.getBillDetailIn();
			InStorage inStorage = inStorageService.findTopByDeatilId(inDetail.getFid());
			
			BigDecimal totalOut = inStorage.getTotalOut().subtract(outStorage.getAccountQuentity());
			inStorage.setTotalOut(totalOut);
			inStorageService.save(inStorage);
			outStorageService.delete(outStorage);
		}
	}
	
	/**
	 * 校验是否可以执行入库反操作(判断之前的入库是否有出库操作)
	 * @param bill
	 * @return true 校验通过  false 校验不通过
	 */
	public boolean checkReverseIn(WarehouseBill bill, List<WarehouseBillDetail> details){
		for(WarehouseBillDetail detail : details){
			switch(bill.getBillType()){
				//期初库存
				case WarehouseBuilderCodeHelper.qckc:
			 	//采购入库
				case WarehouseBuilderCodeHelper.cgrk:
				//销售退货
				case WarehouseBuilderCodeHelper.xsth:
					if(outStorageService.countByBillDetail(detail.getFid()) > 0){
						return false;
					}
					
				//生产退料
				case WarehouseBuilderCodeHelper.sctl:
					if(outStorageService.countByBillDetail(detail.getFid()) > 0){
						return false;
					}
					
				//成品入库
				case WarehouseBuilderCodeHelper.cprk:
					if(outStorageService.countByBillDetail(detail.getFid()) > 0){
						return false;
					}
					
				//盘点单（+-）
				case WarehouseBuilderCodeHelper.pdd:
					if(isPositive(detail.getAccountQuentity()) && outStorageService.countByBillDetail(detail.getFid()) > 0){
						return false;
					}
			}
		}
		return true;
	}
	
	/**
	 * 审核时，如果是出库操作，则检查库存是否足够
	 * @return 库存不足的货品ID，货品属性ID集合
	 */
	public Map<String, Object> checkStock(WarehouseBill bill, List<WarehouseBillDetail> details){
		if(bill.getBillType() == WarehouseBuilderCodeHelper.cgth){
			//采购退货
			return checkAllStock(bill, details);
		}
		else if(bill.getBillType() == WarehouseBuilderCodeHelper.xsch){
			//销售出库
			return checkAllStock(bill, details);
		}
		else if(bill.getBillType() == WarehouseBuilderCodeHelper.bsd){
			//报损单
			return checkAllStock(bill, details);
		}
		else if(bill.getBillType() == WarehouseBuilderCodeHelper.scll){
			//生产领料
			return checkAllStock(bill, details);
		}
		else if(bill.getBillType() == WarehouseBuilderCodeHelper.cptk){
			//成品退库
			return checkAllStock(bill, details);
		}
		else if(bill.getBillType() == WarehouseBuilderCodeHelper.pdd){
			//盘点单（+-）
			return checkAllStock(bill, details);
		}
		else if(bill.getBillType() == WarehouseBuilderCodeHelper.dcd){
			//调仓单（+-）
			Organization org = bill.getOrg();
			FiscalAccount fiscalAccount = bill.getFiscalAccount();
			return checkStockStore(org.getFid(), fiscalAccount.getFid(), bill.getFid());
		}else if(bill.getBillType() == WarehouseBuilderCodeHelper.fhd){
			//发货单
			return checkAllStock(bill, details);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("isEnough", true);
		map.put("detail", new ArrayList<Object[]>(0));
		return map;
	}
	
	/**
	 * 检查入库库存、分仓库存是否足够
	 */
	private Map<String, Object> checkAllStock(WarehouseBill bill, List<WarehouseBillDetail> details){
		//入库
		/*List<Object[]> list = inStorageService.checkStock(details);
		if(CollectionUtils.isNotEmpty(list)){
			logger.debug("入库库存不足!");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("isEnough", false);
			map.put("detail", list);
			return map;
		}*/
		
		//即时分仓
		Organization org = bill.getOrg();
		FiscalAccount fiscalAccountId = bill.getFiscalAccount();
		Map<String, Object> map = checkStockStore(org.getFid(), fiscalAccountId.getFid(), bill.getFid());
		return map;
	}
	
	/**
	 * 检查即时分仓库存是否足够
	 * @param orgId 机构ID
	 * @param fiscalAccountId 财务账套ID
	 * @param billId 单据ID
	 * @return
	 */
	private Map<String, Object> checkStockStore(String orgId, String fiscalAccountId, String billId){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("isEnough", true);
		resultMap.put("detail", new ArrayList<Object[]>(0));
		
		List<Object[]> list = stockStoreService.checkStock(orgId, fiscalAccountId, billId);
		if(CollectionUtils.isNotEmpty(list)){
			logger.debug("即时分仓库存不足!");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("isEnough", false);
			map.put("detail", list);
			return map;
		}
		return resultMap;
	}
	
	
	/**
	 * 判断是否是正数
	 * @param num
	 * @return true 正数  false 负数(0也当正数处理)
	 */
	private boolean isPositive(BigDecimal num){
		BigDecimal zero = new BigDecimal(0);
		if(num == null ){
			throw new RuntimeException("参数不正确!");
		}
		return num.compareTo(zero) >= 0;
	}
	
	/**
	 * 获取勾兑的标识数
	 */
	public BigDecimal getBillTag(WarehouseBill bill){
		switch (bill.getBillType()){
			case WarehouseBuilderCodeHelper.cgth:return new BigDecimal(-1);
			case WarehouseBuilderCodeHelper.xsth:return new BigDecimal(-1);
		}
		return new BigDecimal(1);
	}
	
}
