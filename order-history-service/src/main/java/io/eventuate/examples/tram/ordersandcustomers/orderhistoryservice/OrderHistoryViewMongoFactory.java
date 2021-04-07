package io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice;

import com.mongodb.client.MongoClient;
import io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.domain.CustomerViewRepository;
import io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.domain.OrderViewRepository;
import io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.service.OrderHistoryViewService;
import io.micronaut.context.annotation.Factory;

import javax.inject.Singleton;

@Factory
public class OrderHistoryViewMongoFactory {


  @Singleton
  public CustomerViewRepository customerViewRepository(MongoClient mongoClient) {
    return new CustomerViewRepository(mongoClient);
  }

  @Singleton
  public OrderViewRepository orderViewRepository(MongoClient mongoClient) {
    return new OrderViewRepository(mongoClient);
  }

  @Singleton
  public OrderHistoryViewService orderHistoryViewService(CustomerViewRepository customerViewRepository,
                                                         OrderViewRepository orderViewRepository) {
    return new OrderHistoryViewService(customerViewRepository, orderViewRepository);
  }
}
