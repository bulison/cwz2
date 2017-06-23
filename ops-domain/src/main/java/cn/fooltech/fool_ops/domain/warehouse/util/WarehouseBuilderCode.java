package cn.fooltech.fool_ops.domain.warehouse.util;

/**
 * <p>仓库单据网页服务builder编码</p>
 * @author rqh
 * @version 1.0
 * @date 2015年9月17日
 */
public enum WarehouseBuilderCode {
	
	base, //基础业务
	
	qckc, //期初库存
	
	cgdd, //采购订单
	
	cgrk, //采购入库
	
	cgth, //采购退货
	
	cgxjd, //采购询价单
	
	cgsqd,//采购申请单
	
	cgfp, //采购发票
	
	pdd, //盘点单
	
	dcd, //调仓单
	
	bsd, //报损单
	
	scll, //生产领料
	
	sctl, //生产退料
	
	cprk, //成品入库
	
	cptk, //成品退库
	
	xsdd, //销售订单
	
	xsch, //销售出货
	
	xsth, //销售退货
	
	xsfp, //销售发票
	
	xsbjd, //销售报价单
	
	scjhd,//生产计划单
	
	fysqd,//费用申请单
	
	cgfld,//采购返利单
	
	xsfld,//销售返利单
	
	skd,//收款单
	
	fkd,//付款单
	
	fyd,//费用单

	qcyf,//期初应付

	qcys,//期初应收

	fhd,//发货单

	shd,//收货单

	thbjd,//同行报价单

	cdfbjd,//场地费报价单

	ysfbjd,//运输费报价单
	
	zjjh;//资金计划

	public int parseInt(){
		Integer billType = WarehouseBuilderCodeHelper.getBillType(this);
		return billType; 
	}
}
