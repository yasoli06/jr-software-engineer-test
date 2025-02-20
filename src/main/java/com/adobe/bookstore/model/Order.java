package com.adobe.bookstore.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name ="orders")
public class Order {

    @Id
    private String id;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "status", nullable = false)
    private String status;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private List<OrderItem> items;

    public Order(){
        this.id = UUID.randomUUID().toString();
        this.orderDate = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}
