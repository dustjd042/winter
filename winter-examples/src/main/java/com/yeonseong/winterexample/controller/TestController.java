package com.yeonseong.winterexample.controller;

import com.yeonseong.winterexample.service.TestService;
import com.yeonseong.winter.annotation.Container;
import com.yeonseong.winter.annotation.Load;

@Container(name = "")
public class TestController {

  private final TestService testService;
  private final StringBuilder stringBuilder;

  @Load(container = {"A", "B"})
  public TestController(TestService testService, StringBuilder stringBuilder) {
    this.testService = testService;
    this.stringBuilder = stringBuilder;
  }

  public void getCount() {
    System.out.println(testService.count() + " " + stringBuilder.toString());
  }
}
