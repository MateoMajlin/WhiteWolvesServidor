package winterwolves.personajes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import winterwolves.elementos.Texto;
import winterwolves.personajes.habilidadesGuerrero.GolpeArma;
import winterwolves.utilidades.Config;
import winterwolves.utilidades.Recursos;

public class HudGuerrero {

    private Guerrero guerrero;
    private ShapeRenderer shapeRenderer;
    private Texto textoVida;
    private Texto salir;
    private OrthographicCamera camera;

    private static final float ANCHO_BARRA = 200;
    private static final float ALTO_BARRA_VIDA = 20;
    private static final float ALTO_BARRA_COOLDOWN = 10;
    private static final float MARGEN = 20;

    public HudGuerrero(Guerrero guerrero, OrthographicCamera camera) {
        this.guerrero = guerrero;
        this.camera = camera;

        shapeRenderer = new ShapeRenderer();
        textoVida = new Texto(Recursos.FUENTEMENU, 18, Color.WHITE, true);
        salir = new Texto(Recursos.FUENTEMENU, 25, Color.WHITE, true);
    }

    public void render(SpriteBatch batch) {
        shapeRenderer.setProjectionMatrix(camera.combined);

        float xBarra = camera.viewportWidth - MARGEN - ANCHO_BARRA;
        float yBarraVida = camera.viewportHeight - 40;

        dibujarBarraVida(xBarra, yBarraVida);
        float yCooldownAtaque = dibujarBarraCooldownAtaque(xBarra, yBarraVida - 15);
        float yCooldownDash = dibujarBarraCooldownDash(xBarra, yCooldownAtaque - 15);
        dibujarBarraCooldownHabilidad1(xBarra, yCooldownDash - 15);

        dibujarTextos(batch, xBarra, yBarraVida);
    }

    private void dibujarBarraVida(float x, float y) {
        float vidaMax = 100f;
        float vidaActual = guerrero.getVida();
        float porcentajeVida = vidaActual / vidaMax;
        float anchoVida = ANCHO_BARRA * porcentajeVida;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(x, y, ANCHO_BARRA, ALTO_BARRA_VIDA);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(x, y, anchoVida, ALTO_BARRA_VIDA);
        shapeRenderer.end();
    }

    private float dibujarBarraCooldownAtaque(float x, float y) {
        GolpeArma arma = guerrero.getArma();
        float tiempo = arma.getTiempoDesdeUltimoGolpe();
        float porcentaje = Math.min(tiempo / arma.getCooldown(), 1f);
        float ancho = ANCHO_BARRA * porcentaje;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(x, y, ANCHO_BARRA, ALTO_BARRA_COOLDOWN);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(x, y, ancho, ALTO_BARRA_COOLDOWN);
        shapeRenderer.end();

        return y;
    }

    private float dibujarBarraCooldownDash(float x, float y) {
        float tiempo = guerrero.getTiempoDesdeUltimoDash();
        float porcentaje = Math.min(tiempo / guerrero.COOLDOWN_DASH, 1f);
        float ancho = ANCHO_BARRA * porcentaje;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(x, y, ANCHO_BARRA, ALTO_BARRA_COOLDOWN);
        shapeRenderer.setColor(Color.CYAN);
        shapeRenderer.rect(x, y, ancho, ALTO_BARRA_COOLDOWN);
        shapeRenderer.end();

        return y;
    }

    private void dibujarBarraCooldownHabilidad1(float x, float y) {
        float tiempo = guerrero.getTiempoHabilidad1();
        float porcentaje = Math.min(tiempo / guerrero.getCooldownHabilidad1(), 1f);
        float ancho = ANCHO_BARRA * porcentaje;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(x, y, ANCHO_BARRA, ALTO_BARRA_COOLDOWN);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(x, y, ancho, ALTO_BARRA_COOLDOWN);
        shapeRenderer.end();
    }

    private void dibujarTextos(SpriteBatch batch, float xVida, float yVida) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        textoVida.setTexto("Vida: " + (int) guerrero.getVida());
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
