package io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.domain;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.Money;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderState;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.CustomerView;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.OrderInfo;
import org.bson.Document;
import java.util.Optional;

public class CustomerViewRepository extends AbstractRepository {

  public CustomerViewRepository(MongoClient mongoClient) {
    super(mongoClient, "customers");
  }

  public Optional<CustomerView> findById(Long customerId) {
    return findOne(customerId)
            .map(customerDocument -> {
              CustomerView customerView = new CustomerView();

              customerView.setId(customerId);
              customerView.setName(customerDocument.getString("name"));
              customerView.setCreditLimit(getMoney(customerDocument, "creditLimit"));

              Document orders = customerDocument.get("orders", Document.class);

              if (orders != null) {
                orders.forEach((key, value) -> {
                  Long orderId = Long.parseLong(key);

                  Document orderDocument = (Document) value;

                  OrderInfo orderInfo = new OrderInfo(orderId, getMoney(orderDocument, "orderTotal"));

                  Optional
                          .ofNullable(orderDocument.getString("state"))
                          .map(OrderState::valueOf)
                          .ifPresent(orderInfo::setState);

                  customerView.getOrders().put(orderId, orderInfo);
                });
              }

              return customerView;
            });
  }

  public void addCustomer(Long customerId, String customerName, Money creditLimit) {
    repeatOnFailure(() -> {
      BasicDBObject customer = new BasicDBObject()
              .append("name", customerName)
              .append("creditLimit", creditLimit.getAmount());

      findOneAndUpdate(customerId, customer);
    });
  }

  public void addOrder(Long customerId, Long orderId, Money orderTotal) {
    repeatOnFailure(() -> {
      BasicDBObject orderInfo = new BasicDBObject()
              .append("orderId", orderId)
              .append("orderTotal", orderTotal.getAmount());

      findOneAndUpdate(customerId, new BasicDBObject("orders." + orderId, orderInfo));
    });
  }

  public void updateOrderState(Long customerId, Long orderId, OrderState state) {
    repeatOnFailure(() -> {
      findOneAndUpdate(customerId, new BasicDBObject(String.format("orders.%s.state", orderId), state.name()));
    });
  }
}
