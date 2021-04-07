package io.eventuate.examples.tram.ordersandcustomers.orders.web;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderDetails;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.Order;
import io.eventuate.examples.tram.ordersandcustomers.orders.service.OrderService;
import io.eventuate.examples.tram.ordersandcustomers.orders.webapi.CreateOrderRequest;
import io.eventuate.examples.tram.ordersandcustomers.orders.webapi.CreateOrderResponse;
import io.eventuate.examples.tram.ordersandcustomers.orders.webapi.GetOrderResponse;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.transaction.annotation.TransactionalAdvice;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Controller
public class OrderController {

  @Inject
  private OrderService orderService;

  @PersistenceContext
  private EntityManager entityManager;

  @Post(value = "/orders")
  public CreateOrderResponse createOrder(CreateOrderRequest createOrderRequest) {
    Order order = orderService.createOrder(new OrderDetails(createOrderRequest.getCustomerId(), createOrderRequest.getOrderTotal()));
    return new CreateOrderResponse(order.getId());
  }

  @Get("/orders/{orderId}")
  @TransactionalAdvice
  public HttpResponse<GetOrderResponse> getOrder(Long orderId) {
     return Optional.ofNullable(entityManager.find(Order.class, orderId))
            .map(order -> HttpResponse.ok(new GetOrderResponse(order.getId(), order.getState())))
            .orElseGet(HttpResponse::notFound);
  }
}
