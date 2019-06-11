package com.zhaoguhong.baymax.exception.entity;


import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 异常日志
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "exception_log")
public class ExceptionLog implements Serializable {
  /**
   * 主键id
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  /**
   *  异常发生时间
   */
  private Date createdTime;

  /**
   * 异常信息
   */
  private String exception;

  public ExceptionLog(Date createdTime, String exception) {
    this.createdTime = createdTime;
    this.exception = exception;
  }

}
