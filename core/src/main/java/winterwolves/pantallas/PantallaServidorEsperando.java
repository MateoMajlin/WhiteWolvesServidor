package winterwolves.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import winterwolves.elementos.Texto;
import winterwolves.network.ServerThread;
import winterwolves.utilidades.Config;
import winterwolves.utilidades.Recursos;
import winterwolves.utilidades.Render;

public class PantallaServidorEsperando implements Screen {

    private final ServerThread serverThread;
    private final Texto mensaje;
    private int conectados = 0;

    public PantallaServidorEsperando(ServerThread serverThread) {
        this.serverThread = serverThread;
        mensaje = new Texto(Recursos.FUENTEMENU, 50, Color.WHITE, true);
        mensaje.setTexto("Esperando jugadores... (0/4)");
        mensaje.setPosition(Config.WIDTH / 2f - mensaje.getAncho() / 2f, Config.HEIGTH / 2f);
    }

    @Override
    public void render(float delta) {
        Render.limpiarPantalla(0, 0, 0);

        if (serverThread.getConnectedClients() != conectados) {
            conectados = serverThread.getConnectedClients();
            mensaje.setTexto("Esperando jugadores... (" + conectados + "/4)");
        }

        if (conectados == 4) {
            Render.app.setScreen(new PantallaSeleccion());
        }

        Render.batch.begin();
        mensaje.dibujar();
        Render.batch.end();
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
