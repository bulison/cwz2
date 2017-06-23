package cn.fooltech.fool_ops.domain.asset.vo;

import cn.fooltech.fool_ops.config.Constants;
import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>表单传输对象 - 固定资产计提</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015-12-28 14:24:15
 */
public class AssetDetailVo implements Serializable {

    private static final long serialVersionUID = -3828845334764992846L;
    private Integer type;//类型
    private String date;//日期
    private BigDecimal amount;//金额
    private String remark;//备注
    private String fid;
    private String assetId;//固定资产卡片ID

    /**
     * 起始日期
     */
    private String startDay;

    /**
     * 结束日期
     */
    private String endDay;

    /**
     * 资产编号
     */
    private String assetCode;

    /**
     * 资产名称
     */
    private String assetName;

    /**
     * 部门ID
     */
    private String deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 借方科目名称
     */
    private String debitSubject;

    /**
     * 贷方科目名称
     */
    private String creditSubject;

    /**
     * 折旧科目名称
     */
    private String depreciationSubject;

    /**
     * 凭证ID
     */
    private String voucherId;

    /**
     * 凭证字+凭证号
     */
    private String voucherWordNumber;

    /**
     * 是否展示已生成凭证的数据
     */
    private Integer showGened = Constants.SHOW;

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFid() {
        return this.fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getStartDay() {
        return startDay;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public String getEndDay() {
        return endDay;
    }

    public void setEndDay(String endDay) {
        this.endDay = endDay;
    }

    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDebitSubject() {
        return debitSubject;
    }

    public void setDebitSubject(String debitSubject) {
        this.debitSubject = debitSubject;
    }

    public String getCreditSubject() {
        return creditSubject;
    }

    public void setCreditSubject(String creditSubject) {
        this.creditSubject = creditSubject;
    }

    public String getDepreciationSubject() {
        return depreciationSubject;
    }

    public void setDepreciationSubject(String depreciationSubject) {
        this.depreciationSubject = depreciationSubject;
    }

    public String getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }

    public String getVoucherWordNumber() {
        return voucherWordNumber;
    }

    public void setVoucherWordNumber(String voucherWordNumber) {
        this.voucherWordNumber = voucherWordNumber;
    }

    public Integer getShowGened() {
        return showGened;
    }

    public void setShowGened(Integer showGened) {
        this.showGened = showGened;
    }
}
