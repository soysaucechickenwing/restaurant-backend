package com.johngoodtime.demo.service;

import com.johngoodtime.demo.model.Order;
import com.johngoodtime.demo.model.OrderItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final SesClient sesClient;

    @Value("${app.email.from}")
    private String fromEmail;

    @Value("${app.email.restaurant-email}")
    private String restaurantEmail;

    public EmailService() {
        this.sesClient = SesClient.builder()
                .region(Region.US_EAST_1)
                .build();
    }

    public void sendNewOrderNotification(Order order) {
        StringBuilder items = new StringBuilder();
        for (OrderItem item : order.getItems()) {
            items.append(String.format("  - %s x%d  $%.2f%n",
                    item.getMenuItemName(), item.getQuantity(), item.getSubtotal()));
        }

        String body = String.format(
                "新订单通知\n\n" +
                "订单号：%s\n" +
                "顾客姓名：%s\n" +
                "电话：%s\n" +
                "取餐方式：%s\n\n" +
                "订单内容：\n%s\n" +
                "小计：$%.2f\n" +
                "税：$%.2f\n" +
                "总价：$%.2f\n",
                order.getOrderNumber(),
                order.getCustomerName(),
                order.getCustomerPhone(),
                order.getOrderType(),
                items,
                order.getSubtotal(),
                order.getTax(),
                order.getTotalAmount()
        );

        String subject = String.format("新订单 %s - $%.2f",
                order.getOrderNumber(), order.getTotalAmount());

        sendEmail(restaurantEmail, subject, body);
    }

    public void sendOrderConfirmedToCustomer(Order order) {
        if (order.getCustomerEmail() == null || order.getCustomerEmail().isBlank()) {
            return;
        }

        String estimatedTime = order.getEstimatedReadyAt() != null
                ? order.getEstimatedReadyAt().format(DateTimeFormatter.ofPattern("HH:mm"))
                : "约20分钟后";

        String body = String.format(
                "您好！\n\n" +
                "您的订单已确认，我们正在为您准备。\n\n" +
                "订单号：%s\n" +
                "预计取餐时间：%s\n\n" +
                "感谢您选择 John Goodtime Restaurant！",
                order.getOrderNumber(),
                estimatedTime
        );

        sendEmail(order.getCustomerEmail(), "您的订单已确认 - John Goodtime Restaurant", body);
    }

    public void sendOrderCancelledToCustomer(Order order) {
        if (order.getCustomerEmail() == null || order.getCustomerEmail().isBlank()) {
            return;
        }

        String body = String.format(
                "您好！\n\n" +
                "很遗憾，您的订单已被取消。\n\n" +
                "订单号：%s\n\n" +
                "如您已完成支付，退款将在3-5个工作日内退回您的原支付方式。\n\n" +
                "如有疑问，请联系我们。\n\n" +
                "John Goodtime Restaurant",
                order.getOrderNumber()
        );

        sendEmail(order.getCustomerEmail(), "您的订单已取消 - John Goodtime Restaurant", body);
    }

    private void sendEmail(String to, String subject, String body) {
        try {
            SendEmailRequest request = SendEmailRequest.builder()
                    .destination(Destination.builder().toAddresses(to).build())
                    .message(Message.builder()
                            .subject(Content.builder().data(subject).charset("UTF-8").build())
                            .body(Body.builder()
                                    .text(Content.builder().data(body).charset("UTF-8").build())
                                    .build())
                            .build())
                    .source(fromEmail)
                    .build();

            sesClient.sendEmail(request);
            log.info("Email sent to {} subject: {}", to, subject);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }
}
