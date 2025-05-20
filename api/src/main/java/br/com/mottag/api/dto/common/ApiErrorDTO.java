package br.com.mottag.api.dto.common;

import java.util.List;

public class ApiErrorDTO {
    private String error;
    private List<String> details;
    // TODO: add fields if needed
    // private String code;
    // private LocalDateTime timestamp;
    // private int status;
    // private String path;


    public ApiErrorDTO(String error) {
        this.error = error;
    }

    public ApiErrorDTO(String error, List<String> details) {
        this.error = error;
        this.details = details;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }
}
