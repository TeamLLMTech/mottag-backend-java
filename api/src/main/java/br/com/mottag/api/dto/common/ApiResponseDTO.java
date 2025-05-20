package br.com.mottag.api.dto.common;

import java.util.List;

public class ApiResponseDTO<Type> {
    private Type data;
    // TODO: add warnings if needed
    // private List<String> warnings;
    private PaginationDTO pagination;

    public ApiResponseDTO(Type data) {
        this.data = data;
    }

    public ApiResponseDTO(Type data, PaginationDTO pagination) {
        this.data = data;
        this.pagination = pagination;
    }

    public Type getData() {
        return data;
    }

    public void setData(Type data) {
        this.data = data;
    }

    public PaginationDTO getPagination() {
        return pagination;
    }

    public void setPagination(PaginationDTO pagination) {
        this.pagination = pagination;
    }
}
