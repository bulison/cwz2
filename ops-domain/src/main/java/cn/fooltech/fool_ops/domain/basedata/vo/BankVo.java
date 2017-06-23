package cn.fooltech.fool_ops.domain.basedata.vo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


/**
 * 银行vo类
 *
 * @author lgk
 * @version V1.0
 * @date 2015年9月10日下午2:39:27
 */
public class BankVo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -6154992615909112147L;
    /**
     * id
     */
    private String fid;
    /**
     * 编号
     */
    @NotBlank(message = "编号必填")
    @Length(max = 50, message = "编号长度不能超过50个字符")
    private String code;
    /**
     * 名称
     */
    @NotBlank(message = "名称必填")
    @Length(max = 100, message = "名称长度不能超过100个字符")
    private String name;
    /**
     * 开户行
     */
    @Length(max = 100, message = "开户行长度不能超过100个字符")
    private String bank;

    /**
     * 账号
     */
    @Length(max = 50, message = "账号长度不能超过50个字符")
    private String account;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建人
     */
    private String creator;
    /**
     * 机构id
     */
    private String orgId;
    /**
     * 更新时间
     */
    private String updateTime;
    /**
     * 关键字查找
     */
    private String keyWord;
    /**
     * 创建人名
     */
    private String creatorName;

    /**
     * 类型 1：现金 2：银行
     */
    @NotNull(message = "类型必填")
    private Integer type;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }


}
