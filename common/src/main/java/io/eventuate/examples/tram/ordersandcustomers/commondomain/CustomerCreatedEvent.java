package io.eventuate.examples.tram.ordersandcustomers.commondomain;

import io.eventuate.tram.events.common.DomainEvent;
import io.micronaut.core.annotation.Introspected;

import java.math.BigDecimal;

@Introspected
public class CustomerCreatedEvent implements DomainEvent {
  private String name;
  private BigDecimal creditLimit;

  public CustomerCreatedEvent() {
  }

  public CustomerCreatedEvent(String name, BigDecimal creditLimit) {
    this.name = name;
    this.creditLimit = creditLimit;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getCreditLimit() {
    return creditLimit;
  }

  public void setCreditLimit(BigDecimal creditLimit) {
    this.creditLimit = creditLimit;
  }
}
