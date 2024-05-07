package hckt.simplecloset.global.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class ApiCommonResponse<T> {
    private T result;
    private boolean success;

    public ApiCommonResponse(T result, boolean success) {
        this.result = result;
        this.success = success;
    }
}
