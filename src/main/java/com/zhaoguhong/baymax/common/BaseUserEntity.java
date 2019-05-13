package com.zhaoguhong.baymax.common;

import javax.persistence.MappedSuperclass;

/**
 * 包含用户实体类基础类
 */
@MappedSuperclass
public abstract class BaseUserEntity extends BaseEntity {

  abstract public Long getUserId();

  abstract public void setUserId(Long userId);

  @Override
  public String toString() {
    return "BaseUserEntity{} " + super.toString();
  }

}
