package cn.fooltech.fool_ops.domain.sysman.vo;

import cn.fooltech.fool_ops.utils.tree.FastTreeVo;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

/**
 * 菜单项值对象
 *
 * @author ljb
 */
public class MenuVo extends FastTreeVo<MenuVo> implements Serializable, Comparator<MenuVo> {


    private static final long serialVersionUID = 1944804716783222823L;

    /**
     * 菜单项唯一id
     */
    private String id;
    /**
     * 菜单项标题，一般用于显示
     */
    private String label;
    /**
     * 索引
     */
    private int index = 0;
    /**
     * 菜单项图标，相对路径
     */
    private String icon;
    /**
     * 菜单项地址
     */
    private String url;

    /**
     * 是否延迟加载，用于外部应用
     */
    private boolean lazy;

    /**
     * 子菜单项
     */
    private List<MenuVo> children;

    private String smallIcoPath;

    private String code;

    private String parentId;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isLazy() {
        return lazy;
    }

    public void setLazy(boolean lazy) {
        this.lazy = lazy;
    }

    @Override
    public List<MenuVo> getChildren() {
        if (children == null) children = Lists.newArrayList();
        return children;
    }

    public void setChildren(List<MenuVo> children) {
        this.children = children;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSmallIcoPath() {
        return smallIcoPath;
    }

    public void setSmallIcoPath(String smallIcoPath) {
        this.smallIcoPath = smallIcoPath;
    }

    @Override
    public int compare(MenuVo o1, MenuVo o2) {
        if (o1.getIndex() == o2.getIndex()) {
            return 0;
        } else if (o1.getIndex() > o2.getIndex()) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public String getText() {
        return this.label;
    }
}
