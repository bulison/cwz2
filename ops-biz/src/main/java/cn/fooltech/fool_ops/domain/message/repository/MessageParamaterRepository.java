package cn.fooltech.fool_ops.domain.message.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.domain.message.entity.Message;
import cn.fooltech.fool_ops.domain.message.entity.MessageParamater;

public interface MessageParamaterRepository extends JpaRepository<MessageParamater, String>, FoolJpaSpecificationExecutor<MessageParamater> {

	
	
	/**
	 * 操作信息，将其状态设置为已操作
	 * @param busClass 业务类型
	 * @param busScene 业务场景（操作前的状态）
	 * @param busData 业务数据（FID）
	 */
	@Query("select a from MessageParamater a where busClass=?1 and busScene=?2 and busData=?3" )
	public List<MessageParamater> oper(String busClass, String busScene, String busData);	

	
	@Query("select a from MessageParamater a where busData=?1")
	public List<MessageParamater> getByBusData(String busData);	
	
}	

