package cn.fooltech.fool_ops.utils.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * 普通树状VO对象，适用于EasyUI
 *
 * @author rqh
 * @version 1.0
 * @date 2015年4月16日
 */
public class TreeVo {

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
    private Object attributes;

    /**
     * 子节点集合
     */
    private List<TreeVo> children = new ArrayList<TreeVo>();

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

    public Object getAttributes() {
        return attributes;
    }

    public void setAttributes(Object attributes) {
        this.attributes = attributes;
    }

    public List<TreeVo> getChildren() {
        return children;
    }

    public void setChildren(List<TreeVo> children) {
        this.children = children;
    }

}
