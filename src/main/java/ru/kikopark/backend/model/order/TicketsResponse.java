package ru.kikopark.backend.model.order;

public class TicketsResponse {

    private String type;
    private String description;
    private Ticket[] tickets;

    public TicketsResponse(String type, String description, Ticket[] tickets) {
        this.type = type;
        this.description = description;
        this.tickets = tickets;
    }

    public TicketsResponse(){}

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTickets(Ticket[] tickets) {
        this.tickets = tickets;
    }

    public Ticket[] getTickets() {
        return tickets;
    }
}


