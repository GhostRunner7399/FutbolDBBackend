package edu.uam.backend.cursos.Game.Player.DataTransferObject;

import lombok.Data;

import java.util.List;

@Data
public class PlayerApiContainer {
    private List<PlayerApiResponseDTO> player;

}
