package io.eventuate.examples.tram.ordersandcustomers.customers;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;

@OpenAPIDefinition
public class CustomerServiceMain {
  public static void main(String[] args) {
    Micronaut.run(CustomerServiceMain.class);
  }
}
