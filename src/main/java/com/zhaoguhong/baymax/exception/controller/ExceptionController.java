package com.zhaoguhong.baymax.exception.controller;

import com.zhaoguhong.baymax.exception.dao.ExceptionLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/anon/exception")
public class ExceptionController {

  @Autowired
  private ExceptionLogMapper exceptionLogMapper;

  @GetMapping(value = "/{id}")
  public String getDemo(@PathVariable(value = "id") Long id) {
    return exceptionLogMapper.selectByPrimaryKey(id).getException();
  }

}
