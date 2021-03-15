package main.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;

public class GetModerationRequest implements Serializable {
    @JsonProperty
    Integer offset;

    @JsonProperty
    Integer limit;

    @JsonProperty
    String mode;

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
