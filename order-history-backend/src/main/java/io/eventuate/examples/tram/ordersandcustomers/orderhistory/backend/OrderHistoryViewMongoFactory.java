package io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import io.micronaut.context.annotation.Factory;

import javax.inject.Singleton;

@Factory
public class OrderHistoryViewMongoFactory {

  @Singleton
  public MongoDatabase mongoDatabase(MongoClient mongoClient) {
    return mongoClient.getDatabase("customers_and_orders");
  }

  @Singleton
  public CustomerViewRepository customerViewRepository(MongoDatabase mongoDatabase) {
    return new CustomerViewRepository(mongoDatabase.getCollection("customerView"));
  }

  @Singleton
  public OrderViewRepository orderViewRepository(MongoDatabase mongoDatabase) {
    return new OrderViewRepository(mongoDatabase.getCollection("orderView"));
  }

  @Singleton
  public OrderHistoryViewService orderHistoryViewService(CustomerViewRepository customerViewRepository,
                                                         OrderViewRepository orderViewRepository) {
    return new OrderHistoryViewService(customerViewRepository, orderViewRepository);
  }
}
