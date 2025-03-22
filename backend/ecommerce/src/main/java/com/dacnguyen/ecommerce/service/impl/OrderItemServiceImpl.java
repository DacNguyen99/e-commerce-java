package com.dacnguyen.ecommerce.service.impl;

import com.dacnguyen.ecommerce.dto.general.OrderItemDto;
import com.dacnguyen.ecommerce.dto.request.OrderRequest;
import com.dacnguyen.ecommerce.dto.response.Response;
import com.dacnguyen.ecommerce.entity.Order;
import com.dacnguyen.ecommerce.entity.OrderItem;
import com.dacnguyen.ecommerce.entity.Product;
import com.dacnguyen.ecommerce.entity.User;
import com.dacnguyen.ecommerce.enums.OrderStatus;
import com.dacnguyen.ecommerce.exception.NotFoundException;
import com.dacnguyen.ecommerce.mapper.EntityToDtoMapper;
import com.dacnguyen.ecommerce.repository.OrderItemRepository;
import com.dacnguyen.ecommerce.repository.OrderRepository;
import com.dacnguyen.ecommerce.repository.ProductRepository;
import com.dacnguyen.ecommerce.service.interf.OrderItemService;
import com.dacnguyen.ecommerce.service.interf.UserService;
import com.dacnguyen.ecommerce.specification.OrderItemSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final UserService userService;
    private final EntityToDtoMapper entityToDtoMapper;

    @Override
    public Response placeOrder(OrderRequest orderRequest) {
        User user = userService.getLoginUser();

        // map orderRequest to order entity
        List<OrderItem> orderItems = orderRequest.getItems().stream().map(orderItemRequest -> {
            Product product = productRepository.findById(orderItemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(orderItemRequest.getQuantity());
            orderItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(orderItemRequest.getQuantity())));
            orderItem.setStatus(OrderStatus.PENDING);
            orderItem.setUser(user);
            return orderItem;
        }).toList();

        // calculate total price
        BigDecimal totalPrice = orderRequest.getTotalPrice() != null
                && orderRequest.getTotalPrice().compareTo(BigDecimal.ZERO) > 0
                ? orderRequest.getTotalPrice()
                : orderItems.stream().map(OrderItem::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        // create order entity
        Order order = new Order();
        order.setTotalPrice(totalPrice);
        order.setOrderItemList(orderItems);

        // set order reference in each orderItems
        orderItems.forEach(orderItem -> orderItem.setOrder(order));

        // save order
        orderRepository.save(order);

        return Response.builder()
                .status(200)
                .message("Order Placed!")
                .build();
    }

    @Override
    public Response updateOrderItemStatus(Long orderItemId, String status) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new RuntimeException("Order item not found!"));

        orderItem.setStatus(OrderStatus.valueOf(status.toUpperCase()));
        orderItemRepository.save(orderItem);

        return Response.builder()
                .status(200)
                .message("Order item status updated!")
                .build();
    }

    @Override
    public Response filterOrderItems(OrderStatus status, LocalDateTime startDate,
                                     LocalDateTime endDate, Long itemId, Pageable pageable) {
        Specification<OrderItem> spec = Specification.where(OrderItemSpecification.hasStatus(status))
                .and(OrderItemSpecification.createdBetween(startDate, endDate))
                .and(OrderItemSpecification.hasItemId(itemId));

        Page<OrderItem> orderItemPage = orderItemRepository.findAll(spec, pageable);

        if (orderItemPage.isEmpty()) {
            throw new NotFoundException("No Order found!");
        }

        List<OrderItemDto> orderItemDtoList = orderItemPage.getContent().stream()
                .map(entityToDtoMapper::orderItemToOrderItemDtoWithProductAndUser).toList();

        return Response.builder()
                .status(200)
                .orderItemList(orderItemDtoList)
                .totalPage(orderItemPage.getTotalPages())
                .totalElements(orderItemPage.getTotalElements())
                .build();
    }
}
