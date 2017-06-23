package cn.fooltech.fool_ops.domain.report.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsEntity;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;
import java.util.Date;


/**
 * <p>多栏明细账设置明细</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2016年1月26日
 */
@Entity
@Table(name = "TBD_FISCAL_MULTI_COLUMN_SETTING_DETAIL")
public class FiscalMultiColumnDetail extends OpsEntity {

    private static final long serialVersionUID = -3738547914448230565L;

    /**
     * 多栏明细
     */
    private FiscalMultiColumn fiscalMultiColumn;

    /**
     * 科目
     */
    private FiscalAccountingSubject subject;

    /**
     * 辅助核算类别
     */
    private Integer auxiliaryType;

    /**
     * 辅助核算ID
     */
    private String auxiliaryAttrId;

    /**
     * 余额方向
     */
    private Integer direction;

    /**
     * 创建人
     */
    private User creator;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 获取多栏明细
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FMULTI_ID", nullable = false)
    public FiscalMultiColumn getFiscalMultiColumn() {
        return fiscalMultiColumn;
    }

    /**
     * 设置多栏明细
     *
     * @param fiscalMultiColumn
     */
    public void setFiscalMultiColumn(FiscalMultiColumn fiscalMultiColumn) {
        this.fiscalMultiColumn = fiscalMultiColumn;
    }

    /**
     * 获取科目
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FFISCAL_SUBJECTID", nullable = false)
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
     * 获取辅助属性类别
     *
     * @return
     */
    @Column(name = "FAUXILIARY_TYPE")
    public Integer getAuxiliaryType() {
        return auxiliaryType;
    }

    /**
     * 设置辅助属性类别
     *
     * @param auxiliaryType
     */
    public void setAuxiliaryType(Integer auxiliaryType) {
        this.auxiliaryType = auxiliaryType;
    }

    /**
     * 获取辅助属性ID
     *
     * @return
     */
    @Column(name = "FAUXILIARY_ID")
    public String getAuxiliaryAttrId() {
        return auxiliaryAttrId;
    }

    /**
     * 设置辅助属性ID
     *
     * @param auxiliaryAttrId
     */
    public void setAuxiliaryAttrId(String auxiliaryAttrId) {
        this.auxiliaryAttrId = auxiliaryAttrId;
    }

    /**
     * 获取余额方向
     *
     * @return
     */
    @Column(name = "FDIRECTION")
    public Integer getDirection() {
        return direction;
    }

    /**
     * 设置余额方向
     *
     * @param direction
     */
    public void setDirection(Integer direction) {
        this.direction = direction;
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

}
