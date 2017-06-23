package cn.fooltech.fool_ops.domain.rate.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalPeriod;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;

/**
 * 经营收益率分析表实体类
 * @author hjr
 *	2017-4-14
 */
@Entity
@Table(name="tbd_fiscal_business")
public class FiscalBusiness extends BusinessFormula{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1156973684422312767L;
	/**
	 * 会计期间
	 */
	private FiscalPeriod period;
	/**
	 * 本期金额
	 */
	private BigDecimal value;
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "FVOUCHER_PERIOD_ID", nullable = false)
	public FiscalPeriod getPeriod() {
		return period;
	}
	@Column(name="FVALUE")
	public BigDecimal getValue() {
		return value;
	}
	public void setPeriod(FiscalPeriod period) {
		this.period = period;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}

}
