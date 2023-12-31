package ru.kikopark.backend.model.order;

import java.util.List;

public class TicketsByOrderResponse {
    private String statusName;
    private List<TicketRequest> tickets;

    public TicketsByOrderResponse(String statusName, List<TicketRequest> tickets) {
        this.statusName = statusName;
        this.tickets = tickets;
    }
    public  TicketsByOrderResponse(){};

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public List<TicketRequest> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketRequest> tickets) {
        this.tickets = tickets;
    }
}
