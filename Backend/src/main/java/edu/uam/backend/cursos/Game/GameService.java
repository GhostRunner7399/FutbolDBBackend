package edu.uam.backend.cursos.Game;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uam.backend.cursos.Game.Player.Model.Player;
import edu.uam.backend.cursos.Game.Player.PlayerRepository;
import edu.uam.backend.cursos.Game.model.FormularioALlenarDTO;
import edu.uam.backend.cursos.Game.model.FormularioRespuestasDTO;
import edu.uam.backend.cursos.Game.model.FormularioResultadoDTO;
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

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final List<String> CAMPOS_JUGABLES = Arrays.asList(
            "edad", "paisRegion", "posicion",
            "liga", "equipoActual", "premiosInd", "premiosColect"
    );

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

        List<String> pistasDisponibles = new ArrayList<>(CAMPOS_JUGABLES);
        Collections.shuffle(pistasDisponibles);

        String campo1 = pistasDisponibles.get(0);
        String campo2 = pistasDisponibles.get(1);

        FormularioALlenarDTO dto = new FormularioALlenarDTO();
        dto.setCampo1(campo1);
        dto.setPista1(getValorCampo(jugadorSeleccionado, campo1));
        dto.setCampo2(campo2);
        dto.setPista2(getValorCampo(jugadorSeleccionado, campo2));
        dto.setDescripcion("Adivina al jugador basándote en las pistas dadas. Cada intento revela qué campos has acertado.");

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
        Game partida = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Partida no encontrada."));

        if (partida.isFinalizada()) {
            throw new RuntimeException("La partida ya ha finalizado.");
        }

        Player jugadorSeleccionado = partida.getJugadorSeleccionado();

        Map<String, Object> camposAcertadosMap = new HashMap<>();
        String jsonPrevio = partida.getCamposAcertadosJson();
        if (jsonPrevio != null && !jsonPrevio.isEmpty()) {
            camposAcertadosMap = objectMapper.readValue(jsonPrevio, new TypeReference<>() {});
        }

        FormularioResultadoDTO resultado = new FormularioResultadoDTO();
        List<String> acertadosEstaRonda = new ArrayList<>();
        List<String> incorrectosEstaRonda = new ArrayList<>();

        for (String campo : CAMPOS_JUGABLES) {
            Object valorCorrecto = getValorCampoBruto(jugadorSeleccionado, campo);
            Object respuesta = getValorCampoBruto(respuestas, campo);

            System.out.println("Comparando campo: " + campo);
            System.out.println("Respuesta recibida: '" + respuesta + "'");
            System.out.println("Valor correcto:    '" + valorCorrecto + "'");


            if (respuestaCorrecta(respuesta, valorCorrecto)) {
                if (!camposAcertadosMap.containsKey(campo)) {
                    camposAcertadosMap.put(campo, valorCorrecto);
                    setValorResultado(resultado, campo, valorCorrecto);
                    acertadosEstaRonda.add(campo);
                }
            } else if (respuesta != null && !respuesta.toString().isBlank()) {
                incorrectosEstaRonda.add(campo);
            }
        }

        resultado.setCamposAcertados(acertadosEstaRonda);
        resultado.setCamposIncorrectos(incorrectosEstaRonda);
        resultado.setFaltantes(obtenerCamposFaltantes(camposAcertadosMap));

        String jsonActualizado = objectMapper.writeValueAsString(camposAcertadosMap);
        partida.setCamposAcertadosJson(jsonActualizado);
        partida.setIntentosRealizados(partida.getIntentosRealizados() + 1);

        boolean juegoFinalizado = camposAcertadosMap.size() == CAMPOS_JUGABLES.size();
        if (juegoFinalizado) {
            partida.setFinalizada(true);
            String nombreJugador = jugadorSeleccionado.getPlayerName();
            resultado.setMensajeFinal(" Has adivinado todos los campos correctamente en "
                    + partida.getIntentosRealizados() + " intentos. El jugador es: " + nombreJugador);
            resultado.setPlayerName(nombreJugador);
        } else {
            resultado.setMensajeFinal("Intento #" + partida.getIntentosRealizados() + " procesado. Sigue adivinando los campos faltantes.");
        }
        resultado.setFinalizado(juegoFinalizado);


        gameRepository.save(partida);
        return resultado;
    }

    private List<String> obtenerCamposFaltantes(Map<String, Object> yaAcertados) {
        List<String> faltantes = new ArrayList<>();
        for (String campo : CAMPOS_JUGABLES) {

            if (!yaAcertados.containsKey(campo)) {
                faltantes.add(campo);
            }
        }
        return faltantes;
    }

    private Object getValorCampoBruto(Object objeto, String campo) {
        try {
            Field field = objeto.getClass().getDeclaredField(campo);
            field.setAccessible(true);
            return field.get(objeto);
        } catch (Exception e) {
            return null;
        }
    }

    private void setValorResultado(FormularioResultadoDTO resultado, String campo, Object valor) {
        try {
            Field field = resultado.getClass().getDeclaredField(campo);
            field.setAccessible(true);
            field.set(resultado, valor);
        } catch (Exception ignored) {
        }
    }

    private boolean respuestaCorrecta(Object respuesta, Object valorCorrecto) {
        if (respuesta == null || valorCorrecto == null) return false;

        String r = respuesta.toString().trim();
        String v = valorCorrecto.toString().trim();

        // Comparación ignorando mayúsculas/minúsculas
        return r.equalsIgnoreCase(v);
    }




}