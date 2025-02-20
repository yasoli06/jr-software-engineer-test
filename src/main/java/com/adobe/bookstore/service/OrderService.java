package com.adobe.bookstore.service;

import com.adobe.bookstore.model.BookStock;
import com.adobe.bookstore.model.Order;
import com.adobe.bookstore.model.OrderItem;
import com.adobe.bookstore.repository.BookStockRepository;
import com.adobe.bookstore.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final BookStockRepository bookStockRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, BookStockRepository bookStockRepository) {
        this.orderRepository = orderRepository;
        this.bookStockRepository = bookStockRepository;
    }

    @Transactional
    public Order processOrder(Order order) {
        for (OrderItem item : order.getItems()) {
            BookStock stock = bookStockRepository.findById(item.getBookId())
                    .orElseThrow(() -> new RuntimeException("Book not found: " + item.getBookId()));

            if (stock.getQuantity() < item.getQuantity()) {
                throw new RuntimeException("Not enough stock for book: " + item.getBookId());
            }
        }
        order.setStatus("SUCCESS");
        Order savedOrder = orderRepository.save(order);
        updateStockAsync(order.getItems());

        return savedOrder;
    }
    @Async
    public void updateStockAsync(List<OrderItem> items) {
        for (OrderItem item : items) {
            bookStockRepository.findById(item.getBookId()).ifPresent(stock -> {
                stock.setQuantity(stock.getQuantity() - item.getQuantity());
                bookStockRepository.save(stock);
            });
        }
    }
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
