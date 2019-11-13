package io.eventuate.examples.tram.ordersandcustomers.commondomain;

import io.eventuate.tram.events.common.DomainEvent;
import io.micronaut.core.annotation.Introspected;

@Introspected
public class OrderCreatedEvent implements DomainEvent {

  private OrderDetails orderDetails;

  public OrderCreatedEvent() {
  }

  public OrderCreatedEvent(OrderDetails orderDetails) {
    this.orderDetails = orderDetails;
  }

  public OrderDetails getOrderDetails() {
    return orderDetails;
  }

  public void setOrderDetails(OrderDetails orderDetails) {
    this.orderDetails = orderDetails;
  }
}
