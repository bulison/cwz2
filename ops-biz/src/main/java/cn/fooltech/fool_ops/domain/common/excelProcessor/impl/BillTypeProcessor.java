package cn.fooltech.fool_ops.domain.common.excelProcessor.impl;

import cn.fooltech.fool_ops.domain.common.excelProcessor.Processor;

/**
 * 单据类型
 * @author cwz
 * @version 1.0
 * @date 2016年7月25日
 * 
 * 
 */
public class BillTypeProcessor implements Processor<Integer, String> {

	public static final String TYPE_CGRKD_STR = "采购入库";
	public static final String TYPE_CGTHD_STR = "采购退货";
	public static final String TYPE_CGFP_STR = "采购发票";
	public static final String TYPE_PDD_STR = "盘点单";
	public static final String TYPE_DCD_STR = "调仓单";
	public static final String TYPE_BSD_STR = "报损单";
	public static final String TYPE_SCLLD_STR = "生产领料";
	public static final String TYPE_CPRKD_STR = "成品入库";
	public static final String TYPE_SCTLD_STR = "生产退料";
	public static final String TYPE_CPTKD_STR = "成品退库";
	public static final String TYPE_SSCKD_STR = "销售出货";
	public static final String TYPE_SSTKD_STR = "销售退货";
	public static final String TYPE_SSFP_STR = "销售发票";
	public static final String TYPE_SKD_STR = "收款单";
	public static final String TYPE_FKD_STR = "付款单";
	public static final String TYPE_FYD_STR = "费用单";
	public static final String TYPE_CGFLD_STR = "采购返利单";
	public static final String TYPE_SSFLD_STR = "销售返利单";

	public static final int TYPE_CGRKD = 11;
	public static final int TYPE_CGTHD = 12;
	public static final int TYPE_CGFP = 15;
	public static final int TYPE_PDD = 20;
	public static final int TYPE_DCD = 21;
	public static final int TYPE_BSD = 22;
	public static final int TYPE_SCLLD = 30;
	public static final int TYPE_CPRKD = 31;
	public static final int TYPE_SCTLD = 32;
	public static final int TYPE_CPTKD = 33;
	public static final int TYPE_SSCKD = 41;
	public static final int TYPE_SSTKD = 42;
	public static final int TYPE_SSFP = 44;
	public static final int TYPE_SKD = 51;
	public static final int TYPE_FKD = 52;
	public static final int TYPE_FYD = 53;
	public static final int TYPE_CGFLD = 55;
	public static final int TYPE_SSFLD = 56;

	@Override
	public String process(Integer key) {
		if (key == null)
			return "";
		switch (key) {
		case TYPE_CGRKD:
			return TYPE_CGRKD_STR;
		case TYPE_CGTHD:
			return TYPE_CGTHD_STR;
		case TYPE_CGFP:
			return TYPE_CGFP_STR;
		case TYPE_PDD:
			return TYPE_PDD_STR;
		case TYPE_DCD:
			return TYPE_DCD_STR;
		case TYPE_BSD:
			return TYPE_BSD_STR;
		case TYPE_SCLLD:
			return TYPE_SCLLD_STR;
		case TYPE_CPRKD:
			return TYPE_CPRKD_STR;
		case TYPE_SCTLD:
			return TYPE_SCTLD_STR;
		case TYPE_CPTKD:
			return TYPE_CPTKD_STR;
		case TYPE_SSCKD:
			return TYPE_SSCKD_STR;
		case TYPE_SSTKD:
			return TYPE_SSTKD_STR;
		case TYPE_SSFP:
			return TYPE_SSFP_STR;
		case TYPE_SKD:
			return TYPE_SKD_STR;
		case TYPE_FKD:
			return TYPE_FKD_STR;
		case TYPE_FYD:
			return TYPE_FYD_STR;
		case TYPE_CGFLD:
			return TYPE_CGFLD_STR;
		case TYPE_SSFLD:
			return TYPE_SSFLD_STR;
		}
		return "";
	}

	@Override
	public Integer reprocess(String value) {
		if (TYPE_CGRKD_STR.equals(value)) {
			return TYPE_CGRKD;
		} else if (TYPE_CGTHD_STR.equals(value)) {
			return TYPE_CGTHD;
		} else if (TYPE_CGFP_STR.equals(value)) {
			return TYPE_CGFP;
		} else if (TYPE_PDD_STR.equals(value)) {
			return TYPE_PDD;
		} else if (TYPE_DCD_STR.equals(value)) {
			return TYPE_DCD;
		} else if (TYPE_BSD_STR.equals(value)) {
			return TYPE_BSD;
		} else if (TYPE_SCLLD_STR.equals(value)) {
			return TYPE_SCLLD;
		} else if (TYPE_CPRKD_STR.equals(value)) {
			return TYPE_CPRKD;
		} else if (TYPE_SCTLD_STR.equals(value)) {
			return TYPE_SCTLD;
		} else if (TYPE_CPTKD_STR.equals(value)) {
			return TYPE_CPTKD;
		} else if (TYPE_SSCKD_STR.equals(value)) {
			return TYPE_SSCKD;
		} else if (TYPE_SSTKD_STR.equals(value)) {
			return TYPE_SSTKD;
		} else if (TYPE_SSFP_STR.equals(value)) {
			return TYPE_SSFP;
		} else if (TYPE_SKD_STR.equals(value)) {
			return TYPE_SKD;
		} else if (TYPE_FKD_STR.equals(value)) {
			return TYPE_FKD;
		} else if (TYPE_FYD_STR.equals(value)) {
			return TYPE_FYD;
		} else if (TYPE_CGFLD_STR.equals(value)) {
			return TYPE_CGFLD;
		} else if (TYPE_SSFLD_STR.equals(value)) {
			return TYPE_SSFLD;
		}

		return null;
	}

}
