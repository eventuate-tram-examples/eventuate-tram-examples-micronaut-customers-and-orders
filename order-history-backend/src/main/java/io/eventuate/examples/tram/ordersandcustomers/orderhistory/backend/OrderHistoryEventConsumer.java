package io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderApprovedEvent;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderCreatedEvent;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderRejectedEvent;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;

import javax.inject.Singleton;

@Singleton
public class OrderHistoryEventConsumer {

  private OrderHistoryViewService orderHistoryViewService;

  public OrderHistoryEventConsumer(OrderHistoryViewService orderHistoryViewService) {
    this.orderHistoryViewService = orderHistoryViewService;
  }

  public DomainEventHandlers domainEventHandlers() {
    return DomainEventHandlersBuilder
            .forAggregateType("io.eventuate.examples.tram.ordersandcustomers.orders.domain.Order")
            .onEvent(OrderCreatedEvent.class, this::orderCreatedEventHandler)
            .onEvent(OrderApprovedEvent.class, this::orderApprovedEventHandler)
            .onEvent(OrderRejectedEvent.class, this::orderRejectedEventHandler)
            .build();
  }

  private void orderCreatedEventHandler(DomainEventEnvelope<OrderCreatedEvent> domainEventEnvelope) {
    OrderCreatedEvent orderCreatedEvent = domainEventEnvelope.getEvent();
    orderHistoryViewService.addOrder(orderCreatedEvent.getOrderDetails().getCustomerId(),
            Long.parseLong(domainEventEnvelope.getAggregateId()), orderCreatedEvent.getOrderDetails().getOrderTotal());
  }

  private void orderApprovedEventHandler(DomainEventEnvelope<OrderApprovedEvent> domainEventEnvelope) {
    OrderApprovedEvent orderApprovedEvent = domainEventEnvelope.getEvent();
    orderHistoryViewService.approveOrder(orderApprovedEvent.getOrderDetails().getCustomerId(),
            Long.parseLong(domainEventEnvelope.getAggregateId()));
  }

  private void orderRejectedEventHandler(DomainEventEnvelope<OrderRejectedEvent> domainEventEnvelope) {
    OrderRejectedEvent orderRejectedEvent = domainEventEnvelope.getEvent();
    orderHistoryViewService.rejectOrder(orderRejectedEvent.getOrderDetails().getCustomerId(),
            Long.parseLong(domainEventEnvelope.getAggregateId()));
  }
}
