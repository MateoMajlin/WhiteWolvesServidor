package winterwolves;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import winterwolves.elementos.Texto;
import winterwolves.personajes.Hudeable;
import winterwolves.utilidades.Config;
import winterwolves.utilidades.Recursos;

public class HudJugador2 {

    private Hudeable personaje;
    private ShapeRenderer shapeRenderer;
    private Texto textoVida;
    private Texto indicacion;
    private OrthographicCamera camera;

    private static final float ANCHO_BARRA = 200;
    private static final float ALTO_BARRA_VIDA = 20;
    private static final float ALTO_BARRA_COOLDOWN = 10;
    private static final float MARGEN = 20;
    private static final float ESPACIO_BARRAS = 15;

    public HudJugador2(Hudeable personaje, OrthographicCamera camera) {
        this.personaje = personaje;
        this.camera = camera;

        shapeRenderer = new ShapeRenderer();
        textoVida = new Texto(Recursos.FUENTEMENU, 18, Color.WHITE, true);
        indicacion = new Texto(Recursos.FUENTEMENU, 20, Color.WHITE, true);
    }

    public void render(SpriteBatch batch) {
        shapeRenderer.setProjectionMatrix(camera.combined);

        float xBarra = camera.viewportWidth - MARGEN - ANCHO_BARRA;
        float yBarra = MARGEN; // esquina inferior derecha

        // Vida
        dibujarBarra(xBarra, yBarra, ANCHO_BARRA, ALTO_BARRA_VIDA, personaje.getVida() / 100f, Color.RED);

        float yActual = yBarra + ALTO_BARRA_VIDA + ESPACIO_BARRAS;

        // Cooldowns
        yActual = dibujarBarraCooldown(xBarra, yActual, Math.min(personaje.getArma().getCooldownProgreso(), 1f), Color.BLUE);
        yActual = dibujarBarraCooldown(xBarra, yActual + ESPACIO_BARRAS, Math.min(personaje.getTiempoDesdeUltimoDash() / personaje.getCooldownDash(), 1f), Color.CYAN);
        yActual = dibujarBarraCooldown(xBarra, yActual + ESPACIO_BARRAS, Math.min(personaje.getTiempoHabilidad1() / personaje.getCooldownHabilidad1(), 1f), Color.GREEN);
        yActual = dibujarBarraCooldown(xBarra, yActual + ESPACIO_BARRAS, Math.min(personaje.getTiempoHabilidad2() / personaje.getCooldownHabilidad2(), 1f), Color.YELLOW);

        dibujarTextos(batch, xBarra, yBarra);
    }

    private void dibujarBarra(float x, float y, float anchoTotal, float alto, float porcentaje, Color color) {
        float ancho = anchoTotal * porcentaje;
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(x, y, anchoTotal, alto);
        shapeRenderer.setColor(color);
        shapeRenderer.rect(x, y, ancho, alto);
        shapeRenderer.end();
    }

    private float dibujarBarraCooldown(float x, float y, float porcentaje, Color color) {
        dibujarBarra(x, y, ANCHO_BARRA, ALTO_BARRA_COOLDOWN, porcentaje, color);
        return y;
    }

    private void dibujarTextos(SpriteBatch batch, float xVida, float yVida) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        textoVida.setTexto("Vida: " + (int) personaje.getVida());
        textoVida.setPosition(xVida + 5, yVida + 25);
        textoVida.dibujar();

        indicacion.setTexto("Jugador 2");
        indicacion.setColor(Color.BLACK);
        indicacion.setPosition(xVida, yVida + 60);
        indicacion.dibujar();

        batch.end();
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
