package cn.fooltech.fool_ops.domain.warehouse.builder;

import cn.fooltech.fool_ops.domain.warehouse.service.WarehouseBillService;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCode;

/**
 * <p>仓库单据网页服务构造基类</p>
 * @author rqh
 * @version 1.0
 * @date 2015年9月17日
 */
public abstract class BaseWarehouseWebServiceBuilder extends WarehouseBillService implements IWarehouseWebServiceBuilder{
	
	/**
	 * 编码
	 */
	protected WarehouseBuilderCode builderCode;

	/**
	 * 获取编码
	 * @return
	 */
	@Override
	public WarehouseBuilderCode getBuilderCode() {
		return builderCode;
	}
	
	/**
	 * 设置编码
	 * @param builderCode
	 */
	public void setBuilderCode(WarehouseBuilderCode builderCode) {
		this.builderCode = builderCode;
	}

	@Override
	public boolean support(WarehouseBuilderCode code) {
		return code.equals(this.builderCode);
	}

}
