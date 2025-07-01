package edu.uam.backend.cursos.Game.Player.Model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long playerId;

    @Column(nullable = false)
    String playerName;

    @Column(nullable = false)
    int edad;

    @Column(nullable = false)
    String paisRegion;

    @Column(nullable = false)
    String posicion;

    @Column(nullable = false)
    String liga;

    @Column(nullable = false)
    String equipoActual;

    @Column(nullable = false)
    int premiosInd;

    @Column(nullable = false)
    int premiosColect;

}
