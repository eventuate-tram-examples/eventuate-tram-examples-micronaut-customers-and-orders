package io.eventuate.examples.tram.ordersandcustomers.customers.domain;


import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Credit_Reservation")
public class CreditReservation {
  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne
  private Customer customer;

  private Long orderId;

  private BigDecimal credit;

  public CreditReservation() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public Long getOrderId() {
    return orderId;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }

  public BigDecimal getCredit() {
    return credit;
  }

  public void setCredit(BigDecimal credit) {
    this.credit = credit;
  }
}
