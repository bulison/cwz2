package cn.fooltech.fool_ops.domain.basedata.repository;

import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.basedata.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.math.BigDecimal;
import java.util.List;


/**
 * 单位Repository
 *
 * @author xjh
 */
public interface UnitRepository extends JpaRepository<Unit, String> {

    /**
     * 获取机构下的叶子节点
     * @param groupId
     * @return
     */
    @Query("select u from Unit u where u.parent.fid=?1 and u.flag=" + Unit.FLAG_UNIT + " and u.enable!=" + Unit.UNABLE)
    public List<Unit> getChilds(String groupId);

    /**
     * 获取机构下的叶子节点
     * @param groupId
     * @param unitName
     * @return
     */
    @Query("select u from Unit u where u.parent.fid=?1 and (u.name like %?2% or u.code like %?2%) and u.flag=" + Unit.FLAG_UNIT + " and u.enable!=" + Unit.UNABLE)
    public List<Unit> getChildsOfMatch(String groupId, String unitName);

    /**
     * 获取机构下的叶子节点
     *
     * @param orgId
     * @return
     */
    @Query("select u from Unit u where u.org.fid=?1 and u.flag=" + Unit.FLAG_UNIT + " and u.enable=" + Unit.UNABLE)
    public List<Unit> getLeafUnit(String orgId);

    /**
     * 获取某个机构下，最顶级的单位
     *
     * @param orgId
     * @return
     */
    @Query("select u from Unit u where u.org.fid=?1 and (u.parent.fid is null or u.parent.fid='')")
    public Unit getRootUnit(String orgId);

    /**
     * 判断某个机构里，换算单位在组内是否重复
     *
     * @param orgId   组织机构ID
     * @param groupId 组ID
     * @param scale   换算单位
     * @return
     */
    @Query("select count(*) from Unit u where u.org.fid=?1 and u.parent.fid=?2 and u.scale=?3")
    public Long countByGroupScale(String orgId, String groupId, BigDecimal scale);

    /**
     * 判断某个机构里，换算单位在组内是否重复
     *
     * @param orgId     组织机构ID
     * @param groupId   组ID
     * @param scale     换算单位
     * @param excludeId 排除实体的ID
     * @return
     */
    @Query("select count(*) from Unit u where u.org.fid=?1 and u.parent.fid=?2 and u.scale=?3 and u.fid!=?4")
    public Long countByGroupScale(String orgId, String groupId, BigDecimal scale, String excludeId);

    /**
     * 判断某个机构里，单位名称在组内是否重复
     *
     * @param orgId   组织机构ID
     * @param groupId 组ID
     * @param name    单位名称
     * @return
     */
    @Query("select count(*) from Unit u where u.org.fid=?1 and u.parent.fid=?2 and u.name=?3")
    public Long countByGroupName(String orgId, String groupId, String name);

    /**
     * 判断某个机构里，单位名称在组内是否重复
     *
     * @param orgId     组织机构ID
     * @param groupId   组ID
     * @param name      单位名称
     * @param excludeId 排除实体的ID
     * @return
     */
    @Query("select count(*) from Unit u where u.org.fid=?1 and u.parent.fid=?2 and u.name=?3 and u.fid!=?4")
    public Long countByGroupName(String orgId, String groupId, String name, String excludeId);

    /**
     * 判断某个机构里，单位编号在组内是否重复
     *
     * @param orgId   组织机构ID
     * @param groupId 组ID
     * @param code    单位编号
     * @return
     */
    @Query("select count(*) from Unit u where u.org.fid=?1 and u.parent.fid=?2 and u.code=?3")
    public Long countByGroupCode(String orgId, String groupId, String code);

    /**
     * 判断某个机构里，单位编号在组内是否重复
     *
     * @param orgId     组织机构ID
     * @param groupId   组ID
     * @param code      单位编号
     * @param excludeId 排除实体的ID
     * @return
     */
    @Query("select count(*) from Unit u where u.org.fid=?1 and u.parent.fid=?2 and u.code=?3 and u.fid!=?4")
    public Long countByGroupCode(String orgId, String groupId, String code, String excludeId);

    /**
     * 根据父节点ID统计子节点个数
     *
     * @param parentId
     * @return
     */
    @Query("select count(*) from Unit u where u.parent.fid=?1")
    public Long countByParentId(String parentId);


    /**
     * 根据父ID获取同样父ID的换算关系=1的记账单位
     *
     * @param parentId
     * @return
     */
    @Query("select u from Unit u where u.parent.fid=?1 and u.scale=1")
    @QueryHints({@QueryHint(name = Constants.LIMIT, value = "1")})
    public Unit findTopByGroupId(String groupId);

    /**
     * 根据编号查找
     *
     * @param orgId
     * @param code
     * @return
     */
    @Query("select u from Unit u where u.org.fid=?1 and u.code=?2")
    @QueryHints({@QueryHint(name = Constants.LIMIT, value = "1")})
    public Unit findTopByCode(String orgId, String code);
    /**
     * 
     */
    @Query("select u from Unit u where u.org.fid=?1 and u.code=?2 and u.parent.fid=?3")
    @QueryHints({@QueryHint(name = Constants.LIMIT, value = "1")})
    public Unit findTopByCodeAndParent(String orgId, String code,String parentId);
    /**
     * 根据子节点的编号查找
     * @param orgId
     * @param code
     * @return
     */
    @Query("select u from Unit u where u.org.fid=?1 and u.code=?2 and u.flag=" + Unit.FLAG_UNIT)
    @QueryHints({@QueryHint(name = Constants.LIMIT, value = "1")})
    public Unit findTopByLeafCode(String orgId, String code);
    /**
     * 根据父ID和子编码查找
     */
    @Query("select u from Unit u where u.org.fid=?1 and u.code=?2 and u.parent.fid=?3")
    public Unit findSonByCode(String orgId, String code,String parent);
}
