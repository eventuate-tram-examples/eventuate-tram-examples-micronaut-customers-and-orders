package io.eventuate.examples.tram.ordersandcustomers.commondomain;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class CustomerCreditReservationFailedEvent extends AbstractCustomerOrderEvent {

  public CustomerCreditReservationFailedEvent() {
  }

  public CustomerCreditReservationFailedEvent(Long orderId) {
    super(orderId);
  }
}
