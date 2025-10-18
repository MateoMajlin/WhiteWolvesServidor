package winterwolves.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import winterwolves.personajes.habilidades.Habilidad;
import winterwolves.personajes.armas.Arma;
import winterwolves.personajes.habilidades.ConcentracionMaxima;

public class AnilloConcentracion extends Item {

    String descripcion = "Anillo capaz de ampliar todos " + "\n" + "los sentidos de su portador";

    private final float duracion = 10f;
    private final float cooldown = 10f;
    private final float bonusVelocidad = 5;
    private final float bonusAtaque = 50;

    Texture textura = new Texture("texturas/anillo.png");
    TextureRegion[][] cuadricula = TextureRegion.split(textura, 16, 16);

    TextureRegion frame = cuadricula[0][2];

    public AnilloConcentracion() {
        super(
            "Anillo Concentracion",
            new TextureRegion(new Texture("texturas/anillo.png"), 80, 16, 16, 16) // x, y, width, height
        );
    }

    @Override
    public Habilidad crearHabilidad() {
        return new ConcentracionMaxima(duracion, cooldown, bonusVelocidad, bonusAtaque);
    }

    @Override
    public Arma crearArma(World world, float ppm) {
        return null; // No provee arma
    }

    @Override
    public String getDescripcion() {
        return descripcion;
    }
}
