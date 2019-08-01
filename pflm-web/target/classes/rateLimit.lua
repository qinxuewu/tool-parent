 --限流KEY
local key = "rate.limit:" .. KEYS[1]
 --限流大小
local limit = tonumber(ARGV[1])       
-- 获取当前流量大小
local current = tonumber(redis.call('get', key) or "0")

--如果超出限流大小
if current + 1 > limit then 
  return 0
else 
 -- 没有达到阈值请求数+1，并设置2秒过期
   redis.call("INCRBY", key,"1")
   redis.call("expire", key,"2")
   return current + 1
end