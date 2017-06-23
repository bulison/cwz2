package cn.fooltech.fool_ops.config;

import cn.fooltech.fool_ops.domain.sysman.entity.UserAttr;

import java.math.BigDecimal;

public class Constants {

	public static final String DEFAULT_HEAD_PORTRAIT = "resources/images/user.png";
	public static final String DEFAULT_SECURITY_EXCEPTION_KEY = "OPS_SECURITY_EXCEPTION";
	public static final String DEFAULT_FISCAL_ACCOUNT_NAME = "FiscalAccountName";

	public static final int REDIS_SESSION_TIME_OUT=60*60*24*30*12;
	public static final String REDIS_SESSION_USER_KEY="REDIS_SESSION_USER_KEY_";

	public static final String NEW_USER_DEFAULT_PASSWORD = "123456";

	public static final String RESOURCE_ROOT = "ResourceMaintenance";
	public static final String WORK_BOOK = "WORK_BOOK";
	public final static int SHOW = 1;
	public final static int NOTSHOW = 0;

	public final static short SENABLE = 1;
	public final static short SNOTENABLE = 0;

	public final static String LIMIT = "LIMIT";

	public static final String DEFAULT_INPUT_TYPE = UserAttr.INPUT_TYPE_PINYIN;
	public static final String DEFAULT_LOCAL_CACHE = UserAttr.LOCAL_CACHE_ENABLE;

	/**
	 * 模糊搜索结果集默认大小
	 */
	public static final int VAGUE_SEARCH_SIZE = 10;

	/**
	 * 仓库单据核价权限
	 */
	public static final String PERMISSION_PRICING = "ops_warehouse_pricing";

	/**
	 * 采购一级价权限
	 */
	public static final String PERMISSION_PURCHASE_FIRST_PRICE =  "ops_purchase_first_price";

	/**
	 * 采购二级价权限
	 */
	public static final String PERMISSION_PURCHASE_SECOND_PRICE = "ops_purchase_second_price";

	/**
	 * 0价采购权限
	 */
	public static final String PERMISSION_PURCHASE_ZERO_PRICE = "ops_purchase_zero_price";

	/**
	 * 销售一级价权限
	 */
	public static final String PERMISSION_SELL_FIRST_PRICE = "ops_sell_first_price";

	/**
	 * 销售二级价格权限
	 */
	public static final String PERMISSION_SELL_SECOND_PRICE = "ops_sell_second_price";

	/**
	 * 0价销售权限
	 */
	public static final String PERMISSION_SELL_ZERO_PRICE = "ops_sell_zero_price";

	/**
	 * 库存解锁权限
	 */
	public static final String PERMISSION_STOCK_UNLOCK = "ops_warehouse_unlock";

	public static final String ROOT = "ROOT";

}
