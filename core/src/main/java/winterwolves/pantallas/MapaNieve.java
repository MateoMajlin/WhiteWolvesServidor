package winterwolves.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import winterwolves.Partida;
import winterwolves.elementos.Texto;
import winterwolves.items.AmuletoCuracion;
import winterwolves.items.EspadaItem;
import winterwolves.items.GemaElectrica;
import winterwolves.props.Caja;
import winterwolves.props.Cofre;
import winterwolves.props.CofreHud;
import winterwolves.utilidades.CameraManager;
import winterwolves.utilidades.Config;
import winterwolves.utilidades.PlayerManager;
import winterwolves.utilidades.Render;
import winterwolves.utilidades.Recursos;
import winterwolves.utilidades.Box2DColisiones;
import winterwolves.utilidades.CollisionListener;

public class MapaNieve implements Screen {

    private TiledMap mapa;
    private OrthogonalTiledMapRenderer renderer;
    private CameraManager cameraManager;
    private PlayerManager playerManager;
    private Music musica = Recursos.musicaBatalla;

    private int[] capasFondo = {0, 1};
    private int[] capasDelanteras = {3};

    private World world;
    private Box2DDebugRenderer debugRenderer;

    private Array<Caja> cajas;
    private Cofre cofre;
    private CofreHud hudCofre;

    private final float PPM = 100f;

    private int contCajasDestruidas = 0;
    private int totalCajas;
    private Texto ganaste;

    private Partida partida;
    private int[] personajesElegidosIdx;

    public MapaNieve(int[] personajesElegidosIdx) {
        this.personajesElegidosIdx = personajesElegidosIdx;
    }

    @Override
    public void show() {
        TmxMapLoader loader = new TmxMapLoader();
        mapa = loader.load("mapas/mapaNieve.tmx");

        int mapWidth = mapa.getProperties().get("width", Integer.class)
            * mapa.getProperties().get("tilewidth", Integer.class);
        int mapHeight = mapa.getProperties().get("height", Integer.class)
            * mapa.getProperties().get("tileheight", Integer.class);

        float centroMapaX = mapWidth / 2f;
        float centroMapaY = mapHeight / 2f;

        renderer = new OrthogonalTiledMapRenderer(mapa, 1f);

        cameraManager = new CameraManager(Config.WIDTH, Config.HEIGTH, PPM);

        world = new World(new Vector2(0, 0), true);
        world.setContactListener(new CollisionListener());
        Box2DColisiones.crearCuerposColisiones(mapa, world, "Colisiones", PPM, 2f, 2f);
        debugRenderer = new Box2DDebugRenderer();

        playerManager = new PlayerManager(world, personajesElegidosIdx, PPM, cameraManager.getHud());
        playerManager.getJugador1().getPersonaje().setVida(50);

        partida = new Partida(playerManager.getJugador1().getNombre(), playerManager.getJugador1().getPersonaje(),
            playerManager.getJugador2().getNombre(), playerManager.getJugador2().getPersonaje(),
            60f);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(playerManager.getJugador1().getEntradas());
        multiplexer.addProcessor(playerManager.getJugador2().getEntradas());
        Gdx.input.setInputProcessor(multiplexer);

        cajas = new Array<>();
        cajas.add(new Caja(world, 500 / PPM, 700 / PPM, PPM, 100));
        cajas.add(new Caja(world, 800 / PPM, 600 / PPM, PPM, 100));
        cajas.add(new Caja(world, 1000 / PPM, 500 / PPM, PPM, 125));
        cajas.add(new Caja(world, 1200 / PPM, 400 / PPM, PPM, 60));
        totalCajas = cajas.size;

        cofre = new Cofre(world, 500 / PPM, 500 / PPM, PPM);
        cofre.getInventario().agregarItem(new EspadaItem());
        cofre.getInventario().agregarItem(new AmuletoCuracion());
        cofre.getInventario().agregarItem(new GemaElectrica());

        // Texto "Ganaste"
        ganaste = new Texto(Recursos.FUENTEMENU, 150, Color.BLACK, true);
        ganaste.setTexto("Ganaste");
        ganaste.setPosition(centroMapaX - ganaste.getAncho() / 2f,
            centroMapaY + ganaste.getAlto() / 2f);
    }

    @Override
    public void render(float delta) {
        Render.limpiarPantalla(1, 1, 1);
        world.step(delta, 6, 2);

        // Actualizar cajas
        for (int i = cajas.size - 1; i >= 0; i--) {
            Caja c = cajas.get(i);
            if (c.isMarcadaParaDestruir()) {
                contCajasDestruidas++;
                c.eliminarDelMundo();
                cajas.removeIndex(i);
            }
        }

        // Actualizar partida
        partida.actualizar(delta);

        System.out.println(playerManager.getJugador2().getPersonaje().getVida());
        System.out.println(playerManager.getJugador2().getPersonaje().getVidaMax());
        // Actualizar jugadores
        playerManager.actualizar(delta);

        // Actualizar cámara
        cameraManager.seguir(playerManager.getPosicionJugador1());

        // Renderizar mapa
        renderer.setView(cameraManager.getPrincipal());
        renderer.render(capasFondo);

        // Dibujar jugadores y cajas
        Render.batch.setProjectionMatrix(cameraManager.getPrincipal().combined);
        Render.batch.begin();
        playerManager.draw(Render.batch);
        for (Caja c : cajas) {
            c.actualizar(delta);
            c.draw(Render.batch);
            c.drawVidaTexto(Render.batch);
        }
        if (contCajasDestruidas == totalCajas) ganaste.dibujar();
        cofre.draw(Render.batch);
        Render.batch.end();

        renderer.render(capasDelanteras);

        // Interacciones
        if (!partida.isPartidaFinalizada()) {

            // Intercambiar items
            if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
                playerManager.getJugador1().getPersonaje().intercambiarItems(new AmuletoCuracion(), 1);
            }

            // Abrir cofre
            if (cofre.estaCerca(playerManager.getPosicionJugador1(), 50)
                && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                if (hudCofre == null)
                    hudCofre = new CofreHud(cofre.getInventario(), playerManager.getJugador1().getPersonaje(), cameraManager.getHud());
                hudCofre.toggle();
            }

            // Abrir inventario
            if (Gdx.input.isKeyJustPressed(Input.Keys.I) && (hudCofre == null || !hudCofre.isVisible())) {
                playerManager.getJugador1().toggleInventario();
            }
        }

        // Dibujar HUD
        if (hudCofre != null && hudCofre.isVisible()) {
            hudCofre.actualizar();
            hudCofre.dibujar(Render.batch);
        } else {
            playerManager.drawHud(Render.batch);
        }

        // HUD partida
        Render.batch.setProjectionMatrix(cameraManager.getHud().combined);
        Render.batch.begin();
        partida.dibujarHUD();
        Render.batch.end();

        debugRenderer.render(world, cameraManager.getBox2D().combined);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Render.app.setScreen(new Menu());
            musica.stop();
            Recursos.musica.play();
            Recursos.musica.setVolume(0.3f);
            Recursos.musica.setLooping(true);
            dispose();
        }

        if (partida.isPartidaFinalizada()) {
            playerManager.setPuedeMoverse(false);
        }
    }

    @Override
    public void resize(int width, int height) {
        cameraManager.resize(width, height);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        mapa.dispose();
        renderer.dispose();
        playerManager.dispose();
        world.dispose();
        debugRenderer.dispose();
        for (Caja c : cajas) {
            c.dispose();
        }
    }
}
