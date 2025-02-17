package com.ghw.maplpicturebackend.Utils;

import cn.hutool.bloomfilter.BitMapBloomFilter;
import cn.hutool.bloomfilter.BloomFilterUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {

    @Autowired
    private RedissonClient redissonClient;

    private final StringRedisTemplate stringRedisTemplate;

    private BitMapBloomFilter bloomFilter = BloomFilterUtil.createBitMap(1000);

    // 线程池
    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);

    public RedisUtils(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    // 获取锁
    public boolean tryLock(String key) {
        RLock rLock = redissonClient.getLock(key);
        try {
            boolean b = rLock.tryLock(-1, TimeUnit.SECONDS);
            return BooleanUtil.isTrue(b);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
        }
    }

    // 释放锁
    public void unLock(String key) {
        RLock rLock = redissonClient.getLock(key);
        rLock.unlock();
    }


    /**
     * 实现向redis插入数据, 并可以设置时间
     *
     */

    public void set(String key, Object value, Long time, TimeUnit unit) {
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value), time, unit);
    }

    /**
     * 实现向redis插入数据, 并使用逻辑过期解决缓存击穿问题
     * 设置随机过期时间解决缓存雪崩
     */
    public void setwithLogicalExpire(String key, Object value, Long time, TimeUnit unit) {
        RedisData redisData = new RedisData();
        redisData.setData(value);
        // 设置逻辑过期时间
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(RandomUtil.randomLong(60, 6 * 60 * 60)));
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData));
    }

    /**
     * 实现从缓存中查询数据
     * 用缓存空值 + 布隆过滤器来解决缓存穿透
     * 用互斥锁解决缓存击穿
     *
     */
    public String query(String keypriex) {
        String key = keypriex;
        // 从Redis查询商铺数据的缓存
        if(bloomFilter.contains(key)) {
            String json = stringRedisTemplate.opsForValue().get(key);
            // 判断缓存中是否存在
            if (StringUtils.isNotBlank(json)) {
                // 缓存中存在数据
                return json;
            }
            return null;
        }
        return null;
    }

    public boolean allowRequest(String key, long windowsSize, long rateLimit) {
        long currentTimeMillis = System.currentTimeMillis();

        // 使用Lua脚本来确保原子性操作
        String luaScript = "local key = KEYS[1]\n" +
                "local current_time = tonumber(ARGV[1])\n" +
                "local window_size = tonumber(ARGV[2])\n" +
                "local threshold = tonumber(ARGV[3])\n" +
                "redis.call('ZREMRANGEBYSCORE', key, 0, current_time - window_size)\n" +
                "local count = redis.call('ZCARD', key)\n" +
                "if count >= threshold then\n" +
                "    return tostring(0)\n" +
                "else\n" +
                "    redis.call('ZADD', key, tostring(current_time), current_time)\n" +
                "    return tostring(1)\n" +
                "end";

        DefaultRedisScript<String> redisScript = new DefaultRedisScript<>(luaScript, String.class);
        String result = stringRedisTemplate.execute(redisScript, Collections.singletonList(key),
                String.valueOf(currentTimeMillis), String.valueOf(windowsSize), String.valueOf(rateLimit));

        return "1".equals(result);
    }


    /**
     * 查询缓存中数据， 使用逻辑过期时间来解决缓存击穿问题
     *
     */

//    public <R, ID> R querywithLogicTimeExpire(String keypriex, ID id, Class<R> type, Function<ID, R> dbfallback, Long time, TimeUnit unit) {
//        String key = keypriex + id;
//        // 从Redis查询商铺数据的缓存
//        String json = stringRedisTemplate.opsForValue().get(key);
//        // 判断缓存中是否存在
//        if (StringUtils.isBlank(json)) {
//            // 不存在
//            return null;
//        }
//        // 缓存中存在数据
//        // 1.反序列化shopcache为Java对象
//        RedisData redisData = JSONUtil.toBean(json, RedisData.class);
//        R r = JSONUtil.toBean((JSONObject) redisData.getData(), type);
//        // 判断是否过期
//        LocalDateTime expireTime = redisData.getExpireTime();
//        if(expireTime.isAfter(LocalDateTime.now())) {
//            // 未过期
//            return r;
//        }
//        // 已过期，重建缓存， 查询数据库，重置缓存中的数据
//        // 获取互斥锁
//        String lockkey =  LOCK_SHOP_KEY + id;
//        if (tryLock(lockkey)) {
//            // 获取锁成功，做DoubleCheck
//            if(expireTime.isAfter(LocalDateTime.now())) {
//                // 未过期
//                return r;
//            }
//            // 保证过期，开始缓存重建
//            CACHE_REBUILD_EXECUTOR.submit(() -> {
//                try {
//                    R r1 = dbfallback.apply(id);
//                    // 缓存重建
//                    this.setwithLogicalExpire(key, r1, time, unit);
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }finally {
//                    // 释放锁
//                    unLock(lockkey);
//                }
//
//            });
//        }
//
//        return r;
//    }

}
