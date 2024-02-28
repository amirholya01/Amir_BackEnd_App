package com.amir.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Represents a video entity in the application.
 */
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
    /**
     * The category of the video.
     */
    private Category category;
    /**
     * The user who uploaded the video.
     */
    private User user;

    public Video() {
    }

    /**
     * Constructor for the Video class with title, size, URL, and user.
     *
     * @param title The title of the video.
     * @param size  The size of the video.
     * @param url   The URL of the video.
     * @param user  The user who uploaded the video.
     */
    public Video(String title, double size, String url, User user) {
        this.title = title;
        this.size = size;
        this.url = url;
        this.user = user;
    }

    /**
     * Constructor for the Video class with title, size, URL, category, and user.
     *
     * @param title    The title of the video.
     * @param size     The size of the video.
     * @param url      The URL of the video.
     * @param category The category of the video.
     * @param user     The user who uploaded the video.
     */
    public Video(String title, double size, String url, Category category, User user) {
        this.title = title;
        this.size = size;
        this.url = url;
        this.category = category;
        this.user = user;
    }
}
