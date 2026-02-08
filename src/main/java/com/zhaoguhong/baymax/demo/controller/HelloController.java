package com.zhaoguhong.baymax.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页测试接口
 */
@RestController
public class HelloController {

  @GetMapping("/test")
  public String test() {
    return "hello baymax";
  }
}
