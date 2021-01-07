package com.github.config;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.CountDownLatch;

/**
 * Zookeeper配置
 * @author qinxuewu
 * @create 19/9/2下午1:25
 * @since 1.0.0
 */

@Configuration
public class ZookeeperConfig {
  private static final Logger logger = LoggerFactory.getLogger(ZookeeperConfig.class);

  @Value("${zookeeper.address}")
  private    String connectString;

  @Value("${zookeeper.timeout}")
  private  int timeout;


  @Bean(name = "zkClient")
  public CuratorFramework zkClient(){
    CuratorFramework client = null;
    try {
      RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
      client = CuratorFrameworkFactory.builder().connectString(connectString)
          .retryPolicy(retryPolicy).connectionTimeoutMs(timeout).sessionTimeoutMs(timeout)
          .build();
      client.start();
      logger.info("【初始化ZooKeeper连接状态....】={}",client.getState());
    }catch (Exception e){
      logger.error("初始化ZooKeeper连接异常....】={}",e);
    }
    return  client;
  }



}
