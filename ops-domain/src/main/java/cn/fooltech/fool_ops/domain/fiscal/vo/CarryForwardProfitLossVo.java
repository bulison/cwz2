package cn.fooltech.fool_ops.domain.fiscal.vo;

import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * <p>表单传输对象- 结转损益科目</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2016年2月16日
 */
public class CarryForwardProfitLossVo implements Serializable {

    private static final long serialVersionUID = -7938535915517192065L;

    /**
     * ID
     */
    private String fid;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 转出科目ID
     */
    private String outSubjectId;

    /**
     * 转出科目编号
     */
    private String outSubjectCode;

    /**
     * 转出科目名称
     */
    private String outSubjectName;

    /**
     * 转入科目ID
     */
    private String inSubjectId;

    /**
     * 转入科目编号
     */
    private String inSubjectCode;

    /**
     * 转入科目名称
     */
    private String inSubjectName;

    /**
     * 部门ID
     */
    private String deptId;

    /**
     * 修改时间
     */
    private String updateTime;


    /**
     * 凭证字ID
     */
    private String voucherWordId;

    /**
     * 凭证日期
     */
    private String voucherDate;

    /**
     * 摘要
     */
    @Length(max = 200, message = "摘要字符长度不能超过200个字")
    private String resume;

    /**
     * 财务会计期间ID
     */
    private String fiscalPeriodId;

    /**
     * 转出科目ID集合(用于批量操作)
     */
    private String outSubjectIds;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getOutSubjectId() {
        return outSubjectId;
    }

    public void setOutSubjectId(String outSubjectId) {
        this.outSubjectId = outSubjectId;
    }

    public String getOutSubjectCode() {
        return outSubjectCode;
    }

    public void setOutSubjectCode(String outSubjectCode) {
        this.outSubjectCode = outSubjectCode;
    }

    public String getOutSubjectName() {
        return outSubjectName;
    }

    public void setOutSubjectName(String outSubjectName) {
        this.outSubjectName = outSubjectName;
    }

    public String getInSubjectId() {
        return inSubjectId;
    }

    public void setInSubjectId(String inSubjectId) {
        this.inSubjectId = inSubjectId;
    }

    public String getInSubjectCode() {
        return inSubjectCode;
    }

    public void setInSubjectCode(String inSubjectCode) {
        this.inSubjectCode = inSubjectCode;
    }

    public String getInSubjectName() {
        return inSubjectName;
    }

    public void setInSubjectName(String inSubjectName) {
        this.inSubjectName = inSubjectName;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getVoucherWordId() {
        return voucherWordId;
    }

    public void setVoucherWordId(String voucherWordId) {
        this.voucherWordId = voucherWordId;
    }

    public String getFiscalPeriodId() {
        return fiscalPeriodId;
    }

    public void setFiscalPeriodId(String fiscalPeriodId) {
        this.fiscalPeriodId = fiscalPeriodId;
    }

    public String getOutSubjectIds() {
        return outSubjectIds;
    }

    public void setOutSubjectIds(String outSubjectIds) {
        this.outSubjectIds = outSubjectIds;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

}
