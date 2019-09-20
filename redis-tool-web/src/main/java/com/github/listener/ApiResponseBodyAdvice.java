package com.github.listener;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import com.github.annotation.Encrypt;
import com.github.utils.AesEncryptUtil;


/**
 * 返回数据加密
 *
 * 
 * @author qinxuewu 2019年1月17日上午11:11:44
 *
 */
@SuppressWarnings("rawtypes")
@RestControllerAdvice
public class ApiResponseBodyAdvice implements ResponseBodyAdvice {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${spring.aes.key}")
	private String rawKey;
	

	/**
	 * 判断哪些需要拦截
	 */
	@Override
	public boolean supports(MethodParameter returnType, Class converterType) {
		return true;
	}



	@Override
	public Object beforeBodyWrite(Object body, MethodParameter methodParameter,MediaType selectedContentType, Class selectedConverterType,
			ServerHttpRequest request, ServerHttpResponse response){
		if (body == null){
			return null;
		}
		try {
			if (methodParameter.getMethod().isAnnotationPresent(Encrypt.class)) {		
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("result", AesEncryptUtil.encrypt(body.toString(), rawKey));
				return map;
			}
		} catch (Exception e) {
			logger.debug("beforeBodyWrite返回参加加密异常：{}",e);
		}
		return body;
	}


}
