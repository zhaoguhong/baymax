package com.zhaoguhong.baymax.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @author guhong
 * @date 2019/6/12
 */
public class SqlUtils {

  /**
   * 拼接查询数量sql
   */
  public static String getCountSql(String sql) {
    int fromIndex = StringUtils.indexOfIgnoreCase(sql, "from");
    if (fromIndex > StringUtils.INDEX_NOT_FOUND) {
      sql = sql.substring(fromIndex);
    }
    int orderByIndex = StringUtils.indexOfIgnoreCase(sql, "order by");
    if (orderByIndex > StringUtils.INDEX_NOT_FOUND) {
      sql = sql.substring(0, orderByIndex);
    }
    return "select count(*) " + sql;
  }

  /**
   * 获取mysql分页sql
   */
  public static String getMysqlPageSql(String sql, int pageNo, int pageSize) {
    int startNo = (pageNo - 1) * pageSize;
    return sql + " limit " + startNo + "," + pageSize;
  }

}
