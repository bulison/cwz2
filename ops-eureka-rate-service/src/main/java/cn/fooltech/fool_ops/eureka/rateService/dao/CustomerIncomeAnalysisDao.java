package cn.fooltech.fool_ops.eureka.rateService.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.fooltech.fool_ops.eureka.rateService.vo.CapitalPlanChangeLogTemVo;
import cn.fooltech.fool_ops.eureka.rateService.vo.CapitalPlanDetailTemVo;
import cn.fooltech.fool_ops.eureka.rateService.vo.CustomerIncomeAnalysisVo;
import cn.fooltech.fool_ops.eureka.rateService.vo.PayMentTemVo;
import cn.fooltech.fool_ops.eureka.rateService.vo.WarehouseBillTemVo;

/**
 * 客户收益分析DAO
 * @author hjr
 *2017-4-7
 */
@Mapper
public interface CustomerIncomeAnalysisDao {
	public List<CustomerIncomeAnalysisVo> customerIncomeAnalysisProcessing(@Param("endDay") String endDay,
																			@Param("startDay") String startDay,
																			@Param("orgId") String orgId,
																			@Param("accId") String accId,
																			@Param("customerId")String customerId,
																			@Param("category")String category,
																			@Param("area")String area,
																			@Param("page")Integer page,
																			@Param("rows")Integer rows);
	public List<WarehouseBillTemVo> findWarehouseBillByCustomerId(@Param("customerId")String customerId
																	,@Param("accId")String accId);
	public BigDecimal findRate(@Param("orgId")String orgId);
	public List<PayMentTemVo> findPayMentByWareHouseBillId(@Param("wareHouseBillId")String wareHouseBillId
															,@Param("accId")String accId);
	public List<CapitalPlanDetailTemVo> findPlanDateByCustomerId(@Param("customerId")String customerId
																	,@Param("accId")String accId);
	public WarehouseBillTemVo findWarehouseBillById(@Param("fid")String fid);
//	public List<CapitalPlanDetailTemVo> findPlanDetailByWareHouseBillId(@Param("relationId")String relationId);
	public List<CapitalPlanChangeLogTemVo> findPlanChangeLogByPlanDetailId(@Param("detailId")String detailId
																			,@Param("accId")String accId);
	public BigDecimal findWareHouseBillDetailCostAmount(@Param("wareHouseBillId")String wareHouseBillId
														,@Param("accId")String accId);
	public BigDecimal findCostByWareHouseBillId(@Param("wareHouseBillId")String wareHouseBillId
												,@Param("accId")String accId);
	public List<WarehouseBillTemVo> findWarehouseBillByCustomerIdAndDate(@Param("customerId")String customerId
																	,@Param("startDate")String startDate
																	,@Param("endDate")String endDate
																	,@Param("accId")String accId);
	public List<CapitalPlanDetailTemVo> findPlanDateByCustomerIdAndDate(@Param("customerId")String customerId
																			,@Param("startDate")String startDate
																			,@Param("endDate")String endDate
																			,@Param("accId")String accId);
	public String findAttrNameById(@Param("fid")String fid);


}
