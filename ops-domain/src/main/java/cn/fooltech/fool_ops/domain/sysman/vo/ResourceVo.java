package cn.fooltech.fool_ops.domain.sysman.vo;

import cn.fooltech.fool_ops.utils.tree.FastTreeVo;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

public class ResourceVo extends FastTreeVo<ResourceVo> implements Serializable, Comparator<ResourceVo> {

    private static final long serialVersionUID = 4704630804990522372L;
    private String fid;
    private String parent;//父资源
    private String resName;//资源名称
    private short resType;//0:菜单目录;  1：菜单项;  2：URL;  3：操作
    private String resString;//资源URL
    private String code;    //资源编码
    private String resDesc;
    private Integer rankOrder;//排序字段
    private String smallIcoPath;
    private Integer resApp;//资源应用环境 1=web,2=android,3=iphone
    private Integer show;//是否显示：0不显示，1显示

    /**
     * 子菜单项
     */
    private List<ResourceVo> children = Lists.newArrayList();

    /**
     * 数据过滤方式：
     * 1、查看我(创建人)自已的；
     * 2、查看本部门的（不包含下属部门）；
     * 3、查看本部门的（包含下属部门）；
     * 4、 查看其它部门的(默认本部门及本部门的下属部门的也能查看);
     * 5、查看本单位的；
     */
    private Integer dateFilter;

    /**
     * 资源类型 0：业务资源 1：系统资源
     */
    private Short permType;

    public Integer getResApp() {
        return resApp;
    }

    public void setResApp(Integer resApp) {
        this.resApp = resApp;
    }

    public String getSmallIcoPath() {
        return smallIcoPath;
    }

    public void setSmallIcoPath(String smallIcoPath) {
        this.smallIcoPath = smallIcoPath;
    }

    /**
     * 父资源
     *
     * @return
     */
    public String getParent() {
        return this.parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    /**
     * 资源名称
     *
     * @return
     */
    public String getResName() {
        return this.resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    /**
     * 资源类型,1.菜单 2.URL 3.操作
     *
     * @return
     */
    public short getResType() {
        return this.resType;
    }

    public void setResType(short resType) {
        this.resType = resType;
    }

    /**
     * 资源链接
     *
     * @return
     */
    public String getResString() {
        return this.resString;
    }

    public void setResString(String resString) {
        this.resString = resString;
    }

    /**
     * 资源描述
     *
     * @return
     */
    public String getResDesc() {
        return this.resDesc;
    }

    public void setResDesc(String resDesc) {
        this.resDesc = resDesc;
    }

    public String getFid() {
        return this.fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    /**
     * 资源编码
     *
     * @return
     */
    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 排序字段
     *
     * @return
     */
    public Integer getRankOrder() {
        return this.rankOrder;
    }

    public void setRankOrder(Integer rankOrder) {
        this.rankOrder = rankOrder;
    }

    public Integer getDateFilter() {
        return dateFilter;
    }

    public void setDateFilter(Integer dateFilter) {
        this.dateFilter = dateFilter;
    }

    public Short getPermType() {
        return permType;
    }

    public void setPermType(Short permType) {
        this.permType = permType;
    }

    public Integer getShow() {return show;}

    public void setShow(Integer show) {this.show = show;}

    @Override
    public int compare(ResourceVo o1, ResourceVo o2) {
        if (o1.getRankOrder() == null && o2.getRankOrder() == null) return 0;
        if (o1.getRankOrder() == null) return -1;
        if (o2.getRankOrder() == null) return 1;
        return o1.getRankOrder().compareTo(o2.getRankOrder());
    }

    @Override
    public String getId() {
        return fid;
    }

    @Override
    public String getParentId() {
        return parent;
    }

    @Override
    public List<ResourceVo> getChildren() {
        return this.children;
    }

    @Override
    public String getText() {
        return this.resName;
    }

}
