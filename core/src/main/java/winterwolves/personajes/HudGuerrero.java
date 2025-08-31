package winterwolves.personajes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import winterwolves.elementos.Texto;
import winterwolves.utilidades.Config;
import winterwolves.utilidades.Recursos;

public class HudGuerrero {

    private Guerrero guerrero;
    private ShapeRenderer shapeRenderer;
    private Texto textoVida;
    private Texto salir;
    private OrthographicCamera camera;

    public HudGuerrero(Guerrero guerrero, OrthographicCamera camera) {
        this.guerrero = guerrero;
        this.camera = camera;

        shapeRenderer = new ShapeRenderer();

        textoVida = new Texto(Recursos.FUENTEMENU, 18, Color.WHITE, true);
        salir = new Texto(Recursos.FUENTEMENU, 25, Color.WHITE, true);
    }

    public void render(SpriteBatch batch) {
        shapeRenderer.setProjectionMatrix(camera.combined);

        float anchoBarra = 200;
        float altoBarra = 20;
        float margen = 20;

        // --- Barra de vida ---
        float vidaMax = 100f;
        float vidaActual = guerrero.getVida();
        float porcentajeVida = vidaActual / vidaMax;
        float anchoVida = anchoBarra * porcentajeVida;
        float xVida = camera.viewportWidth - margen - anchoBarra;
        float yVida = camera.viewportHeight - 40;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(xVida, yVida, anchoBarra, altoBarra);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(xVida, yVida, anchoVida, altoBarra);
        shapeRenderer.end();

        // --- Barra de cooldown de ataque ---
        float tiempoTranscurridoAtaque = guerrero.getTiempoDesdeUltimoGolpe();
        float porcentajeCooldownAtaque = Math.min(tiempoTranscurridoAtaque / guerrero.COOLDOWN_GOLPE, 1f);
        float anchoCooldownAtaque = anchoBarra * porcentajeCooldownAtaque;
        float yCooldownAtaque = yVida - 15;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(xVida, yCooldownAtaque, anchoBarra, 10);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(xVida, yCooldownAtaque, anchoCooldownAtaque, 10);
        shapeRenderer.end();

        // --- Barra de cooldown de dash ---
        float tiempoTranscurridoDash = guerrero.getTiempoDesdeUltimoDash();
        float porcentajeCooldownDash = Math.min(tiempoTranscurridoDash / guerrero.COOLDOWN_DASH, 1f);
        float anchoCooldownDash = anchoBarra * porcentajeCooldownDash;
        float yCooldownDash = yCooldownAtaque - 15;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(xVida, yCooldownDash, anchoBarra, 10);
        shapeRenderer.setColor(Color.CYAN);
        shapeRenderer.rect(xVida, yCooldownDash, anchoCooldownDash, 10);
        shapeRenderer.end();

        // --- Texto ---
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        textoVida.setTexto("Vida: " + (int) vidaActual);
        textoVida.setPosition(xVida + 5, yVida + 15);
        textoVida.dibujar();

        salir.setTexto("Presione ESC para volver al menu");
        salir.setColor(Color.BLACK);
        salir.setPosition(10, Config.HEIGTH - 20);
        salir.dibujar();
        batch.end();
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
