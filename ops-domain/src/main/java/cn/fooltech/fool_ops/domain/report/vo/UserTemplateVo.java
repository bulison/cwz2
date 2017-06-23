package cn.fooltech.fool_ops.domain.report.vo;

import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class UserTemplateVo {

    String fid;
    String templateName;
    String reportId;
    String reportName;
    String creatorId;
    String creatorName;
    Date createTime;
    String orgId;
    String orgName;

    /**
     * 查询条件(Json字符串)
     */
    private String condition;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    /**
     * 获取前台表单传过来的单据明细集合
     *
     * @param bill
     */
    @SuppressWarnings("rawtypes")
    public List<UserTemplateDetailVo> getConditionList() {
        List<UserTemplateDetailVo> list = null;
        if (StringUtils.isNotBlank(this.condition)) {
            list = new ArrayList<UserTemplateDetailVo>();
            JSONArray array = JSONArray.fromObject(this.condition);
            List condition = (List) JSONArray.toCollection(array, UserTemplateDetailVo.class);
            Iterator iterator = condition.iterator();
            while (iterator.hasNext()) {
                UserTemplateDetailVo detail = (UserTemplateDetailVo) iterator.next();
                detail.setReportId(this.reportId);
                list.add(detail);
            }
        }
        return list;
    }
}
