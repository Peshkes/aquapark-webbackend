package ru.kikopark.backend.modules.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {
    private InstituteTicketCartItemRequest[] tickets;
    private String email;
    private String name;
    private double sum;
}
