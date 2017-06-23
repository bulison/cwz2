//package cn.fooltech.fool_ops.schedulejob.controller;
//
//import cn.fooltech.fool_ops.schedulejob.domain.job.JobDO;
//import cn.fooltech.fool_ops.schedulejob.domain.job.JobDetailDO;
//import cn.fooltech.fool_ops.schedulejob.domain.job.TriggerDO;
//import cn.fooltech.fool_ops.schedulejob.service.QuartzJobDetailService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.HashSet;
//import java.util.Set;
//
///**
// * Created by Administrator on 2016/11/29.
// */
//
//@RestController("Test2")
//@RequestMapping("/api/test222")
//public class Test233Controller {
//    @Autowired
//    private QuartzJobDetailService quartzJobDetailService;
//    @ApiOperation("添加任务TestJob")
//    @GetMapping("/addTest")
//    public ResponseEntity<Boolean> addTestJob(){
//        JobDetailDO jobDetailDO=new  JobDetailDO();
//        JobDO jobDO=new JobDO();
//        jobDO.setGroup("作业所属组织");
//        jobDO.setDescription("作业描述");
//        jobDO.setName("作业名称");
//        //  System.out.println("cn.fooltech.fool_ops.schedulejob.job.HelloJob");
//        jobDO.setTargetClass("cn.fooltech.fool_ops.schedulejob.job.HelloJob");
//        //jobBO.
//        jobDetailDO.setJobDO(jobDO);
//
//        TriggerDO triggerDO=new TriggerDO();
//        triggerDO.setGroup("触发器所属组织");
//        triggerDO.setName("触发名称");
//        triggerDO.setDescription("触发器描述");
//        //Cron Expression
//        //参考
//        //http://quartz-scheduler.org/documentation/quartz-2.2.x/tutorials/tutorial-lesson-06
//        //http://blog.csdn.net/chh_jiang/article/details/4603529
//        triggerDO.setCronExpression("0 0/1 * * * ?");
//
//
////        TriggerDO triggerDO1=new TriggerDO();
////        triggerDO1.setGroup("触发器所属组织1");
////        triggerDO1.setName("触发名称1");
////        triggerDO1.setDescription("触发器描述1");
////        triggerDO1.setCronExpression("0 0/0.1 * * * ?");
//
//        Set<TriggerDO> triggerDOs=new HashSet<TriggerDO>();
//        triggerDOs.add(triggerDO);
////        triggerDOs.add(triggerDO1);
//        jobDetailDO.setTriggerDOs(triggerDOs);
//
//        boolean result = quartzJobDetailService.add(jobDetailDO);
//        return ResponseEntity.status(HttpStatus.CREATED).body(result);
//    }
//}
