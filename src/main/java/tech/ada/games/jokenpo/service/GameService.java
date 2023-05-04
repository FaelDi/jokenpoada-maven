package tech.ada.games.jokenpo.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.ada.games.jokenpo.dto.GameDto;
import tech.ada.games.jokenpo.dto.GameMoveDto;
import tech.ada.games.jokenpo.dto.ResultDto;
import tech.ada.games.jokenpo.model.Move;
import tech.ada.games.jokenpo.model.Game;
import tech.ada.games.jokenpo.model.Player;
import tech.ada.games.jokenpo.exception.BadRequestException;
import tech.ada.games.jokenpo.exception.DataConflictException;
import tech.ada.games.jokenpo.exception.DataNotFoundException;
import tech.ada.games.jokenpo.model.PlayerMove;
import tech.ada.games.jokenpo.repository.GameRepository;
import tech.ada.games.jokenpo.repository.PlayerMoveRepository;
import tech.ada.games.jokenpo.repository.MoveRepository;
import tech.ada.games.jokenpo.repository.PlayerRepository;
import tech.ada.games.jokenpo.security.SecurityUtils;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@Transactional
public class GameService {

    private final GameRepository gameRepository;

    private final PlayerMoveRepository playerMoveRepository;

    private final MoveRepository moveRepository;

    private final PlayerRepository playerRepository;

    public GameService(GameRepository gameRepository, PlayerMoveRepository playerMoveRepository,
                       MoveRepository moveRepository, PlayerRepository playerRepository) {
        this.gameRepository = gameRepository;
        this.playerMoveRepository = playerMoveRepository;
        this.moveRepository = moveRepository;
        this.playerRepository = playerRepository;
    }

    public void newGame(GameDto gameDto) throws BadRequestException, DataNotFoundException {
        Player currentPlayer = verifyCurrentPlayer();
        Game game = new Game();
        game.setCreator(currentPlayer);
        game.setCreatedAt(LocalDateTime.now());
        game.setFinished(false);
        if (gameDto.getPlayers().size() < 2)
            throw new BadRequestException("O jogo possui menos que dois jogadores!");
        Game savedGame = gameRepository.save(game);
        for (Long playerId : gameDto.getPlayers()) {
            Player player = playerRepository.findById(playerId).orElseThrow(() ->
                    new DataNotFoundException("O jogador não está cadastrado!"));
            PlayerMove playerMove = new PlayerMove();
            playerMove.setGame(savedGame);
            playerMove.setPlayer(player);
            playerMoveRepository.save(playerMove);
        }
        log.info("Jogo iniciado com sucesso!");
    }

    public ResultDto insertPlayerMove(GameMoveDto gameMove) throws DataNotFoundException, BadRequestException, DataConflictException {
        Player currentPlayer = verifyCurrentPlayer();
        Game currentGame = gameRepository.findById(gameMove.getGameId()).orElseThrow(() ->
                new DataNotFoundException("Jogo não cadastrado!"));
        if (currentGame.getFinished())
            throw new BadRequestException("O jogo já foi finalizado!");
        Move move = moveRepository.findById(gameMove.getMoveId()).orElseThrow(() ->
                new DataNotFoundException("Jogada não cadastrada"));
        PlayerMove playerMove = playerMoveRepository.findByUnfinishedGameIdAndPlayer(currentPlayer.getId(),
                gameMove.getGameId()).orElseThrow(() ->
                new DataNotFoundException("Jogador não está cadastrado no jogo!"));
        if (playerMove.getMove() != null)
            throw new DataConflictException("Jogador já realizou a sua jogada!");
        playerMove.setMove(move);
        playerMoveRepository.save(playerMove);
        Long countMovesPlayed = playerMoveRepository.countMovesPlayedByUnfinishedGame(currentGame.getId());
        Long countMovesTotal = playerMoveRepository.countMovesPlayedByUnfinishedGame(currentGame.getId());
        if (Objects.equals(countMovesPlayed, countMovesTotal)) {
            log.info("Todos os jogadores já realizaram suas jogadas! Gerando o resultado final!");
            boolean isSpock = playerMoveRepository.existsSpockByUnfinishedGameId(currentGame.getId());
            boolean isTesoura = playerMoveRepository.existsTesouraByUnfinishedGameId(currentGame.getId());
            boolean isPapel = playerMoveRepository.existsPapelByUnfinishedGameId(currentGame.getId());
            boolean isPedra = playerMoveRepository.existsPedraByUnfinishedGameId(currentGame.getId());
            boolean isLagarto = playerMoveRepository.existsLagartoByUnfinishedGameId(currentGame.getId());
            if (isSpock && (isTesoura || isPedra) && !isPapel && !isLagarto) {
                return produceResult(currentGame, "Spock");
            }
            if (isTesoura && (isPapel || isLagarto) && !isSpock && !isPedra)
                return produceResult(currentGame, "Tesoura");
            if (isPapel && (isPedra || isSpock) && !isTesoura && !isLagarto)
                return produceResult(currentGame, "Papel");
            if (isPedra && (isLagarto || isTesoura) && !isSpock && !isPapel)
                return produceResult(currentGame, "Pedra");
            if (isLagarto && (isSpock || isPapel) && !isTesoura && !isPedra)
                return produceResult(currentGame, "Lagarto");
            return produceResult(currentGame, "Resultado Empate");
        } else {
            long remain = countMovesTotal - countMovesPlayed;
            String msg = "Jogada realizada! Faltam " + remain + " jogadores para finalizar o jogo!";
            log.info(msg);
            ResultDto dto = new ResultDto();
            dto.setMessage(msg);
            return dto;
        }
    }

    public List<Game> findGames() {
        return gameRepository.findAll();
    }

    public Game findGameById(Long id) throws DataNotFoundException {
        return gameRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Este jogo não está cadastrado!"));
    }

    private ResultDto produceResult(Game currentGame, String result) {
        if (!result.equals("Resultado Empate")) {
            List<PlayerMove> playerMoves = playerMoveRepository.findByUnfinishedGameId(currentGame.getId(), result);
            Long moveId = playerMoves.get(0).getMove().getId();
            Set<Player> winners = new HashSet<>();
            List<Long> playerIds = new ArrayList<>();
            String msg;
            if (playerMoves.size() == 1) {
                msg = "Vencedor: " + playerMoves.get(0).getPlayer().getName();
                winners.add(playerMoves.get(0).getPlayer());
                playerIds.add(playerMoves.get(0).getPlayer().getId());
            } else {
                StringBuilder builderMsg = new StringBuilder("Vencedores: ");
                for (PlayerMove pm : playerMoves) {
                    builderMsg.append(pm.getPlayer().getName());
                    if (playerMoves.get(playerMoves.size()-1).equals(pm)) {
                        builderMsg.append(" ");
                    } else {
                        builderMsg.append(" e ");
                    }
                    winners.add(pm.getPlayer());
                    playerIds.add(pm.getPlayer().getId());
                }
                msg = builderMsg.toString();
            }
            currentGame.setWinners(winners);
            currentGame.setFinished(true);
            gameRepository.save(currentGame);
            ResultDto dto = new ResultDto();
            dto.setWinners(playerIds);
            dto.setMoveId(moveId);
            dto.setMessage(msg);
            return dto;
        }
        ResultDto dto = new ResultDto();
        dto.setMessage(result);
        return dto;
    }

    private Player verifyCurrentPlayer() throws DataNotFoundException {
        String currentUser = SecurityUtils.getCurrentUserLogin();
        return playerRepository.findByUsername(currentUser).orElseThrow(() ->
                new DataNotFoundException("O jogador não está cadastrado!"));
    }

}
