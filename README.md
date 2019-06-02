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
+ [spring security](#security)
+ [单点登录](#sso)

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

```xml
    <dependency>
      <groupId>com.alibaba</groupId>
      <artifactId>druid-spring-boot-starter</artifactId>
      <version>1.1.10</version>
    </dependency>
```

设置用户名密码

```
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

```java
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

```java
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

```java
// 占位符 例如：from Demo where id =?
find(String sql, Object... args)
// 命名参数
find(String sql, Map<String, ?> paramMap)
```

## <span id="mybatis">mybatis</span>
maven 依赖

```xml
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
需要注意，xml的namespace必须是mapper类的全限定名，这样才可以建立dao接口与xml的关系

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

```xml
    <dependency>
      <groupId>tk.mybatis</groupId>
      <artifactId>mapper-spring-boot-starter</artifactId>
      <version>1.2.4</version>
    </dependency>
```
常用配置如下

```
# 通用mapper 多个接口时用逗号隔开
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

#### 分页

[pagehelper](https://github.com/pagehelper)是一个很好用的mybatis的分页插件

maven 依赖

```xml
    <dependency>
      <groupId>com.github.pagehelper</groupId>
      <artifactId>pagehelper-spring-boot-starter</artifactId>
      <version>1.2.3</version>
    </dependency>
```
常用配置如下

```
#pagehelper
#指定数据库类型
pagehelper.helperDialect=mysql
#分页合理化参数
pagehelper.reasonable=true
```
使用分页

```java   
    PageHelper.startPage(1, 5);
    List<Demo> demos = demoMapper.selectAll();
```
pagehelper 还有好多玩法，可以参考[这里](https://github.com/pagehelper/Mybatis-PageHelper/blob/master/wikis/zh/HowToUse.md) 

#### 自定义分页
pagehelper 虽然好用，但项目中有自己的分页对象，所以单独写一个拦截器，把他们整合到一起，为保证拦截器的顺序，在`spring ApplicationListener` 添加自定义的拦截器

```java
@Configuration
// 设置mapper扫描的包
@tk.mybatis.spring.annotation.MapperScan(basePackages = "com.zhaoguhong.baymax.*.dao")
@Slf4j
public class MybatisConfig implements ApplicationListener<ContextRefreshedEvent> {

  @Autowired
  private List<SqlSessionFactory> sqlSessionFactoryList;

  /**
   * 添加自定义的分页插件，pageHelper 的分页插件PageInterceptor是用@PostConstruct添加的，自定义的应该在其后面添加
   * 真正执行时顺序是反过来，先执行MyPageInterceptor，再执行 PageInterceptor
   */
  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    MyPageInterceptor interceptor = new MyPageInterceptor();
    for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
      log.info(sqlSessionFactory.getConfiguration().getInterceptors().toString());
      sqlSessionFactory.getConfiguration().addInterceptor(interceptor);
      log.info("注册自定义分页插件成功");
    }
  }

}
```
在使用时只需要传入自定义的分页对象即可

```java
    Page<Demo> page = new Page<>(1, 10);
    demos = demoMapper.getDemos(page);
```

## <span id="security">spring security</span>

安全模块是项目中必不可少的一环，常用的安全框架有`shiro`和`spring security`，shiro相对轻量级，使用非常灵活，`spring security`相对功能更完善，而且可以和spring 无缝衔接。这里选取`spring security`做为安全框架

maven 依赖

```xml
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
```
继承WebSecurityConfigurerAdapter类就可以进行配置了

```java
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private SecurityProperties securityProperties;

  @Autowired
  private UserDetailsService userDetailsService;

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http
        .authorizeRequests()
        // 设置可以匿名访问的url
        .antMatchers(securityProperties.getAnonymousArray()).permitAll()
        // 其它所有请求都要认证
        .anyRequest().authenticated()
        .and()
        .formLogin()
        // 自定义登录页
        .loginPage(securityProperties.getLoginPage())
        // 自定义登录请求路径
        .loginProcessingUrl(securityProperties.getLoginProcessingUrl())
        .permitAll()
        .and()
        .logout()
        .permitAll();

    // 禁用CSRF
    http.csrf().disable();
  }


  @Override
  public void configure(WebSecurity web) throws Exception {
    String[] ignoringArray = securityProperties.getIgnoringArray();
    // 忽略的资源，直接跳过spring security权限校验
    if (ArrayUtils.isNotEmpty(ignoringArray)) {
      web.ignoring().antMatchers(ignoringArray);
    }
  }

  /**
   *
   * 声明密码加密方式
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth)
      throws Exception {
    auth.userDetailsService(userDetailsService)
        // 配置密码加密方式，也可以不指定，默认就是BCryptPasswordEncoder
        .passwordEncoder(passwordEncoder());
  }


}
```

实现`UserDetailsService`接口，定义自己的`UserDetailsService`

```java
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsernameAndIsDeleted(username, SystemConstants.UN_DELETED);

    if (user == null) {
      throw new UsernameNotFoundException("username Not Found");
    }
    return user;
  }

}
```
配置项

```
#匿名访问的url,多个用逗号分隔
security.anonymous=/test
#忽略的资源,直接跳过spring security权限校验,一般是用做静态资源，多个用逗号分隔
security.ignoring=/static/**,/images/**
#自定义登录页面
security.loginPage=/login.html
#自定义登录请求路径
security.loginProcessingUrl=/login
```


## <span id="sso">单点登录</span>
单点登录系统（SSO，single sign-on）指的的，多个系统，共用一套用户体系，只要登录其中一个系统，访问其他系统不需要重新登录

#### CAS
CAS(Central Authentication Service)是耶鲁大学的一个开源项目，是比较流行的单独登录解决方案
在CAS中，只负责登录的系统被称为服务端，其它所有系统被称为客户端

##### 登录流程
1. 用户访问客户端，客户端判断是否登录，如果没有登录，重定向到服务端去登录
2. 服务端登录成功，带着ticket重定向到客户端
3. 客户端拿着ticket发送请求到服务端换取用户信息，获取到后就表示登录成功

##### 登出流程

跳转到sso认证中心进行统一登出，cas 会通知所有客户端进行登出

#### spring security 整合 cas

maven 依赖

```xml
    <dependency>
      <groupId>org.springframework.security</groupId>
      <artifactId>spring-security-cas</artifactId>
    </dependency>
```
spring security 对 cas 做了很好的封装,在使用的过程中，只需要定义好对应的登录fifter和登出fifter即可，整合cas的代码我写在了`WebSecurityConfig`类中

相关属性配置

```
#是否开启单点登录
cas.enable = true
#服务端地址
cas.serverUrl=
#客户端地址
cas.clientUrl=
#登录地址
cas.loginUrl=${cas.serverUrl}/login
#服务端登出地址
cas.serverLogoutUrl=${cas.serverUrl}/logout
#单点登录成功回调地址
cas.clientCasUrl=${cas.clientUrl}/login/cas
```

