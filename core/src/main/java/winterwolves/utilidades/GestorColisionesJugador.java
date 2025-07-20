package winterwolves.utilidades;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class GestorColisionesJugador {

    private TiledMapTileLayer collisionLayer;

    public GestorColisionesJugador(TiledMapTileLayer collisionLayer) {
        this.collisionLayer = collisionLayer;
    }

    public boolean collidesRight(float x, float y, float width, float height) {
        for (float step = 0; step < height; step += collisionLayer.getTileHeight() / 2f) {
            if (isCellBlocked(x + width, y + step)) return true;
        }
        return false;
    }

    public boolean collidesLeft(float x, float y, float width, float height) {
        for (float step = 0; step < height; step += collisionLayer.getTileHeight() / 2f) {
            if (isCellBlocked(x, y + step)) return true;
        }
        return false;
    }

    public boolean collidesTop(float x, float y, float width, float height) {
        for (float step = 0; step < width; step += collisionLayer.getTileWidth() / 2f) {
            if (isCellBlocked(x + step, y + height)) return true;
        }
        return false;
    }

    public boolean collidesBottom(float x, float y, float width, float height) {
        for (float step = 0; step < width; step += collisionLayer.getTileWidth() / 2f) {
            if (isCellBlocked(x + step, y)) return true;
        }
        return false;
    }

    private boolean isCellBlocked(float x, float y) {
        int cellX = (int) (x / collisionLayer.getTileWidth());
        int cellY = (int) (y / collisionLayer.getTileHeight());

        TiledMapTileLayer.Cell cell = collisionLayer.getCell(cellX, cellY);
        return cell != null && cell.getTile().getProperties().containsKey("blocked");
    }
}
