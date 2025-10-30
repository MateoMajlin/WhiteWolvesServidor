package winterwolves.utilidades;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import winterwolves.Jugador;
import winterwolves.io.*;
import winterwolves.personajes.Personaje;

public class PlayerManager {

    private Jugador jugador1;
    private Jugador jugador2;
    private Jugador jugador3;
    private Jugador jugador4;

    public PlayerManager(World world, int[] personajesElegidosIdx, float PPM, OrthographicCamera camaraHud) {
        // Entradas separadas para cada jugador (puedes personalizarlas después)
        EntradasJugador entradas1 = new EntradasJugador();
        EntradasJugador entradas2 = new EntradasJugador2();
        EntradasJugador entradas3 = new EntradasJugador2(); // crea esta clase igual que las otras
        EntradasJugador entradas4 = new EntradasJugador2(); // idem

        Personaje[] personajes = new Personaje[4];
        float[] posicionesX = {4f, 6f, 20f, 22f};
        float[] posicionesY = {2.5f, 3f, 8f, 10f};

        for (int i = 0; i < 4; i++) {
            switch (personajesElegidosIdx[i]) {
                case 0: personajes[i] = new winterwolves.personajes.clases.Guerrero(world, getEntradas(i, entradas1, entradas2, entradas3, entradas4), posicionesX[i], posicionesY[i], PPM, camaraHud); break;
                case 1: personajes[i] = new winterwolves.personajes.clases.Mago(world, getEntradas(i, entradas1, entradas2, entradas3, entradas4), posicionesX[i], posicionesY[i], PPM, camaraHud); break;
                case 2: personajes[i] = new winterwolves.personajes.clases.Clerigo(world, getEntradas(i, entradas1, entradas2, entradas3, entradas4), posicionesX[i], posicionesY[i], PPM, camaraHud); break;
            }
        }

        jugador1 = new Jugador("Jugador1", world, entradas1, 450 / PPM, 450 / PPM, PPM, camaraHud, personajes[0]);
        jugador2 = new Jugador("Jugador2", world, entradas2, 650 / PPM, 450 / PPM, PPM, camaraHud, personajes[1]);
        jugador3 = new Jugador("Jugador3", world, entradas3, 1150 / PPM, 450 / PPM, PPM, camaraHud, personajes[2]);
        jugador4 = new Jugador("Jugador4", world, entradas4, 1250 / PPM, 450 / PPM, PPM, camaraHud, personajes[3]);
    }

    private EntradasJugador getEntradas(int idx, EntradasJugador e1, EntradasJugador e2, EntradasJugador e3, EntradasJugador e4) {
        switch (idx) {
            case 0: return e1;
            case 1: return e2;
            case 2: return e3;
            case 3: return e4;
        }
        return e1;
    }

    public void actualizar(float delta) {
        jugador1.getPersonaje().actualizarInventario();
        jugador2.getPersonaje().actualizarInventario();
        jugador3.getPersonaje().actualizarInventario();
        jugador4.getPersonaje().actualizarInventario();
    }

    public void draw(Batch batch) {
        jugador1.getPersonaje().draw(batch);
        jugador2.getPersonaje().draw(batch);
        jugador3.getPersonaje().draw(batch);
        jugador4.getPersonaje().draw(batch);
    }

    public void drawHud(Batch batch) {
        jugador1.drawHud(Render.batch);
        jugador2.drawHud(Render.batch);
    }

    public Jugador getJugador1() { return jugador1; }
    public Jugador getJugador2() { return jugador2; }
    public Jugador getJugador3() { return jugador3; }
    public Jugador getJugador4() { return jugador4; }

    public Vector2 getPosicionJugador1() { return getPos(jugador1); }
    public Vector2 getPosicionJugador2() { return getPos(jugador2); }
    public Vector2 getPosicionJugador3() { return getPos(jugador3); }
    public Vector2 getPosicionJugador4() { return getPos(jugador4); }

    private Vector2 getPos(Jugador j) {
        return new Vector2(j.getPersonaje().getX() + j.getPersonaje().getWidth() / 2,
            j.getPersonaje().getY() + j.getPersonaje().getHeight() / 2);
    }

    public void setPuedeMoverse(boolean puedeMoverse) {
        jugador1.getPersonaje().setPuedeMoverse(puedeMoverse);
        jugador2.getPersonaje().setPuedeMoverse(puedeMoverse);
        jugador3.getPersonaje().setPuedeMoverse(puedeMoverse);
        jugador4.getPersonaje().setPuedeMoverse(puedeMoverse);
    }

    public void dispose() {
        jugador1.dispose();
        jugador2.dispose();
        jugador3.dispose();
        jugador4.dispose();
    }
}
