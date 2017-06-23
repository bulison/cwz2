package cn.fooltech.fool_ops.domain.basedata.service;


import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.basedata.entity.CustomerAddress;
import cn.fooltech.fool_ops.domain.basedata.entity.CustomerSupplierView;
import cn.fooltech.fool_ops.domain.basedata.repository.CustomerAddressRepository;
import cn.fooltech.fool_ops.domain.basedata.repository.CustomerSupplierViewRepository;
import cn.fooltech.fool_ops.domain.basedata.vo.CustomerAddressVo;
import cn.fooltech.fool_ops.domain.freight.entity.FreightAddress;
import cn.fooltech.fool_ops.domain.freight.repository.FreightAddressRepository;
import cn.fooltech.fool_ops.domain.freight.service.FreightAddressService;
import cn.fooltech.fool_ops.domain.freight.vo.FreightAddressVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.VoFactory;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 客户/供应商默认收/发货地址
 */
@Service
public class CustomerAddressService extends BaseService<CustomerAddress, CustomerAddressVo, String> {


    @Autowired
    private CustomerAddressRepository repository;

    @Autowired
    private CustomerSupplierViewRepository csvRepo;

    @Autowired
    private FreightAddressService addressService;

    @Override
    public CustomerAddressVo getVo(CustomerAddress entity) {
        CustomerAddressVo vo = VoFactory.createValue(CustomerAddressVo.class, entity);
        CustomerSupplierView csv = csvRepo.findOne(entity.getCustomerId());
        if(csv!=null){
            vo.setCustomerName(csv.getName());
            vo.setCustomerType(csv.getType());
        }
        if (!Strings.isNullOrEmpty(entity.getAddressId())) {
            FreightAddress address = addressService.get(entity.getAddressId());
            vo.setAddressName(address.getName());
        }

        return vo;
    }

    @Override
    public CrudRepository<CustomerAddress, String> getRepository() {
        return repository;
    }

    /**
     * 根据客户或供应商ID查询列表
     *
     * @param csvId
     * @return
     */
    public List<CustomerAddressVo> query(String csvId) {
        List<CustomerAddress> datas = repository.findByCsvId(csvId);
        return getVos(datas);
    }

    /**
     * 保存或新增
     *
     * @return
     */
    public RequestResult save(CustomerAddressVo vo) {

        if (Strings.isNullOrEmpty(vo.getCustomerId())) {
            return buildFailRequestResult("往来单位必填");
        }
        CustomerAddress entity = null;
        if (Strings.isNullOrEmpty(vo.getId())) {
            entity = new CustomerAddress();
            entity.setOrgId(SecurityUtil.getCurrentOrgId());
            entity.setAccId(SecurityUtil.getFiscalAccountId());
            entity.setCreatorId(SecurityUtil.getCurrentUserId());
            entity.setCreateTime(new Date());
            entity.setUpdateTime(new Date());

            Long count = repository.countByCsvId(vo.getCustomerId());
            Long count1=repository.countByAddvId(vo.getAddressId(),CustomerAddress.DEFAULT);
            if (count != null && count == 0 && count1==0 && count1!=null) {
                entity.setFdefault(CustomerAddress.DEFAULT);
            } else {
                entity.setFdefault(CustomerAddress.NOT_DEFAULT);
            }
        } else {
            entity = repository.findOne(vo.getId());
        }

        entity.setCustomerId(vo.getCustomerId());
        entity.setAddressId(vo.getAddressId());
        entity.setDescribe(vo.getDescribe());

        repository.save(entity);

        return buildSuccessRequestResult(getVo(entity));
    }

    /**
     * 设置默认
     *
     * @param id
     * @return
     */
    @Transactional
    public RequestResult updateDefault(String id) {
        CustomerAddress entity = repository.findOne(id);
        String csvId = entity.getCustomerId();
        String addId=entity.getAddressId();
        String message="";
        List<CustomerAddress> defaults=repository.findDefaultByaddId(addId, CustomerAddress.DEFAULT);
        //遍历查找他自己是否在默认地址名单上
        for(CustomerAddress csv:defaults){
        	if(csv.getCustomerId()==csvId){
        		return buildFailRequestResult("此地址已是默认状态");
        	}
        }
        if(null==defaults||defaults.size()==0){
            List<CustomerAddress> datas = repository.findDefaultByCsvId(csvId, CustomerAddress.DEFAULT);
            for (CustomerAddress ca : datas) {
                ca.setFdefault(CustomerAddress.NOT_DEFAULT);
                repository.save(ca);
            }
            entity.setFdefault(CustomerAddress.DEFAULT);
            repository.save(entity);
            return buildSuccessRequestResult(getVo(entity));
        }else{
        	message="此地址已有:";
        	int nullCustomer=0;//标记有多少个垃圾数据
        	for(CustomerAddress ca:defaults){
        		CustomerSupplierView csv = csvRepo.findOne(ca.getCustomerId());
        		if(csv==null){
        			nullCustomer=nullCustomer+1;
        		}else{
        			message=message+csv.getName();
        		}
          	 }
        	if(nullCustomer==defaults.size()){
                List<CustomerAddress> datas = repository.findDefaultByCsvId(csvId, CustomerAddress.DEFAULT);
                for (CustomerAddress ca : datas) {
                    ca.setFdefault(CustomerAddress.NOT_DEFAULT);
                    repository.save(ca);
                }
                entity.setFdefault(CustomerAddress.DEFAULT);
                repository.save(entity);
                return buildSuccessRequestResult(getVo(entity));
        	}else{
            	message=message+"设为默认值";
            	return buildFailRequestResult(message);
        	}
        }
/*        List<CustomerAddress> datas = repository.findDefaultByCsvId(csvId, CustomerAddress.DEFAULT);
        for (CustomerAddress ca : datas) {
            ca.setFdefault(CustomerAddress.NOT_DEFAULT);
            repository.save(ca);
        }
        entity.setFdefault(CustomerAddress.DEFAULT);
        repository.save(entity);
        return buildSuccessRequestResult();*/
	
    }

    /**
     * 根据客户或者供应商ID获取默认地址
     * @return
     */
    public FreightAddressVo getFreightAddressByCsvId(String csvId){
        CustomerAddress cAddress = repository.findTopByDefaultAndCsvId(csvId);
        if(cAddress==null)return null;
        FreightAddress address = addressService.get(cAddress.getAddressId());
        return addressService.getVo(address);
    }
	/**
	 * 根据发货地查询记录
	 * @param fid	发货地id
	 * @return
	 */
	public Long queryByDeliveryPlaceCount(String fid){
		return repository.queryByDeliveryPlaceCount(fid);
	};
}
