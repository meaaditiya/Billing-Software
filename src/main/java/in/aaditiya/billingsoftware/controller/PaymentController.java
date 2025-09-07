package in.aaditiya.billingsoftware.controller;

import com.razorpay.RazorpayException;
import in.aaditiya.billingsoftware.io.OrderResponse;
import in.aaditiya.billingsoftware.io.PaymentRequest;
import in.aaditiya.billingsoftware.io.PaymentVerificationRequest;
import in.aaditiya.billingsoftware.io.RazorPayOrderResponse;
import in.aaditiya.billingsoftware.service.OrderService;
import in.aaditiya.billingsoftware.service.RazorpayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final RazorpayService razorpayService;
    private final OrderService orderService;
    @PostMapping("/create-order")
    @ResponseStatus(HttpStatus.CREATED)
    public RazorPayOrderResponse createRazorpayOrder(@RequestBody PaymentRequest request) throws RazorpayException {
        return razorpayService.createOrder(request.getAmount(), request.getCurrency());
    }
    @PostMapping("/verify")
    public OrderResponse verifyPayment(@RequestBody PaymentVerificationRequest request){
         return  orderService.verifyPayment(request);

    }
}
