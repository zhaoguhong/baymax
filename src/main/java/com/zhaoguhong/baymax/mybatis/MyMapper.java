package com.zhaoguhong.baymax.mybatis;

import com.zhaoguhong.baymax.common.BaseEntity;
import com.zhaoguhong.baymax.common.ContextHolder;
import java.time.LocalDateTime;
import java.util.Collection;
import tk.mybatis.mapper.common.Mapper;

/**
 * 定义自己的通用mapper
 */
public interface MyMapper<T extends BaseEntity> extends Mapper<T> {

  default void saveEntity(T entity) {
    entity.setCreatedTime(LocalDateTime.now());
    entity.setCreatedBy(ContextHolder.getLoginUserId());
    entity.setIsDeleted(0);
    insert(entity);
  }

  default void updateEntity(T entity) {
    entity.setUpdatedTime(LocalDateTime.now());
    entity.setUpdatedBy(ContextHolder.getLoginUserId());
    updateByPrimaryKey(entity);
  }

  default void deleteEntity(T entity) {
    entity.setUpdatedTime(LocalDateTime.now());
    entity.setUpdatedBy(ContextHolder.getLoginUserId());
    entity.setIsDeleted(1);
    updateByPrimaryKey(entity);
  }

  default void saveEntities(Collection<T> entities) {
    entities.forEach(entity -> saveEntity(entity));
  }

  default void updateEntities(Collection<T> entities) {
    entities.forEach(entity -> updateEntity(entity));
  }

  default void deleteEntities(Collection<T> entities) {
    entities.forEach(entity -> deleteEntity(entity));
  }

  default T getById(Long id) {
    T entity = selectByPrimaryKey(id);
    if (entity == null || Integer.valueOf(1).equals(entity.getIsDeleted())) {
      return null;
    }
    return entity;
  }
}
