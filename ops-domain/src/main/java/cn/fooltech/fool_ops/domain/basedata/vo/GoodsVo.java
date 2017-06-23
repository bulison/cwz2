package cn.fooltech.fool_ops.domain.basedata.vo;

import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.basedata.entity.Goods;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCode;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 表单传输对象-货品
 *
 * @author rqh
 * @version 1.0
 * @date 2014年12月15日
 * @update rqh 2015-09-10
 */
public class GoodsVo implements Serializable {

    private static final long serialVersionUID = -2384992841811781548L;

    /**
     * ID
     */
    private String fid;

    /**
     * 组织机构ID
     */
    private String orgId;

    /**
     * 编号
     */
    @NotBlank(message = "编号不能为空")
    @Length(max = 50, message = "编号长度不超过{max}个字符")
    private String code;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空")
    @Length(max = 100, message = "名称长度超过{max}个字符")
    private String name;

    /**
     * 描述
     */
    @Length(max = 200, message = "描述长度超过{max}个字符")
    private String describe;

    /**
     * 规格
     */
    @Length(max = 100, message = "规格长度超过{max}个字符")
    private String spec;

    /**
     * 条形码
     */
    @Length(max = 20, message = "条形码长度超过{max}个字符")
    private String barCode;

    /**
     * 重量
     */
    private String weight;

    /**
     * 体积
     */
    private String volume;

    /**
     * 仓储期间
     */
    private Integer storagePeriod;

    /**
     * 分类ID
     */
    private String categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    private String categoryCode;

    /**
     * 货品属性ID
     */
    @Length(max = 32, message = "货品属性长度超过{max}个字符")
    private String goodsSpecId;

    /**
     * 货品属性名称
     */
    private String goodsSpecName;

    /**
     * 货品属性编码
     */
    private String goodsSpecCode;

    /**
     * 货品属性组ID<br>
     * 只是方便前端编写代码，无实际意义，不能用来传递参数到服务器端<br>
     */
    @Length(max = 32, message = "货品属性组长度超过{max}个字符")
    private String goodsSpecGroupId;

    /**
     * 单位ID
     */
    @Length(max = 32, message = "单位长度超过{max}个字符")
    private String unitId;

    /**
     * 单位名称
     */
    private String unitName;

    /**
     * 单位编码
     */
    private String unitCode;

    /**
     * 单位组Id
     */
    private String unitGroupId;

    /**
     * 单位组名称
     */
    private String unitGroupName;

    /**
     * 单位换算关系
     */
    private String unitScale;

    /**
     * 货品标识
     */
    private Integer flag;

    /**
     * 记账标识,货品是否计算库存
     */
    private Integer accountFlag = Goods.ACCOUNT_FLAG_YES;

    /**
     * 已使用标识
     */
    private Integer useFlag;

    /**
     * 上级货品ID
     */
    private String parentId;

    /**
     * 上级货品名称
     */
    private String parentName;

    private String parentCode;

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
     * 修改时间
     */
    private String updateTime;

    /**
     * 记录状态（默认有效）
     */
    private String recordStatus = Goods.STATUS_SAC;

    /**
     * 仓库单据类型
     */
    private WarehouseBuilderCode billType;

    /**
     * 仓库单据ID
     */
    private String billId;

    /**
     * 客户ID(销售商)
     */
    private String customerId;

    /**
     * 供应商ID
     */
    private String supplierId;

    /**
     * 货品的最低销售价
     */
    private String lowestPrice;

    /**
     * 货品的参考价格
     */
    private String referencePrice;

    /**
     * 货品的成本价格
     */
    private String costPrice;

    /**
     * 模糊搜索关键字
     */
    private String searchKey;

    /**
     * 模糊搜索结果集大小
     */
    private Integer searchSize = Constants.VAGUE_SEARCH_SIZE;

    /**
     * 排除的货品ID，多个用逗号隔开
     */
    private String nids;

    /**
     * 是否显示孩子们0：不显示；1显示
     */
    private Integer showChild;

    /**
     * 是否显示孩子们0：不显示；1显示
     */
    private Integer showDisable = Constants.NOTSHOW;
    
    
	/**
	 * 提成点数【0-100之间】 （新增字段）
	 * @author cwz
	 * @date 2017-6-19
	 */
	private BigDecimal percentage;
	
    /**
     * 获取提成点数
     * @return
     */
	public BigDecimal getPercentage() {
		return percentage;
	}
	/**
	 * 设置提成点数
	 * @param percentage
	 */
	public void setPercentage(BigDecimal percentage) {
		this.percentage = percentage;
	}

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getGoodsSpecCode() {
        return goodsSpecCode;
    }

    public void setGoodsSpecCode(String goodsSpecCode) {
        this.goodsSpecCode = goodsSpecCode;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public Integer getStoragePeriod() {
        return storagePeriod;
    }

    public void setStoragePeriod(Integer storagePeriod) {
        this.storagePeriod = storagePeriod;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getGoodsSpecId() {
        return goodsSpecId;
    }

    public void setGoodsSpecId(String goodsSpecId) {
        this.goodsSpecId = goodsSpecId;
    }

    public String getGoodsSpecName() {
        return goodsSpecName;
    }

    public void setGoodsSpecName(String goodsSpecName) {
        this.goodsSpecName = goodsSpecName;
    }

    public String getGoodsSpecGroupId() {
        return goodsSpecGroupId;
    }

    public void setGoodsSpecGroupId(String goodsSpecGroupId) {
        this.goodsSpecGroupId = goodsSpecGroupId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitScale() {
        return unitScale;
    }

    public void setUnitScale(String unitScale) {
        this.unitScale = unitScale;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Integer getAccountFlag() {
        return accountFlag;
    }

    public void setAccountFlag(Integer accountFlag) {
        this.accountFlag = accountFlag;
    }

    public Integer getUseFlag() {
        return useFlag;
    }

    public void setUseFlag(Integer useFlag) {
        this.useFlag = useFlag;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
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

    public String getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus;
    }

    public WarehouseBuilderCode getBillType() {
        return billType;
    }

    public void setBillType(WarehouseBuilderCode billType) {
        this.billType = billType;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(String lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public String getReferencePrice() {
        return referencePrice;
    }

    public void setReferencePrice(String referencePrice) {
        this.referencePrice = referencePrice;
    }

    public String getUnitGroupId() {
        return unitGroupId;
    }

    public void setUnitGroupId(String unitGroupId) {
        this.unitGroupId = unitGroupId;
    }

    public String getUnitGroupName() {
        return unitGroupName;
    }

    public void setUnitGroupName(String unitGroupName) {
        this.unitGroupName = unitGroupName;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(String costPrice) {
        this.costPrice = costPrice;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public Integer getSearchSize() {
        return searchSize;
    }

    public void setSearchSize(Integer searchSize) {
        this.searchSize = searchSize;
    }

    public String getNids() {
        return nids;
    }

    public void setNids(String nids) {
        this.nids = nids;
    }

    public Integer getShowChild() {
        return showChild;
    }

    public void setShowChild(Integer showChild) {
        this.showChild = showChild;
    }

    public Integer getShowDisable() {
        return showDisable;
    }

    public void setShowDisable(Integer showDisable) {
        this.showDisable = showDisable;
    }

}
