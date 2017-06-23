package cn.fooltech.fool_ops.domain.base.vo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;

public class DictionaryVo {
	@ApiModelProperty(value = "主键")
//	@NotBlank(message = "主键不能为空")
	private String id;//ID
	
	
	@ApiModelProperty(value = "编号")
	@Length(max = 50, message = "编号长度超过{max}个字符")
	private String code; //编号
	
	
	@ApiModelProperty(value = "描述")
	private String describe;//描述
	
	
	@ApiModelProperty(value = "key")
	@NotBlank(message = "key不能为空")
    @Length(max = 50, message = "key长度超过{max}个字符")
	private String key;//key
	
	
	@ApiModelProperty(value = "名称")
	@Length(max = 50, message = "名称长度超过{max}个字符")
	private String name;//名称
	
	
	@ApiModelProperty(value = "value")
	@NotBlank(message = "值不能为空")
	@Length(max = 50, message = "值长度超过{max}个字符")
	private String value;//value
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
}
