package vertx.example.stream;

import io.vertx.core.AbstractVerticle;
import vertx.example.stream.message.MyMessage;
import vertx.example.stream.message.MyMessageCodec;
import vertx.example.stream.util.Runner;


public class Publisher extends AbstractVerticle {

    // Convenience method so you can run it in your IDE
    public static void main(String[] args) {
        Runner.run(Publisher.class);
    }


    @Override
    public void start() {
        vertx.eventBus().registerDefaultCodec(MyMessage.class, new MyMessageCodec());

        vertx.setPeriodic(5000, time -> {
            MyMessage msg = MyMessage.builder()
                    .id(System.currentTimeMillis())
                    .statusCode(200)
                    .summary("Message sent from publisher!")
                    .build();

            vertx.eventBus().publish("events", msg);
            System.out.println("sent " + msg);
        });

    }
}
