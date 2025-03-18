package com.dacnguyen.ecommerce.dto.request;

import lombok.Data;

@Data
public class OrderItemRequest {

    private long productId;
    private int quantity;
}
