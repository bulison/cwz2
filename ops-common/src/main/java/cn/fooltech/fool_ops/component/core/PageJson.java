package cn.fooltech.fool_ops.component.core;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * <p>EasyUI分页类</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015年5月6日
 */
public class PageJson {

    public static final int ERROR_CODE_SUCCESS = 0;
    public static final int ERROR_CODE_FAIL = 1;

    private Long total;

    /**
     * 数据集，返回前台使用fastjson转换成JSON Array
     */
    private Object rows;

    /**
     * 其他信息
     **/
    private Object other;

    /**
     * 数据获取成功标识
     */
    private int result = ERROR_CODE_SUCCESS;

    public PageJson() {
        super();
    }

    public PageJson(Long total, List rows) {
        super();
        this.total = total;
        this.rows = rows;
    }

    public PageJson(Page page) {
        super();
        if (null != page) {
            rows = page.getContent();
            total = page.getTotalElements();
        } else {
            total = 0L;
            rows = null;
        }
    }

    public PageJson(int result) {
        this.result = result;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Object getRows() {
        return rows;
    }

    public void setRows(Object rows) {
        this.rows = rows;
    }

    public Object getOther() {
        return other;
    }

    public void setOther(Object other) {
        this.other = other;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
