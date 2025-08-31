package winterwolves.personajes;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.World;
import winterwolves.io.EntradasJugador;

public class Guerrero extends Personaje {

    protected GolpeEspada golpe;

    public final float COOLDOWN_GOLPE = 1f;
    private float tiempoDesdeUltimoGolpe = 0f;

    public Guerrero(World world, EntradasJugador entradas, float x, float y, float ppm) {
        super(world, entradas, x, y, ppm);
        golpe = new GolpeEspada(world, ppm);
    }

    @Override
    public void draw(Batch batch) {
        float delta = com.badlogic.gdx.Gdx.graphics.getDeltaTime();
        tiempoDesdeUltimoGolpe += delta;

        super.draw(batch);

        float desplazamiento = getWidth() * 0.6f;
        float espadaX = getX() + direccionMirando.x * desplazamiento;
        float espadaY = getY() + direccionMirando.y * desplazamiento;

        GolpeEspada.Direccion dir = vectorADireccion(direccionMirando);

        // Activar ataque
        if (entradas.isAtacar() && !golpe.isActivo() && tiempoDesdeUltimoGolpe >= COOLDOWN_GOLPE) {
            golpe.activar(espadaX, espadaY, dir);
            tiempoDesdeUltimoGolpe = 0f;
            setPuedeMoverse(false); // bloquear movimiento al atacar
        }

        // Si el golpe ya terminó, desbloquear movimiento
        if (!golpe.isActivo()) {
            setPuedeMoverse(true);
        }

        golpe.update(delta, espadaX, espadaY);
        golpe.draw(batch, espadaX, espadaY, getWidth(), getHeight(), direccionMirando.angleDeg());
    }

    public float getTiempoDesdeUltimoGolpe() { return tiempoDesdeUltimoGolpe; }

    @Override
    public void dispose() {
        super.dispose();
        golpe.dispose();
    }
}
