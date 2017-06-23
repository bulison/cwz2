package cn.fooltech.fool_ops.domain.warehouse.vo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>表单传输对象- 现金盘点单</p>
 *
 * @author cwz
 * @version 1.0
 * @date 2016年9月18日
 */
public class CashCheckVo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String fid;
    /**
     * 科目
     */
    @NotBlank(message = "科目不能为空")
    private String subjectId;
    private String subjectName;
    private String subjectCode;
    /**
     * 盘点日期
     */
    @NotBlank(message = "盘点日期不能为空")
    private String date;
    /**
     * 盘点金额
     */
    private BigDecimal amount = BigDecimal.ZERO;
    /**
     * 100元(记录张数)
     */
    private Long f100;
    /**
     * 50元
     */
    private Long f050;
    /**
     * 20元
     */
    private Long f020;
    /**
     * 10元
     */
    private Long f010;
    /**
     * 5元
     */
    private Long f005;
    /**
     * 2元
     */
    private Long f002;
    /**
     * 1元
     */
    private Long f001;
    /**
     * 5角
     */
    private Long f_50;
    /**
     * 2角
     */
    private Long f_20;
    /**
     * 1角
     */
    private Long f_10;
    /**
     * 5分
     */
    private Long f_05;
    /**
     * 2分
     */
    private Long f_02;
    /**
     * 1分
     */
    private Long f_01;
    /**
     * 备注
     */
    @Length(max = 50, message = "备注长度超过50个字符")
    private String resume;
    /**
     * 创建人
     */
    private String creatorId;
    private String creatorName;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 修改时间戳(初始值为当前时间)
     */
    private String updateTime;

    private String deptId;//部门
    private String deptName;
    /**
     * 状态：1、可编辑；2、不可编辑
     */
    private String stauts;

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

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getF100() {
        return f100;
    }

    public void setF100(Long f100) {
        this.f100 = f100;
    }

    public Long getF050() {
        return f050;
    }

    public void setF050(Long f050) {
        this.f050 = f050;
    }

    public Long getF020() {
        return f020;
    }

    public void setF020(Long f020) {
        this.f020 = f020;
    }

    public Long getF010() {
        return f010;
    }

    public void setF010(Long f010) {
        this.f010 = f010;
    }

    public Long getF005() {
        return f005;
    }

    public void setF005(Long f005) {
        this.f005 = f005;
    }

    public Long getF002() {
        return f002;
    }

    public void setF002(Long f002) {
        this.f002 = f002;
    }

    public Long getF001() {
        return f001;
    }

    public void setF001(Long f001) {
        this.f001 = f001;
    }

    public Long getF_50() {
        return f_50;
    }

    public void setF_50(Long f_50) {
        this.f_50 = f_50;
    }

    public Long getF_20() {
        return f_20;
    }

    public void setF_20(Long f_20) {
        this.f_20 = f_20;
    }

    public Long getF_10() {
        return f_10;
    }

    public void setF_10(Long f_10) {
        this.f_10 = f_10;
    }

    public Long getF_05() {
        return f_05;
    }

    public void setF_05(Long f_05) {
        this.f_05 = f_05;
    }

    public Long getF_02() {
        return f_02;
    }

    public void setF_02(Long f_02) {
        this.f_02 = f_02;
    }

    public Long getF_01() {
        return f_01;
    }

    public void setF_01(Long f_01) {
        this.f_01 = f_01;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getStauts() {
        return stauts;
    }

    public void setStauts(String stauts) {
        this.stauts = stauts;
    }

}
