package cn.fooltech.fool_ops.utils.tree;

import java.util.List;

/**
 * <p>抽象的树VO</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015年5月28日
 */
public abstract class FastTreeVo<V> {

    public static final String STATE_CLOSED = "closed";
    public static final String STATE_OPEN = "open";
    /**
     * 是否选择，easyui用
     */
    protected boolean checked = false;
    /**
     * 树节点的是否关闭
     */
    protected String state = STATE_OPEN;
    /**
     * 树的层次
     */
    protected Integer level = 0;

    public abstract String getId();

    public abstract String getText();

    public abstract String getParentId();

    public abstract List<V> getChildren();

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
