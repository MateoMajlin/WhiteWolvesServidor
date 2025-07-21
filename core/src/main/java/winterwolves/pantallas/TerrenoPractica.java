package winterwolves.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import winterwolves.io.EntradasJugador;
import winterwolves.personajes.Jugador;
import winterwolves.props.Caja;
import winterwolves.utilidades.Box2DColisiones;
import winterwolves.utilidades.Config;
import winterwolves.utilidades.Recursos;
import winterwolves.utilidades.Render;
import winterwolves.personajes.HudJugador;

public class TerrenoPractica implements Screen {

    private TiledMap mapa;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camara;
    private Music musica = Recursos.musicaBatalla;

    int[] capasFondo = {0, 1};
    int[] capasDelanteras = {3};

    private World world;
    private Box2DDebugRenderer debugRenderer; //no se como usarlo pero no pero nada poniendolo

    private Jugador jugador;
    private HudJugador hud;
    private OrthographicCamera camaraHud;
    private Caja caja;

    private final float PPM = 100f; // esto sirve para escalar los pixeles con los metros que es la unidad de medida que usa box2D

    @Override
    public void show() {

        setearMusica();

        TmxMapLoader loader = new TmxMapLoader();
        mapa = loader.load("mapas/mapaNieve.tmx");

        renderer = new OrthogonalTiledMapRenderer(mapa, 1f);

        camara = new OrthographicCamera();
        camara.setToOrtho(false, Config.WIDTH, Config.HEIGTH);
        camara.position.set(Config.WIDTH / 2f, Config.HEIGTH / 2f, 0);
        camara.update();

        world = new World(new com.badlogic.gdx.math.Vector2(0, 0), true);
        Box2DColisiones.crearCuerposColisiones(mapa, world, "Colisiones", PPM);
        debugRenderer = new Box2DDebugRenderer();

        EntradasJugador entradas = new EntradasJugador();

        jugador = new Jugador(world, entradas, 450 / PPM, 450 / PPM, PPM);

        camaraHud = new OrthographicCamera();
        camaraHud.setToOrtho(false, Config.WIDTH, Config.HEIGTH);
        camaraHud.position.set(Config.WIDTH / 2f, Config.HEIGTH / 2f, 0);
        camaraHud.update();

        hud = new HudJugador(jugador, camaraHud);

        caja = new Caja(world, 500 / PPM, 500 / PPM, PPM);

        Gdx.input.setInputProcessor(entradas);
    }

    @Override
    public void render(float delta) {
        Render.limpiarPantalla(1, 1, 1);

        world.step(delta, 6, 2); // esto hay que usarlo qsy

        camara.position.set(jugador.getX() + jugador.getWidth() / 2, jugador.getY() + jugador.getHeight() / 2, 0);
        camara.update();

        renderer.setView(camara);
        renderer.render(capasFondo);

        Render.batch.setProjectionMatrix(camara.combined);
        Render.batch.begin();
        jugador.draw(Render.batch);
        caja.draw(Render.batch);
        Render.batch.end();

        renderer.render(capasDelanteras);

        hud.render(Render.batch);

        debugRenderer.render(world, camara.combined.scl(1 / PPM));
        camara.combined.scl(PPM); //esto reescala la camara
    }

    @Override
    public void resize(int width, int height) {
        camara.viewportWidth = width;
        camara.viewportHeight = height;
        camara.update();
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
        jugador.dispose();
        world.dispose();
        debugRenderer.dispose();
    }

    private void setearMusica() {
        musica.play();
        musica.setLooping(true);
        musica.setVolume(0.2f);
    }

    public Jugador getJugador(){
        return jugador;
    }
}
