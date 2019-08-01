package io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend;

import com.mongodb.MongoClient;
import io.micronaut.context.annotation.Factory;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.inject.Singleton;

@Factory
public class OrderHistoryViewMongoFactory {

  @Singleton
  public MongoTemplate mongoTemplate(MongoClient mongoClient) {
    return new MongoTemplate(mongoClient, "customers_and_orders");
  }

  @Singleton
  public CustomerViewRepository customerViewRepository(MongoTemplate mongoTemplate) {
    return new CustomerViewRepository(mongoTemplate);
  }

  @Singleton
  public OrderViewRepository orderViewRepository(MongoTemplate mongoTemplate) {
    return new OrderViewRepository(mongoTemplate);
  }

  @Singleton
  public OrderHistoryViewService orderHistoryViewService(CustomerViewRepository customerViewRepository,
                                                         OrderViewRepository orderViewRepository) {
    return new OrderHistoryViewService(customerViewRepository, orderViewRepository);
  }
}
