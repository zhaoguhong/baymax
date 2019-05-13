# 基于springboot的web项目最佳实践


该项目是基于springboot的web项目脚手架，对一些常用的框架进行整合，并进行了简单的二次封装

项目名baymax取自动画片超能陆战队里面的大白，大白是一个医护充气机器人，希望这个项目你能像大白一样贴心，可以减少你的工作量

+ [web](#web)
+ [actuator应用监控](#actuator)
+ [lombok](#lombok)
+ [baseEntity](#baseEntity)
+ [数据库连接池](#datasource)
+ [spring jdbc](#jdbc)
+ [spring data jpa](#jpa)
+ [mybatis](#mybatis)

## <span id="web">web</span>

#### 示例
现在大部分项目都是前后端分离，因此推荐直接使用``@RestController``注解
需要注意的是，**强烈不建议直接用RequstMapping注解并且不指定方法类型的写法**，推荐使用GetMaping或者PostMaping之类的注解

```java
@SpringBootApplication
@RestController
public class BaymaxApplication {

  public static void main(String[] args) {
    SpringApplication.run(BaymaxApplication.class, args);
  }

  @GetMapping("/test")
  public String test() {
    return "hello baymax";
  }
}

```
测试一下

```java
  @Test
  public void testValidation() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/test"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.content().string("hello baymax"));
  }
```
## <span id="2">actuator应用监控</span>
actuator 是 spring 提供的应用监控功能，常用的配置项如下

```
# actuator端口 默认应用端口
management.server.port=8082
# 加载所有的端点 默认只加载 info,health
management.endpoints.web.exposure.include=*
# actuator路径前缀，默认 /actuator
management.endpoints.web.base-path=/actuatorw
```
## <span id="lombok">lombok</span>
lombok可以在编译期生成对应的java代码，使代码看起来更简洁，同时减少开发工作量

用lombok后的实体类

```java
@Data
public class Demo {
  private Long id;
  private String userName;
  private Integer age;
}
```
需要注意，`@Data` 包含 `@ToString、@Getter、@Setter、@EqualsAndHashCode、@RequiredArgsConstructor`，**RequiredArgsConstructor 并不是无参构造**，无参构造的注解是`NoArgsConstructor`

`RequiredArgsConstructor` 会生成 会生成一个包含常量（final），和标识了@NotNull的变量 的构造方法

## <span id="BaseEntity">BaseEntity</span>
把表中的基础字段抽离出来一个BaseEntity,所有的实体类都继承该类

```java
/**
 * 实体类基础类
 */
@Data
public abstract class BaseEntity implements Serializable {
  /**
   * 主键id
   */
  private Long id;
  /**
   * 创建人
   */
  private Long createdBy;
  /**
   * 创建时间
   */
  private Date createdTime;
  /**
   * 更新人
   */
  private Long updatedBy;
  /**
   * 更新时间
   */
  private Date updatedTime;
  /**
   * 是否删除
   */
  private Integer isDeleted;

}
```

## <span id="datasource">数据库连接池</span>

springboot1.X的数据库连接池是tomcat连接池，springboot2默认的数据库连接池由Tomcat换成HikariCP，HikariCP是一个高性能的JDBC连接池，号称最快的连接池

Druid是阿里巴巴数据库事业部出品，为监控而生的数据库连接池，这里选取Druid作为项目的数据库连接池

maven 依赖

```
    <dependency>
      <groupId>com.alibaba</groupId>
      <artifactId>druid-spring-boot-starter</artifactId>
      <version>1.1.10</version>
    </dependency>
```

设置用户名密码

```=
spring.datasource.druid.stat-view-servlet.login-username=admin
spring.datasource.druid.stat-view-servlet.login-password=123456
```

## <span id="jdbc">spring jdbc</span>
maven 依赖

```xml
     <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-jdbc</artifactId>
    </dependency>
```

spring对jdbc做了封装和抽象，最常用的是 `jdbcTemplate` 和 `NamedParameterJdbcTemplate`两个类，前者使用占位符，后者使用命名参数，我在`jdbcDao`做了一层简单的封装，提供统一的对外接口

`jdbcDao`主要方法如下：

```
// 占位符
find(String sql, Object... args)
// 占位符，手动指定映射mapper
find(String sql, Object[] args, RowMapper<T> rowMapper)
// 命名参数
find(String sql, Map<String, ?> paramMap)
// 命名参数，手动指定映射mapper
find(String sql, Map<String, ?> paramMap, RowMapper<T> rowMapper)
//springjdbc 原queryForMap方法,如果没查询到会抛异常，此处如果没有查询到，返回null
queryForMap(String sql, Object... args)
queryForMap(String sql, Map<String, ?> paramMap)
```
## <span id="jpa">spring data jpa</span>
maven 依赖

```xml
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
```
spring data jpa 使操作数据库变得更方便，只需要声明接口即可，我把通用的方法抽取了出来了，封装了一个BaseRepository，使用时，直接继承该接口即可


```java
public interface DemoRepository extends BaseRepository<Demo> {

}
```
`BaseRepository` 主要方法如下

```
// 新增，会对创建时间，创建人自动赋值
void saveEntity(T entity)
// 更新，会对更新时间，更新人自动赋值
void updateEntity(T entity)
// 逻辑删除
void deleteEntity(T entity)
//批量保存
void saveEntites(Collection<T> entitys)
// 批量更新
void updateEntites(Collection<T> entitys)
// 批量逻辑删除
void deleteEntites(Collection<T> entitys)
// 根据id获取实体，会过滤掉逻辑删除的
T getById(Long id)
```

如果想使用传统的sql形式，可以直接使用JpaDao,为了方便使用，我尽量使JpaDao和JdbcDao的接口保持统一

`JpaDao`主要方法如下

```
// 占位符 例如：from Demo where id =?
find(String sql, Object... args)
// 命名参数
find(String sql, Map<String, ?> paramMap)
```

## <span id="mybatis">mybatis</span>
maven 依赖

```java
    <dependency>
      <groupId>org.mybatis.spring.boot</groupId>
      <artifactId>mybatis-spring-boot-starter</artifactId>
      <version>1.3.2</version>
    </dependency>
```
常用配置如下

```
# 配置文件位置 classpath后面要加*，不然后面通配符不管用
mybatis.mapperLocations=classpath*:com/zhaoguhong/baymax/*/mapper/*Mapper.xml
# 开启驼峰命名自动映射
mybatis.configuration.map-underscore-to-camel-case=true
```
dao层直接用接口，简洁，方便

```java
@Mapper
public interface DemoMapper extends MyMapper<Demo>{
  /**
   * 注解方式
   */
  @Select("SELECT * FROM demo WHERE user_name = #{userName}")
  List<Demo> findByUserName(@Param("userName") String userName);
  /**
   * xml方式
   */
  List<Demo> getDemos();
}
```
需要注意，xml的namespace必须是mapper类的全限定名

```xml
<mapper namespace="com.zhaoguhong.baymax.demo.dao.DemoMapper">
  <select id="getDemos" resultType="com.zhaoguhong.baymax.demo.entity.Demo">
		select * from demo
	</select>
</mapper>
```
#### 通用mapper
mybatis 的单表增删改查写起来很啰嗦，[通用mapper](https://github.com/abel533/Mapper)很好的解决了这个问题

maven 依赖

```java
    <dependency>
      <groupId>tk.mybatis</groupId>
      <artifactId>mapper-spring-boot-starter</artifactId>
      <version>1.2.4</version>
    </dependency>
```
常用配置如下

```
#mapper 多个接口时用逗号隔开
mapper.mappers=com.zhaoguhong.baymax.mybatis.MyMapper
mapper.not-empty=false
mapper.identity=MYSQL
```
声明`mapper`需要加`Mapper`注解，还稍显麻烦，可以用扫描的方式

```java
@Configuration
@tk.mybatis.spring.annotation.MapperScan(basePackages = "com.zhaoguhong.baymax.*.dao")
public class MybatisConfig {
}
```
`MyMapper` 接口 中封装了通用的方法，和`jpa`的`BaseRepository`类似，这里不再赘述

