package cn.fooltech.fool_ops.domain.wage.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 工资公式
 *
 * @author xjh
 */
@Entity
@Table(name = "tbd_wage_formula")
public class WageFormula extends OpsOrgEntity {
    public static final short TYPE_INPUT = 0;//手动输入
    public static final short TYPE_FORMULA = 1;//公式计算
    public static final short NOT_VIEW = 0;//不显示
    public static final short VIEW = 1;//显示
    private static final long serialVersionUID = -8881175891586291819L;
    /**
     * 工资项目名称
     */
    private String columnName;

    /**
     * 项目类型
     */
    private Short columnType = TYPE_INPUT;

    /**
     * 公式
     */
    private String formula;

    /**
     * 默认值
     */
    private BigDecimal defaultValue = BigDecimal.ZERO;

    /**
     * 顺序号
     */
    private Integer orderNo = 0;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否显示
     */
    private Short isView = VIEW;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private User creator;

    /**
     * 账套
     */
    private FiscalAccount fiscalAccount;

    /**
     * 修改时间戳
     */
    private Date updateTime;

    /**
     * 部门
     */
    private Organization dept;

    /**
     * 获取所属部门
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FDEPT_ID")
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

    @Column(name = "FCOLUMN_NAME")
    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    @Column(name = "FCOLUMN_TYPE")
    public Short getColumnType() {
        return columnType;
    }

    public void setColumnType(Short columnType) {
        this.columnType = columnType;
    }

    @Column(name = "FFORMULA")
    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    @Column(name = "FDEFAULT_VALUE")
    public BigDecimal getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(BigDecimal defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Column(name = "FORDER_NO")
    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    @Column(name = "FREMARK")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "FIS_VIEW")
    public Short getIsView() {
        return isView;
    }

    public void setIsView(Short isView) {
        this.isView = isView;
    }

    /**
     * 获取创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FCREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取创建人
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCREATOR_ID")
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    /**
     * 获取账套
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FACC_ID")
    public FiscalAccount getFiscalAccount() {
        return fiscalAccount;
    }

    public void setFiscalAccount(FiscalAccount fiscalAccount) {
        this.fiscalAccount = fiscalAccount;
    }

    /**
     * 获取修改时间戳
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FUPDATE_TIME")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
