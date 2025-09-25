package winterwolves.personajes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import winterwolves.items.Inventario;
import winterwolves.items.Item;
import winterwolves.elementos.Texto;
import winterwolves.utilidades.Recursos;
import winterwolves.utilidades.Render;

public class InventarioHud {

    private Inventario inventario;
    private boolean visible = false;
    private int seleccionado = 0;

    private ShapeRenderer shapeRenderer;
    private Texto textoItem;
    private OrthographicCamera camera;

    private static final float ANCHO_CASILLA = 200;
    private static final float ALTO_CASILLA = 200;
    private static final float MARGEN = 10;

    public InventarioHud(Inventario inventario, OrthographicCamera camera) {
        this.inventario = inventario;
        this.camera = camera;

        shapeRenderer = new ShapeRenderer();
        textoItem = new Texto(Recursos.FUENTEMENU, 14, Color.WHITE, true); // puedes poner tu fuente
    }

    public void toggle() {
        visible = !visible;
    }

    public boolean isVisible() {
        return visible;
    }


    public void actualizar() {
        if (!visible || inventario.getItems().isEmpty()) return;

        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.RIGHT)) {
            seleccionado = (seleccionado + 1) % inventario.getItems().size();
        }
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.LEFT)) {
            seleccionado--;
            if (seleccionado < 0) seleccionado = inventario.getItems().size() - 1;
        }
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.UP)) {
            seleccionado -= 3; // asumiendo 3 columnas
            if (seleccionado < 0) seleccionado = 0;
        }
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.DOWN)) {
            seleccionado += 3; // asumiendo 3 columnas
            if (seleccionado >= inventario.getItems().size())
                seleccionado = inventario.getItems().size() - 1;
        }
    }

    public void dibujar(SpriteBatch batch) {
        if (!visible) return;

        int columnas = 3;
        int filas = (int) Math.ceil(inventario.getItems().size() / (float) columnas);

        float anchoTotal = columnas * ANCHO_CASILLA + (columnas - 1) * MARGEN;
        float altoTotal = filas * ALTO_CASILLA + (filas - 1) * MARGEN;

        // Centrado
        float startX = (camera.viewportWidth - anchoTotal) / 2f;
        float startY = (camera.viewportHeight + altoTotal) / 2f; // Y va desde arriba

        shapeRenderer.setProjectionMatrix(camera.combined);

        for (int i = 0; i < inventario.getItems().size(); i++) {
            Item item = inventario.getItem(i);
            float x = startX + (i % columnas) * (ANCHO_CASILLA + MARGEN);
            float y = startY - (i / columnas) * (ALTO_CASILLA + MARGEN);

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(i == seleccionado ? Color.ORANGE : Color.DARK_GRAY);
            shapeRenderer.rect(x, y, ANCHO_CASILLA, ALTO_CASILLA);
            shapeRenderer.end();

            if (item.getTextura() != null) {
                float margenInterno = 8;
                batch.begin();
                batch.draw(item.getTextura(),
                    x + margenInterno,
                    y + margenInterno + 12,
                    ANCHO_CASILLA - 2 * margenInterno,
                    ALTO_CASILLA - 2 * margenInterno - 12);
                batch.end();
            }

            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            textoItem.setTexto(item.getNombre());
            textoItem.setPosition(x + 5, y + ALTO_CASILLA - 5);
            textoItem.dibujar();
            batch.end();
        }
    }


    public Item getSeleccionado() {
        if (inventario.getItems().isEmpty()) return null;
        return inventario.getItem(seleccionado);
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
