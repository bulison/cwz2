package cn.fooltech.fool_ops.domain.sysman.vo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
* 预警阈值设置VO
*/
public class SmgOrgAttrVo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	//主键标识
	private String fid;
	//机构id
	private String orgId;
	//机构名称
	private String orgName;
	//键值
	private String key;
	//配置的数值
	private String value;
	//描述
	private String describe;
	//名称
	private String name;
	//创建时间
	private Date createTime;
	//记录的状态;"SAC"有效数据,"SNU"无效数据
	private String recordState;
	/**
	 * 发送预警人id，用逗号分隔
	 */
	private String ids;
	/**
	 * 类型：0-通用类型，1-发送预警人
	 */
	private String type;

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

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRecordState() {
		return recordState;
	}

	public void setRecordState(String recordState) {
		this.recordState = recordState;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}