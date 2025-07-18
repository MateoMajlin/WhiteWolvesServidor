package winterwolves.personajes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

public class Jugador extends Sprite {

    private Vector2 movimiento = new Vector2();

    private float speed = 60 * 2, gravedad = 60 * 1.8f;

    private TiledMapTileLayer collisionLayer;

    public Jugador(Sprite sprite, TiledMapTileLayer collisionLayer) {
        super(sprite);
        this.collisionLayer = collisionLayer;
    }

    @Override
    public void draw(Batch batch) {
        update(Gdx.graphics.getDeltaTime());
        super.draw(batch);
    }

    public void update(float delta) {
        movimiento.y -= gravedad * delta;

        float oldX = getX(), oldY = getY();
        float tileWidth = collisionLayer.getTileWidth();
        float tileHeight = collisionLayer.getTileHeight();
        boolean collisionX = false, collisionY = false;

        // --- MOVIMIENTO EN X ---
        setX(getX() + movimiento.x * delta);

        if (movimiento.x < 0) {
            // izquierda
            collisionX = isCellBlocked(getX(), getY() + getHeight(), tileWidth, tileHeight) ||
                isCellBlocked(getX(), getY() + getHeight()/2, tileWidth, tileHeight) ||
                isCellBlocked(getX(), getY(), tileWidth, tileHeight);

        } else if (movimiento.x > 0) {
            // derecha
            collisionX = isCellBlocked(getX() + getWidth(), getY() + getHeight(), tileWidth, tileHeight) ||
                isCellBlocked(getX() + getWidth(), getY() + getHeight()/2, tileWidth, tileHeight) ||
                isCellBlocked(getX() + getWidth(), getY(), tileWidth, tileHeight);
        }

        if (collisionX) {
            setX(oldX);
            movimiento.x = 0;
        }

        // --- MOVIMIENTO EN Y ---
        setY(getY() + movimiento.y * delta);

        if (movimiento.y < 0) {
            // abajo
            collisionY = isCellBlocked(getX(), getY(), tileWidth, tileHeight) ||
                isCellBlocked(getX() + getWidth()/2, getY(), tileWidth, tileHeight) ||
                isCellBlocked(getX() + getWidth(), getY(), tileWidth, tileHeight);

        } else if (movimiento.y > 0) {
            // arriba
            collisionY = isCellBlocked(getX(), getY() + getHeight(), tileWidth, tileHeight) ||
                isCellBlocked(getX() + getWidth()/2, getY() + getHeight(), tileWidth, tileHeight) ||
                isCellBlocked(getX() + getWidth(), getY() + getHeight(), tileWidth, tileHeight);
        }

        if (collisionY) {
            setY(oldY);
            movimiento.y = 0;
        }
    }

    private boolean isCellBlocked(float x, float y, float tileWidth, float tileHeight) {
        int cellX = (int)(x / tileWidth);
        int cellY = (int)(y / tileHeight);

        TiledMapTileLayer.Cell cell = collisionLayer.getCell(cellX, cellY);
        return cell != null && cell.getTile().getProperties().containsKey("blocked");
    }

}
