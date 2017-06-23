package cn.fooltech.fool_ops.domain.sysman.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * <p>手机客户端更新</p>
 * @author cwz
 * @date 2017年5月10日
 */
@Entity
@Table(name = "tbd_mupdate_record")
public class MupdateRecord implements Serializable{

	private static final long serialVersionUID = 1L;

	//主键
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "FID", unique = true, nullable = false, length = 32)
	private String id;
    
	// 设备类型：android,ios
	@Column(name = "FDEVICE_TYPE")
	private String deviceType;

	// 版本号
	@Column(name = "FVERSION")
	private String version;

	// 备注(填写更新说明)
	@Column(name = "FREMARK")
	private String remark;

	//是否必要更新，1：是；0：否
	@Column(name = "FIS_NEED")
	private Integer isNeed;

	// 创建时间
	@Column(name = "FCREATE_TIME")
	private Date createTime;

	// 下载路径
	@Column(name = "FDOWN_URL")
	private String downUrl;

	// 下载方式，0：直接下载；1：跳转到目标地址下载
	@Column(name = "FDOWN_TYPE")
	private Integer downType;

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
}