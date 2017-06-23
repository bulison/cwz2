package cn.fooltech.fool_ops.domain.voucher.vo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>表单传输对象- 单据、会计科目关联模板</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2015年12月2日
 */
public class BillSubjectVo implements Serializable {

    private static final long serialVersionUID = 5022946529942758929L;

    /**
     * ID
     */
    private String fid;

    /**
     * 单据类型
     */
    @NotNull(message = "单据类型必填")
    @Max(value = Integer.MAX_VALUE, message = "单据类型不能大于{value}")
    private Integer billType;

    /**
     * 模板编号
     */
    @NotBlank(message = "模板编号必填")
    @Length(max = 50, message = "模板编号长度超过{max}个字符")
    private String templateCode;

    /**
     * 模板名称
     */
    @NotBlank(message = "模板名称必填")
    @Length(max = 50, message = "模板名称长度超过{max}个字符")
    private String templateName;

    /**
     * 凭证字ID
     */
    @NotBlank(message = "凭证字必填")
    @Length(max = 32, message = "凭证字ID超过{max}个字符")
    private String voucherWordId;

    /**
     * 凭证字名称
     */
    private String voucherWordName;

    /**
     * 备注
     */
    @Length(max = 200, message = "备注超过{max}个字符")
    private String remark;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 模板明细(JSON数组字符串)
     */
    private String details;
    /**
     * 红蓝对冲标识1:蓝 -1：红
     */
    private Integer hedge;

    /**
     * 借方科目名称
     */
    private String jsubjectName;
    /**
     * 贷方科目名称
     */
    private String dsubjectName;


    public String getJsubjectName() {
        return jsubjectName;
    }

    public void setJsubjectName(String jsubjectName) {
        this.jsubjectName = jsubjectName;
    }

    public String getDsubjectName() {
        return dsubjectName;
    }

    public void setDsubjectName(String dsubjectName) {
        this.dsubjectName = dsubjectName;
    }

    public Integer getHedge() {
        return hedge;
    }

    public void setHedge(Integer hedge) {
        this.hedge = hedge;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

}
