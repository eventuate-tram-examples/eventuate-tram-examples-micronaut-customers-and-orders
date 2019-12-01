package io.eventuate.examples.tram.ordersandcustomers.customers.webapi;


import io.micronaut.core.annotation.Introspected;

import java.math.BigDecimal;

@Introspected
public class CreateCustomerRequest {
  private String name;
  private BigDecimal creditLimit;

  public CreateCustomerRequest() {
  }

  public CreateCustomerRequest(String name, BigDecimal creditLimit) {

    this.name = name;
    this.creditLimit = creditLimit;
  }


  public String getName() {
    return name;
  }

  public BigDecimal getCreditLimit() {
    return creditLimit;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setCreditLimit(BigDecimal creditLimit) {
    this.creditLimit = creditLimit;
  }
}
