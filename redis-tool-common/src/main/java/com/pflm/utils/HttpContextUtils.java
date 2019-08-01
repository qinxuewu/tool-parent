
/**
 *  自定义异常
 * @author qinxuewu
 * @version 1.00
 * @time  26/11/2018 下午 6:26
 * @email 870439570@qq.com
 */
package com.pflm.utils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class HttpContextUtils {

	public static HttpServletRequest getHttpServletRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}
}
