package ru.kikopark.backend.modules.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private UUID orderId;
    private String status;
    private Timestamp datePaid;
    private Timestamp dateChanged;
    private UUID institutionId;
    private double sum;
}
