package io.eventuate.examples.tram.ordersandcustomers.commondomain;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class CustomerCreditReservedEvent extends AbstractCustomerOrderEvent {

  public CustomerCreditReservedEvent() {
  }

  public CustomerCreditReservedEvent(Long orderId) {
    super(orderId);
  }
}
