package org.example.empresa.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.example.empresa.cache.CacheNames;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CacheConfig {

    @Bean
    public CacheManagerCustomizer<CaffeineCacheManager> cacheManagerCustomizer() {
        return cacheManager -> {

            cacheManager.setAllowNullValues(false);

            cacheManager.registerCustomCache(
                    CacheNames.PRODUCTS, Caffeine.newBuilder()
                            .maximumSize(500)
                            .expireAfterWrite(Duration.ofMinutes(30))
                            .recordStats()
                            .build());

            cacheManager.registerCustomCache(
                    CacheNames.PRODUCT_LIST, Caffeine.newBuilder()
                            .maximumSize(500)
                            .expireAfterWrite(Duration.ofMinutes(5))
                            .recordStats()
                            .build());

            cacheManager.registerCustomCache(
                    CacheNames.PRODUCT_SEARCH, Caffeine.newBuilder()
                            .maximumSize(500)
                            .expireAfterWrite(Duration.ofMinutes(5))
                            .recordStats()
                            .build());

            cacheManager.registerCustomCache(
                    CacheNames.ORDERS, Caffeine.newBuilder()
                            .maximumSize(500)
                            .expireAfterWrite(Duration.ofMinutes(30))
                            .recordStats()
                            .build());

            cacheManager.registerCustomCache(
                    CacheNames.ORDER_LIST, Caffeine.newBuilder()
                            .maximumSize(500)
                            .expireAfterWrite(Duration.ofMinutes(5))
                            .recordStats()
                            .build());

            cacheManager.registerCustomCache(
                    CacheNames.ORDER_SEARCH, Caffeine.newBuilder()
                            .maximumSize(500)
                            .expireAfterWrite(Duration.ofMinutes(5))
                            .recordStats()
                            .build());
        };
    }
}