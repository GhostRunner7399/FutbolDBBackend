package edu.uam.backend.cursos.Game;

import edu.uam.backend.cursos.Game.Player.Model.Player;
import edu.uam.backend.cursos.Game.Player.PlayerRepository;
import edu.uam.backend.cursos.Game.model.FormularioALlenarDTO;
import edu.uam.backend.cursos.Game.model.FormularioRespuestasDTO;
import edu.uam.backend.cursos.Game.model.FormularioResultadoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

@Service
public class GameService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameRepository gameRepository;

    private ObjectMapper objectMapper = new ObjectMapper();


    public FormularioALlenarDTO generarFormularioNuevo(Long[] outGameId) {
        List<Player> jugadores = playerRepository.findAll();
        if (jugadores.isEmpty()) {
            throw new RuntimeException("No hay jugadores disponibles");
        }

        Player jugadorSeleccionado = jugadores.get(new Random().nextInt(jugadores.size()));

        Game nuevaPartida = new Game();
        nuevaPartida.setJugadorSeleccionado(jugadorSeleccionado);
        nuevaPartida.setIntentosRealizados(0);
        nuevaPartida.setFinalizada(false);
        nuevaPartida.setCamposAcertadosJson("{}");

        nuevaPartida = gameRepository.save(nuevaPartida);

        outGameId[0] = nuevaPartida.getId();

        List<String> pistasDisponibles = new ArrayList<>(Arrays.asList(
                "edad", "paisRegion", "posicion", "liga", "equipoActual", "premiosInd", "premiosColect"));

        Random random = new Random();
        String campo1 = pistasDisponibles.remove(random.nextInt(pistasDisponibles.size()));
        String campo2 = pistasDisponibles.remove(random.nextInt(pistasDisponibles.size()));

        FormularioALlenarDTO dto = new FormularioALlenarDTO();
        dto.setCampo1(campo1);
        dto.setPista1(getValorCampo(jugadorSeleccionado, campo1));
        dto.setCampo2(campo2);
        dto.setPista2(getValorCampo(jugadorSeleccionado, campo2));

        return dto;
    }


    private String getValorCampo(Player jugador, String campo) {
        try {
            Field field = Player.class.getDeclaredField(campo);
            field.setAccessible(true);
            Object valor = field.get(jugador);
            return valor != null ? valor.toString() : "";
        } catch (Exception e) {
            return "";
        }
    }
    public FormularioResultadoDTO validarRespuestas(Long gameId, FormularioRespuestasDTO respuestas) throws Exception {
        Optional<Game> optionalGame = gameRepository.findById(gameId);
        if (optionalGame.isEmpty()) {
            throw new RuntimeException("Partida no encontrada.");
        }

        Game partida = optionalGame.get();

        if (partida.isFinalizada()) {
            throw new RuntimeException("La partida ya ha finalizado.");
        }

        Player jugadorSeleccionado = partida.getJugadorSeleccionado();

        Map<String, Object> camposAcertadosMap = new HashMap<>();
        String jsonPrevio = partida.getCamposAcertadosJson();
        if (jsonPrevio != null && !jsonPrevio.isEmpty()) {
            camposAcertadosMap = objectMapper.readValue(jsonPrevio, Map.class);
        }

        FormularioResultadoDTO resultado = new FormularioResultadoDTO();

        if (respuestaCorrecta(respuestas.getPlayerName(), jugadorSeleccionado.getPlayerName())) {
            resultado.setPlayerName(jugadorSeleccionado.getPlayerName());
            camposAcertadosMap.put("playerName", jugadorSeleccionado.getPlayerName());
        }

        if (respuestaCorrecta(respuestas.getEdad(), jugadorSeleccionado.getEdad())) {
            resultado.setEdad(jugadorSeleccionado.getEdad());
            camposAcertadosMap.put("edad", jugadorSeleccionado.getEdad());
        }

        if (respuestaCorrecta(respuestas.getPaisRegion(), jugadorSeleccionado.getPaisRegion())) {
            resultado.setPaisRegion(jugadorSeleccionado.getPaisRegion());
            camposAcertadosMap.put("paisRegion", jugadorSeleccionado.getPaisRegion());
        }

        if (respuestaCorrecta(respuestas.getPosicion(), jugadorSeleccionado.getPosicion())) {
            resultado.setPosicion(jugadorSeleccionado.getPosicion());
            camposAcertadosMap.put("posicion", jugadorSeleccionado.getPosicion());
        }

        if (respuestaCorrecta(respuestas.getTLiga(), jugadorSeleccionado.getLiga())) {
            resultado.setLiga(jugadorSeleccionado.getLiga());
            camposAcertadosMap.put("liga", jugadorSeleccionado.getLiga());
        }

        if (respuestaCorrecta(respuestas.getEquipoActual(), jugadorSeleccionado.getEquipoActual())) {
            resultado.setEquipoActual(jugadorSeleccionado.getEquipoActual());
            camposAcertadosMap.put("equipoActual", jugadorSeleccionado.getEquipoActual());
        }

        if (respuestaCorrecta(respuestas.getPremiosInd(), jugadorSeleccionado.getPremiosInd())) {
            resultado.setPremiosInd(jugadorSeleccionado.getPremiosInd());
            camposAcertadosMap.put("premiosInd", jugadorSeleccionado.getPremiosInd());
        }

        if (respuestaCorrecta(respuestas.getPremiosColect(), jugadorSeleccionado.getPremiosColect())) {
            resultado.setPremiosColect(jugadorSeleccionado.getPremiosColect());
            camposAcertadosMap.put("premiosColect", jugadorSeleccionado.getPremiosColect());
        }


        String jsonActualizado = objectMapper.writeValueAsString(camposAcertadosMap);
        partida.setCamposAcertadosJson(jsonActualizado);

        // Incrementar intentos
        partida.setIntentosRealizados(partida.getIntentosRealizados() + 1);


        if (camposAcertadosMap.size() == 8) {
            partida.setFinalizada(true);

        }

        gameRepository.save(partida);

        return resultado;
    }


    private boolean respuestaCorrecta(Object respuesta, Object valorCorrecto) {
        if (respuesta == null || valorCorrecto == null) return false;
        return respuesta.toString().trim().equalsIgnoreCase(valorCorrecto.toString().trim());
    }
}
