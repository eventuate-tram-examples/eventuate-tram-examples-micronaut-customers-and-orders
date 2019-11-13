package io.eventuate.examples.tram.ordersandcustomers.orders.webapi;


import io.micronaut.core.annotation.Introspected;

@Introspected
public class CreateOrderResponse {
  private Long orderId;

  public CreateOrderResponse() {
  }

  public CreateOrderResponse(Long orderId) {
    this.orderId = orderId;
  }

  public Long getOrderId() {
    return orderId;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }
}
