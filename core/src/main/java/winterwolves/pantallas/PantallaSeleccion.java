package winterwolves.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import winterwolves.elementos.Imagen;
import winterwolves.elementos.Texto;
import winterwolves.utilidades.Config;
import winterwolves.utilidades.Recursos;
import winterwolves.utilidades.Render;

public class PantallaSeleccion implements Screen {

    private Imagen fondo;
    private Imagen[] personajesImg;
    private Texto eleccion;
    private Texto pjElegido;
    private int seleccion = 0;
    ShapeRenderer[] personajesCasillas;
    private final int CANT_PJS = 3;
    private final int casillaAncho = 200;
    private final int casillaAlto = 500;
    private int i = 0;
    public int[] personajesElegidosIdx;
    private int personajesElegidos = 0;
    private boolean partidaIniciada = false;
    private float tiempoEspera = 0f;
    private boolean esperando = false;
    private boolean mostrarMensajeInicio = false;


    @Override
    public void show() {
        fondo = new Imagen("fondoSeleccion.png");
        personajesImg = new Imagen[CANT_PJS];
        personajesImg[0] = new Imagen("zorroEleccion.png");
        personajesImg[1] = new Imagen("pajaroEleccion.png");
        personajesImg[2] = new Imagen("gatoEleccion.png");

        eleccion = new Texto(Recursos.FUENTEMENU, 50, Color.WHITE, true);
        pjElegido = new Texto(Recursos.FUENTEMENU,30,Color.WHITE,true);

        personajesCasillas = new ShapeRenderer[CANT_PJS];
        for (int i = 0; i < CANT_PJS; i++) {
            personajesCasillas[i] = new ShapeRenderer();
        }

        personajesElegidosIdx = new int[2];
        eleccion.setTexto("Elija a su personaje");
        pjElegido.setTexto(" ");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        fondo.escalar(Config.WIDTH, Config.HEIGTH);
        eleccion.setPosition((Config.WIDTH / 2f) - (eleccion.getAncho())/2, Config.HEIGTH - 50);

        pjElegido.setPosition((Config.WIDTH / 2f) - (pjElegido.getAncho()/2), 50);

        Render.batch.begin();
        fondo.dibujar();
        eleccion.dibujar();
        pjElegido.dibujar();
        Render.batch.end();

        dibujarCasillas();

        actualizar();
        elegirPersonaje();
        iniciarPartida();
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

    private void dibujarCasillas() {
        for (int i = 0; i < CANT_PJS; i++) {
            int espaciado = 50;
            float anchoTotal = (CANT_PJS * casillaAncho) + ((CANT_PJS - 1) * espaciado);
            float inicioX = (Config.WIDTH - anchoTotal) / 2f;
            float x = inicioX + i * (casillaAncho + espaciado);
            int y = 100;

            personajesCasillas[i].begin(ShapeRenderer.ShapeType.Filled);
            personajesCasillas[i].setColor(i == seleccion ? Color.ORANGE : Color.DARK_GRAY);
            personajesCasillas[i].rect(x, y, casillaAncho, casillaAlto);
            personajesCasillas[i].end();

            Render.batch.begin();
            personajesImg[i].escalar(casillaAncho - 10, casillaAlto - 10);
            personajesImg[i].setPosition((int) x + 5, y + 5);
            personajesImg[i].dibujar();
            Render.batch.end();
        }
    }

    public void actualizar() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            seleccion = (seleccion + 1) % CANT_PJS;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            seleccion = (seleccion - 1 + CANT_PJS) % CANT_PJS;
        }
    }

    public void elegirPersonaje() {
        String[] elegido = {"Guerrero","Mago","Clerigo"};
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (personajesElegidos < 2) {
                personajesElegidosIdx[personajesElegidos] = seleccion;
                personajesElegidos++;
                pjElegido.setTexto("Personaje Elegido Por Jugador " + personajesElegidos + ": " + elegido[seleccion]);
            }
        }
    }

    private void iniciarPartida() {
        if (!partidaIniciada && personajesElegidos == 2) {
            partidaIniciada = true;
            esperando = true;
            tiempoEspera = 0f;
        }

        if (esperando) {
            tiempoEspera += Gdx.graphics.getDeltaTime();

            if (tiempoEspera >= 1f && !mostrarMensajeInicio) {
                mostrarMensajeInicio = true;
                pjElegido.setTexto("¡Empezando la partida!");
                pjElegido.setPosition((Config.WIDTH / 2f) - (pjElegido.getAncho() / 2), 50);
            }

            if (tiempoEspera >= 3f) {
                esperando = false;
                Render.app.setScreen(new TerrenoPractica(personajesElegidosIdx));
            }
        }
    }

}
