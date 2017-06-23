package cn.fooltech.fool_ops.eureka.rateService.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.fooltech.fool_ops.eureka.rateService.vo.RateMemberDetailVo;
import cn.fooltech.fool_ops.eureka.rateService.vo.RateMemberSumVo;

/**
 * 执行人效益dao
 * @author hjr
 *	2017-3-27
 */
@Mapper
public interface RateMemberDao {
 public List<RateMemberSumVo> queryRateMemberSum(@Param("orgId") String orgId
		 											,@Param("accId") String accId
		 											,@Param("memberId") String memberId
		 												,@Param("sidx") String sidx
		 												);
 public List<RateMemberDetailVo> queryRateMemberDetailByMemberId(@Param("orgId") String orgId
		 													,@Param("accId") String accId
		 													,@Param("memberId") String memberId
		 													,@Param("startDay") String startDay
		 													,@Param("endDay") String endDay);
}
