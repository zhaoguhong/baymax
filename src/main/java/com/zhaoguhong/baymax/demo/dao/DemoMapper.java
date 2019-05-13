package com.zhaoguhong.baymax.demo.dao;

import com.zhaoguhong.baymax.demo.entity.Demo;
import com.zhaoguhong.baymax.mybatis.MyMapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
