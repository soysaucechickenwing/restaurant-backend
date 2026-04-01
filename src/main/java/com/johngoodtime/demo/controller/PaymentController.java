package com.johngoodtime.demo.controller;

import com.johngoodtime.demo.model.Order;
import com.johngoodtime.demo.service.OrderService;
import com.johngoodtime.demo.service.PaymentService;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import com.johngoodtime.demo.model.ErrorResponse;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderService orderService;

    @Value("${app.stripe.webhook-secret}")
    private String webhookSecret;

    public PaymentController(PaymentService paymentService, OrderService orderService) {
        this.paymentService = paymentService;
        this.orderService = orderService;
    }

    @PostMapping("/create-intent/{orderId}")
    public ResponseEntity<?> createPaymentIntent(@PathVariable Long orderId){
        try{
            Order order = orderService.getAllActiveOrders()
                    .stream()
                    .filter(o->o.getId().equals(orderId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            var paymentIntent = paymentService.createPaymentIntent(
                    order.getTotalAmount(),"usd", orderId);

            orderService.updateStripePaymentId(orderId, paymentIntent.getId());

            Map<String, String> response = new HashMap<>();
            response.put("clientSecret", paymentIntent.getClientSecret());
            response.put("paymentIntentId",paymentIntent.getId());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, e.getMessage()));
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload,
                                                @RequestHeader("Stripe-Signature") String sigHeader){

    try{
        Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        if("payment_intent.succeeded".equals(event.getType())){
            PaymentIntent paymentIntent = (PaymentIntent) event
                    .getDataObjectDeserializer()
                    .deserializeUnsafe();
            orderService.markAsPaid(paymentIntent.getId());
        }
        return ResponseEntity.ok("OK");
    }catch(Exception e){
        System.err.println("Webhook error: " + e.getMessage());
        return ResponseEntity.badRequest().body("Webhook error: " + e.getMessage());
    }
    }

}
