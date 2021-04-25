package main.response;


public class ResultResponse {
    private final boolean result;

    public ResultResponse(boolean result) {
        this.result = result;
    }

    public boolean isResult() {
        return result;
    }
}