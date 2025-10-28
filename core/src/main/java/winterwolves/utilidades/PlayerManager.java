package winterwolves.utilidades;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.math.Vector2;

import winterwolves.Jugador;
import winterwolves.personajes.Personaje;
import winterwolves.io.EntradasJugador;
import winterwolves.io.EntradasJugador2;

public class PlayerManager {

    private Jugador jugador1;
    private Jugador jugador2;

    public PlayerManager(World world, int[] personajesElegidosIdx, float PPM, OrthographicCamera camaraHud) {
        EntradasJugador entradas1 = new EntradasJugador();
        EntradasJugador entradas2 = new EntradasJugador2();

        Personaje[] personajes = new Personaje[2];
        float[] posicionesX = {4f, 21f};
        float[] posicionesY = {2.5f, 10f};

        for (int i = 0; i < 2; i++) {
            switch (personajesElegidosIdx[i]) {
                case 0: personajes[i] = new winterwolves.personajes.clases.Guerrero(world, i==0?entradas1:entradas2, posicionesX[i], posicionesY[i], PPM, camaraHud); break;
                case 1: personajes[i] = new winterwolves.personajes.clases.Mago(world, i==0?entradas1:entradas2, posicionesX[i], posicionesY[i], PPM, camaraHud); break;
                case 2: personajes[i] = new winterwolves.personajes.clases.Clerigo(world, i==0?entradas1:entradas2, posicionesX[i], posicionesY[i], PPM, camaraHud); break;
            }
        }

        // Crear jugadores
        jugador1 = new Jugador("Jugador1", world, entradas1, 450 / PPM, 450 / PPM, PPM, camaraHud, personajes[0]);
        jugador2 = new Jugador("Jugador2", world, entradas2, 650 / PPM, 650 / PPM, PPM, camaraHud, personajes[1]);
    }

    public void actualizar(float delta) {
        jugador1.getPersonaje().actualizarInventario();
        jugador2.getPersonaje().actualizarInventario();
    }

    public void draw(Batch batch) {
        jugador1.getPersonaje().draw(batch);
        jugador2.getPersonaje().draw(batch);
    }

    public void drawHud(Batch batch) {
        jugador1.drawHud(Render.batch);
    }

    public void toggleInventarioJugador1() {
        jugador1.toggleInventario();
    }

    public void toggleInventarioJugador2() {
        jugador2.toggleInventario();
    }

    public Jugador getJugador1() { return jugador1; }
    public Jugador getJugador2() { return jugador2; }

    public Vector2 getPosicionJugador1() {
        return new Vector2(jugador1.getPersonaje().getX() + jugador1.getPersonaje().getWidth()/2,
            jugador1.getPersonaje().getY() + jugador1.getPersonaje().getHeight()/2);
    }

    public Vector2 getPosicionJugador2() {
        return new Vector2(jugador2.getPersonaje().getX() + jugador2.getPersonaje().getWidth()/2,
            jugador2.getPersonaje().getY() + jugador2.getPersonaje().getHeight()/2);
    }

    public void setPuedeMoverse(boolean puedeMoverse) {
        jugador1.getPersonaje().setPuedeMoverse(puedeMoverse);
        jugador2.getPersonaje().setPuedeMoverse(puedeMoverse);
    }

    public void dispose() {
        jugador1.dispose();
        jugador2.dispose();
    }
}
