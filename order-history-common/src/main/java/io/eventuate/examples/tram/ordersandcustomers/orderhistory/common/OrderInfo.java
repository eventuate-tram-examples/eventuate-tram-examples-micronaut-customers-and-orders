package io.eventuate.examples.tram.ordersandcustomers.orderhistory.common;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderState;
import io.micronaut.core.annotation.Introspected;

import java.math.BigDecimal;

@Introspected
public class OrderInfo {
  private Long orderId;
  private BigDecimal orderTotal;
  private OrderState state;


  public OrderInfo() {
  }

  public OrderInfo(Long orderId, BigDecimal orderTotal) {
    this.orderId = orderId;
    this.orderTotal = orderTotal;
    this.state = OrderState.PENDING;
  }

  public OrderInfo(Long orderId, BigDecimal orderTotal, OrderState state) {
    this.orderId = orderId;
    this.orderTotal = orderTotal;
    this.state = state;
  }

  public void approve() {
    state = OrderState.APPROVED;
  }

  public void reject() {
    state = OrderState.REJECTED;
  }

  public BigDecimal getOrderTotal() {
    return orderTotal;
  }

  public OrderState getState() {
    return state;
  }

  public Long getOrderId() {
    return orderId;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }

  public void setOrderTotal(BigDecimal orderTotal) {
    this.orderTotal = orderTotal;
  }

  public void setState(OrderState state) {
    this.state = state;
  }
}
