package cn.fooltech.fool_ops.domain.warehouse.builder;

import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCode;

/**
 * <p>仓库单据网页服务构造接口</p>
 * @author rqh
 * @version 1.0
 * @date 2015年9月17日
 */
public interface IWarehouseWebServiceBuilder {
	
	/**
	 * 获取builder编号
	 * @return
	 */
	public abstract WarehouseBuilderCode getBuilderCode();
	
	/**
	 * 用builder编号对照，返回该builder是否支持构建
	 * @param code
	 * @return
	 */
	public abstract boolean support(WarehouseBuilderCode builderCode);
	
}
