package com.amir.repository;

import com.amir.domain.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Repository interface for managing Category entities in the MongoDB database.
 */
@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {

    /**
     * Finds a category by its name.
     *
     * @param categoryName The name of the category to search for.
     * @return An optional containing the category if found, otherwise empty.
     */
    Optional<Category> findByName(String categoryName);
}
