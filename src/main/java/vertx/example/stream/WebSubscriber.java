package vertx.example.stream;

import io.vertx.core.AbstractVerticle;
import vertx.example.stream.message.MyMessage;
import vertx.example.stream.message.MyMessageCodec;
import vertx.example.stream.util.Runner;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;


public class WebSubscriber extends AbstractVerticle {

    // Convenience method so you can run it in your IDE
    public static void main(String[] args) {
        Runner.run(WebSubscriber.class);
    }

    @Override
    public void start() {
        Router router = Router.router(vertx);
        router.get("/events").handler(this::handler);
        vertx.eventBus().registerDefaultCodec(MyMessage.class, new MyMessageCodec());

        vertx.createHttpServer().requestHandler(router::accept).listen(8080);

    }


    private void handler(RoutingContext routingContext) {
        routingContext.response().setChunked(true);

        routingContext.response().headers().add("Content-Type", "text/event-stream;charset=UTF-8");
        routingContext.response().headers().add("Connection", "keep-alive");

        vertx.eventBus().consumer("events", msg -> {
            if (!routingContext.response().closed())
                routingContext.response().write("event: message\ndata: " + msg.body() + "\n\n");
        });
    }
}
