package io.eventuate.examples.tram.ordersandcustomers.orders.service;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.MoneyDTO;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderApprovedEvent;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderDetailsDTO;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderRejectedEvent;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.Order;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.events.publisher.ResultWithEvents;
import io.micronaut.spring.tx.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

import static java.util.Collections.singletonList;

@Singleton
public class OrderService {

  @Inject
  private DomainEventPublisher domainEventPublisher;

  @PersistenceContext
  private EntityManager entityManager;

  @Transactional
  public Order createOrder(OrderDetailsDTO orderDetails) {
    ResultWithEvents<Order> orderWithEvents = Order.createOrder(orderDetails);
    Order order = orderWithEvents.result;
    entityManager.persist(order);
    domainEventPublisher.publish(Order.class, order.getId(), orderWithEvents.events);
    return order;
  }

  @Transactional
  public void approveOrder(Long orderId) {
    Order order = Optional.ofNullable(entityManager.find(Order.class, orderId))
            .orElseThrow(() -> new IllegalArgumentException(String.format("order with id %s not found", orderId)));
    order.noteCreditReserved();
    OrderDetailsDTO orderDetails = new OrderDetailsDTO(order.getOrderDetails().getCustomerId(), new MoneyDTO(order.getOrderDetails().getOrderTotal().getAmount()));
    domainEventPublisher.publish(Order.class,
            orderId, singletonList(new OrderApprovedEvent(orderDetails)));
  }

  @Transactional
  public void rejectOrder(Long orderId) {
    Order order = Optional
            .ofNullable(entityManager.find(Order.class, orderId))
            .orElseThrow(() -> new IllegalArgumentException(String.format("order with id %s not found", orderId)));
    order.noteCreditReservationFailed();
    OrderDetailsDTO orderDetails = new OrderDetailsDTO(order.getOrderDetails().getCustomerId(), new MoneyDTO(order.getOrderDetails().getOrderTotal().getAmount()));
    domainEventPublisher.publish(Order.class,
            orderId, singletonList(new OrderRejectedEvent(orderDetails)));
  }
}
