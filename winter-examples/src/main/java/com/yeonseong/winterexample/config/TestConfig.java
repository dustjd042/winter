package com.yeonseong.winterexample.config;

import com.yeonseong.winter.annotation.Container;

@Container
public class TestConfig {

  @Container
  public StringBuilder testConfigMethod() {
    return new StringBuilder("Test Config Method");
  }
}
