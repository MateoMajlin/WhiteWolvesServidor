package winterwolves.personajes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import winterwolves.elementos.Texto;
import winterwolves.utilidades.Config;
import winterwolves.utilidades.Recursos;

public class Hud {

    private Hudeable personaje;
    private ShapeRenderer shapeRenderer;
    private Texto textoVida;
    private Texto salir;
    private OrthographicCamera camera;

    private static final float ANCHO_BARRA = 200;
    private static final float ALTO_BARRA_VIDA = 20;
    private static final float ALTO_BARRA_COOLDOWN = 10;
    private static final float MARGEN = 20;
    private static final float ESPACIO_BARRAS = 15;

    public Hud(Hudeable personaje, OrthographicCamera camera) {
        this.personaje = personaje;
        this.camera = camera;

        shapeRenderer = new ShapeRenderer();
        textoVida = new Texto(Recursos.FUENTEMENU, 18, Color.WHITE, true);
        salir = new Texto(Recursos.FUENTEMENU, 25, Color.WHITE, true);
    }

    public void render(SpriteBatch batch) {
        shapeRenderer.setProjectionMatrix(camera.combined);

        float xBarra = camera.viewportWidth - MARGEN - ANCHO_BARRA;
        float yBarra = camera.viewportHeight - 40;

        // Vida
        dibujarBarra(xBarra, yBarra, ANCHO_BARRA, ALTO_BARRA_VIDA, personaje.getVida() / 100f, Color.RED);

        float yActual = yBarra - ALTO_BARRA_VIDA - ESPACIO_BARRAS;

        // Cooldowns
        yActual = dibujarBarraCooldown(xBarra, yActual, personaje.getArma().getCooldownProgreso(), Color.BLUE);
        yActual = dibujarBarraCooldown(xBarra, yActual - ESPACIO_BARRAS, Math.min(personaje.getTiempoDesdeUltimoDash() / personaje.getCooldownDash(), 1f), Color.CYAN);
        yActual = dibujarBarraCooldown(xBarra, yActual - ESPACIO_BARRAS, Math.min(personaje.getTiempoHabilidad1() / personaje.getCooldownHabilidad1(), 1f), Color.GREEN);
        yActual = dibujarBarraCooldown(xBarra, yActual - ESPACIO_BARRAS, Math.min(personaje.getTiempoHabilidad2() / personaje.getCooldownHabilidad2(), 1f), Color.YELLOW);

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
        textoVida.setPosition(xVida + 5, yVida + 15);
        textoVida.dibujar();

        salir.setTexto("Presione ESC para volver al menú");
        salir.setColor(Color.BLACK);
        salir.setPosition(10, Config.HEIGTH - 20);
        salir.dibujar();

        batch.end();
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
