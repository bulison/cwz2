package cn.fooltech.fool_ops.domain.sysman.vo;

import java.io.Serializable;
import java.util.Date;

public class MupdateRecordVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 主键
	private String id;

	// 设备类型：android,ios
	private String deviceType;

	// 版本号
	private String version;

	// 备注(填写更新说明)
	private String remark;

	// 是否必要更新，1：是；0：否
	private Integer isNeed;

	// 创建时间
	private Date createTime;

	// 下载路径
	private String downUrl;

	// 下载方式，0：直接下载；1：跳转到目标地址下载
	private Integer downType;
	private Date startDay;

	private Date endDay;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getIsNeed() {
		return isNeed;
	}

	public void setIsNeed(Integer isNeed) {
		this.isNeed = isNeed;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getDownUrl() {
		return downUrl;
	}

	public void setDownUrl(String downUrl) {
		this.downUrl = downUrl;
	}

	public Integer getDownType() {
		return downType;
	}

	public void setDownType(Integer downType) {
		this.downType = downType;
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
	
	

}