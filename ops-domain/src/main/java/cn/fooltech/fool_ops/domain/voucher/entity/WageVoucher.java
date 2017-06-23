package cn.fooltech.fool_ops.domain.voucher.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.domain.wage.entity.WageFormula;

import javax.persistence.*;
import java.util.Date;


/**
 * <p>工资凭证，用于工资生成凭证</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2016年5月11日
 */
@Entity
@Table(name = "TBD_WAGE_VOUCHER")
public class WageVoucher extends OpsOrgEntity {

    private static final long serialVersionUID = 8829724815914760194L;

    /**
     * 工资项目
     */
    private WageFormula wageFormula;

    /**
     * 费用科目(借方)
     */
    private FiscalAccountingSubject expenseSubject;

    /**
     * 科目方向:1.借方；-1.贷方
     */
    private Integer direction;

    /**
     * 应付工资科目(贷方)
     */
    private FiscalAccountingSubject wageSubject;

    /**
     * 凭证字
     */
    @Deprecated
    private AuxiliaryAttr voucherWord;

    /**
     * 摘要
     */
    private String remark;

    /**
     * 创建人
     */
    private User creator;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 部门
     */
    private Organization dept;

    /**
     * 财务账套
     */
    private FiscalAccount fiscalAccount;

    /**
     * 获取方向
     *
     * @return
     */
    @Column(name = "FDIRECTION", nullable = false)
    public Integer getDirection() {
        return direction;
    }

    /**
     * 设置方向
     *
     * @param direction
     */
    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    /**
     * 获取工资项目
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FWAGE_COLUMN", nullable = false)
    public WageFormula getWageFormula() {
        return wageFormula;
    }

    /**
     * 设置工资项目
     *
     * @param wageFormula
     */
    public void setWageFormula(WageFormula wageFormula) {
        this.wageFormula = wageFormula;
    }

    /**
     * 获取费用科目
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FEXPENSE_SUBJECT", nullable = false)
    public FiscalAccountingSubject getExpenseSubject() {
        return expenseSubject;
    }

    /**
     * 设置费用科目
     *
     * @param expenseSubject
     */
    public void setExpenseSubject(FiscalAccountingSubject expenseSubject) {
        this.expenseSubject = expenseSubject;
    }

    /**
     * 获取应付工资科目
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FWAGE_SUBJECT", nullable = false)
    public FiscalAccountingSubject getWageSubject() {
        return wageSubject;
    }

    /**
     * 设置应付工资科目
     *
     * @param wageSubject
     */
    public void setWageSubject(FiscalAccountingSubject wageSubject) {
        this.wageSubject = wageSubject;
    }

    /**
     * 获取凭证字
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FVOUCHER_WORD", nullable = false)
    public AuxiliaryAttr getVoucherWord() {
        return voucherWord;
    }

    /**
     * 设置凭证字
     *
     * @param voucherWord
     */
    public void setVoucherWord(AuxiliaryAttr voucherWord) {
        this.voucherWord = voucherWord;
    }

    /**
     * 获取摘要
     *
     * @return
     */
    @Column(name = "FREMARK", length = 200)
    public String getRemark() {
        return remark;
    }

    /**
     * 设置摘要
     *
     * @param remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取创建人
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCREATOR_ID", nullable = false)
    public User getCreator() {
        return creator;
    }

    /**
     * 设置创建人
     *
     * @param creator
     */
    public void setCreator(User creator) {
        this.creator = creator;
    }

    /**
     * 获取创建时间
     *
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FCREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取修改时间
     *
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FUPDATE_TIME")
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置修改时间
     *
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取部门
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FDEPT_ID", nullable = false)
    public Organization getDept() {
        return dept;
    }

    /**
     * 设置部门
     *
     * @param dept
     */
    public void setDept(Organization dept) {
        this.dept = dept;
    }

    /**
     * 获取财务账套
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FACC_ID", nullable = false)
    public FiscalAccount getFiscalAccount() {
        return fiscalAccount;
    }

    /**
     * 设置财务账套
     *
     * @param fiscalAccount
     */
    public void setFiscalAccount(FiscalAccount fiscalAccount) {
        this.fiscalAccount = fiscalAccount;
    }

}
