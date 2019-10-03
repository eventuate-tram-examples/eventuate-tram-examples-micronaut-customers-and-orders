package io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;

@OpenAPIDefinition
public class OrderHistoryServiceMain {

  public static void main(String[] args) {
    Micronaut.run(OrderHistoryServiceMain.class);
  }

}
