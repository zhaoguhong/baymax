package com.zhaoguhong.baymax.demo.entity;

import com.zhaoguhong.baymax.common.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("示例")
public class Demo extends BaseEntity{
  @NotBlank(message = "用户名不允许为空")
  @ApiModelProperty("用户名")
  private String userName;
  @NotBlank
  @ApiModelProperty("标题")
  private String title;
  @NotNull
  @ApiModelProperty("年龄")
  private Integer age;
}
