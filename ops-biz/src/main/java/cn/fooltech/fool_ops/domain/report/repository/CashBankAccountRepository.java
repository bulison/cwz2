package cn.fooltech.fool_ops.domain.report.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;


@Repository
public class CashBankAccountRepository {

	@PersistenceContext
	private EntityManager entityManager;
	
	/**
	 * 获取收支列表
	 * @param orgId 机构ID
	 * @param start 开始时间
	 * @param end 结束时间
	 * @param firstRow 开始行
	 * @param maxRow 结束行
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> getInOrExpList(String orgId,String start,String end,int type,int maxRow)throws Exception{
		String billTypeStr = "";
		String amountStr = "";
		//0-收支，1-收入，2-支出，空-收支
		if(type==1){
			billTypeStr = " and tpb.FBILL_TYPE = 51";
			amountStr = " and tcb.FINCOME_AMOUNT <> 0";
		}else if(type==2){
			billTypeStr = " and tpb.FBILL_TYPE = 52";
			amountStr = " and tcb.FINCOME_AMOUNT = 0";
		}
		
		String sql = "select * from (select tpb.FAMOUNT,IF(tpb.FBILL_TYPE=51,(select c.FNAME from tbd_customer c where c.FID = tpb.FCUSTOMER_ID),"
				+ "(select s.FNAME from tbd_supplier s where s.FID = tpb.FSUPPLIER_ID)) as fname,tpb.FCREATE_TIME from tsb_payment_bill tpb "
				+ "where tpb.RECORD_STATUS = 1 and tpb.FORG_ID = '"+orgId+"'"+billTypeStr+" UNION all select (tcb.FINCOME_AMOUNT+tcb.FFREE_AMOUNT) famount,"
				+ "(select taa.fname from tbd_auxiliary_attr taa where taa.fid = tcb.FFEE_ID) fnmae,tcb.FCREATE_TIME from tsb_cost_bill tcb "
				+ "where tcb.RECORD_STATUS = 1 and tcb.FORG_ID = '"+orgId+"'"+amountStr+") tmp where tmp.FCREATE_TIME >= '"+start+"' "
						+ "and tmp.FCREATE_TIME <= '"+end+"' order by tmp.FCREATE_TIME desc limit 0,"+maxRow+"";
		
		Query query = entityManager.createNativeQuery(sql);
		List<Object[]> list = query.getResultList();
		if(null==list||list.size()==0)return null;
		return list;
		
	}
	
	/**
	 * 获取净资金,当天收入，当天支出
	 * @param orgId
	 * @param stat
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> getCashTotal(String orgId,String stat,String end)throws Exception{
		String sql = "select a,IFNULL(income,0),IFNULL(outcome,0) from ( select (select (sum(income)-sum(outcome)) from ("
				+ "select FINCOME_AMOUNT as income,FFREE_AMOUNT as outcome from tsb_cost_bill where forg_id='"+orgId+"' and record_status = 1 "
						+ "and FDATE<='"+end+"' union all select sum(case when pb.fbill_type=51 then  pb.famount else 0 end) as income,"
								+ "sum(case when pb.fbill_type=52 then  pb.famount else 0 end) as outcome from tsb_payment_bill pb where "
								+ "pb.FORG_ID = '"+orgId+"' and record_status = 1 and pb.FBILL_DATE <= '"+end+"') tmp) as a,"
										+ "sum(income) income,sum(outcome) outcome from (select FINCOME_AMOUNT as income,FFREE_AMOUNT as outcome "
										+ "from tsb_cost_bill where forg_id='"+orgId+"' and record_status = 1 and FDATE>'"+stat+"' and "
												+ "FDATE<'end' union all select sum(case when pb.fbill_type=51 then  pb.famount else 0 end) as income,"
														+ "sum(case when pb.fbill_type=52 then  pb.famount else 0 end) as outcome from tsb_payment_bill pb "
														+ "where pb.FORG_ID = '"+orgId+"' and record_status = 1 and pb.FBILL_DATE> '"+stat+"' and pb.FBILL_DATE < "
																+ "'"+end+"') tmp) tmp2";
		
		Query query = entityManager.createNativeQuery(sql);
		List<Object[]> list = query.getResultList();
		if(null==list||list.size()==0)return null;
		return list;
		
	}
	
	
	/**
	 * 
	 * @param bankId
	 *            银行账户编号
	 * @param orgId
	 *            机构编号
	 * @param start
	 *            开始时间
	 * @param end
	 *            结束时间
	 * @return
	 * @throws Exception
	 */
	public List getList(String bankId, String orgId, String accId, String start,
			String end,String sort,int order, int first, int max) throws Exception {
		
		String sql = "call p_cashBankAccount_sel('" + orgId + "','" + accId + "','" + bankId
				+ "',date_format('" + start
				+ "','%Y-%c-%d'),date_format('" + end
				+ "','%Y-%c-%d'),'"+sort+"',"+order+","+first+","+max+",";
		
		Query query = entityManager.createNativeQuery(sql+"1)");
		List list = query.getResultList();
		if(null==list||list.size()==0)return null; 
		
		return list;
	}
	
	/**
	 * 
	 * @param bankId
	 *            银行账户编号
	 * @param orgId
	 *            机构编号
	 * @param start
	 *            开始时间
	 * @param end
	 *            结束时间
	 * @return
	 * @throws Exception
	 */
	public long countList(String bankId, String orgId, String accId, String start,
			String end,String sort,int order) throws Exception {
		
		int first = 0;
		int max = Integer.MAX_VALUE;
		String sql = "call p_cashBankAccount_sel('" + orgId + "','" + accId + "','" + bankId
				+ "',date_format('" + start
				+ "','%Y-%c-%d'),date_format('" + end
				+ "','%Y-%c-%d'),'"+sort+"',"+order+","+first+","+max+",";
		
		Query query = entityManager.createNativeQuery(sql+"0)");
		List list = query.getResultList();
		if(null==list||list.size()==0)return 0;
		Object obj = list.get(0);
		long totalCount = Long.parseLong(obj.toString());
		
		return totalCount;
	}
}
