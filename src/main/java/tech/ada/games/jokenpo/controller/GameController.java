package tech.ada.games.jokenpo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.ada.games.jokenpo.dto.GameDto;
import tech.ada.games.jokenpo.dto.GameMoveDto;
import tech.ada.games.jokenpo.dto.ResultDto;
import tech.ada.games.jokenpo.model.Game;
import tech.ada.games.jokenpo.exception.BadRequestException;
import tech.ada.games.jokenpo.exception.DataConflictException;
import tech.ada.games.jokenpo.exception.DataNotFoundException;
import tech.ada.games.jokenpo.service.GameService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/jokenpo/game")
public class GameController implements GameControllerDocs {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/new")
    public ResponseEntity<Void> newGame(@RequestBody GameDto gameDto) throws BadRequestException,
            DataNotFoundException {
        gameService.newGame(gameDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/move")
    public ResponseEntity<ResultDto> insertPlayerMove(@RequestBody GameMoveDto gameMove) throws BadRequestException,
            DataNotFoundException, DataConflictException {
        return new ResponseEntity<>(gameService.insertPlayerMove(gameMove), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<Game>> findGames() {
        return new ResponseEntity<>(gameService.findGames(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> findGame(@PathVariable Long id) throws DataNotFoundException {
        return new ResponseEntity<>(gameService.findGameById(id), HttpStatus.OK);
    }

}
