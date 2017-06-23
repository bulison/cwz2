package cn.fooltech.fool_ops.domain.common.vo;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 普通树状VO对象，适用于EasyUI
 *
 * @author rqh
 * @version 1.0
 * @date 2015年4月16日
 */
public class CommonTreeVo implements Serializable {

    private static final long serialVersionUID = 8365910664403320814L;

    /**
     * ID
     */
    private String id;

    /**
     * 名称
     */
    private String text;

    /**
     * 节点状态
     */
    private String state;

    /**
     * 是否选中
     */
    private String checked;

    /**
     * 节点中其他属性集合
     */
    private Map<String, Object> attributes;

    /**
     * 子节点集合
     */
    private LinkedHashSet<CommonTreeVo> children = new LinkedHashSet<CommonTreeVo>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public LinkedHashSet<CommonTreeVo> getChildren() {
        return children;
    }

    public void setChildren(LinkedHashSet<CommonTreeVo> children) {
        this.children = children;
    }

}
