package io.eventuate.examples.tram.ordersandcustomers.orders;

import io.eventuate.examples.tram.ordersandcustomers.orders.service.CustomerEventConsumer;
import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.micronaut.context.annotation.Factory;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.inject.Singleton;
import javax.persistence.EntityManagerFactory;

@Factory
public class OrderFactory {
  @Singleton
  public DomainEventDispatcher domainEventDispatcher(CustomerEventConsumer customerEventConsumer, DomainEventDispatcherFactory domainEventDispatcherFactory) {
    return domainEventDispatcherFactory.make("consumerServiceEvents", customerEventConsumer.domainEventHandlers());
  }

  @Singleton
  public TransactionTemplate transactionTemplate(SessionFactory sessionFactory) {
    return new TransactionTemplate(new HibernateTransactionManager(sessionFactory));
  }
}
