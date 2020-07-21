package io.eventuate.examples.tram.ordersandcustomers.customers.events;

public class CustomerCreditReservationFailedEvent extends AbstractCustomerOrderEvent {

  public CustomerCreditReservationFailedEvent() {
  }

  public CustomerCreditReservationFailedEvent(Long orderId) {
    super(orderId);
  }


}
