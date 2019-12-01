package io.eventuate.examples.tram.ordersandcustomers.commondomain;

import io.eventuate.tram.events.common.DomainEvent;
import io.micronaut.core.annotation.Introspected;

@Introspected
public class OrderRejectedEvent implements DomainEvent {

  private OrderDetails orderDetails;

  public OrderRejectedEvent() {
  }

  public OrderRejectedEvent(OrderDetails orderDetails) {
    this.orderDetails = orderDetails;
  }

  public OrderDetails getOrderDetails() {
    return orderDetails;
  }

  public void setOrderDetails(OrderDetails orderDetails) {
    this.orderDetails = orderDetails;
  }
}
