package ru.kikopark.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import ru.kikopark.backend.configs.JwtRequestFilter;
import ru.kikopark.backend.model.order.OrderRequest;
import ru.kikopark.backend.model.order.OrderStatus;
import ru.kikopark.backend.model.order.TicketRequest;
import ru.kikopark.backend.model.order.TicketsByOrderResponse;
import ru.kikopark.backend.persistence.order.entities.OrderEntity;
import ru.kikopark.backend.persistence.order.entities.OrderItemEntity;
import ru.kikopark.backend.persistence.order.repositories.OrderItemsRepository;
import ru.kikopark.backend.persistence.order.repositories.OrdersRepository;
import ru.kikopark.backend.persistence.order.repositories.StatusesRepository;
import ru.kikopark.backend.utils.PrintService;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    OrdersRepository ordersRepository;
    OrderItemsRepository orderItemsRepository;
    StatusesRepository statusesRepository;

    @Autowired
    public OrderService(OrdersRepository ordersRepository, OrderItemsRepository orderItemsRepository, StatusesRepository statusesRepository) {
        this.ordersRepository = ordersRepository;
        this.orderItemsRepository = orderItemsRepository;
        this.statusesRepository = statusesRepository;
    }

    public Optional<TicketsByOrderResponse> getTicketsByOrder(Integer id) {
        Optional<OrderEntity> orderEntity = Optional.ofNullable(ordersRepository.getOrderEntityByOrderId(id));
        if (orderEntity.isPresent()) {
            List<OrderItemEntity> orderItemEntities = orderItemsRepository.getOrderItemEntitiesByOrderId(id);
            List<TicketRequest> tickets = orderItemEntities.stream()
                    .map(item -> new TicketRequest(item.getTicketId(), item.getCount()))
                    .collect(Collectors.toList());
            return Optional.of(new TicketsByOrderResponse(ordersRepository.getStatusNameById(id), tickets));
        }
        return Optional.empty();
    }

    @Transactional
    public Optional<OrderEntity> addNewOrder(HttpEntity<String> order) {
        Optional<OrderEntity> addedOrder = Optional.empty();
        Optional<OrderRequest> orderRequest = jsonToOrder(order.getBody());
        if (orderRequest.isPresent()) {
            OrderEntity newOrder = orderEntityMapper(orderRequest.get());
            OrderEntity returnedOrder = ordersRepository.save(newOrder);
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

    @Transactional
    public Optional<OrderEntity> updateOrderStatus(Integer id, HttpEntity<String> httpEntity) {
        Optional<OrderEntity> updatedOrder = Optional.empty();
        Optional<OrderEntity> oldOrder = Optional.ofNullable(ordersRepository.getOrderEntityByOrderId(id));
        if (oldOrder.isEmpty()) {
            return updatedOrder;
        }
        Optional<OrderStatus> orderStatusFromHttp = jsonToOrderStatus(httpEntity.getBody());
        if (orderStatusFromHttp.isPresent()) {
            OrderEntity orderToBeUpdated = oldOrder.get();
            orderToBeUpdated.setStatusId(orderStatusFromHttp.get().getStatus());
            orderToBeUpdated.setDateChanged(new Timestamp(System.currentTimeMillis()));
            OrderEntity returnedOrder = ordersRepository.save(orderToBeUpdated);
            updatedOrder = Optional.of(returnedOrder);
        }
        return updatedOrder;
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

    private Optional<OrderStatus> jsonToOrderStatus(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        Optional<OrderStatus> orderStatus = Optional.empty();
        try {
            OrderStatus mappedOrderStatus = objectMapper.readValue(json, OrderStatus.class);
            orderStatus = Optional.of(mappedOrderStatus);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return orderStatus;
    }
}
