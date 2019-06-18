package com.zhaoguhong.baymax.springcache;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.LinkedHashSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.cache.CacheProperties.Redis;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheManager.RedisCacheManagerBuilder;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

/**
 * 配置springCache
 * @author guhong
 * @date 2019/6/18
 */
@Configuration
@EnableConfigurationProperties(CacheProperties.class)
@EnableCaching
public class SpringCacheConfig {

  @Autowired
  private CacheProperties cacheProperties;

  @Bean
  public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
    RedisCacheManagerBuilder builder = RedisCacheManager
        .builder(redisConnectionFactory)
        .cacheDefaults(determineConfiguration());
    List<String> cacheNames = this.cacheProperties.getCacheNames();
    if (!cacheNames.isEmpty()) {
      builder.initialCacheNames(new LinkedHashSet<>(cacheNames));
    }
    return builder.build();
  }

  private org.springframework.data.redis.cache.RedisCacheConfiguration determineConfiguration() {
    Redis redisProperties = this.cacheProperties.getRedis();
    org.springframework.data.redis.cache.RedisCacheConfiguration config = org.springframework.data.redis.cache.RedisCacheConfiguration
        .defaultCacheConfig();
    // 修改序列化为json
    config = config.serializeValuesWith(RedisSerializationContext.SerializationPair
        .fromSerializer(jackson2JsonRedisSerializer()));
    if (redisProperties.getTimeToLive() != null) {
      config = config.entryTtl(redisProperties.getTimeToLive());
    }
    if (redisProperties.getKeyPrefix() != null) {
      // 重写前缀拼接方式
      config = config.computePrefixWith((cacheName) -> redisProperties.getKeyPrefix() + "::" + cacheName + "::");
    }
    if (!redisProperties.isCacheNullValues()) {
      config = config.disableCachingNullValues();
    }
    if (!redisProperties.isUseKeyPrefix()) {
      config = config.disableKeyPrefix();
    }
    return config;
  }

  public Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer() {
    System.out.println(cacheProperties);
    ObjectMapper om = new ObjectMapper();
    om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    // 将类名称序列化到json串中
    om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer =
        new Jackson2JsonRedisSerializer<Object>(Object.class);
    jackson2JsonRedisSerializer.setObjectMapper(om);
    return jackson2JsonRedisSerializer;
  }

}
