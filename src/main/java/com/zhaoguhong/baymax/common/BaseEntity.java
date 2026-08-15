package com.zhaoguhong.baymax.common;

import java.io.Serializable;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

/**
 * 实体类基础类
 */
@Data
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

  /**
   * 主键id
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private Long id;
  /**
   * 创建人
   */
  private Long createdBy;
  /**
   * 创建时间
   */
  private LocalDateTime createdTime;
  /**
   * 更新人
   */
  private Long updatedBy;
  /**
   * 更新时间
   */
  private LocalDateTime updatedTime;
  /**
   * 是否删除
   */
  private Integer isDeleted;

}
