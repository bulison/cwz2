package cn.fooltech.fool_ops.domain.common.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>表单传输对象 - 附件</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2014年12月24日
 */
public class AttachVo implements Serializable {

    private static final long serialVersionUID = 1127744775321855101L;

    private String fid;

    /**
     * 类型
     */
    private int type;

    /**
     * 标题
     */
    private String title;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人的ID
     */
    private String creatorFid;

    /**
     * 创建人的姓名
     */
    private String creatorName;

    /**
     * 业务ID
     */
    private String busId;

    /**
     * 状态，一般用于标明用户对附件的调整
     * 0 - 初始
     * 1 - 新增
     * 2 - 修改
     * 3 - 删除
     */
    private String status;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreatorFid() {
        return creatorFid;
    }

    public void setCreatorFid(String creatorFid) {
        this.creatorFid = creatorFid;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

	/*public int getStatus() {
        return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}*/


}
