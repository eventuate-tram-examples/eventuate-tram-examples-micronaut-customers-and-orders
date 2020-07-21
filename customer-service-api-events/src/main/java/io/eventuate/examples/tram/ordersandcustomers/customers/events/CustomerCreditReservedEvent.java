package io.eventuate.examples.tram.ordersandcustomers.customers.events;

public class CustomerCreditReservedEvent extends AbstractCustomerOrderEvent {

  public CustomerCreditReservedEvent() {
  }

  public CustomerCreditReservedEvent(Long orderId) {
    super(orderId);
  }
}
