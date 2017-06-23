package cn.fooltech.fool_ops.domain.common.excelProcessor.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;

import cn.fooltech.fool_ops.domain.common.excelProcessor.Processor;
import cn.fooltech.fool_ops.utils.DateUtilTools;


public class DateStringProcessor implements Processor<String, String> {

	
	
	@Override
	public String process(String key) {
		return key;
	}

	/**
	 * Excel特殊处理
	 * @param value
	 * @return 
	 * @see cn.fooltech.fool_ops.domain.common.excelProcessor.Processor#reprocess(java.lang.Object)
	 */
	@Override
	public String reprocess(String value) {
		if(StringUtils.isBlank(value))return null;
		Date date = HSSFDateUtil.getJavaDate(Double.valueOf(value));
		SimpleDateFormat formatter = new SimpleDateFormat(DateUtilTools.DATE_PATTERN_YYYY_MM_DD);
		return formatter.format(date);
	}

}
