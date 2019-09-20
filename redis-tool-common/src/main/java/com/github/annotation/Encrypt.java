package com.github.annotation;
import java.lang.annotation.*;

/**
 * 返回参数  加密注解类
 * @author qinxuewu 2019年1月17日上午10:58:02
 *
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Encrypt {



}
