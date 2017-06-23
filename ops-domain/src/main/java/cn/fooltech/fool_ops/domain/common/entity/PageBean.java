package cn.fooltech.fool_ops.domain.common.entity;

import java.io.Serializable;

/**
 * 分页Bean，该Bean有相关分页属性及相关分页设置方法， pageSize 每页显示行数，默认取Define.pageCount currentPage
 * 当前页，设置该参数翻页
 *
 * @author tjr
 */
public class PageBean implements Serializable {
    private static final long serialVersionUID = -8171787376815711509L;
    /***************************************************************************
     * 总行数
     */
    private int totalRows;
    /***************************************************************************
     * 每页显示的行数
     */
    private int pageSize = 10;
    /***************************************************************************
     * 当前页
     */
    private int currentPage;
    /***************************************************************************
     * 总页数
     */
    private int totalPages;
    /***************************************************************************
     * 当前页在数据库中的起始行
     */
    private int startRow = -1;

    /**
     * 是否存在上一页
     */
    private boolean existPrev = false;

    /**
     * 是否存在下一页
     */
    private boolean existNext = false;

    /***
     * 空构造函数<br/>
     */
    public PageBean() {
    }

    /***
     * 带总行数入参的构造函数<br/>
     *
     * @param totalRows
     *            总行数<br/>
     */
    public PageBean(int totalRows) {
        this.totalRows = totalRows;
    }

    /***
     * 带总行数,每页显示多小行入参的构造函数<br/>
     *
     * @param totalRows
     *            总行数<br/>
     * @param pageSize
     *            每页显示多小行<br/>
     * @param currentPage
     *            当前页<br/>
     */
    public PageBean(int totalRows, int pageSize, int currentPage) {
        this.totalRows = totalRows;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        pageSet();
    }

    /***
     * 设置分页属性，把传进来的参数加以计算， 得出最终每页要显示多小行、总页数等， 该方法为每个分页操作最后一步必须调用
     */
    public void pageSet() {
        if (pageSize > totalRows)
            pageSize = totalRows;
        // 设置每页显示多小行
        if (0 == pageSize)
            pageSize = 10;
        // 设置总页数
        totalPages = totalRows / pageSize;
        if (totalRows % pageSize != 0)
            totalPages += 1;
        // 设置当前页
        if (0 == currentPage)
            currentPage = 1;
        if (currentPage > totalPages)
            currentPage = totalPages;

        if (currentPage > 1) existPrev = true;

        // 设置开始行
        if (-1 == startRow)
            startRow = (currentPage - 1) * pageSize;
        if (startRow < 0) startRow = 0;
    }

    /***
     * 获得总行数<br/>
     *
     * @return
     */
    public int getTotalRows() {
        return totalRows;
    }

    /***
     * 设置总行数<br/>
     *
     * @param totalRows
     */
    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    /***
     * 获得每页显示行数<br/>
     *
     * @return
     */
    public int getPageSize() {
        return pageSize;
    }

    /***
     * 设置每页显示行数<br/>
     *
     * @param pageSize
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /***
     * 获得当前页<br/>
     *
     * @return
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /***
     * 设置当前页<br/>
     *
     * @param currentPage
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    /***
     * 获得总页数<br/>
     *
     * @return
     */
    public int getTotalPages() {
        return totalPages;
    }

    /***
     * 设置总页数<br/>
     *
     * @param totalPages
     */
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    /***
     * 获得起始页<br/>
     *
     * @return
     */
    public int getStartRow() {
        return startRow;
    }

    /***
     * 设置起始页<br/>
     *
     * @param startRow
     */
    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    /**
     * 是否存在下一页
     *
     * @return
     * @returnType boolean
     * @author tjr
     * @date 2014-5-22 上午9:08:35
     * @copyRight tjr
     */
    public boolean isExistNext() {
        return existNext;
    }

    /**
     * 是否存在下一页
     *
     * @param existNext
     * @returnType void
     * @author tjr
     * @date 2014-5-22 上午9:08:52
     * @copyRight tjr
     */
    public void setExistNext(boolean existNext) {
        this.existNext = existNext;
    }

    /**
     * 是否存在上一页
     *
     * @return
     * @returnType boolean
     * @author tjr
     * @date 2014-5-22 上午9:22:09
     * @copyRight tjr
     */
    public boolean isExistPrev() {
        return existPrev;
    }

    /**
     * 是否存在上一页
     *
     * @param existPrev
     * @returnType void
     * @author tjr
     * @date 2014-5-22 上午9:22:21
     * @copyRight tjr
     */
    public void setExistPrev(boolean existPrev) {
        this.existPrev = existPrev;
    }
}
