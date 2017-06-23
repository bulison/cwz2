package cn.fooltech.fool_ops.domain.common.excelProcessor.impl;

import org.apache.commons.lang3.StringUtils;

import cn.fooltech.fool_ops.domain.common.excelProcessor.Processor;


/**
 * <p>财务科目 借贷方向--处理器</p>
 * @author xjh
 * @version 1.0
 * @date 2015年12月1日
 */
public class FiscalDirectionProcessor implements Processor<Integer,String> {

	final String BORROW = "借方";
	final String LOAN = "贷方";
	
	/**
	 * 正向处理
	* @param key
	* @return 
	* @see com.gever.ops.web.excelProcessor.Processor#process(java.lang.Object)
	 */
	@Override
	public String process(Integer key) {
		String direction = "";
		if(key==null)return direction;
		switch(key){
			case 1:direction=BORROW;break;
			case -1:direction=LOAN;break;
			default :;break;
		}
		return direction;
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
		if(StringUtils.isNotBlank(value)){
			if(value.trim().equals(BORROW)){
				direction = 1;
			}else if(value.trim().equals(LOAN)){
				direction = -1;
			}
		}
		return direction;
	}

}
