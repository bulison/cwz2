package cn.fooltech.fool_ops.domain.base.entity;

/**
 * @description:基础的PO类
 * @author: 戴铁坚
 */
public abstract class BasePO {

    public static final int LEFA_CHILD = 1;//叶子节点
    public static final int LEFA_PARENT = 0;//父节点

    public BasePO() {
    }

    /**
     * 抽象方法 取得主键。
     * 如果子类取主键的方法不一样。 就要重写这个方法。
     */
    public abstract String getFid();

    /**
     * 抽象方法 设置主键。
     * 如果子类设置主键的方法不一样。 就要重写这个方法。
     */
    public abstract void setFid(String fid);
}
