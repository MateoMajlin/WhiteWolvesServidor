package winterwolves.personajes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.World;
import winterwolves.io.EntradasJugador;
import winterwolves.personajes.habilidadesGuerrero.*;
import winterwolves.personajes.habilidades.Habilidad;

public class Guerrero extends Personaje implements Hudeable {

    String nombreClase = "Guerrero";

    protected Arma armaBasica;

    public Habilidad habilidad1;
    public Habilidad habilidad2;

    private float velocidadBase = 200f;

    protected World world;
    protected float ppm;

    public Guerrero(World world, EntradasJugador entradas, float x, float y, float ppm) {
        super(world, entradas, x, y, ppm);
        this.world = world;
        this.ppm = ppm;

        this.armaBasica = new Hacha(world, ppm);
        this.habilidad1 = new HabilidadCuracion(2f, 10f, 30);
        this.habilidad2 = new ConcentracionMaxima(10f, 15f, 2f, 20f);

        habilidad1.setPersonaje(this);
        habilidad2.setPersonaje(this);

        this.ataque = armaBasica.getDaño();
        this.ataqueMagico = 10;
        this.defensa = 30;
        this.vida = 100;
    }

    @Override
    public void draw(Batch batch) {
        float delta = Gdx.graphics.getDeltaTime();

        if (habilidad1 != null) habilidad1.actualizar(delta);
        if (habilidad2 != null) habilidad2.actualizar(delta);

        if (entradas.isHabilidad1() && habilidad1 != null) habilidad1.usar();
        if (entradas.isHabilidad2() && habilidad2 != null) habilidad2.usar();

        super.draw(batch);

        if (armaBasica != null) {
            float desplazamiento = getWidth() * 0.6f;
            float armaX = getX() + direccionMirando.x * desplazamiento;
            float armaY = getY() + direccionMirando.y * desplazamiento;

            if (entradas.isGolpeBasico()) {
                armaBasica.atacar(armaX, armaY, direccionMirando, this);
            }

            armaBasica.actualizar(delta, armaX, armaY, this);
            armaBasica.draw(batch, armaX, armaY, getWidth(), getHeight(), direccionMirando.angleDeg());
        }

        if (habilidad1 != null) habilidad1.dibujar(batch, getX(), getY(), getWidth(), getHeight());
        if (habilidad2 != null) habilidad2.dibujar(batch, getX(), getY(), getWidth(), getHeight());
    }

    public float getTiempoHabilidad1() { return habilidad1 != null ? habilidad1.getTiempoDesdeUltimoUso() : 0f; }
    public float getCooldownHabilidad1() { return habilidad1 != null ? habilidad1.getCooldown() : 0f; }

    public float getTiempoHabilidad2() { return habilidad2 != null ? habilidad2.getTiempoDesdeUltimoUso() : 0f; }
    public float getCooldownHabilidad2() { return habilidad2 != null ? habilidad2.getCooldown() : 0f; }

    public float getTiempoDesdeUltimoDash() { return dash.getTiempoDesdeUltimo(); }
    public float getCooldownDash() { return dash.getCooldown(); }

    public Arma getArma() { return armaBasica; }

    @Override
    public int getVida() { return vida; }

    @Override
    public String getClase(){
        return nombreClase;
    }

    @Override
    public void dispose() {
        super.dispose();
        if (armaBasica != null) armaBasica.dispose();
        if (habilidad1 != null) habilidad1.dispose();
        if (habilidad2 != null) habilidad2.dispose();
    }
}
