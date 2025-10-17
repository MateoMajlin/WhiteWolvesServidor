package winterwolves.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import winterwolves.personajes.habilidades.BolaDeFuego;
import winterwolves.personajes.habilidades.Habilidad;
import winterwolves.personajes.armas.Arma;

public class GemaDeFuego extends Item {

    private final float duracion = 5f;
    private final float cooldown = 5f;
    private final int daño = 50;

    private final String descripcion;

    public GemaDeFuego() {
        super("Gema De Fuego",
            new TextureRegion(new Texture("texturas/anillo.png"), 48, 16, 16, 16));
        this.descripcion = "Amuleto que permite lanzar una bola de fuego" + "\n" +
            "que inflige " + daño + " de daño a los enemigos";
    }

    @Override
    public Habilidad crearHabilidad() {
        return new BolaDeFuego(duracion, cooldown, daño);
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
