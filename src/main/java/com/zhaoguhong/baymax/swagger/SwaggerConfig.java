package com.zhaoguhong.baymax.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI 文档配置。
 *
 * @author guhong
 * @date 2019/6/18
 */
@Configuration
@ConditionalOnProperty(name = "springdoc.api-docs.enabled", havingValue = "true", matchIfMissing = true)
public class SwaggerConfig {

  public static final String ACCESS_PREFIX =
      "/v3/api-docs/**,/swagger-ui/**,/swagger-ui.html";

  @Bean
  public OpenAPI openApi() {
    return new OpenAPI()
        .info(new Info()
            .title("接口文档")
            .contact(new Contact()
                .name("孤鸿")
                .url("https://github.com/zhaoguhong/baymax"))
            .version("1.0")
            .description("大白的接口文档"));
  }
}
