package com.amir.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;


/**
 * Represents a category entity in the application.
 */
@Data
@Document(collection = "category")
public class Category {
    @Id
    private String id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;

    @DBRef
    private Set<Video> videos = new HashSet<>(); //The videos associated with the category
}
