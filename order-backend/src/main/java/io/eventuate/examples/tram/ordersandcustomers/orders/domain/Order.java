package io.eventuate.examples.tram.ordersandcustomers.orders.domain;


import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderCreatedEvent;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderDetails;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderState;
import io.eventuate.tram.events.publisher.ResultWithEvents;

import javax.persistence.*;
import java.math.BigDecimal;

import static java.util.Collections.singletonList;

@Entity
@Table(name="orders")
@Access(AccessType.FIELD)
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private OrderState state;

  private Long customerId;

  private BigDecimal orderTotal;

  public Order() {
  }

  public Order(OrderDetails orderDetails) {
    this.customerId = orderDetails.getCustomerId();
    this.orderTotal = orderDetails.getOrderTotal();
    this.state = OrderState.PENDING;
  }

  public static ResultWithEvents<Order> createOrder(OrderDetails orderDetails) {
    Order order = new Order(new OrderDetails(orderDetails.getCustomerId(), orderDetails.getOrderTotal()));
    OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(orderDetails);
    return new ResultWithEvents<>(order, singletonList(orderCreatedEvent));
  }

  public Long getId() {
    return id;
  }

  public void noteCreditReserved() {
    this.state = OrderState.APPROVED;
  }

  public void noteCreditReservationFailed() {
    this.state = OrderState.REJECTED;
  }

  public OrderState getState() {
    return state;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setState(OrderState state) {
    this.state = state;
  }

  public Long getCustomerId() {
    return customerId;
  }

  public void setCustomerId(Long customerId) {
    this.customerId = customerId;
  }

  public BigDecimal getOrderTotal() {
    return orderTotal;
  }

  public void setOrderTotal(BigDecimal orderTotal) {
    this.orderTotal = orderTotal;
  }

  public OrderDetails orderDetails() {
    return new OrderDetails(customerId, orderTotal);
  }
}
