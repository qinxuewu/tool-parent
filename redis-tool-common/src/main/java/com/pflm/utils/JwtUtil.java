package com.pflm.utils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * jwt
 * @author qinxuewu
 * 2018年12月27日下午5:20:36
 *
 */
public class JwtUtil {
	
	 private static Logger logger = LoggerFactory.getLogger(JwtUtil.class);
	//秘钥
	 public static final String SECRET = "ThisIsASecret";
	 public static final String HEADER_STRING = "Authorization";
	 public static final String TOKEN_PREFIX = "Bearer ";
	 public static final String USER_OPENID = "openid";
	//有效期  无穷大
	 public static final long EXPIRATION_TIME = Integer.MAX_VALUE; 
	    public static String generateToken(String openid) {
	        HashMap<String, Object> map = new HashMap<>();
	        //加密参数，可以放任意
	        map.put(USER_OPENID, openid);
	        String jwt = Jwts.builder()
	                .setClaims(map)
	                .setExpiration(new Date(System.currentTimeMillis() +EXPIRATION_TIME ))
	                .signWith(SignatureAlgorithm.HS512, SECRET)
	                .compact();
	        return TOKEN_PREFIX+jwt; //jwt前面一般都会加Bearer
	    }

	    /**
	     * 验证token
	     * @param token
	     */
	    public static Claims validateToken(String token) {
	        try {
	        	return Jwts.parser()
	                    .setSigningKey(SECRET)
	                    .parseClaimsJws(token.replace("Bearer ",""))
	                    .getBody();
	        }catch (Exception e){
	        	logger.debug("validate is token error ", e);
	        	 return null;
	        }
	    }
	    

	    
	    /**
	     * token是否过期
	     * @return  true：过期
	     */
	    public static boolean isTokenExpired(Date expiration) {
	        return expiration.before(new Date());
	    }
	    
	    public static HttpServletRequest validateTokenAndAddUserIdToHeader(HttpServletRequest request) {
	        String token = request.getHeader(HEADER_STRING);
	        if (token != null) {
	            // parse the token.
	            try {
	                Map<String, Object> body = Jwts.parser()
	                        .setSigningKey(SECRET)
	                        .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
	                        .getBody();
	                return new CustomHttpServletRequest(request, body);
	            } catch (Exception e) {
	                logger.info(e.getMessage());
	                throw new TokenValidationException(e.getMessage());
	            }
	        } else {
	            throw new TokenValidationException("Missing token");
	        }
	    }

	    public static class CustomHttpServletRequest extends HttpServletRequestWrapper {
	        private Map<String, String> claims;

	        public CustomHttpServletRequest(HttpServletRequest request, Map<String, ?> claims) {
	            super(request);
	            this.claims = new HashMap<>();
	            claims.forEach((k, v) -> this.claims.put(k, String.valueOf(v)));
	        }

	        @Override
	        public Enumeration<String> getHeaders(String name) {
	            if (claims != null && claims.containsKey(name)) {
	                return Collections.enumeration(Arrays.asList(claims.get(name)));
	            }
	            return super.getHeaders(name);
	        }

	        public Map<String, String> getClaims() {
	            return claims;
	        }
	    }

	    @SuppressWarnings("serial")
		static class TokenValidationException extends RuntimeException {
	        public TokenValidationException(String msg) {
	            super(msg);
	        }
	    }


}
