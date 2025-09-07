package in.aaditiya.billingsoftware.service;

import com.razorpay.RazorpayException;
import in.aaditiya.billingsoftware.io.RazorPayOrderResponse;

public interface RazorpayService {
    RazorPayOrderResponse createOrder(Double amount , String currency)throws RazorpayException;
}
