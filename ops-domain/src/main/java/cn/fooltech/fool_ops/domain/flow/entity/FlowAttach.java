package cn.fooltech.fool_ops.domain.flow.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 流程附件
 *
 * @author yrl
 * @version 1.0
 * @date 2016年7月15日
 */
@Entity
@Table(name = "tflow_attach")
public class FlowAttach extends OpsEntity {
    private static final long serialVersionUID = -7277197323763160934L;
    private String planId;
    private String taskId;
    private String flowRecordId;
    private String attachId;

    @Column(name = "FPLAN_ID")
    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    @Column(name = "FTASK_ID")
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Column(name = "FFLOW_RECORD_ID")
    public String getFlowRecordId() {
        return flowRecordId;
    }

    public void setFlowRecordId(String flowRecordId) {
        this.flowRecordId = flowRecordId;
    }

    @Column(name = "FATTACH_ID")
    public String getAttachId() {
        return attachId;
    }

    public void setAttachId(String attachId) {
        this.attachId = attachId;
    }


}
