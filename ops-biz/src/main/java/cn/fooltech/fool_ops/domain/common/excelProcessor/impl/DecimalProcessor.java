package cn.fooltech.fool_ops.domain.common.excelProcessor.impl;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import cn.fooltech.fool_ops.domain.common.excelProcessor.Processor;
import cn.fooltech.fool_ops.utils.NumberUtil;


/**
 * <p>价格格式转换器</p>
 * @author xjh
 * @version 1.0
 * @date 2015年11月4日
 */
public class DecimalProcessor implements Processor<BigDecimal, String> {

	@Override
	public String process(BigDecimal key) {
		return NumberUtil.stripTrailingZeros(key).toPlainString();
	}

	@Override
	public BigDecimal reprocess(String value) {
		if(StringUtils.isBlank(value))return null;
		return new BigDecimal(value);
	}


}
