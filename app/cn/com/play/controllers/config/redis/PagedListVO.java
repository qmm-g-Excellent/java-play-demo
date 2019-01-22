package cn.com.play.controllers.config.redis;

import io.ebean.PagedList;
import io.ebean.SqlRow;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PagedListVO<T> {

    public List<T> list = new ArrayList<>();
    public int totalPageCount;
    public int pageIndex;
    public int pageSize;

    public PagedListVO() {
    }

    public PagedListVO(PagedList<T> pagedList) {
        list = pagedList.getList();
        totalPageCount = pagedList.getTotalPageCount();
        pageIndex = pagedList.getPageIndex();
        pageSize = pagedList.getPageSize();
    }

    public PagedListVO(int pageIndex, int pageSize, int totalPageCount, List<T> list) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.totalPageCount = totalPageCount;
        this.list = list;
    }

    public <R> PagedListVO(int pageIndex, int pageSize, int totalPageCount, List<R> list, Function<R, T> mapper) {
        this(pageIndex, pageSize, totalPageCount, list.stream().map(mapper).collect(Collectors.toList()));
    }

    public <R> PagedListVO(PagedList<R> pagedList, Function<R, T> mapper) {
        this.pageIndex = pagedList.getPageIndex();
        this.pageSize = pagedList.getPageSize();
        this.totalPageCount = pagedList.getTotalPageCount();
        this.list = pagedList.getList().stream().map(mapper).collect(Collectors.toList());
    }

    public PagedListVO(SqlPagedList pagedList, Function<SqlRow, T> mapper) {
        this.pageIndex = pagedList.getPageIndex();
        this.pageSize = pagedList.getPageSize();
        this.totalPageCount = pagedList.getTotalPageCount();
        this.list = pagedList.getList().stream().map(mapper).collect(Collectors.toList());
    }
}
