package com.amir.repository;

import com.amir.domain.Role;
import com.amir.enums.ERole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Repository interface for managing Role entities in the MongoDB database.
 */
@Repository
public interface RoleRepository extends MongoRepository<Role, String> {
    /**
     * Finds a role by its name.
     *
     * @param name The name of the role to search for.
     * @return An optional containing the role if found, otherwise empty.
     */
    Optional<Role> findByName(ERole name);
}
