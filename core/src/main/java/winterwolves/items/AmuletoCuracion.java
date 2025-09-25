package winterwolves.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import winterwolves.personajes.habilidades.Habilidad;
import winterwolves.personajes.habilidadesGuerrero.Arma;
import winterwolves.personajes.habilidadesGuerrero.HabilidadCuracion;

public class AmuletoCuracion extends Item {

    private final float duracion;
    private final float cooldown;
    private final int curacion;

    public AmuletoCuracion(float duracion, float cooldown, int curacion) {
        super("Amuleto Curacion", new TextureRegion(new Texture("texturas/anillo.png"), 32, 16, 16, 16));
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
        return null; // No provee arma
    }
}
