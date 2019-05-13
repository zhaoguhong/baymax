package com.zhaoguhong.baymax.jpa;

import com.zhaoguhong.baymax.common.BaseEntity;
import java.io.Serializable;
import javax.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.util.Assert;

public class BaseRepositoryFactoryBean<R extends JpaRepository<T, I>, T, I extends Serializable>
    extends JpaRepositoryFactoryBean<R, T, I> {

  public BaseRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
    super(repositoryInterface);
  }

  @Override
  protected RepositoryFactorySupport createRepositoryFactory(EntityManager em) {
    return new MyRepositoryFactory(em);
  }

  private static class MyRepositoryFactory<T extends BaseEntity, I extends Serializable>
      extends JpaRepositoryFactory {

    private final EntityManager em;

    public MyRepositoryFactory(EntityManager em) {
      super(em);
      this.em = em;
    }

    // 设置实现类是BaseRepositoryImpl
    @Override
    protected JpaRepositoryImplementation<?, ?> getTargetRepository(
        RepositoryInformation information, EntityManager entityManager) {
      JpaEntityInformation<?, Serializable> entityInformation =
          this.getEntityInformation(information.getDomainType());
      Object repository = this.getTargetRepositoryViaReflection(information,
          new Object[] {entityInformation, entityManager});
      Assert.isInstanceOf(BaseRepositoryImpl.class, repository);
      return (JpaRepositoryImplementation) repository;
    }

    // 设置自定义实现类class
    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
      return BaseRepositoryImpl.class;
    }
  }
}
