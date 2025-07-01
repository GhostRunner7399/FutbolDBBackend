package edu.uam.backend.cursos.Game.Player;


import edu.uam.backend.cursos.Game.Player.DataTransferObject.PlayerApiContainer;
import edu.uam.backend.cursos.Game.Player.DataTransferObject.PlayerApiResponseDTO;
import edu.uam.backend.cursos.Game.Player.DataTransferObject.TeamApiContainer;
import edu.uam.backend.cursos.Game.Player.DataTransferObject.TeamApiResponseDTO;
import edu.uam.backend.cursos.Game.Player.Model.Enum.Posicion;
import edu.uam.backend.cursos.Game.Player.Model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    public void importarJugadoresPorNombre(String nombreJugador) {
        String apiUrl = "https://www.thesportsdb.com/api/v1/json/3/searchplayers.php?p=" + nombreJugador;

        ResponseEntity<PlayerApiContainer> response = restTemplate.getForEntity(apiUrl, PlayerApiContainer.class);

        if (response.getBody() != null && response.getBody().getPlayer() != null) {
            for (PlayerApiResponseDTO apiPlayer : response.getBody().getPlayer()) {
                Player jugador = new Player();

                jugador.setPlayerName(apiPlayer.getStrPlayer());

                // Edad
                if (apiPlayer.getDateBorn() != null && !apiPlayer.getDateBorn().isEmpty()) {
                    try {
                        LocalDate nacimiento = LocalDate.parse(apiPlayer.getDateBorn(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        jugador.setEdad(Period.between(nacimiento, LocalDate.now()).getYears());
                    } catch (Exception e) {
                        jugador.setEdad(0);
                    }
                } else {
                    jugador.setEdad(0);
                }

                jugador.setPaisRegion(apiPlayer.getStrNationality());

                // Traducir posición
                String posicionTraducida = Posicion.traducirDesdeApi(apiPlayer.getStrPosition());
                if (posicionTraducida == null) continue;
                jugador.setPosicion(posicionTraducida);

                // Consultar la liga real a partir del idTeam
                String liga = obtenerLigaPorTeamId(apiPlayer.getIdTeam());
                jugador.setLiga(liga);

                // Guardar el nombre del equipo actual
                jugador.setEquipoActual(apiPlayer.getStrTeam() != null ? apiPlayer.getStrTeam() : "Desconocido");

                jugador.setPremiosInd(0);
                jugador.setPremiosColect(0);

                playerRepository.save(jugador);
            }
        }
    }

    private String obtenerLigaPorTeamId(String idTeam) {
        if (idTeam == null || idTeam.isEmpty()) {
            return "Desconocida";
        }

        String teamApiUrl = "https://www.thesportsdb.com/api/v1/json/3/lookupteam.php?id=" + idTeam;

        try {
            ResponseEntity<TeamApiContainer> response = restTemplate.getForEntity(teamApiUrl, TeamApiContainer.class);
            if (response.getBody() != null && response.getBody().getTeams() != null && !response.getBody().getTeams().isEmpty()) {
                TeamApiResponseDTO team = response.getBody().getTeams().get(0);
                return team.getStrLeague() != null ? team.getStrLeague() : "Desconocida";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Desconocida";
    }

}
