package tech.ada.games.jokenpo.service;

import org.springframework.stereotype.Service;
import tech.ada.games.jokenpo.dto.MoveDto;
import tech.ada.games.jokenpo.exception.BadRequestException;
import tech.ada.games.jokenpo.exception.DataConflictException;
import tech.ada.games.jokenpo.exception.DataNotFoundException;
import tech.ada.games.jokenpo.model.Move;
import tech.ada.games.jokenpo.repository.MoveRepository;

import java.util.List;

@Service
public class MoveService {

    private final MoveRepository moveRepository;

    public MoveService(MoveRepository moveRepository) {
        this.moveRepository = moveRepository;
    }

    public void createMove(MoveDto moveDto) throws DataConflictException, BadRequestException {
        if (moveRepository.existsByMove(moveDto.getMove()))
            throw new DataConflictException("A jogada já está cadastrada!");
        if (!moveDto.getMove().equalsIgnoreCase("Spock") &&
                !moveDto.getMove().equalsIgnoreCase("Jogada Spock") &&
                !moveDto.getMove().equalsIgnoreCase("Tesoura") &&
                !moveDto.getMove().equalsIgnoreCase("Jogada Tesoura") &&
                !moveDto.getMove().equalsIgnoreCase("Papel") &&
                !moveDto.getMove().equalsIgnoreCase("Jogada Papel") &&
                !moveDto.getMove().equalsIgnoreCase("Pedra") &&
                !moveDto.getMove().equalsIgnoreCase("Jogada Pedra") &&
                !moveDto.getMove().equalsIgnoreCase("Lagarto") &&
                !moveDto.getMove().equalsIgnoreCase("Jogada Lagarto")) {
            throw new BadRequestException("Você pode cadastrar apenas os movimentos Spock, Tesoura, Papel, Pedra e Lagarto");
        }
        Move move = new Move();
        move.setMove(moveDto.getMove());
        moveRepository.save(move);
    }

    public List<Move> findMoves() throws DataNotFoundException {
        List<Move> moves = moveRepository.findAll();
        if (moves.isEmpty())
            throw new DataNotFoundException("Não há jogadas cadastradas!");
        return moves;
    }

    public Move findByMove(String move) throws DataNotFoundException {
        return moveRepository.findByMove(move).orElseThrow(() -> new DataNotFoundException("A jogada não está cadastrada!"));
    }

}
