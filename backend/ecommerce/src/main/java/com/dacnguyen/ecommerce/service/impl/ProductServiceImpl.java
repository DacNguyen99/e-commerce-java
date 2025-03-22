package com.dacnguyen.ecommerce.service.impl;

import com.dacnguyen.ecommerce.dto.general.ProductDto;
import com.dacnguyen.ecommerce.dto.response.Response;
import com.dacnguyen.ecommerce.entity.Category;
import com.dacnguyen.ecommerce.entity.Product;
import com.dacnguyen.ecommerce.exception.NotFoundException;
import com.dacnguyen.ecommerce.mapper.EntityToDtoMapper;
import com.dacnguyen.ecommerce.repository.CategoryRepository;
import com.dacnguyen.ecommerce.repository.ProductRepository;
import com.dacnguyen.ecommerce.service.AwsS3Service;
import com.dacnguyen.ecommerce.service.interf.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final EntityToDtoMapper entityToDtoMapper;
    private final AwsS3Service awsS3Service;

    @Override
    public Response createProduct(Long categoryId, MultipartFile image,
                                  String name, String description, BigDecimal price) throws IOException {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found!"));
        String productImageUrl = awsS3Service.saveImageToS3(image);

        Product product = new Product();
        product.setCategory(category);
        product.setPrice(price);
        product.setName(name);
        product.setDescription(description);
        product.setImageUrl(productImageUrl);
        productRepository.save(product);

        return Response.builder()
                .status(200)
                .message("Product created!")
                .build();
    }

    @Override
    public Response updateProduct(Long productId, Long categoryId, MultipartFile photo,
                                  String name, String description, BigDecimal price) throws IOException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found!"));

        Category category = null;
        if (categoryId != null) {
            category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new NotFoundException("Category not found!"));
        }

        String productImageUrl = null;
        if (photo != null && !photo.isEmpty()) {
            productImageUrl = awsS3Service.saveImageToS3(photo);
        }

        if (category != null) product.setCategory(category);
        if (name != null) product.setName(name);
        if (price != null) product.setPrice(price);
        if (description != null) product.setDescription(description);
        if (productImageUrl != null) product.setImageUrl(productImageUrl);

        productRepository.save(product);

        return Response.builder()
                .status(200)
                .message("Product updated!")
                .build();
    }

    @Override
    public Response deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found!"));
        productRepository.delete(product);

        return Response.builder()
                .status(200)
                .message("Product deleted!")
                .build();
    }

    @Override
    public Response getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found!"));
        ProductDto productDto = entityToDtoMapper.productToProductDto(product);

        return Response.builder()
                .status(200)
                .product(productDto)
                .build();
    }

    @Override
    public Response getAllProducts() {
        List<ProductDto> products = productRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(entityToDtoMapper::productToProductDto).toList();

        return Response.builder()
                .status(200)
                .productList(products)
                .build();
    }

    @Override
    public Response getProductsByCategory(Long categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);

        if (products.isEmpty()) {
            throw new NotFoundException("No product found for this category!");
        }

        List<ProductDto> productDtoList = products.stream()
                .map(entityToDtoMapper::productToProductDto).toList();

        return Response.builder()
                .status(200)
                .productList(productDtoList)
                .build();
    }

    @Override
    public Response searchProduct(String searchValue) {
        List<Product> products = productRepository
                .findByNameContainingOrDescriptionContaining(searchValue, searchValue);

        if (products.isEmpty()) {
            throw new NotFoundException("No product found for this search!");
        }

        List<ProductDto> productDtoList = products.stream()
                .map(entityToDtoMapper::productToProductDto).toList();

        return Response.builder()
                .status(200)
                .productList(productDtoList)
                .build();
    }
}
