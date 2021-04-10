package io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.domain;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.Money;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

@MicronautTest(transactional = false)
public class RepeatOnFailureTest {

  @Inject
  CustomerViewRepository customerViewRepository;

  private final int orderCount = 1000;
  private final int reproductionAttempts = 10;
  private long customerId;

  private List<CompletableFuture<Void>> orderCreators = new ArrayList<>();
  private Executor executor = Executors.newCachedThreadPool();

  @Test//takes about 2-3 sec
  void testConcurrentOrderAdding() {
    // sometimes not reproducible in single iteration
    for (int i = 0; i < reproductionAttempts; i++) {
      customerId = System.nanoTime();
      createOrders();
      waitForOrderCreation();
      assertAllOrdersCreated();
    }
  }

  private void createOrders() {
    AtomicLong id = new AtomicLong();
    int mod = 10;
    for (int i = 0; i < orderCount/mod; i++) {
      final long iteration = i;
      orderCreators.add(CompletableFuture.runAsync(() -> {
        for (int j = 0; j < mod; j++) customerViewRepository.addOrder(customerId, id.incrementAndGet(), new Money(1));
      }, executor));
    }
  }

  private void waitForOrderCreation() {
    orderCreators.forEach(cf -> {
      try {
        cf.get();
      } catch (InterruptedException | ExecutionException e) {
        throw new RuntimeException(e);
      }
    });
  }

  private void assertAllOrdersCreated() {
    Assertions.assertEquals(orderCount, customerViewRepository.findById(customerId).get().getOrders().size());
  }
}
