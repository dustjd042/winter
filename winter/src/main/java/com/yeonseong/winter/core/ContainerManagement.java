package com.yeonseong.winter.core;

import com.yeonseong.winter.annotation.Container;
import com.yeonseong.winter.annotation.Load;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContainerManagement {

  private ContainerManagement () {}

  private static ContainerManagement instance = new ContainerManagement();

  private final Map<Class<?>, Object> containerMap = new HashMap<>();

  private List<Class<?>> containerClassList = new ArrayList<>();
  private List<Method> containerMethodList = new ArrayList<>();
  private List<Constructor<?>> loadConstructorList = new ArrayList<>();

  public static ContainerManagement getInstance() {
    return instance;
  }

  public void setContainerMap(String basePackage) throws Exception {

    String path = basePackage.replace(".", "/");
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    URL resource = classLoader.getResource(path);
    File directory = new File(resource.toURI());

    findAnnotations(basePackage, directory);

    for (var containerClass : containerClassList) {
      setContainerClass(containerClass);
    }

    for (var containerMethod : containerMethodList) {
      setContainerMethod(containerMethod);
    }
  }

  /**
   * 컨테이너 관리 대상 스캔
   *
   * @param packageName
   * @param directory
   * @throws Exception
   */
  private void findAnnotations(String packageName, File directory) throws Exception {

    File[] files = directory.listFiles();
    if (files == null) {
      return;
    }

    for (File file : files) {

      if (file.isDirectory()) {
        findAnnotations(packageName + "." + file.getName(), file);
        continue;
      }

      if (!file.getName().endsWith(".class")) {
        continue;
      }

      String className = packageName + "." + file.getName().replace(".class", "");
      Class<?> clazz = Class.forName(className);

      // 컨테이너 클래스의 경우
      if (clazz.isAnnotationPresent(Container.class)) {

        containerClassList.add(clazz);
        System.out.println("Container class found: " + clazz.getName());

        // 컨테이너 메서드 관리
        Method[] methods = clazz.getMethods();
        for (var method : methods) {
          if (method.isAnnotationPresent(Container.class)) {
            containerMethodList.add(method);
            System.out.println("Container method found: " + method.getName() + " in class " + clazz.getName());
          }
        }

        // 생장자 주입 관리
        Constructor<?>[] constructors = clazz.getConstructors();
        for (var constructor : constructors) {
          if (constructor.isAnnotationPresent(Load.class)) {
            loadConstructorList.add(constructor);
            System.out.println("Load constructor found: " + constructor.getName() + " in class " + clazz.getName());
          }
        }
      }
    }
  }

  private void setContainerClass(Class<?> clazz) throws Exception {

    if (containerMap.containsKey(clazz)) {
      return;
    }

    for (var constructor : clazz.getConstructors()) {
      if (loadConstructorList.contains(constructor)) {
        for (var parameter : constructor.getParameters()) {
          if (!containerMap.containsKey(parameter.getType())) {
            Method method = null;
            for (var containerMethod : containerMethodList) {
              if (containerMethod.getReturnType().equals(parameter.getType())) {
                method = containerMethod;
                break;
              }
            }
            if (method != null) {
              setContainerMethod(method);
            } else {
              setContainerClass(parameter.getType());
            }
          }
        }
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        int i = 0;
        Object[] args = new Object[parameterTypes.length];
        for (var parameterType : parameterTypes) {
          args[i] = containerMap.get(parameterType);
          i++;
        }
        Object object = constructor.newInstance(args);
        containerMap.put(clazz, object);
        System.out.println("Container instance created for class: " + clazz.getName() + " using constructor with parameters: " + parameterTypes.length);
      } else {
        Object object = clazz.getDeclaredConstructor().newInstance();
        containerMap.put(clazz, object);
        System.out.println("Container instance created for class: " + clazz.getName());
      }
    }
  }

  private void setContainerMethod(Method method) throws Exception {

    if (containerMap.containsKey(method.getReturnType())) {
      return;
    }

    if (method.getParameters().length > 0) {
      return;
    } else {
        Object result = method.invoke(containerMap.get(method.getDeclaringClass()));
        containerMap.put(method.getReturnType(), result);
        System.out.println("Container instance created for method: " + method.getName());
    }
  }

  public HashMap<Class<?>, Object> getContainerMap() {
    return new HashMap<>(containerMap);
  }
}
