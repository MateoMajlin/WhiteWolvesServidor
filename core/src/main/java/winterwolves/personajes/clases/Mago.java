package winterwolves.personajes.clases;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.World;
import winterwolves.io.EntradasJugador;
import winterwolves.personajes.AnimacionPersonaje;
import winterwolves.personajes.Personaje;
import winterwolves.personajes.armas.Baston;
import winterwolves.personajes.habilidades.BolaDeFuego;
import winterwolves.personajes.habilidades.ConcentracionMaxima;
import winterwolves.personajes.habilidades.HabilidadCuracion;

public class Mago extends Personaje {

    public Mago(World world, EntradasJugador entradas, float x, float y, float ppm) {
        super(world, entradas, x, y, ppm);
        nombreClase = "Mago";

        animaciones = new AnimacionPersonaje("pajarito.png");

        armaBasica = new Baston(world, ppm);
        habilidad1 = new BolaDeFuego(0f,0f,60);
        habilidad2 = new ConcentracionMaxima(10f, 15f, 2f, 20f);

        habilidad1.setPersonaje(this);
        habilidad2.setPersonaje(this);

        ataque = armaBasica.getDaño();
        ataqueMagico = 50;
        defensa = 30;
        vida = 100;
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
    }
}
