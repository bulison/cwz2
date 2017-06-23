package cn.fooltech.fool_ops.domain.print.entity;


import cn.fooltech.fool_ops.domain.base.entity.OpsEntity;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;

import javax.persistence.*;
import java.util.Date;


/**
 * <p>打印</p>
 *
 * @author 林国坤
 * @version V1.0
 * @date 2016年3月22日下午03:29:50
 */
@Entity
@Table(name = "cfg_print_temp")
public class PrintTemp extends OpsEntity {

    public static final short DEFAULT_IS = 1;

    public static final short DEFAULT_NOT = 0;
    //仓储
    public static final short STORAGE = 1;
    //消费
    public static final short COST = 2;
    //
    public static final short WAGES = 3;
    /**
     *
     */
    private static final long serialVersionUID = 3587393326547222401L;
    /**
     * 模板路径
     */
    private String printTempUrl;
    /**
     * 机构
     */
    private Organization org;//机构
    /**
     * 默认模块标记
     */
    private short defaultFlag;
    /**
     * 模板类型
     */
    private AuxiliaryAttr auxiliaryAttr;


    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 行数
     */
    private Integer pageRow;

    /**
     * 创建时间
     */
    private Date createTime;

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FORG_ID")
    public Organization getOrg() {
        return org;
    }

    /**
     * 设置所属企业
     *
     * @param org
     */
    public void setOrg(Organization org) {
        this.org = org;
    }

    @Column(name = "PRING_TEMP_URL")
    public String getPrintTempUrl() {
        return printTempUrl;
    }

    public void setPrintTempUrl(String printTempUrl) {
        this.printTempUrl = printTempUrl;
    }

    @Column(name = "DEFAULT_FLAG")
    public short getDefaultFlag() {
        return defaultFlag;
    }

    public void setDefaultFlag(short defaultFlag) {
        this.defaultFlag = defaultFlag;
    }

    @Column(name = "FUPDATE_TIME", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name = "CREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取分类
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCODE")
    public AuxiliaryAttr getAuxiliaryAttr() {
        return auxiliaryAttr;
    }

    public void setAuxiliaryAttr(AuxiliaryAttr auxiliaryAttr) {
        this.auxiliaryAttr = auxiliaryAttr;
    }

    @Column(name = "page_row")
    public Integer getPageRow() {
        return pageRow;
    }

    public void setPageRow(Integer pageRow) {
        this.pageRow = pageRow;
    }
}
