package cn.fooltech.fool_ops.domain.warehouse.strategy;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by xjh on 2016/12/21.
 */
public interface BillStrategy {

    /**
     * 获取审核通过时的总仓计算策略
     *
     * @return
     */
    public List<CalMethod> getAuditTotalStrategy();

    /**
     * 获取审核通过时的分仓出仓计算策略
     *
     * @return
     */
    public List<CalMethod> getAuditInBranchStrategy();

    /**
     * 获取审核通过时的分仓出仓计算策略
     *
     * @return
     */
    public default List<CalMethod> getAuditOutBranchStrategy() {
        return Collections.emptyList();
    }

    /**
     * 获取审核通过时的即时分仓入库策略类
     *
     * @return
     */
    public List<CalMethod> getAuditInRealTimeStrategy();

    /**
     * 获取审核通过时的即时分仓出仓策略类
     *
     * @return
     */
    public default List<CalMethod> getAuditOutRealTimeStrategy() {
        return Collections.emptyList();
    }

    /**
     * 获取作废时的总仓计算策略
     *
     * @return
     */
    public List<CalMethod> getCancleTotalStrategy();

    /**
     * 获取作废时的分仓入仓计算策略
     *
     * @return
     */
    public List<CalMethod> getCancleInBranchStrategy();

    /**
     * 获取作废时的分仓出仓计算策略
     *
     * @return
     */
    public default List<CalMethod> getCancleOutBranchStrategy() {
        return Collections.emptyList();
    }

    /**
     * 获取作废时的即时分仓入仓策略类
     *
     * @return
     */
    public List<CalMethod> getCancleInRealTimeStrategy();
    /**
     * 获取作废时判断是否可以作废
     *
     * @return
     */
    public boolean getJudgmentCancle();

    /**
     * 获取作废时的即时分仓出仓策略类
     *
     * @return
     */
    public default List<CalMethod> getCancleOutRealTimeStrategy() {
        return Collections.emptyList();
    }

    /**
     * 判断是否支持当前单据类型
     *
     * @param billType
     * @return
     */
    public boolean isSupport(int billType);
}
