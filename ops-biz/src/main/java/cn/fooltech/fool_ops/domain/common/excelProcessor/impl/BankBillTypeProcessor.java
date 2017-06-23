package cn.fooltech.fool_ops.domain.common.excelProcessor.impl;

import cn.fooltech.fool_ops.domain.common.excelProcessor.Processor;

/**
 * 银行单据类型
 * @author cwz
 * @date 2016-8-15
 */
public class BankBillTypeProcessor implements Processor<Integer, String> {

//	1--期初企业未到账
//	2--期初银行未到账
//	3--银行日记账
//	4--银行对账单
//	5--现金日记账
	public static final String TYPE_QCQYWDZ_STR = "期初企业未到账";
	public static final String TYPE_QCYHWDZ_STR = "期初银行未到账";
	public static final String TYPE_YHRJZ_STR = "银行日记账";
	public static final String TYPE_YHDZD_STR = "银行对账单";
	public static final String TYPE_XJRJZ_STR = "现金日记账";
	
	public static final int TYPE_QCQYWDZ = 1;
	public static final int TYPE_QCYHWDZ = 2;
	public static final int TYPE_YHRJZ = 3;
	public static final int TYPE_YHDZD = 4;
	public static final int TYPE_XJRJZ = 5;

	@Override
	public String process(Integer key) {
		if(key==null)return "";
		switch (key){
		case TYPE_QCQYWDZ:return TYPE_QCQYWDZ_STR;
		case TYPE_QCYHWDZ:return TYPE_QCYHWDZ_STR;
		case TYPE_YHRJZ:return TYPE_YHRJZ_STR;
		case TYPE_YHDZD:return TYPE_YHDZD_STR;
		case TYPE_XJRJZ:return TYPE_XJRJZ_STR;
		}
		return "";
	}

	@Override
	public Integer reprocess(String value) {
		if(TYPE_QCQYWDZ_STR.equals(value)){
			return TYPE_QCQYWDZ;
		}else if(TYPE_QCYHWDZ_STR.equals(value)){
			return TYPE_QCYHWDZ;
		}else if(TYPE_YHRJZ_STR.equals(value)){
			return TYPE_YHRJZ;
		}else if(TYPE_YHDZD_STR.equals(value)){
			return TYPE_YHDZD;
		}else if(TYPE_XJRJZ_STR.equals(value)){
			return TYPE_XJRJZ;
		}
		return null;
	}

	


}
