package cn.fooltech.fool_ops.domain.common.excelProcessor.impl;

import cn.fooltech.fool_ops.domain.common.excelProcessor.Processor;

public class EnableProcessor implements Processor<Short, String> {

	private final String YES = "是";
	private final String NO = "否";
	
	private final short YES_VAL = 1;
	private final short NO_VAL = 0;
	
	@Override
	public String process(Short key) {
		if(key!=null && key==1){
			return YES;
		}
		return NO;
	}

	@Override
	public Short reprocess(String value) {
		if(YES.equals(value)){
			return YES_VAL;
		}
		return NO_VAL;
	}

}
