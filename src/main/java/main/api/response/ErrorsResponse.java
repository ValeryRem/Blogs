package main.api.response;
import java.util.Map;

public class ErrorsResponse {

    private Map<String, String> errors;

    public ErrorsResponse() {
    }

    public ErrorsResponse(Map<String, String> errors) {
        this.errors = errors;
    }

//    public boolean isResult() {
//        return result;
//    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
