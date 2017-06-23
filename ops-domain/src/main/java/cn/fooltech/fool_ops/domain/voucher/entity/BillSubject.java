package cn.fooltech.fool_ops.domain.voucher.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;
import java.util.Date;


/**
 * <p>单据、会计科目关联模板</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2015年11月30日
 */
@Entity
@Table(name = "TBD_VOUCHER_BILL_SUBJECT")
public class BillSubject extends OpsOrgEntity {

    private static final long serialVersionUID = 2246296807331541643L;

    /**
     * 单据类型
     */
    private Integer billType;

    /**
     * 模板名称
     */
    private String templateCode;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 凭证字
     */
    private AuxiliaryAttr voucherWord;

    /**
     * 财务账套
     */
    private FiscalAccount fiscalAccount;

    /**
     * 备注
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
     * 获取单据类型
     *
     * @return
     */
    @Column(name = "FBILL_TYPE", nullable = false)
    public Integer getBillType() {
        return billType;
    }

    /**
     * 设置单据类型
     *
     * @param billType
     */
    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    /**
     * 获取模板编号
     *
     * @return
     */
    @Column(name = "FTEMPLATE_CODE", length = 50, nullable = false)
    public String getTemplateCode() {
        return templateCode;
    }

    /**
     * 设置模板编号
     *
     * @param templateCode
     */
    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    /**
     * 获取模板名称
     *
     * @return
     */
    @Column(name = "FTEMPLATE_NAME", length = 50, nullable = false)
    public String getTemplateName() {
        return templateName;
    }

    /**
     * 设置模板名称
     *
     * @param templateName
     */
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
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
     * 获取备注
     *
     * @return
     */
    @Column(name = "FREMARK", length = 200)
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
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

}
