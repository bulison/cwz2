package cn.fooltech.fool_ops.domain.common.excelProcessor.impl;

import cn.fooltech.fool_ops.domain.common.excelProcessor.Processor;

/**
 * 科目类型
 * @author xjh
 *
 */
public class SubjectTypeProcessor implements Processor<Integer, String> {

	//资产、负债、共同、所有者权益、成本、损益
	public static final String TYPE_ZC_STR = "资产";
	public static final String TYPE_FZ_STR = "负债";
	public static final String TYPE_GT_STR = "共同";
	public static final String TYPE_SYZ_STR = "所有者权益";
	public static final String TYPE_CB_STR = "成本";
	public static final String TYPE_SY_STR = "损益";
	
	public static final int TYPE_ZC = 1;
	public static final int TYPE_FZ = 2;
	public static final int TYPE_GT = 3;
	public static final int TYPE_SYZ = 4;
	public static final int TYPE_CB = 5;
	public static final int TYPE_SY = 6;
	
	@Override
	public String process(Integer key) {
		if(key==null)return "";
		switch (key){
		case TYPE_ZC:return TYPE_ZC_STR;
		case TYPE_FZ:return TYPE_FZ_STR;
		case TYPE_GT:return TYPE_GT_STR;
		case TYPE_SYZ:return TYPE_SYZ_STR;
		case TYPE_CB:return TYPE_CB_STR;
		case TYPE_SY:return TYPE_SY_STR;
		}
		return "";
	}

	@Override
	public Integer reprocess(String value) {
		if(TYPE_ZC_STR.equals(value)){
			return TYPE_ZC;
		}else if(TYPE_FZ_STR.equals(value)){
			return TYPE_FZ;
		}else if(TYPE_GT_STR.equals(value)){
			return TYPE_GT;
		}else if(TYPE_SYZ_STR.equals(value)){
			return TYPE_SYZ;
		}else if(TYPE_CB_STR.equals(value)){
			return TYPE_CB;
		}else if(TYPE_SY_STR.equals(value)){
			return TYPE_SY;
		}
		return null;
	}

}
