package com.github.base;

import java.util.List;
import java.util.Map;


/**
 *  基础Dao(还需在XML文件里，有对应的SQL语句)
 * @author qinxuewu
 * @version 1.00
 * @time  26/11/2018 下午 6:26
 * @email 870439570@qq.com
 */
public interface BaseDao<T> {

    void save(T t);

    void save(Map<String, Object> map);

    void saveBatch(List<T> list);

    int update(T t);

    int updateMap(Map<String, Object> map);

    int delete(Object id);

    int deleteMap(Map<String, Object> map);

    int deleteBatch(Object[] id);

    T queryObject(Object id);

    List<T> queryList(Map<String, Object> map);

    List<T> queryList(Object id);

    int queryTotal(Map<String, Object> map);

    int queryTotal();
}
