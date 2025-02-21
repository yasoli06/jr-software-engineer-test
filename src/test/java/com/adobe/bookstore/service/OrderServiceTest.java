package com.adobe.bookstore.service;

import com.adobe.bookstore.model.BookStock;
import com.adobe.bookstore.model.Order;
import com.adobe.bookstore.model.OrderItem;
import com.adobe.bookstore.model.OrderStatus;
import com.adobe.bookstore.repository.BookStockRepository;
import com.adobe.bookstore.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private BookStockRepository bookStockRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void testProcessOrderSuccess() {
        Order order = new Order();
        OrderItem item = new OrderItem();
        item.setBookId("book1");
        item.setQuantity(1);
        order.setItems(Collections.singletonList(item));

        BookStock stock = new BookStock();
        stock.setId("book1");
        stock.setName("Libro de ejemplo");
        stock.setQuantity(10);

        when(bookStockRepository.findById("book1")).thenReturn(Optional.of(stock));
        when(orderRepository.save(order)).thenReturn(order);

        Order processedOrder = orderService.processOrder(order);

        assertEquals(OrderStatus.SUCCESS, processedOrder.getStatus());
        verify(bookStockRepository, times(2)).findById("book1");
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    public void testProcessOrderInsufficientStock() {
        Order order = new Order();
        OrderItem item = new OrderItem();
        item.setBookId("book1");
        item.setQuantity(5);
        order.setItems(Collections.singletonList(item));

        BookStock stock = new BookStock();
        stock.setId("book1");
        stock.setName("Sample Book");
        stock.setQuantity(2);

        when(bookStockRepository.findById("book1")).thenReturn(Optional.of(stock));
        when(orderRepository.save(order)).thenReturn(order);


        Order processedOrder = orderService.processOrder(order);
        assertEquals(OrderStatus.FAILED, processedOrder.getStatus());
        verify(bookStockRepository, times(1)).findById("book1");
        verify(orderRepository, times(1)).save(order);
    }
}