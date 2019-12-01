package io.eventuate.examples.tram.ordersandcustomers.commondomain;

import io.micronaut.core.annotation.Introspected;

import java.math.BigDecimal;

@Introspected
public class OrderDetails {

  private Long customerId;

  private BigDecimal orderTotal;

  public OrderDetails() {
  }

  public OrderDetails(Long customerId, BigDecimal orderTotal) {
    this.customerId = customerId;
    this.orderTotal = orderTotal;
  }

  public Long getCustomerId() {
    return customerId;
  }

  public BigDecimal getOrderTotal() {
    return orderTotal;
  }

  public void setCustomerId(Long customerId) {
    this.customerId = customerId;
  }

  public void setOrderTotal(BigDecimal orderTotal) {
    this.orderTotal = orderTotal;
  }
}
