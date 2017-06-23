package cn.fooltech.fool_ops.domain.rate.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.domain.rate.entity.RateProgrammeRecord;

public interface RateProgrammeRecordRepository extends FoolJpaRepository<RateProgrammeRecord, String>, 
	FoolJpaSpecificationExecutor<RateProgrammeRecord> {

	@Query("select s from RateProgrammeRecord s where s.rateProgramme.fid=?1")
	public List<RateProgrammeRecord> findByRateId(String rateId);
	
}	

