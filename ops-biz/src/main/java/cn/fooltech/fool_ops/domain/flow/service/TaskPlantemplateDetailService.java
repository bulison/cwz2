package cn.fooltech.fool_ops.domain.flow.service;


import cn.fooltech.fool_ops.domain.base.service.AbstractBaseService;
import cn.fooltech.fool_ops.domain.flow.entity.*;
import cn.fooltech.fool_ops.domain.flow.repository.TaskPlantemplateDetailRepository;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

/**
 * 服务类
 */
@Service
public class TaskPlantemplateDetailService extends AbstractBaseService<TaskPlantemplateDetail, String> {


    @Autowired
    private TaskPlantemplateDetailRepository repository;


    @Override
    public CrudRepository<TaskPlantemplateDetail, String> getRepository() {
        return repository;
    }


    /**
     * 新增
     * @param plan
     * @param task
     * @param taskPlantemplate
     * @param planTemplate
     * @param planTemplateDetail
     * @return
     */
    @Transactional
    public TaskPlantemplateDetail genDataByTask(Plan plan, Task task, TaskPlantemplate taskPlantemplate,
                                                PlanTemplate planTemplate, PlanTemplateDetail planTemplateDetail) {

        TaskPlantemplateDetail entity = new TaskPlantemplateDetail();
        entity.setFiscalAccount(task.getFiscalAccount());
        entity.setCreator(SecurityUtil.getCurrentUser());
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        entity.setOrg(task.getOrg());


        entity.setBill(taskPlantemplate);

        entity.setPlan(plan);
        entity.setTask(task);
        entity.setPlanTemplate(planTemplate);
        entity.setPlanTemplateDetail(planTemplateDetail);
        entity.setStatus(task.getStatus());
        entity.setCreateTime(DateUtilTools.now());
        entity.setUpdateTime(DateUtilTools.now());

        repository.save(entity);

        return entity;
    }
}
