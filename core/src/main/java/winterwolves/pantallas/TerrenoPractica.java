package winterwolves.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import winterwolves.elementos.Texto;
import winterwolves.io.EntradasJugador;
import winterwolves.personajes.Guerrero;
import winterwolves.personajes.HudGuerrero;
import winterwolves.props.Caja;
import winterwolves.utilidades.*;

public class TerrenoPractica implements Screen {

    private TiledMap mapa;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camara;
    private OrthographicCamera camaraBox2D;
    private Music musica = Recursos.musicaBatalla;

    int[] capasFondo = {0, 1};
    int[] capasDelanteras = {3};

    private World world;
    private Box2DDebugRenderer debugRenderer;

    private Guerrero guerrero;
    private HudGuerrero hud;
    private OrthographicCamera camaraHud;
    private Array<Caja> cajas;

    private final float PPM = 100f;

    int contCajasDestruidas = 0;
    int totalCajas;
    Texto ganaste;

    @Override
    public void show() {
        setearMusica();

        TmxMapLoader loader = new TmxMapLoader();
        mapa = loader.load("mapas/mapaNieve.tmx");

        int mapWidth = mapa.getProperties().get("width", Integer.class)
            * mapa.getProperties().get("tilewidth", Integer.class);

        int mapHeight = mapa.getProperties().get("height", Integer.class)
            * mapa.getProperties().get("tileheight", Integer.class);

        float centroMapaX = mapWidth / 2f;
        float centroMapaY = mapHeight / 2f;

        renderer = new OrthogonalTiledMapRenderer(mapa, 1f);

        // Cámara para sprites (pixeles)
        camara = new OrthographicCamera();
        camara.setToOrtho(false, Config.WIDTH, Config.HEIGTH);
        camara.position.set(Config.WIDTH / 2f, Config.HEIGTH / 2f, 0);
        camara.update();

        // Cámara para Box2D (metros)
        camaraBox2D = new OrthographicCamera();
        camaraBox2D.setToOrtho(false, Config.WIDTH / PPM, Config.HEIGTH / PPM);
        camaraBox2D.position.set((Config.WIDTH / 2f) / PPM, (Config.HEIGTH / 2f) / PPM, 0);
        camaraBox2D.update();

        // Mundo Box2D
        world = new World(new Vector2(0, 0), true);
        world.setContactListener(new CollisionListener());
        Box2DColisiones.crearCuerposColisiones(mapa, world, "Colisiones", PPM);

        debugRenderer = new Box2DDebugRenderer();

        EntradasJugador entradas = new EntradasJugador();
        guerrero = new Guerrero(world, entradas, 450 / PPM, 450 / PPM, PPM);

        camaraHud = new OrthographicCamera();
        camaraHud.setToOrtho(false, Config.WIDTH, Config.HEIGTH);
        camaraHud.position.set(Config.WIDTH / 2f, Config.HEIGTH / 2f, 0);
        camaraHud.update();

        hud = new HudGuerrero(guerrero, camaraHud);

        cajas = new Array<>();
        cajas.add(new Caja(world, 500 / PPM, 700 / PPM, PPM));
        cajas.add(new Caja(world, 800 / PPM, 600 / PPM, PPM));
        cajas.add(new Caja(world, 1000 / PPM, 500 / PPM, PPM));
        cajas.add(new Caja(world, 1200 / PPM, 400 / PPM, PPM));

        totalCajas = cajas.size;

        Gdx.input.setInputProcessor(entradas);

        ganaste = new Texto(Recursos.FUENTEMENU, 150, Color.BLACK, true);
        ganaste.setTexto("Ganaste");
        ganaste.setPosition(centroMapaX - ganaste.getAncho() / 2f,
            centroMapaY + ganaste.getAlto() / 2f);
    }

    @Override
    public void render(float delta) {
        Render.limpiarPantalla(1, 1, 1);

        world.step(delta, 6, 2);

        for (int i = cajas.size - 1; i >= 0; i--) {
            Caja c = cajas.get(i);
            if (c.isMarcadaParaDestruir()) {
                contCajasDestruidas++;
                c.eliminarDelMundo();
                cajas.removeIndex(i);
            }
        }

        // Cámara de sprites sigue al guerrero
        camara.position.set(guerrero.getX() + guerrero.getWidth() / 2, guerrero.getY() + guerrero.getHeight() / 2, 0);
        camara.viewportHeight = 550;
        camara.viewportWidth = 550;
        camara.update();

        // Cámara Box2D sigue la misma posición (en metros)
        camaraBox2D.position.set(camara.position.x / PPM, camara.position.y / PPM, 0);
        camaraBox2D.update();

        renderer.setView(camara);
        renderer.render(capasFondo);

        Render.batch.setProjectionMatrix(camara.combined);
        Render.batch.begin();
        guerrero.draw(Render.batch);

        for (Caja c : cajas) {
            c.draw(Render.batch);
        }

        if (contCajasDestruidas == totalCajas) {
            ganaste.dibujar();
        }

        Render.batch.end();

        renderer.render(capasDelanteras);
        hud.render(Render.batch);

        // 🔥 Dibujar las hitbox Box2D
        debugRenderer.render(world, camaraBox2D.combined);

        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
            Render.app.setScreen(new Menu());
            musica.stop();
            Recursos.musica.play();
            Recursos.musica.setVolume(0.3f);
            Recursos.musica.setLooping(true);
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        camara.viewportWidth = width;
        camara.viewportHeight = height;
        camara.update();

        camaraBox2D.viewportWidth = width / PPM;
        camaraBox2D.viewportHeight = height / PPM;
        camaraBox2D.update();
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        mapa.dispose();
        renderer.dispose();
        guerrero.dispose();
        world.dispose();
        debugRenderer.dispose();
        for (Caja c : cajas) {
            c.dispose();
        }
    }

    private void setearMusica() {
        musica.play();
        musica.setLooping(true);
        musica.setVolume(0.2f);
    }

    public Guerrero getGuerrero() {
        return guerrero;
    }
}
