package util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author: brianfroschauer
 * Date: 2019-01-28
 */
public class ErrorMessage {

    private String status;
    private String message;

    @JsonCreator
    public ErrorMessage(@JsonProperty("status") String status,
                        @JsonProperty("message") String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
