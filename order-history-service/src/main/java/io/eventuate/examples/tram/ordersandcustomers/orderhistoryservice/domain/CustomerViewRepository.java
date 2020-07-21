package io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.domain;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.Money;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderState;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.CustomerView;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.OrderInfo;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;

public class CustomerViewRepository {

  private MongoTemplate mongoTemplate;

  public CustomerViewRepository(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public Optional<CustomerView> findById(Long customerId) {
    return Optional.ofNullable(mongoTemplate.findById(customerId, CustomerView.class));
  }

  public void addCustomer(Long customerId, String customerName, Money creditLimit) {
    mongoTemplate.upsert(new Query(where("id").is(customerId)),
            new Update().set("name", customerName).set("creditLimit", creditLimit), CustomerView.class);
  }

  public void addOrder(Long customerId, Long orderId, Money orderTotal) {
    mongoTemplate.upsert(new Query(where("id").is(customerId)),
            new Update().set("orders." + orderId, new OrderInfo(orderId, orderTotal)), CustomerView.class);
  }

  public void updateOrderState(Long customerId, Long orderId, OrderState state) {
    mongoTemplate.upsert(new Query(where("id").is(customerId)),
            new Update().set("orders." + orderId + ".state", state), CustomerView.class);
  }
}
