package cn.fooltech.fool_ops.domain.common.excelProcessor.impl;

import org.apache.commons.lang3.StringUtils;

import cn.fooltech.fool_ops.domain.common.excelProcessor.Processor;


/**
 * <p>性别处理器</p>
 * @author xjh
 * @version 1.0
 * @date 2015年9月23日
 */
public class SexProcessor implements Processor<Short,String> {

	final String MALE = "男";
	final String FEMALE = "女";
	
	/**
	 * 正向处理
	* @param key
	* @return 
	* @see com.gever.ops.web.excelProcessor.Processor#process(java.lang.Object)
	 */
	@Override
	public String process(Short key) {
		String sex = "";
		if(key==null)return sex;
		switch(key){
			case 1:sex=MALE;break;
			case 2:sex=FEMALE;break;
			default :;break;
		}
		return sex;
	}

	/**
	 * 反向处理
	* @param value
	* @return 
	* @see com.gever.ops.web.excelProcessor.Processor#reprocess(java.lang.Object)
	 */
	@Override
	public Short reprocess(String value) {
		short sex = 1;
		if(StringUtils.isNotBlank(value)){
			if(value.trim().equals(FEMALE)){
				sex = 2;
			}
		}
		return sex;
	}

}
