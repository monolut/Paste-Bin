package org.ashirov.nicolai.pastebin.service;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class HashCodeGenerator {

    public String generateHashCode() {
        // Вариант генерации url через Base64
//        byte[] bytes = ByteBuffer.allocate(4).putInt(id).array();
//        String hash = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
//        return hash.length() >= 8 ? hash.substring(0, 8) : hash;

        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

}
