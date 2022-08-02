package cn.limbo;

import com.couchbase.lite.BasicAuthenticator;
import com.couchbase.lite.CouchbaseLite;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.DatabaseConfiguration;
import com.couchbase.lite.Replicator;
import com.couchbase.lite.ReplicatorConfiguration;
import com.couchbase.lite.ReplicatorType;
import com.couchbase.lite.URLEndpoint;
import java.net.URI;
import java.net.URISyntaxException;

public class Start {

  public static void main(String[] args) throws URISyntaxException, InterruptedException {

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

    ReplicatorConfiguration replConfig =
        new ReplicatorConfiguration(database, new
            URLEndpoint(new URI("ws://192.168.14.149:4984/traveldb")));

    replConfig.setAuthenticator(
            new BasicAuthenticator("iotd-knshi38h", "a8996855439".toCharArray()))
        .setType(ReplicatorType.PULL)
//        .setChannels(List.of("*"))
        .setContinuous(true)
        .setAutoPurgeEnabled(true);

    Replicator replicator = new Replicator(replConfig);

    replicator.addDocumentReplicationListener(
        documentReplication -> documentReplication.getDocuments()
            .forEach(replicatedDocument -> System.out.println(replicatedDocument.getID())));

    //Start replication.
    replicator.start(false);
  }


}


