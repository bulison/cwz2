package cn.fooltech.fool_ops.domain.flow.vo;

import cn.fooltech.fool_ops.domain.common.vo.SimpleAttachVo;

import java.util.List;
import java.util.Map;

/**
 * 事件操作流程vo
 *
 * @author yrl
 * @version 1.0
 * @date 2016年7月16日
 */
public class OperateRecordVo {
    private String createTime;//创建时间
    private String describe;//描述
    private String operateName;//操作流程名称
    private List<SimpleAttachVo> fileMap;//多文件下载，键是文件名，值是文件id

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getOperateName() {
        return operateName;
    }

    public void setOperateName(String operateName) {
        this.operateName = operateName;
    }

    public List<SimpleAttachVo> getFileMap() {
        return fileMap;
    }

    public void setFileMap(List<SimpleAttachVo> fileMap) {
        this.fileMap = fileMap;
    }
}
