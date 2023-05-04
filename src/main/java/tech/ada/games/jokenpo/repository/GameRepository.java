package tech.ada.games.jokenpo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.ada.games.jokenpo.model.Game;

public interface GameRepository extends JpaRepository<Game, Long> {

}
