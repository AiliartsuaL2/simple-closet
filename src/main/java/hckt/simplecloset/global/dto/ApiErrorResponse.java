package hckt.simplecloset.global.dto;

public class ApiErrorResponse extends ApiCommonResponse<String> {
    public ApiErrorResponse(String message) {
        super(message, false);
    }
}
