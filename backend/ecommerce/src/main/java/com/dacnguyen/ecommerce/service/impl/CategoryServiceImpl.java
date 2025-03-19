package com.dacnguyen.ecommerce.service.impl;

import com.dacnguyen.ecommerce.dto.general.CategoryDto;
import com.dacnguyen.ecommerce.dto.response.Response;
import com.dacnguyen.ecommerce.entity.Category;
import com.dacnguyen.ecommerce.exception.NotFoundException;
import com.dacnguyen.ecommerce.mapper.EntityToDtoMapper;
import com.dacnguyen.ecommerce.repository.CategoryRepository;
import com.dacnguyen.ecommerce.service.interf.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EntityToDtoMapper entityToDtoMapper;

    @Override
    public Response createCategory(CategoryDto categoryRequest) {
        Category category = new Category();
        category.setName(categoryRequest.getName());
        categoryRepository.save(category);

        return Response.builder()
                .status(200)
                .message("Category created!")
                .build();
    }

    @Override
    public Response updateCategory(long categoryId, CategoryDto categoryRequest) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found!"));

        category.setName(categoryRequest.getName());
        categoryRepository.save(category);

        return Response.builder()
                .status(200)
                .message("Category updated!")
                .build();
    }

    @Override
    public Response getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDto> categoryDtoList = categories
                .stream().map(entityToDtoMapper::categoryToCategoryDto).toList();

        return Response.builder()
                .status(200)
                .categoryList(categoryDtoList)
                .build();
    }

    @Override
    public Response getCategoryById(long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found!"));
        CategoryDto categoryDto = entityToDtoMapper.categoryToCategoryDto(category);

        return Response.builder()
                .status(200)
                .category(categoryDto)
                .build();
    }

    @Override
    public Response deleteCategory(long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found!"));
        categoryRepository.delete(category);

        return Response.builder()
                .status(200)
                .message("Category deleted!")
                .build();
    }
}
