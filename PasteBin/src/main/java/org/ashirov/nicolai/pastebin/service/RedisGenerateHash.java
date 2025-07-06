package org.ashirov.nicolai.pastebin.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisGenerateHash {

    private final RedisTemplate<String, String> redisTemplate;
    private final HashCodeGenerator hashCodeGenerator;

    @Autowired
    public RedisGenerateHash(RedisTemplate<String, String> redisTemplate,  HashCodeGenerator hashCodeGenerator) {
        this.redisTemplate = redisTemplate;
        this.hashCodeGenerator = hashCodeGenerator;
    }

    @PostConstruct
    public void init() {
        Long size = redisTemplate.opsForList().size("hash");
        if(size == null || size <= 200) refill();
    }

    public String getHash(){
        Long size = redisTemplate.opsForList().size("hash");
        if (size == null || size < 200) refill();


        String hash = redisTemplate.opsForList().rightPop("hash");
        if (hash == null) {
            refill();
            hash = redisTemplate.opsForList().rightPop("hash");
        }

        return  hash;
    }

    private void refill(){
        Long size = redisTemplate.opsForList().size("hash");
        if (size != null && size >= 200) {
            return;
        }
        for(int i = 0; i < 800; i++){
            String uuid = hashCodeGenerator.generateHashCode();
            redisTemplate.opsForList().rightPush("hash", uuid);
        }
    }
}
