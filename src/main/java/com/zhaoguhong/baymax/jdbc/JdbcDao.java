package com.zhaoguhong.baymax.jdbc;

import com.zhaoguhong.baymax.common.Page;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;


/**
 * springjdbc 通用Dao
 * 
 * @author zhaoguhong
 * @date 2018年1月9日
 */
@Repository
@Slf4j
public class JdbcDao {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  public JdbcTemplate getJdbcTemplate() {
    return jdbcTemplate;
  }

  public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    return namedParameterJdbcTemplate;
  }

  public List<Map<String, Object>> find(String sql, Object... args) {
    return getJdbcTemplate().query(sql, args, new ColumnMapRowMapper());
  }

  public <T> List<T> find(String sql, Object[] args, RowMapper<T> rowMapper) {
    return getJdbcTemplate().query(sql, args, rowMapper);
  }

  public <T> List<T> find(String sql, RowMapper<T> rowMapper, Object... args) {
    return getJdbcTemplate().query(sql, rowMapper, args);
  }

  public List<Map<String, Object>> find(String sql, Map<String, ?> paramMap) {
    return getNamedParameterJdbcTemplate().query(sql, paramMap, new ColumnMapRowMapper());
  }

  public <T> List<T> find(String sql, Map<String, ?> paramMap, RowMapper<T> rowMapper) {
    return getNamedParameterJdbcTemplate().query(sql, paramMap, rowMapper);
  }

  public <T> T findUnique(String sql, Object... args) {
    List<Map<String, Object>> results = find(sql, args);
    return singleMapResult(DataAccessUtils.singleResult(results));
  }

  public <T> T findUnique(String sql, Map<String, ?> paramMap) {
    List<Map<String, Object>> results = find(sql, paramMap);
    return singleMapResult(DataAccessUtils.singleResult(results));
  }

  /**
   * springjdbc 原queryForMap方法，结果集校验用的是DataAccessUtils.requiredSingleResult(Collection<T>)方法
   * 如果没查询到会抛异常，此处用singleResult方法校验结果集，如果没有查询到，返回null
   */
  public Map<String, Object> queryForMap(String sql, Object... args) {
    List<Map<String, Object>> results = find(sql, args);
    return DataAccessUtils.singleResult(results);
  }

  public Map<String, Object> queryForMap(String sql, Map<String, ?> paramMap) {
    List<Map<String, Object>> results = find(sql, paramMap);
    return DataAccessUtils.singleResult(results);
  }

  public int update(String sql, Object... args) {
    return getJdbcTemplate().update(sql, args);
  }

  public int update(String sql, Map<String, ?> paramMap) {
    return getNamedParameterJdbcTemplate().update(sql, paramMap);
  }
  
  public Integer queryForInt(String sql, Object... args) {
    Integer result = getJdbcTemplate().queryForObject(sql, args, Integer.class);
    return result;
  }

  public Integer queryForInt(String sql, Map<String, Object> paramMap) {
    Integer result = getNamedParameterJdbcTemplate().queryForObject(sql, paramMap, Integer.class);
    return result;
  }

  public Long queryForLong(String sql, Object... args) {
    Long result = getJdbcTemplate().queryForObject(sql, args, Long.class);
    return result;
  }

  public Long queryForLong(String sql, Map<String, Object> paramMap) {
    Long result = getNamedParameterJdbcTemplate().queryForObject(sql, paramMap, Long.class);
    return result;
  }
  
  /**
   * 校验map是否只包含一组数据，并返回value
   */
  @SuppressWarnings("unchecked")
  private <T> T singleMapResult(Map<String, Object> result) {
    if (result == null) {
      return null;
    }
    if (result.size() != 1) {
      log.error("返回结果集必须是唯一对象");
      throw new IncorrectResultSizeDataAccessException(1, result.size());
    }
    return (T) result.values().iterator().next();
  }
  
  @SuppressWarnings({"unchecked", "rawtypes"})
  public void pagingQuery(Page<?> page, String sql, RowMapper<?> mapper, Map<String, Object> parameters) {
    String querySql = this.getPaginationSql(sql, page.getPageNo(), page.getPageSize());
    String countSql = "select count(*) from (" + sql + ") sub_table_alias_";
    if (parameters != null) {
      if (mapper == null) {
        page.setEntityList(((List) namedParameterJdbcTemplate.queryForList(querySql, parameters)));
      } else {
        page.setEntityList((List) namedParameterJdbcTemplate.query(querySql, parameters, mapper));
      }
      page.setTotalCount(namedParameterJdbcTemplate.queryForObject(countSql, parameters, Integer.class));
    } else {
      if (mapper == null) {
        page.setEntityList((List) jdbcTemplate.queryForList(querySql));
      } else {
        page.setEntityList((List) jdbcTemplate.query(querySql, mapper));
      }
      page.setTotalCount(jdbcTemplate.queryForObject(countSql, Integer.class));
    }
  }
  
  public String getPaginationSql(String sql, int pageNo, int pageSize) {
    int startNo = (pageNo - 1) * pageSize;
    return sql + " limit " + startNo + "," + pageSize;
  }

}
