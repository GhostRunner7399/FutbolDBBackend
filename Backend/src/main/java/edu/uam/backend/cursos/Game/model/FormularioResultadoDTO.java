package edu.uam.backend.cursos.Game.model;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class FormularioResultadoDTO {

    private String playerName;

    private Integer edad;

    private String paisRegion;

    private String posicion;

    private String liga;

    private String equipoActual;

    private Integer premiosInd;

    private Integer premiosColect;

    @Getter
    private boolean finalizado;
    private String mensajeFinal;

    private List<String> camposAcertados;
    private List<String> camposIncorrectos;
    private List<String> faltantes;


}