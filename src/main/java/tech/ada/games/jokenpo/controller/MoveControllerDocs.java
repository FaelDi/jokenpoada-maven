package tech.ada.games.jokenpo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import tech.ada.games.jokenpo.dto.MoveDto;
import tech.ada.games.jokenpo.exception.BadRequestException;
import tech.ada.games.jokenpo.exception.DataConflictException;
import tech.ada.games.jokenpo.exception.DataNotFoundException;
import tech.ada.games.jokenpo.model.Move;

import java.util.List;

public interface MoveControllerDocs {

    @Operation(summary = "Registro de uma nova jogada", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Jogada criada com sucesso", content = @Content),
            @ApiResponse(responseCode = "400", description = "Você pode cadastrar apenas os movimentos Spock, Tesoura, Papel, Pedra e Lagarto", content = @Content),
            @ApiResponse(responseCode = "409", description = "A jogada já está cadastrada", content = @Content)
    })
    ResponseEntity<Void> createMove(@RequestBody MoveDto move) throws DataConflictException, BadRequestException;

    @Operation(summary = "Retorna uma lista de jogadas cadastradas", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de jogadas cadastradas",
                content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = List.class))}),
            @ApiResponse(responseCode = "404", description = "Não há jogadas cadastradas", content = @Content)
    })
    ResponseEntity<List<Move>> findMoves() throws DataNotFoundException;

    @Operation(summary = "Retorna uma jogada registrada pelo nome", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jogada encontrada com sucesso",
                content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Move.class))}),
            @ApiResponse(responseCode = "404", description = "A jogada não está cadastrada", content = @Content)
    })
    ResponseEntity<Move> findMove(@PathVariable String move) throws DataNotFoundException;

}
