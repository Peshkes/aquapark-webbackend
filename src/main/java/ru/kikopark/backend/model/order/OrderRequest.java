package ru.kikopark.backend.model.order;

public class OrderRequest {
    private String fullName;
    private Integer price;
    private TicketRequest[] tickets;

    public String getFullName() {
        return fullName;
    }

    public Integer getPrice() {
        return price;
    }

    public TicketRequest[] getTickets() {
        return tickets;
    }
}
