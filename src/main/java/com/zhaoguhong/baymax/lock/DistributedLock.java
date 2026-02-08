package com.zhaoguhong.baymax.lock;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

/**
 * @author guhong
 * @date 2019/6/25
 */
@Component
public class DistributedLock {

  private final ThreadLocal<String> localLockValue = new ThreadLocal<>();

  private static final String RELEASE_LOCK_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
  private static final byte[] RELEASE_LOCK_SCRIPT_BYTES = RELEASE_LOCK_SCRIPT.getBytes(StandardCharsets.UTF_8);

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  /**
   * 获取锁，如果没有获取到，阻塞
   */
  public void lock(String key) {
    while (!tryLock(key)) {
      try {
        Thread.sleep(30);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new RuntimeException("获取锁被中断", e);
      }
    }
  }

  /**
   * 尝试获取锁
   */
  public boolean tryLock(String key) {
    String lockValue = UUID.randomUUID().toString();

    RedisConnectionFactory factory = redisTemplate.getConnectionFactory();
    RedisConnection conn = factory.getConnection();
    try {
      //key不存在则set，并设置超时时间
      Boolean result = conn.set(key.getBytes(StandardCharsets.UTF_8),
          lockValue.getBytes(StandardCharsets.UTF_8),
          Expiration.from(60, TimeUnit.SECONDS), RedisStringCommands.SetOption.SET_IF_ABSENT);
      if (result != null && result) {
        localLockValue.set(lockValue);
        return true;
      }
    } finally {
      //释放连接
      RedisConnectionUtils.releaseConnection(conn, factory);
    }
    return false;
  }

  /**
   * 尝试获取锁，如果指定时间没有获取到，则返回false
   */
  public boolean tryLock(String key, long timeout, TimeUnit unit) {
    long start = System.currentTimeMillis();
    long timeoutMillis = unit.toMillis(timeout);
    while (!tryLock(key)) {
      if ((System.currentTimeMillis() - start) > timeoutMillis) {
        return false;
      }
      try {
        Thread.sleep(30);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        return false;
      }
    }
    return true;
  }

  /**
   * 释放锁
   */
  public boolean unlock(String key) {
    String lockValue = localLockValue.get();
    if (lockValue == null) {
      return false;
    }
    byte[][] keysAndArgs = new byte[2][];
    keysAndArgs[0] = key.getBytes(StandardCharsets.UTF_8);
    keysAndArgs[1] = lockValue.getBytes(StandardCharsets.UTF_8);
    RedisConnectionFactory factory = redisTemplate.getConnectionFactory();
    RedisConnection conn = factory.getConnection();
    try {
      Long result = conn.scriptingCommands()
          .eval(RELEASE_LOCK_SCRIPT_BYTES, ReturnType.INTEGER, 1,
              keysAndArgs);
      if (result != null && result > 0) {
        localLockValue.remove();
        return true;
      }
    } finally {
      RedisConnectionUtils.releaseConnection(conn, factory);
    }
    return false;
  }

}
