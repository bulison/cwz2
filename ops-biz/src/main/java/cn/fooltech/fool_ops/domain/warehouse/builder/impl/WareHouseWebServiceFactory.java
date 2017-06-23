package cn.fooltech.fool_ops.domain.warehouse.builder.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.fooltech.fool_ops.domain.warehouse.builder.IWareHouseWebServiceBuilderRegistry;
import cn.fooltech.fool_ops.domain.warehouse.builder.IWarehouseWebServiceBuilder;
import cn.fooltech.fool_ops.domain.warehouse.util.WarehouseBuilderCode;


/**
 * <p>仓库单据网页服务工厂类</p>
 * @author rqh
 * @version 1.0
 * @date 2015年9月17日
 */
@Component
public class WareHouseWebServiceFactory implements IWareHouseWebServiceBuilderRegistry{

	/**
	 * 所有实现了IWarehouseWebServiceBuilder的对象，自动注入
	 */
	@Autowired
	private List<IWarehouseWebServiceBuilder> builders;
	
	/**
	 * 构造器池
	 */
	private Map<WarehouseBuilderCode, IWarehouseWebServiceBuilder> builderPool;
	
	/**
	 * 初始化
	 */
	@PostConstruct
	public void initPoll(){
		builderPool = new HashMap<WarehouseBuilderCode, IWarehouseWebServiceBuilder>(0);
		if(CollectionUtils.isNotEmpty(builders)){
			for(IWarehouseWebServiceBuilder builder : builders){
				if(builder.getBuilderCode() != null){
					builderPool.put(builder.getBuilderCode(), builder);	
				}
			}
		}
	}
	
	@Override
	public List<IWarehouseWebServiceBuilder> getWebServiceBuilders() {
		return this.builders;
	}

	@Override
	public IWarehouseWebServiceBuilder getHandleBuilder(WarehouseBuilderCode buildCode) {
		return builderPool.get(buildCode);
	}

}
