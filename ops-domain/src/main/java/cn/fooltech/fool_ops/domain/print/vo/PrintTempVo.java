package cn.fooltech.fool_ops.domain.print.vo;


import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;


/**
 * <p>打印</p>
 *
 * @author 林国坤
 * @version V1.0
 * @date 2016年3月22日下午03:29:50
 */
public class PrintTempVo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 3587393326547222401L;

    public static String PATH = "WEB-INF\\views\\print";
    public static String DOWM_PATH = "WEB-INF\\views";
    public static String SUFFIX = ".jsp";
    private String fid;
    /**
     * 模板路径
     */
    private String printTempUrl;

    /**
     * 机构Id
     */
    private String orgId;
    /**
     * 机构名称
     */
    private String orgName;
    /**
     * 默认模块标记
     */
    //private short defaultFlag;

    /**
     * 模板类型
     */
    private String type;
    /**
     * 行数
     */
    private Integer pageRow;

    private String typeName;
    /**
     * 修改时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /**
     * 创建时间
     */
    private String createTime;


    public String getPrintTempUrl() {
        return printTempUrl;
    }

    public void setPrintTempUrl(String printTempUrl) {
        this.printTempUrl = printTempUrl;
    }
    /*public short getDefaultFlag() {
		return defaultFlag;
	}

	public void setDefaultFlag(short defaultFlag) {
		this.defaultFlag = defaultFlag;
	}*/

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }


    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getPageRow() {
        return pageRow;
    }

    public void setPageRow(Integer pageRow) {
        this.pageRow = pageRow;
    }


}
