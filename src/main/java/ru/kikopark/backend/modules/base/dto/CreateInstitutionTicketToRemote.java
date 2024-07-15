package ru.kikopark.backend.modules.base.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateInstitutionTicketToRemote {
    private UUID institutionId;
    private Integer ticketId;
    private Integer price;
    private Double extraValue;
    private Integer extraInterval;
}
