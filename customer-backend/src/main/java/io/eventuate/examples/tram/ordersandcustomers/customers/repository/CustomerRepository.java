package io.eventuate.examples.tram.ordersandcustomers.customers.repository;

import io.eventuate.examples.tram.ordersandcustomers.customers.domain.Customer;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import java.math.BigDecimal;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface CustomerRepository extends CrudRepository<Customer, Long> {
  void updateById(Long id, BigDecimal creditLimit);
}
