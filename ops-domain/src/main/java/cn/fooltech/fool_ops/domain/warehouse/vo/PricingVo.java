package cn.fooltech.fool_ops.domain.warehouse.vo;

import java.io.Serializable;

/**
 * <p>表单传输对象-核价</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2016年4月5日
 */
public class PricingVo implements Serializable {

    private static final long serialVersionUID = -203004274836165476L;

    /**
     * 仓储单据ID
     */
    private String billId;

    /**
     * 用户编号
     */
    private String userCode;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 核价明细信息(JSON数组字符串)
     */
    private String details;

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

}
