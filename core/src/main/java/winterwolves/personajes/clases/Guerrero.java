package winterwolves.personajes.clases;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.World;
import winterwolves.io.EntradasJugador;
import winterwolves.personajes.Personaje;
import winterwolves.personajes.armas.Espada;
import winterwolves.personajes.habilidades.ConcentracionMaxima;
import winterwolves.personajes.habilidades.HabilidadCuracion;

public class Guerrero extends Personaje {

    public Guerrero(World world, EntradasJugador entradas, float x, float y, float ppm, OrthographicCamera camera) {
        super(world, entradas, x, y, ppm, camera);
        nombreClase = "Guerrero";

        armaBasica = new Espada(world, ppm);
        habilidad1 = new HabilidadCuracion(2f, 10f, 30);
        habilidad2 = new ConcentracionMaxima(10f, 15f, 2f, 20f);

        habilidad1.setPersonaje(this);
        habilidad2.setPersonaje(this);

        ataque = armaBasica.getDaño();
        ataqueMagico = 10;
        defensa = 30;
        vida = 100;
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
    }
}
