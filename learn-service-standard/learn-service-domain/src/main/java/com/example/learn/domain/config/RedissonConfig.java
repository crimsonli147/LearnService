package com.example.learn.domain.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

@Configuration
@EnableConfigurationProperties(RedissonProperties.class)
public class RedissonConfig {

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient(RedissonProperties properties) {
        Config config = new Config();

        // 应用通用配置
        applyIfNotNull(properties.getConfig().getThreads(), config::setThreads);
        applyIfNotNull(properties.getConfig().getNettyThreads(), config::setNettyThreads);
        applyIfNotNull(properties.getConfig().getCodec(), codecClass -> {
            try {
                config.setCodec(codecClass.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                throw new IllegalArgumentException("Failed to instantiate codec: " + codecClass.getName() + e.getMessage());
            }
        });

        // 应用服务器配置（自动适模式）
        configureServerMode(config, properties);

        return Redisson.create(config);
    }

    private void configureServerMode(Config config, RedissonProperties properties) {
        List<ServerConfigurator> configurators = Arrays.asList(
                new ClusterConfigurator(),
                new SingleServerConfigurator()
                // 新的配置模式
        );

        for (ServerConfigurator configurator : configurators) {
            if (configurator.configure(config, properties)) {
                return;
            }
        }

        throw new IllegalArgumentException("No valid Redisson server configuration provided");
    }

    private <T> void applyIfNotNull(T value, Consumer<T> consumer) {
        if (value != null) {
            consumer.accept(value);
        }
    }

    private interface ServerConfigurator {
        boolean configure(Config config, RedissonProperties properties);
    }

    private class ClusterConfigurator implements ServerConfigurator {
        @Override
        public boolean configure(Config config, RedissonProperties properties) {
            if (properties.getConfig().getClusterServersConfig() == null) {
                return false;
            }

            ClusterServersConfig clusterConfig = config.useClusterServers();
            RedissonProperties.ClusterServersConfig clusterProps = properties.getConfig().getClusterServersConfig();

            applyIfNotNull(clusterProps.getNodeAddresses(),
                    addresses -> clusterConfig.addNodeAddress(addresses.toArray(new String[0])));
            applyIfNotNull(clusterProps.getPassword(), clusterConfig::setPassword);
            applyIfNotNull(clusterProps.getSlaveConnectionPoolSize(), clusterConfig::setSlaveConnectionPoolSize);
            applyIfNotNull(clusterProps.getMasterConnectionPoolSize(), clusterConfig::setMasterConnectionPoolSize);
            applyIfNotNull(clusterProps.getIdleConnectionTimeout(), clusterConfig::setIdleConnectionTimeout);
            applyIfNotNull(clusterProps.getConnectTimeout(), clusterConfig::setConnectTimeout);
            applyIfNotNull(clusterProps.getTimeout(), clusterConfig::setTimeout);
            applyIfNotNull(clusterProps.getRetryAttempts(), clusterConfig::setRetryAttempts);
            applyIfNotNull(clusterProps.getRetryInterval(), clusterConfig::setRetryInterval);
            applyIfNotNull(clusterProps.getSubscriptionsPerConnection(), clusterConfig::setSubscriptionsPerConnection);

            return true;
        }
    }

    private class SingleServerConfigurator implements ServerConfigurator {
        @Override
        public boolean configure(Config config, RedissonProperties properties) {
            if (properties.getConfig().getSingleServerConfig() == null) {
                return false;
            }

            SingleServerConfig singleConfig = config.useSingleServer();
            RedissonProperties.SingleServerConfig singleProps = properties.getConfig().getSingleServerConfig();

            applyIfNotNull(singleProps.getAddress(), singleConfig::setAddress);
            applyIfNotNull(singleProps.getPassword(), singleConfig::setPassword);
            applyIfNotNull(singleProps.getConnectionPoolSize(), singleConfig::setConnectionPoolSize);
            applyIfNotNull(singleProps.getConnectionMinimumIdleSize(), singleConfig::setConnectionMinimumIdleSize);
            applyIfNotNull(singleProps.getIdleConnectionTimeout(), singleConfig::setIdleConnectionTimeout);
            applyIfNotNull(singleProps.getConnectTimeout(), singleConfig::setConnectTimeout);
            applyIfNotNull(singleProps.getTimeout(), singleConfig::setTimeout);
            applyIfNotNull(singleProps.getRetryAttempts(), singleConfig::setRetryAttempts);
            applyIfNotNull(singleProps.getRetryInterval(), singleConfig::setRetryInterval);
            applyIfNotNull(singleProps.getSubscriptionsPerConnection(), singleConfig::setSubscriptionsPerConnection);

            return true;
        }
    }
}