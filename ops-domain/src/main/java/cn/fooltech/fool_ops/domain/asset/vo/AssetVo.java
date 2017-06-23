package cn.fooltech.fool_ops.domain.asset.vo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>表单传输对象 - 固定资产卡片</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015-12-28 14:23:18
 */
public class AssetVo implements Serializable {

    private static final long serialVersionUID = -3572242133628259995L;
    @NotBlank(message = "资产编号必填")
    @Length(max = 50, message = "资产编号长度超过{max}个字符")
    private String assetCode;//资产编号

    @NotBlank(message = "资产名称必填")
    @Length(max = 50, message = "资产名称长度超过{max}个字符")
    private String assetName;//资产名称

    @NotNull(message = "数量必填")
    @Min(value = 1, message = "数量不能小于{value}")
    @Max(value = Integer.MAX_VALUE, message = "数量不能大于{value}")
    private BigDecimal quentity;//数量

    @NotNull(message = "资产原值必填")
    @Min(value = 1, message = "资产原值不能小于{value}")
    @Max(value = Integer.MAX_VALUE, message = "资产原值不能大于{value}")
    private BigDecimal initialValue;//资产原值

    @NotNull(message = "折旧年限必填")
    @Min(value = 1, message = "折旧年限不能小于{value}")
    @Max(value = 100, message = "折旧年限不能大于{value}")
    private Integer discountYear;// 折旧年限

    @NotNull(message = "残值率必填")
    @Max(value = 1, message = "残值率不能大于{value}")
    private BigDecimal residualRate;//残值率

    @NotNull(message = "资产净值必填")
    @Min(value = 0, message = "资产净值不能小于{value}")
    @Max(value = Integer.MAX_VALUE, message = "资产净值不能大于{value}")
    private BigDecimal residualValue;//资产净值

    private BigDecimal showResidualValue;//资产净值(展示用，=资产净值/数量)

    @Length(max = 50, message = "来源长度超过{max}个字符")
    private String supplier;//来源

    @Length(max = 50, message = "等级长度超过{max}个字符")
    private String grade;//等级

    @Length(max = 50, message = "产地及厂家长度超过{max}个字符")
    private String manufactor;//产地及厂家

    @Length(max = 50, message = "规格型号长度超过{max}个字符")
    private String model;//规格型号

    @NotBlank(message = "使用日期必填")
    @Length(max = 20, message = "使用日期长度超过{max}个字符")
    private String useDate;//使用日期

    @NotBlank(message = "购买日期必填")
    @Length(max = 20, message = "购买日期长度超过{max}个字符")
    private String buyDate;//购买日期

    @NotBlank(message = "计提日期必填")
    @Length(max = 20, message = "计提日期长度超过{max}个字符")
    private String shareDate;//计提日期

    private Short recordStatus;//状态 0--未审核；1--审核；2--计提中；3--计提完成；4--已清算
    private String createTime;//创建时间
    private String updateTime;//修改时间戳
    private String fid;//主键

    private String creatorId;//创建人
    private String creatorName;

    private String auditorId;//审核人
    private String auditorName;
    private String auditTime;

    @NotBlank(message = "资产类型必填")
    @Length(max = 32, message = "资产类型长度超过{max}个字符")
    private String assetTypeId;//资产类型

    private String assetTypeName;

    @NotBlank(message = "部门必填")
    @Length(max = 32, message = "部门长度超过{max}个字符")

    private String deptId;//部门
    private String deptName;

    @Length(max = 32, message = "固定资产科目ID超过{max}个字符")
    private String assetSubjectId;//固定资产科目
    private String assetSubjectName;

    @Length(max = 32, message = "折旧科目ID超过{max}个字符")
    private String depreciationSubjectId;//折旧科目
    private String depreciationSubjectName;

    @Length(max = 32, message = "支付科目ID超过{max}个字符")
    private String paymentSubjectId;//支付科目
    private String paymentSubjectName;

    @Length(max = 32, message = "清算科目ID超过{max}个字符")
    private String clearSubjectId;//清算科目
    private String clearSubjectName;

    private String expenseSubjectId; //费用科目
    private String expenseSubjectName; //费用科目名称

    private BigDecimal sumAccruedValue;//累计已提折旧

    //private String details;//明细json串

    public String getAssetCode() {
        return this.assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    public String getAssetName() {
        return this.assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public BigDecimal getQuentity() {
        return this.quentity;
    }

    public void setQuentity(BigDecimal quentity) {
        this.quentity = quentity;
    }

    public BigDecimal getInitialValue() {
        return this.initialValue;
    }

    public void setInitialValue(BigDecimal initialValue) {
        this.initialValue = initialValue;
    }

    public Integer getDiscountYear() {
        return this.discountYear;
    }

    public void setDiscountYear(Integer discountYear) {
        this.discountYear = discountYear;
    }

    public BigDecimal getResidualRate() {
        return this.residualRate;
    }

    public void setResidualRate(BigDecimal residualRate) {
        this.residualRate = residualRate;
    }

    public BigDecimal getResidualValue() {
        return this.residualValue;
    }

    public void setResidualValue(BigDecimal residualValue) {
        this.residualValue = residualValue;
    }

    public String getSupplier() {
        return this.supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getGrade() {
        return this.grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getManufactor() {
        return this.manufactor;
    }

    public void setManufactor(String manufactor) {
        this.manufactor = manufactor;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getUseDate() {
        return this.useDate;
    }

    public void setUseDate(String useDate) {
        this.useDate = useDate;
    }

    public String getBuyDate() {
        return this.buyDate;
    }

    public void setBuyDate(String buyDate) {
        this.buyDate = buyDate;
    }

    public String getShareDate() {
        return this.shareDate;
    }

    public void setShareDate(String shareDate) {
        this.shareDate = shareDate;
    }

    public Short getRecordStatus() {
        return this.recordStatus;
    }

    public void setRecordStatus(Short recordStatus) {
        this.recordStatus = recordStatus;
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

    public String getAssetSubjectId() {
        return assetSubjectId;
    }

    public void setAssetSubjectId(String assetSubjectId) {
        this.assetSubjectId = assetSubjectId;
    }

    public String getAssetSubjectName() {
        return assetSubjectName;
    }

    public void setAssetSubjectName(String assetSubjectName) {
        this.assetSubjectName = assetSubjectName;
    }

    public String getDepreciationSubjectId() {
        return depreciationSubjectId;
    }

    public void setDepreciationSubjectId(String depreciationSubjectId) {
        this.depreciationSubjectId = depreciationSubjectId;
    }

    public String getDepreciationSubjectName() {
        return depreciationSubjectName;
    }

    public void setDepreciationSubjectName(String depreciationSubjectName) {
        this.depreciationSubjectName = depreciationSubjectName;
    }

    public String getPaymentSubjectId() {
        return paymentSubjectId;
    }

    public void setPaymentSubjectId(String paymentSubjectId) {
        this.paymentSubjectId = paymentSubjectId;
    }

    public String getPaymentSubjectName() {
        return paymentSubjectName;
    }

    public void setPaymentSubjectName(String paymentSubjectName) {
        this.paymentSubjectName = paymentSubjectName;
    }

    public String getClearSubjectId() {
        return clearSubjectId;
    }

    public void setClearSubjectId(String clearSubjectId) {
        this.clearSubjectId = clearSubjectId;
    }

    public String getClearSubjectName() {
        return clearSubjectName;
    }

    public void setClearSubjectName(String clearSubjectName) {
        this.clearSubjectName = clearSubjectName;
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

    public String getAssetTypeId() {
        return assetTypeId;
    }

    public void setAssetTypeId(String assetTypeId) {
        this.assetTypeId = assetTypeId;
    }

    public String getAssetTypeName() {
        return assetTypeName;
    }

    public void setAssetTypeName(String assetTypeName) {
        this.assetTypeName = assetTypeName;
    }

    public BigDecimal getSumAccruedValue() {
        return sumAccruedValue;
    }

    public void setSumAccruedValue(BigDecimal sumAccruedValue) {
        this.sumAccruedValue = sumAccruedValue;
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

    public String getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }

    public BigDecimal getShowResidualValue() {
        return showResidualValue;
    }

    public void setShowResidualValue(BigDecimal showResidualValue) {
        this.showResidualValue = showResidualValue;
    }

	/*public String getDetails() {
        return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}*/

    /**
     * 获取前台表单传过来的单据明细集合
     */
	/*@SuppressWarnings("rawtypes")
	@JsonIgnore
	public List<AssetDetailVo> getDetailVos() {
		List<AssetDetailVo> list = new ArrayList<AssetDetailVo>();
		if(StringUtils.isNotBlank(this.details)){
			JSONArray array = JSONArray.fromObject(this.details);
			List details = (List)JSONArray.toCollection(array, WarehouseBillDetailVo.class);
			Iterator iterator = details.iterator();
			while(iterator.hasNext()){
				AssetDetailVo detail = (AssetDetailVo) iterator.next();
				list.add(detail);
			}
		}
		return list;
	}*/
}
