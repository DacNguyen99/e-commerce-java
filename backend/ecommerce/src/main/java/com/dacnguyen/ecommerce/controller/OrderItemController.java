package com.dacnguyen.ecommerce.controller;

import com.dacnguyen.ecommerce.dto.request.OrderRequest;
import com.dacnguyen.ecommerce.dto.response.Response;
import com.dacnguyen.ecommerce.enums.OrderStatus;
import com.dacnguyen.ecommerce.service.interf.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderItemController {

    private final OrderItemService orderItemService;

    @PostMapping("/place")
    public ResponseEntity<Response> placeOrder(@RequestBody OrderRequest orderRequest) {
        return ResponseEntity.ok(orderItemService.placeOrder(orderRequest));
    }

    @PostMapping("/update-item-status/{orderItemId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateOrderItemStatus(@PathVariable Long orderItemId, @RequestParam String status) {
        return ResponseEntity.ok(orderItemService.updateOrderItemStatus(orderItemId, status));
    }

    public ResponseEntity<Response> filterOrderItems(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long itemId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000") int size
            ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        OrderStatus orderStatus = status != null ? OrderStatus.valueOf(status.toUpperCase()) : null;

        return ResponseEntity.ok(orderItemService.filterOrderItems(orderStatus, startDate, endDate, itemId, pageable));
    }
}
