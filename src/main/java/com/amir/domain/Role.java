package com.amir.domain;

import com.amir.enums.ERole;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a role entity in the application.
 */
@Data
@Document(collection = "roles")
public class Role {
    /**
     * The unique identifier for the role.
     */
    @Id
    private String id;
    /**
     * The name of the role.
     */
    private ERole name;

    /**
     * Default constructor for the Role class.
     */
    public Role() {
    }


    /**
     * Constructor for the Role class with a given name.
     *
     * @param name The name of the role.
     */
    public Role(ERole name) {
        this.name = name;
    }
}
