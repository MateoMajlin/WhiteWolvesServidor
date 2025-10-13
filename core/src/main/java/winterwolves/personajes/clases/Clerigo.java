package winterwolves.personajes.clases;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.World;
import winterwolves.io.EntradasJugador;
import winterwolves.personajes.Personaje;
import winterwolves.personajes.armas.Baston;
import winterwolves.personajes.armas.Espada;
import winterwolves.personajes.habilidades.ConcentracionMaxima;
import winterwolves.personajes.habilidades.HabilidadCuracion;
import winterwolves.personajes.habilidades.RayoElectrico;

public class Clerigo extends Personaje{

    public Clerigo(World world, EntradasJugador entradas, float x, float y, float ppm, OrthographicCamera camera) {
        super(world, entradas, x, y, ppm, camera);

        nombreClase = "Clerigo";

        armaBasica = new Baston(world, ppm);
        habilidad1 = new HabilidadCuracion(2f, 10f, 30);
        habilidad2 = new RayoElectrico(5f,10f,60);

        habilidad1.setPersonaje(this);
        habilidad2.setPersonaje(this);

        ataque = armaBasica.getDaño();
        ataqueMagico = 40;
        defensa = 60;
        vida = 150;
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
    }
}
