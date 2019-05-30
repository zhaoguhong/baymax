package com.zhaoguhong.baymax.demo.repository;

import com.zhaoguhong.baymax.demo.entity.Demo;
import com.zhaoguhong.baymax.jpa.BaseRepository;
import java.util.List;

public interface DemoRepository extends BaseRepository<Demo> {

  List<Demo> findByUserName(String userName);

}
