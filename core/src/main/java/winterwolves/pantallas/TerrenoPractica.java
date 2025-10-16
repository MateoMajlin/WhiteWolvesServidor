package winterwolves.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import winterwolves.elementos.Texto;
import winterwolves.io.EntradasJugador;
import winterwolves.items.*;
import winterwolves.personajes.Hud;
import winterwolves.personajes.InventarioHud;
import winterwolves.personajes.Personaje;
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
    Texto ganaste;

    private Jugador jugador;

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

        // Crear mundo y colisiones
        world = new World(new Vector2(0, 0), true);
        world.setContactListener(new CollisionListener());
        Box2DColisiones.crearCuerposColisiones(mapa, world, "Colisiones", PPM, 2f, 2f);

        debugRenderer = new Box2DDebugRenderer();

        EntradasJugador entradas = new EntradasJugador();
        jugador = new Jugador("Mateo", world, entradas, 450 / PPM, 450 / PPM, PPM, camaraHud);

        Gdx.input.setInputProcessor(entradas);

        // Items iniciales
        Personaje p = jugador.getPersonaje();
        EspadaItem espada = new EspadaItem();
        GemaDeFuego gema = new GemaDeFuego(5f, 5f, 60);
        GemaElectrica gemaElectrica = new GemaElectrica(5f, 10f, 80);

        p.getInventario().agregarItem(espada);
        p.getInventario().agregarItem(gema);
        p.getInventario().agregarItem(gemaElectrica);

        p.equiparArma(espada);
        p.equiparItem1(gema);
        p.equiparItem2(gemaElectrica);

        p.setVida(50);

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
        cofre.getInventario().agregarItem(new AmuletoCuracion(5f, 5f, 60));
        cofre.getInventario().agregarItem(new GemaElectrica(5f, 10f, 80));

        // Texto de victoria
        ganaste = new Texto(Recursos.FUENTEMENU, 150, Color.BLACK, true);
        ganaste.setTexto("Ganaste");
        ganaste.setPosition(centroMapaX - ganaste.getAncho() / 2f,
            centroMapaY + ganaste.getAlto() / 2f);
    }

    @Override
    public void render(float delta) {
        Render.limpiarPantalla(1, 1, 1);

        Personaje p = jugador.getPersonaje();
        world.step(delta, 6, 2);

        // Manejo de cajas
        for (int i = cajas.size - 1; i >= 0; i--) {
            Caja c = cajas.get(i);
            if (c.isMarcadaParaDestruir()) {
                contCajasDestruidas++;
                c.eliminarDelMundo();
                cajas.removeIndex(i);
            }
        }

        // Debug Inventario
        if(Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            System.out.println("=== Estado del personaje ===");
            System.out.println("Inventario:");
            for (int i = 0; i < p.getInventario().getItems().size(); i++) {
                Item item = p.getInventario().getItem(i);
                System.out.println(" - Item " + i + ": " + (item != null ? item.getNombre() : "VACÍO"));
            }
            System.out.println("Slots:");
            for (int slot = 0; slot < 3; slot++) {
                Item itemSlot = p.getSlot(slot);
                System.out.println(" - Slot " + slot + ": " + (itemSlot != null ? itemSlot.getNombre() : "VACÍO"));
            }
        }

        // Cámara
        camara.position.set(p.getX() + p.getWidth() / 2, p.getY() + p.getHeight() / 2, 0);
        camara.update();
        camaraBox2D.position.set(camara.position.x / PPM, camara.position.y / PPM, 0);
        camaraBox2D.update();

        // Render fondo
        renderer.setView(camara);
        renderer.render(capasFondo);

        // Render personajes y objetos
        Render.batch.setProjectionMatrix(camara.combined);
        Render.batch.begin();
        p.draw(Render.batch);
        for (Caja c : cajas) {
            c.actualizar(delta);
            c.draw(Render.batch);
            c.drawVidaTexto(Render.batch);
        }
        if (contCajasDestruidas == totalCajas) {
            ganaste.dibujar();
        }
        cofre.draw(Render.batch);
        Render.batch.end();

        // Render capas delanteras
        renderer.render(capasDelanteras);

        // Interacciones
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            p.intercambiarItems(new AmuletoCuracion(2f,5f,30),1);
        }

        if (cofre.estaCerca(new Vector2(p.getX(), p.getY()), 50) && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            if (hudCofre == null) {
                hudCofre = new CofreHud(cofre.getInventario(), p, camaraHud);
            }
            hudCofre.toggle();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            jugador.toggleInventario();
        }

        p.actualizarInventario();

        // HUD e Inventario
        if (hudCofre != null && hudCofre.isVisible()) {
            hudCofre.actualizar();
            hudCofre.dibujar(Render.batch);
        } else {
            jugador.drawHud(Render.batch);
        }

        // Debug Box2D
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
