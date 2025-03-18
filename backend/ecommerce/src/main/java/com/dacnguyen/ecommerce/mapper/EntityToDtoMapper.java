package com.dacnguyen.ecommerce.mapper;

import com.dacnguyen.ecommerce.dto.general.*;
import com.dacnguyen.ecommerce.entity.*;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class EntityToDtoMapper {

    // user to userDto
    public UserDto userToUserDto(User user) {
        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole().name());
        userDto.setName(user.getName());

        return userDto;
    }

    // address to addressDto
    public AddressDto addressToAddressDto(Address address) {
        AddressDto addressDto = new AddressDto();
        addressDto.setId(address.getId());
        addressDto.setStreet(address.getStreet());
        addressDto.setCity(address.getCity());
        addressDto.setState(address.getState());
        addressDto.setZipCode(address.getZipCode());
        addressDto.setCountry(address.getCountry());
        return addressDto;
    }

    // category to categoryDto
    public CategoryDto categoryToCategoryDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }

    // orderItem to orderItemDto
    public OrderItemDto orderItemToOrderItemDto(OrderItem orderItem) {
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setId(orderItem.getId());
        orderItemDto.setQuantity(orderItem.getQuantity());
        orderItemDto.setPrice(orderItem.getPrice());
        orderItemDto.setStatus(orderItem.getStatus().name());
        orderItemDto.setCreatedAt(orderItem.getCreatedAt());
        return orderItemDto;
    }

    // product to productDto
    public ProductDto productToProductDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setImageUrl(product.getImageUrl());
        return productDto;
    }

    // user to userDto with address
    public UserDto userToUserDtoWithAddress(User user) {
        UserDto userDto = userToUserDto(user);
        if (user.getAddress() != null) {
            AddressDto addressDto = addressToAddressDto(user.getAddress());
            userDto.setAddress(addressDto);
        }
        return userDto;
    }

    // orderItem to orderItemDto with Product
    public OrderItemDto orderItemToOrderItemDtoWithProduct(OrderItem orderItem) {
        OrderItemDto orderItemDto = orderItemToOrderItemDto(orderItem);
        if (orderItem.getProduct() != null) {
            ProductDto productDto = productToProductDto(orderItem.getProduct());
            orderItemDto.setProduct(productDto);
        }
        return orderItemDto;
    }

    // orderItem to orderItemDto with Product and User
    public OrderItemDto orderItemToOrderItemDtoWithProductAndUser(OrderItem orderItem) {
        OrderItemDto orderItemDto = orderItemToOrderItemDtoWithProduct(orderItem);

        if (orderItem.getUser() != null) {
            UserDto userDto = userToUserDtoWithAddress(orderItem.getUser());
            orderItemDto.setUser(userDto);
        }
        return orderItemDto;
    }

    // user to userDto with address and orderItem history
    public UserDto userToUserDtoWithAddressAndOrderHistory(User user) {
        UserDto userDto = userToUserDtoWithAddress(user);

        if (user.getOrderItemList() != null && !user.getOrderItemList().isEmpty()) {
            userDto.setOrderItemList(user.getOrderItemList()
                    .stream()
                    .map(this::orderItemToOrderItemDtoWithProduct)
                    .collect(Collectors.toList())
            );
        }

        return userDto;
    }
}
