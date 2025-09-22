package winterwolves.items;

import winterwolves.personajes.habilidades.Habilidad;
import winterwolves.personajes.habilidadesGuerrero.ConcentracionMaxima;

public class AnilloConcentracion implements ItemEquipable {

    private final float duracion;
    private final float cooldown;
    private final float bonusVelocidad;
    private final float bonusAtaque;

    public AnilloConcentracion(float duracion, float cooldown, float bonusVelocidad, float bonusAtaque) {
        this.duracion = duracion;
        this.cooldown = cooldown;
        this.bonusVelocidad = bonusVelocidad;
        this.bonusAtaque = bonusAtaque;
    }

    @Override
    public Habilidad crearHabilidad() {
        return new ConcentracionMaxima(duracion, cooldown, bonusVelocidad, bonusAtaque);
    }
}
