package cn.fooltech.fool_ops.domain.fiscal.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;

import javax.persistence.*;
import java.util.Date;


/**
 * <p>结转损益科目</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2016年2月15日
 */
@Entity
@Table(name = "TBD_FISCAL_CARRY_FORWARD_PROFIT_LOSS")
public class CarryForwardProfitLoss extends OpsOrgEntity {

    public static final int TYPE_PROFIT_LOSS = 1;//结转损益
    public static final int TYPE_MANUFACTURE = 2;//结转制造费用
    private static final long serialVersionUID = -336345363761704152L;
    /**
     * 类别:1--结转损益；2--结转制造费用
     */
    private Integer type = TYPE_PROFIT_LOSS;

    /**
     * 转出科目
     */
    private FiscalAccountingSubject outSubject;

    /**
     * 转入科目
     */
    private FiscalAccountingSubject inSubject;

    /**
     * 部门
     */
    private Organization dept;

    /**
     * 财务账套
     */
    private FiscalAccount fiscalAccount;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 获取转出科目
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FOUT_SUBJECTID", nullable = false)
    public FiscalAccountingSubject getOutSubject() {
        return outSubject;
    }

    /**
     * 设置转出科目
     *
     * @param outSubject
     */
    public void setOutSubject(FiscalAccountingSubject outSubject) {
        this.outSubject = outSubject;
    }

    /**
     * 获取转入科目
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FIN_SUBJECTID", nullable = false)
    public FiscalAccountingSubject getInSubject() {
        return inSubject;
    }

    /**
     * 设置转入科目
     *
     * @param inSubject
     */
    public void setInSubject(FiscalAccountingSubject inSubject) {
        this.inSubject = inSubject;
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

    /**
     * 获取修改时间
     *
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FUPDATE_TIME", nullable = false)
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
     * 获得类型
     *
     * @return
     */
    @Column(name = "FTYPE")
    public Integer getType() {
        return type;
    }

    /**
     * 设置类型
     *
     * @param type
     */
    public void setType(Integer type) {
        this.type = type;
    }

}
