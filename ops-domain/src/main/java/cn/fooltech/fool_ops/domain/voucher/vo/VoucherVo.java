package cn.fooltech.fool_ops.domain.voucher.vo;

import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * <p>表单传输对象- 凭证</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2015年11月23日
 */
public class VoucherVo implements Serializable {

    private static final long serialVersionUID = 5851245245107278701L;

    /**
     * ID
     */
    private String fid;

    /**
     * 机构ID
     */
    private String orgId;

    /**
     * 凭证字ID
     */
    @NotBlank(message = "凭证字必填")
    @Length(max = 50, message = "凭证字ID超过{max}个字符")
    private String voucherWordId;

    /**
     * 凭证字名称
     */
    private String voucherWordName;

    /**
     * 凭证号
     */
    @Min(value = 0, message = "凭证号不能小于{value}")
    @Max(value = Integer.MAX_VALUE, message = "凭证号不能大于{value}")
    private Long voucherNumber;

    /**
     * 凭证摘要
     */
    private String voucherResume;

    /**
     * 凭证字号
     */
    private String voucherWordNumber;

    /**
     * 附件数
     */
    @Min(value = 0, message = "附件数不能小于{value}")
    @Max(value = Integer.MAX_VALUE, message = "附件数不能大于{value}")
    private Long accessoryNumber;

    /**
     * 顺序号
     */
    @Min(value = 0, message = "顺序号不能小于{value}")
    @Max(value = Integer.MAX_VALUE, message = "顺序号不能大于{value}")
    private Long number;

    /**
     * 凭证主管ID
     */
    private String supervisorId;

    /**
     * 凭证主管名称
     */
    private String supervisorName;

    /**
     * 记账人ID
     */
    private String postPeopleId;

    /**
     * 记账人名称
     */
    private String postPeopleName;

    /**
     * 凭证日期
     */
    @NotBlank(message = "凭证日期必填")
    @Length(max = 50, message = "凭证日期长度超过{max}个字符")
    private String voucherDate;

    /**
     * 财务账套ID
     */
    private String fiscalAccountId;

    /**
     * 财务会计期间ID
     */
    private String fiscalPeriodId;

    /**
     * 财务账套名称
     */
    private String fiscalAccountName;

    /**
     * 创建人ID
     */
    private String creatorId;

    /**
     * 创建人名称
     */
    private String creatorName;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 审核人ID
     */
    private String auditorId;

    /**
     * 审核人名称
     */
    private String auditorName;

    /**
     * 审核时间
     */
    private String auditDate;

    /**
     * 作废人ID
     */
    private String cancelorId;

    /**
     * 作废人名称
     */
    private String cancelorName;

    /**
     * 作废时间
     */
    private String cancelDate;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 状态
     */
    private Integer recordStatus;

    /**
     * 前台传过来的凭证明细集合(JSON数组格式)
     */
    private String details;

    /**
     * 开始凭证号
     */
    private Integer startVoucherNumber;

    /**
     * 结束凭证号
     */
    private Integer endVoucherNumber;

    /**
     * 开始日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDay;

    /**
     * 结束日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDay;

    /**
     * 过账标识<br>
     * 0 终止 1警告 2允许<br>
     */
    private Integer postAccountFlag = 0;

    /**
     * 凭证ID(多个逗号隔开)
     */
    private String ids;

    /**
     * 科目ID
     */
    private String subjectId;
    
    /**
     * 科目编号
     */
    private String subjectCode;
    /**
     * 科目父ID
     */
    private String subjectParentId;
    /**
     * 科目类别,关联财务辅助属性
     */
    private String subjectAttrId;
    /**
     * 科目层级
     */
    private Integer subjectLevel;
    /**
     * 科目节点标识：0-父节点，1-叶子节点
     */
    private Short subjectFlag;

    
	public String getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}

	public Short getSubjectFlag() {
		return subjectFlag;
	}

	public void setSubjectFlag(Short subjectFlag) {
		this.subjectFlag = subjectFlag;
	}

	public String getSubjectAttrId() {
		return subjectAttrId;
	}

	public void setSubjectAttrId(String subjectAttrId) {
		this.subjectAttrId = subjectAttrId;
	}

	public String getSubjectParentId() {
		return subjectParentId;
	}

	public void setSubjectParentId(String subjectParentId) {
		this.subjectParentId = subjectParentId;
	}

	public Integer getSubjectLevel() {
		return subjectLevel;
	}

	public void setSubjectLevel(Integer subjectLevel) {
		this.subjectLevel = subjectLevel;
	}

	/**
     * 合计借方金额
     */
    private BigDecimal totalAmount = BigDecimal.ZERO;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
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

    public Long getVoucherNumber() {
        return voucherNumber;
    }

    public void setVoucherNumber(Long voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    public String getVoucherResume() {
        return voucherResume;
    }

    public void setVoucherResume(String voucherResume) {
        this.voucherResume = voucherResume;
    }

    public String getVoucherWordNumber() {
        return voucherWordNumber;
    }

    public void setVoucherWordNumber(String voucherWordNumber) {
        this.voucherWordNumber = voucherWordNumber;
    }

    public Long getAccessoryNumber() {
        return accessoryNumber;
    }

    public void setAccessoryNumber(Long accessoryNumber) {
        this.accessoryNumber = accessoryNumber;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(String supervisorId) {
        this.supervisorId = supervisorId;
    }

    public String getSupervisorName() {
        return supervisorName;
    }

    public void setSupervisorName(String supervisorName) {
        this.supervisorName = supervisorName;
    }

    public String getPostPeopleId() {
        return postPeopleId;
    }

    public void setPostPeopleId(String postPeopleId) {
        this.postPeopleId = postPeopleId;
    }

    public String getPostPeopleName() {
        return postPeopleName;
    }

    public void setPostPeopleName(String postPeopleName) {
        this.postPeopleName = postPeopleName;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getFiscalAccountId() {
        return fiscalAccountId;
    }

    public void setFiscalAccountId(String fiscalAccountId) {
        this.fiscalAccountId = fiscalAccountId;
    }

    public String getFiscalPeriodId() {
        return fiscalPeriodId;
    }

    public void setFiscalPeriodId(String fiscalPeriodId) {
        this.fiscalPeriodId = fiscalPeriodId;
    }

    public String getFiscalAccountName() {
        return fiscalAccountName;
    }

    public void setFiscalAccountName(String fiscalAccountName) {
        this.fiscalAccountName = fiscalAccountName;
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

    public String getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(String auditorId) {
        this.auditorId = auditorId;
    }

    public String getAuditorName() {
        return auditorName;
    }

    public void setAuditorName(String auditorName) {
        this.auditorName = auditorName;
    }

    public String getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(String auditDate) {
        this.auditDate = auditDate;
    }

    public String getCancelorId() {
        return cancelorId;
    }

    public void setCancelorId(String cancelorId) {
        this.cancelorId = cancelorId;
    }

    public String getCancelorName() {
        return cancelorName;
    }

    public void setCancelorName(String cancelorName) {
        this.cancelorName = cancelorName;
    }

    public String getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(String cancelDate) {
        this.cancelDate = cancelDate;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Integer getStartVoucherNumber() {
        return startVoucherNumber;
    }

    public void setStartVoucherNumber(Integer startVoucherNumber) {
        this.startVoucherNumber = startVoucherNumber;
    }

    public Integer getEndVoucherNumber() {
        return endVoucherNumber;
    }

    public void setEndVoucherNumber(Integer endVoucherNumber) {
        this.endVoucherNumber = endVoucherNumber;
    }

    public Date getStartDay() {
        return startDay;
    }

    public void setStartDay(Date startDay) {
        this.startDay = startDay;
    }

    public Date getEndDay() {
        return endDay;
    }

    public void setEndDay(Date endDay) {
        this.endDay = endDay;
    }

    public Integer getPostAccountFlag() {
        return postAccountFlag;
    }

    public void setPostAccountFlag(Integer postAccountFlag) {
        this.postAccountFlag = postAccountFlag;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    /**
     * 获取前台表单传过来的凭证明细集合
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List<VoucherDetailVo> getDetailList() {
        List<VoucherDetailVo> vos = new ArrayList<VoucherDetailVo>();
        if (StringUtils.isNotBlank(this.details)) {
            JSONArray jsonArray = JSONArray.fromObject(details);
            List list = (List) JSONArray.toCollection(jsonArray, VoucherDetailVo.class);
            Iterator iteartor = list.iterator();
            while (iteartor.hasNext()) {
                VoucherDetailVo vo = (VoucherDetailVo) iteartor.next();
                vos.add(vo);
            }
        }
        return vos;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

}
