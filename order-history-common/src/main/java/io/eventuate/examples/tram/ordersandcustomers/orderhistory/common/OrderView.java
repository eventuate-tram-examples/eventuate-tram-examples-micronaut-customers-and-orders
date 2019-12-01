package io.eventuate.examples.tram.ordersandcustomers.orderhistory.common;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderState;
import io.micronaut.core.annotation.Introspected;

import java.math.BigDecimal;

@Introspected
public class OrderView {
  private Long id;
  private OrderState state;
  private BigDecimal orderTotal;

  public OrderView() {
  }

  public OrderView(Long id, BigDecimal orderTotal) {
    this.id = id;
    this.orderTotal = orderTotal;
    this.state = OrderState.PENDING;
  }

  public OrderView(Long id, OrderState state, BigDecimal orderTotal) {
    this.id = id;
    this.state = state;
    this.orderTotal = orderTotal;
  }

  public BigDecimal getOrderTotal() {
    return orderTotal;
  }

  public OrderState getState() {
    return state;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setState(OrderState state) {
    this.state = state;
  }

  public void setOrderTotal(BigDecimal orderTotal) {
    this.orderTotal = orderTotal;
  }
}
