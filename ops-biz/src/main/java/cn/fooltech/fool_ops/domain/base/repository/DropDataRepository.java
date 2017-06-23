package cn.fooltech.fool_ops.domain.base.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 根据账套删除
 * @author xjh
 *
 */
@Repository
public class DropDataRepository {

	@PersistenceContext
	private EntityManager entityManager;
	
	/**
	 * 根据账套删除
	 * @param clazzName
	 * @param accId
	 */
	@Transactional
	public void deleteDataByAccId(String clazzName, String accId){
		String hql = "delete from "+clazzName+" where fiscalAccount.fid=:accId";
		Query query = entityManager.createQuery(hql);
		query.setParameter("accId", accId);
		query.executeUpdate();
	}
	/**
	 * 对多栏明细账明细（tbd_fiscal_multi_column_setting_detail）的特殊处理
	 */
	@Transactional
	public void deleteFiscalMultiColumnDetailByMasterAccId(String clazzName, String accId){
		String hql = "delete a from TBD_FISCAL_MULTI_COLUMN_SETTING_DETAIL as a inner join TBD_FISCAL_MULTI_COLUMN_SETTING as b on a.FMULTI_ID=b.fid  where b.FACC_ID=:accId";
	//	Query query = entityManager.createQuery(hql);
		Query query=entityManager.createNativeQuery(hql);
		query.setParameter("accId", accId);
		query.executeUpdate();
	}
	/**
	 * 根据机构ID删除
	 * @param clazzName
	 * @param orgId
	 */
	@Transactional
	public void deleteDataByOrgId(String clazzName, String orgId){
		String hql = "delete from "+clazzName+" where org.fid=:orgId";
		Query query = entityManager.createQuery(hql);
		query.setParameter("orgId", orgId);
		query.executeUpdate();
	}
}
