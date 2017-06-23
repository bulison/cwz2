package cn.fooltech.fool_ops.domain.fiscal.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * <p>
 * 计提税费
 * </p>
 *
 * @author xjh
 * @version 1.0
 * @date 2016年4月12日
 */
@Entity
@Table(name = "tbd_fiscal_accrue_taxation")
public class AccrueTaxation extends OpsOrgEntity {

    public static final int TYPE_SUBJECT_AMOUNT = 1;//科目余额
    public static final int TYPE_PERIOD_AMOUNT = 2;//期间发生额
    private static final long serialVersionUID = 5583555483534265488L;
    /**
     * 凭证字
     */
    private AuxiliaryAttr voucherWord;
    /**
     * 税费基数科目取数范围
     * 1--取科目余额
     * 2--取期间发生额
     */
    private Integer baseType;
    /**
     * 税费基数科目
     */
    private FiscalAccountingSubject baseSubject;

    /**
     * 征收税费科目
     */
    private FiscalAccountingSubject collectSubject;

    /**
     * 支付税费科目
     */
    private FiscalAccountingSubject paySubject;

    /**
     * 附加税费科目
     */
    private FiscalAccountingSubject addSubject;

    /**
     * 税点
     */
    private BigDecimal point;

    /**
     * 部门
     */
    private Organization dept;

    /**
     * 账套
     */
    private FiscalAccount fiscalAccount;

    /**
     * 修改时间戳
     */
    private Date updateTime;

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FBASE_SUBJECT_ID")
    public FiscalAccountingSubject getBaseSubject() {
        return baseSubject;
    }

    public void setBaseSubject(FiscalAccountingSubject baseSubject) {
        this.baseSubject = baseSubject;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FPAY_SUBJECT_ID")
    public FiscalAccountingSubject getPaySubject() {
        return paySubject;
    }

    public void setPaySubject(FiscalAccountingSubject paySubject) {
        this.paySubject = paySubject;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FADD_SUBJECT_ID")
    public FiscalAccountingSubject getAddSubject() {
        return addSubject;
    }

    public void setAddSubject(FiscalAccountingSubject addSubject) {
        this.addSubject = addSubject;
    }

    @Column(name = "FPOINT")
    public BigDecimal getPoint() {
        return point;
    }

    public void setPoint(BigDecimal point) {
        this.point = point;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FDEPT_ID")
    public Organization getDept() {
        return dept;
    }

    public void setDept(Organization dept) {
        this.dept = dept;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FACC_ID")
    public FiscalAccount getFiscalAccount() {
        return fiscalAccount;
    }

    public void setFiscalAccount(FiscalAccount fiscalAccount) {
        this.fiscalAccount = fiscalAccount;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FUPDATE_TIME")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FVOUCHER_WORD_ID")
    public AuxiliaryAttr getVoucherWord() {
        return voucherWord;
    }

    public void setVoucherWord(AuxiliaryAttr voucherWord) {
        this.voucherWord = voucherWord;
    }

    @Column(name = "FBASE_TYPE")
    public Integer getBaseType() {
        return baseType;
    }

    public void setBaseType(Integer baseType) {
        this.baseType = baseType;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCOLLECT_SUBJECTID")
    public FiscalAccountingSubject getCollectSubject() {
        return collectSubject;
    }

    public void setCollectSubject(FiscalAccountingSubject collectSubject) {
        this.collectSubject = collectSubject;
    }
}
