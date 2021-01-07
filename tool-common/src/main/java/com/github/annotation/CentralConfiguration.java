package com.github.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @author: qinxuewu
 * @date:   2021/1/7 10:57 上午
 * @description:    配置中心注解  基于ZK实现  注解在public变量或方法上
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CentralConfiguration {

  /**
   * 配置项Key 对应ZK的路径
   *
   *
   * @return
   */
  public String key() default "";

  /**
   * 配置项默认值
   * @return
   */
  public String defaultValue() default "";

}
