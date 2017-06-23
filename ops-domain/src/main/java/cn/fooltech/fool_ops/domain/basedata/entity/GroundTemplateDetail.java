package cn.fooltech.fool_ops.domain.basedata.entity;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 场地费报价模板明细
 */
@ApiModel("场地费报价模板明细")
@ToString
@NoArgsConstructor
@Entity
@Table(name = "tsb_ground_template_detail")
public class GroundTemplateDetail {

    private static final long serialVersionUID = 1L;


    //主键
    private String id;


    //模板ID
    private GroundTemplate template;


    //运输费用ID
    private String transportCostId;


    //运输单位ID
    private String transportUnitId;


    //成本费用标识
    private Integer costSign;


    //描述

    private String describe;


    //创建时间
    private Date createTime;


    //创建人
    private String creatorId;


    //修改时间戳
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date updateTime;


    //组织ID
    private String orgId;


    //账套ID
    private String accId;

    @Id
    @Column(name = "FID", unique = true, nullable = false, insertable = true, updatable = false, length = 32)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = {})
    @JoinColumn(name = "FTEMPLATE_ID")
    public GroundTemplate getTemplate() {
        return template;
    }

    public void setTemplate(GroundTemplate template) {
        this.template = template;
    }

    @Column(name = "FTRANSPORT_COST_ID")
    public String getTransportCostId() {
        return transportCostId;
    }

    public void setTransportCostId(String transportCostId) {
        this.transportCostId = transportCostId;
    }

    @Column(name = "FTRANSPORT_UNIT_ID")
    public String getTransportUnitId() {
        return transportUnitId;
    }

    public void setTransportUnitId(String transportUnitId) {
        this.transportUnitId = transportUnitId;
    }

    @Column(name = "FCOST_SIGN")
    public Integer getCostSign() {
        return costSign;
    }

    public void setCostSign(Integer costSign) {
        this.costSign = costSign;
    }

    @Column(name = "FDESCRIBE")
    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    @Column(name = "FCREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "FCREATOR_ID")
    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    @Column(name = "FUPDATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name = "FORG_ID")
    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }


    @Column(name = "FACC_ID")
    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }
}