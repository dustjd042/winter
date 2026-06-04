package com.yeonseong.test.service;

import com.yeonseong.test.model.FireModel;
import com.yeonseong.test.model.TestModel;
import com.yeonseong.winter.annotation.Container;
import com.yeonseong.winter.annotation.Load;

@Container
public class TestService {

  private final TestModel testModel;
  private final FireModel fireModel;

  @Load
  public TestService(TestModel testModel, FireModel fireModel) {
    this.testModel = testModel;
    this.fireModel = fireModel;
  }

  public int count() {
    System.out.println(fireModel.getFire());
    return testModel.selectCount();
  }
}
