package cn.fooltech.fool_ops.domain.analysis.service;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.analysis.entity.TransportRoute;
import cn.fooltech.fool_ops.domain.analysis.repository.TransportRouteRepository;
import cn.fooltech.fool_ops.domain.analysis.vo.TransportRouteVo;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.VoFactory;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 服务类
 */
@Service
public class TransportRouteService extends BaseService<TransportRoute, TransportRouteVo, String> {

	@Autowired
	private TransportRouteRepository repository;

	/**
	 * 实体转换VO
	 * 
	 * @param entity
	 * @return
	 */
	@Override
	public TransportRouteVo getVo(TransportRoute entity) {
		TransportRouteVo vo = VoFactory.createValue(TransportRouteVo.class, entity);

		return vo;
	}

	@Override
	public CrudRepository<TransportRoute, String> getRepository() {
		return repository;
	}

	/**
	 * 查找分页
	 * 
	 * @param vo
	 * @param paramater
	 * @return
	 */
	public Page<TransportRouteVo> query(TransportRouteVo vo, PageParamater paramater) {

		String accId = SecurityUtil.getFiscalAccountId();
		Sort sort = new Sort(Sort.Direction.DESC, "createTime");
		PageRequest pageRequest = getPageRequest(paramater, sort);
		Page<TransportRoute> page = repository.findPageBy(accId, vo, pageRequest);
		return getPageVos(page, pageRequest);
	}

}
