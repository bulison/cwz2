package cn.fooltech.fool_ops.domain.common.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>分页查询结果基类</p>
 *
 * @author rqh
 * @version 1.0
 * @date 2014年12月12日
 */
public class ResultObject<T> {

    /**
     * 数据列表
     */
    private List<T> list;

    /**
     * 分页信息
     */
    private PageBean pageBean;

    /**
     * 其他数据
     */
    private Map<String, Object> data = new HashMap<String, Object>();

    public ResultObject(List<T> list, PageBean pageBean) {
        super();
        this.list = list;
        this.pageBean = pageBean;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public PageBean getPageBean() {
        return pageBean;
    }

    public void setPageBean(PageBean pageBean) {
        this.pageBean = pageBean;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

}
