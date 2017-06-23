package cn.fooltech.fool_ops.domain.transport.service;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.base.service.BaseService;
import cn.fooltech.fool_ops.domain.transport.entity.TransportBilldetail;
import cn.fooltech.fool_ops.domain.transport.repository.TransportBilldetailRepository;
import cn.fooltech.fool_ops.domain.transport.vo.TransportBilldetailVo;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import cn.fooltech.fool_ops.utils.VoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 收/发货单从表
 */
@Service
public class TransportBilldetailService extends BaseService<TransportBilldetail, TransportBilldetailVo, String>{


    @Autowired
    private TransportBilldetailRepository repository;

	@Override
    public TransportBilldetailVo getVo(TransportBilldetail entity) {
        TransportBilldetailVo vo = VoFactory.createValue(TransportBilldetailVo.class, entity);
        return vo;
    }

    @Override
    public CrudRepository<TransportBilldetail, String> getRepository() {
        return repository;
    }

    /**
     * 新增并删除旧数据
     * @param vo
     * @param bill
     * @return
     */
    public RequestResult save(TransportBilldetailVo vo, WarehouseBill bill){


        TransportBilldetail entity = VoFactory.createValue(TransportBilldetail.class, vo);
        entity.setAccId(SecurityUtil.getFiscalAccountId());
        entity.setCreatorId(SecurityUtil.getCurrentUserId());
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        entity.setOrgId(SecurityUtil.getCurrentOrgId());

        entity.setTransportBill(bill);
        repository.save(entity);

        return buildSuccessRequestResult();
	}

    /**
     * 删除所有
     * @param details
     * @return
     */
	public RequestResult deleteAll(List<TransportBilldetail> details){
        repository.delete(details);
        return buildSuccessRequestResult();
    }

    /**
     * 根据主表ID查询明细
     * @param billId
     * @return
     */
    public List<TransportBilldetail> getTransportBillDetails(String billId){
        return repository.findByBillId(billId);
    }

    /**
     * 根据主表ID查询明细
     * @param billId
     * @return
     */
    public List<TransportBilldetailVo> getTransportBillDetailsVo(String billId){
        List<TransportBilldetail> entities = repository.findByBillId(billId);
        return getVos(entities);
    }

}
