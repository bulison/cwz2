package cn.fooltech.fool_ops.domain.flow.service;


import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.AbstractBaseService;
import cn.fooltech.fool_ops.domain.flow.entity.Plan;
import cn.fooltech.fool_ops.domain.flow.entity.TaskPlantemplate;
import cn.fooltech.fool_ops.domain.flow.repository.TaskPlantemplateRepository;
import cn.fooltech.fool_ops.domain.flow.vo.PlanTemplateVo;
import cn.fooltech.fool_ops.domain.flow.vo.TemplateData;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 计划事件关联模板服务类
 */
@Service
public class TaskPlantemplateService extends AbstractBaseService<TaskPlantemplate, String> {


    @Autowired
    private TaskPlantemplateRepository repository;

    @Override
    public CrudRepository<TaskPlantemplate, String> getRepository() {
        return repository;
    }

    /**
     * 根据模板数据生成
     * @param template
     * @return
     */
    @Transactional
    public TaskPlantemplate genDataByTemplate(TemplateData template, Plan plan){

        TaskPlantemplate tp = new TaskPlantemplate();
        tp.setAmount(template.getTotalAmount());
        tp.setCreateTime(DateUtilTools.now());
        tp.setCreator(SecurityUtil.getCurrentUser());

        tp.setDate(template.getStartDate());
        tp.setDeliveryPlace(template.getPublishAddress());
        tp.setFiscalAccount(plan.getFiscalAccount());
        tp.setPlan(plan);
        tp.setOrg(plan.getOrg());
        tp.setPlanTemplate(template.getTemplate());
        tp.setReceiptPlace(template.getReceiveAddress());
        tp.setShipmentType(template.getShipmentType());
        tp.setTemplateType(template.getFlag());
        tp.setTransportId(template.getRouteIds());
        tp.setTransportType(template.getTransportType());
        tp.setUpdateTime(DateUtilTools.now());

        tp.setTransportQuentity(template.getTransportQuentity());

        if(template.getFlag()== PlanTemplateVo.TEMPLATE_TYPE_SALE){
            String customerId = template.getCustomer()==null?null:template.getCustomer().getFid();
            tp.setCustomerId(customerId);
        }else{
            String supplierId = template.getSupplier()==null?null:template.getSupplier().getFid();
            tp.setCustomerId(supplierId);
        }

        repository.save(tp);

        return tp;
    }
}
