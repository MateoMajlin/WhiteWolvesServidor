package winterwolves.props;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import winterwolves.items.Item;
import winterwolves.items.Inventario;
import winterwolves.elementos.Imagen;
import winterwolves.elementos.Texto;
import winterwolves.personajes.Personaje;
import winterwolves.utilidades.Recursos;

public class CofreHud {

    private Inventario inventarioPersonaje;
    private Inventario inventarioCofre;
    private boolean visible = false;

    private Imagen fondoHud;
    private ShapeRenderer shapeRenderer;
    private Texto textoItem;
    private Texto textoNombre;
    private Texto textoEstadisticas;
    private OrthographicCamera cameraHud;

    private static final float ANCHO_CASILLA = 272;
    private static final float ALTO_CASILLA = 234;

    private CasillaInventario[] casillasPersonaje = new CasillaInventario[3];
    private CasillaInventario[] casillasCofre = new CasillaInventario[3];

    private Personaje personaje;

    public CofreHud(Inventario inventarioCofre, Personaje personaje, OrthographicCamera cameraHud) {
        this.inventarioCofre = inventarioCofre;
        this.inventarioPersonaje = personaje.getInventario();
        this.personaje = personaje;
        this.cameraHud = cameraHud;

        fondoHud = new Imagen("inventarioFondo.png");
        shapeRenderer = new ShapeRenderer();
        textoItem = new Texto(Recursos.FUENTEMENU, 14, Color.WHITE, true);
        textoNombre = new Texto(Recursos.FUENTEMENU,50,Color.BLACK,true);
        textoEstadisticas = new Texto(Recursos.FUENTEMENU,25,Color.WHITE,true);

        casillasPersonaje[0] = new CasillaInventario(84, 366, ANCHO_CASILLA, ALTO_CASILLA);
        casillasPersonaje[1] = new CasillaInventario(404, 366, ANCHO_CASILLA, ALTO_CASILLA);
        casillasPersonaje[2] = new CasillaInventario(724, 366, ANCHO_CASILLA, ALTO_CASILLA);

        casillasCofre[0] = new CasillaInventario(84, 100, ANCHO_CASILLA, ALTO_CASILLA);
        casillasCofre[1] = new CasillaInventario(404, 100, ANCHO_CASILLA, ALTO_CASILLA);
        casillasCofre[2] = new CasillaInventario(724, 100, ANCHO_CASILLA, ALTO_CASILLA);
    }

    public void toggle() {
        visible = !visible;
        personaje.setPuedeMoverse(!visible);
        if (visible) {
            inventarioPersonaje = personaje.getInventario();
        }
    }

    public boolean isVisible() {
        return visible;
    }

    public void dibujar(SpriteBatch batch) {
        if (!visible) return;

        batch.setProjectionMatrix(cameraHud.combined);

        // Fondo y stats
        fondoHud.setTransparencia(0.9f);
        batch.begin();
        fondoHud.dibujar();
        textoNombre.setTexto(personaje.getClase());
        textoNombre.setPosition(80, 680);
        textoNombre.dibujar();

        textoEstadisticas.setTexto(
                "ATQ: " + personaje.getAtaque() +
                " ATQ.M: " + personaje.getAtaqueMagico() +
                " DEF: " + personaje.getDefensa() +
                " PV: " + personaje.getVida()
        );
        textoEstadisticas.setPosition(400, 670);
        textoEstadisticas.dibujar();
        batch.end();

        for (int i = 0; i < 3; i++) dibujarCasilla(batch, casillasPersonaje[i], inventarioPersonaje.getItem(i));

        for (int i = 0; i < 3; i++) dibujarCasilla(batch, casillasCofre[i], inventarioCofre.getItem(i));
    }

    private void dibujarCasilla(SpriteBatch batch, CasillaInventario casilla, Item item) {
        if (item == null) return;

        shapeRenderer.setProjectionMatrix(cameraHud.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(casilla.x, casilla.y, casilla.ancho, casilla.alto);
        shapeRenderer.end();

        if (item.getTextura() != null) {
            float margenInterno = 8;
            batch.begin();
            batch.draw(item.getTextura(),
                casilla.x + margenInterno,
                casilla.y + margenInterno + 12,
                casilla.ancho - 2 * margenInterno,
                casilla.alto - 2 * margenInterno - 12
            );
            batch.end();
        }

        batch.begin();
        textoItem.setTexto(item.getNombre());
        textoItem.setPosition(casilla.x + 5, casilla.y + casilla.alto - 5);
        textoItem.dibujar();
        batch.end();
    }

    public void dispose() {
        shapeRenderer.dispose();
        fondoHud.dispose();
    }

    private static class CasillaInventario {
        float x, y, ancho, alto;
        CasillaInventario(float x, float y, float ancho, float alto) {
            this.x = x; this.y = y; this.ancho = ancho; this.alto = alto;
        }
    }
}
