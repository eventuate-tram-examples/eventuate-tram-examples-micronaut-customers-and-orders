package io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.web.orders;

import io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend.OrderViewRepository;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.OrderView;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller
public class OrderViewController {

  private OrderViewRepository orderViewRepository;

  public OrderViewController(OrderViewRepository orderViewRepository) {
    this.orderViewRepository = orderViewRepository;
  }

  @Get("/orders/{orderId}")
  public HttpResponse<OrderView> getCustomer(Long orderId) {
    return orderViewRepository
            .findById(orderId)
            .map(HttpResponse::ok)
            .orElseGet(HttpResponse::notFound);
  }
}
