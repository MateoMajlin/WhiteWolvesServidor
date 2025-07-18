package winterwolves.personajes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import winterwolves.io.EntradasJugador;

public class Jugador extends Sprite {

    private Vector2 movimiento = new Vector2();
    private float speed = 60 * 2, gravedad = 60 * 1.8f;
    private TiledMapTileLayer collisionLayer;

    private EntradasJugador entradas;

    public Jugador(Sprite sprite, TiledMapTileLayer collisionLayer, EntradasJugador entradas) {
        super(sprite);
        this.collisionLayer = collisionLayer;
        this.entradas = entradas;
    }

    @Override
    public void draw(Batch batch) {
        update(Gdx.graphics.getDeltaTime());
        super.draw(batch);
    }

    public void update(float delta) {

        float oldX = getX(), oldY = getY();
        boolean collisionX = false, collisionY = false;

        movimiento.set(0,0);

        if (entradas.isArriba()) movimiento.y = speed;
        if (entradas.isAbajo()) movimiento.y = -speed;
        if (entradas.isIzquierda()) movimiento.x = -speed;
        if (entradas.isDerecha()) movimiento.x = speed;

        if (movimiento.len() > 0) movimiento.nor().scl(speed); // esto evita que el personaje se mueva mas rapido en diagoonal


        // Movimiento en X
        setX(getX() + movimiento.x * delta);
        if (movimiento.x < 0) collisionX = collidesLeft();
        else if (movimiento.x > 0) collisionX = collidesRight();
        if (collisionX) {
            setX(oldX);
        }

        // Movimiento en Y
        setY(getY() + movimiento.y * delta);
        if (movimiento.y < 0) collisionY = collidesBottom();
        else if (movimiento.y > 0) collisionY = collidesTop();
        if (collisionY) {
            setY(oldY);
        }

        setX(getX() + movimiento.x * delta);

        // manejo de colisiones

        if (movimiento.x < 0) { // izquierda
            collisionX = collidesLeft();
        } else if (movimiento.x > 0) { // derecha
            collisionX = collidesRight();
        }

        if (collisionX) {
            setX(oldX);
            movimiento.x = 0;
        }

        setY(getY() + movimiento.y * delta);

        if (movimiento.y < 0) { // abajo
            collisionY = collidesBottom();
        } else if (movimiento.y > 0) { // arriba
            collisionY = collidesTop();
        }

        if (collisionY) {
            setY(oldY);
            movimiento.y = 0;
        }
    }

    public boolean collidesRight() {
        for (float step = 0; step < getHeight(); step += collisionLayer.getTileHeight() / 2f) {
            if (isCellBlocked(getX() + getWidth(), getY() + step))
                return true;
        }
        return false;
    }

    public boolean collidesLeft() {
        for (float step = 0; step < getHeight(); step += collisionLayer.getTileHeight() / 2f) {
            if (isCellBlocked(getX(), getY() + step))
                return true;
        }
        return false;
    }

    public boolean collidesTop() {
        for (float step = 0; step < getWidth(); step += collisionLayer.getTileWidth() / 2f) {
            if (isCellBlocked(getX() + step, getY() + getHeight()))
                return true;
        }
        return false;
    }

    public boolean collidesBottom() {
        for (float step = 0; step < getWidth(); step += collisionLayer.getTileWidth() / 2f) {
            if (isCellBlocked(getX() + step, getY()))
                return true;
        }
        return false;
    }

    private boolean isCellBlocked(float x, float y) {
        int cellX = (int) (x / collisionLayer.getTileWidth());
        int cellY = (int) (y / collisionLayer.getTileHeight());

        TiledMapTileLayer.Cell cell = collisionLayer.getCell(cellX, cellY);
        return cell != null && cell.getTile().getProperties().containsKey("blocked");
    }

    public Vector2 getMovimiento() {
        return movimiento;
    }
}
