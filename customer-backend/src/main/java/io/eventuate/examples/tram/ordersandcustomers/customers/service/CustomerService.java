package io.eventuate.examples.tram.ordersandcustomers.customers.service;

import io.eventuate.examples.tram.ordersandcustomers.customers.domain.Customer;
import io.eventuate.examples.tram.ordersandcustomers.customers.repository.CustomerRepository;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.events.publisher.ResultWithEvents;

import javax.inject.Singleton;
import javax.transaction.Transactional;
import java.math.BigDecimal;

@Singleton
public class CustomerService {

  private DomainEventPublisher domainEventPublisher;
  private CustomerRepository customerRepository;

  public CustomerService(DomainEventPublisher domainEventPublisher, CustomerRepository customerRepository) {
    this.domainEventPublisher = domainEventPublisher;
    this.customerRepository = customerRepository;
  }

  @Transactional
  public Customer createCustomer(String name, BigDecimal creditLimit) {
    ResultWithEvents<Customer> customerWithEvents = Customer.create(name, creditLimit);
    Customer customer = customerWithEvents.result;
    customerRepository.save(customer);
    domainEventPublisher.publish(Customer.class, customer.getId(), customerWithEvents.events);
    return customer;
  }
}
