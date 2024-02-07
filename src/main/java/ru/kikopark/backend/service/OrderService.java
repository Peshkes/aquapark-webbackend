package ru.kikopark.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.kikopark.backend.configs.JwtRequestFilter;
import ru.kikopark.backend.model.order.*;
import ru.kikopark.backend.persistence.order.entities.*;
import ru.kikopark.backend.persistence.order.repositories.*;
import ru.kikopark.backend.utils.PrintService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class OrderService {

    OrdersRepository ordersRepository;
    OrderItemsRepository orderItemsRepository;
    StatusesRepository statusesRepository;
    TicketsRepository ticketsRepository;
    TypesRepository typesRepository;
    TimeRepository timeRepository;
    private static final Logger logger = Logger.getLogger(OrderService.class.getName());

    public OrderService(OrdersRepository ordersRepository, OrderItemsRepository orderItemsRepository, StatusesRepository statusesRepository, TicketsRepository ticketsRepository, TypesRepository typesRepository, TimeRepository timeRepository) {
        this.ordersRepository = ordersRepository;
        this.orderItemsRepository = orderItemsRepository;
        this.statusesRepository = statusesRepository;
        this.ticketsRepository = ticketsRepository;
        this.typesRepository = typesRepository;
        this.timeRepository = timeRepository;
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
    public Optional<TicketsResponse[]> getTickets() {
        try {
            List<TypeEntity> typesList = typesRepository.findAll();

            if (typesList.isEmpty()) {
                return Optional.empty();
            }

            TicketsResponse[] ticketsResponses = typesList.stream()
                    .map(type -> {
                        TicketsResponse ticketsResponse = new TicketsResponse();
                        ticketsResponse.setType(type.getName());
                        ticketsResponse.setDescription(type.getDescription());

                        List<TicketEntity> ticketEntities = ticketsRepository.getTicketEntitiesByTypeId(type.getTypeId());

                        if (!ticketEntities.isEmpty()) {
                            List<Ticket> ticketsList = ticketEntities.stream()
                                    .map(ticketEntity -> {
                                        TimeEntity timeEntity = timeRepository.getTimeEntityByTimeId(ticketEntity.getTimeId());
                                        return new Ticket(
                                                ticketEntity.getTicketId(),
                                                ticketEntity.getName(),
                                                ticketEntity.getPrice(),
                                                timeEntity != null ? timeEntity.getMinutes() : null
                                        );
                                    })
                                    .toList();

                            Ticket[] ticketsArray = ticketsList.toArray(new Ticket[0]);
                            ticketsResponse.setTickets(ticketsArray);
                        }

                        return ticketsResponse;
                    })
                    .toArray(TicketsResponse[]::new);

            return Optional.of(ticketsResponses);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
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
