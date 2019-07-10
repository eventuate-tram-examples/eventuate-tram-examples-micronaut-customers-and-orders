package io.eventuate.examples.tram.ordersandcustomers.orders;

import io.eventuate.common.jdbc.micronaut.EventuateMicronautTransactionManagement;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;
import java.sql.Connection;
import java.util.function.Consumer;

@Singleton
public class EventuateMicronautJpaTransactionManagement implements EventuateMicronautTransactionManagement {

  @Inject
  private DataSource dataSource;

  @Override
  public void doWithTransaction(Consumer<Connection> consumer) {
    consumer.accept(DataSourceUtils.getConnection(dataSource));
  }
}
