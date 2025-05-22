package br.com.mottag.api.controller;

import br.com.mottag.api.dto.PatioRequestDTO;
import br.com.mottag.api.dto.PatioResponseDTO;
import br.com.mottag.api.dto.common.ApiErrorDTO;
import br.com.mottag.api.dto.common.ApiResponseDTO;
import br.com.mottag.api.dto.common.ApiResponseWithPaginationDTO;
import br.com.mottag.api.dto.common.PaginationDTO;
import br.com.mottag.api.openapi.model.ApiResponsePatioDTO;
import br.com.mottag.api.openapi.model.ApiResponsePatioListDTO;
import br.com.mottag.api.service.PatioService;
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
@RequestMapping("/patios")
@Tag(name = "patios", description = "CRUD de Pátios")
public class PatioController {

    private final PatioService patioService;

    public PatioController(PatioService patioService) {
        this.patioService = patioService;
    }

    @Operation(summary = "Cria um novo pátio")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Pátio criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiResponsePatioDTO.class
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
    @PostMapping
    public ResponseEntity<PatioResponseDTO> createPatio(@Valid @RequestBody PatioRequestDTO dto) {
        return new ResponseEntity<PatioResponseDTO>(
                this.patioService.save(dto),
                HttpStatus.CREATED
        );
    }

    @Operation(
            summary = "Lista todos os pátios",
            parameters = {
                    @Parameter(name = "page", description = "Número da página (0..N)", example = "0"),
                    @Parameter(name = "size", description = "Quantidade de elementos por página", example = "10"),
                    @Parameter(name = "sort", description = "Critério de ordenação no formato: propriedade[,asc|desc]. " +
                            "Pode ser usado múltiplas vezes, por exemplo: sort=nome,asc&sort=id,desc")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pátios listados com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiResponsePatioListDTO.class
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
    public ResponseEntity<ApiResponseDTO<List<PatioResponseDTO>>> findAllPatio(
            @Parameter(hidden = true) @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<PatioResponseDTO> patios = this.patioService.findAll(pageable);
        PaginationDTO pagination = PaginationDTO.fromPage(patios);
        return new ResponseEntity<>(
                new ApiResponseWithPaginationDTO<>(patios.getContent(), pagination),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Retorna um pátio por id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pátio encontrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiResponsePatioDTO.class
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
                    description = "Pátio não encontrado para o ID fornecido",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiErrorDTO.class
                            )
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<PatioResponseDTO> findByIdPatio(
            @Parameter(name = "id", description = "ID do pátio", example = "1")
            @PathVariable Long id
    ) {
        return new ResponseEntity<PatioResponseDTO>(
                this.patioService.findById(id),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Atualiza um pátio")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pátio atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiResponsePatioDTO.class
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
                    description = "Pátio não encontrado para o ID fornecido",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiErrorDTO.class
                            )
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<PatioResponseDTO> updatePatio(
            @Parameter(name = "id", description = "ID do pátio", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody PatioRequestDTO dto
    ) {
        return new ResponseEntity<PatioResponseDTO>(
                this.patioService.update(id, dto),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Deleta um pátio")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Pátio deletado com sucesso"
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
                    description = "Pátio não encontrado para o ID fornecido",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiErrorDTO.class
                            )
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatio(
            @Parameter(name = "id", description = "ID do pátio", example = "1")
            @PathVariable Long id
    ) {
        this.patioService.delete(id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
