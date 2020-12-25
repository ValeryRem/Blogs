package main.api.response;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class ErrorsResponse {
    boolean result;
    Map<String, Object> errors = new LinkedHashMap<>();

    public ErrorsResponse() {
    }

    public ErrorsResponse(boolean result, Map<String, Object> errors) {
        this.result = result;
        this.errors = errors;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public Map<String, Object> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, Object> errors) {
        this.errors = errors;
    }
}
