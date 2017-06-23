package cn.fooltech.fool_ops.domain.basedata.repository;

import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.basedata.entity.Bank;
import cn.fooltech.fool_ops.utils.PredicateUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public interface BankRepository extends JpaRepository<Bank, String>, JpaSpecificationExecutor<Bank> {

    /**
     * 根据银行账号查询
     *
     * @param orgId
     * @param account
     * @return
     */
    @Query("select b from Bank b where b.org.fid=?1 and b.account like %?2%")
    public List<Bank> findByAccount(String orgId, String account);

    /**
     * 根据银行账号查询数目
     *
     * @param orgId
     * @param account
     * @return
     */
    @Query("select count(*) from Bank b where b.org.fid=?1 and b.account=?2")
    public Long countByAccount(String orgId, String account);

    /**
     * 根据银行编号计算个数
     *
     * @param orgId
     * @param code
     * @return
     */
    @Query("select count(*) from Bank b where b.org.fid=?1 and b.code=?2")
    public Long countByCode(String orgId, String code);

    /**
     * 根据银行编号计算个数
     *
     * @param orgId
     * @param code
     * @return
     */
    @Query("select count(*) from Bank b where b.org.fid=?1 and b.code=?2 and b.fid!=?3")
    public Long countByCode(String orgId, String code, String exculdeId);

    /**
     * 根据银行编号计算个数
     *
     * @param orgId
     * @param code
     * @return
     */
    @Query("select b from Bank b where b.org.fid=?1 and b.code=?2")
    @QueryHints({@QueryHint(name = Constants.LIMIT, value = "1")})
    public Bank findTopByCode(String orgId, String code);

    /**
     * 根据名称计算个数
     *
     * @param orgId
     * @param name
     * @param fid
     * @return
     */
    @Query("select count(*) from Bank b where b.org.fid=?1 and b.name=?2 and fid!=?3")
    public Long countByName(String orgId, String name, String exculdeId);

    /**
     * 根据名称计算个数
     *
     * @param orgId
     * @param name
     * @param fid
     * @return
     */
    @Query("select count(*) from Bank b where b.org.fid=?1 and b.name=?2")
    public Long countByName(String orgId, String name);

    /**
     * 根据搜索关键字模糊匹配account code name
     *
     * @param orgId
     * @param pageRequest
     * @return
     */
    public default Page<Bank> findPageBySearchKey(String orgId, String searchKey, Pageable page) {
        return findAll(new Specification<Bank>() {

            @Override
            public Predicate toPredicate(Root<Bank> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicates = Lists.newArrayList();
                predicates.add(builder.equal(root.<String>get("org").get("fid"), orgId));
                if (StringUtils.isNotBlank(searchKey)) {
                    String key = PredicateUtils.getAnyLike(searchKey);
                    predicates.add(builder.or(
                            builder.like(root.<String>get("account"), key),
                            builder.like(root.<String>get("code"), key),
                            builder.like(root.<String>get("name"), key)
                    ));
                }
                Predicate predicate = builder.and(predicates.toArray(new Predicate[]{}));
                return predicate;
            }
        }, page);
    }

    /**
     * 根据机构ID查询
     *
     * @return
     */
    @Query("select b from Bank b where b.org.fid=?1")
    public List<Bank> findByOrgId(String orgId);

    /**
     * 根据机构ID和类型查询
     *
     * @return
     */
    @Query("select b from Bank b where b.org.fid=?1 and b.type=?2")
    public List<Bank> findByOrgIdAndType(String orgId, int type);
}
