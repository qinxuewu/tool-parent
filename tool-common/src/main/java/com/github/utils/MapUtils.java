package com.github.utils;
import java.util.HashMap;



/**
 * Map工具类
 * @author qinxuewu
 * @date 2016年12月21日 下午12:53:33
 */
public class MapUtils extends HashMap<String, Object> {

    @Override
    public MapUtils put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
