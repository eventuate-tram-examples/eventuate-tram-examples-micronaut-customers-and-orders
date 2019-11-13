package io.eventuate.examples.tram.ordersandcustomers.endtoendtests;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderState;
import io.eventuate.examples.tram.ordersandcustomers.customers.webapi.CreateCustomerRequest;
import io.eventuate.examples.tram.ordersandcustomers.customers.webapi.CreateCustomerResponse;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.CustomerView;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.OrderInfo;
import io.eventuate.examples.tram.ordersandcustomers.orders.webapi.CreateOrderRequest;
import io.eventuate.examples.tram.ordersandcustomers.orders.webapi.CreateOrderResponse;
import io.eventuate.examples.tram.ordersandcustomers.orders.webapi.GetOrderResponse;
import io.eventuate.util.test.async.Eventually;
import io.micronaut.context.annotation.Value;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;


@MicronautTest
public class CustomersAndOrdersE2ETest{

  @Value("${docker.host.ip}")
  private String hostName;

  private String baseUrlOrders(String path) {
    return "http://"+hostName+":8081/" + path;
  }

  private String baseUrlCustomers(String path) {
    return "http://" + hostName + ":8082/" + path;
  }

  private String baseUrlOrderHistory(String path) {
    return "http://"+hostName+":8083/" + path;
  }

  @Inject
  private RestTemplate restTemplate;

  @Test
  public void shouldApprove() {
    Long customerId = createCustomer("Fred", new BigDecimal("15.00"));
    Long orderId = createOrder(customerId, new BigDecimal("12.34"));
    assertOrderState(orderId, OrderState.APPROVED);
  }

  @Test
  public void shouldReject() {
    Long customerId = createCustomer("Fred", new BigDecimal("15.00"));
    Long orderId = createOrder(customerId, new BigDecimal("123.34"));
    assertOrderState(orderId, OrderState.REJECTED);
  }

  @Test
  public void shouldRejectForNonExistentCustomerId() {
    Long customerId = System.nanoTime();
    Long orderId = createOrder(customerId, new BigDecimal("123.34"));
    assertOrderState(orderId, OrderState.REJECTED);
  }

  @Test
  public void shouldRejectApproveAndKeepOrdersInHistory() {
    Long customerId = createCustomer("John", new BigDecimal("1000"));

    Long order1Id = createOrder(customerId, new BigDecimal("100"));

    assertOrderState(order1Id, OrderState.APPROVED);

    Long order2Id = createOrder(customerId, new BigDecimal("1000"));

    assertOrderState(order2Id, OrderState.REJECTED);

    Eventually.eventually(100, 400, TimeUnit.MILLISECONDS, () -> {
      CustomerView customerView = getCustomerView(customerId);

      Map<Long, OrderInfo> orders = customerView.getOrders();

      assertEquals(2, orders.size());

      assertEquals(orders.get(order1Id).getState(), OrderState.APPROVED);
      assertEquals(orders.get(order2Id).getState(), OrderState.REJECTED);
    });
  }

  private CustomerView getCustomerView(Long customerId) {
    String customerHistoryUrl = baseUrlOrderHistory("customers") + "/" + customerId;
    ResponseEntity<CustomerView> response = restTemplate.getForEntity(customerHistoryUrl, CustomerView.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());

    Assert.notNull(response);

    return response.getBody();
  }


  private Long createCustomer(String name, BigDecimal credit) {
    return restTemplate.postForObject(baseUrlCustomers("customers"),
            new CreateCustomerRequest(name, credit), CreateCustomerResponse.class).getCustomerId();
  }

  private Long createOrder(Long customerId, BigDecimal orderTotal) {
    return restTemplate.postForObject(baseUrlOrders("orders"),
            new CreateOrderRequest(customerId, orderTotal), CreateOrderResponse.class).getOrderId();
  }

  private void assertOrderState(Long id, OrderState expectedState) {
    Eventually.eventually(100, 400, TimeUnit.MILLISECONDS, () -> {
      ResponseEntity<GetOrderResponse> response =
              restTemplate.getForEntity(baseUrlOrders("orders/" + id), GetOrderResponse.class);

      assertEquals(HttpStatus.OK, response.getStatusCode());

      GetOrderResponse order = response.getBody();

      assertEquals(expectedState, order.getOrderState());
    });
  }
}
