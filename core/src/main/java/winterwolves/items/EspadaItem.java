package winterwolves.items;

import winterwolves.personajes.habilidadesGuerrero.Arma;
import winterwolves.personajes.habilidadesGuerrero.Espada;
import com.badlogic.gdx.physics.box2d.World;
import winterwolves.items.ItemEquipable;
import winterwolves.personajes.habilidades.Habilidad;

public class EspadaItem implements ItemEquipable {

    @Override
    public Habilidad crearHabilidad() {
        return null; // Esta espada no tiene habilidad propia
    }

    @Override
    public Arma crearArma(World world, float ppm) {
        return new Espada(world, ppm);
    }
}
