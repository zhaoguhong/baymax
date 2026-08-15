# Baymax

Baymax 是一个基于 Spring Boot 的 Web 项目示例，覆盖 Web、统一响应、异常处理、数据校验、日志、Spring Security、CAS、JDBC、JPA、MyBatis、TK Mapper、PageHelper、Redis、MongoDB、邮件和 OpenAPI 等常用能力。

项目已升级到 JDK 17 与 Spring Boot 3，源码使用 Jakarta API。

## 技术栈

| 组件 | 版本 |
| --- | --- |
| JDK | 17 |
| Spring Boot | 3.5.16 |
| Spring Framework | 6.2.19 |
| Spring Security | 6.5.11 |
| Hibernate ORM | 6.6.53.Final |
| MyBatis | 3.5.19 |
| TK Mapper Starter | 5.0.2 |
| PageHelper Core | 6.1.1 |
| Druid Boot 3 Starter | 1.2.28 |
| springdoc-openapi | 2.9.0 |
| MySQL Connector/J | 9.7.0 |
| Maven Wrapper | 3.9.11 |

Spring Boot 管理的依赖不在 `pom.xml` 中重复指定版本；上表中的 Spring Framework、Spring Security、Hibernate 和 MySQL Connector/J 版本由 Spring Boot 3.5.16 的依赖管理提供。

## 环境要求

- JDK 17 或更高版本。Maven Enforcer 会在版本不满足时终止构建。
- MySQL 8.x，开发环境默认连接 `localhost:3306/baymax`。
- Redis，开发环境默认连接 `localhost:6379`。
- MongoDB，开发环境默认连接 `mongodb://localhost:27017/test`。
- Docker 可选，可用于运行 MongoDB 等本地依赖。

项目自带 Maven Wrapper，不要求全局安装 Maven：

```bash
./mvnw --version
```

如果机器上安装了多个 JDK，请先确认输出中的 Java 版本为 17 或更高版本。

## 本地运行

未指定 profile 时默认启用 `dev`。

开发环境默认配置为：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/baymax?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull
spring.datasource.username=root
spring.datasource.password=123456
spring.data.redis.host=localhost
spring.data.redis.port=6379
spring.data.mongodb.uri=mongodb://localhost:27017/test
```

先准备名为 `baymax` 的 MySQL 数据库及项目所需表结构，然后运行：

```bash
./mvnw spring-boot:run
```

本地开发配置的 `spring.jpa.hibernate.ddl-auto` 为 `update`。如果只希望校验现有表结构而不执行 DDL，可运行：

```bash
./mvnw spring-boot:run \
  -Dspring-boot.run.arguments=--spring.jpa.hibernate.ddl-auto=validate
```

### MongoDB

可以使用带持久化数据卷的 Docker 容器：

```bash
docker run -d \
  --name baymax-mongodb \
  -p 27017:27017 \
  -v baymax-mongodb-data:/data/db \
  mongo:8
```

容器已经存在时直接启动即可：

```bash
docker start baymax-mongodb
```

## 配置文件

| 文件 | 用途 |
| --- | --- |
| `application.properties` | 应用名、缓存、Actuator、Security、MyBatis 等公共配置 |
| `application-dev.properties` | 本地开发默认值，可被环境变量覆盖 |
| `application-prod.properties` | 生产配置，仅从环境变量读取敏感信息，不提供默认密码 |

生产环境通过 `SPRING_PROFILES_ACTIVE=prod` 启用，至少需要提供数据库、Redis 和 MongoDB 连接信息：

```bash
export SPRING_PROFILES_ACTIVE=prod
export DB_URL='jdbc:mysql://db-host:3306/baymax?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull'
export DB_USERNAME='baymax'
export DB_PASSWORD='change-me'
export DRUID_USERNAME='admin'
export DRUID_PASSWORD='change-me'
export REDIS_HOST='redis-host'
export REDIS_PORT='6379'
export REDIS_PASSWORD='change-me'
export MONGODB_URI='mongodb://mongo-host:27017/baymax'

./mvnw spring-boot:run
```

生产环境默认使用 `spring.jpa.hibernate.ddl-auto=validate`，不会自动修改表结构。可以通过 `JPA_DDL_AUTO` 显式覆盖，但生产环境建议使用 Flyway 或 Liquibase 管理数据库变更。

邮件相关环境变量为 `MAIL_HOST`、`MAIL_USERNAME`、`MAIL_PASSWORD` 和 `MAIL_SENDER`。OpenAPI 文档在生产环境默认关闭，可通过 `SWAGGER_ENABLED=true` 开启。

## 数据访问

项目同时保留多种数据访问方式，便于按场景选用：

- Spring JDBC 与自定义 `JdbcDao`。
- Spring Data JPA 与自定义基础 Repository。
- MyBatis XML、自定义分页拦截器和 TK Mapper。
- RedisTemplate、Spring Cache 与 Redis 分布式锁。
- MongoTemplate。

### TK Mapper 与 PageHelper

TK Mapper 使用官方项目 [abel533/Mapper](https://github.com/abel533/Mapper) 的 Spring Boot 3 Starter 5.0.2。

PageHelper 使用官方项目 [pagehelper-org/Mybatis-PageHelper](https://github.com/pagehelper-org/Mybatis-PageHelper) 的 Core 6.1.1，而不是 PageHelper Starter。PageHelper Starter 会引入官方 MyBatis Starter，与 TK Mapper Starter 的 MyBatis 自动配置产生顺序冲突，因此项目在 `MybatisConfig` 中显式注册 `PageInterceptor` 和自定义 `MyPageInterceptor`。

TK Mapper 5 通过 `@RegisterMapper` 发现官方基础 Mapper 接口。项目的 `MyMapper` 包含 Java `default` 方法，因此不能再配置到 `mapper.mappers`，否则这些方法会被错误地当作 SQL Provider 方法解析。

## API 文档与监控

本地启动后可访问：

- OpenAPI JSON：<http://localhost:8080/v3/api-docs>
- Swagger UI：<http://localhost:8080/swagger-ui/index.html>
- 健康检查：<http://localhost:8080/actuator/health>
- Druid 控制台：<http://localhost:8080/druid/>
- 示例接口：<http://localhost:8080/test/successResult>

生产环境默认只允许匿名访问健康检查，并默认关闭 OpenAPI。Actuator 仅暴露 `health`、`info` 和 `metrics`。

## Security 与 CAS

项目使用 Spring Security 6 的 `SecurityFilterChain` 配置表单登录。静态资源和配置的匿名路径使用 `permitAll`，其余请求需要认证。

CAS 默认关闭。启用时设置：

```bash
export CAS_ENABLED=true
export CAS_SERVER_URL='https://cas.example.com/cas'
export CAS_CLIENT_URL='https://app.example.com'
export CAS_LOGIN_URL='https://cas.example.com/cas/login'
export CAS_SERVER_LOGOUT_URL='https://cas.example.com/cas/logout'
export CAS_CLIENT_CAS_URL='https://app.example.com/login/cas'
```

CAS 相关 Bean 仅在 `cas.enable=true` 时加载。

## 测试

全量测试会连接本地 MySQL、Redis 和 MongoDB：

```bash
./mvnw test
```

MySQL 写入型测试使用事务回滚；Redis 测试使用随机临时键并在结束后清理。MongoDB 测试会保留写入的测试数据，便于人工检查。

邮件测试默认跳过，只有显式提供真实邮箱凭据并设置 `RUN_MAIL_TESTS=true` 时才会执行：

```bash
RUN_MAIL_TESTS=true \
MAIL_HOST=smtp.example.com \
MAIL_USERNAME=user@example.com \
MAIL_PASSWORD=secret \
MAIL_SENDER=user@example.com \
./mvnw test -Dtest=EmailTest
```

## 构建

```bash
./mvnw clean package
java -jar target/baymax-0.0.1-SNAPSHOT.jar
```

生产部署时不要把真实密码写入配置文件或提交到版本库，应通过环境变量或专用密钥管理服务注入。
