package com.github.aspect;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.github.annotation.RedisRateLimit;
import com.github.exception.MyException;
import com.github.utils.HttpContextUtils;


/**
 * 请求参数解密
 * 
 * @author qinxuewu 2019年1月17日下午6:31:39
 *
 */
@Aspect
@Component
public class DecryptAspect {

	public final Logger log = LoggerFactory.getLogger(getClass());

	@Pointcut("@annotation(com.github.annotation.Decrypt)")
	public void decryptPointCut() {

	}

	@Around("decryptPointCut()")
	public Object interceptor(ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		RedisRateLimit rateLimit = method.getAnnotation(RedisRateLimit.class);
		if (rateLimit != null) {
	        //请求的参数
	        Object[] args = joinPoint.getArgs();
			// 获取request
			HttpServletRequest request = HttpContextUtils.getHttpServletRequest();

		} else {
			return joinPoint.proceed();
		}
		throw new MyException("请求参数解密异常");
	}
}
