package com.zhaoguhong.baymax.demo.controller;

import com.zhaoguhong.baymax.common.ResponseResult;
import com.zhaoguhong.baymax.demo.dao.DemoMapper;
import com.zhaoguhong.baymax.demo.entity.Demo;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author guhong
 * @date 2019/5/30
 */
@RestController
@RequestMapping("/test")
public class DemoController {

  @Autowired
  private DemoMapper demoMapper;

  /**
   * 测试成功的 ResponseResult
   */
  @GetMapping("/successResult")
  public ResponseResult<List<Demo>> test() {
    List<Demo> demos = demoMapper.getDemos();
    return ResponseResult.success(demos);
  }

  /**
   * 测试失败的 ResponseResult
   */
  @GetMapping("/errorResult")
  public ResponseResult<List<Demo>> demo() {
    return ResponseResult.error("操作失败");
  }

  /**
   * 新增
   */
  @PostMapping("/add")
  public ResponseResult<String> add(@RequestBody @Valid Demo demo) {
    demoMapper.insert(demo);
    return ResponseResult.success();
  }

}
