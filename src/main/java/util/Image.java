package util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Image {

    private final String code;

    @JsonCreator
    public Image(@JsonProperty("username") String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
