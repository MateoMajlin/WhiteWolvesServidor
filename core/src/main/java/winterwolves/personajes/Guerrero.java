package winterwolves.personajes;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.World;
import winterwolves.io.EntradasJugador;
import winterwolves.personajes.habilidadesGuerrero.GolpeEspada;
import winterwolves.personajes.habilidadesGuerrero.HabilidadCuracion;
import winterwolves.personajes.habilidadesGuerrero.GolpeArma;

public class Guerrero extends Personaje {

    protected GolpeArma armaBasica;
    private HabilidadCuracion habilidad1;

    public Guerrero(World world, EntradasJugador entradas, float x, float y, float ppm) {
        super(world, entradas, x, y, ppm);
        this.armaBasica = new GolpeEspada(world, ppm);

        this.habilidad1 = new HabilidadCuracion(2f, 10f, 30);
    }

    @Override
    public void draw(Batch batch) {
        float delta = com.badlogic.gdx.Gdx.graphics.getDeltaTime();

        habilidad1.actualizar(delta);

        setPuedeMoverse(!habilidad1.isActiva());

        super.draw(batch);

        float desplazamiento = getWidth() * 0.6f;
        float armaX = getX() + direccionMirando.x * desplazamiento;
        float armaY = getY() + direccionMirando.y * desplazamiento;

        GolpeArma.Direccion dir = GolpeArma.vectorADireccion(direccionMirando);

        ataqueBasico(armaX, armaY, dir);
        actualizarYdibujarGolpe(batch, delta, armaX, armaY);

        // --- Dibujar animación de curación ---
        habilidad1.dibujar(batch, getX(), getY(), getWidth(), getHeight());
    }

    // === Ataque básico ===
    protected void ataqueBasico(float x, float y, GolpeArma.Direccion dir) {
        if (entradas.isGolpeBasico() && !armaBasica.isActivo() &&
            armaBasica.getTiempoDesdeUltimoGolpe() >= armaBasica.getCooldown()) {
            armaBasica.activar(x, y, dir);
            setPuedeMoverse(false);
        }

        if (!armaBasica.isActivo()) {
            setPuedeMoverse(true);
        }
    }

    protected void actualizarYdibujarGolpe(Batch batch, float delta, float x, float y) {
        armaBasica.update(delta, x, y);
        armaBasica.draw(batch, x, y, getWidth(), getHeight(), direccionMirando.angleDeg());
    }

    @Override
    public void usarHabilidadEspecial() {
        int vidaRecuperada = habilidad1.usar();
        if (vidaRecuperada > 0) {
            vida += (int)(100 * (vidaRecuperada / 100f));
            if (vida > 100) vida = 100;
            setPuedeMoverse(false);
        }
    }

    public GolpeArma getArma() {
        return armaBasica;
    }

    public float getTiempoHabilidad1() {
        return habilidad1.getTiempoDesdeUltimoUso();
    }

    public float getCooldownHabilidad1() {
        return habilidad1.getCooldown();
    }

    @Override
    public void dispose() {
        super.dispose();
        armaBasica.dispose();
    }
}
