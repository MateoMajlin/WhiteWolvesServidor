package winterwolves.personajes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import winterwolves.elementos.Imagen;
import winterwolves.items.Inventario;
import winterwolves.items.Item;
import winterwolves.elementos.Texto;
import winterwolves.utilidades.Recursos;

public class InventarioHud {

    private Inventario inventario;
    private boolean visible = false;
    private int seleccionado = 0;

    private Imagen fondoInventario;

    private ShapeRenderer shapeRenderer;
    private Texto textoItem;
    private Texto textoNombre;
    private Texto textoEstadisticas;
    private OrthographicCamera camera;

    // tamaño estándar de las casillas
    private static final float ANCHO_CASILLA = 272;
    private static final float ALTO_CASILLA = 234;

    // casillas independientes
    private CasillaInventario casilla1;
    private CasillaInventario casilla2;
    private CasillaInventario casilla3;

    public InventarioHud(Inventario inventario, OrthographicCamera camera) {
        this.inventario = inventario;
        this.camera = camera;

        fondoInventario = new Imagen("inventarioFondo.png");
        shapeRenderer = new ShapeRenderer();
        textoItem = new Texto(Recursos.FUENTEMENU, 14, Color.WHITE, true);
        textoNombre = new Texto(Recursos.FUENTEMENU,50,Color.BLACK,true);
        textoEstadisticas = new Texto(Recursos.FUENTEMENU,25,Color.WHITE,true);

        casilla1 = new CasillaInventario(84, 366, ANCHO_CASILLA, ALTO_CASILLA);
        casilla2 = new CasillaInventario(404, 366, ANCHO_CASILLA, ALTO_CASILLA);
        casilla3 = new CasillaInventario(724, 366, ANCHO_CASILLA, ALTO_CASILLA);
    }

    public void toggle() {
        visible = !visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public void actualizar() {
        if (!visible || inventario.getItems().isEmpty()) return;

        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            seleccionado = (seleccionado + 1) % 3;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            seleccionado--;
            if (seleccionado < 0) seleccionado = 2;
        }
    }

    public void dibujar(SpriteBatch batch, Personaje j) {
        if (!visible) return;

        textoNombre.setTexto(j.getClase());
        textoNombre.setPosition(80, 680);
        textoEstadisticas.setTexto(
                "ATQ: " + j.getAtaque() +
                " ATQ.M: " + j.getAtaqueMagico() +
                " DEF: " + j.getDefensa() +
                " PV: " + j.getVida()
        );
        textoEstadisticas.setPosition(400, 670);

        fondoInventario.setTransparencia(0.9f);
        batch.begin();
        fondoInventario.dibujar();
        textoNombre.dibujar();
        textoEstadisticas.dibujar();
        batch.end();

        // Dibujar casillas
        dibujarCasilla(batch, casilla1, j, 0);
        dibujarCasilla(batch, casilla2, j, 1);
        dibujarCasilla(batch, casilla3, j, 2);

        // Descripciones
        dibujarDescripcionItems(batch, j, 0, 84, 350);
        dibujarDescripcionItems(batch, j, 1, 404, 350);
        dibujarDescripcionItems(batch, j, 2, 724, 350);
    }

    private void dibujarCasilla(SpriteBatch batch, CasillaInventario casilla, Personaje j, int index) {
        Item item = j.getSlot(index);
        if (item == null) return;

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(index == seleccionado ? Color.ORANGE : Color.DARK_GRAY);
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

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        textoItem.setTexto(item.getNombre());
        textoItem.setPosition(casilla.x + 5, casilla.y + casilla.alto - 5);
        textoItem.dibujar();
        batch.end();
    }

    private void dibujarDescripcionItems(SpriteBatch batch, Personaje j, int index, int posX, int posY) {
        Item item = j.getSlot(index);
        if (item == null) return;

        if (index == seleccionado) {
            Texto textoDescripcion = new Texto(Recursos.FUENTEMENU, 14, Color.WHITE, true);
            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            textoDescripcion.setTexto(item.getDescripcion());
            textoDescripcion.setPosition(posX, posY);
            textoDescripcion.dibujar();
            batch.end();
        }
    }


    public Item getSeleccionado() {
        if (inventario.getItems().isEmpty()) return null;
        int index = Math.min(seleccionado, inventario.getItems().size() - 1);
        return inventario.getItem(index);
    }

    public void dispose() {
        shapeRenderer.dispose();
        fondoInventario.dispose();
    }

    private static class CasillaInventario {
        float x, y, ancho, alto;

        CasillaInventario(float x, float y, float ancho, float alto) {
            this.x = x;
            this.y = y;
            this.ancho = ancho;
            this.alto = alto;
        }
    }
}
