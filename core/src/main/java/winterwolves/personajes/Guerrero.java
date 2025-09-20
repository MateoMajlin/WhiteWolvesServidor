package winterwolves.personajes;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.World;
import winterwolves.io.EntradasJugador;
import winterwolves.personajes.habilidadesGuerrero.*;

public class Guerrero extends Personaje {

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
        float delta = com.badlogic.gdx.Gdx.graphics.getDeltaTime();

        habilidad1.actualizar(delta);
        habilidad2.actualizar(delta);

        if (entradas.isHabilidad2() && habilidad2.puedeUsarse()) {
            usarConcentracionMaxima();
        }

        setPuedeMoverse(!habilidad2.isCargando() && !habilidad1.isActiva());

        super.draw(batch);

        float desplazamiento = getWidth() * 0.6f;
        float armaX = getX() + direccionMirando.x * desplazamiento;
        float armaY = getY() + direccionMirando.y * desplazamiento;

        if (entradas.isGolpeBasico()) {
            armaBasica.atacar(armaX, armaY, direccionMirando, this);
        }
        armaBasica.actualizar(delta, armaX, armaY, this);
        armaBasica.draw(batch, armaX, armaY, getWidth(), getHeight(), direccionMirando.angleDeg());


        habilidad1.dibujar(batch, getX(), getY(), getWidth(), getHeight());
        habilidad2.dibujar(batch, getX(), getY(), getWidth(), getHeight());
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

    public boolean usarConcentracionMaxima() { return habilidad2.usar(); }
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
