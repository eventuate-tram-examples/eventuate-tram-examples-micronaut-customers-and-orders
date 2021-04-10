package io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.domain;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.Money;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderState;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.OrderView;
import java.util.Optional;

public class OrderViewRepository extends AbstractRepository {

  public OrderViewRepository(MongoClient mongoClient) {
    super(mongoClient, "orders");
  }

  public Optional<OrderView> findById(Long orderId) {
    return findOne(orderId)
            .map(orderDocument -> {

              OrderView orderView = new OrderView(orderId, getMoney(orderDocument, "orderTotal"));

              Optional
                      .ofNullable(orderDocument.getString("state"))
                      .map(OrderState::valueOf).ifPresent(orderView::setState);

              return orderView;
            });
  }

  public void addOrder(Long orderId, Money orderTotal) {
    repeatOnFailure(() -> {
      findOneAndUpdate(orderId, new BasicDBObject("orderTotal", orderTotal.getAmount()));
    });
  }

  public void updateOrderState(Long orderId, OrderState state) {
    repeatOnFailure(() -> {
      findOneAndUpdate(orderId, new BasicDBObject("state", state.name()));
    });
  }
}
