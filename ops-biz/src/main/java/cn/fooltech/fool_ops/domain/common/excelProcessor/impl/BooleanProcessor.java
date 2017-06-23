package cn.fooltech.fool_ops.domain.common.excelProcessor.impl;

import cn.fooltech.fool_ops.domain.common.excelProcessor.Processor;

public class BooleanProcessor implements Processor<Boolean, String> {

	private final String YES = "是";
	private final String NO = "否";
	
	@Override
	public String process(Boolean key) {
		if(key!=null && key){
			return YES;
		}
		return NO;
	}

	@Override
	public Boolean reprocess(String value) {
		if(YES.equals(value)){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

}
