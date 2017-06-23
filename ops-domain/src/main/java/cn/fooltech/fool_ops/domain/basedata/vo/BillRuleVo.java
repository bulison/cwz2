package cn.fooltech.fool_ops.domain.basedata.vo;

import java.io.Serializable;

/**
 * <p>表单传输对象- 单据单号生成规则</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2015年9月14日
 */
public class BillRuleVo implements Serializable {

    private static final long serialVersionUID = 6938773220081348947L;

    /**
     * ID
     */
    private String fid;

    /**
     * 单据名称
     */
    private String billName;

    /**
     * 单据类型
     */
    private Integer billType;

    /**
     * 规则类型
     */
    private Integer ruleType;

    /**
     * 前缀
     */
    private String prefix = "";

    /**
     * 年月日
     */
    private String date;

    /**
     * 序号长度
     */
    private Integer serial;

    /**
     * 现时单号
     */
    private String lazyCode;

    /**
     * 修改时间
     */
    private String updateTime;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getBillName() {
        return billName;
    }

    public void setBillName(String billName) {
        this.billName = billName;
    }

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public Integer getRuleType() {
        return ruleType;
    }

    public void setRuleType(Integer ruleType) {
        this.ruleType = ruleType;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public String getLazyCode() {
        return lazyCode;
    }

    public void setLazyCode(String lazyCode) {
        this.lazyCode = lazyCode;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

}
