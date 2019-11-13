package io.eventuate.examples.tram.ordersandcustomers.orderhistory.common;

import io.micronaut.core.annotation.Introspected;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Introspected
public class CustomerView {

  private Long id;
  private Map<Long, OrderInfo> orders = new HashMap<>();
  private String name;
  private BigDecimal creditLimit;

  public CustomerView() {
  }

  public CustomerView(Long id, Map<Long, OrderInfo> orders, String name, BigDecimal creditLimit) {
    this.id = id;
    this.orders = orders;
    this.name = name;
    this.creditLimit = creditLimit;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public Map<Long, OrderInfo> getOrders() {
    return orders;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setCreditLimit(BigDecimal creditLimit) {
    this.creditLimit = creditLimit;
  }

  public BigDecimal getCreditLimit() {
    return creditLimit;
  }

  public void setOrders(Map<Long, OrderInfo> orders) {
    this.orders = orders;
  }
}
