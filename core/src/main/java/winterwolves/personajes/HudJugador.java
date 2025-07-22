package winterwolves.personajes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;

import winterwolves.elementos.Texto;
import winterwolves.personajes.Jugador;
import winterwolves.utilidades.Recursos;
import winterwolves.utilidades.Render;

public class HudJugador {

    private Jugador jugador;
    private ShapeRenderer shapeRenderer;
    private Texto textoVida;
    private Texto salir;
    private OrthographicCamera camera;

    public HudJugador(Jugador jugador, OrthographicCamera camera) {
        this.jugador = jugador;
        this.camera = camera;

        shapeRenderer = new ShapeRenderer();

        textoVida = new Texto(Recursos.FUENTEMENU, 18, Color.WHITE, true);
        salir = new Texto(Recursos.FUENTEMENU, 25, Color.WHITE, true);
    }

    public void render(SpriteBatch batch) {
        shapeRenderer.setProjectionMatrix(camera.combined);

        float vidaMax = 100f;
        float vidaActual = jugador.getVida();
        float anchoBarra = 200;
        float altoBarra = 20;
        float porcentajeVida = vidaActual / vidaMax;
        float anchoVida = anchoBarra * porcentajeVida;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(20, camera.viewportHeight - 40, anchoBarra, altoBarra);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(20, camera.viewportHeight - 40, anchoVida, altoBarra);
        shapeRenderer.end();

        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        textoVida.setTexto("Vida: " + (int) vidaActual);
        textoVida.setPosition(25, camera.viewportHeight - 25);
        textoVida.dibujar();
        salir.setTexto("Presione ESC para volver al menu");
        salir.setColor(Color.BLACK);
        salir.setPosition(25, camera.viewportHeight - 50);
        salir.dibujar();
        batch.end();
    }


    public void dispose() {
        shapeRenderer.dispose();
    }
}
