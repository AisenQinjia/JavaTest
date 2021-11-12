#
local function split2(str,sp)
    local index = string.find(str,sp)
    local pair = {}
    if index then
        pair.key   = string.sub(str,1,index-1)
        pair.value = string.sub(str,index + 1, #str)
    end
    return pair
end

local ret = {}
local sets = redis.call('Zrangebyscore', 'zzzz','(0','1621394822001')
local tStr = sets[0]

for i =0,(#sets-1) do
    local e = sets[i]
    local onePod = string.find("1-2",'-')
end

return ret
