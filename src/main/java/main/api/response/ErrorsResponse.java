package main.api.response;

import java.util.LinkedHashMap;
import java.util.Map;

public class ErrorsResponse {
    private boolean result  ;
    private LinkedHashMap<String, Object> errors;

    public ErrorsResponse() {
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public LinkedHashMap<String, Object> getErrors() {
        return errors;
    }

    public void setErrors(LinkedHashMap<String, Object> errors) {
        this.errors = errors;
    }
}
