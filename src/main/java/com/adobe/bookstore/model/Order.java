package com.adobe.bookstore.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@JsonIgnoreProperties(value = {"id", "orderDate", "status"}, allowGetters = true)
@Entity
@Table(name ="orders")
public class Order {

    @Id
    private String id;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private List<OrderItem> items;

    public Order(){
        this.id = UUID.randomUUID().toString();
        this.orderDate = LocalDateTime.now();
        this.status = OrderStatus.PENDING;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

}
