package cn.fooltech.fool_ops.domain.fiscal.repository;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;

public interface FiscalAccountRepository extends JpaRepository<FiscalAccount, String>, JpaSpecificationExecutor<FiscalAccount> {

	/**
	 * 查询默认登录账套
	 */
	@Query("select f from FiscalAccount f where f.org.fid=:orgId and f.defaultFlag=:defaultFlag")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public FiscalAccount findTopByDefaultFlag(@Param("orgId") String orgId, @Param("defaultFlag") Short defaultFlag);

	/**
	 * 根据机构ID查询账套分页
	 * @param orgId
	 * @param pageRequest
	 * @return
	 */
	@Query("select f from FiscalAccount f where f.org.fid=?1")
	public Page<FiscalAccount> findPageByOrgId(String orgId, Pageable page);
	
	/**
	 * 根据机构ID查询账套
	 * @param orgId
	 * @return
	 */
	@Query("select f from FiscalAccount f where f.org.fid=?1")
	public List<FiscalAccount> findByOrgId(String orgId, Sort sort);
	
	/**
	 * 根据名称计算个数
	 * @param orgId
	 * @param name
	 * @param fid
	 * @return
	 */
	@Query("select count(*) from FiscalAccount b where b.org.fid=?1 and b.name=?2 and fid!=?3")
	public Long countByName(String orgId, String name, String exculdeId);
	
	/**
	 * 根据名称计算个数
	 * @param orgId
	 * @param name
	 * @param fid
	 * @return
	 */
	@Query("select count(*) from FiscalAccount b where b.org.fid=?1 and b.name=?2")
	public Long countByName(String orgId, String name);
	/**
	 * 查询所有账套(初始化数据用,用完后注释)
	 */
	@Query("select f from FiscalAccount f")
	public List<FiscalAccount> findAll();
}
