package cn.com.play.controllers.config.redis;

import io.ebean.Ebean;
import io.ebean.SqlQuery;
import io.ebean.SqlRow;

import java.util.List;
import java.util.Map;


public class SqlPagedList {
    private int pageSize;
    private int pageIndex;
    private List<SqlRow> list;
    private int totalPageCount;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public List<SqlRow> getList() {
        return list;
    }

    public void setList(List<SqlRow> list) {
        this.list = list;
    }

    public int getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;
    }

    public static SqlPagedList findSqlPagedList(String sql, Map<String, Object> params, int pageIndex, int pageSize) {
        SqlPagedList sqlPagedList = new SqlPagedList();

        sqlPagedList.setPageIndex(pageIndex);
        sqlPagedList.setPageSize(pageSize);

        List<SqlRow> list = getPagedList(sql, params, pageIndex, pageSize);
        sqlPagedList.setList(list);

        int totalPageCount = getTotalPageCount(sql, params, pageSize);
        sqlPagedList.setTotalPageCount(totalPageCount);

        return sqlPagedList;
    }

    private static int getTotalPageCount(String sql, Map<String, Object> params, int pageSize) {
        String summarySql = "select count(1) as totalCount from (" + sql + ") as query";
        SqlQuery summaryQuery = Ebean.createSqlQuery(summarySql);
        setConditionParams(params, summaryQuery);

        SqlRow sqlRow = summaryQuery.findOne();
        int totalCount = sqlRow.getInteger("totalCount");
        return totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
    }

    private static List<SqlRow> getPagedList(String sql, Map<String, Object> params, int pageIndex, int pageSize) {
        String pageSql = sql + " limit :pageSize offset :pageIndex";
        SqlQuery query = Ebean.createSqlQuery(pageSql);
        setConditionParams(params, query);
        query.setParameter("pageSize", pageSize);
        query.setParameter("pageIndex", pageIndex * pageSize);

        return query.findList();
    }

    private static void setConditionParams(Map<String, Object> params, SqlQuery query) {
        params.keySet().stream().forEach(key -> query.setParameter(key, params.get(key)));
    }
}
