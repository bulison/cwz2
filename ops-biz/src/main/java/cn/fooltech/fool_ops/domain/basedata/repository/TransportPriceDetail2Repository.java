package cn.fooltech.fool_ops.domain.basedata.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.domain.basedata.entity.TransportPriceDetail1;
import cn.fooltech.fool_ops.domain.basedata.entity.TransportPriceDetail2;

public interface TransportPriceDetail2Repository extends FoolJpaRepository<TransportPriceDetail2, String> {


	/**
	 * 根据运输报价id删除
	 * @param tempId
	 */
	@Modifying
	@Query("delete from TransportPriceDetail2 a where bill.id=?1")
	public void delByTempId(String tempId);
	
	/**
	 * 根据id，账套，关联id查询从1记录
	 * @param accId
	 * @param templateId
	 * @param id
	 * @return
	 */
    @Query("select a from TransportPriceDetail2 a where fiscalAccount.fid=?1 and bill.id=?2 and detail1.id=?3")
	public List<TransportPriceDetail2> queryDetail(String accId,String billId,String detail1Id);

	/**
	 * 根据bill.id获取从2记录
	 * @param billId
	 * @return
	 */
	@Query("select a from TransportPriceDetail2 a where bill.id=?1")
	public List<TransportPriceDetail2> queryDetail(String billId);
    
}
