package cn.fooltech.fool_ops.domain.common.excelProcessor.impl;

import com.google.common.base.Strings;

import cn.fooltech.fool_ops.domain.common.excelProcessor.Processor;

/**
 * <p>红南对冲处理器</p>
 * @author cwz
 * @version 1.0
 * @date 2016年7月25日
 */
public class HedgeProcessor implements Processor<Integer,String> {

	final String HONG = "红";
	final String LAN = "蓝";
	
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
			case -1:type=HONG;break;
			case 1:type=LAN;break;
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
		Integer direction = null;
		if(!Strings.isNullOrEmpty(value)){
			if(value.trim().equals(HONG)){
				return -1;
			}else if(value.trim().equals(LAN)){
				return 1;
			}
		}
		return direction;
	}

}
