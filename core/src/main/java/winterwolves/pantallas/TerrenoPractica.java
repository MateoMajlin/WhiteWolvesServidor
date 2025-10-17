package winterwolves.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
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

import winterwolves.Jugador;
import winterwolves.Partida;
import winterwolves.elementos.Texto;
import winterwolves.io.EntradasJugador;
import winterwolves.io.EntradasJugador2;
import winterwolves.items.*;
import winterwolves.personajes.Personaje;
import winterwolves.personajes.clases.Clerigo;
import winterwolves.personajes.clases.Guerrero;
import winterwolves.personajes.clases.Mago;
import winterwolves.props.Caja;
import winterwolves.props.Cofre;
import winterwolves.props.CofreHud;
import winterwolves.utilidades.*;

public class TerrenoPractica implements Screen {

    private TiledMap mapa;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camara;
    private OrthographicCamera camaraBox2D;
    private OrthographicCamera camaraHud;
    private Music musica = Recursos.musicaBatalla;

    int[] capasFondo = {0, 1};
    int[] capasDelanteras = {3};

    private World world;
    private Box2DDebugRenderer debugRenderer;

    private Array<Caja> cajas;
    private Cofre cofre;
    private CofreHud hudCofre;

    private final float PPM = 100f;

    int contCajasDestruidas = 0;
    int totalCajas;
    private Texto ganaste;

    private Jugador jugador, dummy;
    private Partida partida;

    public PantallaSeleccion pantallaSeleccion;
    private int[] personajesElegidosIdx;

    public TerrenoPractica(int[] personajesElegidosIdx) {
        this.personajesElegidosIdx = personajesElegidosIdx;
    }

    @Override
    public void show() {
        // Cargar mapa
        TmxMapLoader loader = new TmxMapLoader();
        mapa = loader.load("mapas/mapaNieve.tmx");

        int mapWidth = mapa.getProperties().get("width", Integer.class)
            * mapa.getProperties().get("tilewidth", Integer.class);
        int mapHeight = mapa.getProperties().get("height", Integer.class)
            * mapa.getProperties().get("tileheight", Integer.class);

        float centroMapaX = mapWidth / 2f;
        float centroMapaY = mapHeight / 2f;

        renderer = new OrthogonalTiledMapRenderer(mapa, 1f);

        camara = new OrthographicCamera();
        camara.setToOrtho(false, Config.WIDTH, Config.HEIGTH);
        camara.position.set(Config.WIDTH / 2f, Config.HEIGTH / 2f, 0);
        camara.update();

        camaraBox2D = new OrthographicCamera();
        camaraBox2D.setToOrtho(false, Config.WIDTH / PPM, Config.HEIGTH / PPM);
        camaraBox2D.position.set((Config.WIDTH / 2f) / PPM, (Config.HEIGTH / 2f) / PPM, 0);
        camaraBox2D.update();

        camaraHud = new OrthographicCamera();
        camaraHud.setToOrtho(false, Config.WIDTH, Config.HEIGTH);
        camaraHud.position.set(Config.WIDTH / 2f, Config.HEIGTH / 2f, 0);
        camaraHud.update();

        world = new World(new Vector2(0, 0), true);
        world.setContactListener(new CollisionListener());
        Box2DColisiones.crearCuerposColisiones(mapa, world, "Colisiones", PPM, 2f, 2f);

        debugRenderer = new Box2DDebugRenderer();

        EntradasJugador entradas1 = new EntradasJugador();
        EntradasJugador2 entradas2 = new EntradasJugador2();
        EntradasJugador[] entradasJugadores = new EntradasJugador[]{entradas1, entradas2};

        Personaje[] personajes = aplicarPersonajesJugadores(world, entradasJugadores, personajesElegidosIdx, PPM, camaraHud);

        jugador = new Jugador("Mateo", world, entradas1, 450 / PPM, 450 / PPM, PPM, camaraHud, personajes[0]);
        dummy = new Jugador("Dummy", world, entradas2, 650 / PPM, 650 / PPM, PPM, camaraHud, personajes[1]);

        // Crear partida (duración 60 segundos)
        partida = new Partida(jugador.getNombre(), jugador.getPersonaje(),
            dummy.getNombre(), dummy.getPersonaje(),
            60f);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(entradas1);
        multiplexer.addProcessor(entradas2);
        Gdx.input.setInputProcessor(multiplexer);

        jugador.getPersonaje().setVida(50);

        // Cajas
        cajas = new Array<>();
        cajas.add(new Caja(world, 500 / PPM, 700 / PPM, PPM, 100));
        cajas.add(new Caja(world, 800 / PPM, 600 / PPM, PPM, 100));
        cajas.add(new Caja(world, 1000 / PPM, 500 / PPM, PPM, 125));
        cajas.add(new Caja(world, 1200 / PPM, 400 / PPM, PPM, 60));
        totalCajas = cajas.size;

        // Cofre
        cofre = new Cofre(world, 300 / PPM, 200 / PPM, PPM);
        cofre.getInventario().agregarItem(new EspadaItem());
        cofre.getInventario().agregarItem(new AmuletoCuracion());
        cofre.getInventario().agregarItem(new GemaElectrica());

        // Texto "Ganaste"
        ganaste = new Texto(Recursos.FUENTEMENU, 150, Color.BLACK, true);
        ganaste.setTexto("Ganaste");
        ganaste.setPosition(centroMapaX - ganaste.getAncho() / 2f,
            centroMapaY + ganaste.getAlto() / 2f);
    }

    private Personaje[] aplicarPersonajesJugadores(World world, EntradasJugador[] entradasJugadores,
                                                   int[] personajesElegidosIdx, float ppm, OrthographicCamera camaraHud) {
        Personaje[] personajes = new Personaje[2];

        float[] posicionesX = {2f, 10f};
        float[] posicionesY = {3f, 5f};

        for (int i = 0; i < 2; i++) {
            switch (personajesElegidosIdx[i]) {
                case 0:
                    personajes[i] = new Guerrero(world, entradasJugadores[i], posicionesX[i], posicionesY[i], ppm, camaraHud);
                    break;
                case 1:
                    personajes[i] = new Mago(world, entradasJugadores[i], posicionesX[i], posicionesY[i], ppm, camaraHud);
                    break;
                case 2:
                    personajes[i] = new Clerigo(world, entradasJugadores[i], posicionesX[i], posicionesY[i], ppm, camaraHud);
                    break;
            }
        }
        return personajes;
    }

    @Override
    public void render(float delta) {
        Render.limpiarPantalla(1, 1, 1);
        world.step(delta, 6, 2);

        Personaje pj1 = jugador.getPersonaje();
        Personaje pj2 = dummy.getPersonaje();

        // Actualizar cajas
        for (int i = cajas.size - 1; i >= 0; i--) {
            Caja c = cajas.get(i);
            if (c.isMarcadaParaDestruir()) {
                contCajasDestruidas++;
                c.eliminarDelMundo();
                cajas.removeIndex(i);
            }
        }

        // Actualizar partida (tiempo, kills, etc.)
        partida.actualizar(delta);

        // Actualizar cámara
        camara.position.set(pj1.getX() + pj1.getWidth() / 2, pj1.getY() + pj1.getHeight() / 2, 0);
        camara.update();
        camaraBox2D.position.set(camara.position.x / PPM, camara.position.y / PPM, 0);
        camaraBox2D.update();

        // Renderizar mapa
        renderer.setView(camara);
        renderer.render(capasFondo);

        // Dibujar entidades
        Render.batch.setProjectionMatrix(camara.combined);
        Render.batch.begin();
        pj1.draw(Render.batch);
        pj2.draw(Render.batch);
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
            if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
                pj1.intercambiarItems(new AmuletoCuracion(), 1);
            }

            if (cofre.estaCerca(new Vector2(pj1.getX(), pj1.getY()), 50)
                && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                if (hudCofre == null)
                    hudCofre = new CofreHud(cofre.getInventario(), pj1, camaraHud);
                hudCofre.toggle();
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.I) && (hudCofre == null || !hudCofre.isVisible())) {
                jugador.toggleInventario();
            }
        }

        pj1.actualizarInventario();

        if (hudCofre != null && hudCofre.isVisible()) {
            hudCofre.actualizar();
            hudCofre.dibujar(Render.batch);
        } else {
            jugador.drawHud(Render.batch);
        }

        // HUD de la partida
        Render.batch.setProjectionMatrix(camaraHud.combined);
        Render.batch.begin();
        partida.dibujarHUD();
        Render.batch.end();

        // Debug
        debugRenderer.render(world, camaraBox2D.combined);

        // Salir al menú
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Render.app.setScreen(new Menu());
            musica.stop();
            Recursos.musica.play();
            Recursos.musica.setVolume(0.3f);
            Recursos.musica.setLooping(true);
            dispose();
        }

        // Bloquear movimiento si terminó la partida
        if (partida.isPartidaFinalizada()) {
            pj1.setPuedeMoverse(false);
            pj2.setPuedeMoverse(false);
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
        jugador.dispose();
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
}
