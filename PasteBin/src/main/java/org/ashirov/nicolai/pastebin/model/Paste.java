package org.ashirov.nicolai.pastebin.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Paste {
    @Id
    private int id;

    private String content;
    private String url;
    private LocalDateTime created;
    private LocalDateTime expires;
}
