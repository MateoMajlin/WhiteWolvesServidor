package winterwolves.items;

import com.badlogic.gdx.physics.box2d.World;
import winterwolves.personajes.habilidades.Habilidad;
import winterwolves.personajes.habilidadesGuerrero.Arma;
import winterwolves.personajes.habilidadesGuerrero.HabilidadCuracion;

public class AmuletoCuracion implements ItemEquipable {

    private final float duracion;
    private final float cooldown;
    private final int curacion;

    public AmuletoCuracion(float duracion, float cooldown, int curacion) {
        this.duracion = duracion;
        this.cooldown = cooldown;
        this.curacion = curacion;
    }

    @Override
    public Habilidad crearHabilidad() {
        return new HabilidadCuracion(duracion, cooldown, curacion);
    }

    @Override
    public Arma crearArma(World world, float ppm) {
        return null;
    }
}
