package winterwolves.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import winterwolves.io.EntradasJugador;
import winterwolves.personajes.Jugador;
import winterwolves.utilidades.Config;
import winterwolves.utilidades.Render;

public class TerrenoPractica implements Screen {

    private TiledMap mapa;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camara;

    int[] capasFondo = {0,1};
    int[] capasDelanteras = {2};

    private Jugador jugador;

    @Override
    public void show() {
        TmxMapLoader loader = new TmxMapLoader();
        mapa = loader.load("mapas/mapaNieve.tmx");

        renderer = new OrthogonalTiledMapRenderer(mapa);

        camara = new OrthographicCamera();
        camara.setToOrtho(false, Config.WIDTH, Config.HEIGTH);
        camara.position.set(Config.WIDTH / 2f, Config.HEIGTH / 2f, 0);
        camara.update();

        EntradasJugador entradas = new EntradasJugador();
        jugador = new Jugador(new Sprite(new Texture("guerrero.png")), (TiledMapTileLayer) mapa.getLayers().get(1),entradas);
        jugador.setPosition(450, 450);
        jugador.setSize(30f,30f);

        Gdx.input.setInputProcessor(entradas);
    }

    @Override
    public void render(float delta) {
        Render.limpiarPantalla(1, 1, 1);

        camara.position.set(jugador.getX() + jugador.getWidth() / 2, jugador.getY() + jugador.getHeight() / 2, 0); // esto es para que la camara siga al jugador
        camara.update();

        renderer.setView(camara); // esto setea la carga del mapa a lo que mira la camara
        renderer.render(capasFondo);

        Render.batch.setProjectionMatrix(camara.combined);
        Render.batch.begin();
        jugador.draw(Render.batch);
        Render.batch.end();

        renderer.render(capasDelanteras);
    }

    @Override
    public void resize(int width, int height) {
        camara.viewportWidth = width;
        camara.viewportHeight = height;
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        mapa.dispose();
        renderer.dispose();
        jugador.getTexture().dispose();
    }
}
