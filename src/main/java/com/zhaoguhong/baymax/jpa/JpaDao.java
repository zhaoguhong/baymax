package com.zhaoguhong.baymax.jpa;

import com.zhaoguhong.baymax.common.BaseEntity;
import com.zhaoguhong.baymax.common.Page;
import com.zhaoguhong.baymax.util.SqlUtils;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;

/**
 * JpaDao DAO
 */
@Repository
public class JpaDao {

  @PersistenceContext
  EntityManager entityManager;

  /**
   * 根据id获取对象
   */
  public <T> T get(Class<T> clazz, Serializable id) {
    return entityManager.find(clazz, id);
  }

  @SuppressWarnings("unchecked")
  public <T> List<T> find(String hql, Object... parameters) {
    return createQuery(hql, parameters).getResultList();
  }

  @SuppressWarnings("unchecked")
  public <T> List<T> find(String hql, Map<String, ?> parameters) {
    return createQuery(hql, parameters).getResultList();
  }

  public <T> List<T> findAll(Class<T> cls) {
    String hql = "from " + cls.getName();
    if (BaseEntity.class.isAssignableFrom(cls)) {
      hql += " where isDeleted = 0 order by createdTime desc";
    }
    return find(hql);
  }


  public <T> Page<T> find(Page<T> page, String hql, Object... parameters) {
    Query query = createQuery(hql, parameters);
    long totalCount = findUnique(SqlUtils.getCountSql(hql), parameters);
    page.setTotalCount((int) totalCount);
    query.setFirstResult(page.getFirstEntityIndex());
    query.setMaxResults(page.getPageSize());
    page.setEntityList(query.getResultList());
    return page;
  }

  public <T> Page<T> find(Page<T> page, String hql, Map<String, ?> parameters) {
    Query query = createQuery(hql, parameters);
    long totalCount = findUnique(SqlUtils.getCountSql(hql), parameters);
    page.setTotalCount((int) totalCount);
    query.setFirstResult(page.getFirstEntityIndex());
    query.setMaxResults(page.getPageSize());
    page.setEntityList(query.getResultList());
    return page;
  }

  public <T> T findUnique(String hql, Object... parameters) {
    return (T) createQuery(hql, parameters).getSingleResult();
  }

  public <T> T findUnique(String hql, Map<String, ?> parameters) {
    return (T) createQuery(hql, parameters).getSingleResult();
  }

  public Query createQuery(String hql, Object... parameters) {
    hql = hql.replace("?", "?variable");
    for (int i = 1; hql.contains("?variable"); i++) {
      hql = hql.replaceFirst("\\?variable", "?" + i);
    }
    Query query = entityManager.createQuery(hql);
    if (parameters != null) {
      for (int i = 0; i < parameters.length; i++) {
        query.setParameter(i + 1, parameters[i]);
      }
    }
    return query;
  }

  public Query createQuery(String queryString, Map<String, ?> parameters) {
    Query query = entityManager.createQuery(queryString);
    if (parameters != null) {
      parameters.forEach((key, value) -> {
        query.setParameter(key, value);
      });
    }
    return query;
  }

}
