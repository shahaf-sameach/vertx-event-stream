package vertx.example.stream.message;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.JsonObject;

/**
 * Vert.x core example for {@link io.vertx.core.eventbus.EventBus} and {@link MessageCodec}
 * @author Junbong
 */
public class MyMessageCodec implements MessageCodec<MyMessage, MyMessage> {
    @Override
    public void encodeToWire(Buffer buffer, MyMessage myMessage) {
        // Easiest ways is using JSON object
        JsonObject jsonToEncode = new JsonObject();
        jsonToEncode.put("statusCode", myMessage.getStatusCode());
        jsonToEncode.put("id", myMessage.getId());
        jsonToEncode.put("summary", myMessage.getSummary());

        // Encode object to string
        String jsonToStr = jsonToEncode.encode();

        // Length of JSON: is NOT characters count
        int length = jsonToStr.getBytes().length;

        // Write data into given buffer
        buffer.appendInt(length);
        buffer.appendString(jsonToStr);
    }

    @Override
    public MyMessage decodeFromWire(int position, Buffer buffer) {
        // My custom message starting from this *position* of buffer
        int _pos = position;

        // Length of JSON
        int length = buffer.getInt(_pos);

        // Get JSON string by it`s length
        // Jump 4 because getInt() == 4 bytes
        String jsonStr = buffer.getString(_pos+=4, _pos+=length);
        JsonObject contentJson = new JsonObject(jsonStr);

        // Get fields
        int statusCode = contentJson.getInteger("statusCode");
        long resultCode = contentJson.getLong("id");
        String summary = contentJson.getString("summary");

        // We can finally create custom message object
        return MyMessage.builder()
                .id(resultCode)
                .statusCode(statusCode)
                .summary(summary)
                .build();
    }

    @Override
    public MyMessage transform(MyMessage myMessage) {
        // If a message is sent *locally* across the event bus.
        // This example sends message just as is
        return myMessage;
    }

    @Override
    public String name() {
        // Each codec must have a unique name.
        // This is used to identify a codec when sending a message and for unregistering codecs.
        return this.getClass().getSimpleName();
    }

    @Override
    public byte systemCodecID() {
        // Always -1
        return -1;
    }
}