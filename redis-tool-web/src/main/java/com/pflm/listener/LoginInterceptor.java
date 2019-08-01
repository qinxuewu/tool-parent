package com.pflm.listener;
import io.jsonwebtoken.Claims;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.pflm.exception.MyException;
import com.pflm.utils.JwtUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;




/**
 * 拦截器
 * @author qxw
 * @data 2018年6月15日下午5:11:13
 */
@Component
public class LoginInterceptor implements HandlerInterceptor{


	private   Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		  String token = request.getHeader("Authorization");	
		   //凭证为空
          if(StringUtils.isBlank(token)){
              throw new MyException("Authorization 不能为空", HttpStatus.UNAUTHORIZED.value());
          }	                    
          //检查jwt令牌, 如果令牌不合法或者过期, 里面会直接抛出异常, 下面的catch部分会直接返回
          Claims claims = JwtUtil.validateToken(token);
         
          if(claims == null || JwtUtil.isTokenExpired(claims.getExpiration())){
              throw new MyException("Authorization 失效，请重新授权", HttpStatus.NOT_FOUND.value());
          }	    

          //检查当前token存储的openid是否和请求用户的openid相等
          String tokenOpenid=claims.get("openid").toString();
          String openid=request.getParameter("openid");
          if(!tokenOpenid.equals(openid)){
        	    log.debug("非法请求！当前openid和token存储的openid不匹配：openid:{},tokenOpenid:{}",openid,tokenOpenid);
        	    throw new MyException("非法请求,请重新授权", HttpStatus.NOT_FOUND.value());
          }
          //重新包装request 
          request = JwtUtil.validateTokenAndAddUserIdToHeader(request);      
        return true;  
	}

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) throws Exception {}
	



}
