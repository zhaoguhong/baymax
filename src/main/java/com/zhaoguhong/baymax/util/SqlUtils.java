package com.zhaoguhong.baymax.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @author guhong
 * @date 2019/6/12
 */
public class SqlUtils {

  /**
   * 拼接查询数量sql，使用包装方式以支持子查询
   */
  public static String getCountSql(String sql) {
    // 去除末尾的 order by 子句以优化性能
    int orderByIndex = StringUtils.indexOfIgnoreCase(sql, "order by");
    if (orderByIndex > StringUtils.INDEX_NOT_FOUND) {
      sql = sql.substring(0, orderByIndex);
    }
    return "select count(*) from (" + sql + ") t";
  }

  /**
   * 生成 Hibernate 6 可执行的 JPQL 计数语句。
   * JPQL 不支持 SQL 风格的派生表，因此需要保留 from 子句并替换 select 子句。
   */
  public static String getCountHql(String hql) {
    int orderByIndex = StringUtils.indexOfIgnoreCase(hql, " order by ");
    if (orderByIndex > StringUtils.INDEX_NOT_FOUND) {
      hql = hql.substring(0, orderByIndex);
    }
    String trimmedHql = hql.trim();
    if (StringUtils.startsWithIgnoreCase(trimmedHql, "from ")) {
      return "select count(*) " + trimmedHql;
    }
    int fromIndex = StringUtils.indexOfIgnoreCase(trimmedHql, " from ");
    if (fromIndex == StringUtils.INDEX_NOT_FOUND) {
      throw new IllegalArgumentException("无法从 HQL 中解析 from 子句: " + hql);
    }
    return "select count(*)" + trimmedHql.substring(fromIndex);
  }

  /**
   * 获取mysql分页sql
   */
  public static String getMysqlPageSql(String sql, int pageNo, int pageSize) {
    int startNo = (pageNo - 1) * pageSize;
    return sql + " limit " + startNo + "," + pageSize;
  }

}
