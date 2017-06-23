package cn.fooltech.fool_ops.domain.freight.vo;

import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.freight.entity.FreightAddress;
import cn.fooltech.fool_ops.utils.tree.FastTreeVo;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;


/**
 * 货运地址VO
 *
 * @author cwz
 *         2016-12-5
 */
public class FreightAddressVo extends FastTreeVo<FreightAddressVo> implements Serializable {
    private static final long serialVersionUID = 1L;
    private String fid;
    private Short enable;//状态：0--停用 1--启用
    private String code;//编号
    private String describe;//描述
    private String fullParentId;//父路径
    private String assetgroundId;///辅助属性场地类型Id
    private String assetgroundName;///辅助属性场地类型名称
    private String name;//名称
    private String orgId;//机构ID
    private String parentId;//父ID
    private String parentCode;//父编号
    private String parentName;//父名称
    private Short recipientSign;//收货标识：0-否 1-是
    private String updateTime;//修改时间
    private String assetwarehouseId;//辅助属性仓库Id
    private String assetwarehouseName;//辅助属性仓库名称
    private Short transfer = FreightAddress.TRANSFER_N;//是否中转站  0:不是；1:是

    private List<FreightAddressVo> children = Lists.newArrayList();

    /**
     * 是否显示跟节点
     */
    private Integer showRoot = Constants.SHOW;

    /**
     * 是否显示无效数据
     */
    private Integer showDisable = Constants.NOTSHOW;
    /**
     * 节点标识 1为子节点，0为父节点
     */
    private Short flag;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 创建人
     */
    private String creatorId;
    private String creatorName;

    /**
     * 账套
     */
    private String fiscalAccountId;
    private String fiscalAccountName;
    /**
     * 搜索关键字
     */
    private String searchKey;
    /**
     *
     *损耗地址
     * 
     */
    private String transportLoss;
    private String transportLossId;
    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public Integer getShowRoot() {
        return showRoot;
    }

    public void setShowRoot(Integer showRoot) {
        this.showRoot = showRoot;
    }

    public Integer getShowDisable() {
        return showDisable;
    }

    public void setShowDisable(Integer showDisable) {
        this.showDisable = showDisable;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public Short getEnable() {
        return enable;
    }

    public void setEnable(Short enable) {
        this.enable = enable;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getFullParentId() {
        return fullParentId;
    }

    public void setFullParentId(String fullParentId) {
        this.fullParentId = fullParentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getAssetgroundId() {
        return assetgroundId;
    }

    public void setAssetgroundId(String assetgroundId) {
        this.assetgroundId = assetgroundId;
    }

    public String getAssetgroundName() {
        return assetgroundName;
    }

    public void setAssetgroundName(String assetgroundName) {
        this.assetgroundName = assetgroundName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Short getRecipientSign() {
        return recipientSign;
    }

    public void setRecipientSign(Short recipientSign) {
        this.recipientSign = recipientSign;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getAssetwarehouseId() {
        return assetwarehouseId;
    }

    public void setAssetwarehouseId(String assetwarehouseId) {
        this.assetwarehouseId = assetwarehouseId;
    }

    public String getAssetwarehouseName() {
        return assetwarehouseName;
    }

    public void setAssetwarehouseName(String assetwarehouseName) {
        this.assetwarehouseName = assetwarehouseName;
    }

    public Short getFlag() {
        return flag;
    }

    public void setFlag(Short flag) {
        this.flag = flag;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public String getFiscalAccountId() {
        return fiscalAccountId;
    }

    public void setFiscalAccountId(String fiscalAccountId) {
        this.fiscalAccountId = fiscalAccountId;
    }

    public String getFiscalAccountName() {
        return fiscalAccountName;
    }

    public void setFiscalAccountName(String fiscalAccountName) {
        this.fiscalAccountName = fiscalAccountName;
    }

    @Override
    public String getId() {
        return fid;
    }

    @Override
    public String getText() {
        if (name != null || code != null) {
            return code + " " + name;
        } else {
            return name;
        }
    }

    @Override
    public List<FreightAddressVo> getChildren() {
        return children;
    }

    public void setChildren(List<FreightAddressVo> children) {
        this.children = children;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

	public String getTransportLoss() {
		return transportLoss;
	}

	public String getTransportLossId() {
		return transportLossId;
	}

	public void setTransportLoss(String transportLoss) {
		this.transportLoss = transportLoss;
	}

	public void setTransportLossId(String transportLossId) {
		this.transportLossId = transportLossId;
	}

    public Short getTransfer() {
        return transfer;
    }

    public void setTransfer(Short transfer) {
        this.transfer = transfer;
    }
}