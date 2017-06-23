package cn.fooltech.fool_ops.domain.wage.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsEntity;
import cn.fooltech.fool_ops.domain.basedata.entity.Member;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 工资明细
 *
 * @author xjh
 */
@Entity
@Table(name = "tbd_wage_detail")
public class WageDetail extends OpsEntity {

    private static final long serialVersionUID = 3035368185676637923L;

    /**
     * 关联工资
     */
    private Wage wage;

    /**
     * 人员
     */
    private Member member;

    /**
     * 工资公式
     */
    private WageFormula formula;

    /**
     * 输入值
     */
    private BigDecimal value;

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FWAGE_ID")
    public Wage getWage() {
        return wage;
    }

    public void setWage(Wage wage) {
        this.wage = wage;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FMEMBER_ID")
    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCOLUMN_ID")
    public WageFormula getFormula() {
        return formula;
    }

    public void setFormula(WageFormula formula) {
        this.formula = formula;
    }

    @Column(name = "FVALUE")
    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
