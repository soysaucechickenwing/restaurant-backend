package com.johngoodtime.demo.service;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentService {

    @Value("${app.stripe.secret-key}")
    private String secretKey;


    public PaymentIntent createPaymentIntent(BigDecimal amount, String currency, Long orderId) throws Exception {
        Stripe.apiKey = secretKey;

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount.multiply(BigDecimal.valueOf(100)).longValue()) // Stripe用分为单位
                .setCurrency(currency)
                .putMetadata("orderId", orderId.toString())
                .build();

        return PaymentIntent.create(params);
    }
}
