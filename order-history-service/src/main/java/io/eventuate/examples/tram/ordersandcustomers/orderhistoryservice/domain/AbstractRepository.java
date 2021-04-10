package io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.domain;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoCommandException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.Money;
import org.bson.Document;
import org.bson.types.Decimal128;

import java.math.BigDecimal;
import java.util.Optional;

public class AbstractRepository {
  private static final String DATABASE = "customers_orders";
  private static final int REPEAT_ON_FAILURE_COUNT = 10;
  private final String collection;

  private MongoClient mongoClient;

  public AbstractRepository(MongoClient mongoClient, String collection) {
    this.mongoClient = mongoClient;
    this.collection = collection;
  }

  protected void findOneAndUpdate(Long id, BasicDBObject newValue) {
    collection().findOneAndUpdate(new BasicDBObject("_id", id),
            new BasicDBObject("$set", newValue),
            upsertOptions());
  }

  protected Optional<Document> findOne(Long id) {
    Document customerDocument = collection()
            .find(new BasicDBObject("_id", id))
            .first();

    return Optional
            .ofNullable(customerDocument);
  }

  protected FindOneAndUpdateOptions upsertOptions() {
    FindOneAndUpdateOptions opts = new FindOneAndUpdateOptions();

    opts.upsert(true);

    return opts;
  }

  protected MongoCollection<Document> collection() {
    return mongoClient
            .getDatabase(DATABASE)
            .getCollection(collection);
  }

  protected Money getMoney(Document document, String field) {
    BigDecimal bigDecimal = Optional
            .ofNullable(document.get(field, org.bson.types.Decimal128.class))
            .map(Decimal128::bigDecimalValue)
            .orElse(new BigDecimal(0));

    return new Money(bigDecimal);
  }

  protected void repeatOnFailure(Runnable action) {
    MongoCommandException mongoCommandException = null;

    for (int i = 0; i < REPEAT_ON_FAILURE_COUNT; i++) {
      try {
        action.run();
        return;
      } catch (MongoCommandException e) {
        mongoCommandException = e;
      }
    }

    throw new RuntimeException(mongoCommandException);
  }
}
