package winterwolves.pantallas;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import winterwolves.elementos.Imagen;
import winterwolves.utilidades.Config;
import winterwolves.utilidades.Recursos;
import winterwolves.utilidades.Render;

public class PantallaCarga implements Screen {
    Imagen fondo;
    SpriteBatch b;
    boolean fadeInTerminado = false, termina = false;
    float a = 0;
    float contTiempo = 0, tiempoEspera = 5;
    float contTiempoTermina = 0, tiempoTermina = 5;

    @Override
    public void show() {
        fondo = new Imagen(Recursos.LOGO);
        b = Render.batch;
        fondo.setTransparencia(a);
        fondo.escalar(Config.WIDTH,Config.HEIGTH);
    }

    @Override
    public void render(float delta) {
        Render.limpiarPantalla(0,0,0);

        b.begin();
            fondo.dibujar();
        b.end();

        procesarFade();
    }

    private void procesarFade() {

        if (!fadeInTerminado) {
            a += 0.01f;
            if (a > 1) {
                a = 1;
                fadeInTerminado = true;
            }
        } else {
            contTiempo += 0.05f;
            if(contTiempo>tiempoEspera) {
                a -= 0.01f;
                if (a < 0){
                    a = 0;
                    termina = true;
                }
            }
        }
        fondo.setTransparencia(a);

        if(termina){
            contTiempoTermina += 0.1f;
            if(contTiempoTermina>tiempoTermina){
                Render.app.setScreen(new Menu());
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

