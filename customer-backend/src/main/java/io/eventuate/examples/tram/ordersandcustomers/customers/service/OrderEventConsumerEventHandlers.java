package io.eventuate.examples.tram.ordersandcustomers.customers.service;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.CustomerCreditReservationFailedEvent;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.CustomerCreditReservedEvent;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.CustomerValidationFailedEvent;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderCreatedEvent;
import io.eventuate.examples.tram.ordersandcustomers.customers.domain.Customer;
import io.eventuate.examples.tram.ordersandcustomers.customers.domain.CustomerCreditLimitExceededException;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.micronaut.spring.tx.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;

@Singleton
public class OrderEventConsumerEventHandlers {
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Inject
  private DomainEventPublisher domainEventPublisher;

  @PersistenceContext
  private EntityManager entityManager;

  @Transactional
  public void orderCreatedEventHandler(DomainEventEnvelope<OrderCreatedEvent> domainEventEnvelope/*, EntityManager entityManager*/) {

    Long orderId = Long.parseLong(domainEventEnvelope.getAggregateId());

    OrderCreatedEvent orderCreatedEvent = domainEventEnvelope.getEvent();

    Long customerId = orderCreatedEvent.getOrderDetails().getCustomerId();

    Customer customer = entityManager.find(Customer.class, customerId);

    if (customer == null) {
      logger.info("Non-existent customer: {}", customerId);
      domainEventPublisher.publish(Customer.class,
              customerId,
              Collections.singletonList(new CustomerValidationFailedEvent(orderId)));
      return;
    }

    try {
      customer.reserveCredit(orderId, orderCreatedEvent.getOrderDetails().getOrderTotal());

      CustomerCreditReservedEvent customerCreditReservedEvent =
              new CustomerCreditReservedEvent(orderId);

      domainEventPublisher.publish(Customer.class,
              customer.getId(),
              Collections.singletonList(customerCreditReservedEvent));

    } catch (CustomerCreditLimitExceededException e) {

      CustomerCreditReservationFailedEvent customerCreditReservationFailedEvent =
              new CustomerCreditReservationFailedEvent(orderId);

      domainEventPublisher.publish(Customer.class,
              customer.getId(),
              Collections.singletonList(customerCreditReservationFailedEvent));
    }
  }
}
