package com.github.config;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.annotation.CentralConfiguration;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author: qinxuewu
 * @date:   2021/1/7 10:59 上午
 * @description:    配置中心注解值设置，初始化启动时加载ZK到内存中，并监听key的变更
 */
@Component
@Order(10)
public class CentralConfigurationBeanPostProcessor implements BeanPostProcessor {
  private static Logger logger = LoggerFactory.getLogger(CentralConfigurationBeanPostProcessor.class);

  @Autowired
  private CuratorFramework zkClient;

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName)
      throws BeansException {
    return bean;
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    processCentralConfigurationFields(bean);
    processCentralConfigurationMethods(bean);
    return bean;
  }

  /**
   * 处理添加了@CentralConfiguration注解的字段
   * @param bean
   */
  private void processCentralConfigurationFields(final Object bean) {
    Field[] fields = bean.getClass().getFields();
    for (final Field field : fields) {
      if (field.isAnnotationPresent(CentralConfiguration.class)) {
         CentralConfiguration centralConfiguration = field.getAnnotation(CentralConfiguration.class);
        try {
          // 创建本节点数据变化监听事件
          final NodeCache nodeCache = new NodeCache(zkClient, centralConfiguration.key(), false);
          nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
              // 获取值后，设置到bean的字段中
              setValueToField(bean, field, new String(nodeCache.getCurrentData().getData()));
            }
          });
          nodeCache.start();
        } catch (Exception e) {
          logger.error(e.getMessage(), e);
        }
      }
    }
  }

  /**
   * 处理添加了@CentralConfiguration注解的方法
   * @param bean
   */
  private void processCentralConfigurationMethods(final Object bean) {
    Method[] methods = bean.getClass().getMethods();
    for (final Method method : methods) {
      if (method.isAnnotationPresent(CentralConfiguration.class)) {
         CentralConfiguration centralConfiguration = method.getAnnotation(CentralConfiguration.class);
        try {
          // 创建本节点数据变化监听事件
          final NodeCache nodeCache = new NodeCache(zkClient, centralConfiguration.key(), false);
          nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
              method.invoke(bean, new String(nodeCache.getCurrentData().getData()));
            }
          });
          nodeCache.start();
        } catch (Exception e) {
          logger.error(e.getMessage(), e);
        }
      }
    }
  }

  private void setValueToField(Object bean, Field field, String value) {
    if (logger.isDebugEnabled()) {
      logger.debug("Type of {}.{} is {}.", bean.getClass().getSimpleName(), field.getName(), field.getType().getName());
    }
    try {
      // 将字符串value转换为Field字段对应的类型
      switch (field.getType().getName()) {
        case "java.lang.String":
          field.set(bean, value);
          break;
        case "int":
          field.set(bean, StringUtils.isBlank(value) ? 0 : Integer.parseInt(value));
          break;
        case "java.lang.Integer":
          field.set(bean, StringUtils.isBlank(value) ? null : Integer.valueOf(value));
          break;
        case "long":
          field.set(bean, StringUtils.isBlank(value) ? 0L : Long.parseLong(value));
          break;
        case "java.lang.Long":
          field.set(bean, StringUtils.isBlank(value) ? null : Long.valueOf(value));
          break;
        case "double":
          field.set(bean, StringUtils.isBlank(value) ? 0.0 : Double.parseDouble(value));
          break;
        case "java.lang.Double":
          field.set(bean, StringUtils.isBlank(value) ? null : Double.valueOf(value));
          break;
        case "float":
          field.set(bean, StringUtils.isBlank(value) ? 0.0 : Float.parseFloat(value));
          break;
        case "java.lang.Float":
          field.set(bean, StringUtils.isBlank(value) ? null : Float.valueOf(value));
          break;
        case "boolean":
          field.set(bean, !StringUtils.isBlank(value) && Boolean.parseBoolean(value));
          break;
        case "java.lang.Boolean":
          field.set(bean, StringUtils.isBlank(value) ? null : Boolean.valueOf(value));
          break;
        case "com.alibaba.fastjson.JSONObject":
          field.set(bean, StringUtils.isBlank(value) ? null : JSONObject.parse(value));
          break;
        case "com.alibaba.fastjson.JSONArray":
          field.set(bean, StringUtils.isBlank(value) ? null : JSONArray.parse(value));
          break;
        case "java.util.List":
        case "java.util.ArrayList":
          field.set(bean, StringUtils.isBlank(value) ? Collections.emptyList() : Arrays.asList(value.split(",")));
          break;
        default:
          throw new RuntimeException("Unsupport type " + field.getType().getName() + " of " + bean.getClass().getSimpleName() + "." + field.getName());
      }
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("Illegal Argument [" + value + "] for "
          + bean.getClass().getSimpleName() + "." + field.getName() + "("
          + field.getType().getName() + "): " + e.getMessage());
    } catch (IllegalAccessException e) {
      throw new RuntimeException("Illegal Access to " + bean.getClass().getSimpleName() + "."
          + field.getName() + ": " + e.getMessage());
    }
    String maskedValue = value == null ? "<null>" : ("".equals(value) ? "" : (value.length() <= 3 ? (value.substring(0, 1) + "***" + value.substring(value.length() - 1)) : (value.substring(0, 2) + "***" + value.substring(value.length() - 1))));
    logger.info("Set value [{}] to [{}.{}].", maskedValue, bean.getClass().getSimpleName(), field.getName());
  }
}
