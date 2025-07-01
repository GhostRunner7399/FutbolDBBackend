package edu.uam.backend.cursos.Game.Player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api")
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @PostMapping("/importar/{nombre}")
    public ResponseEntity<String> importarPorNombre(@PathVariable String nombre) {
        playerService.importarJugadoresPorNombre(nombre);
        return ResponseEntity.ok("Jugadores importados para: " + nombre);
    }


}
