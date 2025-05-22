package br.com.mottag.api.dto.common;

public class ApiResponseDTO<Type> {
    private Type data;
    // TODO: add warnings if needed
    // private List<String> warnings;

    public ApiResponseDTO() {
    }

    public ApiResponseDTO(Type data) {
        this.data = data;
    }

    public Type getData() {
        return data;
    }

    public void setData(Type data) {
        this.data = data;
    }
}
