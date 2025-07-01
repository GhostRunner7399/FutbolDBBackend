package edu.uam.backend.cursos.Game.Player.Model.Enum;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
@Getter
public enum Posicion {
    POR("goalkeeper"),
    LD("right back"),
    CAD("right wing back"),
    DFC("centre-back"),
    LI("left back"),
    CAI("left wing back"),
    MCD("defensive midfield"),
    MC("central midfield"),
    MCO("attacking midfield"),
    MD("right midfield"),
    MI("left midfield"),
    ED("right winger"),
    EI("left winger"),
    SD("centre-forward"),
    DEL("striker");

    private final String apiName;

    Posicion(String apiName) {
        this.apiName = apiName;
    }

    public String getApiName() {
        return apiName;
    }

    private static final Map<String, Posicion> mapApiToEnum = new HashMap<>();

    static {
        for (Posicion pos : Posicion.values()) {
            mapApiToEnum.put(pos.apiName.toLowerCase(), pos);
        }
    }

    public static String traducirDesdeApi(String apiPosition) {
        if (apiPosition == null) return "DEL";

        String normalizado = apiPosition.trim().toLowerCase();

        Posicion pos = mapApiToEnum.get(normalizado);

        return pos != null ? pos.name() : "DEL";
    }
}
