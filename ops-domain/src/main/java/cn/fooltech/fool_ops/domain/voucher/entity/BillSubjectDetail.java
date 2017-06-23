package cn.fooltech.fool_ops.domain.voucher.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;
import java.util.Date;


/**
 * <p>单据关联会计科目模板明细</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2016年5月17日
 */
@Entity
@Table(name = "TBD_VOUCHER_BILL_SUBJECT_DETAIL")
public class BillSubjectDetail extends OpsOrgEntity {

    /**
     * 红色对冲标识
     */
    public static final int HEDGE_RED = -1;
    /**
     * 蓝色对冲标识
     */
    public static final int HEDGE_BLUE = 1;
    /**
     * 方向- 借方
     */
    public static final int DIRECTION_BORROW = 1;
    /**
     * 方向- 贷方
     */
    public static final int DIRECTION_LOAN = -1;
    /**
     * 科目来源- 模板
     */
    public static final int SUBJECT_SOURCE_TEMPLATE = 1;
    /**
     * 科目来源- 银行账号关联科目
     */
    public static final int SUBJECT_SOURCE_BANK = 2;
    /**
     * 金额来源- 单据金额（不含税金额）
     */
    public static final int AMOUNT_SOURCE_BILL = 1;
    /**
     * 金额来源- 成本金额
     */
    public static final int AMOUNT_SOURCE_COST = 2;
    /**
     * 金额来源- 税额
     */
    public static final int AMOUNT_SOURCE_TAX = 3;
    /**
     * 金额来源- 含税金额
     */
    public static final int AMOUNT_SOURCE_INCLUDE_TAX = 4;
    /**
     * 金额来源- 利润
     */
    public static final int AMOUNT_SOURCE_PROFIT = 5;
    /**
     * 金额来源- 提成金额
     */
    public static final int AMOUNT_PERCENTAGE  = 6;
    private static final long serialVersionUID = -1204002086159517912L;
    /**
     * 模板
     */
    private BillSubject billSubject;
    /**
     * 方向
     */
    private Integer direction;
    /**
     * 科目来源<br>
     * 针对收款单、付款单、采购返利单、销售返利单、费用单<br>
     */
    private Integer subjectSource;
    /**
     * 科目
     */
    private FiscalAccountingSubject subject;
    /**
     * 金额来源<br>
     * 针对所有仓库单据<br>
     */
    private Integer amountSource = AMOUNT_SOURCE_BILL;
    /**
     * 摘要
     */
    private String resume;
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
     * 财务账套
     */
    private FiscalAccount fiscalAccount;
    /**
     * 红蓝对冲标识
     */
    private Integer hedge;

    /**
     * 获取模板
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FTEMPLATE_ID", nullable = false)
    public BillSubject getBillSubject() {
        return billSubject;
    }

    /**
     * 设置模板
     *
     * @param billSubject
     */
    public void setBillSubject(BillSubject billSubject) {
        this.billSubject = billSubject;
    }

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
     * 获取科目来源
     *
     * @return
     */
    @Column(name = "FSUBJECT_SOURCE", nullable = false)
    public Integer getSubjectSource() {
        return subjectSource;
    }

    /**
     * 设置科目来源
     *
     * @param subjectSource
     */
    public void setSubjectSource(Integer subjectSource) {
        this.subjectSource = subjectSource;
    }

    /**
     * 获取科目
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FSUBJECT_ID")
    public FiscalAccountingSubject getSubject() {
        return subject;
    }

    /**
     * 设置科目
     *
     * @param subject
     */
    public void setSubject(FiscalAccountingSubject subject) {
        this.subject = subject;
    }

    /**
     * 获取金额来源
     *
     * @return
     */
    @Column(name = "FAMOUNT_SOURCE", nullable = false)
    public Integer getAmountSource() {
        return amountSource;
    }

    /**
     * 设置金额来源
     *
     * @param amountSource
     */
    public void setAmountSource(Integer amountSource) {
        this.amountSource = amountSource;
    }

    /**
     * 获取摘要
     *
     * @return
     */
    @Column(name = "FRESUME", length = 200)
    public String getResume() {
        return resume;
    }

    /**
     * 设置摘要
     *
     * @param resume
     */
    public void setResume(String resume) {
        this.resume = resume;
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

    @Column(name = "FHEDGE")
    public Integer getHedge() {
        return hedge;
    }

    public void setHedge(Integer hedge) {
        this.hedge = hedge;
    }

}
