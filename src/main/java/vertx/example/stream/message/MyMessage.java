package vertx.example.stream.message;

import lombok.Builder;
import lombok.ToString;

@Builder
@ToString
public class MyMessage {
    private int statusCode;
    private long id;
    private String summary;


    public int getStatusCode() {
        return statusCode;
    }

    public long getId() {
        return id;
    }

    public String getSummary() {
        return summary;
    }
}