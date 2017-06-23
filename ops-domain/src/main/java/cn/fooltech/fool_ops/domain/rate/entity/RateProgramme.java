package cn.fooltech.fool_ops.domain.rate.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import com.google.common.collect.Lists;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 收益率计算方案
 *
 * @author xjh
 */
@Entity
@Table(name = "rate_programme")
public class RateProgramme extends OpsOrgEntity {

    private static final long serialVersionUID = 6527002048686988079L;
    private static final Integer default_cycle=30; //周期默认30日
    /**
     * 方案名称
     */
    private String name;

    /**
     * 创建人
     */
    private User creator;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 计算周期
     */
    private Integer cycle=default_cycle;
    /**
     * 利润
     */
    private BigDecimal profit;
    /**
     * 利润率
     */
    private BigDecimal profitRate;
    /**
     * 周期收益率
     */
    private BigDecimal cycleProfitRate;
    /**
     * 账套ID
     */
    private FiscalAccount fiscalAccount;
    private List<RateProgrammeRecord> records = Lists.newArrayList();

    @Column(name = "FNAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCREATOR_ID", nullable = false)
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FCREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "rateProgramme")
    public List<RateProgrammeRecord> getRecords() {
        return records;
    }

    public void setRecords(List<RateProgrammeRecord> records) {
        this.records = records;
    }

    @Column(name = "FCYCLE")
    public Integer getCycle() {
        return cycle;
    }

    public void setCycle(Integer cycle) {
        this.cycle = cycle;
    }
    @Column(name = "FPROFIT")
	public BigDecimal getProfit() {
		return profit;
	}
    @Column(name = "FPROFIT_RATE")
	public BigDecimal getProfitRate() {
		return profitRate;
	}
    @Column(name = "FCYCLE_PROFIT_RATE")
	public BigDecimal getCycleProfitRate() {
		return cycleProfitRate;
	}
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FACC_ID", nullable = false)
	public FiscalAccount getFiscalAccount() {
		return fiscalAccount;
	}

	public void setProfit(BigDecimal profit) {
		this.profit = profit;
	}

	public void setProfitRate(BigDecimal profitRate) {
		this.profitRate = profitRate;
	}

	public void setCycleProfitRate(BigDecimal cycleProfitRate) {
		this.cycleProfitRate = cycleProfitRate;
	}

	public void setFiscalAccount(FiscalAccount fiscalAccount) {
		this.fiscalAccount = fiscalAccount;
	}
    
}
