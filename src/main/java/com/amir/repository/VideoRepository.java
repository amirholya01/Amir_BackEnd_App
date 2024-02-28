package com.amir.repository;

import com.amir.domain.Video;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Repository interface for managing Video entities in the MongoDB database.
 */
@Repository
public interface VideoRepository extends MongoRepository<Video, String> {

    /**
     * Finds videos by the username of the user who uploaded them.
     *
     * @param username The username of the user.
     * @return A list of videos uploaded by the user.
     */

    List<Video> findByUserUsername(String user);

    /**
     * Finds videos by their URL.
     *
     * @param url The URL of the video.
     * @return An optional containing a list of videos if found, otherwise empty.
     */
    Optional<List<Video>> findByUrl(String url);

    /**
     * Finds videos by the category ID they belong to.
     *
     * @param categoryId The ID of the category.
     * @return A list of videos belonging to the category.
     */
    List<Video> findByCategoryId(String categoryId);
}
