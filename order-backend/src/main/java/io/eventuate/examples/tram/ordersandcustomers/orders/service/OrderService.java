package io.eventuate.examples.tram.ordersandcustomers.orders.service;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderApprovedEvent;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderDetails;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderRejectedEvent;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.Order;
import io.eventuate.examples.tram.ordersandcustomers.orders.repository.OrderRepository;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.events.publisher.ResultWithEvents;

import javax.inject.Singleton;
import javax.transaction.Transactional;

import static java.util.Collections.singletonList;

@Singleton
public class OrderService {

  private DomainEventPublisher domainEventPublisher;
  private OrderRepository orderRepository;

  public OrderService(DomainEventPublisher domainEventPublisher, OrderRepository orderRepository) {
    this.domainEventPublisher = domainEventPublisher;
    this.orderRepository = orderRepository;
  }

  @Transactional
  public Order createOrder(OrderDetails orderDetails) {
    ResultWithEvents<Order> orderWithEvents = Order.createOrder(orderDetails);
    Order order = orderWithEvents.result;
    orderRepository.save(order);
    domainEventPublisher.publish(Order.class, order.getId(), orderWithEvents.events);
    return order;
  }

  public void approveOrder(Long orderId) {
    Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException(String.format("order with id %s not found", orderId)));
    order.noteCreditReserved();
    orderRepository.updateById(orderId, order.getState());
    OrderDetails orderDetails = new OrderDetails(order.orderDetails().getCustomerId(), order.orderDetails().getOrderTotal());
    domainEventPublisher.publish(Order.class,
            orderId, singletonList(new OrderApprovedEvent(orderDetails)));
  }

  public void rejectOrder(Long orderId) {
    Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException(String.format("order with id %s not found", orderId)));
    order.noteCreditReservationFailed();
    orderRepository.updateById(orderId, order.getState());
    OrderDetails orderDetails = new OrderDetails(order.orderDetails().getCustomerId(), order.orderDetails().getOrderTotal());
    domainEventPublisher.publish(Order.class,
            orderId, singletonList(new OrderRejectedEvent(orderDetails)));
  }
}
