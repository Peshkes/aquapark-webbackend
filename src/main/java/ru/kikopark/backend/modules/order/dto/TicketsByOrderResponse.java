package ru.kikopark.backend.modules.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketsByOrderResponse {
    private String ticketType;
    private UUID orderItemId;
    private UUID institutionTicketsId;
    private Integer count;
}
