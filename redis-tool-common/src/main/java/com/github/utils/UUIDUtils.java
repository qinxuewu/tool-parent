package com.github.utils;

import java.util.Random;
import java.util.UUID;

/**
 * Created by Levan on 2018-11-27.
 */
public class UUIDUtils {



    /**
     * 获取UUID
     */
    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }
    
    
	/**
	 * 随机验证码
	 * @return
	 */
	public static String getRadomNumber(){
		Random random = new Random();
		String result = "";
		for (int i = 0; i < 4; i++) {
			result += random.nextInt(10);
		} 
		return result;
	}
}
