package winterwolves.personajes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.World;
import winterwolves.io.EntradasJugador;
import winterwolves.personajes.habilidadesGuerrero.*;

public class Guerrero extends Personaje implements Hudeable {

    protected Arma armaBasica;
    public HabilidadCuracion habilidad1;
    public ConcentracionMaxima habilidad2;

    private float velocidadBase = 200f;

    public Guerrero(World world, EntradasJugador entradas, float x, float y, float ppm) {
        super(world, entradas, x, y, ppm);
        this.armaBasica = new Espada(world, ppm);
        this.habilidad1 = new HabilidadCuracion(2f, 10f, 30);
        this.habilidad2 = new ConcentracionMaxima(10f, 15f, 2f, 20f);
    }

    @Override
    public void draw(Batch batch) {
        float delta = Gdx.graphics.getDeltaTime();

        // Actualizar habilidades
        habilidad1.actualizar(delta);
        habilidad2.actualizar(delta);

        // Usar concentración máxima si se presiona y puede usarse
        if (entradas.isHabilidad2() && habilidad2.puedeUsarse()) {
            usarConcentracionMaxima();
        }

        // Bloquear movimiento mientras se usan habilidades o el arma está activa
        setPuedeMoverse(!habilidad2.isCargando() && !habilidad1.isActiva() && !armaBasica.isActivo());

        super.draw(batch);

        // Posición del arma
        float desplazamiento = getWidth() * 0.6f;
        float armaX = getX() + direccionMirando.x * desplazamiento;
        float armaY = getY() + direccionMirando.y * desplazamiento;

        // Golpe básico solo si el jugador lo solicita
        if (entradas.isGolpeBasico()) {
            armaBasica.atacar(armaX, armaY, direccionMirando, this);
        }

        // Actualizar y dibujar arma
        armaBasica.actualizar(delta, armaX, armaY, this);
        armaBasica.draw(batch, armaX, armaY, getWidth(), getHeight(), direccionMirando.angleDeg());

        // Dibujar animaciones de habilidades
        habilidad1.dibujar(batch, getX(), getY(), getWidth(), getHeight());
        habilidad2.dibujar(batch, getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void usarHabilidadEspecial() {
        int vidaRecuperada = habilidad1.usar();
        if (vidaRecuperada > 0) {
            vida += vidaRecuperada;
            if (vida > 100) vida = 100;
            setPuedeMoverse(false); // Bloqueo de movimiento durante curación
        }
    }

    public float getVelocidadActual() {
        if (habilidad2.isCargando()) return 0f;
        return velocidadBase + habilidad2.getBonusVelocidad();
    }

    public boolean usarConcentracionMaxima() {
        return habilidad2.usar();
    }

    public Arma getArma() {
        return armaBasica;
    }

    // -----------------------------
    // Métodos para HUD (HUDEable)
    // -----------------------------
    public float getTiempoHabilidad1() { return habilidad1.getTiempoDesdeUltimoUso(); }
    public float getCooldownHabilidad1() { return habilidad1.getCooldown(); }
    public float getTiempoHabilidad2() { return habilidad2.getTiempoDesdeUltimoUso(); }
    public float getCooldownHabilidad2() { return habilidad2.getCooldown(); }
    public float getTiempoDesdeUltimoDash() { return tiempoDesdeUltimoDash; }
    public float getCooldownDash() { return COOLDOWN_DASH; }

    @Override
    public int getVida() { return vida; }

    @Override
    public void dispose() {
        super.dispose();
        armaBasica.dispose();
        habilidad1.dispose();
        habilidad2.dispose();
    }
}
