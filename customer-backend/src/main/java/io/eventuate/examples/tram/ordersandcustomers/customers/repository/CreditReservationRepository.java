package io.eventuate.examples.tram.ordersandcustomers.customers.repository;

import io.eventuate.examples.tram.ordersandcustomers.customers.domain.CreditReservation;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface CreditReservationRepository extends CrudRepository<CreditReservation, Long> {
  List<CreditReservation> findAllByCustomerId(Long orderId);
}
