package com.zhaoguhong.baymax.demo.entity;

import com.zhaoguhong.baymax.common.BaseEntity;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author guhong
 * @date 2019/5/5
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Demo extends BaseEntity{
  @NotBlank(message = "用户名不允许为空")
  private String userName;
  @NotBlank
  private String title;
  @NotNull
  private Integer age;
}
