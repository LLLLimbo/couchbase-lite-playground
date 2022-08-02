package cn.limbo;

import com.couchbase.lite.CouchbaseLite;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.DataSource;
import com.couchbase.lite.Database;
import com.couchbase.lite.DatabaseConfiguration;
import com.couchbase.lite.Expression;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryBuilder;
import com.couchbase.lite.Result;
import com.couchbase.lite.SelectResult;

public class Check {

  public static void main(String[] args) {
    CouchbaseLite.init();

    // Create a database
    System.out.println("Starting DB");
    DatabaseConfiguration cfg = new DatabaseConfiguration();
    Database database = null;
    try {
      database = new Database("traveldb", cfg);
    } catch (CouchbaseLiteException e) {
      e.printStackTrace();
    }
    assert database != null;
    Query query = QueryBuilder
        .select(SelectResult.all())
        .from(DataSource.database(database))
        .where(Expression.property("type").equalTo(Expression.string("hotel")));
    try {
      for (Result result : query.execute()) {
        System.out.println(result.toJSON());
        System.out.println(result.getDictionary("traveldb").getString("name"));
      }
    } catch (CouchbaseLiteException e) {
      System.out.println(e.getInfo());
    }
  }

}
