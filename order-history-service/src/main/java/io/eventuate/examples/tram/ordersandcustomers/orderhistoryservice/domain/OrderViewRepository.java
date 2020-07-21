package io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.domain;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.Money;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderState;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.OrderView;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;

public class OrderViewRepository {

  private MongoTemplate mongoTemplate;

  public OrderViewRepository(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public Optional<OrderView> findById(Long orderId) {
    return Optional.ofNullable(mongoTemplate.findById(orderId, OrderView.class));
  }

  public void addOrder(Long orderId, Money orderTotal) {
    mongoTemplate.upsert(new Query(where("id").is(orderId)),
            new Update().set("orderTotal", orderTotal), OrderView.class);
  }

  public void updateOrderState(Long orderId, OrderState state) {
    mongoTemplate.updateFirst(new Query(where("id").is(orderId)),
            new Update().set("state", state), OrderView.class);
  }
}
