package winterwolves.pantallas;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import winterwolves.elementos.Imagen;
import winterwolves.utilidades.Config;
import winterwolves.utilidades.Recursos;
import winterwolves.utilidades.Render;

public class TerrenoPractica implements Screen {

    Imagen fondo;
    SpriteBatch b;

    @Override
    public void show() {
        fondo = new Imagen(Recursos.FONDO_PRACTICAS);
        fondo.escalar(Config.WIDTH,Config.HEIGTH);
        b = Render.batch;
    }

    @Override
    public void render(float delta) {
        Render.limpiarPantalla(1,1,1);
        b.begin();
        fondo.dibujar();
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
}
