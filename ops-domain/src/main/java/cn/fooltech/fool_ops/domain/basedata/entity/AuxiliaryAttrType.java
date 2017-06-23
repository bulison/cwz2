package cn.fooltech.fool_ops.domain.basedata.entity;

import cn.fooltech.fool_ops.domain.base.entity.StorageBaseEntity;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;

import javax.persistence.*;


/**
 * <p>基础属性类别：地区，客户类别，征信级别，货品类别，在职状况，学历，仓库</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015年9月6日
 */
@Entity
@Table(name = "tbd_auxiliary_attr_type")
public class AuxiliaryAttrType extends StorageBaseEntity {

    public static final String CODE_AREA = "001";//地区
    public static final String CODE_CUSTOMER_TYPE = "002";//客户类别
    public static final String CODE_WAREHOUSE = "007";//仓库
    public static final String CODE_EDUCATION = "006";//学历
    public static final String CODE_CREDIT = "003";//征信级别
    public static final String CODE_GOODS_TYPE = "004";//货品类别
    public static final String CODE_JOB_STATUS = "005";//在职状况
    public static final String CODE_COST_TYPE = "008";//费用项目
    public static final String CODE_SUBJECT_TYPE = "009";//科目类别
    public static final String CODE_VOUCHER_WORD = "010";//凭证字
    public static final String CODE_CURRENCY = "011";//币别
    public static final String CODE_PROJECT = "012";//财务项目
    public static final String CODE_ABSTRACT = "014";//摘要
    public static final String CODE_SETTLEMENT_TYPE = "015";//结算方式
    public static final String CODE_ASSET = "016";//固定资产类型
    public static final String CODE_PRINT = "017";//打印类型
    public static final String CODE_TRANSIT_FEE = "018";//运输费用
    public static final String CODE_TRANSIT_TYPE = "019";//运输方式
    public static final String CODE_SHIPMENT_TYPE = "020";//装运方式
    public static final String CODE_TRANSIT_FEE_UNIT = "021";//运输费计价单位
    public static final String CODE_SPACE_TYPE = "022";//场地类型
    public static final String CODE_TRANSPORT_LOSS = "023";//运输损耗
    /**
     * 不是树状结构
     */
    public static final short TREE_FLAG_NO = 0;
    /**
     * 是树状结构
     */
    public static final short TREE_FLAG_YES = 1;
    private static final long serialVersionUID = -2515260897713608621L;
    /**
     * 树状结构标识
     *
     * @add by rqh
     */
    private Short treeFlag = TREE_FLAG_NO;

    /**
     * 财务账套
     *
     * @add by rqh
     */
    private FiscalAccount fiscalAccount;

    public AuxiliaryAttrType() {
        super();
    }

    public AuxiliaryAttrType(String fid) {
        this.fid = fid;
    }

    /**
     * 获取树状结构标识
     *
     * @return
     */
    @Column(name = "FISTREE")
    public Short getTreeFlag() {
        return treeFlag;
    }

    /**
     * 设置树状结构标识
     *
     * @param treeFlag
     */
    public void setTreeFlag(Short treeFlag) {
        this.treeFlag = treeFlag;
    }

    /**
     * 获取财务账套
     *
     * @return
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FACC_ID")
    public FiscalAccount getFiscalAccount() {
        return fiscalAccount;
    }

    /**
     * 设置财务账套
     *
     * @param fiscalAccount
     */
    public void setFiscalAccount(FiscalAccount fiscalAccount) {
        this.fiscalAccount = fiscalAccount;
    }

}
