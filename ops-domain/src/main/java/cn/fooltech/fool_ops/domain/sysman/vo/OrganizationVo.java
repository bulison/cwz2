package cn.fooltech.fool_ops.domain.sysman.vo;

import cn.fooltech.fool_ops.utils.tree.FastTreeVo;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

/**
 * <p>表单传输对象 - 组织 </p>
 */
public class OrganizationVo extends FastTreeVo<OrganizationVo> implements Serializable, Comparator<OrganizationVo> {

    private static final long serialVersionUID = -1953071278262171002L;
    private String fid;
    private String orgName;//机构名称
    private String postCode;//邮编
    private String faddress;//地址
    private String fax;//传真
    private String email;//电子邮箱
    private String phoneOne;//联系电话
    private String orgDesc;//组织描述
    private String homePage;//主页
    private String parent;//父机构
    private String principal;//负责人
    private String orgId;    //单位ID
    private String orgCode;//编码

    /**
     * 子
     */
    private List<OrganizationVo> children;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getFid() {
        return this.fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    /**
     * 组织名称
     *
     * @return
     */
    public String getOrgName() {
        return this.orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    /**
     * 邮编
     *
     * @return
     */
    public String getPostCode() {
        return this.postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    /**
     * 地址
     *
     * @return
     */
    public String getFaddress() {
        return this.faddress;
    }

    public void setFaddress(String faddress) {
        this.faddress = faddress;
    }

    /**
     * 传真
     *
     * @return
     */
    public String getFax() {
        return this.fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    /**
     * 电子邮箱
     *
     * @return
     */
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 电话1
     *
     * @return
     */
    public String getPhoneOne() {
        return this.phoneOne;
    }

    public void setPhoneOne(String phoneOne) {
        this.phoneOne = phoneOne;
    }

    /**
     * 组织描述
     *
     * @return
     */
    public String getOrgDesc() {
        return this.orgDesc;
    }

    public void setOrgDesc(String orgDesc) {
        this.orgDesc = orgDesc;
    }

    /**
     * 主页
     *
     * @return
     */
    public String getHomePage() {
        return this.homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    /**
     * 父组织
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
     * 负责人
     *
     * @return
     */
    public String getPrincipal() {
        return this.principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    @Override
    public int compare(OrganizationVo o1, OrganizationVo o2) {
        if(o1.getOrgCode()==null && o2.getOrgCode()==null){
            return 0;
        }
        if(o1.getOrgCode()==null){
            return -1;
        }
        if(o2.getOrgCode()==null){
            return 1;
        }
        return o1.getOrgCode().compareTo(o2.getOrgCode());
    }

    @Override
    public String getId() {
        return this.fid;
    }

    @Override
    public String getParentId() {
        return this.parent;
    }

    @Override
    public List<OrganizationVo> getChildren() {
        if (children == null) children = Lists.newArrayList();
        return children;
    }

    @Override
    public String getText() {
        return this.orgName;
    }

}
