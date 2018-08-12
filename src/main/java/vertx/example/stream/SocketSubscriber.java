package vertx.example.stream;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;
import vertx.example.stream.message.MyMessage;
import vertx.example.stream.message.MyMessageCodec;
import vertx.example.stream.util.Runner;


public class SocketSubscriber extends AbstractVerticle {

    public static void main(String[] args) {
        Runner.run(SocketSubscriber.class);
    }

    @Override
    public void start() {
        vertx.eventBus().registerDefaultCodec(MyMessage.class, new MyMessageCodec());

        NetServer server = vertx.createNetServer();

        server.connectHandler(new Handler<NetSocket>() {

            @Override
            public void handle(NetSocket netSocket) {
                System.out.println("Incoming connection!");


                vertx.eventBus().consumer("events", msg -> {
                    netSocket.write(msg.body().toString());
                });
            }
        });

        server.listen(1234);
    }
}
