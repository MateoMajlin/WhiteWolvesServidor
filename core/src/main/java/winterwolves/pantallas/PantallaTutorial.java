package winterwolves.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import winterwolves.elementos.Imagen;
import winterwolves.elementos.Texto;
import winterwolves.io.Entradas;
import winterwolves.utilidades.Config;
import winterwolves.utilidades.Recursos;
import winterwolves.utilidades.Render;

public class PantallaTutorial implements Screen {

    Imagen fondo,mashoop;
    SpriteBatch b;

    String textosOpc[] = {"Controles","Inventario","Cofres","Objetivo"};
    Texto opciones[] = new Texto[textosOpc.length];

    Entradas entradas = new Entradas(this);

    int opc = 1;
    public float tiempo = 0;

    @Override
    public void show() {
        fondo = new Imagen(Recursos.FONDOTUTO);
        fondo.escalar(Config.WIDTH,Config.HEIGTH);
        mashoop = new Imagen(Recursos.MASHOOP);
        b = Render.batch;

        cargarOpciones();

        Gdx.input.setInputProcessor(entradas);
    }

    @Override
    public void render(float delta) {
        actualizarTiempo(delta);
        dibujar();
        manejarEntradas();
        actualizarColoresOpciones();
    }

    private void dibujar() {
        b.begin();
        fondo.dibujar();
        mashoop.dibujar();
        for (int i = 0; i < opciones.length; i++) {
            opciones[i].dibujar();
        }
        b.end();
    }

    private void actualizarTiempo(float delta) {
        tiempo += delta;
    }

    private void cargarOpciones() {
        int avance = 120;

        for (int i = 0; i < opciones.length; i++) {
            opciones[i] = new Texto(Recursos.FUENTEMENU,80,Color.WHITE,true);
            opciones[i].setColor(Color.RED); //nota flashera: si inicializas su color en algo diferente a blanco despues al querer cambiarle de color se va a quedar negro.
            opciones[i].setTexto(textosOpc[i]);
            opciones[i].setPosition((Config.WIDTH/2)-(opciones[i].getAncho()/2)+180,(Config.HEIGTH)-(opciones[i].getAlto())-(avance*i));
        }
    }

    private void manejarEntradas() {

        if (entradas.isAbajo() && tiempo > 0.1f) {
            tiempo = 0;
            opc++;
            if (opc > opciones.length) {
                opc = 1;
            }
        }

        if (entradas.isArriba() && tiempo > 0.1f) {
            tiempo = 0;
            opc--;
            if (opc < 1) {
                opc = opciones.length;
            }
        }

        if (entradas.isEnter()) {
            ejecutarOpcion();
        }
    }

    private void ejecutarOpcion() {
        Recursos.musica.stop();
        Recursos.musica.dispose();
        switch (opc) {
            case 1:
                Render.app.setScreen(new Menu());
                break;
            case 2:
                Render.app.setScreen(new Menu());
                break;
            case 3:
                Render.app.setScreen(new Menu());
                break;
            case 4:
                Render.app.setScreen(new Menu());
                break;
        }
    }

    private void actualizarColoresOpciones() {
        for (int i = 0; i < opciones.length; i++) {
            if (i == (opc - 1)) {
                opciones[i].setColor(Color.GREEN);
            } else {
                opciones[i].setColor(Color.RED);
            }
        }
    }
    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
