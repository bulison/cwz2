package cn.fooltech.fool_ops.domain.report.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 资产负债实体类
 *
 * @author xjh
 */
@Entity
@Table(name = "tbd_fiscal_profit_formula")
public class ProfitSheetFormula extends ProfitFormula {

    private static final long serialVersionUID = 6052693907355770389L;


}
