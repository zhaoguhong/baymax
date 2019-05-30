package com.zhaoguhong.baymax.user.entity.repository;

import com.zhaoguhong.baymax.jpa.BaseRepository;
import com.zhaoguhong.baymax.user.entity.User;

/**
 * @author guhong
 * @date 2019/5/28
 */
public interface UserRepository extends BaseRepository<User> {

  User findByUsernameAndIsDeleted(String username, Integer deleted);

}
