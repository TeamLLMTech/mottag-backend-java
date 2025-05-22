package br.com.mottag.api.dto.common;

public class ApiResponseWithPaginationDTO<Type> extends ApiResponseDTO<Type> {
    private PaginationDTO pagination;

    public ApiResponseWithPaginationDTO() {
        super();
    }

    public ApiResponseWithPaginationDTO(Type data) {
        super(data);
    }

    public ApiResponseWithPaginationDTO(Type data, PaginationDTO pagination) {
        super(data);
        this.pagination = pagination;
    }

    public PaginationDTO getPagination() {
        return pagination;
    }

    public void setPagination(PaginationDTO pagination) {
        this.pagination = pagination;
    }
}
