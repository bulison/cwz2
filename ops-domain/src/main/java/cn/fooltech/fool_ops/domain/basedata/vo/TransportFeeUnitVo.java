package cn.fooltech.fool_ops.domain.basedata.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import org.hibernate.validator.constraints.Length;

/**
 * 运输费计价单位(辅助属性)VO
 * @author hjr
 * 2017-04-20
 */
public class TransportFeeUnitVo implements Serializable{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 4343572895391078870L;

	/**
     * 主键
     */
    protected String fid;

    /**
     * 编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    @Length(max = 200, message = "描述不能超过{max}个字符")
    private String describe;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 数据是否有效
     */
    private Short enable;

    /**
     * 1为子节点，0为父节点
     */
    private Short flag;

    /**
     * 系统标识
     */
    private Short systemSign;
    /**
     * 换算关系
     */
    private BigDecimal scale;
	public String getFid() {
		return fid;
	}
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public String getDescribe() {
		return describe;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public Short getEnable() {
		return enable;
	}
	public Short getFlag() {
		return flag;
	}
	public Short getSystemSign() {
		return systemSign;
	}
	public BigDecimal getScale() {
		return scale;
	}
	public void setFid(String fid) {
		this.fid = fid;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public void setEnable(Short enable) {
		this.enable = enable;
	}
	public void setFlag(Short flag) {
		this.flag = flag;
	}
	public void setSystemSign(Short systemSign) {
		this.systemSign = systemSign;
	}
	public void setScale(BigDecimal scale) {
		this.scale = scale;
	}
    
}
