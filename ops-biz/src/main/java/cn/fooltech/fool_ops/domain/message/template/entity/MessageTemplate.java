package cn.fooltech.fool_ops.domain.message.template.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import cn.fooltech.fool_ops.domain.base.entity.OpsEntity;


/**
 * <p>
 * 消息模板
 * </p>
 * 
 * @author csf
 * @version 1.0
 * @date 2014年9月1日
 */
@Entity
@Table(name = "tmc_message_template")
public class MessageTemplate extends OpsEntity {

	private static final long serialVersionUID = -1738783062393396200L;
	
	/**
	 * 提醒消息
	 */
	public final static int TYPE_NOTIFY = 0;
	
	/**
	 * 待办
	 */
	public final static int TYPE_WAIT_PROCESS = 1;
	
	/**
	 * 预警
	 */
	public final static int TYPE_WARNING = 2;

	/**
	 * 模板编号
	 */
	private Integer code;

	/**
	 * 模板
	 */
	private String formatPattern;

	/**
	 * 消息内容格式描述
	 */
	private String contentDesc;
	
	public static final String SEND_TYPE_INNER = "INNER";//站内信
	public static final String SEND_TYPE_EMAIL = "EMAIL";//邮箱
	public static final String SEND_TYPE_PHONE = "PHONE";//手机短信
	public static final String SEND_TYPE_PUSH = "PUSH";//移动端推送
	
	/**
	 * 发送类型
	 */
	private String sendType;
	
	/**
	 * 是否有效 
	 */
	private Boolean enable;
	
	/**
	 * 消息类型  0：提醒消息; 1:待办;  2:预警;
	 */
	private Integer opertype = TYPE_NOTIFY;
	

	/**
	 * 标题
	 */
	private String title;
	
	/**
	 * 触发动作类型
	 */
	private String triggerType;
	
	/**
	 * 业务类型
	 */
	private String busClass;
	
	/**
	 * 业务场景
	 */
	private String busScene;

	/**
	 * 获取模板编号
	 * 
	 * @return
	 */
	@Column(name = "FCODE", length = 50, nullable = false, unique = true)
	public Integer getCode() {
		return code;
	}

	/**
	 * 设置模板编号
	 * 
	 * @param code
	 */
	public void setCode(Integer code) {
		this.code = code;
	}

	/**
	 * 获取格式化形式
	 * 
	 * @return
	 */
	@Column(name = "FPATTERN", length = 100, nullable = false)
	public String getFormatPattern() {
		return formatPattern;
	}

	/**
	 * 设置格式化形式
	 * 
	 * @param formatPattern
	 */
	public void setFormatPattern(String formatPattern) {
		this.formatPattern = formatPattern;
	}

	/**
	 * 获取消息内容格式描述
	 * 
	 * @return
	 */
	@Column(name = "FCONTENT_DESC", length = 500)
	public String getContentDesc() {
		return contentDesc;
	}

	/**
	 * 设置消息内容格式描述
	 * 
	 * @param formatPattern
	 */
	public void setContentDesc(String contentDesc) {
		this.contentDesc = contentDesc;
	}

	@Column(name = "SEND_TYPE", length = 8)
	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	@Column(name = "ENABLE")
	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	@Column(name = "OPERTYPE")
	public Integer getOpertype() {
		return opertype;
	}

	public void setOpertype(Integer opertype) {
		this.opertype = opertype;
	}

	@Column(name = "FTITLE")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "FTRIGGER_TYPE")
	public String getTriggerType() {
		return triggerType;
	}

	public void setTriggerType(String triggerType) {
		this.triggerType = triggerType;
	}

	@Column(name = "FBUS_CLASS")
	public String getBusClass() {
		return busClass;
	}

	public void setBusClass(String busClass) {
		this.busClass = busClass;
	}

	@Column(name = "FBUS_SCENE")
	public String getBusScene() {
		return busScene;
	}

	public void setBusScene(String busScene) {
		this.busScene = busScene;
	}
}
