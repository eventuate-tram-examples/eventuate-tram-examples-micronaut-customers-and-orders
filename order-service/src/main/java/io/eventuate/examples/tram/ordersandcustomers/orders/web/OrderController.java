package io.eventuate.examples.tram.ordersandcustomers.orders.web;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderDetails;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.Order;
import io.eventuate.examples.tram.ordersandcustomers.orders.repositroy.OrderRepository;
import io.eventuate.examples.tram.ordersandcustomers.orders.service.OrderService;
import io.eventuate.examples.tram.ordersandcustomers.orders.webapi.CreateOrderRequest;
import io.eventuate.examples.tram.ordersandcustomers.orders.webapi.CreateOrderResponse;
import io.eventuate.examples.tram.ordersandcustomers.orders.webapi.GetOrderResponse;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;

import javax.transaction.Transactional;

@Controller
public class OrderController {

  private OrderService orderService;
  private OrderRepository orderRepository;

  public OrderController(OrderService orderService, OrderRepository orderRepository) {
    this.orderService = orderService;
    this.orderRepository = orderRepository;
  }

  @Post(value = "/orders")
  public CreateOrderResponse createOrder(CreateOrderRequest createOrderRequest) {
    Order order = orderService.createOrder(new OrderDetails(createOrderRequest.getCustomerId(), createOrderRequest.getOrderTotal()));
    return new CreateOrderResponse(order.getId());
  }

  @Get("/orders/{orderId}")
  @Transactional
  public HttpResponse<GetOrderResponse> getOrder(Long orderId) {
     return orderRepository.findById(orderId)
            .map(order -> HttpResponse.ok(new GetOrderResponse(order.getId(), order.getState())))
            .orElseGet(HttpResponse::notFound);
  }
}
