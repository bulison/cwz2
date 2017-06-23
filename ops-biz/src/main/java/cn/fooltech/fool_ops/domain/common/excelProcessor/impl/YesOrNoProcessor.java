package cn.fooltech.fool_ops.domain.common.excelProcessor.impl;

import org.apache.commons.lang3.StringUtils;

import cn.fooltech.fool_ops.domain.common.excelProcessor.Processor;


public class YesOrNoProcessor implements Processor<Integer, String> {

	private final String YES = "是";
	private final String NO = "否";
	
	private final int YES_VAL = 1;
	private final int NO_VAL = 0;
	
	@Override
	public String process(Integer key) {
		if(key!=null && key==1){
			return YES;
		}
		return NO;
	}

	@Override
	public Integer reprocess(String value) {
		if(StringUtils.isBlank(value))return NO_VAL;
		if(YES.equals(value.trim())){
			return YES_VAL;
		}
		return NO_VAL;
	}

}
