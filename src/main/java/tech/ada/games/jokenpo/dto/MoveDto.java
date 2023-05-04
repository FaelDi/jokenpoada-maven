package tech.ada.games.jokenpo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.ada.games.jokenpo.model.Move;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MoveDto {

    private String move;

    public MoveDto(Move move) {
        this.move = move.getMove();
    }

}
