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
   * 获取mysql分页sql
   */
  public static String getMysqlPageSql(String sql, int pageNo, int pageSize) {
    int startNo = (pageNo - 1) * pageSize;
    return sql + " limit " + startNo + "," + pageSize;
  }

}
