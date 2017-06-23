package cn.fooltech.fool_ops.domain.basedata.entity;

import cn.fooltech.fool_ops.domain.base.entity.OpsOrgEntity;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;

import javax.persistence.*;
import java.util.Date;


/**
 * 银行实体类
 *
 * @author lgk
 * @version V1.0
 * @date 2015年9月8日上午9:34:14
 */
@Entity
@Table(name = "tbd_bank")
public class Bank extends OpsOrgEntity {

    public static final Integer TYPE_CASH = 1;//现金
    public static final Integer TYPE_BANK = 2;//银行

    private static final long serialVersionUID = 1L;
    /**
     * 名称
     */
    private String name;
    /**
     * 编号
     */
    private String code;
    /**
     * 开户行
     */
    private String bank;
    /**
     * 账号
     */
    private String account;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建人
     */
    private User createor;
    /**
     * 修改时间戳
     */
    private Date updateTime;
    /**
     * 部门
     */
    private Organization dept;

    //private List<InitialBank> initialBanks;

    /**
     * 1：现金 2：银行
     */
    private Integer type = TYPE_BANK;

    @Column(name = "FNAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "FBANK")
    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    @Column(name = "FACCOUNT")
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FCREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FCREATOR", updatable = false)
    public User getCreateor() {
        return createor;
    }

    public void setCreateor(User createor) {
        this.createor = createor;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FUPDATE_TIME")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name = "FCODE")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取所属部门
     *
     * @return
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "FDEPT_ID")
    public Organization getDept() {
        return dept;
    }

    /**
     * 设置部门
     *
     * @param dept
     */
    public void setDept(Organization dept) {
        this.dept = dept;
    }

    /**
     * 多个银行
     *
     * @return
     */
    /*@OneToMany(fetch = FetchType.LAZY, mappedBy = "bank", cascade = CascadeType.ALL)
    public List<InitialBank> getInitialBanks() {
		return initialBanks;
	}

	public void setInitialBanks(List<InitialBank> initialBanks) {
		this.initialBanks = initialBanks;
	}*/
    @Column(name = "FTYPE")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

}
