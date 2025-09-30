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
import winterwolves.items.GemaDeFuego;
import winterwolves.personajes.InventarioHud;
import winterwolves.io.EntradasJugador;
import winterwolves.items.AmuletoCuracion;
import winterwolves.items.AnilloConcentracion;
import winterwolves.items.EspadaItem;
import winterwolves.personajes.Hud;
import winterwolves.personajes.clases.Mago;
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

    private Mago mago;
    private InventarioHud inventarioHud;

    private Hud hud;
    private OrthographicCamera camaraHud;
    private Array<Caja> cajas;

    private final float PPM = 100f;

    int contCajasDestruidas = 0;
    int totalCajas;
    Texto ganaste;

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

        camara = new OrthographicCamera();
        camara.setToOrtho(false, Config.WIDTH, Config.HEIGTH);
        camara.position.set(Config.WIDTH / 2f, Config.HEIGTH / 2f, 0);
        camara.update();

        camaraBox2D = new OrthographicCamera();
        camaraBox2D.setToOrtho(false, Config.WIDTH / PPM, Config.HEIGTH / PPM);
        camaraBox2D.position.set((Config.WIDTH / 2f) / PPM, (Config.HEIGTH / 2f) / PPM, 0);
        camaraBox2D.update();

        world = new World(new Vector2(0, 0), true);
        world.setContactListener(new CollisionListener());

        Box2DColisiones.crearCuerposColisiones(mapa, world, "Colisiones", PPM, 2f, 2f);

        debugRenderer = new Box2DDebugRenderer();

        EntradasJugador entradas = new EntradasJugador();

        mago = new Mago(world, entradas, 450 / PPM, 450 / PPM, PPM);

        EspadaItem espadaItem = new EspadaItem();
        GemaDeFuego gema = new GemaDeFuego(3f,2f,20);
        AnilloConcentracion anillo = new AnilloConcentracion(10f, 15f, 2f, 20f);

        mago.getInventario().agregarItem(espadaItem);
        mago.getInventario().agregarItem(gema);
        mago.getInventario().agregarItem(anillo);

        mago.equiparArma(espadaItem);
        mago.equiparItem1(gema);
        mago.equiparItem2(anillo);

        mago.setVida(50);

        camaraHud = new OrthographicCamera();
        camaraHud.setToOrtho(false, Config.WIDTH, Config.HEIGTH);
        camaraHud.position.set(Config.WIDTH / 2f, Config.HEIGTH / 2f, 0);
        camaraHud.update();

        inventarioHud = new InventarioHud(mago.getInventario(), camaraHud);
        hud = new Hud(mago, camaraHud);

        cajas = new Array<>();
        cajas.add(new Caja(world, 500 / PPM, 700 / PPM, PPM,100));
        cajas.add(new Caja(world, 800 / PPM, 600 / PPM, PPM,100));
        cajas.add(new Caja(world, 1000 / PPM, 500 / PPM, PPM,125));
        cajas.add(new Caja(world, 1200 / PPM, 400 / PPM, PPM,60));
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

        // Actualizar cajas
        for (int i = cajas.size - 1; i >= 0; i--) {
            Caja c = cajas.get(i);
            if (c.isMarcadaParaDestruir()) {
                contCajasDestruidas++;
                c.eliminarDelMundo();
                cajas.removeIndex(i);
            }
        }

        // Seguir al mago
        camara.position.set(
            mago.getX() + mago.getWidth() / 2,
            mago.getY() + mago.getHeight() / 2,
            0
        );
        camara.update();

        camaraBox2D.position.set(camara.position.x / PPM, camara.position.y / PPM, 0);
        camaraBox2D.update();

        // Fondo
        renderer.setView(camara);
        renderer.render(capasFondo);

        // Dibujar mundo
        Render.batch.setProjectionMatrix(camara.combined);
        Render.batch.begin();
        mago.draw(Render.batch);
        for (Caja c : cajas) {
            c.actualizar(delta);
            c.draw(Render.batch);
            c.drawVidaTexto(Render.batch);
        }
        if (contCajasDestruidas == totalCajas) {
            ganaste.dibujar();
        }
        Render.batch.end();

        // Capas delanteras
        renderer.render(capasDelanteras);

        // Toggle inventario
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.I)) {
            inventarioHud.toggle();
            if(mago.getPuedeMoverse() == false) {
                mago.setPuedeMoverse(true);
            }
            else if(mago.getPuedeMoverse() == true){
                mago.setPuedeMoverse(false);
            }
        }


        // Actualizar inventario
        inventarioHud.actualizar();

        // Dibujar HUD o Inventario (mutuamente excluyentes)
        if (inventarioHud.isVisible()) {
            Render.batch.setProjectionMatrix(camaraHud.combined);
            inventarioHud.dibujar(Render.batch,mago);
        } else {
            Render.batch.setProjectionMatrix(camaraHud.combined);
            hud.render(Render.batch);
        }

        debugRenderer.render(world, camaraBox2D.combined);

        // ESC para volver al menú
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
        mago.dispose();
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
