package com.ngkk.webapp_springboot.services;

import com.ngkk.webapp_springboot.dtos.CategoryDTO;
import com.ngkk.webapp_springboot.models.Category;
import com.ngkk.webapp_springboot.repositories.CategoryRepository;
import com.ngkk.webapp_springboot.services.impl.ICategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService implements ICategoryService {

    CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Category createCategory(CategoryDTO categoryDTO) {
        Category newCategory = Category
                .builder()
                .name(categoryDTO.getName())
                .build();
        return categoryRepository.save(newCategory);
    }

    @Override
    public Category getCategoryById(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional
    public Category updateCategory(long categoryId, CategoryDTO categoryDTO) {
        Category existCategory = getCategoryById(categoryId);
        existCategory.setName(categoryDTO.getName());
        categoryRepository.save(existCategory);
        return existCategory;

    }

    @Override
    @Transactional
    public void deleteCategory(long id) {
        categoryRepository.deleteById(id);
    }
}
