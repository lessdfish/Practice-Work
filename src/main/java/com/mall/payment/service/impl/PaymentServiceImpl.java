package com.mall.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall.common.exception.BusinessException;
import com.mall.common.exception.ErrorCode;
import com.mall.common.transaction.AfterCommitExecutor;
import com.mall.inventory.service.InventoryService;
import com.mall.order.domain.Order;
import com.mall.order.domain.OrderStatus;
import com.mall.order.event.OrderPaidEvent;
import com.mall.order.mapper.OrderMapper;
import com.mall.order.mq.OrderPaidEventProducer;
import com.mall.order.service.OrderStateService;
import com.mall.payment.domain.Payment;
import com.mall.payment.domain.PaymentStatus;
import com.mall.payment.dto.CreatePaymentResponse;
import com.mall.payment.dto.PaymentCallbackRequest;
import com.mall.payment.dto.PaymentCallbackResponse;
import com.mall.payment.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * ClassName:PaymentServiceImpl
 * Package:com.mall.payment.service.impl
 * Description:
 *
 * @Author:lyp
 * @Create:2026/9/4 - 15:28
 * @Version: v1.0
 *
 */
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{
    private final PaymentMapper paymentMapper;
    private final OrderMapper orderMapper;
    private final OrderStateService orderStateService;
    private final InventoryService inventoryService;
    private final OrderPaidEventProducer orderPaidEventProducer;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CreatePaymentResponse createPayment(Long userId, Long orderId) {
        Order order = findUserOrder(userId,orderId);

        if (order.getStatus() != OrderStatus.WAIT_PAY.getCode()) {
            throw new BusinessException(ErrorCode.ORDER_NOT_PAYABLE);
        }

        Payment existing = findByOrderId(orderId);
        if (existing!=null) {
            return toCreateResponse(existing);
        }

        Payment payment = new Payment();
        payment.setPaymentNo(generatePaymentNo());
        payment.setOrderId(orderId);
        payment.setUserId(userId);
        payment.setAmount(order.getAmount());
        payment.setStatus(PaymentStatus.WAIT_PAY.getCode());

        try {
            paymentMapper.insert(payment);
            return toCreateResponse(payment);
        }catch (DuplicateKeyException e){
            Payment concurrent = findByOrderId(orderId);

            if (concurrent!=null) {
                return toCreateResponse(concurrent);
            }
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentCallbackResponse handleSuccessCallback(PaymentCallbackRequest request) {
        int updated = paymentMapper.markPaid(
                request.paymentNo(), request.thirdPartyTradeNo(),request.amount(),PaymentStatus.WAIT_PAY.getCode(),PaymentStatus.SUCCESS.getCode()
        );

        if (updated == 1) {
            Payment payment = findByPaymentNo(request.paymentNo());
            Order order = orderMapper.selectById(payment.getOrderId());
            orderStateService.transition(payment.getOrderId(), OrderStatus.WAIT_PAY,OrderStatus.PAID);
            inventoryService.confirmStock(payment.getOrderId());

            var event = new OrderPaidEvent(
                    UUID.randomUUID().toString(),
                    order.getId(),
                    order.getProductId(),
                    order.getQuantity(),
                    LocalDateTime.now()
            );

            AfterCommitExecutor.execute(()-> orderPaidEventProducer.send(event));

            return toCallbackResponse(payment);

        }

        Payment payment = findByPaymentNo(request.paymentNo());
        if(payment.getAmount().compareTo(request.amount())!=0){
            throw new BusinessException(ErrorCode.PAYMENT_AMOUNT_MISMATCH);
        }

        if(payment.getStatus() == PaymentStatus.SUCCESS.getCode()
        && request.thirdPartyTradeNo().equals(payment.getThirdPartyTradeNo())){
            return toCallbackResponse(payment);
        }

        throw new BusinessException(ErrorCode.PAYMENT_CALLBACK_CONFLICT);
    }

    private Order findUserOrder(Long userId,Long orderId){
        Order order = orderMapper.selectOne(
                new LambdaQueryWrapper<Order>()
                        .eq(Order::getId,orderId)
                        .eq(Order::getUserId,userId)
        );

        if (order == null) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        return order;
    }

    private Payment findByOrderId(Long orderId){
        return paymentMapper.selectOne(
                new LambdaQueryWrapper<Payment>()
                        .eq(Payment::getOrderId,orderId)
        );
    }

    private Payment findByPaymentNo(String paymentNo){
        Payment payment = paymentMapper.selectOne(
                new LambdaQueryWrapper<Payment>()
                        .eq(Payment::getPaymentNo,paymentNo)
        );
        if (payment == null) {
            throw new BusinessException(ErrorCode.PAYMENT_NOT_FOUND);
        }
        return payment;
    }

    private CreatePaymentResponse toCreateResponse(Payment payment){
        return new CreatePaymentResponse(
                payment.getPaymentNo(),
                payment.getOrderId(),
                payment.getAmount(),
                PaymentStatus.WAIT_PAY.name()
        );
    }

    private PaymentCallbackResponse toCallbackResponse(Payment payment){
        return new PaymentCallbackResponse(
                payment.getPaymentNo(),
                payment.getOrderId(),
                PaymentStatus.SUCCESS.name()
        );
    }

    private String generatePaymentNo(){
        return "PAY"
                + UUID.randomUUID()
                .toString()
                .replace("-","");
    }
}
