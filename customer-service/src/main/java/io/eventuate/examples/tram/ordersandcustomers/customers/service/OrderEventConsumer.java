package io.eventuate.examples.tram.ordersandcustomers.customers.service;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderCreatedEvent;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class OrderEventConsumer {

  @Inject
  private CustomerService customerService;

  public DomainEventHandlers domainEventHandlers() {
    return DomainEventHandlersBuilder
            .forAggregateType("io.eventuate.examples.tram.ordersandcustomers.orders.domain.Order")
            .onEvent(OrderCreatedEvent.class, this::orderCreatedEventHandler)
            .build();
  }

  private void orderCreatedEventHandler(DomainEventEnvelope<OrderCreatedEvent> domainEventEnvelope) {
    Long orderId = Long.parseLong(domainEventEnvelope.getAggregateId());

    OrderCreatedEvent orderCreatedEvent = domainEventEnvelope.getEvent();

    customerService.reserveCredit(orderId, orderCreatedEvent);
  }
}
