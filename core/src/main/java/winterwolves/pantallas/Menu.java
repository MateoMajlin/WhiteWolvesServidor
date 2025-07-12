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
import winterwolves.utilidades.Config;
import winterwolves.utilidades.Recursos;
import winterwolves.utilidades.Render;

public class Menu implements Screen {

    Imagen fondo;
    SpriteBatch b;

    Texto titulo;
    Texto o1,o2,o3,o4;

    @Override
    public void show() {
        fondo = new Imagen(Recursos.FONDO);
        fondo.escalar(Config.WIDTH,Config.HEIGTH);
        b = Render.batch;
        cargarTexto();

    }

    @Override
    public void render(float delta) {
        b.begin();
            fondo.dibujar();
            titulo.dibujar();
            o1.dibujar();
            o2.dibujar();
            o3.dibujar();
            o4.dibujar();
        b.end();
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

    private void cargarTexto() {
        titulo = new Texto(Recursos.FUENTEMENU,150,Color.BLACK,true);
        titulo.setTexto("WHITE WOLVES");
        titulo.setPosition((Config.WIDTH/2)-(titulo.getAncho()/2),(Config.HEIGTH/2)+(titulo.getAlto()/2)+200);

        o1 = new Texto(Recursos.FUENTEMENU,80,Color.WHITE,true);
        o1.setTexto("Nueva Partida");
        o1.centrar();

        o2 = new Texto(Recursos.FUENTEMENU,80,Color.WHITE,true);
        o2.setTexto("Opciones");
        o2.setPosition((Config.WIDTH/2)-(o2.getAncho()/2),(Config.HEIGTH/2)+(o2.getAlto()/2)-80);

        o3 = new Texto(Recursos.FUENTEMENU,80,Color.WHITE,true);
        o3.setTexto("Creditos");
        o3.setPosition((Config.WIDTH/2)-(o3.getAncho()/2),(Config.HEIGTH/2)+(o3.getAlto()/2)-160);

        o4 = new Texto(Recursos.FUENTEMENU,80,Color.WHITE,true);
        o4.setTexto("Salir");
        o4.setPosition((Config.WIDTH/2)-(o4.getAncho()/2),(Config.HEIGTH/2)+(o4.getAlto()/2)-240);
    }
}
