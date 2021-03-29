package main.api.response;
import java.util.Map;

public class ErrorsResponse {
    private final boolean result = false ;
    private final Map<String, String> errors;

    public ErrorsResponse(Map<String, String> errors) {
        this.errors = errors;
    }

    public boolean isResult() {
        return result;
    }

    public Map<String, String> getErrors() {
        return errors;
    }


}
