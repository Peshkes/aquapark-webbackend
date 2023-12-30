package ru.kikopark.backend.model.order;

public class TicketRequest {
    Integer ticketId;
    Integer count;

    public TicketRequest(Integer ticketId, Integer count) {
        this.ticketId = ticketId;
        this.count = count;
    }

    public Integer getTicketId() {
        return ticketId;
    }

    public Integer getCount() {
        return count;
    }
}
