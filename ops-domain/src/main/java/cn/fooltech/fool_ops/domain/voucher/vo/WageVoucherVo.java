package cn.fooltech.fool_ops.domain.voucher.vo;

import java.io.Serializable;

/**
 * <p>工资凭证表单传输对象</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2016年5月12日
 */
public class WageVoucherVo implements Serializable {

    private static final long serialVersionUID = -6482146930640375266L;

    /**
     * ID
     */
    private String fid;

    /**
     * 工资项目ID
     */
    private String wageFormulaId;

    /**
     * 工资项目名称
     */
    private String wageFormulaName;

    /**
     * 费用科目ID
     */
    private String expenseSubjectId;

    /**
     * 费用科目名称
     */
    private String expenseSubjectName;

    /**
     * 应付工资科目ID
     */
    private String wageSubjectId;

    /**
     * 应付工资科目名称
     */
    private String wageSubjectName;

    /**
     * 凭证字ID
     */
    private String voucherWordId;

    /**
     * 凭证字名称
     */
    private String voucherWordName;

    /**
     * 部门ID
     */
    private String deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 摘要
     */
    private String remark;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 科目方向:1.借方；-1.贷方
     */
    private Integer direction;


    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getWageFormulaId() {
        return wageFormulaId;
    }

    public void setWageFormulaId(String wageFormulaId) {
        this.wageFormulaId = wageFormulaId;
    }

    public String getWageFormulaName() {
        return wageFormulaName;
    }

    public void setWageFormulaName(String wageFormulaName) {
        this.wageFormulaName = wageFormulaName;
    }

    public String getExpenseSubjectId() {
        return expenseSubjectId;
    }

    public void setExpenseSubjectId(String expenseSubjectId) {
        this.expenseSubjectId = expenseSubjectId;
    }

    public String getExpenseSubjectName() {
        return expenseSubjectName;
    }

    public void setExpenseSubjectName(String expenseSubjectName) {
        this.expenseSubjectName = expenseSubjectName;
    }

    public String getWageSubjectId() {
        return wageSubjectId;
    }

    public void setWageSubjectId(String wageSubjectId) {
        this.wageSubjectId = wageSubjectId;
    }

    public String getWageSubjectName() {
        return wageSubjectName;
    }

    public void setWageSubjectName(String wageSubjectName) {
        this.wageSubjectName = wageSubjectName;
    }

    public String getVoucherWordId() {
        return voucherWordId;
    }

    public void setVoucherWordId(String voucherWordId) {
        this.voucherWordId = voucherWordId;
    }

    public String getVoucherWordName() {
        return voucherWordName;
    }

    public void setVoucherWordName(String voucherWordName) {
        this.voucherWordName = voucherWordName;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

}
