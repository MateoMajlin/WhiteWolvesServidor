package winterwolves.pantallas;

import com.badlogic.gdx.Screen;
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

    private final float PPM = 100f;
    private int contCajasDestruidas = 0;
    private int totalCajas;

    private Partida partida;
    private ServerThread serverThread;

    private int numPlayersConectados = 0;
    private int[] personajesElegidosIdx;

    public MapaNieve(int[] personajesElegidosIdx) {
        this.personajesElegidosIdx = personajesElegidosIdx;
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

        // Hilo servidor
        serverThread = new ServerThread(this);
        serverThread.start();

        System.out.println("Servidor iniciado, esperando jugadores...");
    }

    @Override
    public void startGame() {
        for (int i = 0; i < serverThread.getMaxClients(); i++) {
            serverThread.getClientePorId(i).setJugador(playerManager.getJugador(i));
        }

//        partida = new Partida(
//            playerManager.getJugador(0).getNombre(),
//            playerManager.getJugador(0).getPersonaje(),
//            playerManager.getJugador(1).getNombre(),
//            playerManager.getJugador(1).getPersonaje(),
//            120f
//        );

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
        if (playerManager != null) {
            playerManager.actualizar(delta);
        }

        update();

        if (world != null) {
            world.step(delta, 6, 2);
        }

        for (int i = cajas.size - 1; i >= 0; i--) {
            Caja c = cajas.get(i);
            if (c.isMarcadaParaDestruir()) {
                contCajasDestruidas++;
                c.eliminarDelMundo();
                cajas.removeIndex(i);
            }
        }

        if (partida != null) {
            partida.actualizar(delta);
        }
    }

    private void update() {
            playerManager.getJugador(1).getPersonaje().moverSegunCliente();
            playerManager.getJugador(2).getPersonaje().moverSegunCliente();
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
