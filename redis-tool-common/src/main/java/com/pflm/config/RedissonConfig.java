package com.pflm.config;
import org.apache.commons.lang.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Redisson分布式锁
 * https://github.com/redisson/redisson/wiki
 * @author qxw
 * 2017年11月22日
 * 
 */
@Component
public class RedissonConfig {
	  
	private Logger log = LoggerFactory.getLogger(getClass());
	/**
	 * 是否开启Redisson分布式锁  true开启   false关闭
	 */
    @Value("${spring.redis.open: false}")
    private boolean open;
    
    @Value("${spring.redis.host}")
    private String host;
    
    @Value("${spring.redis.port}")
    private String port;
    
    @Value("${spring.redis.password}")
    private String password;
	  @Bean
	  public  RedissonClient redissonClient() {
		  RedissonClient redissonClient=null;
		  if(open){
			  Config    config = new Config();
				//单Redis节点模式
				config.useSingleServer().setAddress("redis://"+host+":"+port+"").setPassword(StringUtils.isEmpty(password)?null:password)
				//最小空闲连接数 默认值：32长期保持一定数量的连接有利于提高瞬时写入反应速度
				.setConnectionMinimumIdleSize(5)
				//连接池大小默认值：64
				.setConnectionPoolSize(64)
				//连接空闲超时，单位：毫秒 默认值：10000 如果当前连接池里的连接数量超过了最小空闲连接数，
				//而同时有连接空闲时间超过了该数值，那么这些连接将会自动被关闭，并从连接池里去掉。时间单位是毫秒。
				.setIdleConnectionTimeout(10000)
				//同节点建立连接时的等待超时。时间单位是毫秒。 默认值：10000
				.setConnectTimeout(10000)
				//等待节点回复命令的时间。该时间从命令发送成功时开始计时 默认值：3000
				.setTimeout(3000)
				//命令失败重试次数 默认值：3
				.setRetryAttempts(2)
				//在一条命令发送失败以后，等待重试发送的时间间隔。时间单位是毫秒 默认值：1500
				.setRetryInterval(1500);	
				 redissonClient = Redisson.create(config);
				 log.debug("******************RedissonClient初始化完成**********************");				
		  }
		  return redissonClient;
	  }

}
