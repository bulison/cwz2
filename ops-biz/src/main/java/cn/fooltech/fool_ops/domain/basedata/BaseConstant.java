package cn.fooltech.fool_ops.domain.basedata;

import cn.fooltech.fool_ops.domain.basedata.entity.*;
import cn.fooltech.fool_ops.domain.freight.entity.FreightAddress;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import com.google.common.collect.Maps;

import java.util.Map;

public class BaseConstant {

    public static final int timeout =1*60*60*24*31*12;

    public static final String CUSTOMER = Customer.class.getSimpleName();
    public static final String SUPPLIER = Supplier.class.getSimpleName();
    public static final String GOODS = Goods.class.getSimpleName();
    public static final String DEMARTMENT = Organization.class.getSimpleName();
    public static final String MEMBER = Member.class.getSimpleName();
    public static final String USER = User.class.getSimpleName();
    public static final String CSV = "CSV";
    public static final String ADDRESS = FreightAddress.class.getSimpleName();

    public static final String AUXILIARY_ATTR = AuxiliaryAttr.class.getSimpleName();
    public static final String AUXILIARY_ATTR_AREA = AuxiliaryAttr.class
            .getSimpleName() + "_Area";
    public static final String AUXILIARY_ATTR_CUSTOMER_TYPE = AuxiliaryAttr.class
            .getSimpleName() + "_CustomerType";
    public static final String AUXILIARY_ATTR_WAREHOUSE = AuxiliaryAttr.class
            .getSimpleName() + "_Warehouse";
    public static final String AUXILIARY_ATTR_EDUCATION = AuxiliaryAttr.class
            .getSimpleName() + "_Education";
    public static final String AUXILIARY_ATTR_CREDIT = AuxiliaryAttr.class
            .getSimpleName() + "_Credit";
    public static final String AUXILIARY_ATTR_GOODS_TYPE = AuxiliaryAttr.class
            .getSimpleName() + "_GoodsType";
    public static final String AUXILIARY_ATTR_JOB_STATUS = AuxiliaryAttr.class
            .getSimpleName() + "_JobStatus";
    public static final String AUXILIARY_ATTR_COST_TYPE = AuxiliaryAttr.class
            .getSimpleName() + "_CostType";
    public static final String AUXILIARY_ATTR_SUBJECT_TYPE = AuxiliaryAttr.class
            .getSimpleName() + "_SubjectType";
    public static final String AUXILIARY_ATTR_VOUCHER_WORD = AuxiliaryAttr.class
            .getSimpleName() + "_VoucherWord";
    public static final String AUXILIARY_ATTR_CURRENCY = AuxiliaryAttr.class
            .getSimpleName() + "_Currency";
    public static final String AUXILIARY_ATTR_PROJECT = AuxiliaryAttr.class
            .getSimpleName() + "_Project";
    public static final String AUXILIARY_ATTR_ABSTRACT = AuxiliaryAttr.class
            .getSimpleName() + "_Abstract";
    public static final String AUXILIARY_ATTR_SETTLEMENT_TYPE = AuxiliaryAttr.class
            .getSimpleName() + "_SettlementType";
    public static final String AUXILIARY_ATTR_ASSET = AuxiliaryAttr.class
            .getSimpleName() + "_Asset";
    public static final String AUXILIARY_ATTR_PRINT = AuxiliaryAttr.class
            .getSimpleName() + "_Print";

    private static Map<String, String> map = Maps.newHashMap();
    private static Map<String, String> map2 = Maps.newHashMap();

    static {
        map.put(AUXILIARY_ATTR_AREA, AuxiliaryAttrType.CODE_AREA);
        map.put(AUXILIARY_ATTR_CUSTOMER_TYPE, AuxiliaryAttrType.CODE_CUSTOMER_TYPE);
        map.put(AUXILIARY_ATTR_WAREHOUSE, AuxiliaryAttrType.CODE_WAREHOUSE);
        map.put(AUXILIARY_ATTR_EDUCATION, AuxiliaryAttrType.CODE_EDUCATION);
        map.put(AUXILIARY_ATTR_CREDIT, AuxiliaryAttrType.CODE_CREDIT);
        map.put(AUXILIARY_ATTR_GOODS_TYPE, AuxiliaryAttrType.CODE_GOODS_TYPE);
        map.put(AUXILIARY_ATTR_JOB_STATUS, AuxiliaryAttrType.CODE_JOB_STATUS);
        map.put(AUXILIARY_ATTR_COST_TYPE, AuxiliaryAttrType.CODE_COST_TYPE);
        map.put(AUXILIARY_ATTR_SUBJECT_TYPE, AuxiliaryAttrType.CODE_SUBJECT_TYPE);
        map.put(AUXILIARY_ATTR_VOUCHER_WORD, AuxiliaryAttrType.CODE_VOUCHER_WORD);
        map.put(AUXILIARY_ATTR_CURRENCY, AuxiliaryAttrType.CODE_CURRENCY);
        map.put(AUXILIARY_ATTR_PROJECT, AuxiliaryAttrType.CODE_PROJECT);
        map.put(AUXILIARY_ATTR_ABSTRACT, AuxiliaryAttrType.CODE_ABSTRACT);
        map.put(AUXILIARY_ATTR_SETTLEMENT_TYPE, AuxiliaryAttrType.CODE_SETTLEMENT_TYPE);
        map.put(AUXILIARY_ATTR_ASSET, AuxiliaryAttrType.CODE_ASSET);
        map.put(AUXILIARY_ATTR_PRINT, AuxiliaryAttrType.CODE_PRINT);

        map2.put(AuxiliaryAttrType.CODE_AREA, AUXILIARY_ATTR_AREA);
        map2.put(AuxiliaryAttrType.CODE_CUSTOMER_TYPE, AUXILIARY_ATTR_CUSTOMER_TYPE);
        map2.put(AuxiliaryAttrType.CODE_WAREHOUSE, AUXILIARY_ATTR_WAREHOUSE);
        map2.put(AuxiliaryAttrType.CODE_EDUCATION, AUXILIARY_ATTR_EDUCATION);
        map2.put(AuxiliaryAttrType.CODE_CREDIT, AUXILIARY_ATTR_CREDIT);
        map2.put(AuxiliaryAttrType.CODE_GOODS_TYPE, AUXILIARY_ATTR_GOODS_TYPE);
        map2.put(AuxiliaryAttrType.CODE_JOB_STATUS, AUXILIARY_ATTR_JOB_STATUS);
        map2.put(AuxiliaryAttrType.CODE_COST_TYPE, AUXILIARY_ATTR_COST_TYPE);
        map2.put(AuxiliaryAttrType.CODE_SUBJECT_TYPE, AUXILIARY_ATTR_SUBJECT_TYPE);
        map2.put(AuxiliaryAttrType.CODE_VOUCHER_WORD, AUXILIARY_ATTR_VOUCHER_WORD);
        map2.put(AuxiliaryAttrType.CODE_CURRENCY, AUXILIARY_ATTR_CURRENCY);
        map2.put(AuxiliaryAttrType.CODE_PROJECT, AUXILIARY_ATTR_PROJECT);
        map2.put(AuxiliaryAttrType.CODE_ABSTRACT, AUXILIARY_ATTR_ABSTRACT);
        map2.put(AuxiliaryAttrType.CODE_SETTLEMENT_TYPE, AUXILIARY_ATTR_SETTLEMENT_TYPE);
        map2.put(AuxiliaryAttrType.CODE_ASSET, AUXILIARY_ATTR_ASSET);
        map2.put(AuxiliaryAttrType.CODE_PRINT, AUXILIARY_ATTR_PRINT);
    }


    /**
     * 获得目录编号
     *
     * @return
     */
    public static String getCategoryCode(String constantType) {
        return map.get(constantType);
    }

    /**
     * 获得目录编号
     *
     * @return
     */
    public static String getCacheKey(String categoryCode) {
        return map2.get(categoryCode);
    }
}
