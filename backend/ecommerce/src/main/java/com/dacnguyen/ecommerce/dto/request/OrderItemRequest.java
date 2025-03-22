package com.dacnguyen.ecommerce.dto.request;

import lombok.Data;

@Data
public class OrderItemRequest {

    private Long productId;
    private int quantity;
}
