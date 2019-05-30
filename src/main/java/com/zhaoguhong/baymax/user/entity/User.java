package com.zhaoguhong.baymax.user.entity;

import com.zhaoguhong.baymax.common.BaseEntity;
import java.util.Collection;
import java.util.HashSet;
import javax.persistence.Entity;
import javax.persistence.Transient;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 用户
 */
@Entity
@ToString(callSuper = true)
@Data
public class User extends BaseEntity implements UserDetails {

  /**
   * 用户名
   */
  private String username;
  /**
   * 密码
   */
  private String password;
  /**
   * 手机号码
   */
  private String mobile;
  /**
   * 帐号是否未过期
   */
  private boolean accountNonExpired;
  /**
   * 是否未锁定
   */
  private boolean accountNonLocked;
  /**
   * 凭据是否未过期
   */
  private boolean credentialsNonExpired;
  /**
   * 是否启用
   */
  private boolean enabled;

  @Transient
  private Collection<? extends GrantedAuthority> authorities = new HashSet<>();


}
