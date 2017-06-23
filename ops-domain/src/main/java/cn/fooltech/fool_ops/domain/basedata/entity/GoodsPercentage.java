package cn.fooltech.fool_ops.domain.basedata.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 货品提成表
 * </p>
 * 
 * @author cwz
 * @date 2017年6月19日
 */
@Entity
@Table(name = "tbd_goods_percentage")
public class GoodsPercentage extends OpsOrgEntity {

	private static final long serialVersionUID = 1L;

	// 货品ID
	@ApiModelProperty(value = "货品ID")
	private Goods goods;

	// 提成点数【0-100之间】
	@ApiModelProperty(value = "提成点数配【0-100之间】")
	private BigDecimal percentage;

	// 是否最后录入
	@ApiModelProperty(value = "是否最后录入")
	private Integer isLast;

	// 修改时间戳【初始值为当前时间】
	@ApiModelProperty(value = "修改时间戳【初始值为当前时间】")
	private Date updateTime;

	// 账套ID
	@ApiModelProperty(value = "账套ID")
	private FiscalAccount fiscalAccount;

	/**
	 * 获取货品
	 *
	 * @return
	 */
	@ManyToOne
	@JoinColumn(name = "FGOODS_ID")
	public Goods getGoods() {
		return goods;
	}

	/**
	 * 设置货品
	 *
	 * @param goods
	 */
	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	/**
	 * 获取提成点数配【0-100之间】
	 * 
	 * @return
	 */
	@Column(name = "FPERCENTAGE")
	public BigDecimal getPercentage() {
		return percentage;
	}

	/**
	 * 设置提成点数配【0-100之间】
	 * 
	 * @param percentage
	 */
	public void setPercentage(BigDecimal percentage) {
		this.percentage = percentage;
	}

	/**
	 * 获取是否最后录入
	 * 
	 * @return
	 */
	@Column(name = "FIS_LAST")
	public Integer getIsLast() {
		return isLast;
	}

	/**
	 * 设置是否最后录入
	 * 
	 * @param isLast
	 */
	public void setIsLast(Integer isLast) {
		this.isLast = isLast;
	}

	/**
	 * 获取修改时间戳【初始值为当前时间】
	 * 
	 * @return
	 */
	@Column(name = "FUPDATE_TIME")
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * 设置修改时间戳【初始值为当前时间】
	 * 
	 * @param updateTime
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * 获取财务账套
	 *
	 * @return
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FACC_ID")
	public FiscalAccount getFiscalAccount() {
		return fiscalAccount;
	}

	/**
	 * 设置财务账套
	 *
	 * @param fiscalAccount
	 */
	public void setFiscalAccount(FiscalAccount fiscalAccount) {
		this.fiscalAccount = fiscalAccount;
	}

}