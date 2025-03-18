package com.dacnguyen.ecommerce.repository;

import com.dacnguyen.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryId(long categoryId);

    List<Product> findByNameContainingOrDescriptionContaining(String name, String description);
}
