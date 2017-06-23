package cn.fooltech.fool_ops.domain.common.excelProcessor.impl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;

import cn.fooltech.fool_ops.domain.common.excelProcessor.Processor;
import cn.fooltech.fool_ops.utils.DateUtilTools;


public class DateTimeProcessor implements Processor<Date, String> {

	@Override
	public String process(Date key) {
		return DateUtilTools.date2String(key, DateUtilTools.DATE_PATTERN_YYYY_MM_DDHHMMSS);
	}

	/**
	 * Excel特殊处理
	 * @param value
	 * @return 
	 * @see com.gever.ops.web.excelProcessor.Processor#reprocess(java.lang.Object)
	 */
	@Override
	public Date reprocess(String value) {
		if(StringUtils.isBlank(value))return null;
		return HSSFDateUtil.getJavaDate(Double.valueOf(value));
	}

}
