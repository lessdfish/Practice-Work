---
---
---
---

local capacity = tonumber(ARGV[1])

local refillRate = tonumber(ARGV[2])

local requested = tonumber(ARGV[3])

local ttl = tonumber(ARGV[4])

local redisTime = redis.call('TIME')

local now = tonumber(redisTime[1] * 1000 + math.floor(tonumber(redisTime[2]) / 1000))

local values = redis.call(
        'HMGET',
        KEYS[1],
        'tokens',
        'lastRefill'
)

local tokens = tonumber(values[1])
local lastRefill = tonumber(values[2])

if not tokens then
    tokens = capacity
    lastRefill = now
end

local elapsed = math.max(0,now - lastRefill)

local refill = elapsed * refillRate /1000

tokens = math.min(capacity,tokens+refill)

local allowed = 0

if tokens >= requested then
    tokens = tokens - requested
    allowed = 1
end

redis.call(
        'HSET',
        KEYS[1],
        'tokens',
        tokens,
        'lastRefill',
        now
)

redis.call(
        'PEXPIRE',
        KEYS[1],
        ttl
)
return allowed
