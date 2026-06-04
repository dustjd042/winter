package com.yeonseong.test;

import com.yeonseong.test.config.TestConfig;
import com.yeonseong.test.controller.TestController;
import com.yeonseong.winter.WinterApplication;
import com.yeonseong.winter.core.ContainerManagement;

public class Main {
  public static void main(String[] args) {

    WinterApplication.run(Main.class, args);

    ContainerManagement.getInstance().getContainerMap().forEach((key, value) -> {
      System.out.println("Container Name: " + key);
      System.out.println("Container Instance: " + value);
    });

    TestController testController = (TestController) ContainerManagement.getInstance().getContainerMap().get(TestController.class);
    testController.getCount();
  }
}
