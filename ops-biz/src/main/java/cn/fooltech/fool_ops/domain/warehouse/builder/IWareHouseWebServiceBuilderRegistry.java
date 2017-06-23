package cn.fooltech.fool_ops.domain.warehouse.builder;

import java.util.List;

import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCode;


/**
 * <p>仓库单据网页服务builder的统一注册接口</p>
 * @author rqh
 * @version 1.0
 * @date 2015年9月17日
 */
public interface IWareHouseWebServiceBuilderRegistry {
	
	/**
	 * 获取builder集合
	 * @return
	 */
	public List<IWarehouseWebServiceBuilder> getWebServiceBuilders();

	/**
	 * 根据builder编码获取可执行的builder
	 * @param code
	 * @return
	 */
	public IWarehouseWebServiceBuilder getHandleBuilder(WarehouseBuilderCode code);
	
}
