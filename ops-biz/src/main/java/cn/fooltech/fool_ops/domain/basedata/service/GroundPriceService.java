package cn.fooltech.fool_ops.domain.basedata.service;


import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.GroundPrice;
import cn.fooltech.fool_ops.domain.basedata.entity.GroundPriceDetail;
import cn.fooltech.fool_ops.domain.basedata.repository.GroundPriceRepository;
import cn.fooltech.fool_ops.domain.basedata.vo.GroundPriceDetailVo;
import cn.fooltech.fool_ops.domain.basedata.vo.GroundPriceVo;
import cn.fooltech.fool_ops.domain.common.service.AttachService;
import cn.fooltech.fool_ops.domain.freight.entity.FreightAddress;
import cn.fooltech.fool_ops.domain.freight.repository.FreightAddressRepository;
import cn.fooltech.fool_ops.utils.JsonUtil;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.VoFactory;
import com.google.common.base.Strings;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 场地费报价服务类
 */
@Service
public class GroundPriceService extends BaseService<GroundPrice, GroundPriceVo, String>{


    @Autowired
    private GroundPriceRepository repository;

    @Autowired
    private GroundPriceDetailService detailService;

    @Autowired
    private FreightAddressRepository freightAddressRepository;
    
    @Autowired
    private AttachService attachService;

	@Override
    public GroundPriceVo getVo(GroundPrice entity) {
        GroundPriceVo vo = VoFactory.createValue(GroundPriceVo.class, entity);
        if(!Strings.isNullOrEmpty(entity.getAddressId())){
            FreightAddress freightAddress = freightAddressRepository.findOne(entity.getAddressId());
            vo.setAddressId(entity.getAddressId());
            vo.setAddressName(freightAddress.getName());
        }

        List<GroundPriceDetailVo> details = detailService.findByBillId(entity.getId());
        vo.setDetailVos(details);
        return vo;
    }

    @Override
    public CrudRepository<GroundPrice, String> getRepository() {
        return repository;
    }

    /**
     *  查询分页
     * @param paramater
     * @param vo
     * @return
     */
    public Page<GroundPriceVo> query(GroundPriceVo vo, PageParamater paramater){

        String accId = SecurityUtil.getFiscalAccountId();
        Sort sort = new Sort(Sort.Direction.DESC, "code");
        PageRequest pageRequest = getPageRequest(paramater, sort);
        Page<GroundPrice> page = repository.findPageBy(accId, vo, pageRequest);
        return getPageVos(page, pageRequest);
    }

    /**
     * 修改或新增
     * @param vo
     * @return
     */
    @Transactional
    public RequestResult save(GroundPriceVo vo){

        if(isExistCode(vo.getCode(), vo.getId())){
            return buildFailRequestResult("单号已存在");
        }

        GroundPrice entity = null;
        if(Strings.isNullOrEmpty(vo.getId())){

            entity = new GroundPrice();
            entity.setAccId(SecurityUtil.getFiscalAccountId());
            entity.setCreatorId(SecurityUtil.getCurrentUserId());
            entity.setCreateTime(new Date());
            entity.setUpdateTime(new Date());
            entity.setOrgId(SecurityUtil.getCurrentOrgId());
        }else{
            entity = repository.findOne(vo.getId());

            if(entity.getUpdateTime().compareTo(vo.getUpdateTime())!=0){
                return buildFailRequestResult("数据已被其他用户修改，请刷新再试");
            }
        }
        entity.setAddressId(vo.getAddressId());
        entity.setBillDate(vo.getBillDate());
        entity.setCode(vo.getCode());
        entity.setDescribe(vo.getDescribe());
        entity.setEffectiveDate(vo.getEffectiveDate());
        entity.setEnable(GroundPrice.ENABLE_Y);
        entity.setUpdateTime(new Date());

        repository.save(entity);

        String details = vo.getDetails();
        RequestResult saveDetail = detailService.saveDetails(entity, details);

        //entity.setCostAmount(sumDetailCostAmount((List<GroundPriceDetailVo>)saveDetail.getData()));
        //repository.save(entity);

        List<GroundPrice> existValids = repository.findValidByAddressId(entity.getAddressId(), entity.getId());
        if(existValids.size()>0){

            GroundPrice first = existValids.remove(0);
            if(first.getBillDate().compareTo(entity.getBillDate())>0){
                entity.setEnable(GroundPrice.ENABLE_N);
                repository.save(entity);
            }else{
                first.setEnable(GroundPrice.ENABLE_N);
                repository.save(first);
            }

            for(GroundPrice groundPrice:existValids){
                groundPrice.setEnable(GroundPrice.ENABLE_N);
                repository.save(groundPrice);
            }
        }

        attachService.saveBase64Attach(vo.getBase64Str(), entity.getId());
        
        return buildSuccessRequestResult(entity.getId());
    }

    /**
     * 合计成本金额
     * @return
     */
    public BigDecimal sumDetailCostAmount(List<GroundPriceDetailVo> detailVos){
        BigDecimal sum = BigDecimal.ZERO;
        for(GroundPriceDetailVo detailVo:detailVos){
            if(detailVo.getCostSign()== GroundPriceDetail.COST_SIGN_Y){
                sum = NumberUtil.add(sum, detailVo.getAmount());
            }
        }
        return sum;
    }

    /**
     * 判断编号是否存在
     * @param code
     * @param excludeId
     * @return
     */
    private boolean isExistCode(String code, String excludeId){
        Long count = 0L;
        String accId = SecurityUtil.getFiscalAccountId();
        if(Strings.isNullOrEmpty(excludeId)){
            count = repository.countByCode(code, accId);
        }else{
            count = repository.countByCode(code, excludeId, accId);
        }
        if(count!=null && count>0){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 删除
     * @return
     */
    public RequestResult delete(String id){
        detailService.findByBillId(id);
        repository.delete(id);
        return buildSuccessRequestResult();
    }
    
	/**
	 * 根据场地id查询记录
	 * @param fid	场地id	
	 * @return
	 */
	public Long queryByAddressCount(String fid){
		return repository.queryByAddressCount(fid);
		
	}
}
