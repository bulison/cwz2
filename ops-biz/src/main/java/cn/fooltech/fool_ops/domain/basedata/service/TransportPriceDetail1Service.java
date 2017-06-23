package cn.fooltech.fool_ops.domain.basedata.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.TransportPriceDetail1;
import cn.fooltech.fool_ops.domain.basedata.repository.TransportPriceDetail1Repository;
import cn.fooltech.fool_ops.domain.basedata.vo.TransportPriceDetail1Vo;
import cn.fooltech.fool_ops.domain.basedata.vo.TransportPriceDetail2Vo;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccount;
import cn.fooltech.fool_ops.domain.freight.entity.FreightAddress;
import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
import cn.fooltech.fool_ops.domain.sysman.entity.User;
import cn.fooltech.fool_ops.utils.JsonUtil;
import cn.fooltech.fool_ops.utils.VoFactory;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class TransportPriceDetail1Service extends BaseService<TransportPriceDetail1, TransportPriceDetail1Vo, String> {


    @Autowired
    private TransportPriceDetail1Repository repository;
    /**
     * 运输费报价(从2表)服务类
     */
    @Autowired
    private TransportPriceDetail2Service detail2Service;

    @Override
    public TransportPriceDetail1Vo getVo(TransportPriceDetail1 entity) {
        TransportPriceDetail1Vo vo = VoFactory.createValue(TransportPriceDetail1Vo.class, entity);
        User user = entity.getCreator();
        if(user!=null){
        	vo.setCreatorId(user.getFid());
        	vo.setCreatorName(user.getUserName());
        }
        FreightAddress deliveryPlace = entity.getDeliveryPlace();
        if(deliveryPlace!=null){
        	vo.setDeliveryPlaceId(deliveryPlace.getFid());
        	vo.setDeliveryPlaceName(deliveryPlace.getName());
        }
        FreightAddress receiptPlace = entity.getReceiptPlace();
        if(receiptPlace!=null){
        	vo.setReceiptPlaceId(receiptPlace.getFid());
        	vo.setReceiptPlaceName(receiptPlace.getName());
        }
        FiscalAccount account = entity.getFiscalAccount();
        if(account!=null){
        	vo.setFiscalAccountId(account.getFid());
        	vo.setFiscalAccountName(account.getName());
        }
        Organization org = entity.getOrg();
        if(org!=null){
        	vo.setOrgId(org.getFid());
        	vo.setOrgName(org.getOrgName());
        }
        AuxiliaryAttr attr = entity.getTransportType();
	    if(attr!=null){
	    	vo.setTransportTypeId(attr.getFid());
	    	vo.setTransportTypeName(attr.getName());
	    }
	    AuxiliaryAttr shipmentType = entity.getShipmentType();
	    if(shipmentType!=null){
	    	vo.setShipmentTypeId(shipmentType.getFid());
	    	vo.setShipmentTypeName(shipmentType.getName());
	    }
        AuxiliaryAttr transportUnit = entity.getTransportUnit();
        if(transportUnit!=null){
        	vo.setTransportUnitId(transportUnit.getFid());
        	vo.setTransportUnitName(transportUnit.getName());
        }
	    
        return vo;
    }

    @Override
    public CrudRepository<TransportPriceDetail1, String> getRepository() {
        return repository;
    }
    
    /**
     * 运输费报价查找分页
     * @param billId
     * @param paramater
     * @return
     */
    public List<TransportPriceDetail1Vo> query(String billId, PageParamater paramater) {
        Sort sort = new Sort(Sort.Direction.ASC, "createTime");
        PageRequest request = getPageRequest(paramater,sort);
        Page<TransportPriceDetail1> page = repository.findByBillId(billId, request);
        Page<TransportPriceDetail1Vo> pageVos = getPageVos(page, request);
        List<TransportPriceDetail1Vo> list = pageVos.getContent();
        List<TransportPriceDetail1Vo> list2= Lists.newArrayList();
        for (TransportPriceDetail1Vo vo : list) {
            List<TransportPriceDetail2Vo> detail2Vos = detail2Service.query(billId, vo.getId());
            JSONArray jsonArray = JSONArray.fromObject(detail2Vos);
            vo.setDetails2(jsonArray);
            list2.add(vo);

		}
        return list2;
    }

	/**
	 * josn转换List<TransportTemplateDetail1Vo>
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<TransportPriceDetail1Vo> getDetails(String details) {
		List<TransportPriceDetail1Vo> list = JsonUtil.toObjectList(details, TransportPriceDetail1Vo.class);
		return list;
	}
	/**
	 * 根据运输报价id删除
	 * @param tempId
	 */
	@Transactional
	public void delByTempId(String tempId){
		repository.delByTempId(tempId);
	};
}
