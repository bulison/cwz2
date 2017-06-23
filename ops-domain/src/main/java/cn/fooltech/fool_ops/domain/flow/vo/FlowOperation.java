package cn.fooltech.fool_ops.domain.flow.vo;

/**
 * 流程操作类
 *
 * @author xjh
 */
public class FlowOperation {

    /**
     * 按钮ID
     */
    private String id;

    /**
     * 按钮中文名称
     */
    private String name;

    /**
     * 按钮样式
     */
    private String clazz;

    /**
     * 按钮类型
     */
    private String type;

    /**
     * 按钮需要的权限
     */
    private String auth;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

}
