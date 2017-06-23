package cn.fooltech.fool_ops.schedulejob.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.fooltech.fool_ops.domain.basedata.service.PurchasePriceService;
import cn.fooltech.fool_ops.domain.basedata.service.TransportPriceService;
import cn.fooltech.fool_ops.domain.flow.service.PlanService;
import cn.fooltech.fool_ops.domain.sysman.service.SmgOrgAttrService;

/**
 * 定时任务入口
 * Created by xjh on 2016/12/20.
 */
@Component
@Configurable
@EnableScheduling
public class ScheduleStarter {

    @Autowired
    private TestScheduleService testScheduleService;

    @Autowired
    private BillRuleScheduleService billRuleScheduleService;

    @Autowired
    private TodayCostAnalysisScheduleService todayCostAnalysisScheduleService;

    @Autowired
    private TaskScheduleService taskScheduleService;
    
    @Autowired
    private SmgOrgAttrService orgAttrService;
    
    @Autowired
    private PlanService planService;
    @Autowired
    private PurchasePriceService purchasePriceService;
    @Autowired
    private TransportPriceService transportPriceService;
    /**
     * 提示调度
     */
    //@Scheduled(cron="0 0/1 * * * ? ")
    public void test(){
        testScheduleService.execute();
    }

    /**
     * 0点更新单据延时单号
     */
    @Scheduled(cron="0 0 0 * * ?")
    public void updateBillCode(){
        billRuleScheduleService.execute();
    }

    /**
     * 0点30分更新成本分析表
     */
    @Scheduled(cron="0 5 0 * * ?")
    public void todayCostAnalysis(){
        todayCostAnalysisScheduleService.execute();
    }

    /**
     * 1点更新事件延迟
     */
    @Scheduled(cron="0 10 0 * * ?")
    public void updateTaskDelay(){
        taskScheduleService.execute();
    }
    
    /**
     * 0点检查资金池预警额度,如果不高于，则需要预警,自动发送信息给“发送预警人”
     */
    @Scheduled(cron="0 15 0 * * ?")
    public void checkWarningQuota(){
    	orgAttrService.checkWarningQuota();
    }
    /**
     * 收益率轮询
     */
    @Scheduled(cron="0 15 0 * * ?")
    public void yieldPolling(){
    	try {
			planService.yieldPolling();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    /**
     * 货品价格报价过期提示
     */
    @Scheduled(cron="0 0 0 * * ?")
    public void checkExpiredPurchasePrice(){
    	try {
    		purchasePriceService.checkExpiredPurchasePrice();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    /**
     * 运输费报价过期提示
     */
    @Scheduled(cron="0 0 0 * * ?")
    public void checkExpiredTransportPrice(){
    	try {
    		transportPriceService.checkExpiredTransportPrice();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
