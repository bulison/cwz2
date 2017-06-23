package cn.fooltech.fool_ops.domain.basedata.service;

import java.util.List;

import cn.fooltech.fool_ops.domain.basedata.vo.TransportPriceDetail1Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttr;
import cn.fooltech.fool_ops.domain.basedata.entity.TransportPrice;
import cn.fooltech.fool_ops.domain.basedata.entity.TransportPriceDetail1;
import cn.fooltech.fool_ops.domain.basedata.entity.TransportPriceDetail2;
import cn.fooltech.fool_ops.domain.basedata.repository.TransportPriceDetail2Repository;
import cn.fooltech.fool_ops.domain.basedata.vo.TransportPriceDetail2Vo;
import cn.fooltech.fool_ops.utils.JsonUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.VoFactory;

@Service
public class TransportPriceDetail2Service extends BaseService<TransportPriceDetail2, TransportPriceDetail2Vo, String> {


    @Autowired
    private TransportPriceDetail2Repository repository;

    @Override
    public TransportPriceDetail2Vo getVo(TransportPriceDetail2 entity) {
        TransportPriceDetail2Vo vo = VoFactory.createValue(TransportPriceDetail2Vo.class, entity);
        vo.setId(entity.getId());
        vo.setDescribe(entity.getDescribe());
        TransportPrice bill = entity.getBill();
        if(bill!=null){
        	vo.setBillId(bill.getId());
        }
        TransportPriceDetail1 detail1 = entity.getDetail1();
        if(detail1!=null){
        	vo.setDetail1Id(detail1.getId());
            vo.setDeliveryPlace(entity.getDetail1().getDeliveryPlace().getName());
            vo.setReceiptPlace(entity.getDetail1().getReceiptPlace().getName());
        }
        AuxiliaryAttr transportCost = entity.getTransportCost();
        if(transportCost!=null){
        	vo.setTransportCostId(transportCost.getFid());
            vo.setTransportCostCode(transportCost.getCode());
        	vo.setTransportCostName(transportCost.getName());
        }
        AuxiliaryAttr transportUnit = entity.getTransportUnit();
        if(transportUnit!=null){
        	vo.setTransportUnitId(transportUnit.getFid());
        	vo.setTransportUnitName(transportUnit.getName());
        }
        return vo;
    }

    @Override
    public CrudRepository<TransportPriceDetail2, String> getRepository() {
        return repository;
    }
    /**
     * 运输费报价查找分页
     * @param billId
     * @param detail1Id
     * @return
     */
    public List<TransportPriceDetail2Vo> query(String billId, String detail1Id) {
        String accId = SecurityUtil.getFiscalAccountId();
        List<TransportPriceDetail2> list = repository.queryDetail(accId, billId, detail1Id);
        return getVos(list);
    }

    /**
     * 根据主表ID查询明细表2的数据
     * @param billId
     * @return
     */
    public List<TransportPriceDetail2> findByBillId(String billId){
        return repository.queryDetail(billId);
    }

    /**
     * 运输费查找
     * @param billId
     * @return
     */
    public List<TransportPriceDetail2Vo> query(String billId) {
        List<TransportPriceDetail2> list = repository.queryDetail(billId);
        return getVos(list);
    }
	/**
	 * josn转换List<TransportTemplateDetail1Vo>
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<TransportPriceDetail2Vo> getDetails(String details) {
		List<TransportPriceDetail2Vo> list = JsonUtil.toObjectList(details, TransportPriceDetail2Vo.class);
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
