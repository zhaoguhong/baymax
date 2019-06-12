package com.zhaoguhong.baymax.jdbc;

import com.zhaoguhong.baymax.common.Page;
import com.zhaoguhong.baymax.util.SqlUtils;
import java.util.Collections;
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

  public Integer queryForInt(String sql, Map<String, ?> paramMap) {
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

  public Page<Map<String, Object>> find(Page<Map<String, Object>> page, String sql, Map<String, ?> parameters) {
    return find(page,sql,parameters,null);
  }

  /**
   * 分页查询
   * @param page  分页对象
   * @param sql   sql语句
   * @param parameters  参数
   * @param mapper  映射mapper
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public <T> Page<T> find(Page<T> page, String sql, Map<String, ?> parameters, RowMapper<?> mapper) {
    String querySql = SqlUtils.getMysqlPageSql(sql, page.getPageNo(), page.getPageSize());
    if(parameters == null){
      parameters = Collections.emptyMap();
    }
    if (mapper == null) {
      page.setEntityList(((List) this.find(querySql, parameters)));
    } else {
      page.setEntityList((List) this.find(querySql, parameters, mapper));
    }

    String countSql = SqlUtils.getCountSql(sql);
    page.setTotalCount(this.queryForInt(countSql, parameters));
    return page;
  }

  /**
   * 分页
   * @param page   分页对象
   * @param sql    sql语句
   * @param mapper 映射mapper
   * @param args   参数
   * @param <T>    返回值泛型类型
   * @return
   */
  public <T> Page<T> find(Page<T> page, String sql, RowMapper<T> mapper, Object... args) {
    String querySql = SqlUtils.getMysqlPageSql(sql, page.getPageNo(), page.getPageSize());
    if (mapper == null) {
      page.setEntityList(((List) this.find(querySql, args)));
    } else {
      page.setEntityList((List) this.find(querySql, args, mapper));
    }
    String countSql = SqlUtils.getCountSql(sql);
    page.setTotalCount(this.queryForInt(countSql, args));
    return page;
  }

}
