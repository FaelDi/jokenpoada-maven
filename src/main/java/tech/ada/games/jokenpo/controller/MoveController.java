package tech.ada.games.jokenpo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.ada.games.jokenpo.dto.MoveDto;
import tech.ada.games.jokenpo.exception.BadRequestException;
import tech.ada.games.jokenpo.exception.DataConflictException;
import tech.ada.games.jokenpo.exception.DataNotFoundException;
import tech.ada.games.jokenpo.model.Move;
import tech.ada.games.jokenpo.service.MoveService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/jokenpo/move")
public class MoveController implements MoveControllerDocs {

    private final MoveService moveService;

    public MoveController(MoveService moveService) {
        this.moveService = moveService;
    }

    @PostMapping("")
    public ResponseEntity<Void> createMove(@RequestBody MoveDto move) throws DataConflictException, BadRequestException {
        moveService.createMove(move);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<Move>> findMoves() throws DataNotFoundException {
        return new ResponseEntity<>(moveService.findMoves(), HttpStatus.OK);
    }

    @GetMapping("/{move}")
    public ResponseEntity<Move> findMove(@PathVariable String move) throws DataNotFoundException {
        return new ResponseEntity<>(moveService.findByMove(move), HttpStatus.OK);
    }

}
