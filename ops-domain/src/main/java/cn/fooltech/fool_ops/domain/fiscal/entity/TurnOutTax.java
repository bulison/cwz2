package cn.fooltech.fool_ops.domain.fiscal.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;

import javax.persistence.*;
import java.util.Date;


/**
 * <p>
 * 转出未交增值税
 * </p>
 *
 * @author xjh
 * @version 1.0
 * @date 2016年4月12日
 */
@Entity
@Table(name = "tbd_fiscal_turn_out_tax")
public class TurnOutTax extends OpsOrgEntity {

    private static final long serialVersionUID = 6006492147737961884L;

    /**
     * 凭证字
     */
    private AuxiliaryAttr voucherWord;

    /**
     * 销项税科目
     */
    private FiscalAccountingSubject outSubject;

    /**
     * 进项税科目
     */
    private FiscalAccountingSubject inSubject;

    /**
     * 转出未交增值税科目
     */
    private FiscalAccountingSubject taxSubject;

    /**
     * 未交增值税科目
     */
    private FiscalAccountingSubject unpaidSubject;

    /**
     * 支付税费科目
     */
    private FiscalAccountingSubject paySubject;

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
    @JoinColumn(name = "FVOUCHER_WORD_ID")
    public AuxiliaryAttr getVoucherWord() {
        return voucherWord;
    }

    public void setVoucherWord(AuxiliaryAttr voucherWord) {
        this.voucherWord = voucherWord;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FOUT_SUBJECT_ID")
    public FiscalAccountingSubject getOutSubject() {
        return outSubject;
    }

    public void setOutSubject(FiscalAccountingSubject outSubject) {
        this.outSubject = outSubject;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FIN_SUBJECT_ID")
    public FiscalAccountingSubject getInSubject() {
        return inSubject;
    }

    public void setInSubject(FiscalAccountingSubject inSubject) {
        this.inSubject = inSubject;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FTAX_SUBJECT_ID")
    public FiscalAccountingSubject getTaxSubject() {
        return taxSubject;
    }

    public void setTaxSubject(FiscalAccountingSubject taxSubject) {
        this.taxSubject = taxSubject;
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
    @JoinColumn(name = "FUNPAID_SUBJECTID")
    public FiscalAccountingSubject getUnpaidSubject() {
        return unpaidSubject;
    }

    public void setUnpaidSubject(FiscalAccountingSubject unpaidSubject) {
        this.unpaidSubject = unpaidSubject;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FPAY_SUBJECTID")
    public FiscalAccountingSubject getPaySubject() {
        return paySubject;
    }

    public void setPaySubject(FiscalAccountingSubject paySubject) {
        this.paySubject = paySubject;
    }
}
