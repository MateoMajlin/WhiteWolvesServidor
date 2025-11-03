package winterwolves.pantallas;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import winterwolves.Partida;
import winterwolves.network.GameController;
import winterwolves.network.ServerThread;
import winterwolves.utilidades.CameraManager;
import winterwolves.utilidades.Config;
import winterwolves.utilidades.PlayerManager;

public class MapaNieve implements Screen, GameController {

    private TiledMap mapa;
    private OrthogonalTiledMapRenderer renderer;
    private CameraManager cameraManager;
    private PlayerManager playerManager;

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private final float PPM = 100f;

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

        // Cámara
        cameraManager = new CameraManager(Config.WIDTH, Config.HEIGTH, PPM);

        // Mundo Box2D
        world = new World(new Vector2(0, 0), true);
        debugRenderer = new Box2DDebugRenderer();

        // Inicializar jugadores (PlayerManager)
        playerManager = new PlayerManager(world, personajesElegidosIdx, PPM, cameraManager.getHud());

        // Inicializar servidor
        serverThread = new ServerThread(this);
        serverThread.start();

        System.out.println("Servidor iniciado, esperando jugadores...");
    }

    @Override
    public void startGame() {

        partida = new Partida(
            playerManager.getJugador(0).getNombre(),
            playerManager.getJugador(0).getPersonaje(),
            playerManager.getJugador(1).getNombre(),
            playerManager.getJugador(1).getPersonaje(),
            120f
        );
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
        if (world != null) {
            world.step(delta, 6, 2);
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
    }
}
