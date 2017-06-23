package cn.fooltech.fool_ops.domain.initialBank.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>表单传输对象 - 现金银行期初</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015-09-09 11:12:47
 */
public class InitialBankVo implements Serializable {

    private static final long serialVersionUID = 7152373062796829277L;

    private BigDecimal amount;//结余金额
    private String describe;//描述
    private String createTime;//创建时间
    private String updateTime;//更新时间戳
    private String fid;

    private String periodId;//会计期间ID
    private String periodName;//会计期间

    private String bankId;//银行ID
    private String bankName;//银行名称
    private String bankCode;//银行编号
    private String bankAccount;//银行账号
    private String searchKey;//搜索条件
    private Integer checkoutStatus;//会计期间是否已结账

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescribe() {
        return this.describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getFid() {
        return this.fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getPeriodId() {
        return periodId;
    }

    public void setPeriodId(String periodId) {
        this.periodId = periodId;
    }

    public String getPeriodName() {
        return periodName;
    }

    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public Integer getCheckoutStatus() {
        return checkoutStatus;
    }

    public void setCheckoutStatus(Integer checkoutStatus) {
        this.checkoutStatus = checkoutStatus;
    }


}
