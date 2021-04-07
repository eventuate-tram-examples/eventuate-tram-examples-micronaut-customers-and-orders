package io.eventuate.examples.tram.ordersandcustomers.orderhistory.common;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.Money;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderState;

public class OrderInfo {

  private Long orderId;
  private Money orderTotal;
  private OrderState state;


  public OrderInfo() {
  }

  public OrderInfo(Long orderId, Money orderTotal) {
    this.orderId = orderId;
    this.orderTotal = orderTotal;
    this.state = OrderState.PENDING;
  }

  public void approve() {
    state = OrderState.APPROVED;
  }

  public void reject() {
    state = OrderState.REJECTED;
  }

  public Money getOrderTotal() {
    return orderTotal;
  }

  public OrderState getState() {
    return state;
  }

  public void setState(OrderState state) {
    this.state = state;
  }
}
