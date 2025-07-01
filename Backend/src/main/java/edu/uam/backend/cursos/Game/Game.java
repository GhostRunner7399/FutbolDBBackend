package edu.uam.backend.cursos.Game;

import edu.uam.backend.cursos.Game.Player.Model.Player;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Getter
@Setter
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    private Player jugadorSeleccionado;

    private String pistasDadas;

    private Integer intentosRealizados;

    private boolean finalizada;

    @Lob
    private String camposAcertadosJson;

}
