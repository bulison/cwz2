package cn.fooltech.fool_ops.domain.common.excelProcessor.impl;

import org.apache.commons.lang3.StringUtils;

import cn.fooltech.fool_ops.domain.common.excelProcessor.Processor;


/**
 * <p>科目关联类型--处理器</p>
 * @author xjh
 * @version 1.0
 * @date 2015年12月1日
 */
public class SubjectRelationTypeProcessor implements Processor<Integer,String> {

	//1银行、2供应商、3销售商、4部门、5职员、6仓库、7项目，8货品
	final int RELATION_BANK = 1;
	final int RELATION_SUPPLIER = 2;
	final int RELATION_CUSTOMER = 3;
	final int RELATION_DEPARTMENT = 4;
	final int RELATION_MEMBER = 5;
	final int RELATION_WAREHOUSE = 6;
	final int RELATION_PROJECT = 7;
	final int RELATION_GOODS = 8;
	
	final String RELATION_BANK_STR = "银行";
	final String RELATION_SUPPLIER_STR = "供应商";
	final String RELATION_CUSTOMER_STR = "销售商";
	final String RELATION_DEPARTMENT_STR = "部门";
	final String RELATION_MEMBER_STR = "职员";
	final String RELATION_WAREHOUSE_STR = "仓库";
	final String RELATION_PROJECT_STR = "项目";
	final String RELATION_GOODS_STR = "货品";
	
	/**
	 * 正向处理
	* @param key
	* @return 
	* @see com.gever.ops.web.excelProcessor.Processor#process(java.lang.Object)
	 */
	@Override
	public String process(Integer key) {
		String val = "";
		if(key==null)return val;
		switch(key){
			case RELATION_BANK:val=RELATION_BANK_STR;break;
			case RELATION_SUPPLIER:val=RELATION_SUPPLIER_STR;break;
			case RELATION_CUSTOMER:val=RELATION_CUSTOMER_STR;break;
			case RELATION_DEPARTMENT:val=RELATION_DEPARTMENT_STR;break;
			case RELATION_MEMBER:val=RELATION_MEMBER_STR;break;
			case RELATION_WAREHOUSE:val=RELATION_WAREHOUSE_STR;break;
			case RELATION_PROJECT:val=RELATION_PROJECT_STR;break;
			case RELATION_GOODS:val=RELATION_GOODS_STR;break;
			default :;break;
		}
		return val;
	}

	/**
	 * 反向处理
	* @param value
	* @return 
	* @see com.gever.ops.web.excelProcessor.Processor#reprocess(java.lang.Object)
	 */
	@Override
	public Integer reprocess(String value) {
		Integer key = null;
		if(StringUtils.isNotBlank(value)){
			if(value.trim().equals(RELATION_BANK_STR)){
				key = RELATION_BANK;
			}else if(value.trim().equals(RELATION_SUPPLIER_STR)){
				key = RELATION_SUPPLIER;
			}else if(value.trim().equals(RELATION_CUSTOMER_STR)){
				key = RELATION_CUSTOMER;
			}else if(value.trim().equals(RELATION_DEPARTMENT_STR)){
				key = RELATION_DEPARTMENT;
			}else if(value.trim().equals(RELATION_MEMBER_STR)){
				key = RELATION_MEMBER;
			}else if(value.trim().equals(RELATION_WAREHOUSE_STR)){
				key = RELATION_WAREHOUSE;
			}else if(value.trim().equals(RELATION_PROJECT_STR)){
				key = RELATION_PROJECT;
			}else if(value.trim().equals(RELATION_GOODS_STR)){
				key = RELATION_GOODS;
			}
		}
		return key;
	}

}
