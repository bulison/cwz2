package cn.fooltech.fool_ops.domain.basedata.service;


import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.GroundPrice;
import cn.fooltech.fool_ops.domain.basedata.entity.GroundPriceDetail;
import cn.fooltech.fool_ops.domain.basedata.repository.AuxiliaryAttrRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.GroundPriceDetailRepository;
import cn.fooltech.fool_ops.domain.basedata.vo.GroundPriceDetailVo;
import cn.fooltech.fool_ops.domain.basedata.vo.GroundPriceVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.VoFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * 场地报价明细
 */
@Service
public class GroundPriceDetailService extends BaseService<GroundPriceDetail, GroundPriceDetailVo, String>{

    @Autowired
    private AuxiliaryAttrRepository attrRepository;


    @Autowired
    private GroundPriceDetailRepository repository;

	@Override
    public GroundPriceDetailVo getVo(GroundPriceDetail entity) {
        GroundPriceDetailVo vo = VoFactory.createValue(GroundPriceDetailVo.class, entity);

        if(!Strings.isNullOrEmpty(entity.getTransportCostId())){
            AuxiliaryAttr transportCost = attrRepository.findOne(entity.getTransportCostId());
            vo.setTransportCostName(transportCost.getName());
        }

        if(!Strings.isNullOrEmpty(entity.getTransportUnitId())){
            AuxiliaryAttr transportUnit = attrRepository.findOne(entity.getTransportUnitId());
            vo.setTransportUnitName(transportUnit.getName());
        }

        return vo;
    }

    @Override
    public CrudRepository<GroundPriceDetail, String> getRepository() {
        return repository;
    }

    /**
     * 保存场地报价明细
     * @param details
     * @return
     */
    @Transactional
    public RequestResult saveDetails(GroundPrice entity, String details){

        List<GroundPriceDetailVo> detailList = Lists.newArrayList();
        List<GroundPriceVo> list = Lists.newArrayList();
        if(StringUtils.isNotBlank(details)){
            JSONArray array = JSONArray.fromObject(details);
            JsonConfig config = new JsonConfig();
            config.setRootClass(GroundPriceDetailVo.class);
            config.setAllowNonStringKeys(true);
            detailList = (List)JSONArray.toCollection(array, config);
            Iterator iterator = detailList.iterator();
            while(iterator.hasNext()){
                GroundPriceDetailVo detail = (GroundPriceDetailVo) iterator.next();
                save(entity, detail);
            }
        }
        return buildSuccessRequestResult(detailList);
    }

    /**
     * 保存场地报价明细
     * @param detail
     * @return
     */
    @Transactional
    public RequestResult save(GroundPrice entity, GroundPriceDetailVo detail){

        GroundPriceDetail detailEntity = VoFactory.createValue(GroundPriceDetail.class, detail);
        detailEntity.setAccId(SecurityUtil.getFiscalAccountId());
        detailEntity.setCreateTime(new Date());
        detailEntity.setCreatorId(SecurityUtil.getCurrentUserId());
        detailEntity.setDescribe("");
        detailEntity.setUpdateTime(new Date());
        detailEntity.setOrgId(SecurityUtil.getCurrentOrgId());
        detailEntity.setBillId(entity.getId());
        repository.save(detailEntity);

        return buildSuccessRequestResult();
    }

    /**
     * 根据主表ID查询明细
     * @param billId
     * @return
     */
    public List<GroundPriceDetailVo> findByBillId(String billId){
        return getVos(repository.findByBillId(billId));
    }

    /**
     * 根据主表ID删除
     * @return
     */
    public RequestResult deleteByBillId(String billId){
        List<GroundPriceDetail> datas = repository.findByBillId(billId);
        repository.delete(datas);
        return buildSuccessRequestResult();
    }
}
