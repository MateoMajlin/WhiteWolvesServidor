package winterwolves.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import winterwolves.personajes.habilidades.Habilidad;
import winterwolves.personajes.habilidadesGuerrero.Arma;

public abstract class Item {

    protected String nombre;
    protected TextureRegion textura;

    public Item(String nombre, TextureRegion textura) {
        this.nombre = nombre;
        this.textura = textura;
    }


    public String getNombre() { return nombre; }
    public TextureRegion getTextura() { return textura; }

    // Métodos que los items pueden implementar
    public abstract Habilidad crearHabilidad();      // retorna la habilidad que provee el item
    public abstract Arma crearArma(World world, float ppm); // retorna el arma que provee el item

    public abstract String getDescripcion();
}
