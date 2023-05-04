package tech.ada.games.jokenpo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tech.ada.games.jokenpo.model.PlayerMove;

import java.util.List;
import java.util.Optional;

public interface PlayerMoveRepository extends JpaRepository<PlayerMove, Long> {

    @Query("SELECT COUNT(pm) FROM PlayerMove pm WHERE pm.player.id = :playerId AND pm.game.finished = FALSE")
    Long countByUnfinishedGameAndPlayer(@Param("playerId") Long playerId);

    @Query("SELECT COUNT(pm) FROM PlayerMove pm WHERE pm.game.id = :gameId AND pm.game.finished = FALSE AND " +
            "pm.move IS NOT NULL")
    Long countMovesPlayedByUnfinishedGame(@Param("gameId") Long gameId);

    @Query("SELECT COUNT(pm) FROM PlayerMove pm WHERE pm.game.id = :gameId AND pm.game.finished = FALSE")
    Long countByUnfinishedGameId(@Param("gameId") Long gameId);

    @Query("SELECT pm FROM PlayerMove pm WHERE pm.player.id = :playerId AND pm.game.id = :gameId AND " +
            "pm.game.finished = FALSE")
    Optional<PlayerMove> findByUnfinishedGameIdAndPlayer(@Param("playerId") Long playerId, @Param("gameId") Long gameId);

    @Query("SELECT EXISTS (SELECT pm FROM PlayerMove pm WHERE pm.game.id = :gameId AND pm.game.finished = FALSE " +
            "AND (UPPER(pm.move.move) = 'SPOCK' OR UPPER(pm.move.move) = 'JOGADA SPOCK'))")
    boolean existsSpockByUnfinishedGameId(@Param("gameId") Long gameId);

    @Query("SELECT EXISTS (SELECT pm FROM PlayerMove pm WHERE pm.game.id = :gameId AND pm.game.finished = FALSE " +
            "AND (UPPER(pm.move.move) = 'TESOURA' OR UPPER(pm.move.move) = 'JOGADA TESOURA'))")
    boolean existsTesouraByUnfinishedGameId(@Param("gameId") Long gameId);

    @Query("SELECT EXISTS (SELECT pm FROM PlayerMove pm WHERE pm.game.id = :gameId AND pm.game.finished = FALSE " +
            "AND (UPPER(pm.move.move) = 'PAPEL' OR UPPER(pm.move.move) = 'JOGADA PAPEL'))")
    boolean existsPapelByUnfinishedGameId(@Param("gameId") Long gameId);

    @Query("SELECT EXISTS (SELECT pm FROM PlayerMove pm WHERE pm.game.id = :gameId AND pm.game.finished = FALSE " +
            "AND (UPPER(pm.move.move) = 'PEDRA' OR UPPER(pm.move.move) = 'JOGADA PEDRA'))")
    boolean existsPedraByUnfinishedGameId(@Param("gameId") Long gameId);

    @Query("SELECT EXISTS (SELECT pm FROM PlayerMove pm WHERE pm.game.id = :gameId AND pm.game.finished = FALSE " +
            "AND (UPPER(pm.move.move) = 'LAGARTO' OR UPPER(pm.move.move) = 'JOGADA LAGARTO'))")
    boolean existsLagartoByUnfinishedGameId(@Param("gameId") Long gameId);

    @Query("SELECT pm FROM PlayerMove pm WHERE pm.game.id = :gameId AND pm.game.finished = FALSE " +
            "AND UPPER(pm.move.move) ILIKE '%:move%'")
    List<PlayerMove> findByUnfinishedGameId(@Param("gameId") Long gameId, @Param("move") String move);

}
