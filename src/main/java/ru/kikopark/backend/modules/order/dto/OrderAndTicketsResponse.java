package ru.kikopark.backend.modules.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.kikopark.backend.modules.order.entities.OrderEntity;

import java.lang.reflect.Array;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class OrderAndTicketsResponse {
    private OrderDto order;
    private TicketsByOrderResponse[] orderItems;
}
