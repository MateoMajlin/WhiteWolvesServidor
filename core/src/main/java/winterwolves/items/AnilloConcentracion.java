package winterwolves.items;

import com.badlogic.gdx.physics.box2d.World;
import winterwolves.personajes.habilidades.Habilidad;
import winterwolves.personajes.habilidadesGuerrero.Arma;
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

    @Override
    public Arma crearArma(World world, float ppm) {
        return null;
    }
}
