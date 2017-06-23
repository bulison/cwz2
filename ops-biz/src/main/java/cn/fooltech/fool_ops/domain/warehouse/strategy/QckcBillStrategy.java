package cn.fooltech.fool_ops.domain.warehouse.strategy;

import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * 期初库存单据策略
 * Created by xjh on 2016/12/21.
 */
@Component("ops.QckcBillStrategy")
public class QckcBillStrategy implements BillStrategy {

    /*
    审核：
    期间总库存金额表:
    上期结余数量 = 上期结余数量 + 单据数量；
    上期结余金额 = 上期结余金额 + 单据金额；
    本期结存数量 = 本期结存数量 + 单据数量；
    本期结存金额 = 本期结存金额 + 单据金额；
    上期结存单价 = 上期结存金额 / 上期结存数量；
    本期结存单价 = 本期结存金额 / 本期结存数量；*/
    private List<CalMethod> auditTotalMethods = Lists.newArrayList(

            new CalMethod("preQuentity", "preQuentity", CalMethod.Compute.Add, "accountQuentity"),
            new CalMethod("preAmount", "preAmount", CalMethod.Compute.Add, "type"),
            new CalMethod("accountQuentity", "accountQuentity", CalMethod.Compute.Add, "accountQuentity"),
            new CalMethod("accountAmount", "accountAmount", CalMethod.Compute.Add, "type"),

            new CalMethod("prePrice", "preAmount", CalMethod.Compute.Div, "preQuentity", CalMethod.Ref.R11),
            new CalMethod("accountPrice", "accountAmount", CalMethod.Compute.Div, "accountQuentity", CalMethod.Ref.R11)

    );

    /*
    审核：
    期间分仓库存表：
    上期结余数量 = 上期结余数量 + 单据数量；
    上期结余金额 = 上期结余金额 + 单据金额；
    本期结存数量 = 本期结存数量 + 单据数量；
    本期结存金额 = 本期结存金额 + 单据金额；
    上期结存单价 = 上期结存金额 / 上期结存数量；
    本期结存单价 = 本期结存金额 / 本期结存数量；
    */
    private List<CalMethod> auditInBranchMethods = Lists.newArrayList(

            new CalMethod("preQuentity", "preQuentity", CalMethod.Compute.Add, "accountQuentity"),
            new CalMethod("preAmount", "preAmount", CalMethod.Compute.Add, "type"),
            new CalMethod("accountQuentity", "accountQuentity", CalMethod.Compute.Add, "accountQuentity"),
            new CalMethod("accountAmount", "accountAmount", CalMethod.Compute.Add, "type"),

            new CalMethod("prePrice", "preAmount", CalMethod.Compute.Div, "preQuentity", CalMethod.Ref.R11),
            new CalMethod("accountPrice", "accountAmount", CalMethod.Compute.Div, "accountQuentity", CalMethod.Ref.R11)

    );

    /*
    审核：
    即时分仓库存表:
    结存数量 = 结存数量 + 单据数量；
    结存金额 = 结存金额 + 单据金额；
    结存单价 = 结存金额 / 结存数量；（如果结存数量=0，即不改变结存单价）
    */
    private List<CalMethod> auditInRealTimeMethods = Lists.newArrayList(

            new CalMethod("accountQuentity", "accountQuentity", CalMethod.Compute.Add, "accountQuentity"),
            new CalMethod("accountAmount", "accountAmount", CalMethod.Compute.Add, "type"),
            new CalMethod("accountPrice", "accountAmount", CalMethod.Compute.Div, "accountQuentity", CalMethod.Ref.R11)
    );


    /*
    作废：
    期间总库存金额表:
    上期结余数量 = 上期结余数量 - 单据数量；
    上期结余金额 = 上期结余金额 - 单据金额；
    本期结存数量 = 本期结存数量 - 单据数量；
    本期结存金额 = 本期结存金额 - 单据金额；
    上期结存单价 = 上期结存金额 / 上期结存数量；
    本期结存单价 = 本期结存金额 / 本期结存数量；*/
    private List<CalMethod> cancleTotalMethods = Lists.newArrayList(

            new CalMethod("preQuentity", "preQuentity", CalMethod.Compute.Sub, "accountQuentity"),
            new CalMethod("preAmount", "preAmount", CalMethod.Compute.Sub, "type"),
            new CalMethod("accountQuentity", "accountQuentity", CalMethod.Compute.Sub, "accountQuentity"),
            new CalMethod("accountAmount", "accountAmount", CalMethod.Compute.Sub, "type"),

            new CalMethod("prePrice", "preAmount", CalMethod.Compute.Div, "preQuentity", CalMethod.Ref.R11),
            new CalMethod("accountPrice", "accountAmount", CalMethod.Compute.Div, "accountQuentity", CalMethod.Ref.R11)

    );

    /*
    作废：
    期间分仓库存表：
    上期结余数量 = 上期结余数量 - 单据数量；
    上期结余金额 = 上期结余金额 - 单据金额；
    本期结存数量 = 本期结存数量 - 单据数量；
    本期结存金额 = 本期结存金额 - 单据金额；
    上期结存单价 = 上期结存金额 / 上期结存数量；
    本期结存单价 = 本期结存金额 / 本期结存数量；
    */
    private List<CalMethod> cancleInBranchMethods = Lists.newArrayList(

            new CalMethod("preQuentity", "preQuentity", CalMethod.Compute.Sub, "accountQuentity"),
            new CalMethod("preAmount", "preAmount", CalMethod.Compute.Sub, "type"),
            new CalMethod("accountQuentity", "accountQuentity", CalMethod.Compute.Sub, "accountQuentity"),
            new CalMethod("accountAmount", "accountAmount", CalMethod.Compute.Sub, "type"),

            new CalMethod("prePrice", "preAmount", CalMethod.Compute.Div, "preQuentity", CalMethod.Ref.R11),
            new CalMethod("accountPrice", "accountAmount", CalMethod.Compute.Div, "accountQuentity", CalMethod.Ref.R11)

    );

    /*
    作废：
    即时分仓库存表:
    结存数量 = 结存数量 - 单据数量；
    结存金额 = 结存金额 - 单据金额；
    结存单价 = 结存金额 / 结存数量；（如果结存数量=0，即不改变结存单价）
    */
    private List<CalMethod> cancleInRealTimeMethods = Lists.newArrayList(

            new CalMethod("accountQuentity", "accountQuentity", CalMethod.Compute.Sub, "accountQuentity"),
            new CalMethod("accountAmount", "accountAmount", CalMethod.Compute.Sub, "type"),
            new CalMethod("accountPrice", "accountAmount", CalMethod.Compute.Div, "accountQuentity", CalMethod.Ref.R11)
    );

    @Override
    public List<CalMethod> getCancleTotalStrategy() {
        return cancleTotalMethods;
    }

    @Override
    public List<CalMethod> getCancleInBranchStrategy() {
        return cancleInBranchMethods;
    }

    @Override
    public List<CalMethod> getCancleInRealTimeStrategy() {
        return cancleInRealTimeMethods;
    }

    @Override
    public List<CalMethod> getAuditTotalStrategy() {
        return auditTotalMethods;
    }

    @Override
    public List<CalMethod> getAuditInBranchStrategy() {
        return auditInBranchMethods;
    }

    @Override
    public List<CalMethod> getAuditInRealTimeStrategy() {
        return auditInRealTimeMethods;
    }

    @Override
    public boolean isSupport(int billType) {
        return billType == WarehouseBuilderCodeHelper.qckc;
    }

	@Override
	public boolean getJudgmentCancle() {
		return false;
	}
    
}
