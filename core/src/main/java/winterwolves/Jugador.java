package winterwolves;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.graphics.OrthographicCamera;
import winterwolves.io.EntradasJugador;
import winterwolves.personajes.Hud;
import winterwolves.personajes.InventarioHud;
import winterwolves.personajes.Personaje;
import winterwolves.personajes.clases.Mago;

public class Jugador {

    private Personaje personaje;
    private EntradasJugador entradas;
    private Hud hud;
    private InventarioHud inventarioHud;

    private OrthographicCamera camaraHud;
    private World world;
    private float ppm;

    private String nombre;

    public Jugador(String nombre, World world, EntradasJugador entradas, float x, float y, float ppm, OrthographicCamera camaraHud) {
        this.nombre = nombre;
        this.world = world;
        this.entradas = entradas;
        this.ppm = ppm;
        this.camaraHud = camaraHud;

        this.personaje = new Mago(world, entradas, x, y, ppm, camaraHud);

        this.hud = personaje.hud;
        this.inventarioHud = personaje.inventarioHud;
    }

    public void update() {
        personaje.actualizarInventario();
    }

    public void draw(SpriteBatch batch) {
        personaje.draw(batch);
    }

    public void drawHud(SpriteBatch batch) {
        if (inventarioHud != null && inventarioHud.isVisible()) {
            personaje.dibujarInventario(batch);
        } else {
            personaje.dibujarHud(batch);
        }
    }

    public Personaje getPersonaje() {
        return personaje;
    }

    public void cambiarClase(Personaje nuevo) {
        if (this.personaje != null) {
            this.personaje.dispose();
        }
        this.personaje = nuevo;
        this.hud = nuevo.hud;
        this.inventarioHud = nuevo.inventarioHud;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nuevoNombre) {
        this.nombre = nuevoNombre;
    }

    public void toggleInventario() {
        personaje.toggleInventario();
    }

    public void dispose() {
        personaje.dispose();
    }
}
