package cn.fooltech.fool_ops.domain.common.excelProcessor.impl;

import org.apache.commons.lang3.StringUtils;

import cn.fooltech.fool_ops.domain.common.excelProcessor.Processor;


/**
 * <p>性别处理器</p>
 * @author xjh
 * @version 1.0
 * @date 2015年9月23日
 */
public class BankTypeProcessor implements Processor<Integer,String> {

	final String XJ = "现金";
	final String YH = "银行";
	
	/**
	 * 正向处理
	* @param key
	* @return 
	* @see com.gever.ops.web.excelProcessor.Processor#process(java.lang.Object)
	 */
	@Override
	public String process(Integer key) {
		String type = "";
		if(key==null)return type;
		switch(key){
			case 1:type=XJ;break;
			case 2:type=YH;break;
			default :;break;
		}
		return type;
	}

	/**
	 * 反向处理
	* @param value
	* @return 
	* @see com.gever.ops.web.excelProcessor.Processor#reprocess(java.lang.Object)
	 */
	@Override
	public Integer reprocess(String value) {
		if(StringUtils.isNotBlank(value)){
			if(value.trim().equals(YH)){
				return 2;
			}else if(value.trim().equals(XJ)){
				return 1;
			}
		}
		return null;
	}

}
