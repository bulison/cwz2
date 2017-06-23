package cn.fooltech.fool_ops.utils;

import java.io.Serializable;

/**
 * <p>系统错误码</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2016年2月18日
 */
public class ErrorCode implements Serializable {

    //业务异常
    public static final int ERROR_CODE = -1;
    //数据不存在
    public static final int NOT_EXIST_CODE = -2;
    //数据过时
    public static final int OUT_DATE_CODE = -3;
    //存在未过账或者未作废的凭证
    public static final int EXIST_UNPOSTED_UNCANCEL_VOUCHER = 101;
    //期间损益类科目的结余金额不为零
    public static final int BALANCE_OF_LOSS_SUBJECT_NOT_ZERO = 102;
    //仓储会计期间未启用
    public static final int STOCK_PERIOD_UN_USED = 103;
    //仓储会计期间已结账
    public static final int STOCK_PERIOD_CHECKED = 104;
    //仓储会计期间不存在
    public static final int STOCK_PERIOD_NOT_EXIST = 105;
    //财务会计期间未启用
    public static final int FISCAL_PERIOD_UN_USED = 106;
    //财务会计期间已结账
    public static final int FISCAL_PERIOD_CHECKED = 107;
    //财务会计期间不存在
    public static final int FISCAL_PERIOD_NOT_EXIST = 108;
    //仓储单据，需要库存解锁才能出库
    public static final int WAREHOUSE_NEED_UNLOCK = 201;
    private static final long serialVersionUID = 7402286904386660332L;

    //不存在模板
    public static final int FLOW_NOT_EXIST_TEMPLATE = 301;

    //不存在供应商
    public static final int FLOW_NOT_EXIST_SUPPLIER = 302;

    //不存在客户
    public static final int FLOW_NOT_EXIST_CUSTOMER = 303;

    //事件已生成过
    public static final int FLOW_ALREADY_GENED = 304;

}
