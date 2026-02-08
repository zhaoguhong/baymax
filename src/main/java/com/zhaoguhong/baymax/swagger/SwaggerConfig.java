package com.zhaoguhong.baymax.swagger;

import io.swagger.annotations.ApiOperation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
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

  /**
   * 解决 Spring Boot 2.6+ 与 Springfox 的兼容性问题
   * Springfox 假设 Spring MVC 使用 AntPathMatcher，但 Spring Boot 2.6+ 默认使用 PathPatternParser
   * Actuator 端点始终使用 PathPatternParser，会导致 Springfox 的 NullPointerException
   * 此 BeanPostProcessor 过滤掉使用 PathPatternParser 的 HandlerMapping，避免冲突
   */
  @Bean
  public static BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
    return new BeanPostProcessor() {
      @Override
      public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider) {
          customizeSpringfoxHandlerMappings(getHandlerMappings(bean));
        }
        return bean;
      }

      private <T extends RequestMappingInfoHandlerMapping> void customizeSpringfoxHandlerMappings(
          List<T> mappings) {
        List<T> copy = mappings.stream()
            .filter(mapping -> mapping.getPatternParser() == null)
            .collect(Collectors.toList());
        mappings.clear();
        mappings.addAll(copy);
      }

      @SuppressWarnings("unchecked")
      private List<RequestMappingInfoHandlerMapping> getHandlerMappings(Object bean) {
        try {
          Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");
          field.setAccessible(true);
          return (List<RequestMappingInfoHandlerMapping>) field.get(bean);
        } catch (IllegalArgumentException | IllegalAccessException e) {
          throw new IllegalStateException(e);
        }
      }
    };
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
