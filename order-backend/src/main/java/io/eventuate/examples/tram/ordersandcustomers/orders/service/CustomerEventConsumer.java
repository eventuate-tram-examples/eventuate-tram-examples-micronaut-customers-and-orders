package io.eventuate.examples.tram.ordersandcustomers.orders.service;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.CustomerCreditReservationFailedEvent;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.CustomerCreditReservedEvent;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.CustomerValidationFailedEvent;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class CustomerEventConsumer {

  @Inject
  private CustomerEventConsumerEventHandlers customerEventConsumerEventHandlers;

  public DomainEventHandlers domainEventHandlers() {
    return DomainEventHandlersBuilder
            .forAggregateType("io.eventuate.examples.tram.ordersandcustomers.customers.domain.Customer")
            .onEvent(CustomerCreditReservedEvent.class, customerEventConsumerEventHandlers::handleCustomerCreditReservedEvent)
            .onEvent(CustomerCreditReservationFailedEvent.class, customerEventConsumerEventHandlers::handleCustomerCreditReservationFailedEvent)
            .onEvent(CustomerValidationFailedEvent.class, customerEventConsumerEventHandlers::handleCustomerValidationFailedEvent)
            .build();
  }
}
