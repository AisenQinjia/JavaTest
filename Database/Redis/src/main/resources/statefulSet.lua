--[[
   设置对应服务连接pod
   input:
    KEYS: uid连接状态key
    ARGV:  服务名,podNum
   output: 成功返回1,其他情况返回-1
]]
-- 返回服务的pod和状态值  服务状态格式: podId-podState
local linkingQueryKey = KEYS[1]
local serviceKey = ARGV[1]
local podNum = ARGV[2]
redis.call("HSET", linkingQueryKey, serviceKey,podNum)
return 0