package winterwolves.pantallas;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import winterwolves.elementos.Imagen;
import winterwolves.utilidades.Recursos;
import winterwolves.utilidades.Render;

public class Menu implements Screen {

    Imagen fondo;
    SpriteBatch b;

    @Override
    public void show() {
        fondo = new Imagen(Recursos.FONDO);
        b = Render.batch;
        fondo.escalar(640,480);
        fondo.setTransparencia(0.5f);
    }

    @Override
    public void render(float delta) {
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
