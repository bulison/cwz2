package cn.fooltech.fool_ops.validator.bill;

import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCodeHelper;
import com.google.common.collect.Maps;

import java.util.Map;

public class BillClassTransferUtils {

    private final static Map<Integer, Class> clazzs = Maps.newHashMap();

    static {
        clazzs.put(WarehouseBuilderCodeHelper.bsd, Bsd.class);
        clazzs.put(WarehouseBuilderCodeHelper.cgdd, Cgdd.class);
        clazzs.put(WarehouseBuilderCodeHelper.cgfp, Cgfp.class);
        clazzs.put(WarehouseBuilderCodeHelper.cgrk, Cgrk.class);
        clazzs.put(WarehouseBuilderCodeHelper.cgsqd, Cgsqd.class);
        clazzs.put(WarehouseBuilderCodeHelper.cgth, Cgth.class);
        clazzs.put(WarehouseBuilderCodeHelper.cgxjd, Cgxjd.class);
        clazzs.put(WarehouseBuilderCodeHelper.cprk, Cprk.class);
        clazzs.put(WarehouseBuilderCodeHelper.cptk, Cptk.class);
        clazzs.put(WarehouseBuilderCodeHelper.dcd, Dcd.class);
        clazzs.put(WarehouseBuilderCodeHelper.pdd, Pdd.class);
        clazzs.put(WarehouseBuilderCodeHelper.qckc, Qckc.class);
        clazzs.put(WarehouseBuilderCodeHelper.scjhd, Scjhd.class);
        clazzs.put(WarehouseBuilderCodeHelper.scll, Scll.class);
        clazzs.put(WarehouseBuilderCodeHelper.sctl, Sctl.class);
        clazzs.put(WarehouseBuilderCodeHelper.xsch, Xsch.class);
        clazzs.put(WarehouseBuilderCodeHelper.xsdd, Xsdd.class);
        clazzs.put(WarehouseBuilderCodeHelper.xsfp, Xsfp.class);
        clazzs.put(WarehouseBuilderCodeHelper.xsth, Xsth.class);
    }

    public static Class getSupportClass(int billType) {
        return clazzs.get(billType);
    }
}
