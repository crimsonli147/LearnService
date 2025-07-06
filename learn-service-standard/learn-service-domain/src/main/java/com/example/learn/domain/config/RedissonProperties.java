package com.example.learn.domain.config;

import lombok.Data;
import org.redisson.client.codec.Codec;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "spring.redis.redisson.config")
public class RedissonProperties {
    private Config config;

    @Data
    public static class Config {
        private ClusterServersConfig clusterServersConfig;
        private SingleServerConfig singleServerConfig;
        // 其他配置模式，如：
        // private MasterSlaveServersConfig masterSlaveServersConfig;
        // private SentinelServersConfig sentinelServersConfig;
        private Integer threads;
        private Integer nettyThreads;
        private Class<? extends Codec> codec;
    }

    @Data
    public static class ClusterServersConfig {
        private List<String> nodeAddresses;
        private String password;
        private Integer slaveConnectionPoolSize;
        private Integer masterConnectionPoolSize;
        private Integer idleConnectionTimeout;
        private Integer connectTimeout;
        private Integer timeout;
        private Integer retryAttempts;
        private Integer retryInterval;
        private Integer subscriptionsPerConnection;
    }

    @Data
    public static class SingleServerConfig {
        private String address;
        private String password;
        private Integer connectionPoolSize;
        private Integer connectionMinimumIdleSize;
        private Integer idleConnectionTimeout;
        private Integer connectTimeout;
        private Integer timeout;
        private Integer retryAttempts;
        private Integer retryInterval;
        private Integer subscriptionsPerConnection;
    }
}