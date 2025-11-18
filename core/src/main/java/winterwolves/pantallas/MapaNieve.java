package winterwolves.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
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
import winterwolves.items.*;
import winterwolves.network.Client;
import winterwolves.network.GameController;
import winterwolves.network.ServerThread;
import winterwolves.personajes.Personaje;
import winterwolves.props.*;
import winterwolves.utilidades.*;

public class MapaNieve implements Screen, GameController {

    private TiledMap mapa;
    private OrthogonalTiledMapRenderer renderer;
    private CameraManager cameraManager;
    private PlayerManager playerManager;

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Array<Caja> cajas;
    private Cofre cofre;

    private int[] capasFondo = {0, 1};
    private int[] capasDelanteras = {3};

    private final float PPM = 100f;
    private int contCajasDestruidas = 0;
    private int totalCajas;

    private Partida partida;
    private ServerThread serverThread;

    private int numPlayersConectados = 0;
    private int[] personajesElegidosIdx;

    public MapaNieve(int[] personajesElegidosIdx, ServerThread serverThread) {
        this.personajesElegidosIdx = personajesElegidosIdx;
        this.serverThread = serverThread;
    }

    @Override
    public void show() {
        // Cargar mapa
        TmxMapLoader loader = new TmxMapLoader();
        mapa = loader.load("mapas/mapaNieve.tmx");
        renderer = new OrthogonalTiledMapRenderer(mapa, 1f);

        // Cámara (solo si el servidor necesita vistas debug)
        cameraManager = new CameraManager(Config.WIDTH, Config.HEIGTH, PPM);

        // Mundo Box2D
        world = new World(new Vector2(0, 0), true);
        world.setContactListener(new CollisionListener());
        Box2DColisiones.crearCuerposColisiones(mapa, world, "Colisiones", PPM, 2f, 2f);
        debugRenderer = new Box2DDebugRenderer();

        // Crear cajas
        cajas = new Array<>();
        cajas.add(new Caja(world, 500 / PPM, 700 / PPM, PPM, 100));
        cajas.add(new Caja(world, 800 / PPM, 600 / PPM, PPM, 100));
        cajas.add(new Caja(world, 1000 / PPM, 500 / PPM, PPM, 125));
        cajas.add(new Caja(world, 1200 / PPM, 400 / PPM, PPM, 60));
        totalCajas = cajas.size;

        // Cofre con ítems
        cofre = new Cofre(world, 500 / PPM, 500 / PPM, PPM);
        cofre.getInventario().agregarItem(new EspadaItem());
        cofre.getInventario().agregarItem(new AmuletoCuracion());
        cofre.getInventario().agregarItem(new GemaElectrica());

        playerManager = new PlayerManager(world, personajesElegidosIdx, PPM, cameraManager.getHud());

        System.out.println("Servidor iniciado, esperando jugadores...");

        partida = new Partida(
            playerManager.getJugador(1).getNombre(),
            playerManager.getJugador(1).getPersonaje(),
            playerManager.getJugador(2).getNombre(),
            playerManager.getJugador(2).getPersonaje(),
            120f
        );
    }

    @Override
    public void startGame() {

        for (int i = 0; i < serverThread.getMaxClients(); i++) {
            serverThread.getClientePorId(i).setJugador(playerManager.getJugador(i));
        }

        for (int i = 0; i < serverThread.getClients().size(); i++) {
            serverThread.getClients().get(i).setJugador(playerManager.getJugador(i + 1));
        }

        System.out.println("Partida iniciada en el servidor");
    }


    @Override
    public void connect(int numPlayer) {
        this.numPlayersConectados = numPlayer;
        System.out.println("Jugador " + numPlayer + " conectado al servidor");
    }

    @Override
    public void render(float delta) {
        if (playerManager == null) return;

        Render.limpiarPantalla(1, 1, 1);
        world.step(delta, 6, 2);

        update();
        for (int i = cajas.size - 1; i >= 0; i--) {
            Caja c = cajas.get(i);
            if (c.isMarcadaParaDestruir()) {
                contCajasDestruidas++;
                c.eliminarDelMundo();
                cajas.removeIndex(i);
            }
        }

        partida.actualizar(delta);
        playerManager.actualizar(delta);
        cameraManager.seguir(playerManager.getPosicionJugador(0));

        renderer.setView(cameraManager.getPrincipal());
        renderer.render(capasFondo);

        Render.batch.setProjectionMatrix(cameraManager.getPrincipal().combined);
        Render.batch.begin();
        playerManager.draw(Render.batch);
        for (Caja c : cajas) {
            c.actualizar(delta);
            c.draw(Render.batch);
            c.drawVidaTexto(Render.batch);
        }
//        if (contCajasDestruidas == totalCajas) ganaste.dibujar();
        cofre.draw(Render.batch);
        Render.batch.end();

        renderer.render(capasDelanteras);


        Render.batch.setProjectionMatrix(cameraManager.getHud().combined);
        Render.batch.begin();
        partida.dibujarHUD();
        Render.batch.end();

        debugRenderer.render(world, cameraManager.getBox2D().combined);

    }

    private void update() {
            Jugador p1 = playerManager.getJugador(1);
            Jugador p2 = playerManager.getJugador(2);
            p1.getPersonaje().moverSegunCliente();
            p2.getPersonaje().moverSegunCliente();
            if(p1.getPersonaje().getMensajeJugador() != null) {
                serverThread.sendMessageToAll(p1.getPersonaje().enviarPosicion());

            }
            if(p2.getPersonaje().getMensajeJugador() != null) {
            serverThread.sendMessageToAll(p2.getPersonaje().enviarPosicion());
            }
    }


    @Override public void resize(int width, int height) { cameraManager.resize(width, height); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        if (mapa != null) mapa.dispose();
        if (renderer != null) renderer.dispose();
        if (playerManager != null) playerManager.dispose();
        if (world != null) world.dispose();
        if (debugRenderer != null) debugRenderer.dispose();
        if (serverThread != null) serverThread.terminate();
        for (Caja c : cajas) c.dispose();
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

}
