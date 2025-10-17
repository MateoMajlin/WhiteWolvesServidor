package winterwolves.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import winterwolves.personajes.habilidades.BolaDeFuego;
import winterwolves.personajes.habilidades.Habilidad;
import winterwolves.personajes.armas.Arma;
import winterwolves.personajes.habilidades.RayoElectrico;

public class GemaElectrica extends Item {

    private final float duracion = 5f;
    private final float cooldown = 10f;
    private final int daño = 70;

    private final String descripcion;

    public GemaElectrica() {
        super("Gema Electrica",
            new TextureRegion(new Texture("texturas/anillo.png"), 32, 16, 16, 16));
        this.descripcion = "Amuleto que permite lanzar un rayo que atravieza " + "\n" +
            "e inflige " + daño + " de daño a los enemigos";
    }

    @Override
    public Habilidad crearHabilidad() {
        return new RayoElectrico(duracion, cooldown, daño);
    }

    @Override
    public Arma crearArma(World world, float ppm) {return null;}

    @Override
    public String getDescripcion() {
        return descripcion;
    }
}
