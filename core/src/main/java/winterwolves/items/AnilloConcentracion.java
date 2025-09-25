package winterwolves.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import winterwolves.personajes.habilidades.Habilidad;
import winterwolves.personajes.habilidadesGuerrero.Arma;
import winterwolves.personajes.habilidadesGuerrero.ConcentracionMaxima;

public class AnilloConcentracion extends Item {

    private final float duracion;
    private final float cooldown;
    private final float bonusVelocidad;
    private final float bonusAtaque;

    Texture textura = new Texture("texturas/anillo.png");
    TextureRegion[][] cuadricula = TextureRegion.split(textura, 16, 16);

    TextureRegion frame = cuadricula[0][2];

    public AnilloConcentracion(float duracion, float cooldown, float bonusVelocidad, float bonusAtaque) {
        super(
            "Anillo Concentracion",
            new TextureRegion(new Texture("texturas/anillo.png"), 64, 16, 16, 16) // x, y, width, height
        );
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
        return null; // No provee arma
    }
}
