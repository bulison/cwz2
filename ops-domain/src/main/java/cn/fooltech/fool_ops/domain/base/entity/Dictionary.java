package cn.fooltech.fool_ops.domain.base.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import io.swagger.annotations.ApiModelProperty;
/**
 * 
 * @author hjr
 *字典-实体类
 */
@Entity
@Table(name = "smg_tdict")
public class Dictionary {
	/**
	 * ID
	 */
	private String id;
	/**
	 * 编号
	 */
	private String code; 
	/**
	 * 描述
	 */
	private String describe;
	/**
	 * key
	 */
	private String key;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 值
	 */
	private String value;
	@ApiModelProperty(value = "主键")
	@Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "FID")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@ApiModelProperty(value = "编号")
	@Column(name = "FCODE")
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@ApiModelProperty(value = "描述")
	@Column(name = "FDESCRIBE")
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	@ApiModelProperty(value = "key")
	@Column(name = "FKEY")
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	@ApiModelProperty(value = "名称")
	@Column(name = "FNAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@ApiModelProperty(value = "value")
	@Column(name = "FVALUE")
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}	
