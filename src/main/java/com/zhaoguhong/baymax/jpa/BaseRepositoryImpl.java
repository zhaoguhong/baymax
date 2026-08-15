package com.zhaoguhong.baymax.jpa;


import com.zhaoguhong.baymax.common.BaseEntity;
import com.zhaoguhong.baymax.common.ContextHolder;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

public class BaseRepositoryImpl<T extends BaseEntity> extends SimpleJpaRepository<T, Long>
    implements BaseRepository<T> {

  private final EntityManager entityManager;

  public BaseRepositoryImpl(JpaEntityInformation<T, Long> entityInformation,
      EntityManager entityManager) {
    super(entityInformation, entityManager);
    this.entityManager = entityManager;
  }

  @Override
  @Transactional
  public void saveEntity(T entity) {
    entity.setCreatedTime(LocalDateTime.now());
    entity.setCreatedBy(ContextHolder.getLoginUserId());
    entity.setIsDeleted(0);
    save(entity);
  }

  @Override
  @Transactional
  public void updateEntity(T entity) {
    entity.setUpdatedTime(LocalDateTime.now());
    entity.setUpdatedBy(ContextHolder.getLoginUserId());
    save(entity);
  }

  @Override
  @Transactional
  public void deleteEntity(T entity) {
    entity.setUpdatedTime(LocalDateTime.now());
    entity.setUpdatedBy(ContextHolder.getLoginUserId());
    entity.setIsDeleted(1);
    save(entity);
  }

  @Override
  @Transactional
  public void saveEntities(Collection<T> entities) {
    entities.forEach(entity -> saveEntity(entity));
  }

  @Override
  @Transactional
  public void updateEntities(Collection<T> entities) {
    entities.forEach(entity -> updateEntity(entity));
  }

  @Override
  @Transactional
  public void deleteEntities(Collection<T> entities) {
    entities.forEach(entity -> deleteEntity(entity));
  }

  @Override
  public T findActiveById(Long id) {
    T entity = findById(id).orElse(null);
    if (entity == null || Integer.valueOf(1).equals(entity.getIsDeleted())) {
      return null;
    }
    return entity;
  }

  @Override
  public T findByIdAndUserId(Long id, Long userId) {
    String jpql = "from " + getDomainClass().getName() + " where isDeleted = 0 and id =:id and userId =:userId ";
    return entityManager.createQuery(jpql, getDomainClass())
        .setParameter("id", id)
        .setParameter("userId", userId).getSingleResult();
  }

  @Override
  public T findByIdForLoginUser(Long id) {
    return findByIdAndUserId(id, ContextHolder.getRequiredLoginUserId());
  }

  @Override
  public List<T> getAll() {
    String jpql = "from " + getDomainClass().getName() + " where isDeleted = 0";
    return entityManager.createQuery(jpql, getDomainClass()).getResultList();
  }
}
