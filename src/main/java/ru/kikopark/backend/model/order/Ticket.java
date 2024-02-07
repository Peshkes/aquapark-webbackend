package ru.kikopark.backend.model.order;

public class Ticket {
    private Integer id;
    private String Name;
    private Integer price;
    private Integer time;

    public Ticket(Integer id, String name, Integer price, Integer time) {
        this.id = id;
        Name = name;
        this.price = price;
        this.time = time;
    }

    public Ticket() {
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return Name;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getTime() {
        return time;
    }
}
