package br.com.mottag.api.config;

import br.com.mottag.api.dto.common.ApiErrorDTO;
import br.com.mottag.api.dto.common.ApiResponseDTO;
import br.com.mottag.api.dto.common.ApiResponseWithPaginationDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class ApiResponseWrapperAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return !returnType.getParameterType().equals(ApiResponseDTO.class)
                && !returnType.getParameterType().equals(ApiResponseWithPaginationDTO.class)
                && !returnType.getParameterType().equals(ApiErrorDTO.class);
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        if (isSwaggerRequest(request)) {
            return body;
        }

        if (body instanceof ApiResponseDTO || body instanceof ApiErrorDTO) {
            return body;
        }

        return new ApiResponseDTO<>(body);
    }

    private boolean isSwaggerRequest(ServerHttpRequest request) {
        String path = request.getURI().getPath();
        return path.contains("/swagger") || path.contains("/v3/api-docs");
    }

}
