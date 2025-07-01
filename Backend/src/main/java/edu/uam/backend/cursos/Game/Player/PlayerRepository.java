package edu.uam.backend.cursos.Game.Player;

import edu.uam.backend.cursos.Game.Player.Model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {

}
