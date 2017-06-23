package cn.fooltech.fool_ops.domain.report.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 资产负债实体类
 *
 * @author xjh
 */
@Entity
@Table(name = "tbd_fiscal_balancesheet_formula")
public class BalanceSheetFormula extends SheetFormula {

    private static final long serialVersionUID = -1652303843913541347L;

}
