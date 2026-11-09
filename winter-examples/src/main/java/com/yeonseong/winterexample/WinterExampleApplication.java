package com.yeonseong.winterexample;

import com.yeonseong.winterexample.config.TestConfig;
import com.yeonseong.winterexample.controller.TestController;
import com.yeonseong.winter.WinterApplication;
import com.yeonseong.winter.core.ContainerManagement;

public class WinterExampleApplication {
  public static void main(String[] args) {

    WinterApplication.run(WinterExampleApplication.class, args);

    ContainerManagement.getInstance().getContainerMap().forEach((key, value) -> {
      System.out.println("Container Name: " + key);
      System.out.println("Container Instance: " + value);
    });

    TestController testController = (TestController) ContainerManagement.getInstance().getContainerMap().get(TestController.class);
    testController.getCount();
  }
}
