package com.zhaoguhong.baymax.jpa;

import com.zhaoguhong.baymax.common.BaseEntity;
import com.zhaoguhong.baymax.common.BaseUserEntity;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 扩展jpa接口
 *
 * @author zhaojianhe
 */
@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, Long> {

  /**
   * 新增，会对创建时间，创建人自动赋值
   */
  void saveEntity(T entity);

  /**
   * 更新，会对更新时间，更新人自动赋值
   */
  void updateEntity(T entity);

  /**
   * 逻辑删除
   */
  void deleteEntity(T entity);

  /**
   * 批量保存
   */
  void saveEntites(Collection<T> entitys);

  /**
   * 批量更新
   */
  void updateEntites(Collection<T> entitys);

  /**
   * 批量逻辑删除
   */
  void deleteEntites(Collection<T> entitys);

  /**
   * 根据id获取实体，会过滤掉逻辑删除的
   */
  T getById(Long id);

  /**
   * 根据id获取实体，会限制userId是当前用户，并且过滤掉逻辑删除的
   */
  <T extends BaseUserEntity> T findByIdForLoginUser(Long id);

  /**
   * 根据id和用户id获取实体，并且过滤掉逻辑删除的
   */
  <T extends BaseUserEntity> T findByIdAndUserId(Long id, Long userId);

  /**
   * 获取全部实体，会过滤掉逻辑删除的
   */
  List<T> getAll();

}
