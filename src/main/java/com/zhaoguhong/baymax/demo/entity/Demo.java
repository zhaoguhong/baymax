package com.zhaoguhong.baymax.demo.entity;

import com.zhaoguhong.baymax.common.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author guhong
 * @date 2019/5/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Schema(description = "示例")
public class Demo extends BaseEntity{
  @NotBlank(message = "用户名不允许为空")
  @Schema(description = "用户名")
  private String userName;
  @NotBlank
  @Schema(description = "标题")
  private String title;
  @NotNull
  @Schema(description = "年龄")
  private Integer age;
}
