package cn.fooltech.fool_ops.domain.wage.vo;

import cn.fooltech.fool_ops.domain.wage.entity.WageFormula;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>表单传输对象 - 工资</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015-12-21 15:55:44
 */
public class WageVo implements Serializable {

    private static final long serialVersionUID = -7025166770563009973L;

    @NotEmpty(message = "月份必填")
    private String wageDate;//月份

    @Length(max = 200, message = "备注不能超过200个字符")
    private String remark;//备注
    private String createTime;//创建时间
    private String auditorTime;//审核时间
    private String updateTime;//更新时间
    private String fid;//主键

    @NotEmpty(message = "部门必填")
    private String deptId;//部门
    private String deptName;

    private String creatorId;//创建人
    private String creatorName;

    private String auditorId;//审核人
    private String auditorName;

    private String memberId;//人员ID，用于搜索

    private String details;//明细json字符串
    private String year;//年份(统计工资用)

    /**
     * 获取前台表单传过来的单据明细集合
     */
    @JsonIgnore
    public List<List<WageDetailVo>> getDetailList(List<WageFormulaVo> viewFormulas) {
        List<List<WageDetailVo>> allList = Lists.newArrayList();
        if (StringUtils.isNotBlank(this.details)) {
            JSONArray array = JSONArray.fromObject(this.details);
            for (int i = 0; i < array.size(); i++) {
                List<WageDetailVo> datas = Lists.newArrayList();
                JSONObject json = (JSONObject) array.get(i);

                for (WageFormulaVo formulaVo : viewFormulas) {
//                	Short isView = formulaVo.getIsView();
//                	if(isView==0)continue;
                    WageDetailVo detailVo = new WageDetailVo();
                    String memberId = json.getString("memberId");
                    detailVo.setMemberId(memberId);
                    detailVo.setFormulaId(formulaVo.getFid());

                    String valueStr = json.getString(formulaVo.getFid());
                    if (valueStr != null) {
                        BigDecimal value = new BigDecimal(valueStr);
                        detailVo.setValue(value);
                    } else {
                        if (formulaVo.getColumnType() == WageFormula.TYPE_INPUT) {
                            detailVo.setValue(formulaVo.getDefaultValue());
                        } else {
                            detailVo.setValue(BigDecimal.ZERO);
                        }
                    }
                    datas.add(detailVo);
                }

                allList.add(datas);

            }
        }
        return allList;
    }

    public String getWageDate() {
        return this.wageDate;
    }

    public void setWageDate(String wageDate) {
        this.wageDate = wageDate;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAuditorTime() {
        return this.auditorTime;
    }

    public void setAuditorTime(String auditorTime) {
        this.auditorTime = auditorTime;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getFid() {
        return this.fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
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

    public String getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(String auditorId) {
        this.auditorId = auditorId;
    }

    public String getAuditorName() {
        return auditorName;
    }

    public void setAuditorName(String auditorName) {
        this.auditorName = auditorName;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
