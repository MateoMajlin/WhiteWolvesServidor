package winterwolves.personajes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.World;
import winterwolves.io.EntradasJugador;
import winterwolves.items.ItemEquipable;
import winterwolves.personajes.habilidadesGuerrero.Arma;
import winterwolves.personajes.habilidadesGuerrero.ConcentracionMaxima;
import winterwolves.personajes.habilidadesGuerrero.Espada;
import winterwolves.personajes.habilidades.Habilidad;
import winterwolves.personajes.habilidadesGuerrero.HabilidadCuracion;

public class Guerrero extends Personaje implements Hudeable {

    protected Arma armaBasica;
    public Habilidad habilidad1;
    public Habilidad habilidad2;

    private float velocidadBase = 200f;

    // Campos para el World y PPM
    protected World world;
    protected float ppm;

    // Slots de items
    private ItemEquipable slotArma;
    private ItemEquipable slotHabilidad1;
    private ItemEquipable slotHabilidad2;

    public Guerrero(World world, EntradasJugador entradas, float x, float y, float ppm) {
        super(world, entradas, x, y, ppm);
        this.world = world;  // Guardar referencia
        this.ppm = ppm;      // Guardar ppm

        // Arma y habilidades por defecto
        this.armaBasica = new Espada(world, ppm);
        this.habilidad1 = new HabilidadCuracion(2f, 10f, 30);
        this.habilidad2 = new ConcentracionMaxima(10f, 15f, 2f, 20f);

        habilidad1.setPersonaje(this);
        habilidad2.setPersonaje(this);
    }

    @Override
    public void draw(Batch batch) {
        float delta = Gdx.graphics.getDeltaTime();

        // Actualizar habilidades
        if (habilidad1 != null) habilidad1.actualizar(delta);
        if (habilidad2 != null) habilidad2.actualizar(delta);

        // Usar habilidades
        if (entradas.isHabilidad1() && habilidad1 != null) habilidad1.usar();
        if (entradas.isHabilidad2() && habilidad2 != null) habilidad2.usar();

        // Actualizar velocidad
        speed = getVelocidadActual();

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


    public void equiparArma(ItemEquipable item) {
        if (inventario.getItems().contains(item)) {
            if (armaBasica != null) armaBasica.dispose();
            slotArma = item;
            armaBasica = item.crearArma(world, ppm);
        }
    }

    public void quitarArma() {
        if (armaBasica != null) armaBasica.dispose();
        armaBasica = null;
        slotArma = null;
    }

    public void equiparItem1(ItemEquipable item) {
        if (inventario.getItems().contains(item)) {
            slotHabilidad1 = item;
            habilidad1 = item.crearHabilidad();
            if (habilidad1 != null) habilidad1.setPersonaje(this);
        }
    }

    public void equiparItem2(ItemEquipable item) {
        if (inventario.getItems().contains(item)) {
            slotHabilidad2 = item;
            habilidad2 = item.crearHabilidad();
            if (habilidad2 != null) habilidad2.setPersonaje(this);
        }
    }

    public void quitarItem1() { habilidad1 = null; slotHabilidad1 = null; }
    public void quitarItem2() { habilidad2 = null; slotHabilidad2 = null; }

    public float getTiempoHabilidad1() { return habilidad1 != null ? habilidad1.getTiempoDesdeUltimoUso() : 0f; }
    public float getCooldownHabilidad1() { return habilidad1 != null ? habilidad1.getCooldown() : 0f; }

    public float getTiempoHabilidad2() { return habilidad2 != null ? habilidad2.getTiempoDesdeUltimoUso() : 0f; }
    public float getCooldownHabilidad2() { return habilidad2 != null ? habilidad2.getCooldown() : 0f; }

    public float getTiempoDesdeUltimoDash() { return dash.getTiempoDesdeUltimo(); }
    public float getCooldownDash() { return dash.getCooldown(); }

    public float getVelocidadActual() {
        return (habilidad2 != null && habilidad2.isCargando()) ? 0f : velocidadBase + (habilidad2 != null ? habilidad2.getBonusVelocidad() : 0f);
    }

    public Arma getArma() { return armaBasica; }

    @Override
    public int getVida() { return vida; }

    @Override
    public void dispose() {
        super.dispose();
        if (armaBasica != null) armaBasica.dispose();
        if (habilidad1 != null) habilidad1.dispose();
        if (habilidad2 != null) habilidad2.dispose();
    }
}
