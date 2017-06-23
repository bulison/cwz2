package cn.fooltech.fool_ops.domain.bom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import cn.fooltech.fool_ops.domain.bom.entity.BomDetail;

public interface BomDetailRepository extends JpaRepository<BomDetail, String>, JpaSpecificationExecutor<BomDetail>{

	/**
	 * 根据bomId查询明细
	 * @param bomId
	 * @return
	 */
	@Query("select b from BomDetail b where b.bom.fid=?1")
	public List<BomDetail> findByBomId(String bomId);


}
