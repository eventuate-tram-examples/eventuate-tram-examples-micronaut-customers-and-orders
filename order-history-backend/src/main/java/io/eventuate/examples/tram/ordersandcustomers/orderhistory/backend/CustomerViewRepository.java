package io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderState;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.CustomerView;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.OrderInfo;
import org.bson.Document;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CustomerViewRepository {

  private MongoCollection<Document> customerViewCollection;

  public CustomerViewRepository(MongoCollection<Document> customerViewCollection) {
    this.customerViewCollection = customerViewCollection;
  }

  public Optional<CustomerView> findById(Long customerId) {

    BasicDBObject queryObject = new BasicDBObject();
    queryObject.put("_id", customerId);
    FindIterable<Document> findIterable = customerViewCollection.find(queryObject);

    for (Document document : findIterable) {
      CustomerView customerView = new CustomerView();

      Long id = document.getLong("_id");
      String name = document.getString("name");
      BigDecimal creditLimit = new BigDecimal(document.getString("creditLimit"));

      Document ordersDocument = (Document)document.get("order");

      if (ordersDocument != null) {
        Map<Long, OrderInfo> orders = new HashMap<>();

        for (Object orderObject : ordersDocument.values()) {
          Document orderDocument = (Document) orderObject;

          OrderInfo orderInfo = new OrderInfo();

          orderInfo.setOrderId(orderDocument.getLong("orderId"));
          orderInfo.setState(OrderState.valueOf(orderDocument.getString("state")));
          String orderTotal = orderDocument.getString("orderTotal");
          if (orderTotal != null) {
            orderInfo.setOrderTotal(new BigDecimal(orderTotal));
          }

          orders.put(orderInfo.getOrderId(), orderInfo);
        }

        customerView.setOrders(orders);
      }

      customerView.setId(id);
      customerView.setName(name);
      customerView.setCreditLimit(creditLimit);

      return Optional.of(customerView);
    }

    return Optional.empty();
  }

  public void addCustomer(Long customerId, String customerName, BigDecimal creditLimit) {
    BasicDBObject queryObject = new BasicDBObject();
    queryObject.put("_id", customerId);

    BasicDBObject customerObject = new BasicDBObject();
    customerObject.put("_id", customerId);
    customerObject.put("name", customerName);
    customerObject.put("creditLimit", creditLimit.toString());

    BasicDBObject updateObject = new BasicDBObject();
    updateObject.put("$set", customerObject);

    customerViewCollection.updateOne(queryObject, updateObject, new UpdateOptions().upsert(true));
  }

  public void addOrder(Long customerId, Long orderId, BigDecimal orderTotal) {

    BasicDBObject queryObject = new BasicDBObject();
    queryObject.put("_id", customerId);

    BasicDBObject customerObject = new BasicDBObject();
    customerObject.put("_id", customerId);

    BasicDBObject orderInfoObject = new BasicDBObject();
    orderInfoObject.put("orderId", orderId);
    orderInfoObject.put("orderTotal", orderTotal.toString());
    orderInfoObject.put("state", OrderState.PENDING.toString());

    customerObject.put("order." + orderId, orderInfoObject);

    BasicDBObject updateObject = new BasicDBObject();
    updateObject.put("$set", customerObject);

    customerViewCollection.updateOne(queryObject, updateObject, new UpdateOptions().upsert(true));
  }

  public void updateOrderState(Long customerId, Long orderId, OrderState state) {

    BasicDBObject queryObject = new BasicDBObject();
    queryObject.put("_id", customerId);

    BasicDBObject customerObject = new BasicDBObject();

    customerObject.put("order." + orderId + ".state" , state.toString());

    BasicDBObject updateObject = new BasicDBObject();
    updateObject.put("$set", customerObject);

    customerViewCollection.updateOne(queryObject, updateObject, new UpdateOptions().upsert(true));
  }
}
