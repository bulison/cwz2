package cn.fooltech.fool_ops.domain.basedata.repository;

import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.basedata.entity.BillRule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.List;

public interface BillRuleRepository extends JpaRepository<BillRule, String> {

    /**
     * 根据日期格式选择单据规则
     *
     * @param dateFormats
     * @return
     */
    public List<BillRule> findByDateIn(List<String> dateFormats);

    /**
     * 根据机构ID和单据类型查询单据规则（限制结果集1个）
     *
     * @param orgId    机构ID
     * @param billType 单据类型
     * @return
     */
    @Query("select b from BillRule b where b.org.fid=?1 and b.billType=?2")
    @QueryHints({@QueryHint(name = Constants.LIMIT, value = "1")})
    public BillRule findTopByBillType(String orgId, int billType);



    /**
     * 根据机构ID查找分页
     *
     * @param orgId
     * @param page
     * @return
     */
    @Query("select b from BillRule b where b.org.fid=?1")
    public Page<BillRule> findPageByOrgId(String orgId, Pageable page);

    /**
     * 根据机构ID和单据类型查询单据规则
     *
     * @param orgId     机构ID
     * @param billTypes 单据类型s
     * @return
     */
    @Query("select b from BillRule b where b.org.fid=?1 and b.billType in ?2 order by b.billType asc")
    public List<BillRule> findByBillTypes(String orgId, List<Integer> types);

    /**
     * 根据机构ID查询单据规则
     *
     * @param orgId 机构ID
     * @return
     */
    @Query("select b from BillRule b where b.org.fid=?1 order by b.billType asc")
    public List<BillRule> findByBillTypes(String orgId);

}
