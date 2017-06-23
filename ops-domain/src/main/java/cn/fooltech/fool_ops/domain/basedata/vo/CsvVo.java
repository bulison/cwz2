package cn.fooltech.fool_ops.domain.basedata.vo;

import java.io.Serializable;

/**
 * 表单传输对象-供应商
 *
 * @author xjh
 * @version 1.0
 * @date 2015年10月8日
 */
public class CsvVo implements Serializable {

    private static final long serialVersionUID = -2452880076958483094L;

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
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String describe;

    /**
     * 简称
     */
    private String shortName;

    /**
     * 地区ID
     */
    private String areaId;

    /**
     * 地区名称
     */
    private String areaName;

    /**
     * 类别ID
     */
    private String categoryId;

    /**
     * 类别名称
     */
    private String categoryName;

    /**
     * 业务联系人移动电话
     */
    private String phone;

    /**
     * 记录状态
     */
    private String recordStatus;

    /**
     * 类型 1：客户；2：供应商
     */
    private Integer type;

    /**
     * 搜索关键字
     */
    private String searchKey;

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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

}
