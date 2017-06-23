package cn.fooltech.fool_ops.domain.basedata.repository;

import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.basedata.entity.GoodsBar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.List;

/**
 * 货品条码Repository
 *
 * @author xjh
 */
public interface GoodsBarRepository extends JpaRepository<GoodsBar, String> {

    /**
     * 根据条码查询
     *
     * @param orgId
     * @param barCode
     * @return
     */
    @Query("select count(*) from GoodsBar b where b.org.fid=?1 and b.barCode=?2")
    public Long countByBarCode(String orgId, String barCode);

    /**
     * 根据条码查询
     *
     * @param orgId
     * @param barCode
     * @return
     */
    @Query("select b from GoodsBar b where b.org.fid=?1 and b.barCode=?2")
    @QueryHints({@QueryHint(name = Constants.LIMIT, value = "1")})
    public GoodsBar findTopByBarCode(String orgId, String barCode);

    /**
     * 根据条码查询
     *
     * @param orgId
     * @param barCode
     * @param excludeId
     * @return
     */
    @Query("select count(*) from GoodsBar b where b.org.fid=?1 and b.barCode=?2 and b.fid!=?3")
    public Long countByBarCode(String orgId, String barCode, String excludeId);

    /**
     * 根据机构ID和货品id查询条码分页
     *
     * @param orgId
     * @param goodsId
     * @return
     */
    @Query("select b from GoodsBar b where b.org.fid=?1 and b.goods.fid=?2")
    public Page<GoodsBar> findPageByGoodsId(String orgId, String goodsId, Pageable page);

    /**
     * 根据货品id查询条码
     *
     * @param goodsId
     * @return
     */
    @Query("select b from GoodsBar b where b.goods.fid=?1")
    public List<GoodsBar> findByGoodsId(String goodsId);

    /**
     * 根据货品ID，属性ID，单位ID查询
     *
     * @param goodsId
     * @param specId
     * @param unitId
     * @return
     */
    @Query("select b from GoodsBar b where b.goods.fid=?1 and b.spec.fid=?2 and b.unit.fid=?3")
    @QueryHints({@QueryHint(name = Constants.LIMIT, value = "1")})
    public GoodsBar findTopByGoods(String goodsId, String specId, String unitId);
    
    @Query("delete from GoodsBar a where goods.fid=?1")
    @Modifying
    public void deleteByGoodsId(String goodsId);
}
