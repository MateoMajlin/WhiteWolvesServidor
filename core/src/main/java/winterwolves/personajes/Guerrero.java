package winterwolves.personajes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.World;
import winterwolves.io.EntradasJugador;
import winterwolves.items.ItemEquipable;
import winterwolves.personajes.habilidadesGuerrero.*;
import winterwolves.personajes.habilidades.Habilidad;

public class Guerrero extends Personaje implements Hudeable {

    protected Arma armaBasica;
    public Habilidad habilidad1;
    public Habilidad habilidad2;

    private float velocidadBase = 200f;

    public Guerrero(World world, EntradasJugador entradas, float x, float y, float ppm) {
        super(world, entradas, x, y, ppm);

        this.armaBasica = new Espada(world, ppm);
        this.habilidad1 = new HabilidadCuracion(2f, 10f, 30);
        this.habilidad2 = new ConcentracionMaxima(10f, 15f, 2f, 20f);

        habilidad1.setPersonaje(this);
        habilidad2.setPersonaje(this);
    }

    @Override
    public void draw(Batch batch) {
        float delta = Gdx.graphics.getDeltaTime();

        habilidad1.actualizar(delta);
        habilidad2.actualizar(delta);

        if (entradas.isHabilidad1()) habilidad1.usar();
        if (entradas.isHabilidad2()) habilidad2.usar();

        setPuedeMoverse(!habilidad2.isCargando() && !habilidad1.isActiva() && !armaBasica.isActivo());

        speed = getVelocidadActual();

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

    // Dentro de Guerrero
    private ItemEquipable slotHabilidad1;
    private ItemEquipable slotHabilidad2;

    public void equiparItem1(ItemEquipable item) {
        if (inventario.getItems().contains(item)) {
            this.slotHabilidad1 = item;
            this.habilidad1 = item.crearHabilidad();
            this.habilidad1.setPersonaje(this);
        }
    }

    public void equiparItem2(ItemEquipable item) {
        if (inventario.getItems().contains(item)) {
            this.slotHabilidad2 = item;
            this.habilidad2 = item.crearHabilidad();
            this.habilidad2.setPersonaje(this);
        }
    }

    public void quitarItem1() {
        this.slotHabilidad1 = null;
        this.habilidad1 = null;
    }

    public void quitarItem2() {
        this.slotHabilidad2 = null;
        this.habilidad2 = null;
    }

    public float getTiempoHabilidad1() { return habilidad1.getTiempoDesdeUltimoUso(); }
    public float getCooldownHabilidad1() { return habilidad1.getCooldown(); }

    public float getTiempoHabilidad2() { return habilidad2.getTiempoDesdeUltimoUso(); }
    public float getCooldownHabilidad2() { return habilidad2.getCooldown(); }

    public float getTiempoDesdeUltimoDash() { return dash.getTiempoDesdeUltimo(); }
    public float getCooldownDash() { return dash.getCooldown(); }

    public float getVelocidadActual() {
        if (habilidad2.isCargando()) return 0f;
        return velocidadBase + habilidad2.getBonusVelocidad();
    }

    public Arma getArma() { return armaBasica; }

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
