package cn.fooltech.fool_ops.domain.excelmap.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;
import java.util.Date;


/**
 * <p>Excel导入异常实体类</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015年10月27日
 */
@Entity
@Table(name = "cfg_excel_exception")
public class ExcelException extends OpsOrgEntity {

    private static final long serialVersionUID = 1304533198515051767L;

    private String code;//流水号
    private Integer rowNum;//行号
    private String describe;//描述

    private Date createTime;//创建时间
    private User creator;//创建人

    @Column(name = "FCODE")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "FDESCRIBE")
    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    @Column(name = "FCREATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @JoinColumn(name = "FCREATOR_ID")
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    @Column(name = "FROW_NUM")
    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }
}
