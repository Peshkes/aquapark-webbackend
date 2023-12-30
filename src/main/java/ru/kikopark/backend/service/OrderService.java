package ru.kikopark.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import ru.kikopark.backend.model.order.OrderRequest;
import ru.kikopark.backend.model.order.TicketRequest;
import ru.kikopark.backend.persistence.order.entities.OrderEntity;
import ru.kikopark.backend.persistence.order.entities.OrderItemEntity;
import ru.kikopark.backend.persistence.order.repositories.OrderItemsRepository;
import ru.kikopark.backend.persistence.order.repositories.OrderRepository;
import ru.kikopark.backend.utils.PrintService;

import java.util.Optional;

@Service
public class OrderService {

    OrderRepository orderRepository;
    OrderItemsRepository orderItemsRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderItemsRepository orderItemsRepository) {
        this.orderRepository = orderRepository;
        this.orderItemsRepository = orderItemsRepository;
    }

    public Optional<OrderEntity> addNewOrder(HttpEntity<String> order) {
        Optional<OrderEntity> addedOrder = Optional.empty();
        Optional<OrderRequest> orderRequest = jsonToOrder(order.getBody());
        if (orderRequest.isPresent()) {
            OrderEntity newOrder = orderEntityMapper(orderRequest.get());
            OrderEntity returnedOrder = orderRepository.save(newOrder);
            addedOrder = Optional.of(returnedOrder);
            for (TicketRequest ticket : orderRequest.get().getTickets()) {
                OrderItemEntity newOrderItem = orderItemEntityMapper(returnedOrder.getOrderId(), ticket);
                orderItemsRepository.save(newOrderItem);
                try {
                    PrintService.createTicket(returnedOrder.getOrderId().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return addedOrder;
    }

//    utils
    private OrderEntity orderEntityMapper(OrderRequest orderRequest) {
        return new OrderEntity(orderRequest.getFullName(), orderRequest.getPrice());
    }

    private OrderItemEntity orderItemEntityMapper(Integer orderId, TicketRequest ticketRequest) {
        return new OrderItemEntity(orderId, ticketRequest.getTicketId(), ticketRequest.getCount());
    }

    private Optional<OrderRequest> jsonToOrder(String json) {
        ObjectMapper mapper = new ObjectMapper();
        Optional<OrderRequest> order = Optional.empty();
        try {
            OrderRequest mappedOrder = mapper.readValue(json, OrderRequest.class);
            order = Optional.of(mappedOrder);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return order;
    }
}
