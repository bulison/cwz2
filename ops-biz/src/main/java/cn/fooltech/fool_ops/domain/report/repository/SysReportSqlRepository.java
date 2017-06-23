package cn.fooltech.fool_ops.domain.report.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.Parameter;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cn.fooltech.fool_ops.domain.report.entity.SysReport;
import cn.fooltech.fool_ops.domain.report.entity.SysReportSql;
import cn.fooltech.fool_ops.domain.report.entity.UserTemplateDetail;

@Repository
public class SysReportSqlRepository{
	
	@PersistenceContext
	private EntityManager em;
	
	public static final String ORG_ID = "ORGID"; //机构ID
	
	public static final String ACCOUNTID = "ACCOUNTID"; //财务账套ID
	
	public static final String PAGE_START = "START"; //起始位置
	
	public static final String PAGE_RESULT = "MAXRESULT"; //页面大小
	 
	public static final String COUNTFLAG = "COUNTFLAG"; //是否统计行数-0 否，1是
	
	public static final int SCALE = 2; //数据精度
	
	public static final int MAX = 65535;
	

	/**
	 * 根据reportId计算报表总数
	 * @param reportId
	 * @return
	 */
	public Long countByReportId(String reportId){
		TypedQuery<Long> query = em.createQuery("select count(*) from SysReportSql a where a.sysReport.fid = :reportId", Long.class);
	    query.setParameter("reportId", reportId);
	    return query.getSingleResult();
	}

	/**
	 * 根据reportId计算报表总数
	 * @param reportId
	 * @param excludeId
	 * @return
	 */
	public Long countByReportId(String reportId, String excludeId){
		TypedQuery<Long> query = em.createQuery("select count(*) from SysReportSql a where a.sysReport.fid = :reportId and a.fid!=:excludeId", Long.class);
	    query.setParameter("reportId", reportId);
	    query.setParameter("excludeId", excludeId);
	    return query.getSingleResult();
	}
	
	/**
	 * 根据系统报表获取记录
	 * @param reportId 系统报表ID
	 * @return
	 */
	public SysReportSql findTopBySysReportId(String reportId){
		TypedQuery<SysReportSql> query = em.createQuery("select a from SysReportSql a where a.sysReport.fid = :reportId", SysReportSql.class);
	    query.setParameter("reportId", reportId);
	    query.setMaxResults(1);
	    List<SysReportSql> list = query.getResultList();
	    if(list.size()>0)return list.get(0);
	    return null;
	}
	
	
	/**
	 * 执行报表查询
	 * @param orgId 机构ID
	 * @param fiscalAccountId 财务账套ID
	 * @param conditions 查询条件
	 * @param sysReportId 系统报表ID
	 * @param start 起始位置
	 * @param maxResult 页面大小
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> queryByPage(String orgId, String fiscalAccountId, List<UserTemplateDetail> conditions, 
			String sysReportId, int start, int maxResult){
		SysReportSql entity = findTopBySysReportId(sysReportId);
		Assert.notNull(entity, "报表查询语句不存在!");
		
		Query query = em.createNativeQuery(entity.getSql());
		addQueryParameter(query, orgId, fiscalAccountId, conditions);
		setPage(query, entity.getSql(), start, maxResult);
		List<Object[]> datas = query.getResultList();
		addCountInfo(entity, datas);
		return datas;
	}
	
	/**
	 * 查询报表的所有数据
	 * @param orgId 机构ID
	 * @param fiscalAccountId 财务账套ID
	 * @param conditions 查询条件
	 * @param sysReportId 系统报表ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> queryAll(String orgId, String fiscalAccountId, List<UserTemplateDetail> conditions, 
			String sysReportId){
		SysReportSql entity = findTopBySysReportId(sysReportId);
		Assert.notNull(entity, "报表查询语句不存在!");
		
		Query query = em.createNativeQuery(entity.getSql());
		addQueryParameter(query, orgId, fiscalAccountId, conditions);
		if(procedureExist(entity.getSql())){
			setPageForProcedure(query, 0, MAX);
		}
		
		List<Object[]> datas = query.getResultList();
		addCountInfo(entity, datas);
		return datas;
	}
	
	/**
	 * 统计结果集的行数
	 * @param orgId 机构ID
	 * @param fiscalAccountId 财务账套ID
	 * @param conditions 查询条件
	 * @param sysReportId 系统报表ID
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public int count(String orgId, String fiscalAccountId, List<UserTemplateDetail> conditions, 
			String sysReportId){
		SysReportSql entity = findTopBySysReportId(sysReportId);
		Assert.notNull(entity, "报表查询语句不存在!");
		
		int total = 0;
		String sql = entity.getSql();
		if(procedureExist(sql)){
			Query query = em.createNativeQuery(sql);
			addQueryParameter(query, orgId, fiscalAccountId, conditions);
			query.setParameter(COUNTFLAG, 1);
			query.setParameter(PAGE_START, 0);
			query.setParameter(PAGE_RESULT, 10);
			List list = query.getResultList();
			total = ((BigInteger) list.get(0)).intValue();
		}
		else{
			sql = "select count(*) from (" + sql + ") as temp";
			Query query = em.createNativeQuery(sql);
			addQueryParameter(query, orgId, fiscalAccountId, conditions);
			List list = query.getResultList();
			total = ((BigInteger) list.get(0)).intValue();
		}
		return total;
	}
	
	/**
	 * 设置分页参数
	 * @param query
	 * @param start 起始位置
	 * @param maxResult 分页大小
	 */
	private void setPage(Query query, String sql, int start, int maxResult){
		if(procedureExist(sql)){
			query.setParameter(COUNTFLAG, 0);
			query.setParameter(PAGE_START, start);
			query.setParameter(PAGE_RESULT, maxResult);
		}else{
			query.setFirstResult(start);
			query.setMaxResults(maxResult);
		}
	}
	
	/**
	 * 设置分页参数
	 * @param query
	 * @param start 起始位置
	 * @param maxResult 分页大小
	 */
	private void setPageForProcedure(Query query, int start, int maxResult){
		query.setParameter(COUNTFLAG, 0);
		query.setParameter(PAGE_START, start);
		query.setParameter(PAGE_RESULT, maxResult);
	}
	
	/**
	 * 添加查询参数
	 * @param query
	 * @param orgId
	 * @param fiscalAccountId
	 * @param conditions
	 */
	private void addQueryParameter(Query query, String orgId, String fiscalAccountId, List<UserTemplateDetail> conditions){
		//客户端的查询条件
		Map<String, Object> conditionMap = getConditionMap(conditions);
		
		//SQL参数名称
		//ParameterRegistrationImpl
		Set<Parameter<?>> sqlParameterNames = query.getParameters();
		
		Set<String> parameterNames = Sets.newHashSet();
		for(Parameter paramater : sqlParameterNames){
			parameterNames.add(paramater.getName());
		}
		
		if(parameterNames.isEmpty()){
			return;
		}
		
		if(!conditionMap.isEmpty()){
			Set<String> conditionNames = conditionMap.keySet();
			for(String conditionName : conditionNames){
				if(conditionName==null)continue;
				if(parameterNames.contains(conditionName)){
					query.setParameter(conditionName, conditionMap.get(conditionName));
				}
				else{ 
					System.err.println(conditionName + " 条件不存在于SQL中!");
					//throw new RuntimeException("SQL参数与预先配置的参数不匹配!");
				}
			}	
		}
		//处理sql参数客户端无传值的情况
		for(String parameterName : parameterNames){
			if(conditionMap.get(parameterName) == null){
				query.setParameter(parameterName, "");
			}
		}
		//财务账套
		if(parameterNames.contains(ACCOUNTID)){
			query.setParameter(ACCOUNTID, fiscalAccountId);
		}
		//组织机构
		if(parameterNames.contains(ORG_ID)){
			query.setParameter(ORG_ID, orgId);
		}
	}
	
	/**
	 * 获取客户端的查询条件名称和值
	 * @param conditions
	 * @return
	 */
	private Map<String, Object> getConditionMap(List<UserTemplateDetail> conditions){
		Map<String, Object> map = new HashMap<String, Object>();
		if(CollectionUtils.isNotEmpty(conditions)){
			for(UserTemplateDetail condition : conditions){
				if(condition.getValue()==null){
					map.put(condition.getAliasName(), "".intern());
				}else{
					map.put(condition.getAliasName(), condition.getValue().trim());
				}
			}
		}
		return map;
	}
		
	/**
	 * 判断是否是调用了存储过程
	 * @param sql SQL语句
	 */
	private boolean procedureExist(String sql){
		Pattern pattern = Pattern.compile("^\\s*call\\s+", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(sql);
		return matcher.find();
	}
	
	/**
	 * 添加统计信息
	 */
	private void addCountInfo(SysReportSql reportSql, List<Object[]> datas){
		SysReport report = reportSql.getSysReport();
		if(StringUtils.isBlank(report.getCountInfo()) || datas.isEmpty()){
			return;
		}

		Object[] row = new Object[datas.get(0).length]; //新的一行
		
		String[] numbers = report.getCountInfo().split(","); //需要统计的列序号
		Set<String> set = new HashSet<String>();
		CollectionUtils.addAll(set, numbers);
		for(int i=0; i<row.length; i++){
			if(set.contains(String.valueOf(i + 1))){
				row[i] = count(datas, i);
			}
			else{
				row[i] = "";
			}
		}
		row[0] = "合计：" + row[0];  
		datas.add(row);
	}
	
	/**
	 * 统计计算
	 * @param datas 数据
	 * @param columnIndex 列序号
	 */
	private BigDecimal count(List<Object[]> datas, int columnIndex){
		BigDecimal total = BigDecimal.ZERO;
		for(Object[] array : datas){
			if(columnIndex >= array.length || array[columnIndex] == null || StringUtils.isBlank(array[columnIndex].toString())){
				continue;
			}
			BigDecimal temp = new BigDecimal(array[columnIndex].toString());
			total = total.add(temp);
		}
		return total.setScale(SCALE, BigDecimal.ROUND_HALF_UP);
	}

	@Transactional
	public SysReportSql save(SysReportSql entity) {
		if (Strings.isNullOrEmpty(entity.getFid())) {
			em.persist(entity);
			return entity;
		}else{
			return em.merge(entity);
		}
	}
	
	@Transactional
	public SysReportSql findOne(String id) {
		TypedQuery<SysReportSql> query = em.createQuery("select a from SysReportSql a where a.fid = :id", SysReportSql.class);
	    query.setParameter("id", id);
	    query.setMaxResults(1);
	    return query.getSingleResult();
	}

	@Transactional
	public void delete(String id) {
		Query query = em.createQuery("delete from SysReportSql a where a.fid = :id");
		query.setParameter("id", id);
		query.executeUpdate();
	}
}
