package io.eventuate.examples.tram.ordersandcustomers.customers.domain;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.CustomerCreatedEvent;
import io.eventuate.tram.events.publisher.ResultWithEvents;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;

@Entity
@Table(name="Customer")
public class Customer {

  @Id
  @GeneratedValue
  private Long id;

  private String name;

  private BigDecimal creditLimit;

  @OneToMany
  private List<CreditReservation> creditReservations = new ArrayList<>();

  BigDecimal availableCredit() {
    return creditLimit
            .subtract(creditReservations
                    .stream()
                    .map(CreditReservation::getCredit)
                    .reduce(new BigDecimal(0), BigDecimal::add));
  }

  public Customer() {
  }

  public Customer(String name, BigDecimal creditLimit) {
    this.name = name;
    this.creditLimit = creditLimit;
  }

  public static ResultWithEvents<Customer> create(String name, BigDecimal creditLimit) {
    Customer customer = new Customer(name, creditLimit);
    CustomerCreatedEvent customerCreatedEvent = new CustomerCreatedEvent(customer.getName(), creditLimit);
    return new ResultWithEvents<>(customer, singletonList(customerCreatedEvent));
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getCreditLimit() {
    return creditLimit;
  }

  public void setCreditLimit(BigDecimal creditLimit) {
    this.creditLimit = creditLimit;
  }

  public CreditReservation reserveCredit(Long orderId, BigDecimal orderTotal) {
    if (availableCredit().compareTo(orderTotal) >= 0) {
      CreditReservation creditReservation = new CreditReservation();
      creditReservation.setCredit(orderTotal);
      creditReservation.setOrderId(orderId);
      creditReservation.setCustomer(this);
      creditReservations.add(creditReservation);
      return creditReservation;
    } else
      throw new CustomerCreditLimitExceededException();
  }

  public List<CreditReservation> getCreditReservations() {
    return creditReservations;
  }

  public void setCreditReservations(List<CreditReservation> creditReservations) {
    this.creditReservations = creditReservations;
  }
}
