package com.zhaoguhong.baymax.mybatis;

import com.zhaoguhong.baymax.common.BaseEntity;
import com.zhaoguhong.baymax.common.ContextHolder;
import java.util.Collection;
import java.util.Date;
import tk.mybatis.mapper.common.Mapper;

/**
 * 定义自己的通用mapper
 */
public interface MyMapper<T extends BaseEntity> extends Mapper<T> {

  default void saveEntity(T entity) {
    entity.setCreatedTime(new Date());
    entity.setCreatedBy(ContextHolder.getLoginUserId());
    entity.setIsDeleted(0);
    insert(entity);
  }

  default void updateEntity(T entity) {
    entity.setUpdatedTime(new Date());
    entity.setUpdatedBy(ContextHolder.getLoginUserId());
    updateByPrimaryKey(entity);
  }

  default void deleteEntity(T entity) {
    entity.setUpdatedTime(new Date());
    entity.setUpdatedBy(ContextHolder.getLoginUserId());
    entity.setIsDeleted(0);
    updateByPrimaryKey(entity);
  }

  default void saveEntites(Collection<T> entitys) {
    entitys.forEach(entity -> saveEntity(entity));
  }

  default void updateEntites(Collection<T> entitys) {
    entitys.forEach(entity -> updateEntity(entity));
  }

  default void deleteEntites(Collection<T> entitys) {
    entitys.forEach(entity -> deleteEntity(entity));
  }

  default T getById(Long id) {
    T entity = selectByPrimaryKey(id);
    return entity.getIsDeleted() == 1 ? null : entity;
  }
}
