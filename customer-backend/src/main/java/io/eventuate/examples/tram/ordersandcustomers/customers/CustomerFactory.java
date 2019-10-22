package io.eventuate.examples.tram.ordersandcustomers.customers;

import io.eventuate.examples.tram.ordersandcustomers.customers.repository.CustomerRepository;
import io.eventuate.examples.tram.ordersandcustomers.customers.service.OrderEventConsumer;
import io.eventuate.tram.consumer.common.MessageConsumerImplementation;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.eventuate.tram.messaging.consumer.MessageHandler;
import io.eventuate.tram.messaging.consumer.MessageSubscription;
import io.micronaut.context.annotation.Factory;

import javax.inject.Singleton;
import java.util.Set;
import java.util.UUID;

@Factory
public class CustomerFactory {

  @Singleton
  public OrderEventConsumer orderEventConsumer(CustomerRepository customerRepository, DomainEventPublisher domainEventPublisher) {
    return new OrderEventConsumer(customerRepository, domainEventPublisher);
  }

  @Singleton
  public DomainEventDispatcher domainEventDispatcher(OrderEventConsumer orderEventConsumer,
                                                     DomainEventDispatcherFactory domainEventDispatcherFactory) {
    return domainEventDispatcherFactory.make("orderServiceEvents", orderEventConsumer.domainEventHandlers());
  }

  @Singleton
  public MessageConsumerImplementation messageConsumerImplementation() {
    return new MessageConsumerImplementation() {
      private String id = UUID.randomUUID().toString();

      @Override
      public MessageSubscription subscribe(String s, Set<String> set, MessageHandler messageHandler) {
        return () -> {};
      }

      @Override
      public String getId() {
        return id;
      }

      @Override
      public void close() {

      }
    };
  }
}
