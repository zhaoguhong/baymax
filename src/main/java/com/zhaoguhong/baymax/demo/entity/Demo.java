package com.zhaoguhong.baymax.demo.entity;

import com.zhaoguhong.baymax.common.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Data;

/**
 * @author guhong
 * @date 2019/5/5
 */
@Data
@Entity
public class Demo extends BaseEntity{
  private String userName;
  private Integer age;
}
