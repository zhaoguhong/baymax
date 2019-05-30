package com.zhaoguhong.baymax.security.service;

import com.zhaoguhong.baymax.common.SystemConstants;
import com.zhaoguhong.baymax.user.entity.User;
import com.zhaoguhong.baymax.user.entity.repository.UserRepository;
import java.security.Principal;
import lombok.extern.slf4j.Slf4j;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.cas.userdetails.AbstractCasAssertionUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * cas service
 */
@Service
@Slf4j
public final class CasUserDetailsServiceImpl extends AbstractCasAssertionUserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  protected UserDetails loadUserDetails(final Assertion assertion) {
    // 拿到单点返回的用户信息，去数据查询，然后返回，此处模拟一个用户，所以直接写死
    Principal principal = assertion.getPrincipal();
    String principalName = assertion.getPrincipal().getName();
    log.info("用户：{} 单点登录成功",principalName);
    User user = userRepository.findByUsernameAndIsDeleted("test", SystemConstants.UN_DELETED);

    if (user == null) {
      throw new UsernameNotFoundException("username Not Found");
    }
    return user;
  }
}
