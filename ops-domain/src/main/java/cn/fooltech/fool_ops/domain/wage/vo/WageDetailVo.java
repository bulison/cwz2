package cn.fooltech.fool_ops.domain.wage.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>表单传输对象 - 工资明细</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015-12-21 15:56:21
 */
public class WageDetailVo implements Serializable {

    private static final long serialVersionUID = 9154121754890710406L;
    private BigDecimal value;//输入值
    private String wageId;//工资ID
    private String wageDate;

    private String memberId;//人员ID
    private String memberName;//人员名称
    private String memberCode;//人员编号
    private String memberDept;//人员部门
    private String deptId;//部门ID
    private String deptName;//部门名称

    private String formulaId;//公式ID
    private String formula;//公式
    private String fid;

    public BigDecimal getValue() {
        return this.value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getFid() {
        return this.fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getWageId() {
        return wageId;
    }

    public void setWageId(String wageId) {
        this.wageId = wageId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getFormulaId() {
        return formulaId;
    }

    public void setFormulaId(String formulaId) {
        this.formulaId = formulaId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public String getMemberDept() {
        return memberDept;
    }

    public void setMemberDept(String memberDept) {
        this.memberDept = memberDept;
    }

    public String getWageDate() {
        return wageDate;
    }

    public void setWageDate(String wageDate) {
        this.wageDate = wageDate;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

}
