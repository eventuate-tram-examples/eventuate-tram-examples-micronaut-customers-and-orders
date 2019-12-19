package io.eventuate.examples.tram.ordersandcustomers.customers.service;

import io.eventuate.common.jdbc.EventuateTransactionTemplate;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.CustomerCreditReservationFailedEvent;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.CustomerCreditReservedEvent;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.CustomerValidationFailedEvent;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderCreatedEvent;
import io.eventuate.examples.tram.ordersandcustomers.customers.domain.CreditReservation;
import io.eventuate.examples.tram.ordersandcustomers.customers.domain.Customer;
import io.eventuate.examples.tram.ordersandcustomers.customers.domain.CustomerCreditLimitExceededException;
import io.eventuate.examples.tram.ordersandcustomers.customers.repository.CreditReservationRepository;
import io.eventuate.examples.tram.ordersandcustomers.customers.repository.CustomerRepository;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.Collections;

@Singleton
public class OrderEventConsumer {
  private Logger logger = LoggerFactory.getLogger(getClass());

  private CustomerRepository customerRepository;
  private CreditReservationRepository creditReservationRepository;
  private DomainEventPublisher domainEventPublisher;
  private EventuateTransactionTemplate eventuateTransactionTemplate;

  public OrderEventConsumer(EventuateTransactionTemplate eventuateTransactionTemplate,
                            CustomerRepository customerRepository,
                            CreditReservationRepository creditReservationRepository,
                            DomainEventPublisher domainEventPublisher) {
    this.eventuateTransactionTemplate = eventuateTransactionTemplate;
    this.customerRepository = customerRepository;
    this.creditReservationRepository = creditReservationRepository;
    this.domainEventPublisher = domainEventPublisher;
  }

  public DomainEventHandlers domainEventHandlers() {
    return DomainEventHandlersBuilder
            .forAggregateType("io.eventuate.examples.tram.ordersandcustomers.orders.domain.Order")
            .onEvent(OrderCreatedEvent.class, this::orderCreatedEventHandler)
            .build();
  }

  private void orderCreatedEventHandler(DomainEventEnvelope<OrderCreatedEvent> domainEventEnvelope) {
    eventuateTransactionTemplate.executeInTransaction(() -> {
      Long orderId = Long.parseLong(domainEventEnvelope.getAggregateId());

      OrderCreatedEvent orderCreatedEvent = domainEventEnvelope.getEvent();

      Long customerId = orderCreatedEvent.getOrderDetails().getCustomerId();

      Customer customer = customerRepository.findById(customerId).orElse(null);

      if (customer == null) {
        logger.info("Non-existent customer: {}", customerId);
        domainEventPublisher.publish(Customer.class,
                customerId,
                Collections.singletonList(new CustomerValidationFailedEvent(orderId)));
        return null;
      }

      try {
        customer.setCreditReservations(creditReservationRepository.findAllByCustomerId(customerId));

        CreditReservation creditReservation = customer.reserveCredit(orderId, orderCreatedEvent.getOrderDetails().getOrderTotal());

        CustomerCreditReservedEvent customerCreditReservedEvent =
                new CustomerCreditReservedEvent(orderId);

        domainEventPublisher.publish(Customer.class,
                customer.getId(),
                Collections.singletonList(customerCreditReservedEvent));

        creditReservationRepository.save(creditReservation);
      } catch (CustomerCreditLimitExceededException e) {

        CustomerCreditReservationFailedEvent customerCreditReservationFailedEvent =
                new CustomerCreditReservationFailedEvent(orderId);

        domainEventPublisher.publish(Customer.class,
                customer.getId(),
                Collections.singletonList(customerCreditReservationFailedEvent));
      }

      return null;
    });
  }
}
