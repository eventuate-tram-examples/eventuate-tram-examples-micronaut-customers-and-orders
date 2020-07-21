package io.eventuate.examples.tram.ordersandcustomers.orders.service;

import io.eventuate.examples.tram.ordersandcustomers.customers.events.CustomerCreditReservationFailedEvent;
import io.eventuate.examples.tram.ordersandcustomers.customers.events.CustomerCreditReservedEvent;
import io.eventuate.examples.tram.ordersandcustomers.customers.events.CustomerValidationFailedEvent;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class CustomerEventConsumer {

  @Inject
  private OrderService orderService;

  public DomainEventHandlers domainEventHandlers() {
    return DomainEventHandlersBuilder
            .forAggregateType("io.eventuate.examples.tram.ordersandcustomers.customers.domain.Customer")
            .onEvent(CustomerCreditReservedEvent.class, this::handleCustomerCreditReservedEvent)
            .onEvent(CustomerCreditReservationFailedEvent.class, this::handleCustomerCreditReservationFailedEvent)
            .onEvent(CustomerValidationFailedEvent.class, this::handleCustomerValidationFailedEvent)
            .build();
  }

  private void handleCustomerCreditReservedEvent(DomainEventEnvelope<CustomerCreditReservedEvent> domainEventEnvelope) {
    orderService.approveOrder(domainEventEnvelope.getEvent().getOrderId());
  }

  private void handleCustomerCreditReservationFailedEvent(DomainEventEnvelope<CustomerCreditReservationFailedEvent> domainEventEnvelope) {
    orderService.rejectOrder(domainEventEnvelope.getEvent().getOrderId());
  }

  private void handleCustomerValidationFailedEvent(DomainEventEnvelope<CustomerValidationFailedEvent> domainEventEnvelope) {
    orderService.rejectOrder(domainEventEnvelope.getEvent().getOrderId());
  }
}
