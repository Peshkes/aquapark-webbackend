package ru.kikopark.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kikopark.backend.model.authentication.AccountResponse;
import ru.kikopark.backend.persistence.authentication.entities.AccountEntity;
import ru.kikopark.backend.persistence.authentication.entities.RoleEntity;
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

    @PostMapping("/create-order")
    public ResponseEntity<Integer> addAccount(HttpEntity<String> httpEntity){
        Optional<OrderEntity> insertionSuccess = orderService.addNewOrder(httpEntity);
        Integer orderId = null;
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        if (insertionSuccess.isPresent()){
            orderId = insertionSuccess.get().getOrderId();
            httpStatus = HttpStatus.OK;
        }
        return new ResponseEntity<>(orderId, httpStatus);
    }
}
