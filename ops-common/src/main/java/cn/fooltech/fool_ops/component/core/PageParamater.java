package cn.fooltech.fool_ops.component.core;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>分页的请求参数类</p>
 *
 * @author xjh
 * @version 1.0
 * @date 2015年5月6日
 */
@ApiModel("分页的请求参数类")
public class PageParamater {

    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int DEFAULT_PAGE_NO = 1;

    @ApiModelProperty(value = "页码，默认1")
    protected Integer page = DEFAULT_PAGE_NO;

    @ApiModelProperty(value = "一页大小，默认10")
    protected Integer rows = DEFAULT_PAGE_SIZE;

    @ApiModelProperty(value = "需要排序的属性", hidden = true)
    protected String sort;

    @ApiModelProperty(value = "排序方式：asc，desc", hidden = true)
    protected String order;

    @ApiModelProperty(hidden = true)
    protected Integer start = 0;


    public PageParamater() {
        super();
        this.start = 0;
    }

    public PageParamater(Integer page, Integer rows, Integer start) {
        super();
        this.page = page;
        this.rows = rows;
        this.start = (page - 1 < 0 ? 0 : page - 1) * rows;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "PageParamater [page=" + page + ", rows=" + rows + ", sort="
                + sort + ", order=" + order + ", start=" + start + "]";
    }
}
