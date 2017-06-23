package cn.fooltech.fool_ops.domain.sysman.vo;

import java.io.Serializable;

public class UserAttrVo implements Serializable {

    private static final long serialVersionUID = -6881089433721269600L;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * KEY
     */
    private String key;

    /**
     * VALUE
     */
    private String value;

    /**
     * 描述
     */
    private String describe;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
