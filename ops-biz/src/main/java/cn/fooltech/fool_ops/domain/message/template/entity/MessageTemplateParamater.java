package cn.fooltech.fool_ops.domain.message.template.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cn.fooltech.fool_ops.domain.base.entity.OpsEntity;


/**
 * <p>消息模板参数</p>
 * 
 * @author xjh
 * @version 1.0
 * @date 2016年5月19日
 */
@Entity
@Table(name = "tmc_message_template_paramater")
public class MessageTemplateParamater extends OpsEntity {
	private static final long serialVersionUID = 5499609332715884473L;

	public static final String TYPE_REPLACE = "REPLACE";
	public static final String TYPE_BUSINESS = "BUSINESS";
	
	/**
	 * 关联的模板
	 */
	private MessageTemplate template;
	
	/**
	 * Map的参数Key
	 */
	private String paramaterKey;
	
	/**
	 * 对象属性
	 */
	private String objField;
	
	/**
	 * 参数类型
	 */
	private String paramaterType;
	
	/**
	 * 参数别名
	 */
	private String paramaterAlias;
	
	/**
	 * 类型
	 */
	private String type = TYPE_REPLACE;
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "FTEMPLATE_ID")
	public MessageTemplate getTemplate() {
		return template;
	}
	public void setTemplate(MessageTemplate template) {
		this.template = template;
	}
	
	@Column(name = "FPARAMATER_KEY")
	public String getParamaterKey() {
		return paramaterKey;
	}
	public void setParamaterKey(String paramaterKey) {
		this.paramaterKey = paramaterKey;
	}
	
	@Column(name = "FPARAMATER_TYPE")
	public String getParamaterType() {
		return paramaterType;
	}
	public void setParamaterType(String paramaterType) {
		this.paramaterType = paramaterType;
	}
	
	@Column(name = "FOBJ_FIELD")
	public String getObjField() {
		return objField;
	}
	public void setObjField(String objField) {
		this.objField = objField;
	}
	
	@Column(name = "FPARAMATER_ALIAS")
	public String getParamaterAlias() {
		return paramaterAlias;
	}
	public void setParamaterAlias(String paramaterAlias) {
		this.paramaterAlias = paramaterAlias;
	}
	
	@Column(name = "FTYPE")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
