package vertx.example.stream.util;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Runner {

    public static void run(Class clazz)  {
        VertxOptions options = new VertxOptions();
        try {
            String local = InetAddress.getLocalHost().getHostAddress();
            options.setClusterHost(local);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        options.setClustered(true);

        Vertx.clusteredVertx(options, res -> {
            if (res.succeeded()) {
                Vertx vertx = res.result();
                vertx.deployVerticle(clazz.getName());
                System.out.println("Deployed " + clazz.getName());
            } else {
                res.cause().printStackTrace();
            }
        });
    }
}