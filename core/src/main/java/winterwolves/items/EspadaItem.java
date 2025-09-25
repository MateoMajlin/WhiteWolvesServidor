package winterwolves.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import winterwolves.personajes.habilidades.Habilidad;
import winterwolves.personajes.habilidadesGuerrero.Arma;

public class EspadaItem extends Item {

    public EspadaItem() {
        super("Espada", new TextureRegion(new Texture("texturas/espada.png")));
    }

    @Override
    public Habilidad crearHabilidad() {
        return null;
    }

    @Override
    public Arma crearArma(World world, float ppm) {
        return new Arma(world, ppm);
    }
}
