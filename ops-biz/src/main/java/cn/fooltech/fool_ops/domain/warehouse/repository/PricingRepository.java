package cn.fooltech.fool_ops.domain.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.fooltech.fool_ops.domain.warehouse.entity.Pricing;

/**
 * 单据核价Repository
 * @author xjh
 *
 */
public interface PricingRepository extends JpaRepository<Pricing, String>{

	
}
