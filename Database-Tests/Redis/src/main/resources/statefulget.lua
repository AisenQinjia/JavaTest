--[[
   获取对应服务连接pod
   input:
    KEYS: uid连接状态key, 服务状态查询key
    ARGV:  服务名,起始有效时间
   output: 一个可用的podId,其他情况返回-1
]]
-- 返回服务的pod和状态值  服务状态格式: podId-podState
local function splitState(str,sp)
    local index = string.find(str,sp)
    local pair = {}
    if index then
        local id   = string.sub(str,1,index-1)
        local state = tonumber(string.sub(str,index + 1, #str))
        return id,state
    end
end
local ret = {}
-- 查找连接
local linkingQueryKey = KEYS[1]
local serviceKey = ARGV[1]
local currentPodNum = redis.call("HGET", linkingQueryKey, serviceKey)
if currentPodNum then
    -- 查找到对应服务,直接返回
    table.insert(ret,currentPodNum);
    return ret
else
    -- 目标连接不存在,分配一个连接
    local serviceQueryKey = KEYS[2]
    local beginTimeStamp = ARGV[2]

    local stateList = redis.call('ZRANGEBYSCORE', serviceQueryKey,beginTimeStamp,'+inf','WITHSCORES')

    local stateTable = {}
    -- 获取状态表: {podNum : {score,state}}
    for i =1,#stateList, 2 do
        local podNumState = stateList[i]
        local score = tonumber(stateList[i+1])
        local podNum,podState = splitState(podNumState,'-')
        if (not stateTable[podNum]) or (stateTable[podNum].score < score)  then
            stateTable[podNum] = {}
            stateTable[podNum].score = score
            stateTable[podNum].state = podState
        end
    end
    --todo: 定期清理有序集合
    local podNum   = nil
    local podState = nil
    for key,val in pairs(stateTable) do
        if not podState or podState > val.state  then
            podNum = key
            podState = val.state
        end
    end

    if not podNum then
        -- 找不到一个可用的服务
        table.insert(ret,'-1');
        return ret
    else
        --记录连接
        redis.call("HSET", linkingQueryKey, serviceKey,podNum)
        table.insert(ret,podNum)
        return ret
    end
end
