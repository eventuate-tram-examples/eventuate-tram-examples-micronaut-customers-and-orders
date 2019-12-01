package io.eventuate.examples.tram.ordersandcustomers.orders.webapi;


import io.micronaut.core.annotation.Introspected;

import java.math.BigDecimal;

@Introspected
public class CreateOrderRequest {
  private BigDecimal orderTotal;
  private Long customerId;

  public CreateOrderRequest() {
  }

  public CreateOrderRequest(Long customerId, BigDecimal orderTotal) {
    this.customerId = customerId;
    this.orderTotal = orderTotal;
  }

  public BigDecimal getOrderTotal() {
    return orderTotal;
  }

  public Long getCustomerId() {
    return customerId;
  }

  public void setOrderTotal(BigDecimal orderTotal) {
    this.orderTotal = orderTotal;
  }

  public void setCustomerId(Long customerId) {
    this.customerId = customerId;
  }
}
