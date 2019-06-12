package com.zhaoguhong.baymax.common;

import javax.persistence.MappedSuperclass;
import lombok.ToString;

/**
 * 包含用户实体类基础类
 */
@MappedSuperclass
@ToString(callSuper = true)
public abstract class BaseUserEntity extends BaseEntity {

  abstract public Long getUserId();

  abstract public void setUserId(Long userId);

}
