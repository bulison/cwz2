package cn.fooltech.fool_ops.component.core;

import com.google.common.collect.Lists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据转换对象接口
 *
 * @param <E>
 * @param <V>
 * @author xjh
 */
public abstract interface DtoTransfer<E, V> {

    public abstract V getVo(E entity);

    //获取列表Vos
    default List<V> getVos(List<E> entitys) {
        List<V> list = Lists.newArrayList();
        if (entitys == null) return list;

        list = entitys
                .stream()
                .map(p->getVo(p))
                .collect(Collectors.toList());

        return list;
    }

    //获取分页Vos
    default Page<V> getPageVos(Page<E> es, Pageable pageable) {
        List<V> list = es.getContent()
                .stream()
                .map(p->getVo(p))
                .collect(Collectors.toList());

        Page<V> page = new PageImpl<V>(list, pageable, es.getTotalElements());
        return page;
    }
}
