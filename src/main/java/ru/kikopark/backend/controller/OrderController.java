package ru.kikopark.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.kikopark.backend.model.order.TicketsByOrderResponse;
import ru.kikopark.backend.persistence.order.entities.OrderEntity;
import ru.kikopark.backend.service.OrderService;

import java.util.Optional;

@RestController
public class OrderController {
    OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    @GetMapping("/employee/order")
    public Optional<TicketsByOrderResponse> getTicketsByOrder(@RequestParam Integer id){
        return orderService.getTicketsByOrder(id);
    }

    @GetMapping("/admin/order")
    public Optional<TicketsByOrderResponse> getAdminTicketsByOrder(@RequestParam Integer id){
        return orderService.getTicketsByOrder(id);
    }

    @PostMapping("/guest/create-order")
    public ResponseEntity<Integer> addOrder(HttpEntity<String> httpEntity){
        Optional<OrderEntity> insertionSuccess = orderService.addNewOrder(httpEntity);
        Integer orderId = null;
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        if (insertionSuccess.isPresent()){
            orderId = insertionSuccess.get().getOrderId();
            httpStatus = HttpStatus.OK;
        }
        return new ResponseEntity<>(orderId, httpStatus);
    }

    @PutMapping("/admin/update-order-status")
    public ResponseEntity<Integer> updateOrderStatus(@RequestParam Integer id, HttpEntity<String> httpEntity){
        Optional<OrderEntity> updateSuccess = orderService.updateOrderStatus(id, httpEntity);
        Integer orderId = null;
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        if (updateSuccess.isPresent()){
            orderId = updateSuccess.get().getOrderId();
            httpStatus = HttpStatus.OK;
        }
        return new ResponseEntity<>(orderId, httpStatus);
    }
}
