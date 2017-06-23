package cn.fooltech.fool_ops.schedulejob.schedule;

import cn.fooltech.fool_ops.domain.basedata.service.TodayCostAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/1/11.
 */
@Service
public class TodayCostAnalysisScheduleService  implements OpsSchedule {

    @Autowired
    TodayCostAnalysisService todayCostAnalysisService;

    @Override
    public void execute() {
        todayCostAnalysisService.genRoute();
        todayCostAnalysisService.analysis();
    }
}
