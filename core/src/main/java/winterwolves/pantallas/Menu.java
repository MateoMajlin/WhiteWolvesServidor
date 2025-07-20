package winterwolves.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.Color;
import winterwolves.elementos.Imagen;
import winterwolves.elementos.Texto;
import winterwolves.io.Entradas;
import winterwolves.utilidades.Config;
import winterwolves.utilidades.Recursos;
import winterwolves.utilidades.Render;

public class Menu implements Screen {

    Imagen fondo;
    SpriteBatch b;

    Texto titulo;
    Texto opciones[] = new Texto[4];
    String textosOpc[] = {"Nueva Partida","Opciones","Creditos","Salir"};

    Entradas entradas = new Entradas(this);

    int opc = 1;
    public float tiempo = 0;


    @Override
    public void show() {
        fondo = new Imagen(Recursos.FONDO);
        fondo.escalar(Config.WIDTH,Config.HEIGTH);
        b = Render.batch;
        cargarOpciones();

        Gdx.input.setInputProcessor(entradas);

    }

    @Override
    public void render(float delta) {
        b.begin();
            fondo.dibujar();
            titulo.dibujar();
        for (int i = 0; i < opciones.length; i++) {
            opciones[i].dibujar();
        }
        b.end();

        tiempo+=delta;

        if(entradas.isAbajo()) {
            if(tiempo>0.1f){
                tiempo = 0;
                opc++;
                if(opc>4){
                    opc = 1;
                }
            }
        }
        if(entradas.isArriba()) {
            if(tiempo>0.1f){
                tiempo = 0;
                opc--;
                if(opc<1){
                    opc = 4;
                }
            }
        }

        for (int i = 0; i < opciones.length; i++) {
            if(i==(opc-1)){
                opciones[i].setColor(Color.GREEN);
            } else {
                opciones[i].setColor(Color.WHITE);
            }
        }

        if(entradas.isEnter()){
            if(opc == 1){
                Recursos.musica.stop();
                Render.app.setScreen(new TerrenoPractica());
                Recursos.musica.dispose();
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

    private void cargarOpciones() {

        int avance = 80;

        titulo = new Texto(Recursos.FUENTEMENU,150,Color.BLACK,true);
        titulo.setTexto("WHITE WOLVES");
        titulo.setPosition((Config.WIDTH/2)-(titulo.getAncho()/2),(Config.HEIGTH/2)+(titulo.getAlto()/2)+200);

        for (int i = 0; i < opciones.length; i++) {
            opciones[i] = new Texto(Recursos.FUENTEMENU,80,Color.WHITE,true);
            opciones[i].setTexto(textosOpc[i]);
            opciones[i].setPosition((Config.WIDTH/2)-(opciones[i].getAncho()/2),(Config.HEIGTH/2)+(opciones[i].getAlto()-(avance*i)));
        }
    }
}
