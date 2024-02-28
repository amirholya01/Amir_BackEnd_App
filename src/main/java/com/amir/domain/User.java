package com.amir.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * Represents a user entity in the application.
 */

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id; //The unique identifier for the user.

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 120)
    @JsonIgnore
    private String password;

    private Integer active;

    @DBRef
    private Set<Role> authorities = new HashSet<>(); //The roles assigned to the user.

    @DBRef
    private Set<Video> videos = new HashSet<>(); //The videos uploaded by the user

    private Date created;
    private Date modified;

    public User() {
    }


    /**
     * Constructor for the User class with username, email, and password.
     *
     * @param username The username of the user.
     * @param email    The email address of the user.
     * @param password The password of the user.
     */
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }


    /**
     * Constructor for the User class with all fields.
     *
     * @param id       The unique identifier for the user.
     * @param username The username of the user.
     * @param email    The email address of the user.
     * @param password The password of the user.
     * @param active   The status indicating whether the user is active or not.
     */
    public User(String id, String username, String email, String password, Integer active) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.active = active;
    }
}
