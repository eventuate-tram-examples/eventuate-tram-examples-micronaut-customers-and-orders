package io.eventuate.examples.tram.ordersandcustomers.orders.repositroy;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderState;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.Order;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface OrderRepository extends CrudRepository<Order, Long> {
  void updateById(Long id, OrderState state);
}
