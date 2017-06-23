package cn.fooltech.fool_ops.domain.basedata.repository;


import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.basedata.entity.CustomerAddress;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.List;


public interface CustomerAddressRepository extends FoolJpaRepository<CustomerAddress, String> {

    /**
     * 根据往来单位ID查询客户地址列表
     *
     * @param csvId
     * @return
     */
    @Query("select c from CustomerAddress c where c.customerId=?1")
    public List<CustomerAddress> findByCsvId(String csvId);

    /**
     * 根据往来单位ID统计客户地址列表
     *
     * @param csvId
     * @return
     */
    @Query("select count(*) from CustomerAddress c where c.customerId=?1")
    public Long countByCsvId(String csvId);
    /**
     * 根据地址ID统计客户地址列表
     */
    @Query("select count(*) from CustomerAddress c where c.addressId=?1 and c.fdefault=?2")
    public Long countByAddvId(String addId,Integer fdefault);
    /**
     * 根据往来单位ID查询客户地址列表
     *
     * @param csvId
     * @param fdefault
     * @return
     */
    @Query("select c from CustomerAddress c where c.customerId=?1 and fdefault=?2")
    public List<CustomerAddress> findDefaultByCsvId(String csvId, Integer fdefault);
    /**
     * 根据货运地址ID查询是否已经有客户设置此地址为默认地址
     * @param address
     * @param fdefault
     * @return
     */
    @Query("select c from CustomerAddress c where c.addressId=?1 and fdefault=?2")
    public List<CustomerAddress> findDefaultByaddId(String addId, Integer fdefault);
    /**
     * 根据往来单位ID查询客户地址
     *
     * @param csvId
     * @return
     */
    @Query("select c from CustomerAddress c where c.customerId=?1 and fdefault=1")
    @QueryHints({@QueryHint(name = Constants.LIMIT, value = "1")})
    public CustomerAddress findTopByDefaultAndCsvId(String csvId);
    
	/**
	 * 根据发货地查询记录
	 * @param fid	发货地id
	 * @return
	 */
	@Query("select count(a) from CustomerAddress a where addressId=?1")
	public Long queryByDeliveryPlaceCount(String fid);

    /**
     * 根据发货地查询默认地址
     * @param addressId	发货地id
     * @return
     */
    @Query("select a from CustomerAddress a where addressId=?1 and fdefault=1")
    @QueryHints({@QueryHint(name = Constants.LIMIT, value = "1")})
    public CustomerAddress queryDefaultByDeliveryPlace(String addressId);
}
