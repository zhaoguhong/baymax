package com.zhaoguhong.baymax.common;

import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 项目上下文
 */
@Component
public class ContextHolder implements ApplicationContextAware {

  private static ApplicationContext applicationContext;

  // 模拟的用户标示
  public static final String MOCK_USER_FLAG = "MOCK_USER_FLAG";

  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  /**
   * 获取当前线程的request
   */
  public static HttpServletRequest getRequest() {
    return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
  }

  /**
   * 获取当前线程的response
   */
  public static HttpServletResponse getResponse() {
    return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
  }

  /**
   * 获取HttpSession
   */
  public static HttpSession getHttpSession() {
    return getRequest().getSession();
  }

  /**
   * 把值放入session
   */
  public static void setSessionAttribute(String key, Serializable entity) {
    getHttpSession().setAttribute(key, entity);
  }

  public static Object getSessionAttribute(String key) {
    return getHttpSession().getAttribute(key);
  }

  /**
   * 把值放入当前request
   */
  public static void setRequestAttribute(String key, Object entity) {
    getRequest().setAttribute(key, entity);
  }

  public static Object getRequestAttribute(String key) {
    return getRequest().getAttribute(key);
  }

  @Override
  public void setApplicationContext(ApplicationContext context) {
    Assert.notNull(context, "WebApplicationContext can not be null");
    applicationContext = context;
  }

  /**
   * 根据beanId获取bean
   */
  @SuppressWarnings("unchecked")
  public static <T> T getBean(String beanId) {
    return (T) applicationContext.getBean(beanId);
  }
//
//  /**
//   * 获取当前登录用户
//   */
//  public static User getLoginUser() {
//    User user = (User) getRequestAttribute(MOCK_USER_FLAG);
//    if (user != null) {
//      return user;
//    }
//    SecurityContext ctx = SecurityContextHolder.getContext();
//    Authentication auth = ctx.getAuthentication();
//    if (auth != null && auth.getPrincipal() instanceof EzyUser) {
//      return (User) auth.getPrincipal();
//    }
//    return null;
//  }
//
//  /**
//   * 根据用户id模拟用户登录，当前请求有效
//   */
//  public static void mockUserByUserId(Long id) {
//    if (getLoginUser() != null) {
//      throw new BusinessException("当前用户已登录，不能mock用户");
//    }
//    UserRepository userRepository = getBean("userRepository");
//    User user = userRepository.getById(id);
//    if (user == null) {
//      throw new BusinessException("用户不存在");
//    }
//    setRequestAttribute(MOCK_USER_FLAG, user);
//  }
//
//  /**
//   * 获取当前登录用户 id
//   */
//  public static Long getLoginUserId() {
//    User user = getLoginUser();
//    return user == null ? null : user.getId();
//  }
//
//  /**
//   * 获取当前登录用户,获取为空抛出异常
//   */
//  public static User getRequiredLoginUser() {
//    User user = getLoginUser();
//    if (user == null) {
//      throw new NoneLoginException("用户未登录");
//    }
//    return user;
//  }
//
//  /**
//   * 获取当前登录用户 id，获取为空抛出异常
//   */
//  public static Long getRequiredLoginUserId() {
//    return getRequiredLoginUser().getId();
//  }
//
//  /**
//   * 获取正常登录用户 id，试用用户抛出异常
//   * @return
//   * @author sr on 2019-04-11
//   */
//  public static Long getNormalLoginUserId() {
//    User user = getRequiredLoginUser();
//    if (Boolean.TRUE.equals(user.getTried())) { // 试用用户
//      throw new BusinessException("试用用户无权限操作");
//    }
//    return user.getId();
//  }

}
