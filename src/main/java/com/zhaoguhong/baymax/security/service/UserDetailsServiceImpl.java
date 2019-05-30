package com.zhaoguhong.baymax.security.service;

import com.zhaoguhong.baymax.common.SystemConstants;
import com.zhaoguhong.baymax.user.entity.User;
import com.zhaoguhong.baymax.user.entity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
