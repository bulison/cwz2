/**
 *
 */
package cn.fooltech.fool_ops.domain.common.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsEntity;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;
import java.io.File;
import java.util.Date;


/**
 * <p>附件</p>
 *
 * @author ljb
 * @version 1.0
 * @date 2014年7月9日
 */
@Entity
@Table(name = "TCM_ATTACH")
public class Attach extends OpsEntity {

    /**
     * 类型 - 图片
     */
    public static final int TYPE_IMAGE = 1;
    /**
     * 类型 - 文件
     */
    public static final int TYPE_FILE = 2;
    /**
     * 类型 - 音频
     */
    public static final int TYPE_AUDIO = 3;
    /**
     * 类型 - 视频
     */
    public static final int TYPE_VIDEO = 4;
    /**
     * 状态 - 初始
     */
    public static final String STATUS_INIT = "0";
    /**
     * 状态 - 新增
     */
    public static final String STATUS_INSERT = "1";
    /**
     * 状态 - 修改
     */
    public static final String STATUS_UPDATE = "2";
    /**
     * 状态 - 删除
     */
    public static final String STATUS_DELETE = "3";
    private static final long serialVersionUID = -1853465444512624217L;
    /**
     * 类型
     */
    private int type = TYPE_IMAGE;
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
     * 创建人
     */
    private User creator;
    /**
     * 业务ID
     */
    private String busId;
    /**
     * 附件对象
     */
    private File file;
    /**
     * 状态，一般用于标明用户对附件的调整
     * 0 - 初始
     * 1 - 新增
     * 2 - 修改
     * 3 - 删除
     */
    private String status;

    /**
     * 获取类型
     *
     * @return
     */
    @Column(name = "FTYPE")
    public int getType() {
        return type;
    }

    /**
     * 设置类型
     *
     * @param type
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * 获取文件标题
     *
     * @return
     */
    @Column(name = "FTITLE", length = 100)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取文件相对路径
     * 附件中心处理后生成，与原始文件路径和文件名不一样。
     *
     * @return
     */
    @Column(name = "FFILE_PATH", length = 200)
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * 获取原始文件名
     *
     * @return
     */
    @Column(name = "FFILE_NAME", length = 100)
    public String getFileName() {
        return fileName;
    }

    /**
     * 设置原始文件名
     *
     * @param fileName
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * 获取创建时间
     *
     * @return
     */
    @Column(name = "FCREATE_TIME", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取创建人
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCREATOR_ID", updatable = false)
    public User getCreator() {
        return creator;
    }

    /**
     * 设置创建人
     *
     * @param creator
     */
    public void setCreator(User creator) {
        this.creator = creator;
    }

    /**
     * 获取业务主键
     * 各业务自定义，用于唯一标示对象，推荐使用业务主键fid
     *
     * @return
     */
    @Column(name = "FBUS_ID", length = 50)
    public String getBusId() {
        return busId;
    }

    /**
     * 设置业务主键
     *
     * @param busId
     */
    public void setBusId(String busId) {
        this.busId = busId;
    }

    /**
     * 获取附件文件
     *
     * @return
     */
    @Transient
    public File getFile() {
        return file;
    }

    /**
     * 设置附件文件
     *
     * @param file
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * 获取状态
     *
     * @return
     */
    @Column(name = "FSTATUS")
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态
     *
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

}
