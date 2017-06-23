package cn.fooltech.fool_ops.utils.tree;

import java.util.List;

public interface TreeCallback<E, V> {

    public List<E> getChildren(E e);

    public V getDataVo(E e);
}
