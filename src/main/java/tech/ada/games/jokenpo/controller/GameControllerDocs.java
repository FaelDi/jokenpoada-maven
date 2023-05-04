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
import tech.ada.games.jokenpo.dto.GameDto;
import tech.ada.games.jokenpo.dto.GameMoveDto;
import tech.ada.games.jokenpo.dto.ResultDto;
import tech.ada.games.jokenpo.model.Game;
import tech.ada.games.jokenpo.exception.BadRequestException;
import tech.ada.games.jokenpo.exception.DataConflictException;
import tech.ada.games.jokenpo.exception.DataNotFoundException;

import java.util.List;

public interface GameControllerDocs {

    @Operation(summary = "Registro de um novo jogo", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Jogo iniciado com sucesso!", content = @Content),
            @ApiResponse(responseCode = "400", description = "O jogo possui menos que dois jogadores!", content = @Content),
            @ApiResponse(responseCode = "404", description = "O jogador não está cadastrado!", content = @Content)
    })
    ResponseEntity<Void> newGame(@RequestBody GameDto gameDto) throws BadRequestException,
            DataNotFoundException;

    @Operation(summary = "Registro de uma jogada do jogador logado", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jogada realizada! Caso todos os jogadores da partida tenham" +
                    "realizado suas jogadas, a partida encerra com o resultado final!",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResultDto.class))}),
            @ApiResponse(responseCode = "400", description = "O jogo já foi finalizado!", content = @Content),
            @ApiResponse(responseCode = "404", description = "O jogador ou a jogada não estão cadastrados", content = @Content),
            @ApiResponse(responseCode = "409", description = "Jogador já realizou a sua jogada!", content = @Content)
    })
    ResponseEntity<ResultDto> insertPlayerMove(@RequestBody GameMoveDto gameMove) throws BadRequestException,
            DataNotFoundException, DataConflictException;

    @Operation(summary = "Retorna uma lista de jogos cadastrados", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de jogos cadastrados",
                content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = List.class))})
    })
    ResponseEntity<List<Game>> findGames();

    @Operation(summary = "Retorna um jogo registrada pelo id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jogo encontrado com sucesso",
                content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Game.class))}),
            @ApiResponse(responseCode = "404", description = "Este jogo não está cadastrado", content = @Content)
    })
    ResponseEntity<Game> findGame(@PathVariable Long id) throws DataNotFoundException;

}
