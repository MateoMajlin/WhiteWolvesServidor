package winterwolves.personajes;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.World;
import winterwolves.io.EntradasJugador;
import winterwolves.personajes.habilidadesGuerrero.*;

public class Guerrero extends Personaje {

    protected Arma armaBasica;
    private HabilidadCuracion habilidad1;
    public ConcentracionMaxima habilidad2;

    private float velocidadBase = 200f;
    private int dañoBase = 10;

    public Guerrero(World world, EntradasJugador entradas, float x, float y, float ppm) {
        super(world, entradas, x, y, ppm);
        this.armaBasica = new Espada(world, ppm);
        this.habilidad1 = new HabilidadCuracion(2f, 10f, 30);
        this.habilidad2 = new ConcentracionMaxima(10f, 15f, 2f, 20f);
    }

    @Override
    public void draw(Batch batch) {
        float delta = com.badlogic.gdx.Gdx.graphics.getDeltaTime();

        habilidad1.actualizar(delta);
        habilidad2.actualizar(delta);

        if (entradas.isHabilidad2() && habilidad2.puedeUsarse()) {
           usarConcentracionMaxima();
        }

        if (habilidad2.isCargando()) {
            setPuedeMoverse(false);
        } else if (habilidad2.isActiva()) {
            setPuedeMoverse(!habilidad1.isActiva());
        } else {
            setPuedeMoverse(!habilidad1.isActiva());
        }

        super.draw(batch);

        // Posición del arma
        float desplazamiento = getWidth() * 0.6f;
        float armaX = getX() + direccionMirando.x * desplazamiento;
        float armaY = getY() + direccionMirando.y * desplazamiento;
        Arma.Direccion dir = Arma.vectorADireccion(direccionMirando);

        ataqueBasico(armaX, armaY, dir);

        actualizarYdibujarGolpe(batch, delta, armaX, armaY);

        habilidad1.dibujar(batch, getX(), getY(), getWidth(), getHeight());
        habilidad2.dibujar(batch, getX(), getY(), getWidth(), getHeight());
    }

    protected void ataqueBasico(float x, float y, Arma.Direccion dir) {
        if (entradas.isGolpeBasico() && armaBasica.puedeAtacar() && !armaBasica.isActivo()) {
            armaBasica.activar(x, y, dir);

            if (habilidad2.isActiva()) {
                float multiplicador = 1f + habilidad2.getBonusAtaque() / 100f;
                armaBasica.setMultiplicadorDaño(multiplicador);
            } else {
                armaBasica.setMultiplicadorDaño(1f);
            }

            setPuedeMoverse(false);
        }

        if (!armaBasica.isActivo() && !habilidad2.isCargando()) {
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
            vida += vidaRecuperada;
            if (vida > 100) vida = 100;
            setPuedeMoverse(false);
        }
    }

    public float getVelocidadActual() {
        if (habilidad2.isCargando()) return 0f;
        return velocidadBase + habilidad2.getBonusVelocidad();
    }

    public boolean usarConcentracionMaxima() {
        return habilidad2.usar();
    }

    public Arma getArma() { return armaBasica; }
    public float getTiempoHabilidad1() { return habilidad1.getTiempoDesdeUltimoUso(); }
    public float getCooldownHabilidad1() { return habilidad1.getCooldown(); }
    public float getTiempoHabilidad2() { return habilidad2.getTiempoDesdeUltimoUso(); }
    public float getCooldownHabilidad2() { return habilidad2.getCooldown(); }

    @Override
    public void dispose() {
        super.dispose();
        armaBasica.dispose();
        habilidad2.dispose();
    }
}
