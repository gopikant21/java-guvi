package org.example.practicetest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.practicetest.config.WebMvcTestSecurityConfig;
import org.example.practicetest.controller.OrderController;
import org.example.practicetest.dto.order.ProcessOrderRequest;
import org.example.practicetest.entity.Merchant;
import org.example.practicetest.entity.OrderEntity;
import org.example.practicetest.entity.OrderStatus;
import org.example.practicetest.security.JwtService;
import org.example.practicetest.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@Import(WebMvcTestSecurityConfig.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @MockBean
    private JwtService jwtService;

    @Test
    void createShouldReturnCreatedOrder() throws Exception {
        OrderEntity request = order("ORD-1", OrderStatus.PENDING);
        OrderEntity saved = order("ORD-1", OrderStatus.PENDING);
        saved.setId(5L);
        when(orderService.create(any(OrderEntity.class))).thenReturn(saved);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5));
    }

    @Test
    void processShouldReturnProcessedOrder() throws Exception {
        OrderEntity processed = order("ORD-1", OrderStatus.PROCESSED);
        processed.setId(5L);
        when(orderService.process(new ProcessOrderRequest(5L))).thenReturn(processed);

        mockMvc.perform(post("/api/orders/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"orderId\":5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderStatus").value("PROCESSED"));
    }

    @Test
    void getAllShouldReturnOrders() throws Exception {
        when(orderService.getAll()).thenReturn(List.of(order("ORD-1", OrderStatus.PENDING)));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderNumber").value("ORD-1"));
    }

    private OrderEntity order(String orderNumber, OrderStatus status) {
        Merchant merchant = new Merchant();
        merchant.setId(1L);
        OrderEntity order = new OrderEntity();
        order.setOrderNumber(orderNumber);
        order.setOrderStatus(status);
        order.setTotalAmount(BigDecimal.valueOf(100));
        order.setMerchant(merchant);
        return order;
    }
}


