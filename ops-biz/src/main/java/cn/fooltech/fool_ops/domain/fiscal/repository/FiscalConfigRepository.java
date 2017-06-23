package cn.fooltech.fool_ops.domain.fiscal.repository;

import javax.persistence.QueryHint;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalConfig;

public interface FiscalConfigRepository extends JpaRepository<FiscalConfig, String> {

	/**
	 * 查询财务参数设置列表信息
	 * @param accId
	 * @param pageRequest
	 * @return
	 */
	@Query("select f from FiscalConfig f where f.fiscalAccount.fid=?1")
	public Page<FiscalConfig> findPageByAccId(String accId, Pageable page);
	
	/**
	 * 查询财务参数设置列表信息
	 * @param accId
	 * @param pageRequest
	 * @return
	 */
	@Query("select f from FiscalConfig f where f.fiscalAccount.fid=?1 and f.code=?2")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public FiscalConfig findTopByAccIdAndCode(String accId, String code);

}
