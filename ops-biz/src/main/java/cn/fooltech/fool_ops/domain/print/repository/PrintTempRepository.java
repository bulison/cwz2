package cn.fooltech.fool_ops.domain.print.repository;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.print.entity.PrintTemp;

public interface PrintTempRepository extends JpaRepository<PrintTemp, String> {

	/**
	 * 根据机构ID和属性ID查询
	 * @param orgId
	 * @param attrId
	 * @return
	 */
	@Query("select p from PrintTemp p where p.auxiliaryAttr.fid=?1 and p.org.fid=?2")
	public List<PrintTemp> findByOrgIdAndAttrId(String orgId, String attrId);
	
	/**
	 * 根据机构ID和属性ID查询（限制1个结果集）
	 * @param orgId
	 * @param attrId
	 * @return
	 */
	@Query("select p from PrintTemp p where p.auxiliaryAttr.fid=?1 and p.org.fid=?2")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
	public PrintTemp findTopByOrgIdAndAttrId(String orgId, String attrId);
	
	/**
	 * 根据机构ID和属性ID查询数目
	 * @param orgId
	 * @param attrId
	 * @return
	 */
	@Query("select count(*) from PrintTemp p where p.auxiliaryAttr.fid=?1 and p.org.fid=?2")
	public Long countByOrgIdAndAttrId(String orgId, String attrId);
	
    /**
     * 获取打印模板
     * @param orgId
     * @param code
     * @return
     */
	@Query("select p from PrintTemp p where p.auxiliaryAttr.code=?2 and p.org.fid=?1")
	@QueryHints({@QueryHint(name=Constants.LIMIT,value="1")})
    public PrintTemp getByOrgCode(String orgId,String code);
}
