package tech.ada.games.jokenpo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.ada.games.jokenpo.model.Player;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDto {

    private String username;
    private String password;
    private String name;

    public PlayerDto(Player player) {
        this.username = player.getUsername();
        this.name = player.getName();
    }

}
