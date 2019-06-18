package com.zhaoguhong.baymax.swagger;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author guhong
 * @date 2019/6/18
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

  @Value("${swagger.enable:false}")
  private boolean swaggerEnable;

  //文档访问前缀
  public static final String ACCESS_PREFIX = "/swagger-resources/**,/swagger-ui.html**,/webjars/**,/v2/**";

  @Bean
  public Docket docket() {
    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(apiInfo())
        // 设置是否开启swagger,生产环境关闭
        .enable(swaggerEnable)
        .select()
        // 当前包路径
        .apis(RequestHandlerSelectors.basePackage("com.zhaoguhong.baymax"))
        .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
        .paths(PathSelectors.any())
        .build();
  }

  // 构建api文档的详细信息
  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        // 页面标题
        .title("接口文档")
        // 创建人
        .contact(new Contact("孤鸿", "https://github.com/zhaoguhong/baymax", ""))
        // 版本号
        .version("1.0")
        // 描述
        .description("大白的接口文档")
        .build();
  }
}
