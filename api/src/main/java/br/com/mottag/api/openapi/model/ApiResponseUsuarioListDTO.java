package br.com.mottag.api.openapi.model;

import br.com.mottag.api.dto.UsuarioResponseDTO;
import br.com.mottag.api.dto.common.ApiResponseWithPaginationDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "ApiResponseUsuarioListDTO", description = "Resposta da API para uma lista de Usuario")
public class ApiResponseUsuarioListDTO extends ApiResponseWithPaginationDTO<List<UsuarioResponseDTO>> {
}
