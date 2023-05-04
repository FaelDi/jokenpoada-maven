package tech.ada.games.jokenpo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.ada.games.jokenpo.dto.PlayerDto;
import tech.ada.games.jokenpo.exception.DataConflictException;
import tech.ada.games.jokenpo.exception.DataNotFoundException;
import tech.ada.games.jokenpo.model.Player;
import tech.ada.games.jokenpo.model.Role;
import tech.ada.games.jokenpo.repository.PlayerMoveRepository;
import tech.ada.games.jokenpo.repository.PlayerRepository;
import tech.ada.games.jokenpo.repository.RoleRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final RoleRepository roleRepository;
    private final PlayerMoveRepository playerMoveRepository;
    private final PasswordEncoder passwordEncoder;

    public PlayerService(PlayerRepository playerRepository, RoleRepository roleRepository,
                         PlayerMoveRepository playerMoveRepository, PasswordEncoder passwordEncoder) {
        this.playerRepository = playerRepository;
        this.roleRepository = roleRepository;
        this.playerMoveRepository = playerMoveRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public void createPlayer(PlayerDto playerDto) throws DataConflictException {
        if (playerRepository.existsByUsername(playerDto.getUsername()))
            throw new DataConflictException("O jogador já está cadastrado!");

        Player player = new Player();
        player.setName(playerDto.getName());
        player.setUsername(playerDto.getUsername());
        player.setPassword(passwordEncoder.encode(playerDto.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName("ROLE_USER").get();
        roles.add(role);
        player.setRoles(roles);
        playerRepository.save(player);
        log.info("Jogador registrado com sucesso!");
    }

    public List<Player> findPlayers() throws DataNotFoundException {
        List<Player> players = playerRepository.findAll();
        if (players.isEmpty())
            throw new DataNotFoundException("Não há jogadores cadastrados!");
        return players;
    }

    public Player findByPlayer(String player) throws DataNotFoundException {
        return playerRepository.findByUsername(player).orElseThrow(() -> new DataNotFoundException("O jogador não está cadastrado!"));
    }

    public void deletePlayer(Long id) throws DataNotFoundException, DataConflictException {
        Player p = playerRepository.findById(id).orElseThrow(() -> new DataNotFoundException("O jogador não está cadastrado!"));
        if (playerMoveRepository.countByUnfinishedGameAndPlayer(id) > 0) {
            throw new DataConflictException("O jogador está registrado em uma partida não finalizada!");
        }
        playerRepository.delete(p);
    }

}
