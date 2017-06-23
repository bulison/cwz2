package cn.fooltech.fool_ops.domain.warehouse.service.bill;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.CrudRepository;

import cn.fooltech.fool_ops.component.core.DtoTransfer;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
import cn.fooltech.fool_ops.domain.warehouse.vo.StockLockingVo;
import cn.fooltech.fool_ops.domain.warehouse.vo.WarehouseBillVo;


/**
 * <p>仓库单据网页服务类接口</p>
 * @author rqh
 * @version 1.0
 * @date 2015年9月17日
 */
public interface IWareHouseWebService extends DtoTransfer<WarehouseBill, WarehouseBillVo>{
	
	public abstract CrudRepository<WarehouseBill, String> getRepository();
	
	/**
	 * 分页查询
	 * @param vo
	 * @param paramater
	 * @return
	 */
	public Page<WarehouseBillVo> query(WarehouseBillVo vo, PageParamater paramater);
	
	/**
	 * 获取记录
	 * @param id 实体ID
	 * @return
	 */
	public WarehouseBillVo getById(String id);
	
	/**
	 * 获取记录
	 * @param id 实体ID
	 * @return
	 */
	public default WarehouseBillVo getById(String id, boolean transfer){
		WarehouseBill bill = getRepository().findOne(id);
		if(bill==null)return null;
		else return getVo(bill, transfer);
	}
	
	/**
	 * 单个实体转vo
	 * @param entity
	 * @return
	 */
	public WarehouseBillVo getVo(WarehouseBill entity, boolean transfer);
		
	/**
	 * 多个实体转vo
	 * @param entities
	 * @return
	 */
	public default List<WarehouseBillVo> getVos(List<WarehouseBill> entities, boolean transfer){
		List<WarehouseBillVo> vos = new ArrayList<WarehouseBillVo>();
		if(CollectionUtils.isNotEmpty(entities)){
			for(WarehouseBill entity : entities){
				vos.add(getVo(entity, transfer));
			}
		}
		return vos;
	}
	
	/**
	 * 新增、编辑
	 * @param vo
	 * @return
	 */
	public RequestResult save(WarehouseBillVo vo);
	
	/**
	 * 删除
	 * @param id 实体ID
	 * @return
	 */
	public RequestResult delete(String id);
	
	/**
	 * 审核仓库单据
	 * @param id 仓库单据ID
	 * @param vo
	 */
	public RequestResult passAudit(String id, StockLockingVo vo);
	
	/**
	 * 作废
	 * @param id 仓库单据ID
	 * @return
	 */
	public RequestResult cancel(String id);
	
}
