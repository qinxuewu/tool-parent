package com.pflm.aspect;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import com.pflm.annotation.RedisRateLimit;
import com.pflm.exception.MyException;
import com.pflm.utils.HttpContextUtils;
import com.pflm.utils.IPUtils;

/**
 * redis限流
 * @author qxw
 * @data 2018年11月1日上午10:32:15
 */
@Aspect
@Component
public class RedisRateLimitAspect {
	 @Autowired
	 private RedisTemplate<String, Serializable> limitRedisTemplate;
	 
	 @Autowired
	 private DefaultRedisScript<Number> redisluaScript;
	 
	 @Pointcut("@annotation(com.pflm.annotation.RedisRateLimit)")
	 public void redisPointCut() {
	 }
	 
	 
	 @Around("redisPointCut()")
	 public Object interceptor(ProceedingJoinPoint joinPoint) throws Throwable{
		 	//获取注解上的值
	        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
	        Method method = signature.getMethod();
	        Class<?> targetClass = method.getDeclaringClass();
	        RedisRateLimit rateLimit = method.getAnnotation(RedisRateLimit.class);
	        String msg="已达到限流阀值";
	        if (rateLimit != null) {
	        	  //获取request
	            HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
	            String ipAddress = IPUtils.getIpAddr(request);
	             //组装每个用户访问的限流标识key  ip-类名-方法名-指定的key
	            StringBuffer stringBuffer = new StringBuffer();
	            stringBuffer.append(ipAddress).append("-").append(targetClass.getName()).append("- ") .append(method.getName()).append("-").append(rateLimit.key());
	            List<String> keys = Collections.singletonList(stringBuffer.toString());
	            //使用redus lua脚本控制多次操作的连续性
	            Number number = limitRedisTemplate.execute(redisluaScript, keys, rateLimit.count(), rateLimit.time());
	            if (number != null && number.intValue() != 0 && number.intValue() <= rateLimit.count()) {
	                return joinPoint.proceed();
	            }
	            msg=rateLimit.msg();
	        } else {
	            return joinPoint.proceed();
	        }
	        throw new MyException(msg);
	    }

	  
}
