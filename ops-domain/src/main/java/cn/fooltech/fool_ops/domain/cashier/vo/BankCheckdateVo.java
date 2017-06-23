package cn.fooltech.fool_ops.domain.cashier.vo;

import java.io.Serializable;

/**
 * <p>表单传输对象 - 出纳-轧账日期</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015-12-14 15:50:04
 */
public class BankCheckdateVo implements Serializable {

    private static final long serialVersionUID = 6238916802865555886L;
    private String checkDate;//扎帐日期
    private String createTime;
    private String updateTime;
    private String fid;

    /**
     * 创建人
     */
    private String creatorId;
    private String creatorName;

    /**
     * 账套
     */
    private String fiscalAccountId;
    private String fiscalAccountName;

    public String getCheckDate() {
        return this.checkDate;
    }

    public void setCheckDate(String checkDate) {
        this.checkDate = checkDate;
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

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getFiscalAccountId() {
        return fiscalAccountId;
    }

    public void setFiscalAccountId(String fiscalAccountId) {
        this.fiscalAccountId = fiscalAccountId;
    }

    public String getFiscalAccountName() {
        return fiscalAccountName;
    }

    public void setFiscalAccountName(String fiscalAccountName) {
        this.fiscalAccountName = fiscalAccountName;
    }

}
