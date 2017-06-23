package cn.fooltech.fool_ops.domain.common.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import cn.fooltech.fool_ops.domain.common.entity.Attach;

public interface AttachRepository extends JpaRepository<Attach, String> {

	/**
	 * 根据业务ID获取附件信息
	 * @param busId
	 * @param status
	 * @return
	 */
	public List<Attach> findByBusIdAndStatus(String busId, String status, Sort sort);
	
	/**
	 * 根据业务ID和状态获取附件信息
	 * @param busId
	 * @param status
	 * @return
	 */
	public List<Attach> findByBusIdAndStatus(String busId, String status);
	
	/**
	 * 根据业务ID获取附件信息
	 * @param busId
	 * @return
	 */
	public List<Attach> findByBusId(String busId);
}
