package tech.ada.games.jokenpo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.ada.games.jokenpo.model.Move;

import java.util.Optional;

public interface MoveRepository extends JpaRepository<Move, Long> {

    Optional<Move> findByMove(String move);
    Boolean existsByMove(String move);

}
