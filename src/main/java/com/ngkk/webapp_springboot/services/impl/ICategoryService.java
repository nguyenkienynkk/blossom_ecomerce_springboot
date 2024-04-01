package com.ngkk.webapp_springboot.services.impl;

import com.ngkk.webapp_springboot.dtos.CategoryDTO;
import com.ngkk.webapp_springboot.models.Category;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ICategoryService {
    Category createCategory(CategoryDTO category);

    Category getCategoryById(long id);

    List<Category> getAllCategories();

    Category updateCategory(long categoryId, CategoryDTO category);

    void deleteCategory(long id);
}
