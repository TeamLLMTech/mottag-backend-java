package br.com.mottag.api.openapi.model;

import br.com.mottag.api.dto.UsuarioResponseDTO;
import br.com.mottag.api.dto.common.ApiResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ApiResponseUsuarioDTO", description = "Resposta da API para uma entidade Usuario")
public class ApiResponseUsuarioDTO extends ApiResponseDTO<UsuarioResponseDTO> {
}
