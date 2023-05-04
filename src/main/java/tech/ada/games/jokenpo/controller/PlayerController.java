package tech.ada.games.jokenpo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.ada.games.jokenpo.dto.PlayerDto;
import tech.ada.games.jokenpo.exception.DataConflictException;
import tech.ada.games.jokenpo.exception.DataNotFoundException;
import tech.ada.games.jokenpo.model.Player;
import tech.ada.games.jokenpo.service.PlayerService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/jokenpo/player")
public class PlayerController implements PlayerControllerDocs {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createPlayer(@RequestBody PlayerDto player) throws DataConflictException {
        playerService.createPlayer(player);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<Player>> findPlayers() throws DataNotFoundException {
        return new ResponseEntity<>(playerService.findPlayers(), HttpStatus.OK);
    }

    @GetMapping("/{player}")
    public ResponseEntity<Player> findPlayer(@PathVariable String player) throws DataNotFoundException {
        return new ResponseEntity<>(playerService.findByPlayer(player), HttpStatus.OK);
    }

    @DeleteMapping("/{player}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlayer(@PathVariable Long playerId) throws DataConflictException, DataNotFoundException {
        playerService.deletePlayer(playerId);
    }

}
