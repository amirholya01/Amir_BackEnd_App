package com.amir.service;

import com.amir.domain.Category;
import com.amir.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing categories.
 */
@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;


    /**
     * Creates a new category.
     *
     * @param category The category object to be created.
     * @return The created category.
     */
    public Category create(Category category){
        return categoryRepository.save(category);
    }


    /**
     * Retrieves all categories.
     *
     * @return List of all categories.
     */
    public List<Category> findAllCategories(){
        return categoryRepository.findAll();
    }
}
