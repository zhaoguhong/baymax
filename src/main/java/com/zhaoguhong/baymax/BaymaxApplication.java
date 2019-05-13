package com.zhaoguhong.baymax;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
