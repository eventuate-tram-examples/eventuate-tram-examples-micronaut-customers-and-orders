package io.eventuate.examples.tram.ordersandcustomers.orders;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;

@OpenAPIDefinition
public class OrderServiceMain {
  public static void main(String[] args) {
    Micronaut.run(OrderServiceMain.class);
  }
}
