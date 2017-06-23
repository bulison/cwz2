package cn.fooltech.fool_ops.domain.payment.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;

import cn.fooltech.fool_ops.domain.cost.entity.CostBill;
import cn.fooltech.fool_ops.domain.payment.entity.PaymentBill;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;


public interface CheckFilter {

	/**
	 * 查询未勾对单据，添加过滤
	 */
	public default void addCheckFilter(PaymentBill bill, int checkBillType, List<String> excludeIds, 
			List<Predicate> predicates, CriteriaBuilder builder, Root<?> root){
		//------------------------------收款单----------------------------
		if(bill.getBillType()==PaymentBill.TYPE_INCOME){
			//情况1
			if(bill.getCustomer()!=null && bill.getAmount().compareTo(BigDecimal.ZERO)>=0){
				if(checkBillType==WarehouseBuilderCodeHelper.xsfld){
					//销售返利单:只显示对公的销售返利，且销售返利单的客户与收款单的客户一致
					predicates.add(builder.equal(root.get("toPublic"), PaymentBill.TO_PUBLIC));
					predicates.add(builder.equal(root.get("customer").get("fid"), bill.getCustomer().getFid()));
				}else if(checkBillType==WarehouseBuilderCodeHelper.fyd){
					//费用单：只显示费用标识不为0，且费用单往来单位与收款单的客户一致的单据
					predicates.add(builder.notEqual(root.get("expenseType"), CostBill.EXPENSE_TYPE_COMMON));
					predicates.add(builder.equal(root.get("csv").get("fid"), bill.getCustomer().getFid()));
				}else if(checkBillType==WarehouseBuilderCodeHelper.skd){
					//收款单：只显示金额小于0，且两张收款单的客户一致的单据
					predicates.add(builder.lessThan(root.get("amount"), BigDecimal.ZERO));
					predicates.add(builder.equal(root.get("customer").get("fid"), bill.getCustomer().getFid()));
				}else if(checkBillType==WarehouseBuilderCodeHelper.xsch
						||checkBillType==WarehouseBuilderCodeHelper.xsth
						||checkBillType==WarehouseBuilderCodeHelper.xsfp
						||checkBillType==WarehouseBuilderCodeHelper.qcys){
					predicates.add(builder.equal(root.get("customer").get("fid"), bill.getCustomer().getFid()));
				}
			//情况2
			}else if(bill.getCustomer()!=null && bill.getAmount().compareTo(BigDecimal.ZERO)<0){
				if(checkBillType==WarehouseBuilderCodeHelper.xsfld){
					//销售返利单:只显示对公的销售返利，且销售返利单的客户与收款单的客户一致
					predicates.add(builder.equal(root.get("toPublic"), PaymentBill.TO_PUBLIC));
					predicates.add(builder.equal(root.get("customer").get("fid"), bill.getCustomer().getFid()));
				
				}else if(checkBillType==WarehouseBuilderCodeHelper.fyd){
					//费用单：只显示费用标识为2，且费用单往来单位与收款单的客户一致的单据
					predicates.add(builder.equal(root.get("expenseType"), CostBill.EXPENSE_TYPE_SUBTRACT));
					predicates.add(builder.equal(root.get("csv").get("fid"), bill.getCustomer().getFid()));
					
				}else if(checkBillType==WarehouseBuilderCodeHelper.skd){
					//收款单：只显示金额大于0，且两张收款单的客户一致的单据
					predicates.add(builder.greaterThan(root.get("amount"), BigDecimal.ZERO));
					predicates.add(builder.equal(root.get("customer").get("fid"), bill.getCustomer().getFid()));
					
				}else if(checkBillType==WarehouseBuilderCodeHelper.xsch||checkBillType==WarehouseBuilderCodeHelper.xsth){
					predicates.add(builder.equal(root.get("customer").get("fid"), bill.getCustomer().getFid()));
				}
			//情况3
			}else if(bill.getCustomer()==null && bill.getAmount().compareTo(BigDecimal.ZERO)>0){
				if(checkBillType==WarehouseBuilderCodeHelper.fyd){
					//费用单：只显示费用标识为0，且费用金额小于0的单据
					predicates.add(builder.equal(root.get("expenseType"), CostBill.EXPENSE_TYPE_COMMON));
					predicates.add(builder.lessThan(root.get("freeAmount"), BigDecimal.ZERO));
					
				}else if(checkBillType==WarehouseBuilderCodeHelper.skd){
					//收款单：只显示金额小于0，且两张收款单的客户都为空的单据
					predicates.add(builder.lessThan(root.get("amount"), BigDecimal.ZERO));
					predicates.add(builder.isNull(root.get("customer")));
				}
			}
			//情况4
			else if(bill.getCustomer()==null && bill.getAmount().compareTo(BigDecimal.ZERO)<0){
				if(checkBillType==WarehouseBuilderCodeHelper.xsfld){
					//销售返利单：只显示对私的销售返利
					predicates.add(builder.equal(root.get("toPublic"), PaymentBill.TO_PRIVATE));
				}else if(checkBillType==WarehouseBuilderCodeHelper.skd){
					//收款单：只显示金额大于0，且两张收款单的客户都为空的单据
					predicates.add(builder.greaterThan(root.get("amount"), BigDecimal.ZERO));
					predicates.add(builder.isNull(root.get("customer")));
				}
			}
			
			//------------------------------付款单----------------------------
		}else if(bill.getBillType()==PaymentBill.TYPE_EXPEND){
			//情况1
			if(bill.getSupplier()!=null && bill.getAmount().compareTo(BigDecimal.ZERO)>=0){
				if(checkBillType==WarehouseBuilderCodeHelper.cgfld){
					//采购返利单：只显示采购返利单的供应商与付款单的供应商一致的单据
					predicates.add(builder.equal(root.get("supplier").get("fid"), bill.getSupplier().getFid()));
				
				}else if(checkBillType==WarehouseBuilderCodeHelper.fyd){
					//费用单：只显示费用标识不为0，且费用单往来单位与付款单的供应商一致的单据
					predicates.add(builder.notEqual(root.get("expenseType"), CostBill.EXPENSE_TYPE_COMMON));
					predicates.add(builder.equal(root.get("csv").get("fid"), bill.getSupplier().getFid()));
				
				}else if(checkBillType==WarehouseBuilderCodeHelper.fkd){
					//付款单：只显示金额小于0，且两张付款单的供应商一致的单据
					predicates.add(builder.lessThan(root.get("amount"), BigDecimal.ZERO));
					predicates.add(builder.equal(root.get("supplier").get("fid"), bill.getSupplier().getFid()));
				
				}else if(checkBillType==WarehouseBuilderCodeHelper.cgrk
						||checkBillType==WarehouseBuilderCodeHelper.cgth
						||checkBillType==WarehouseBuilderCodeHelper.cgfp
						||checkBillType==WarehouseBuilderCodeHelper.qcyf){
					predicates.add(builder.equal(root.get("supplier").get("fid"), bill.getSupplier().getFid()));
				
				}
			//情况2
			}else if(bill.getSupplier()!=null && bill.getAmount().compareTo(BigDecimal.ZERO)<0){
				if(checkBillType==WarehouseBuilderCodeHelper.cgfld){
					//采购返利单：只显示采购返利单的供应商与付款单的供应商一致的单据
					predicates.add(builder.equal(root.get("supplier").get("fid"), bill.getSupplier().getFid()));
					
				}else if(checkBillType==WarehouseBuilderCodeHelper.fyd){
					//费用单：只显示费用标识为2，且费用单往来单位与付款单的供应商一致的单据
					predicates.add(builder.equal(root.get("expenseType"), CostBill.EXPENSE_TYPE_SUBTRACT));
					predicates.add(builder.equal(root.get("csv").get("fid"), bill.getSupplier().getFid()));
					
				}else if(checkBillType==WarehouseBuilderCodeHelper.fkd){
					//付款单：只显示金额大于0，且两张付款单的供应商一致的单据
					predicates.add(builder.greaterThan(root.get("amount"), BigDecimal.ZERO));
					predicates.add(builder.equal(root.get("supplier").get("fid"), bill.getSupplier().getFid()));
				
				}else if(checkBillType==WarehouseBuilderCodeHelper.cgrk||checkBillType==WarehouseBuilderCodeHelper.cgth){
					predicates.add(builder.equal(root.get("supplier").get("fid"), bill.getSupplier().getFid()));
					
				}
			//情况3
			}else if(bill.getSupplier()==null && bill.getAmount().compareTo(BigDecimal.ZERO)>0){
				if(checkBillType==WarehouseBuilderCodeHelper.fyd){
					//费用单：只显示费用标识为0，且费用金额大于0的单据
					predicates.add(builder.equal(root.get("expenseType"), CostBill.EXPENSE_TYPE_COMMON));
					predicates.add(builder.greaterThan(root.get("freeAmount"), BigDecimal.ZERO));
				
				}else if(checkBillType==WarehouseBuilderCodeHelper.fkd){
					//付款单：只显示金额小于0，且两张付款单的供应商都为空的单据
					predicates.add(builder.lessThan(root.get("amount"), BigDecimal.ZERO));
					predicates.add(builder.isNull(root.get("supplier")));
				}
			}
			//情况4
			else if(bill.getSupplier()==null && bill.getAmount().compareTo(BigDecimal.ZERO)<0){
				if(checkBillType==WarehouseBuilderCodeHelper.fkd){
					//付款单：只显示金额大于0，且两张付款单的供应商都为空的单据
					predicates.add(builder.greaterThan(root.get("amount"), BigDecimal.ZERO));
					predicates.add(builder.isNull(root.get("supplier")));
					
				}
			}

		}

		//排除IDs
		if(excludeIds.size()>0){
			predicates.add(root.get("fid").in(excludeIds).not());
		}
	}
}
