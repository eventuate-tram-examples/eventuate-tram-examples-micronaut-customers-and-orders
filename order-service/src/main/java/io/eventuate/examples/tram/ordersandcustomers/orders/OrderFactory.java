package io.eventuate.examples.tram.ordersandcustomers.orders;

import io.eventuate.examples.tram.ordersandcustomers.orders.service.CustomerEventConsumer;
import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.micronaut.context.annotation.Factory;

import javax.inject.Singleton;

@Factory
public class OrderFactory {
  @Singleton
  public DomainEventDispatcher domainEventDispatcher(CustomerEventConsumer customerEventConsumer, DomainEventDispatcherFactory domainEventDispatcherFactory) {
    return domainEventDispatcherFactory.make("consumerServiceEvents", customerEventConsumer.domainEventHandlers());
  }
}
