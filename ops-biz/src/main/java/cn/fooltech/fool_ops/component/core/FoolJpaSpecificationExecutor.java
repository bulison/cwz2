package cn.fooltech.fool_ops.component.core;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * //目前版本没有limit接口，用分页的方式查询
 *
 * @param <T>
 * @author xjh
 */
public interface FoolJpaSpecificationExecutor<T> extends JpaSpecificationExecutor<T> {

    /**
     * 查找所有，限制结果集大小不超过limit
     *
     * @param specification
     * @param limit
     * @return
     */
    public default List<T> findAll(Specification<T> specification, Integer limit) {
        int qlimit = Integer.MAX_VALUE;
        if (limit != null) {
            qlimit = limit;
        }

        PageRequest pageRequest = new PageRequest(0, qlimit);
        return findAll(specification, pageRequest).getContent();
    }

    /**
     * 查找所有，限制结果集大小不超过limit
     *
     * @param specification
     * @param sort
     * @param limit
     * @return
     */
    public default List<T> findAll(Specification<T> specification, Sort sort, Integer limit) {
        int qlimit = Integer.MAX_VALUE;
        if (limit != null) {
            qlimit = limit;
        }

        PageRequest pageRequest = new PageRequest(0, qlimit, sort);
        return findAll(specification, pageRequest).getContent();
    }

    /**
     * 查找第一个
     *
     * @param specification
     * @return
     */
    public default T findTop(Specification<T> specification) {
        PageRequest pageRequest = new PageRequest(0, 1);
        Page<T> page = findAll(specification, pageRequest);
        if (page.getTotalElements() > 0) {
            return page.getContent().get(0);
        } else {
            return null;
        }
    }

    /**
     * 查找第一个
     *
     * @param specification
     * @param sort
     * @return
     */
    public default T findTop(Specification<T> specification, Sort sort) {
        PageRequest pageRequest = new PageRequest(0, 1, sort);
        Page<T> page = findAll(specification, pageRequest);
        if (page.getTotalElements() > 0) {
            return page.getContent().get(0);
        } else {
            return null;
        }
    }

    /**
     * 判断是否存在
     *
     * @param specification
     * @return
     */
    public default boolean isExist(Specification<T> specification) {
        Long count = count(specification);
        if (count != null && count > 0) {
            return true;
        }
        return false;
    }
}
