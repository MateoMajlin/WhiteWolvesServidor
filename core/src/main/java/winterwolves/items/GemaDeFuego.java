package winterwolves.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import winterwolves.personajes.habilidades.BolaDeFuego;
import winterwolves.personajes.habilidades.Habilidad;
import winterwolves.personajes.armas.Arma;

public class GemaDeFuego extends Item {

    private final float duracion;
    private final float cooldown;
    private final int daño;

    private final String descripcion;

    public GemaDeFuego(float duracion, float cooldown, int daño) {
        super("Gema De Fuego",
            new TextureRegion(new Texture("texturas/anillo.png"), 48, 16, 16, 16));
        this.duracion = duracion;
        this.cooldown = cooldown;
        this.daño = daño;
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
