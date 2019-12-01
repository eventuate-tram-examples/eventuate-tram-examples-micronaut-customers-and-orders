package io.eventuate.examples.tram.ordersandcustomers.commondomain;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class CustomerValidationFailedEvent extends AbstractCustomerOrderEvent {

  public CustomerValidationFailedEvent(Long orderId) {
    super(orderId);
  }

  public CustomerValidationFailedEvent() {
  }
}
