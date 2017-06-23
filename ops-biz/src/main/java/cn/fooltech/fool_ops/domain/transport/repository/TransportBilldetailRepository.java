package cn.fooltech.fool_ops.domain.transport.repository;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.domain.transport.entity.TransportBilldetail;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 收/发货单从表
 */
public interface TransportBilldetailRepository extends FoolJpaRepository<TransportBilldetail, String>{

    /**
     * 根据主表ID查找
     * @param billId
     * @return
     */
    @Query("select t from TransportBilldetail t where t.transportBill.fid=?1")
    public List<TransportBilldetail> findByBillId(String billId);

}
