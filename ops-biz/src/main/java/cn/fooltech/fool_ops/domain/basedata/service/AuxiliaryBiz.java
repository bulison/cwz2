package cn.fooltech.fool_ops.domain.basedata.service;

/**
 * 辅助属性保存后会调的接口
 * Created by xjh on 2016/12/5.
 */
public interface AuxiliaryBiz {

    /**
     * 保存辅助属性后要做的事情
     */
    public void saveAfter(String attrId);

    /**
     * 是否需要做
     *
     * @param typeCode
     * @return
     */
    public boolean isSupport(String typeCode);
}
