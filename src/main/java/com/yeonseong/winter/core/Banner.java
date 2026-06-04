package com.yeonseong.winter.core;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Banner {

  public void print() throws IOException {
    try (InputStream is = Banner.class.getClassLoader().getResourceAsStream("banner.txt")) {
      if (is == null) {
        throw new RuntimeException("banner.txt not found on classpath");
      }
      String banner = new String(is.readAllBytes(), StandardCharsets.UTF_8);
      System.out.println(banner);
    }
  }
}