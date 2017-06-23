package cn.fooltech.fool_ops.domain.voucher.vo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>表单传输对象- 单据关联会计科目模板明细</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2016年5月17日
 */
public class BillSubjectDetailVo implements Serializable {

    private static final long serialVersionUID = 5498056138601343986L;

    /**
     * ID
     */
    private String fid;

    /**
     * 模板ID
     */
    @NotBlank(message = "模板编号必填")
    @Length(max = 32, message = "模板编号长度超过{max}个字符")
    private String billSubjectId;

    /**
     * 模板编号
     */
    private String billSubjectCode;

    /**
     * 模板名称
     */
    private String billSubjectName;

    /**
     * 方向
     */
    @NotNull(message = "方向必填")
    private Integer direction;

    /**
     * 科目来源
     */
    @NotNull(message = "科目来源必填")
    private Integer subjectSource;

    /**
     * 科目
     */
    @Length(max = 32, message = "科目ID长度超过{max}个字符")
    private String subjectId;

    /**
     * 科目名称
     */
    private String subjectName;

    /**
     * 金额来源
     */
    @NotNull(message = "金额来源必填")
    private Integer amountSource;

    /**
     * 摘要
     */
    @Length(max = 200, message = "摘要长度超过{max}个字符")
    private String resume;

    /**
     * 红蓝对冲标识1:蓝 -1：红
     */
    @NotNull(message = "红蓝对冲必填")
    private Integer hedge;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getBillSubjectId() {
        return billSubjectId;
    }

    public void setBillSubjectId(String billSubjectId) {
        this.billSubjectId = billSubjectId;
    }

    public String getBillSubjectCode() {
        return billSubjectCode;
    }

    public void setBillSubjectCode(String billSubjectCode) {
        this.billSubjectCode = billSubjectCode;
    }

    public String getBillSubjectName() {
        return billSubjectName;
    }

    public void setBillSubjectName(String billSubjectName) {
        this.billSubjectName = billSubjectName;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public Integer getSubjectSource() {
        return subjectSource;
    }

    public void setSubjectSource(Integer subjectSource) {
        this.subjectSource = subjectSource;
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

    public Integer getAmountSource() {
        return amountSource;
    }

    public void setAmountSource(Integer amountSource) {
        this.amountSource = amountSource;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public Integer getHedge() {
        return hedge;
    }

    public void setHedge(Integer hedge) {
        this.hedge = hedge;
    }

}
