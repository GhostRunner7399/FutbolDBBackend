package edu.uam.backend.cursos.Game;

import edu.uam.backend.cursos.Game.model.FormularioALlenarDTO;
import edu.uam.backend.cursos.Game.model.FormularioRespuestasDTO;
import edu.uam.backend.cursos.Game.model.FormularioResultadoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
public class GameController {

    @Autowired
    private GameService gameService;

    // Crear nueva partida y devolver pistas + gameId
    @GetMapping("/formulario")
    public NuevaPartidaDTO nuevaPartida() {
        Long[] gameIdHolder = new Long[1];
        FormularioALlenarDTO pistas = gameService.generarFormularioNuevo(gameIdHolder);
        return new NuevaPartidaDTO(gameIdHolder[0], pistas);
    }

    // Validar respuestas enviando gameId para saber la partida
    @PostMapping("/responder/{gameId}")
    public FormularioResultadoDTO validarRespuestas(@PathVariable Long gameId, @RequestBody FormularioRespuestasDTO respuestas) throws Exception {
        return gameService.validarRespuestas(gameId, respuestas);
    }

    // DTO para devolver gameId + pistas juntos
    public static class NuevaPartidaDTO {
        private Long gameId;
        private FormularioALlenarDTO pistas;

        public NuevaPartidaDTO(Long gameId, FormularioALlenarDTO pistas) {
            this.gameId = gameId;
            this.pistas = pistas;
        }

        public Long getGameId() {
            return gameId;
        }

        public FormularioALlenarDTO getPistas() {
            return pistas;
        }
    }
}
