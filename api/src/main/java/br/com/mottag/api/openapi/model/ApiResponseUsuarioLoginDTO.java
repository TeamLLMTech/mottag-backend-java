package br.com.mottag.api.openapi.model;

import br.com.mottag.api.dto.UsuarioLoginResponseDTO;
import br.com.mottag.api.dto.UsuarioResponseDTO;
import br.com.mottag.api.dto.common.ApiResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ApiResponseUsuarioLoginDTO", description = "Resposta da API para um login")
public class ApiResponseUsuarioLoginDTO extends ApiResponseDTO<UsuarioLoginResponseDTO> {
}
