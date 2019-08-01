package com.pflm.annotation;
import java.lang.annotation.*;

/**
 * 请求参数  解密
 * @author qinxuewu 2019年1月17日上午10:58:02
 *
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Decrypt {



}
