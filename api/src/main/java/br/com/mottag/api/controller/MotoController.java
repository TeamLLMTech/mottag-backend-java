package br.com.mottag.api.controller;

import br.com.mottag.api.dto.MotoRequestDTO;
import br.com.mottag.api.dto.MotoResponseDTO;
import br.com.mottag.api.dto.common.ApiErrorDTO;
import br.com.mottag.api.dto.common.ApiResponseDTO;
import br.com.mottag.api.dto.common.ApiResponseWithPaginationDTO;
import br.com.mottag.api.dto.common.PaginationDTO;
import br.com.mottag.api.model.StatusMoto;
import br.com.mottag.api.openapi.model.ApiResponseMotoDTO;
import br.com.mottag.api.openapi.model.ApiResponseMotoListDTO;
import br.com.mottag.api.openapi.model.ApiResponsePatioDTO;
import br.com.mottag.api.openapi.model.ApiResponsePatioListDTO;
import br.com.mottag.api.service.MotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/motos")
@Tag(name = "motos", description = "CRUD de Motos")
public class MotoController {

    private final MotoService motoService;

    public MotoController(MotoService motoService) {
        this.motoService = motoService;
    }

    @Operation(summary = "Cria uma nova moto")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Moto criada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiResponseMotoDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parâmetros informados são inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiErrorDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Parâmetros informados não atendem aos requisitos esperados",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiErrorDTO.class
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<MotoResponseDTO> createMoto(@Valid @RequestBody MotoRequestDTO dto) {
        return new ResponseEntity<MotoResponseDTO>(
                this.motoService.save(dto),
                HttpStatus.CREATED
        );
    }

    @Operation(
            summary = "Lista todas as motos",
            parameters = {
                    @Parameter(name = "idPatio", description = "Filtra motos de um pátio específico"),
                    @Parameter(name = "status", description = "Filtra motos de um status específico"),
                    @Parameter(name = "page", description = "Número da página (0..N)", example = "0"),
                    @Parameter(name = "size", description = "Quantidade de elementos por página", example = "10"),
                    @Parameter(name = "sort", description = "Critério de ordenação no formato: propriedade[,asc|desc]. " +
                            "Pode ser usado múltiplas vezes, por exemplo: sort=nome,asc&sort=id,desc")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Motos listadas com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiResponseMotoListDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parâmetros informados são inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiErrorDTO.class
                            )
                    )
            )
    })
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<MotoResponseDTO>>> findAllMoto(
            @Parameter(hidden = true) @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(required = false) Long idPatio,
            @RequestParam(required = false) StatusMoto status
    ) {
        Page<MotoResponseDTO> motos = this.motoService.findAll(pageable, idPatio, status);
        PaginationDTO pagination = PaginationDTO.fromPage(motos);
        return new ResponseEntity<>(
                new ApiResponseWithPaginationDTO<>(motos.getContent(), pagination),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Retorna uma moto por id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Moto encontrada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiResponseMotoDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parâmetros informados são inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiErrorDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Moto não encontrada para o ID fornecido",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiErrorDTO.class
                            )
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<MotoResponseDTO> findByIdMoto(
            @Parameter(name = "id", description = "ID da moto", example = "1")
            @PathVariable Long id
    ) {
        return new ResponseEntity<MotoResponseDTO>(
                this.motoService.findById(id),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Atualiza uma moto")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Moto atualizada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiResponseMotoDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parâmetros informados são inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiErrorDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Moto não encontrada para o ID fornecido",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiErrorDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Parâmetros informados não atendem aos requisitos esperados",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiErrorDTO.class
                            )
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<MotoResponseDTO> updateMoto(
            @Parameter(name = "id", description = "ID da moto", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody MotoRequestDTO dto
    ) {
        return new ResponseEntity<MotoResponseDTO>(
                this.motoService.update(id, dto),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Deleta uma moto")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Moto deletada com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parâmetros informados são inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiErrorDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Moto não encontrada para o ID fornecido",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiErrorDTO.class
                            )
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMoto(
            @Parameter(name = "id", description = "ID da moto", example = "1")
            @PathVariable Long id
    ) {
        this.motoService.delete(id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
