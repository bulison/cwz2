package cn.fooltech.fool_ops.domain.report.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>表单传输对象 - 资产负债</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2016-02-16 09:34:26
 */
public class BalanceSheetVo implements Serializable {

    private static final long serialVersionUID = -7983453208651558756L;
    private String assetItem;//资产项目
    private Integer assetNumber;//资产行号
    private BigDecimal assetPeriodEnd;//资产期未余额
    private BigDecimal assetYearBegin;//资产年初余额
    private String assetFormula;//资产公式
    private String debitItem;//负债项目
    private Integer debitNumber;//负债行号
    private BigDecimal debitPeriodEnd;//负债期未余额
    private BigDecimal debitYearBegin;//负债年初余额
    private String debitFormula;//负债公式
    private String updateTime;//修改时间戳
    private short flag = 0;//查找的是资产还是负债0：资产；1：负债
    private String fid;
    private String periodId;//会计期间ID

    public String getAssetItem() {
        return this.assetItem;
    }

    public void setAssetItem(String assetItem) {
        this.assetItem = assetItem;
    }

    public Integer getAssetNumber() {
        return this.assetNumber;
    }

    public void setAssetNumber(Integer assetNumber) {
        this.assetNumber = assetNumber;
    }

    public String getAssetFormula() {
        return this.assetFormula;
    }

    public void setAssetFormula(String assetFormula) {
        this.assetFormula = assetFormula;
    }

    public String getDebitItem() {
        return this.debitItem;
    }

    public void setDebitItem(String debitItem) {
        this.debitItem = debitItem;
    }

    public Integer getDebitNumber() {
        return this.debitNumber;
    }

    public void setDebitNumber(Integer debitNumber) {
        this.debitNumber = debitNumber;
    }

    public String getDebitFormula() {
        return this.debitFormula;
    }

    public void setDebitFormula(String debitFormula) {
        this.debitFormula = debitFormula;
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

    public BigDecimal getAssetPeriodEnd() {
        return assetPeriodEnd;
    }

    public void setAssetPeriodEnd(BigDecimal assetPeriodEnd) {
        this.assetPeriodEnd = assetPeriodEnd;
    }

    public BigDecimal getAssetYearBegin() {
        return assetYearBegin;
    }

    public void setAssetYearBegin(BigDecimal assetYearBegin) {
        this.assetYearBegin = assetYearBegin;
    }

    public BigDecimal getDebitPeriodEnd() {
        return debitPeriodEnd;
    }

    public void setDebitPeriodEnd(BigDecimal debitPeriodEnd) {
        this.debitPeriodEnd = debitPeriodEnd;
    }

    public BigDecimal getDebitYearBegin() {
        return debitYearBegin;
    }

    public void setDebitYearBegin(BigDecimal debitYearBegin) {
        this.debitYearBegin = debitYearBegin;
    }

    public short getFlag() {
        return flag;
    }

    public void setFlag(short flag) {
        this.flag = flag;
    }

    public String getPeriodId() {
        return periodId;
    }

    public void setPeriodId(String periodId) {
        this.periodId = periodId;
    }
}
