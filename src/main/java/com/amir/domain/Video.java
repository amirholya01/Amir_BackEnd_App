package com.amir.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "videos")
public class Video {
    @Id
    private String id;
    private String title;
    private double size;
    private String url;
    private Date created;
    private Date modified;
    private Category category;
    private User user;

    public Video() {
    }

    public Video(String title, double size, String url, User user) {
        this.title = title;
        this.size = size;
        this.url = url;
        this.user = user;
    }

    public Video(String title, double size, String url, Category category, User user) {
        this.title = title;
        this.size = size;
        this.url = url;
        this.category = category;
        this.user = user;
    }
}
