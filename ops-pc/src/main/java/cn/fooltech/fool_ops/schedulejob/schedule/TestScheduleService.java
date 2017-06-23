package cn.fooltech.fool_ops.schedulejob.schedule;

import org.springframework.stereotype.Service;

/**
 * Created by Derek on 2016/12/20.
 */
@Service
public class TestScheduleService implements OpsSchedule {

    @Override
    public void execute() {
        System.out.println("==========TestScheduleService execute!=============");
    }
}
