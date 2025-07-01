package edu.uam.backend.cursos.Game.model;

import lombok.Data;

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
}