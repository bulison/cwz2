package cn.fooltech.fool_ops.domain.wage.vo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>表单传输对象 - 工资公式</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015-12-21 10:05:05
 */
public class WageFormulaVo implements Serializable {

    private static final long serialVersionUID = -5510279303612259268L;

    @NotEmpty(message = "工资项目名称必填")
    private String columnName;//工资名称

    @NotNull(message = "项目类型必填")
    private Short columnType;//工资类型 0:手动输入 1:公式
    private String formula;//公式
    private BigDecimal defaultValue;//默认值

    @Max(value = 99999, message = "顺序号不能大于99999")
    private Integer orderNo;//顺序号

    @Length(max = 200, message = "备注不能超过200个字符")
    private String remark;//备注

    @NotNull(message = "是否可见必填")
    private Short isView;//是否可见
    private String createTime;//创建时间
    private String updateTime;//更新时间
    private String fid;//主键

    public String getColumnName() {
        return this.columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Short getColumnType() {
        return this.columnType;
    }

    public void setColumnType(Short columnType) {
        this.columnType = columnType;
    }

    public String getFormula() {
        return this.formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public BigDecimal getDefaultValue() {
        return this.defaultValue;
    }

    public void setDefaultValue(BigDecimal defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Integer getOrderNo() {
        return this.orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Short getIsView() {
        return this.isView;
    }

    public void setIsView(Short isView) {
        this.isView = isView;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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
}
