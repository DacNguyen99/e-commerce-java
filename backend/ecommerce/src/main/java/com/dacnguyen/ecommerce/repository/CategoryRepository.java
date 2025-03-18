package com.dacnguyen.ecommerce.repository;

import com.dacnguyen.ecommerce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
