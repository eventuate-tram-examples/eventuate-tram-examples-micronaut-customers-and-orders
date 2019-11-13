package io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderState;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.OrderView;
import org.bson.Document;

import java.math.BigDecimal;
import java.util.Optional;

public class OrderViewRepository {

  private MongoCollection<Document> orderViewCollection;

  public OrderViewRepository(MongoCollection<Document> orderViewCollection) {
    this.orderViewCollection = orderViewCollection;
  }

  public Optional<OrderView> findById(Long orderId) {
    BasicDBObject searchObject = new BasicDBObject();
    searchObject.put("_id", orderId);
    FindIterable<Document> findIterable = orderViewCollection.find(searchObject);

    for (Document document : findIterable) {
      Long id = document.getLong("_id");
      OrderState state = OrderState.valueOf(document.getString("state"));
      BigDecimal orderTotal = new BigDecimal(document.getString("orderTotal"));

      OrderView orderView = new OrderView();
      orderView.setId(id);
      orderView.setState(state);
      orderView.setOrderTotal(orderTotal);

      return Optional.of(orderView);
    }

    return Optional.empty();
  }

  public void addOrder(Long orderId, BigDecimal orderTotal) {
    BasicDBObject queryObject = new BasicDBObject();
    queryObject.put("_id", orderId);

    BasicDBObject orderObject = new BasicDBObject();
    orderObject.put("_id", orderId);
    orderObject.put("orderTotal", orderTotal.toString());

    BasicDBObject updateObject = new BasicDBObject();
    updateObject.put("$set", orderObject);

    orderViewCollection.updateOne(queryObject, updateObject, new UpdateOptions().upsert(true));
  }

  public void updateOrderState(Long orderId, OrderState state) {
    BasicDBObject queryObject = new BasicDBObject();
    queryObject.put("_id", orderId);

    BasicDBObject orderObject = new BasicDBObject();
    orderObject.put("_id", orderId);
    orderObject.put("state", state.toString());

    BasicDBObject updateObject = new BasicDBObject();
    updateObject.put("$set", orderObject);

    orderViewCollection.updateOne(queryObject, updateObject, new UpdateOptions().upsert(true));
  }
}
