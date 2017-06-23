package cn.fooltech.fool_ops.domain.warehouse.util;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>仓库单据网页服务builder编码工具类</p>
 * @author rqh
 * @version 1.0
 * @date 2015年9月22日
 */
public class WarehouseBuilderCodeHelper {
	
	//基础业务
	public static final int base = 0;
	
	//期初库存
	public static final int qckc = 91;
	
	//期初应付
	public static final int qcyf = 92;
	
	//期初应收
	public static final int qcys = 93;
	
	//采购订单
	public static final int cgdd = 10;
	
	//采购入库
	public static final int cgrk = 11;
	
	//采购退货
	public static final int cgth = 12;
	
	//采购询价单
	public static final int cgxjd = 13;
	
	//采购申请单
	public static final int cgsqd = 14;
	
	//采购发票
	public static final int cgfp = 15;
	
	//盘点单
	public static final int pdd = 20;
	
	//调仓单
	public static final int dcd = 21;
	
	//报损单
	public static final int bsd = 22;
	
	//生产领料
	public static final int scll = 30;
	
	//生产退料
	public static final int sctl = 32;
	
	//成品入库
	public static final int cprk = 31;
	
	//成品退库
	public static final int cptk = 33;
	
	//销售订单
	public static final int xsdd = 40;
	
	//销售出货
	public static final int xsch = 41;
	
	//销售退货
	public static final int xsth = 42;
	
	//销售报价单
	public static final int xsbjd = 43;
	
	//销售发票
	public static final int xsfp = 44;
	
	//费用单
	public static final int fyd = 53;
	
	//收款单
	public static final int skd = 51;
	
	//付款单
	public static final int fkd = 52;
	
    //生产计划单	
	public static final int scjhd = 34;
	
	//费用申请单
	public static final int fysqd = 54;
	
	//采购返利单
	public static final int cgfld = 55;
	
	//销售返利单
	public static final int xsfld = 56;
	
	//工资单
	public static final int gzd = 110;
	
	//固定资产
	public static final int gdzc = 120;
	
	//待摊费用
	public static final int dtfy = 130;

	//发货单
	public static final int fhd = 23;

	//收货单
	public static final int shd = 24;

	//同行报价单
	public static final int thbjd = 61;

	//场地费报价单
	public static final int cdfbjd = 62;

	//运输费报价单
	public static final int ysfbjd = 63;
	
	//资金计划
	public static final int zjjh = 100;

	private static final Map<WarehouseBuilderCode, Integer> map;
	
	static{
		map = new HashMap<WarehouseBuilderCode, Integer>();
		map.put(WarehouseBuilderCode.base, base);//不做保存，只是查询用
		map.put(WarehouseBuilderCode.qckc, qckc);
		map.put(WarehouseBuilderCode.cgdd, cgdd);
		map.put(WarehouseBuilderCode.cgrk, cgrk);
		map.put(WarehouseBuilderCode.cgth, cgth);
		map.put(WarehouseBuilderCode.cgxjd, cgxjd);
		map.put(WarehouseBuilderCode.pdd, pdd);
		map.put(WarehouseBuilderCode.dcd, dcd);
		map.put(WarehouseBuilderCode.bsd, bsd);
		map.put(WarehouseBuilderCode.scll, scll);
		map.put(WarehouseBuilderCode.sctl, sctl);
		map.put(WarehouseBuilderCode.cprk, cprk);
		map.put(WarehouseBuilderCode.cptk, cptk);
		map.put(WarehouseBuilderCode.xsdd, xsdd);
		map.put(WarehouseBuilderCode.xsch, xsch);
		map.put(WarehouseBuilderCode.xsth, xsth);
		map.put(WarehouseBuilderCode.xsbjd, xsbjd);
		map.put(WarehouseBuilderCode.xsfld, xsfld);
		map.put(WarehouseBuilderCode.cgsqd, cgsqd);
		map.put(WarehouseBuilderCode.cgfld, cgfld);
		map.put(WarehouseBuilderCode.scjhd, scjhd);
		map.put(WarehouseBuilderCode.fysqd, fysqd);
		map.put(WarehouseBuilderCode.cgfp, cgfp);
		map.put(WarehouseBuilderCode.xsfp, xsfp);
		map.put(WarehouseBuilderCode.skd, skd);
		map.put(WarehouseBuilderCode.fkd, fkd);

		map.put(WarehouseBuilderCode.qcyf, qcyf);
		map.put(WarehouseBuilderCode.qcys, qcys);

		map.put(WarehouseBuilderCode.fhd, fhd);
		map.put(WarehouseBuilderCode.shd, shd);
		map.put(WarehouseBuilderCode.thbjd, thbjd);
		map.put(WarehouseBuilderCode.cdfbjd, cdfbjd);
		map.put(WarehouseBuilderCode.ysfbjd, ysfbjd);
		
		map.put(WarehouseBuilderCode.zjjh, zjjh);
	}
		
	//期初库存
	public static final String qckc1 = "期初库存";
		
	//采购订单
	public static final String cgdd1 = "采购订单";
		
	//采购入库
	public static final String cgrk1 = "采购入库";
		
	//采购退货
	public static final String cgth1 = "采购退货";
		
	//采购询价单
	public static final String cgxjd1 = "采购询价单";
	
	//采购发票
	public static final String cgfp1 = "采购发票";
		
	//盘点单
	public static final String pdd1 = "盘点单";
		
	//调仓单
	public static final String dcd1 = "调仓单";
		
	//报损单
	public static final String bsd1 = "报损单";
		
	//生产领料
	public static final String scll1 = "生产领料";
		
	//生产退料
	public static final String sctl1 = "生产退料";
		
	//成品入库
	public static final String cprk1 = "成品入库";
		
	//成品退库
	public static final String cptk1 = "成品退库";
		
	//销售订单
	public static final String xsdd1 = "销售订单";
		
	//销售出货
	public static final String xsch1 = "销售出货";
		
	//销售退货
	public static final String xsth1 = "销售退货";
		
	//销售报价单
	public static final String xsbjd1 = "销售报价单";
	
	//销售发票
	public static final String xsfp1 = "销售发票";
		
	//费用单
	public static final String fyd1 = "费用单";
		
	//收款单
	public static final String skd1 = "收款单";
		
	//付款单
	public static final String fkd1 = "付款单";	
	
	//采购申请单
	public static final String cgsqd1 = "采购申请单";
	
	//生产计划单
	public static final String scjhd1 = "生产计划单";
	
	//费用申请单
	public static final String fysqd1="费用申请单";
	
	//采购返利单
	public static final String cgfld1="采购返利单";
	
	//销售返利单
	public static final String xsfld1 = "销售返利单";

	//期初应付
	public static final String qcyf1 = "期初应付";

	//期初应收
	public static final String qcys1 = "期初应收";

	//发货单
	public static final String fhd1 = "发货单";

	//收货单
	public static final String shd1 = "收货单";

	//同行报价单
	public static final String thbjd1 = "同行报价单";

	//场地费报价单
	public static final String cdfbjd1 = "场地费报价单";

	public static final String ysfbjd1 = "运输费报价单";
	
	public static final String zjjh1 = "资金计划";

	private static final Map<WarehouseBuilderCode, String> maps;
	private static final Map<Integer, String> maps2;
	
	static{
		maps = new HashMap<WarehouseBuilderCode, String>();
		maps.put(WarehouseBuilderCode.qckc, qckc1);
		maps.put(WarehouseBuilderCode.cgdd, cgdd1);
		maps.put(WarehouseBuilderCode.cgrk, cgrk1);
		maps.put(WarehouseBuilderCode.cgth, cgth1);
		maps.put(WarehouseBuilderCode.cgxjd, cgxjd1);
		maps.put(WarehouseBuilderCode.pdd, pdd1);
		maps.put(WarehouseBuilderCode.dcd, dcd1);
		maps.put(WarehouseBuilderCode.bsd, bsd1);
		maps.put(WarehouseBuilderCode.scll, scll1);
		maps.put(WarehouseBuilderCode.sctl, sctl1);
		maps.put(WarehouseBuilderCode.cprk, cprk1);
		maps.put(WarehouseBuilderCode.cptk, cptk1);
		maps.put(WarehouseBuilderCode.xsdd, xsdd1);
		maps.put(WarehouseBuilderCode.xsch, xsch1);
		maps.put(WarehouseBuilderCode.xsth, xsth1);
		maps.put(WarehouseBuilderCode.xsbjd, xsbjd1);
		maps.put(WarehouseBuilderCode.cgsqd, cgsqd1);
		maps.put(WarehouseBuilderCode.scjhd, scjhd1);
		maps.put(WarehouseBuilderCode.fysqd, fysqd1);
		maps.put(WarehouseBuilderCode.cgfld, cgfld1);
		maps.put(WarehouseBuilderCode.xsfld, xsfld1);
		maps.put(WarehouseBuilderCode.cgfp, cgfp1);
		maps.put(WarehouseBuilderCode.xsfp, xsfp1);
		maps.put(WarehouseBuilderCode.skd, skd1);
		maps.put(WarehouseBuilderCode.fkd, fkd1);
		maps.put(WarehouseBuilderCode.fyd, fyd1);

		maps.put(WarehouseBuilderCode.qcyf, qcyf1);
		maps.put(WarehouseBuilderCode.qcys, qcys1);

		maps.put(WarehouseBuilderCode.fhd, fhd1);
		maps.put(WarehouseBuilderCode.shd, shd1);
		maps.put(WarehouseBuilderCode.thbjd, thbjd1);
		maps.put(WarehouseBuilderCode.cdfbjd, cdfbjd1);
		maps.put(WarehouseBuilderCode.ysfbjd, ysfbjd1);
		maps.put(WarehouseBuilderCode.zjjh, zjjh1);

	}
	
	static{
		maps2 = new HashMap<Integer, String>();
		maps2.put(qckc, qckc1);
		maps2.put(cgdd, cgdd1);
		maps2.put(cgrk, cgrk1);
		maps2.put(cgth, cgth1);
		maps2.put(cgxjd, cgxjd1);
		maps2.put(pdd, pdd1);
		maps2.put(dcd, dcd1);
		maps2.put(bsd, bsd1);
		maps2.put(scll, scll1);
		maps2.put(sctl, sctl1);
		maps2.put(cprk, cprk1);
		maps2.put(cptk, cptk1);
		maps2.put(xsdd, xsdd1);
		maps2.put(xsch, xsch1);
		maps2.put(xsth, xsth1);
		maps2.put(xsbjd, xsbjd1);
		maps2.put(cgsqd, cgsqd1);
		maps2.put(scjhd, scjhd1);
		maps2.put(fysqd, fysqd1);
		maps2.put(cgfld, cgfld1);
		maps2.put(xsfld, xsfld1);
		maps2.put(cgfp, cgfp1);
		maps2.put(xsfp, xsfp1);
		maps2.put(skd, skd1);
		maps2.put(fkd, fkd1);
		maps2.put(fyd, fyd1);

		maps2.put(qcyf, qcyf1);
		maps2.put(qcys, qcys1);

		maps2.put(fhd, fhd1);
		maps2.put(shd, shd1);
		maps2.put(thbjd, thbjd1);
		maps2.put(cdfbjd, cdfbjd1);
		maps2.put(ysfbjd, ysfbjd1);
		maps2.put(zjjh, zjjh1);
	}
	
	/**
	 * 获取单据名称
	 * @param buildCode
	 * @return
	 */
	public static String getBillName(WarehouseBuilderCode buildCode){
		return maps.get(buildCode);
	}
	
	/**
	 * 获取单据名称
	 * @param billType
	 * @return
	 */
	public static String getBillName(Integer billType){
		return maps2.get(billType);
	}
	
	/**
	 * 获取仓库单据类型
	 * @param buildCode 编码
	 * @return
	 */
	public static int getBillType(WarehouseBuilderCode buildCode){
		return map.get(buildCode);
	}
	
}
