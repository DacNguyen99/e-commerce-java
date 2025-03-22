package com.dacnguyen.ecommerce.service.interf;

import com.dacnguyen.ecommerce.dto.general.CategoryDto;
import com.dacnguyen.ecommerce.dto.response.Response;

public interface CategoryService {

    Response createCategory(CategoryDto categoryRequest);

    Response updateCategory(Long categoryId, CategoryDto categoryRequest);

    Response getAllCategories();

    Response getCategoryById(Long categoryId);

    Response deleteCategory(Long categoryId);
}
