package cn.fooltech.fool_ops.domain.basedata.service;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.Goods;
import cn.fooltech.fool_ops.domain.basedata.entity.GoodsPercentage;
import cn.fooltech.fool_ops.domain.basedata.repository.GoodsPercentageRepository;
import cn.fooltech.fool_ops.domain.basedata.vo.GoodsPercentageVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.VoFactory;

/**
 * 
 * <p>
 * 货品提成服务类
 * </p>
 * 
 * @author cwz
 * @date 2017年6月19日
 */
@Service
public class GoodsPercentageService extends BaseService<GoodsPercentage, GoodsPercentageVo, String> {

	@Autowired
	private GoodsPercentageRepository repository;
	
	@Autowired
	private GoodsService goodsService;

	/**
	 * 实体转换VO
	 * 
	 * @param entity
	 * @return
	 */
	@Override
	public GoodsPercentageVo getVo(GoodsPercentage entity) {
		GoodsPercentageVo vo = VoFactory.createValue(GoodsPercentageVo.class, entity);
		Goods goods = entity.getGoods();
		if(goods!=null){
			vo.setGoodsId(goods.getFid());
			vo.setGoodsName(goods.getName());
		}
		return vo;
	}

	@Override
	public CrudRepository<GoodsPercentage, String> getRepository() {
		return repository;
	}

	/**
	 * 查找分页
	 * 
	 * @param vo
	 * @param paramater
	 * @return
	 */
	public Page<GoodsPercentageVo> query(GoodsPercentageVo vo, PageParamater paramater) {

		String accId = SecurityUtil.getFiscalAccountId();
		Sort sort = new Sort(Sort.Direction.DESC, "goods.code");
		PageRequest pageRequest = getPageRequest(paramater, sort);
		Page<GoodsPercentage> page = repository.findPageBy(accId, vo, pageRequest);
		return getPageVos(page, pageRequest);
	}

	/**
	 * 修改或新增
	 * 
	 * @param vo
	 * @return
	 */
	@Transactional
	public RequestResult save(GoodsPercentageVo vo) {

		GoodsPercentage entity = null;
		if (Strings.isNullOrEmpty(vo.getFid())) {
			entity = new GoodsPercentage();
			entity.setFiscalAccount(SecurityUtil.getFiscalAccount());
			entity.setOrg(SecurityUtil.getCurrentOrg());
			entity.setUpdateTime(new Date());
		} else {
			entity = repository.findOne(vo.getFid());
			if (entity.getUpdateTime().compareTo(vo.getUpdateTime()) != 0) {
				return buildFailRequestResult("数据已被其他用户修改，请刷新再试");
			}
			entity.setUpdateTime(vo.getUpdateTime());
		}
		if(Strings.isNullOrEmpty(vo.getGoodsId())){
			return buildFailRequestResult("商品不能为空!");
		}
		Goods goods = goodsService.get(vo.getGoodsId());
		if(goods==null){
			return buildFailRequestResult("商品已删除,请刷新再试!");
		}
		entity.setGoods(goods);
		entity.setPercentage(vo.getPercentage());
		entity.setIsLast(vo.getIsLast());

		repository.save(entity);

		return buildSuccessRequestResult(getVo(entity));
	}
	/**
	 * 检索是否存在相同提成点数
	 * @param goodsId
	 * @param percentage
	 * @param orgId
	 * @param accId
	 * @return
	 */
	public GoodsPercentage findTopBy(String goodsId,BigDecimal percentage,String orgId, String accId){
		return repository.findTopBy(goodsId, percentage, orgId, accId);
	}
}
