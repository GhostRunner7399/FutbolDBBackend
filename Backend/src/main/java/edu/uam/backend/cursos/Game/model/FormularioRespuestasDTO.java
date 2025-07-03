package edu.uam.backend.cursos.Game.model;

import lombok.Data;

@Data
public class FormularioRespuestasDTO {
    private String playerName;
    private Integer edad;
    private String paisRegion;
    private String posicion;
    private String liga;
    private String equipoActual;
    private Integer premiosInd;
    private Integer premiosColect;

}
