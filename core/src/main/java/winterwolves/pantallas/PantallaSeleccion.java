package winterwolves.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

import winterwolves.elementos.Texto;
import winterwolves.elementos.Imagen;
import winterwolves.utilidades.Config;
import winterwolves.utilidades.Recursos;
import winterwolves.utilidades.Render;

import winterwolves.network.ServerThread;
import winterwolves.network.Client;

import java.util.ArrayList;

public class PantallaSeleccion implements Screen {

    private Imagen fondo;
    private Texto titulo;
    private Texto[] jugadoresTxt = new Texto[2];
    private Texto estado;

    private ServerThread server;
    private float tiempo = 0;
    private boolean juegoIniciando = false;

    public PantallaSeleccion(ServerThread serverThread) {
        this.server = serverThread;
    }

    @Override
    public void show() {

        fondo = new Imagen("fondoSeleccion.png");

        titulo = new Texto(Recursos.FUENTEMENU, 50, Color.WHITE, true);
        titulo.setTexto("Esperando jugadores...");

        jugadoresTxt[0] = new Texto(Recursos.FUENTEMENU, 30, Color.YELLOW, true);
        jugadoresTxt[1] = new Texto(Recursos.FUENTEMENU, 30, Color.YELLOW, true);

        estado = new Texto(Recursos.FUENTEMENU, 35, Color.WHITE, true);
        estado.setTexto("");
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        fondo.escalar(Config.WIDTH, Config.HEIGTH);

        Render.batch.begin();
        fondo.dibujar();

        titulo.setPosition(Config.WIDTH/2f - titulo.getAncho()/2f, Config.HEIGTH - 80);
        titulo.dibujar();

        Render.batch.end();

        mostrarClientes(delta);
    }

    private void mostrarClientes(float delta) {

        ArrayList<Client> clientes = server.getClients();

        Render.batch.begin();

        for (int i = 0; i < jugadoresTxt.length; i++) {

            if (i < clientes.size()) {

                int pj = server.personajesElegidos.get(i);  // PERSONAJE QUE ELIGIÓ EL CLIENTE

                jugadoresTxt[i].setTexto("Jugador " + (i+1) + " conectado - PJ = " + pj);

            } else {
                jugadoresTxt[i].setTexto("Jugador " + (i+1) + ": esperando...");
            }

            jugadoresTxt[i].setPosition(
                Config.WIDTH/2f - jugadoresTxt[i].getAncho()/2f,
                Config.HEIGTH - 200 - i * 40
            );

            jugadoresTxt[i].dibujar();
        }

        estado.setPosition(Config.WIDTH/2f - estado.getAncho()/2f, 80);
        estado.dibujar();

        Render.batch.end();

        if (clientes.size() == 2 && !juegoIniciando) {
            juegoIniciando = true;
            estado.setTexto("¡Jugadores conectados! Iniciando...");
        }

        if (juegoIniciando) {
            tiempo += delta;
            if (tiempo >= 2) iniciarPartida();
        }
    }

    private void iniciarPartida() {

        int[] personajes = new int[2];
        personajes[0] = server.personajesElegidos.get(0);
        personajes[1] = server.personajesElegidos.get(1);

        MapaNieve mapa = new MapaNieve(personajes, server);

        Render.app.setScreen(mapa);
        mapa.startGame();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
