package com.example.learn.domain.utils;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redisson 操作工具类
 * 提供对 Redis 各种数据结构的操作方法封装
 */
@Slf4j
@Component
public class RedissonUtils {

    @Autowired
    private RedissonClient redissonClient;

    // ============================= 通用操作 ============================

    /**
     * 设置缓存失效时间
     * @param key 键
     * @param time 时间
     * @param timeUnit 时间单位
     * @return 设置是否成功
     */
    public boolean expire(String key, long time, TimeUnit timeUnit) {
        try {
            if (time > 0) {
                redissonClient.getBucket(key).expire(time, timeUnit);
            }
            return true;
        } catch (Exception e) {
            log.error("Failed to set key expiration, key: {}", key, e);
            return false;
        }
    }

    /**
     * 获取缓存剩余存活时间
     * @param key 键
     * @return 剩余时间(毫秒)
     */
    public long getExpire(String key) {
        return redissonClient.getBucket(key).remainTimeToLive();
    }

    /**
     * 判断key是否存在
     * @param key 键
     * @return 是否存在
     */
    public boolean hasKey(String key) {
        try {
            return redissonClient.getBucket(key).isExists();
        } catch (Exception e) {
            log.error("Failed to check key existence, key: {}", key, e);
            return false;
        }
    }

    /**
     * 删除缓存
     * @param keys 可以传一个或多个key
     */
    public void del(String... keys) {
        if (keys != null && keys.length > 0) {
            try {
                if (keys.length == 1) {
                    redissonClient.getBucket(keys[0]).delete();
                } else {
                    redissonClient.getKeys().delete(keys);
                }
            } catch (Exception e) {
                log.error("Failed to delete keys: {}", Arrays.toString(keys), e);
            }
        }
    }

    // ============================ String 操作 ===========================

    /**
     * 获取缓存值
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : redissonClient.getBucket(key).get();
    }

    /**
     * 设置缓存值
     * @param key 键
     * @param value 值
     * @return 设置是否成功
     */
    public boolean set(String key, Object value) {
        try {
            redissonClient.getBucket(key).set(value);
            return true;
        } catch (Exception e) {
            log.error("Failed to set value, key: {}", key, e);
            return false;
        }
    }

    /**
     * 设置缓存值并指定过期时间
     * @param key 键
     * @param value 值
     * @param time 时间
     * @param timeUnit 时间单位
     * @return 设置是否成功
     */
    public boolean set(String key, Object value, long time, TimeUnit timeUnit) {
        try {
            if (time > 0) {
                redissonClient.getBucket(key).set(value, time, timeUnit);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("Failed to set value with TTL, key: {}", key, e);
            return false;
        }
    }

    // ================================ Map 操作 ================================

    /**
     * 获取Hash中的值
     * @param key 键
     * @param field 项
     * @return 值
     */
    public Object hget(String key, String field) {
        return redissonClient.getMap(key).get(field);
    }

    /**
     * 获取Hash中的所有键值
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hmget(String key) {
        return redissonClient.getMap(key);
    }

    /**
     * 设置Hash多个键值
     * @param key 键
     * @param map 对应多个键值
     * @return 设置是否成功
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redissonClient.getMap(key).putAll(map);
            return true;
        } catch (Exception e) {
            log.error("Failed to set hash values, key: {}", key, e);
            return false;
        }
    }

    /**
     * 设置Hash多个键值并指定过期时间
     * @param key 键
     * @param map 对应多个键值
     * @param time 时间
     * @param timeUnit 时间单位
     * @return 设置是否成功
     */
    public boolean hmset(String key, Map<String, Object> map, long time, TimeUnit timeUnit) {
        try {
            redissonClient.getMap(key).putAll(map);
            if (time > 0) {
                expire(key, time, timeUnit);
            }
            return true;
        } catch (Exception e) {
            log.error("Failed to set hash values with TTL, key: {}", key, e);
            return false;
        }
    }

    /**
     * 向Hash表中放入数据
     * @param key 键
     * @param field 项
     * @param value 值
     * @return 设置是否成功
     */
    public boolean hset(String key, String field, Object value) {
        try {
            redissonClient.getMap(key).put(field, value);
            return true;
        } catch (Exception e) {
            log.error("Failed to set hash field, key: {}, field: {}", key, field, e);
            return false;
        }
    }

    /**
     * 向Hash表中放入数据并指定过期时间
     * @param key 键
     * @param field 项
     * @param value 值
     * @param time 时间
     * @param timeUnit 时间单位
     * @return 设置是否成功
     */
    public boolean hset(String key, String field, Object value, long time, TimeUnit timeUnit) {
        try {
            redissonClient.getMap(key).put(field, value);
            if (time > 0) {
                expire(key, time, timeUnit);
            }
            return true;
        } catch (Exception e) {
            log.error("Failed to set hash field with TTL, key: {}, field: {}", key, field, e);
            return false;
        }
    }

    /**
     * 删除Hash表中的值
     * @param key 键
     * @param fields 项 可以是多个
     */
    public void hdel(String key, Object... fields) {
        try {
            RMap<Object, Object> map = redissonClient.getMap(key);
            for (Object field : fields) {
                map.remove(field);
            }
        } catch (Exception e) {
            log.error("Failed to delete hash fields, key: {}, fields: {}", key, Arrays.toString(fields), e);
        }
    }

    // ============================ Set 操作 =============================

    /**
     * 获取Set中的所有值
     * @param key 键
     * @return Set中的所有值
     */
    public Set<Object> sGet(String key) {
        return redissonClient.getSet(key);
    }

    /**
     * 判断Set中是否包含某个值
     * @param key 键
     * @param value 值
     * @return 是否包含
     */
    public boolean sHasKey(String key, Object value) {
        return redissonClient.getSet(key).contains(value);
    }

    /**
     * 向Set中添加多个元素
     * @param key 键
     * @param values 值 可以是多个
     * @return 是否至少有一个元素被成功添加
     */
    public boolean sSet(String key, Object... values) {
        try {
            if (values == null || values.length == 0) {
                return false;
            }
            return redissonClient.getSet(key).addAll(Arrays.asList(values));
        } catch (Exception e) {
            log.error("Failed to add set elements, key: {}, values: {}", key, Arrays.toString(values), e);
            return false;
        }
    }

    // =============================== List 操作 ==============================

    /**
     * 获取List中指定范围的值
     * @param key 键
     * @param start 开始索引
     * @param end 结束索引
     * @return List中指定范围的值
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redissonClient.getList(key).subList((int) start, (int) end + 1);
        } catch (Exception e) {
            log.error("Failed to get list range, key: {}, start: {}, end: {}", key, start, end, e);
            return null;
        }
    }

    /**
     * 向List中添加多个元素
     * @param key 键
     * @param value 值列表
     * @return 设置是否成功
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            redissonClient.getList(key).addAll(value);
            return true;
        } catch (Exception e) {
            log.error("Failed to add list elements, key: {}", key, e);
            return false;
        }
    }

    // ========================== 分布式锁操作 ==========================

    /**
     * 获取分布式锁
     * @param lockKey 锁键
     * @param waitTime 等待时间
     * @param leaseTime 持有时间
     * @param unit 时间单位
     * @return 是否获取成功
     * @throws InterruptedException 中断异常
     */
    public boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException {
        return redissonClient.getLock(lockKey).tryLock(waitTime, leaseTime, unit);
    }

    /**
     * 释放分布式锁
     * @param lockKey 锁键
     */
    public void unlock(String lockKey) {
        try {
            redissonClient.getLock(lockKey).unlock();
        } catch (Exception e) {
            log.error("Failed to unlock, lockKey: {}", lockKey, e);
        }
    }
}