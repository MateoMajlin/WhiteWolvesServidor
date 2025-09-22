package winterwolves.items;

import winterwolves.personajes.habilidades.Habilidad;
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
}
