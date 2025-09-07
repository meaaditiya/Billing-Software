package in.aaditiya.billingsoftware.service;

import in.aaditiya.billingsoftware.io.OrderRequest;
import in.aaditiya.billingsoftware.io.OrderResponse;
import in.aaditiya.billingsoftware.io.PaymentVerificationRequest;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderRequest request);
    void deleteOrder(String orderId);
    List<OrderResponse> getLatestOrders();
    OrderResponse verifyPayment(PaymentVerificationRequest request);
    Double sumSalesByDate(LocalDate date);
    Long countByOrderDate(LocalDate date);
    List<OrderResponse> findRecentOrders();
}
