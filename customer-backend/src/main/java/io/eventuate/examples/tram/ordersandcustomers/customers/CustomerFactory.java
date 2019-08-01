package io.eventuate.examples.tram.ordersandcustomers.customers;

import io.eventuate.examples.tram.ordersandcustomers.customers.service.OrderEventConsumer;
import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.micronaut.context.annotation.Factory;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.inject.Singleton;
import javax.persistence.EntityManagerFactory;

@Factory
public class CustomerFactory {
  @Singleton
  public DomainEventDispatcher domainEventDispatcher(OrderEventConsumer orderEventConsumer,
                                                     DomainEventDispatcherFactory domainEventDispatcherFactory) {
    return domainEventDispatcherFactory.make("orderServiceEvents", orderEventConsumer.domainEventHandlers());
  }

  @Singleton
  public TransactionTemplate transactionTemplate(SessionFactory sessionFactory) {
    return new TransactionTemplate(new HibernateTransactionManager(sessionFactory));
  }
}
