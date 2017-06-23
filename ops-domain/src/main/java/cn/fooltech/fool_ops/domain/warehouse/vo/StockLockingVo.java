package cn.fooltech.fool_ops.domain.warehouse.vo;

import java.io.Serializable;

/**
 * <p>表单传输对象- 库存锁定</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2016年6月3日
 */
public class StockLockingVo implements Serializable {

    private static final long serialVersionUID = -3253499607620538344L;

    /**
     * 用户账号
     */
    private String userCode;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 库存锁定时，是否强制出库<br>
     * 1- 是，0- 否
     */
    private Integer coerceOutStock = 0;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getCoerceOutStock() {
        return coerceOutStock;
    }

    public void setCoerceOutStock(Integer coerceOutStock) {
        this.coerceOutStock = coerceOutStock;
    }
}
