package org.ashirov.nicolai.pastebin.dao;

import org.ashirov.nicolai.pastebin.model.Paste;
import org.ashirov.nicolai.pastebin.service.RedisGenerateHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class PasteDao {

//    @Value("${aws-bucket}")
//    private String bucketName;
//    private final S3Client s3Client;

    private final JdbcTemplate jdbcTemplate;
    private final RedisGenerateHash redisGenerateHash;

    @Autowired
    public PasteDao(JdbcTemplate jdbcTemplate, RedisGenerateHash redisGenerateHash) {
        this.jdbcTemplate = jdbcTemplate;
        this.redisGenerateHash = redisGenerateHash;
    }

//    public String getPaste(String url) {
//        String sql = "SELECT expires FROM paste WHERE url = ?";
//
//        return jdbcTemplate.query(sql, new Object[]{url}, rs -> {
//            if (rs.next()) {
//                LocalDateTime expires = rs.getTimestamp("expires").toLocalDateTime();
//                LocalDateTime now = LocalDateTime.now();
//                if (expires.isAfter(now)) {
//                    return s3Client.getObjectAsBytes(
//                            builder -> builder.bucket(bucketName).key(url).build()
//                    ).asUtf8String();
//                }
//            }
//            return null;
//        });
//    }

    public String getPaste(String url) {
        String sql = "SELECT d.content, p.expires FROM paste p JOIN data d ON p.url = d.url WHERE p.url = ?";

        return jdbcTemplate.query(sql, new Object[]{url}, rs -> {
            if (rs.next()) {
                LocalDateTime expires = rs.getTimestamp("expires").toLocalDateTime();
                if(expires.isAfter(LocalDateTime.now())) {
                    return rs.getString("content");
                }
            }
            return null;
        });
    }

    public String insertPaste(Paste paste) {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        String url = redisGenerateHash.getHash();


        paste.setUrl(url);

        jdbcTemplate.update("INSERT INTO paste (url, created, expires) VALUES(?, ?, ?)",  paste.getUrl(), now, paste.getExpires());

        jdbcTemplate.update("INSERT INTO data VALUES(?, ?)", paste.getUrl(), paste.getContent());

        return paste.getUrl();
    }

//    public String insertPaste(Paste paste) {
//        Timestamp now =  Timestamp.valueOf(LocalDateTime.now());
//        Integer id = jdbcTemplate.queryForObject(
//                "INSERT INTO paste (created, expires) VALUES (?, ?) RETURNING id",
//                Integer.class,
//                now,
//                paste.getExpires()
//        );
//
//        paste.setUrl(hashCodeGenerator.generateHashCode(id));
//
//        jdbcTemplate.update("UPDATE paste SET url = ? WHERE id = ?",paste.getUrl(), id);
//
//        s3Client.putObject(
//                builder -> builder.bucket(bucketName).key(paste.getUrl()).build(),
//                RequestBody.fromString(paste.getContent())
//        );
//
//        return paste.getUrl();
//    }
}
