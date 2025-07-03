package edu.uam.backend.cursos.Game;

import edu.uam.backend.cursos.Game.Player.Model.Player;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Player jugadorSeleccionado;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String pistasDadas;

    private int intentosRealizados = 0;

    private boolean finalizada;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String camposAcertadosJson = "{}";
}
