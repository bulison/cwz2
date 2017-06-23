package cn.fooltech.fool_ops.domain.basedata.repository;

import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.basedata.entity.AuxiliaryAttrType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.List;

/**
 * 基础属性类别 持久层(地区，客户类别，征信级别，货品类别，在职状况，学历，仓库)
 *
 * @author cwz
 * @date 2016-10-24
 */
public interface AuxiliaryAttrTypeRepository extends JpaRepository<AuxiliaryAttrType, String>, JpaSpecificationExecutor<AuxiliaryAttrType> {

    /**
     * 根据辅助属性分类编码查找辅助属性分类
     */
    @Query("select u from AuxiliaryAttrType u where code=?1 and u.org.fid=?2 and (fiscalAccount.fid=?3 or fiscalAccount is null) order by fiscalAccount.fid desc")
    public List<AuxiliaryAttrType> findByCode(String code, String orgId, String accountId);

    @Query("select u from AuxiliaryAttrType u where org.fid=?1  and  (fiscalAccount.fid=?2 or fiscalAccount.fid is null) and code !='007' and code!='021'")
    public List<AuxiliaryAttrType> findAllEnable(String orgId, String fiscalAccountId);

    @Query("select a from AuxiliaryAttrType a where a.org.fid=?1 and  (fiscalAccount.fid=?2 or fiscalAccount.fid is null)")
    public Page<AuxiliaryAttrType> findPageByOrgIdOrderByCreateTimeDesc(String orgId, String accountId, Pageable pageable);

    /**
     * 根据辅助属性分类编码查找辅助属性分类
     */
    @Query("select u from AuxiliaryAttrType u where u.org.fid=?1 and code='007'")
    public List<AuxiliaryAttrType> findWarehouse(String orgId);
    /**
     * 根据辅助属性分类编码查找辅助属性分类(运输费用单位)
     */
    @Query("select u from AuxiliaryAttrType u where u.org.fid=?1 and code='021'")
    public List<AuxiliaryAttrType> findTransportFee(String orgId);
   /**
    * 修复旧数据,找出所有没有损耗地址(CODE=023)的数据
    * @param orgId
    * @return
    */
    @Query("select u from AuxiliaryAttrType u WHERE forg_id=?1 and fCode='023'")
    public List<AuxiliaryAttrType> queryNotLoss(String orgId);

    /**
     * 通过名字查询
     */
    @Query("select u from AuxiliaryAttrType u WHERE name=?1 and u.org.fid=?2")
    @QueryHints({@QueryHint(name= Constants.LIMIT,value="1")})
    public AuxiliaryAttrType getAttrTypeByName(String name, String orgId);
}
