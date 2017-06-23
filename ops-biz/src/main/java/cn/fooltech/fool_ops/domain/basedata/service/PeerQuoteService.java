package cn.fooltech.fool_ops.domain.basedata.service;


import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.*;
import cn.fooltech.fool_ops.domain.basedata.repository.*;
import cn.fooltech.fool_ops.domain.basedata.vo.PeerQuoteVo;
import cn.fooltech.fool_ops.domain.common.service.AttachService;
import cn.fooltech.fool_ops.domain.freight.entity.FreightAddress;
import cn.fooltech.fool_ops.domain.freight.repository.FreightAddressRepository;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.VoFactory;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 同行报价服务类
 */
@Service
public class PeerQuoteService extends BaseService<PeerQuote, PeerQuoteVo, String> {


    @Autowired
    private PeerQuoteRepository repository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private GoodsSpecRepository specRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private FreightAddressRepository addressRepository;
    
    @Autowired
    private AttachService attachService;

    /**
     * 实体转换VO
     * @param entity
     * @return
     */
    @Override
    public PeerQuoteVo getVo(PeerQuote entity) {
        if(entity==null)return null;
        PeerQuoteVo vo = VoFactory.createValue(PeerQuoteVo.class, entity);
        if(!Strings.isNullOrEmpty(entity.getCustomerId())){
            Customer customer = customerRepository.findOne(entity.getCustomerId());
            vo.setCustomerName(customer.getName());
            vo.setCustomerId(entity.getCustomerId());
        }
        if(entity.getGoods()!=null){
            Goods goods = entity.getGoods();
            vo.setGoodsName(goods.getName());
            vo.setGoodsId(goods.getFid());
        }
        if(!Strings.isNullOrEmpty(entity.getGoodSpecId())){
            GoodsSpec spec = specRepository.findOne(entity.getGoodSpecId());
            vo.setGoodSpecId(entity.getGoodSpecId());
            vo.setSpecName(spec.getName());
        }
        if(!Strings.isNullOrEmpty(entity.getUnitId())){
            Unit unit = unitRepository.findOne(entity.getUnitId());
            vo.setUnitName(unit.getName());
            vo.setUnitId(entity.getUnitId());
        }
        if(!Strings.isNullOrEmpty(entity.getReceiptPlace())){
            FreightAddress receiptPlace = addressRepository.findOne(entity.getReceiptPlace());
            vo.setReceiptPlace(receiptPlace.getFid());
            vo.setReceiptPlaceName(receiptPlace.getName());
        }
        return vo;
    }

    @Override
    public CrudRepository<PeerQuote, String> getRepository() {
        return repository;
    }

    /**
     * 查找分页
     * @param vo
     * @param paramater
     * @return
     */
    public Page<PeerQuoteVo> query(PeerQuoteVo vo, PageParamater paramater){

        String accId = SecurityUtil.getFiscalAccountId();
        String creatorId = SecurityUtil.getCurrentUserId();
        Sort sort = new Sort(Sort.Direction.DESC, "code");
        PageRequest pageRequest = getPageRequest(paramater, sort);
        Page<PeerQuote> page = repository.findPageBy(accId, creatorId, vo, pageRequest);
        return getPageVos(page, pageRequest);
    }

    /**
     * 修改或新增
     * @param vo
     * @return
     */
    @Transactional
    public RequestResult save(PeerQuoteVo vo){

        PeerQuote entity = null;
        if(Strings.isNullOrEmpty(vo.getId())){
            entity = new PeerQuote();
            entity.setAccId(SecurityUtil.getFiscalAccountId());
            entity.setCreatorId(SecurityUtil.getCurrentUserId());
            entity.setCreateTime(new Date());
            entity.setUpdateTime(new Date());
            entity.setOrgId(SecurityUtil.getCurrentOrgId());
            entity.setBillDate(new Date());
        }else{
            entity = repository.findOne(vo.getId());

            if(entity.getUpdateTime().compareTo(vo.getUpdateTime())!=0){
                return buildFailRequestResult("数据已被其他用户修改，请刷新再试");
            }
            entity.setUpdateTime(new Date());
        }
        if(Strings.isNullOrEmpty(vo.getGoodSpecId())){
            entity.setGoodSpecId(null);
        }else{
            entity.setGoodSpecId(vo.getGoodSpecId());
        }

        entity.setCode(vo.getCode());
        entity.setDescribe(vo.getDescribe());
        entity.setGoods(goodsRepository.findOne(vo.getGoodsId()));
        entity.setCustomerId(vo.getCustomerId());
        entity.setUnitId(vo.getUnitId());
        entity.setReceiptPlace(vo.getReceiptPlace());
        entity.setDeliveryPrice(vo.getDeliveryPrice());
        entity.setSupplier(vo.getSupplier());

        repository.save(entity);

        attachService.saveBase64Attach(vo.getBase64Str(), entity.getId());
        
        return buildSuccessRequestResult(getVo(entity));
    }

    /**
     * 根据属性查询最后一次记录
     * @param goodsId
     * @param goodSpecId
     * @param unitId
     * @param receiptPlace
     * @return
     */
    public RequestResult queryLast(String goodsId, String goodSpecId, String unitId,
                                   String receiptPlace){

        String accId = SecurityUtil.getFiscalAccountId();
        String creatorId = SecurityUtil.getCurrentUserId();
        PeerQuote entity = repository.findLastRecord(accId, creatorId, goodsId, goodSpecId, unitId, receiptPlace);
        return buildSuccessRequestResult(getVo(entity));
    }
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
    /**
     * 根据收货地ID获取列表
     * @param vo
     * @return
     */
    public List queryByReceiptPlaceCount(String receiptPlace,String faccId){
    	String sql= " SELECT * FROM tsb_peer_quote p where p.FRECEIPT_PLACE=? and p.FACC_ID=?";
    	List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,new Object[]{receiptPlace,faccId});
    	return list;
    }
}
