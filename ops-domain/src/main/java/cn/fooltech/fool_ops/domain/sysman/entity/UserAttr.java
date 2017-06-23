package cn.fooltech.fool_ops.domain.sysman.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 用户属性
 *
 * @author lzf
 * @version 1.0
 * @date 2015年3月1日
 */
@Entity
@Table(name = "SMG_TUSER_ATTR")
public class UserAttr extends OpsEntity {
    public static final String INPUT_TYPE_PINYIN = "PINYIN";//拼音
    public static final String INPUT_TYPE_FIVEPEN = "FIVEPEN";//五笔

    public static final String INPUT_TYPE = "INPUT_TYPE";//输入法

    public static final String LOCAL_CACHE = "LOCAL_CACHE";//本地缓存
    public static final String LOCAL_CACHE_ENABLE = "1";//本地缓存开启
    public static final String LOCAL_CACHE_DISABLE = "0";//本地缓存关闭


    private static final long serialVersionUID = 7463784005192768061L;

    /**
     * 用户ID
     */
    private String userID;

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

    /**
     * 获取用户ID
     *
     * @return
     */
    @Column(name = "FUSER_ID", length = 32)
    public String getUserID() {
        return userID;
    }

    /**
     * 设置用户ID
     *
     * @param category
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }


    /**
     * 获取KEY
     *
     * @return
     */
    @Column(name = "FKEY", length = 50)
    public String getKey() {
        return key;
    }

    /**
     * 设置KEY
     *
     * @param category
     */
    public void setKey(String key) {
        this.key = key;
    }


    /**
     * 获取VALUE
     *
     * @return
     */
    @Column(name = "FVALUE", length = 50)
    public String getValue() {
        return value;
    }

    /**
     * 设置VALUE
     *
     * @param category
     */
    public void setValue(String value) {
        this.value = value;
    }


    /**
     * 获取描述
     *
     * @return
     */
    @Column(name = "FDESCRIBE", length = 200)
    public String getDescribe() {
        return describe;
    }

    /**
     * 设置描述
     *
     * @param category
     */
    public void setDescribe(String describe) {
        this.describe = describe;
    }

}
