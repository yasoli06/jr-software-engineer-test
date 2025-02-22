package com.adobe.bookstore.resource;

import com.adobe.bookstore.model.Order;
import com.adobe.bookstore.model.OrderItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateOrder() throws Exception {
        String validBookId = "ae1666d6-6100-4ef0-9037-b45dd0d5bb0e";
        Order order = new Order();
        OrderItem item = new OrderItem();
        item.setBookId(validBookId);
        item.setQuantity(1);
        order.setItems(Collections.singletonList(item));

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.notNullValue()));
    }

    @Test
    public void testGetOrders() throws Exception {
        String validBookId = "ae1666d6-6100-4ef0-9037-b45dd0d5bb0e";
        Order order = new Order();
        OrderItem item = new OrderItem();
        item.setBookId(validBookId);
        item.setQuantity(1);
        order.setItems(Collections.singletonList(item));

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }
    @Test
    public void testOrderContainsListOfBooksAndQuantity() throws Exception {
        // IDs válidos según import.sql
        String validBookId1 = "ae1666d6-6100-4ef0-9037-b45dd0d5bb0e";
        String validBookId2 = "22d580fc-d02e-4f70-9980-f9693c18f6e0";

        // Creamos una orden con dos ítems
        Order order = new Order();
        OrderItem item1 = new OrderItem();
        item1.setBookId(validBookId1);
        item1.setQuantity(2);

        OrderItem item2 = new OrderItem();
        item2.setBookId(validBookId2);
        item2.setQuantity(3);

        order.setItems(Arrays.asList(item1, item2));

        // Creamos la orden usando POST (esto solo retorna el ID)
        String orderId = mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Ahora, usamos el endpoint GET para obtener la lista de órdenes
        // y verificamos que la orden recién creada contenga el array "items" con los valores esperados.
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                // Buscamos la orden con el ID devuelto
                .andExpect(jsonPath("$[?(@.id=='" + orderId + "')].items").exists())
                .andExpect(jsonPath("$[?(@.id=='" + orderId + "')].items[0].bookId").value(validBookId1))
                .andExpect(jsonPath("$[?(@.id=='" + orderId + "')].items[0].quantity").value(2))
                .andExpect(jsonPath("$[?(@.id=='" + orderId + "')].items[1].bookId").value(validBookId2))
                .andExpect(jsonPath("$[?(@.id=='" + orderId + "')].items[1].quantity").value(3));
    }
}
