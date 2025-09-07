package in.aaditiya.billingsoftware.service.impl;

import in.aaditiya.billingsoftware.entity.OrderEntity;
import in.aaditiya.billingsoftware.entity.OrderItemEntity;
import in.aaditiya.billingsoftware.io.*;
import in.aaditiya.billingsoftware.repository.OrderEntityRepository;
import in.aaditiya.billingsoftware.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderEntityRepository orderEntityRepository;
    @Override
    public OrderResponse createOrder(OrderRequest request){
        OrderEntity newOrder = convertToOrderEntity(request);
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setStatus(newOrder.getPaymentMethod()== PaymentMethod.CASH ?
                PaymentDetails.PaymentStatus.COMPLETED : PaymentDetails.PaymentStatus.PENDING);
        newOrder.setPaymentDetails(paymentDetails);
        List<OrderItemEntity> orderItems =  request.getCartItems().stream()
                .map(this::convertToOrderItemEntity)
                .collect(Collectors.toList());
        newOrder.setItems(orderItems);
       newOrder =  orderEntityRepository.save(newOrder);
       return convertToResponse(newOrder);
    }
    private OrderEntity convertToOrderEntity(OrderRequest request){
        return OrderEntity.builder()
                .customerName(request.getCustomerName())
                .phoneNumber(request.getPhoneNumber())
                .subtotal(request.getSubtotal())
                .tax(request.getTax())
                .grandTotal((request.getGrandTotal()))
                .paymentMethod(PaymentMethod.valueOf(request.getPaymentMethod()))
                .build();

    }
    private OrderResponse convertToResponse(OrderEntity neworder){
        return OrderResponse.builder()
                .orderId(neworder.getOrderId())
                .customerName(neworder.getCustomerName())
                .phoneNumber(neworder.getPhoneNumber())
                .subtotal(neworder.getSubtotal())
                .tax(neworder.getTax())
                .grandTotal(neworder.getGrandTotal())
                .paymentMethod(neworder.getPaymentMethod())
                .items(neworder.getItems().stream()
                        .map(this::convertToItemResponse)
                        .collect(Collectors.toList()))
                .paymentDetails(neworder.getPaymentDetails())
                .createdAt(neworder.getCreatedAt())
                .build();
    }
    private OrderItemEntity convertToOrderItemEntity(OrderRequest.OrderItemRequest orderItemRequest){
                 return  OrderItemEntity.builder()
                          .itemId(orderItemRequest.getItemId())
                          .name(orderItemRequest.getName())
                          .price(orderItemRequest.getPrice())
                          .quantity(orderItemRequest.getQuantity())
                          .build();
    }
    private OrderResponse.OrderItemResponse convertToItemResponse(OrderItemEntity orderItemEntity){
       return  OrderResponse.OrderItemResponse.builder()
                .itemId(orderItemEntity.getItemId())
                .name(orderItemEntity.getName())
                .price(orderItemEntity.getPrice())
                .quantity(orderItemEntity.getQuantity())
                .build();
    }
    @Override
    public void deleteOrder(String orderId){
        OrderEntity existingOrder =  orderEntityRepository.findByOrderId((orderId))
                .orElseThrow(()-> new RuntimeException("Order not found"));
        orderEntityRepository.delete(existingOrder);
    }
    @Override
    public List<OrderResponse> getLatestOrders(){
        return orderEntityRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

    }
    @Override
    public OrderResponse verifyPayment(PaymentVerificationRequest request){
     OrderEntity existingOrder=    orderEntityRepository.findByOrderId(request.getOrderId())
                .orElseThrow(()-> new RuntimeException("Order not found"));
     if(!verifyRazorpaySignature(request.getRazorpayOrderId(), request.getRazorpayPaymentId(),request.getRazorpaySignature())){
         throw new RuntimeException("Payment verification failed");
     }
     PaymentDetails paymentDetails = existingOrder.getPaymentDetails();
     paymentDetails.setRazorpayOrderId(request.getRazorpayOrderId());
     paymentDetails.setRazorpayPaymentId(request.getRazorpayPaymentId());
     paymentDetails.setRazorpaySignature(request.getRazorpaySignature());
     paymentDetails.setStatus(PaymentDetails.PaymentStatus.COMPLETED);
     existingOrder =  orderEntityRepository.save(existingOrder);
     return convertToResponse(existingOrder);
    }

    @Override
    public Double sumSalesByDate(LocalDate date) {
        return orderEntityRepository.sumSalesByDate(date);
    }

    @Override
    public Long countByOrderDate(LocalDate date) {
        return orderEntityRepository.countByOrderDate(date);
    }

    @Override
    public List<OrderResponse> findRecentOrders() {
        return orderEntityRepository.findRecentOrders(PageRequest.of(0,5))
                .stream()
                .map(orderEntity -> convertToResponse(orderEntity))
                .collect(Collectors.toList());
    }

    private boolean verifyRazorpaySignature(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) {
        return true;
    }
}
