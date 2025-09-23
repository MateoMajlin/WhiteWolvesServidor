package winterwolves.items;

import winterwolves.personajes.habilidades.Habilidad;
import winterwolves.personajes.habilidadesGuerrero.Arma;
import com.badlogic.gdx.physics.box2d.World;

public interface ItemEquipable {
    Habilidad crearHabilidad(); // Retorna la habilidad que provee el item (si aplica)
    Arma crearArma(World world, float ppm); // Retorna el arma que provee el item (si aplica)
}
