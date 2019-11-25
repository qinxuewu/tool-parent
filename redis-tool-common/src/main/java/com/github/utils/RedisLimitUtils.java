//package com.github.utils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.connection.RedisConnection;
//import org.springframework.data.redis.connection.ReturnType;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.script.DefaultRedisScript;
//import org.springframework.stereotype.Component;
//
///**
// * redis 分布式锁Utils
// *
// * @author Zl
// * @date 2019/8/2
// * @since
// */
//@Component
//public class RedisLimitUtils {
//
//    private static final DefaultRedisScript<String> LIMIT_LUA;
//    private static final String LIMIT_HEARD = "limit:";
//
//    static {
//        StringBuilder sb = new StringBuilder();
//        sb.append("local key = KEYS[1] ");
//        sb.append("local capacity = tonumber(ARGV[1]) ");
//        sb.append("local intervalTime = tonumber(ARGV[2]) ");
//        sb.append("local expireTime = tonumber(ARGV[3]) ");
//        sb.append("local ttlTime = redis.call(\"pttl\",key) ");
//        //计算时差需要累加的令牌数量
//        sb.append("local sum = math.floor((expireTime - ttlTime) / intervalTime) ");
//        sb.append("if sum > 0 then ");
//        //累加令牌 超过桶容量时直接set为容量值 会自动进行初始化 要求间隔时间一点要小于过期时间的的1/2
//        sb.append("    if redis.call(\"incrby\",key,sum) > capacity then ");
//        sb.append("        redis.call(\"set\",key,capacity) ");
//        sb.append("    end ");
//        //重新写入过期时间 避免重复计算
//        sb.append("    redis.call(\"pexpire\",key,expireTime) ");
//        sb.append("end ");
//        //进行decr令牌
//        sb.append("if redis.call(\"decr\",key) >= 0 ");
//        sb.append("        then ");
//        sb.append("    return 1 ");
//        sb.append("else ");
//        //失败回滚令牌
//        sb.append("    redis.call(\"incr\",key) ");
//        sb.append("    return 0 ");
//        sb.append("end ");
//        DefaultRedisScript<String> script = new DefaultRedisScript<>();
//        script.setScriptText(sb.toString());
//        LIMIT_LUA = script;
//    }
//
//    @Autowired
//    private RedisTemplate<String, Long> redisTemplate;
//
//
//    /**
//     * 获取上次填入桶时间与过期时间的差值
//     * now - 设置的过期时间 - ttl的时间 = 上次设置的时间
//     *
//     * @return
//     */
//    public boolean limitHandler(String key, Long capacity, Long intervalTime, Long expireTime) {
//        try {
//            Object execute = redisTemplate.execute(
//                    (RedisConnection connection) -> connection.eval(
//                            LIMIT_LUA.getScriptAsString().getBytes(),
//                            ReturnType.INTEGER,
//                            1,
//                            buildLimitKey(key).getBytes(),
//                            capacity.toString().getBytes(),
//                            intervalTime.toString().getBytes(),
//                            expireTime.toString().getBytes())
//            );
//            return execute.equals(1L);
//        } catch (Exception e) {
//          e.printStackTrace();
//        }
//        return false;
//    }
//
//    private String buildLimitKey(String key) {
//        return LIMIT_HEARD + key;
//    }
//
//}
