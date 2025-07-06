package org.ashirov.nicolai.pastebin.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class RedisGenerateHash {

    private final RedisTemplate<String, String> redisTemplate;
    private final HashCodeGenerator hashCodeGenerator;

    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    @Autowired
    public RedisGenerateHash(RedisTemplate<String, String> redisTemplate, HashCodeGenerator hashCodeGenerator) {
        this.redisTemplate = redisTemplate;
        this.hashCodeGenerator = hashCodeGenerator;
    }

    @PostConstruct
    public void init() {
        Long size = redisTemplate.opsForList().size("hash");
        if (size == null || size <= 200) {
            refillAsync();
        }
    }

    public CompletableFuture<String> getHashAsync() {
        return CompletableFuture.supplyAsync(() -> {
            Long size = redisTemplate.opsForList().size("hash");
            if (size == null || size < 200) {
                refillAsync();
            }

            String hash = redisTemplate.opsForList().rightPop("hash");
            if (hash == null) {
                refillAsync();
                hash = redisTemplate.opsForList().rightPop("hash");
            }

            return hash;
        }, executorService);
    }

    private void refillAsync() {
        Long size = redisTemplate.opsForList().size("hash");
        if (size != null && size >= 200) {
            return;
        }

        for (int i = 0; i < 4; i++) {
            executorService.submit(() -> {
                for (int j = 0; j < 200; j++) {
                    String uuid = hashCodeGenerator.generateHashCode();
                    redisTemplate.opsForList().rightPush("hash", uuid);
                }
            });
        }
    }
}
