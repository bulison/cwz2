package cn.fooltech.fool_ops.domain.excelmap.vo;

import java.io.Serializable;
import java.util.Date;


/**
 * <p>Excel导入异常VO类</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015年10月27日
 */
public class ExcelExceptionVo implements Serializable {

    private static final long serialVersionUID = -5181411119650122144L;

    private String fid;
    private String code;//流水号
    private String describe;//描述
    private Integer rowNum;//行号

    private Date createTime;//创建时间
    private String creatorId;//创建人
    private String creatorName;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }
}
