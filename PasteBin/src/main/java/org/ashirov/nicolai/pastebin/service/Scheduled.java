package org.ashirov.nicolai.pastebin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;

@Component
public class Scheduled {

    @Value("${aws-bucket}")
    private String bucketName;

    private S3Client s3Client;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public Scheduled(JdbcTemplate jdbcTemplate, S3Client s3Client) {
        this.jdbcTemplate = jdbcTemplate;
        this.s3Client = s3Client;
    }

//    @org.springframework.scheduling.annotation.Scheduled(fixedRate = 60 * 60)
//    public void deletePaste() {
//
//        var urls = jdbcTemplate.queryForList("SELECT url FROM paste WHERE expires < now()", String.class);
//
//        urls.forEach(url -> {
//            try {
//                s3Client.deleteObject(builder -> {
//                    builder.bucket(bucketName).key(url).build();
//                });
//            } catch (Exception e) {
//                System.out.println(url);
//                e.printStackTrace();
//            }
//        });
//
//        jdbcTemplate.update("DELETE FROM paste WHERE expires < now()");
//    }

    @org.springframework.scheduling.annotation.Scheduled(fixedRate = 60 * 60)
    public void deletePaste() {
        jdbcTemplate.update("DELETE FROM data WHERE url IN (SELECT url FROM paste WHERE expires < now())");
        jdbcTemplate.update("DELETE FROM paste WHERE expires < now()");
    }

}
