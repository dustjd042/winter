package com.yeonseong.winter;
import com.yeonseong.winter.core.Banner;
import com.yeonseong.winter.core.ContainerManagement;

public class WinterApplication {
  public static void run(Class<?> main, String[] args) {
    try {
      ContainerManagement.getInstance().setContainerMap(main.getPackageName());
      new Banner().print();
    } catch (Exception e) {
      throw new RuntimeException("Failed to initialize container: " + e.getMessage(), e);
    }
  }
}