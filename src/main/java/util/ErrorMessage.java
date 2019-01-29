package util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author: brianfroschauer
 * Date: 2019-01-28
 */
public class ErrorMessage {

    private Integer status;
    private String message;

    @JsonCreator
    public ErrorMessage(@JsonProperty("status") Integer status,
                        @JsonProperty("message") String message) {
        this.status = status;
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}