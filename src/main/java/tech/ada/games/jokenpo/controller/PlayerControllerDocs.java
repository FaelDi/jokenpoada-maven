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
import tech.ada.games.jokenpo.dto.PlayerDto;
import tech.ada.games.jokenpo.exception.DataConflictException;
import tech.ada.games.jokenpo.exception.DataNotFoundException;
import tech.ada.games.jokenpo.model.Player;

import java.util.List;

public interface PlayerControllerDocs {

    @Operation(summary = "Registro de um novo jogador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Jogador criado com sucesso", content = @Content),
            @ApiResponse(responseCode = "409", description = "O Jogador já está cadastrado", content = @Content)
    })
    ResponseEntity<Void> createPlayer(@RequestBody PlayerDto player) throws DataConflictException;

    @Operation(summary = "Retorna uma lista de jogadores cadastrados", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de jogadores cadastrados",
                content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = List.class))}),
            @ApiResponse(responseCode = "404", description = "Não há jogadores cadastrados", content = @Content)
    })
    ResponseEntity<List<Player>> findPlayers() throws DataNotFoundException;

    @Operation(summary = "Retorna um jogador registrado pelo nome", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jogador encontrado com sucesso",
                content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Player.class))}),
            @ApiResponse(responseCode = "404", description = "O jogador não está cadastrado", content = @Content)
    })
    ResponseEntity<Player> findPlayer(@PathVariable String player) throws DataNotFoundException;

    @Operation(summary = "Exclui um jogador pelo seu id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Jogador excluído com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "O jogador não está cadastrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "O jogador está registrado no jogo atual", content = @Content)
    })
    void deletePlayer(@PathVariable Long playerId) throws DataConflictException, DataNotFoundException;

}
